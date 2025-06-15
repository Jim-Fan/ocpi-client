package org.sdf.jimfan.ocpiclient.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

public class OcpiException extends Throwable {

	private static final long serialVersionUID = -3433881768723480472L;
	private final HttpStatusCode statusCode;
	private final HttpHeaders headers;
	private final String body;
	
	public OcpiException(HttpStatusCode statusCode, HttpHeaders headers, String body) {
		super();
		this.statusCode = statusCode;
		this.headers = headers;
		this.body = body;
	}

	public HttpStatusCode getStatusCode() {
		return statusCode;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}
}
