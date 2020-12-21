package com.amithkoujalgi.realmmanager;
//import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.KeycloakBuilder;

import com.amithkoujalgi.realmmanager.domain.Client;
import com.amithkoujalgi.realmmanager.domain.Realm;
import com.amithkoujalgi.realmmanager.domain.User;
import com.amithkoujalgi.realmmanager.domain.UserCreds;
import com.amithkoujalgi.realmmanager.util.HTTPUtil;
import com.amithkoujalgi.realmmanager.util.RealmConfigFileUtil;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class RealmManager {

	@Value( "${app.keycloak.server.admin.url}" )
	private String keycloakServerEndpoint;

	@Value( "${app.keycloak.server.admin.master-realm}" )
	private String masterRealm;

	@Value( "${app.keycloak.server.admin.client-id}" )
	private String masterClientId;

	@Value( "${app.keycloak.server.admin.username}" )
	private String masterUsername;

	@Value( "${app.keycloak.server.admin.password}" )
	private String masterPassword;

	@Value( "${app.keycloak.custom-realm.root-url}" )
	private String tenantRootUrl;

	@Value( "${app.keycloak.custom-realm.admin-user.username}" )
	private String tenantUsername;

	@Value( "${app.keycloak.custom-realm.admin-user.password}" )
	private String tenantUserPassword;

	@Value( "${app.keycloak.custom-realm.admin-user.firstname}" )
	private String tenantUserFirstName;

	@Value( "${app.keycloak.server.auth-domain}" )
	private String keycloakAuthDomain;

	public void createRealm( String realmName ) throws Exception
	{

		String ENDPOINT_CREATE_REALM = keycloakServerEndpoint + "/admin/realms/";
		String ENDPOINT_CREATE_USER = keycloakServerEndpoint + "/admin/realms/%s/users";
		String ENDPOINT_CREATE_CLIENT = keycloakServerEndpoint + "/admin/realms/%s/clients/";

		RealmConfigFileUtil.create(realmName, keycloakAuthDomain);

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

		Client c = new Client(true, realmName + "-auth", tenantRootUrl.replace("{realm-name}", realmName),
				Arrays.asList(tenantRootUrl.replace("{realm-name}", realmName) + "/*"));
		HTTPUtil.post(String.format(ENDPOINT_CREATE_CLIENT, realmName), c, headers);
	}

	public void deleteRealm( String realmName ) throws Exception
	{
		RealmConfigFileUtil.delete(realmName);
	}
}
