package es.ulpgc.utils;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import es.ulpgc.datamart.DatamartStore;
import es.ulpgc.model.ArtistEvent;
import es.ulpgc.model.TicketmasterEvent;

public class EventSubscriber {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "spotify-events";
    private static final String TOPIC_NAME_TM = "ticketmaster-events";

    public static void subscribe() {
        Connection connection;
        Session session;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            connection = factory.createConnection();
            connection.setClientID("business-unit");
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topicSpotify = session.createTopic(TOPIC_NAME);
            MessageConsumer consumerSpotify = session.createDurableSubscriber(topicSpotify, "business-unit-subscriber-spotify");
            consumerSpotify.setMessageListener(message -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        String eventJson = textMessage.getText();
                        System.out.println("Received Spotify message: " + eventJson);

                        ArtistEvent event = ArtistEvent.deserialize(eventJson);
                        if (event != null) {
                            DatamartStore.saveArtistToDatamart(event);
                        } else {
                            System.err.println("Failed to deserialize ArtistEvent JSON: " + eventJson);
                        }
                    } catch (JMSException exception) {
                        System.err.println("Error processing Spotify message: " + exception.getMessage());
                    }
                }
            });

            Topic topicTM = session.createTopic(TOPIC_NAME_TM);
            MessageConsumer consumerTM = session.createDurableSubscriber(topicTM, "business-unit-subscriber-ticketmaster");
            consumerTM.setMessageListener(message -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        String eventJson = textMessage.getText();
                        System.out.println("Received Ticketmaster message: " + eventJson);

                        TicketmasterEvent event = TicketmasterEvent.deserialize(eventJson);
                        if (event != null) {
                            DatamartStore.saveEventToDatamart(event);
                        } else {
                            System.err.println("Failed to deserialize TicketmasterEvent JSON: " + eventJson);
                        }
                    } catch (JMSException exception) {
                        System.err.println("Error processing Ticketmaster message: " + exception.getMessage());
                    }
                }
            });

            System.out.println("Subscribed to topics: " + TOPIC_NAME + " and " + TOPIC_NAME_TM);
        } catch (JMSException exception) {
            System.err.println("Error subscribing to topics: " + exception.getMessage());
        }
    }
}