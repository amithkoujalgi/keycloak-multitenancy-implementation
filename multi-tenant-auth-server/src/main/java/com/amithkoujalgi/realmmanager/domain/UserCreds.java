package com.amithkoujalgi.realmmanager.domain;

public class UserCreds {

	private boolean temporary;

	private String value, type = "password";

	public UserCreds( boolean temporary, String value )
	{
		this.temporary = temporary;
		this.value = value;
	}
}
