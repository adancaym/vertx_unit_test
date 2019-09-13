package com.example.messenger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class MessengerVerticle extends AbstractVerticle{

	
	@Override
	public void start() throws Exception {
		
		System.out.println("Meesenger init");

		EventBus eb = vertx.eventBus();
		
		MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
		consumer.handler(message -> {
		  System.out.println("I have received a message: " + message.body());
		  message.reply(message.body());
		});
	}
	
}
