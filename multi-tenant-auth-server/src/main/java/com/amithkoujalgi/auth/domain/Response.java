package com.amithkoujalgi.auth.domain;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class Response<T> implements Serializable {

	private Object object;

	private T result;

	private OperationStatus status;

	private WebappException webappException;

	private String exception;

	private HttpStatus httpStatus;

	private Integer httpStatusCode;

	private String errorCode;

	public Response()
	{

	}

	public Response( T result )
	{
		this.result = result;
	}

	public Response( T result, OperationStatus status )
	{
		this.result = result;
		this.status = status;
	}

	public Response( Integer httpStatusCode )
	{
		this.httpStatusCode = httpStatusCode;
	}

	public Response( T result, OperationStatus status, HttpStatus httpStatus )
	{
		this.result = result;
		this.status = status;
		this.httpStatus = httpStatus;
	}

	public T getResult()
	{
		return result;
	}

	public void setResult( T result )
	{
		this.result = result;
	}

	public OperationStatus getStatus()
	{
		return status;
	}

	public void setStatus( OperationStatus status )
	{
		this.status = status;
	}

	public WebappException getWebappException()
	{
		return webappException;
	}

	public void setWebappException( WebappException webappException )
	{
		this.webappException = webappException;
	}

	public HttpStatus getHttpStatus()
	{
		return httpStatus;
	}

	public void setHttpStatus( HttpStatus httpStatus )
	{
		this.httpStatus = httpStatus;
	}

	public Integer getHttpStatusCode()
	{
		return httpStatusCode;
	}

	public void setHttpStatusCode( Integer httpStatusCode )
	{
		this.httpStatusCode = httpStatusCode;
	}

	public String getException()
	{
		return exception;
	}

	public void setException( String exception )
	{
		this.exception = exception;
	}

	public Object getObject()
	{
		return object;
	}

	public void setObject( Object object )
	{
		this.object = object;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode( String errorCode )
	{
		this.errorCode = errorCode;
	}
}
