package com.cook.jms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer implements Runnable {


	ActiveMQConnectionFactory connectionFactory = null;

	public Producer(ActiveMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void run() {
		try {

			Connection connection = connectionFactory.createConnection();
			connection.start();


			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);


			Destination destination = session.createTopic("Date");


			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");	
			
			   Calendar cal = Calendar.getInstance();
			   


			String text = "Today is " + dateFormat.format(cal.getTime());
			TextMessage message = session.createTextMessage(text);
			
            System.out.println("Message Sent");
	
			producer.send(message);

			session.close();
			connection.close();
		} catch (JMSException jmse) {
			System.out.println("Exception: " + jmse.getMessage());
		}
	}

}
