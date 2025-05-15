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

public class EventSubscriber {
    private static final String BROKER_URL = "tcp://localhost:61616"; // Update with your broker URL
    private static final String TOPIC_NAME = "spotify-events";
    private static final String EVENT_STORE_DIR = "eventstore";

    public static void subscribe() {
        Connection connection;
        Session session;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            connection = factory.createConnection();
            connection.setClientID("event-store-builder"); // Durable subscription
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(TOPIC_NAME);

            MessageConsumer consumer = session.createDurableSubscriber(topic, "event-store-subscriber");
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        String eventJson = textMessage.getText();
                        System.out.println("Received message: " + eventJson); // Debug log

                        // Deserialize the JSON into an ArtistEvent
                        ArtistEvent event = ArtistEvent.deserialize(eventJson);
                        if (event != null) {
                            storeEvent(event);
                        } else {
                            System.err.println("Failed to deserialize event JSON: " + eventJson);
                        }
                    } catch (JMSException e) {
                        System.err.println("Error processing message: " + e.getMessage());
                    }
                }
            });

            System.out.println("Subscribed to topic: " + TOPIC_NAME);
        } catch (JMSException e) {
            System.err.println("Error subscribing to topic: " + e.getMessage());
        }
    }

    private static void storeEvent(ArtistEvent event) {
        try {
            // Extract date from the event timestamp
            LocalDate date = event.getTs().toLocalDate();
            String formattedDate = date.format(DateTimeFormatter.BASIC_ISO_DATE);

            // Build directory structure
            String topicDir = EVENT_STORE_DIR + "/" + TOPIC_NAME;
            String filePath = topicDir + "/" + event.getSs() + "/" + formattedDate + ".events";

            // Ensure directories exist
            System.out.println("Creating directories: " + topicDir + "/" + event.getSs());
            Files.createDirectories(Paths.get(topicDir + "/" + event.getSs()));

            // Append event to file
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                System.out.println("Serialized event JSON: " + event.toJson());
                writer.write(event.toJson());
                writer.newLine();
                System.out.println("Event written to file: " + filePath); // Debug log
            }

            System.out.println("Event stored: " + filePath);
        } catch (IOException exception) {
            System.err.println("Error storing event: " + exception.getMessage());
        }
    }
}
