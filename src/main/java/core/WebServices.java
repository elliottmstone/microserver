package core;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public final class WebServices {
	
	private static final Logger LOG = LoggerFactory.getLogger(WebServices.class);
	
	private static WebClient webClient;
	
	private WebServices() {
		
		throw new AssertionError("WebServices should not be instantiated.");
		
	}
	
	public static void init(Vertx vertx, JsonObject config) throws Exception {
			
			webClient = WebClient.create(vertx);
			
	}
	
	
	public static WebClient getWebClient() {
		
		if(webClient == null) {
			
			LOG.error("WebServices has not been initialized. Please call WebServices.init(vertx)");
			
		}
		
		return webClient;
		
	}
	
	public static Future<HttpResponse<Buffer>> get(String host, String uri, Map<String, String> queryParams) {
		
		Future<HttpResponse<Buffer>> responseFuture = Future.future();
		
		HttpRequest<Buffer> getRequest = webClient.get(host, uri);
		
		for(String queryParamName : queryParams.keySet()) {
			
			String paramValue = queryParams.get(queryParamName);
			
			getRequest.addQueryParam(queryParamName, paramValue);
		}
		
		getRequest.send(response -> {
			
			if(response.succeeded()) {
				
				responseFuture.complete(response.result());
				
			} else {
				
				LOG.error("GET request to " + uri + " failed", response.cause());
				
				responseFuture.fail(response.cause());
				
			}
			
		});
		
		return responseFuture;
		
	}
	

	public static Future<HttpResponse<Buffer>> get(int port, String host, String uri){
		
		return get(port, host, uri, new HashMap<>(), new HashMap<>());
		
	}
	
	
	public static Future<HttpResponse<Buffer>> get(int port, String host, String uri, Map<String, String> headers, Map<String, String> queryParameters) {
		
		Future<HttpResponse<Buffer>> responseFuture = Future.future();
		
		HttpRequest<Buffer> getRequest = webClient.get(port, host, uri);
			
		for(String queryParam: queryParameters.keySet()) {
			
			String paramValue = queryParameters.get(queryParam);
			
			getRequest.addQueryParam(queryParam, paramValue);
			
		}
		
		MultiMap requestHeaders = getRequest.headers();
		
		for(String headerName : headers.keySet()) {
			
			String headerValue = headers.get(headerName);
			
			requestHeaders.set(headerName, headerValue);
			
		}
		
			
	
		
		getRequest.send(request -> {
			
			if(request.succeeded()) {
				
				HttpResponse<Buffer> response = request.result();
				
				responseFuture.complete(response);
				
			} else {
				
				LOG.error("GET request to " + host + ":" + port + " failed.", request.cause());
				
				responseFuture.fail(request.cause());
				
			}
			
		});
		
		return responseFuture;
		
	}
	
	public static Future<HttpResponse<Buffer>> post(int port, String host, String uri, Map<String, String> headers){
		
		return post(port, host, uri, headers, null);
		
	}
	
	public static Future<HttpResponse<Buffer>> post(int port, String host, String uri, JsonObject body){
		
		return post(port, host, uri, new HashMap<>(), body);
		
	}
	
	public static Future<HttpResponse<Buffer>> post(int port, String host, String uri, Map<String, String> headers, JsonObject body){
		
		Future<HttpResponse<Buffer>> postFuture = Future.future();
		
		HttpRequest<Buffer> postRequest = webClient.post(port, host, uri);
		
		for(String headerName : headers.keySet()) {
			
			String headerValue = headers.get(headerName);
			
			postRequest.putHeader(headerName, headerValue);
			
		}
		
		
			
		
		
		if(body == null) {
			
			postRequest.send(request -> {
				
				if(request.succeeded()) {
					
					postFuture.complete(request.result());
					
				} else {
					
					LOG.error("POST request to " + host + ":" + port + " failed.", request.cause());
					
					postFuture.fail(request.cause());
					
				}
				
			});
			
			
		} else {
	
			postRequest.sendJsonObject(body, request -> {
				
				if(request.succeeded()) {
					
					postFuture.complete(request.result());
					
				} else {
					
					LOG.error("POST request to " + host + ":" + port + " failed.", request.cause());
					
					postFuture.fail(request.cause());
					
				}
				
			});
			
		}
		
		
		return postFuture;
		
	}
	
	
	
	
	
	
	

	
	
}
