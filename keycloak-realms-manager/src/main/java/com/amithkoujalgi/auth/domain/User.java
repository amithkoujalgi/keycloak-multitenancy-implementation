package com.amithkoujalgi.auth.domain;

import java.util.ArrayList;
import java.util.List;

public class User {

	private String username;

	private boolean enabled;

	private String firstName;

	private List<UserCreds> credentials = new ArrayList<>();

	public User( String username, boolean enabled, String firstName )
	{
		this.username = username;
		this.enabled = enabled;
		this.firstName = firstName;
	}

	public List<UserCreds> getCredentials()
	{
		return credentials;
	}

	public void setCredentials( List<UserCreds> credentials )
	{
		this.credentials = credentials;
	}
}
