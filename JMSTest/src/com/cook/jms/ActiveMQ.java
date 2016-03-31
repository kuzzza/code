package com.cook.jms;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQ {

	public static void main(String[] args) throws JMSException {
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");

		Thread topicConsumerThread = new Thread(new Consumer(
				connectionFactory));
		topicConsumerThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Thread topicProducerThread = new Thread(new Producer(
				connectionFactory));
		topicProducerThread.start();
		
	}

}
