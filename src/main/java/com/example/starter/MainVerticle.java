package com.example.starter;

import com.example.http.HttpServerVerticle;
import com.example.messenger.MessengerVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  
	  Promise<String> deployServer = Promise.promise();
   
	  vertx.deployVerticle(new HttpServerVerticle(), deployServer);
	  
	  deployServer.future().compose(id->{
		  Promise<String> deployMessenger = Promise.promise();
		  
		  vertx.deployVerticle(new MessengerVerticle(), deployMessenger);
	  
		  return deployMessenger.future();
	  }).setHandler(ar -> {   
	      if (ar.succeeded()) {
	    	  startPromise.complete();
	        } else {
	        	startPromise.fail(ar.cause());
	        }
	      });;
  }
}
