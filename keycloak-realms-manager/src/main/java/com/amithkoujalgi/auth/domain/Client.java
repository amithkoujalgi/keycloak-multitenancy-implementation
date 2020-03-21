package com.amithkoujalgi.auth.domain;

import java.util.List;

public class Client {

	private boolean publicClient;

	private String clientId;

	private String rootUrl;

	private List<String> redirectUris;

	public Client( boolean publicClient, String clientId, String rootUrl, List<String> redirectUris )
	{
		this.publicClient = publicClient;
		this.clientId = clientId;
		this.rootUrl = rootUrl;
		this.redirectUris = redirectUris;
	}
}
