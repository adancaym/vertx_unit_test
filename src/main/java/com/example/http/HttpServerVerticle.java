package com.example.http;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;;

public class HttpServerVerticle extends AbstractVerticle{
	

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		
	   HttpServer server = vertx.createHttpServer();

	   
	   Router router = Router.router(vertx);

	   router.get("/").handler(this::indexHandler);
	   router.post("/post_query_param").handler(this::indexPOSTHandlerQueryParam);
	   router.post("/post_json_param").handler(this::indexPOSTHandlerJsonObject);
	   router.post("/post_form_param").handler(this::indexPOSTHandlerForm);
	   router.get("/event_bus").handler(this::useEventBus);
	   
	   server.requestHandler(router);
	   
	   server.listen(ConstantsServer.PORT, httpServerDeploy -> {
		      if (httpServerDeploy.succeeded()) 
		      {
			        startPromise.complete();
		      } else {
			        startPromise.fail(httpServerDeploy.cause());
		      }
	    });
	}
	
	 private void indexHandler(RoutingContext context) {
		 context
		 .response()
		 .putHeader(ConstantsServer.H_CONTENT_TYPE,ConstantsServer.H_CONTENT_TYPE_TEXT)
		 .end(
				 ConstantsServer
				 .getBodyIndex()
		 );
	 }

	 private void indexPOSTHandlerQueryParam(RoutingContext context) {
		 
		 String param = context.request().getParam("key");
		 JsonObject result = new JsonObject();
		 result.put("key", param);
		 
		 context
		 .response()
		 .putHeader(ConstantsServer.H_CONTENT_TYPE, ConstantsServer.H_CONTENT_TYPE_JSON)
		 .end(
				 result
				 .encodePrettily() 
		 );
		   
	 }
	 
	 private void indexPOSTHandlerJsonObject(RoutingContext context) {
		 
		context
		.request()
		.bodyHandler(
				buff->{
					context
					.response()
					.end(
							buff
							.toJsonObject()
							.encodePrettily()
						);
				}
		);
		 
	 }
	 

	 private void indexPOSTHandlerForm(RoutingContext context) {
		
		 context
		 .request()
		 .setExpectMultipart(true)
		 .endHandler(v -> {
			 String param = context.request().formAttributes().get("key");
			 JsonObject result = new JsonObject();
			 result.put("key", param);
			 context.request().response().end(result.encodePrettily());
	      });
	 }

	 private void useEventBus(RoutingContext context) {
		 
		 EventBus eventBus = vertx.eventBus();
		 
		 eventBus
		 .request(
				 "news.uk.sport", 
				 context.request().getParam("key"),
				 replay->{
				 	context
				 	.request()
				 	.response()
				 	.end(
				 			new JsonObject()
				 			.put(
				 					"key",
				 					replay.result().body()).encodePrettily()
			 			);
				 }
		 );
		 
		
		

	 }
}
