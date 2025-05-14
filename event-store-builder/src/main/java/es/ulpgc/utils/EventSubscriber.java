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

public class EventSubscriber {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "spotify-events";
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
            Topic topic = session.createTopic(TOPIC_NAME);

            MessageConsumer consumer = session.createDurableSubscriber(topic, "event-store-subscriber");
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        String eventJson = textMessage.getText();
                        storeEvent(eventJson);
                    } catch (JMSException exception) {
                        System.err.println("Error processing message: " + exception.getMessage());
                    }
                }
            });

            System.out.println("Subscribed to topic: " + TOPIC_NAME);
        } catch (JMSException exception) {
            System.err.println("Error subscribing to topic: " + exception.getMessage());
        }
    }

    private static void storeEvent(String eventJson) {
        try {
            LocalDate date = LocalDate.now();
            String formattedDate = date.format(DateTimeFormatter.BASIC_ISO_DATE);

            String topicDirectory = EVENT_STORE_DIR + "/" + TOPIC_NAME;
            String filePath = topicDirectory + "/spotify-feeder/" + formattedDate + ".events";

            Files.createDirectories(Paths.get(topicDirectory + "/spotify-feeder"));

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(eventJson);
                writer.newLine();
            }

            System.out.println("Event stored: " + filePath);
        } catch (IOException exception) {
            System.err.println("Error storing event: " + exception.getMessage());
        }
    }
}
