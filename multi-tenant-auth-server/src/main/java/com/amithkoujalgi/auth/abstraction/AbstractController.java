package com.amithkoujalgi.auth.abstraction;

import com.amithkoujalgi.auth.domain.OperationStatus;
import com.amithkoujalgi.auth.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AbstractController {

	protected <T> ResponseEntity<Response<T>> buildResponse( T t )
	{
		final Response<T> appResponse = new Response<>();
		appResponse.setResult(t);
		appResponse.setStatus(OperationStatus.SUCCESS);
		appResponse.setHttpStatus(HttpStatus.OK);
		appResponse.setHttpStatusCode(appResponse.getHttpStatus().value());
		return new ResponseEntity<>(appResponse, HttpStatus.OK);
	}

	protected <T> ResponseEntity<Response<T>> buildErrorResponse( T t )
	{
		final Response<T> appResponse = new Response<>();
		appResponse.setResult(t);
		appResponse.setStatus(OperationStatus.ERROR);
		appResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
		appResponse.setHttpStatusCode(appResponse.getHttpStatus().value());
		return new ResponseEntity<>(appResponse, HttpStatus.OK);
	}
}
