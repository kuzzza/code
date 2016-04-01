package com.cooksys.jms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.jboss.logging.Logger;

public class Producer {
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageProducer producer;
	private InputStream in;

	private static Logger logger = Logger.getLogger(Producer.class);

	public void sendFile(String fileName) {
		logger.info("--sendFile start--");
		try {
			init(fileName);
			byte[] buffer = new byte[1024];
			int c = -1;
			while ((c = in.read(buffer)) > 0) {
				StreamMessage smsg = session.createStreamMessage();
				smsg.writeBytes(buffer, 0, c);
				producer.send(smsg);
				logger.info("send: " + c);
			}
			logger.info("--sendFile end--");
		} catch (Exception e) {
			logger.error("--sendFile fail--", e);
		} finally {
			close();
		}
	}

	private void init(String fileName) throws Exception {
		File file = new File(fileName);
		in = new FileInputStream(file);
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue("queue1");
		producer = session.createProducer(destination);
		// producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		connection.start();
	}

	private void close() {
		logger.info("--producer close start--");
		try {
			if (in != null) {
				in.close();
			}
			if (connection != null) {
				connection.close();
			}
			logger.info("--producer close end--");
		} catch (IOException e) {
			logger.error("--close InputStream fail--", e);
		} catch (JMSException e) {
			logger.error("--close connection fail--", e);
		}
		System.exit(0);
	}

	public static void main(String argv[]) {
		// ClassLoader loader = Producer.class.getClassLoader();
		String fileName = "c:\\Code\\file.pdf";
		new Producer().sendFile(fileName);
	}

}