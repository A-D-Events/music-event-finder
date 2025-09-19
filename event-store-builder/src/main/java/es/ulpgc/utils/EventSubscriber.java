package es.ulpgc.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import es.ulpgc.model.ArtistEvent;
import es.ulpgc.model.TicketmasterEvent;

public class EventSubscriber {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "spotify-events";
    private static final String TOPIC_NAME_TM = "ticketmaster-events";
    private static final String EVENT_STORE_DIR = "eventstore";

    public static void subscribe() {
        Connection connection;
        Session session;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            connection = factory.createConnection();
            connection.setClientID("event-store-builder");
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Suscriptor para spotify-events
            Topic topicSpotify = session.createTopic(TOPIC_NAME);
            MessageConsumer consumerSpotify = session.createDurableSubscriber(topicSpotify, "event-store-subscriber-spotify");
            consumerSpotify.setMessageListener(message -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        String eventJson = textMessage.getText();
                        System.out.println("Received Spotify message: " + eventJson);

                        ArtistEvent event = ArtistEvent.deserialize(eventJson);
                        if (event != null) {
                            storeArtistEvent(event);
                        } else {
                            System.err.println("Failed to deserialize ArtistEvent JSON: " + eventJson);
                        }
                    } catch (JMSException e) {
                        System.err.println("Error processing Spotify message: " + e.getMessage());
                    }
                }
            });

            // Suscriptor para ticketmaster-events
            Topic topicTM = session.createTopic(TOPIC_NAME_TM);
            MessageConsumer consumerTM = session.createDurableSubscriber(topicTM, "event-store-subscriber-ticketmaster");
            consumerTM.setMessageListener(message -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        String eventJson = textMessage.getText();
                        System.out.println("Received Ticketmaster message: " + eventJson);

                        TicketmasterEvent event = TicketmasterEvent.deserialize(eventJson);
                        if (event != null) {
                            storeTicketmasterEvent(event);
                        } else {
                            System.err.println("Failed to deserialize TicketmasterEvent JSON: " + eventJson);
                        }
                    } catch (JMSException e) {
                        System.err.println("Error processing Ticketmaster message: " + e.getMessage());
                    }
                }
            });

            System.out.println("Subscribed to topics: " + TOPIC_NAME + " and " + TOPIC_NAME_TM);
        } catch (JMSException e) {
            System.err.println("Error subscribing to topics: " + e.getMessage());
        }
    }

    private static void storeArtistEvent(ArtistEvent event) {
        try {
            LocalDate date = event.getTs().toLocalDate();
            String formattedDate = date.format(DateTimeFormatter.BASIC_ISO_DATE);

            String topicDir = EVENT_STORE_DIR + "/" + TOPIC_NAME;
            String filePath = topicDir + "/" + event.getSs() + "/" + formattedDate + ".events";

            Files.createDirectories(Paths.get(topicDir + "/" + event.getSs()));

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(event.toJson());
                writer.newLine();
            }

            System.out.println("ArtistEvent stored: " + filePath);
        } catch (IOException exception) {
            System.err.println("Error storing ArtistEvent: " + exception.getMessage());
        }
    }

    private static void storeTicketmasterEvent(TicketmasterEvent event) {
        try {
            LocalDate date = event.getTs().toLocalDate();
            String formattedDate = date.format(DateTimeFormatter.BASIC_ISO_DATE);

            String topicDir = EVENT_STORE_DIR + "/" + TOPIC_NAME_TM;
            String filePath = topicDir + "/" + event.getSs() + "/" + formattedDate + ".events";

            Files.createDirectories(Paths.get(topicDir + "/" + event.getSs()));

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(event.toJson());
                writer.newLine();
            }

            System.out.println("TicketmasterEvent stored: " + filePath);
        } catch (IOException exception) {
            System.err.println("Error storing TicketmasterEvent: " + exception.getMessage());
        }
    }
}