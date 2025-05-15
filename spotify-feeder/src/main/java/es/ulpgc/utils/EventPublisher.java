package es.ulpgc.utils;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class EventPublisher {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "spotify-events";

    public static void publishEvent(String eventJson) {
        Connection connection = null;
        Session session = null;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(TOPIC_NAME);

            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage(eventJson);

            producer.send(message);
            System.out.println("Event published to topic: " + TOPIC_NAME);

        } catch (JMSException exception) {
            System.err.println("Error publishing event: " + exception.getMessage());
        } finally {
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException exception) {
                System.err.println("Error closing resources: " + exception.getMessage());
            }
        }
    }
}
