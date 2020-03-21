package com.amithkoujalgi.realmmanager.domain;

public class Realm {

	private String id, realm, enabled;

	public Realm( String id, String realm, String enabled )
	{
		this.id = id;
		this.realm = realm;
		this.enabled = enabled;
	}
}
