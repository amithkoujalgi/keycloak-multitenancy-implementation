package com.amithkoujalgi.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import java.io.IOException;
import java.util.*;

public class TestAddRealm {

	private static String KEYCLOAK_SERVER_ENDPOINT = "http://localhost:8080/auth";

	private static String ENDPOINT_CREATE_REALM = KEYCLOAK_SERVER_ENDPOINT + "/admin/realms/";

	private static String ENDPOINT_CREATE_USER = KEYCLOAK_SERVER_ENDPOINT + "/admin/realms/%s/users";

	private static String ENDPOINT_CREATE_CLIENT = KEYCLOAK_SERVER_ENDPOINT + "/admin/realms/%s/clients/";

	public static void main( String[] args ) throws Exception
	{
		String realmName = "test";

		String clientId = "admin-cli";
		String username = "admin";
		String password = "admin";
		String masterRealm = "master";

		Keycloak kc = KeycloakBuilder.builder().serverUrl(KEYCLOAK_SERVER_ENDPOINT).realm(masterRealm)
				.username(username).password(password).clientId(clientId).grantType("password")
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();

		String accessToken = kc.tokenManager().getAccessTokenString();

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "bearer " + accessToken);

		// Create realm

		//		post(ENDPOINT_CREATE_REALM, new Realm(realmName, realmName, "true"), headers);

		// Create user

		String tenantUsername = "admin";
		String tenantUserPassword = "admin";

		String tenantUserFirstName = "Administrator";

		User u = new User(tenantUsername, true, tenantUserFirstName);
		u.setCredentials(Arrays.asList(new UserCreds(false, tenantUserPassword)));

		//		post(String.format(ENDPOINT_CREATE_USER, realmName), u, headers);

		// Create client

		String clientName = realmName + "-auth";
		String clientRootUrl = "http://" + realmName + ".localhost:9090";
		Client c = new Client(true, clientName, clientRootUrl, Arrays.asList(clientRootUrl + "/*"));
		post(String.format(ENDPOINT_CREATE_CLIENT, realmName), c, headers);

	}

	public static void post( String url, Object body, Map<String, String> headers ) throws IOException
	{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		String json = new Gson().toJson(body);
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		for( Map.Entry e : headers.entrySet() )
		{
			httpPost.setHeader((String) e.getKey(), (String) e.getValue());
		}

		CloseableHttpResponse response = client.execute(httpPost);
		System.out.println(response.toString());
		client.close();
	}

	public static void toJSON( Object o )
	{
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(o));
	}
}

class Realm {

	private String id, realm, enabled;

	public Realm( String id, String realm, String enabled )
	{
		this.id = id;
		this.realm = realm;
		this.enabled = enabled;
	}
}

class User {

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

class UserCreds {

	private boolean temporary;

	private String value, type = "password";

	public UserCreds( boolean temporary, String value )
	{
		this.temporary = temporary;
		this.value = value;
	}
}

class Client {

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