package com.amithkoujalgi.auth;

import com.amithkoujalgi.auth.domain.Client;
import com.amithkoujalgi.auth.domain.Realm;
import com.amithkoujalgi.auth.domain.User;
import com.amithkoujalgi.auth.domain.UserCreds;
import com.amithkoujalgi.auth.util.HTTPUtil;
import com.amithkoujalgi.auth.util.RealmConfigFileUtil;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CreateRealm {

	public static void main( String[] args ) throws Exception
	{
		createRealm("org2");
	}

	public static void createRealm( String realmName ) throws Exception
	{
		String keycloakServerEndpoint = getConfigProperty("keycloak.server.admin.url");
		String masterRealm = getConfigProperty("keycloak.server.admin.master-realm");
		String masterClientId = getConfigProperty("keycloak.server.admin.client-id");
		String masterUsername = getConfigProperty("keycloak.server.admin.username");
		String masterPassword = getConfigProperty("keycloak.server.admin.password");

		String tenantRootUrl = getConfigProperty("keycloak.custom-realm.root-url").replace("{realm-name}", realmName);
		String tenantUsername = getConfigProperty("keycloak.custom-realm.admin-user.username");
		String tenantUserPassword = getConfigProperty("keycloak.custom-realm.admin-user.password");
		String tenantUserFirstName = getConfigProperty("keycloak.custom-realm.admin-user.firstname");

		String ENDPOINT_CREATE_REALM = keycloakServerEndpoint + "/admin/realms/";
		String ENDPOINT_CREATE_USER = keycloakServerEndpoint + "/admin/realms/%s/users";
		String ENDPOINT_CREATE_CLIENT = keycloakServerEndpoint + "/admin/realms/%s/clients/";

		RealmConfigFileUtil.create(realmName);

		Keycloak kc = KeycloakBuilder.builder().serverUrl(keycloakServerEndpoint).realm(masterRealm)
				.username(masterUsername).password(masterPassword).clientId(masterClientId).grantType("password")
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();

		String accessToken = kc.tokenManager().getAccessTokenString();
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "bearer " + accessToken);

		// Create realm

		HTTPUtil.post(ENDPOINT_CREATE_REALM, new Realm(realmName, realmName, "true"), headers);

		// Create user

		User u = new User(tenantUsername, true, tenantUserFirstName);
		u.setCredentials(Arrays.asList(new UserCreds(false, tenantUserPassword)));

		HTTPUtil.post(String.format(ENDPOINT_CREATE_USER, realmName), u, headers);

		// Create client

		Client c = new Client(true, realmName + "-auth", tenantRootUrl, Arrays.asList(tenantRootUrl + "/*"));
		HTTPUtil.post(String.format(ENDPOINT_CREATE_CLIENT, realmName), c, headers);
	}

	private static String getConfigProperty( String key ) throws IOException
	{
		InputStream input = RealmAdd.class.getResourceAsStream("/application.properties");
		Properties prop = new Properties();
		prop.load(input);
		return prop.getProperty(key);
	}
}
