package com.example.starter;

import com.example.http.HttpServerVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  
	  Promise<String> deployServer = Promise.promise();
   
	  vertx.deployVerticle(new HttpServerVerticle(), deployServer);
	  
	  deployServer.future().setHandler(ar -> {
	      if (ar.succeeded()) {
	    	  startPromise.complete();
	        } else {
	        	startPromise.fail(ar.cause());
	        }
        });
	  
  }
}
