package com.amithkoujalgi.auth.model;

import org.keycloak.representations.IDToken;

public class Token {

	private String token;

	private IDToken idToken;

	public Token( String token, IDToken idToken )
	{
		this.token = token;
		this.idToken = idToken;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken( String token )
	{
		this.token = token;
	}

	public IDToken getIdToken()
	{
		return idToken;
	}

	public void setIdToken( IDToken idToken )
	{
		this.idToken = idToken;
	}
}
