package com.amithkoujalgi.auth;

public class RealmAdd {

	public static void main( String[] args )
	{
		//https://www.n-k.de/2016/08/keycloak-admin-client.html
		//https://www.keycloak.org/docs/latest/securing_apps/index.html#java-adapters

		//		//Initialize the Keycloak client:
		//		Keycloak kc = KeycloakBuilder.builder().serverUrl("http://your.keycloak.domain/auth").realm("master")
		//				.username("admin").password("secret").clientId("admin-cli")
		//				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
		//
		//		//Create a new realm in Keycloak
		//		RealmRepresentation realm = new RealmRepresentation();
		//		realm.setRealm("demo");
		//		//		realm.set... // all the realm attributes to set
		//		kc.realms().create(realm);
		//
		//		//Create a new user in the created demo realm
		//		UserRepresentation user = new UserRepresentation();
		//		user.setUsername("johndoe");
		//		//		user.set... // all the user attributes to set
		//		kc.realm("demo").users().create(user);
	}
}
