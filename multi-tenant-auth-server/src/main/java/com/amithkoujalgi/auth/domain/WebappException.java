package com.amithkoujalgi.auth.domain;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public class WebappException extends Exception {

	private static final String APPLICATION_ERROR = "app.error";

	private final HttpStatus errorCode;

	private final String message;

	private final Object details;

	private final String customErrorCode;

	public WebappException( String message )
	{
		this.customErrorCode = message;
		this.message = getMessageFromBundle(message);
		this.details = message;
		this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public WebappException( Throwable exception )
	{
		super(exception);
		this.customErrorCode = APPLICATION_ERROR;
		this.message = getMessageFromBundle(exception.getMessage());
		this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
		this.details = message;
	}

	public WebappException( String message, Object details )
	{
		this.customErrorCode = message;
		this.details = details;
		this.message = getMessageFromBundle(message);
		this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public WebappException( String msg, Throwable cause )
	{
		super(cause);
		this.customErrorCode = msg;
		this.message = getMessageFromBundle(msg);
		this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
		this.details = msg;
	}

	public WebappException( String message, HttpStatus code )
	{
		this.errorCode = code;
		this.customErrorCode = message;
		this.message = getMessageFromBundle(message);
		this.details = message;
	}

	public WebappException( String message, Object details, HttpStatus code )
	{
		this.customErrorCode = message;
		this.details = details;
		this.message = message;
		this.errorCode = code;
	}

	public WebappException( String message, Object[] messagePlaceholders )
	{
		this.customErrorCode = message;
		this.message = MessageFormat.format(getMessageFromBundle(message), messagePlaceholders);
		this.details = message;
		this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public WebappException( String message, Object[] messagePlaceholders, HttpStatus code )
	{
		this.errorCode = code;
		this.customErrorCode = message;
		this.message = MessageFormat.format(getMessageFromBundle(message), messagePlaceholders);
		this.details = message;
	}

	private String getMessageFromBundle( String message )
	{
		try
		{
			return message;
		}
		catch( Exception e )
		{
			// do nothing
		}
		return message;
	}

	public Object getDetails()
	{
		return details;
	}

	public HttpStatus getErrorCode()
	{
		return errorCode;
	}

	@Override
	public String getMessage()
	{
		return message;
	}

	@Override
	public String toString()
	{
		return "WebappException{" + "errorCode=" + errorCode + ", messagebundle='" + message + '\'' + ", details="
				+ details + '}';
	}

	public String getCustomErrorCode()
	{
		return customErrorCode;
	}
}
