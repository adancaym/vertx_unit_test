package com.example.http;

public class ConstantsServer {

	//PORT BASE
	public static final int PORT = 8080;
	
	
	
	public static final String H_CONTENT_TYPE= "content-type";
	public static final String H_CONTENT_TYPE_TEXT= "text/plain";
	public static final String H_CONTENT_TYPE_JSON= "application/json";
	
	public static String getBodyIndex() {
		return "Hello from Vert.x!";
	}
	
}
