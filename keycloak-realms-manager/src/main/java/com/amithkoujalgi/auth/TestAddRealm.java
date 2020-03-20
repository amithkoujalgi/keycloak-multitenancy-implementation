package com.amithkoujalgi.auth;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;

public class TestAddRealm {

	public static void main( String[] args ) throws Exception
	{
		String realmName = "test";

		//		// Initialize the Keycloak client
		//		Keycloak kc = KeycloakBuilder.builder().serverUrl("http://localhost:8080/auth").realm("master")
		//				.username("admin").password("admin").clientId("admin-cli")
		//				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
		//
		//		RealmRepresentation realm = new RealmRepresentation();
		//		realm.setId(realmName);
		//		realm.setRealm(realmName);
		//		//		realm.setEnabled(true);
		//		realm.setDisplayName(realmName);
		//
		//		kc.realms().create(realm);

		Keycloak k = Keycloak.getInstance("http://localhost:8080/auth", "master", "admin", "admin", "admin-cli");

		RealmRepresentation realm = new RealmRepresentation();
		realm.setId(realmName);
		realm.setRealm(realmName);
		realm.setEnabled(true);
		realm.setDisplayName(realmName);

		k.realms().create(realm);
	}
}