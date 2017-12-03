package core;


import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;

public class Server extends AbstractVerticle {
	
	private static Logger LOG = LoggerFactory.getLogger(Server.class);
	
	@Override
	public void start(Future<Void> fut) throws Exception {
		
		try {
			
			WebServices.init(vertx, config());
					
		} catch(Exception e) {
			
			LOG.error("Error starting Server. MicroService: " + config().getString("microservice.name")+  " will shut down.");
			
			throw e;
		}
		
		Router router = Router.router(vertx);
				
		
		router.route("/").handler(r -> basicResponse(r));
		
		int port = config().getInteger("http.port");
		
		vertx.createHttpServer().requestHandler(router::accept)
		
		.listen(port , result -> {
			
			if(result.succeeded()) {
				
				LOG.info("MicroServer running at localhost:" + port);
				
				fut.complete();
				
			} else {
				
				fut.fail(result.cause());
				
			}
			
		});
		
		
	}

	private void basicResponse(RoutingContext routingContext) {
			
		routingContext.response().putHeader("content-type", "text/html").end("Hello from Vert.x! An Elliott Stone production.");
			
	}
	
	

		
	}

