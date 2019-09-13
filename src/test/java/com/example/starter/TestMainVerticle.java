package com.example.starter;

import io.vertx.core.MultiMap;
import io.vertx.core.Promise; 
import io.vertx.core.Vertx;

import io.vertx.core.buffer.Buffer;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.multipart.MultipartForm;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.example.http.ConstantsServer;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

 
	@BeforeEach
	void init_serverVertx (Vertx vertx, VertxTestContext testContext) {
		Promise<String> dbVerticleDeployment = Promise.promise();
	    vertx.deployVerticle(new MainVerticle(), dbVerticleDeployment);
	    dbVerticleDeployment.future().setHandler(ar -> {
	      if (ar.succeeded()) {

	    	  testContext.completeNow();
	      } else {
	    	  testContext.failed();
	      }
	    });  
	}
  
  @Test
  @DisplayName("Visit root")
  void visit_root(Vertx vertx, VertxTestContext testContext) {
      System.out.println("Visit root \n");
	  WebClient client = WebClient.create(vertx);
	  client.get(ConstantsServer.PORT, "localhost", "/")
	    .as(BodyCodec.string())
	    .send(testContext.succeeding(response -> testContext.verify(() -> {
	      assertEquals(response.body(),"Hello from Vert.x!");
    	  testContext.completeNow();
	    })));
  }
  
  @Test
  @DisplayName("Visit post query param")
  void visit_root_post_query_param(Vertx vertx, VertxTestContext testContext) {
      System.out.println("Visit post query param \n");

	  WebClient client = WebClient.create(vertx);
	 	  
	  client.post(ConstantsServer.PORT,"localhost","/post_query_param").
	  addQueryParam("key","test_query")
	  .as(BodyCodec.jsonObject() )
	  .send(ar->{
		  	  if (ar.succeeded()) {
  	  	        HttpResponse<JsonObject> response = ar.result();
  	  	        assertEquals(response.body().getString("key"), "test_query");
  	  	        testContext.completeNow();
		      } else {
		        ar.cause().printStackTrace();
		      }
	  });
	
  }
  @Test
  @DisplayName("Visit event_bus")
  void visit_event_bus(Vertx vertx, VertxTestContext testContext) {
      System.out.println("Visit event bus \n");

	  WebClient client = WebClient.create(vertx);
	 	  
	  client.get(ConstantsServer.PORT,"localhost","/event_bus").
	  addQueryParam("key","test_query")
	  .as(BodyCodec.jsonObject() )
	  .send(ar->{
		  	  if (ar.succeeded()) {
  	  	        HttpResponse<JsonObject> response = ar.result();
  	  	        assertEquals(response.body().getString("key"), "test_query");
  	  	        testContext.completeNow();
		      } else {
		        ar.cause().printStackTrace();
		      }
	  });
	
  }
 


  @Test
  //@DisplayName("Visit post json")
  void visit_root_post_send_json(Vertx vertx, VertxTestContext testContext) {
      System.out.println("Visit post json \n");
	  WebClient client = WebClient.create(vertx);
	  JsonObject dataSend = new JsonObject();
	  dataSend.put("key", "Dale");
	  client.post(ConstantsServer.PORT, "localhost", "/post_json_param")
      .putHeader(ConstantsServer.H_CONTENT_TYPE,ConstantsServer.H_CONTENT_TYPE_JSON)
      .as(BodyCodec.jsonObject())
	  .sendJsonObject(dataSend, ar->{
		  if (ar.succeeded()) {
		        HttpResponse<JsonObject> response = ar.result();
  	  	        assertEquals(dataSend.getString("key"),response.body().getString("key"));
		        testContext.completeNow();
		      } else {
		        ar.cause().printStackTrace();
		      }
	  });
  }
  
  	@Test
	void visit_root_post_send_form(Vertx vertx, VertxTestContext testContext) {
  		
  		WebClient client = WebClient.create(vertx);
		MultiMap form = MultiMap.caseInsensitiveMultiMap();
		
		form.set("key", "Dale");
		  client.post(ConstantsServer.PORT, "localhost", "/post_form_param")
	      .as(BodyCodec.jsonObject())
	      .putHeader("content-type", "multipart/form-data")
    	  .sendForm(form, ar -> {
			   if (ar.succeeded()) {
			        HttpResponse<JsonObject> response = ar.result();
	  	  	        assertEquals(form.get("key"),response.body().getString("key"));
			        testContext.completeNow();
			      } else {
			        ar.cause().printStackTrace();
			      }
		   });
	   
	}
  
 
}
