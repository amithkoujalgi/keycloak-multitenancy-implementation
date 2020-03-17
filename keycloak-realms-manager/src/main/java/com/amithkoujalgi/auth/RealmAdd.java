package com.amithkoujalgi.auth;

import java.io.*;

public class RealmAdd {

	public static void main( String[] args ) throws Exception
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
		createRealm("master");
	}

	public static String getTemplate() throws IOException
	{
		InputStream is = RealmAdd.class.getResourceAsStream("/keycloak-auth-server-config-template.json");
		if( is == null )
		{
			throw new IllegalStateException("Not able to find the template file");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder templateString = new StringBuilder();
		String line;
		while( (line = reader.readLine()) != null )
		{
			templateString.append(line);
		}
		reader.close();
		return templateString.toString();
	}

	public static void createRealm( String realmName ) throws Exception
	{
		String REALMS_DIRECTORY =
				System.getProperty("user.home") + File.separator + "keycloak" + File.separator + "realms";

		File dir = new File(REALMS_DIRECTORY);
		if( !dir.exists() )
		{
			dir.mkdirs();
		}

		File existingRealm = new File(REALMS_DIRECTORY + File.separator + realmName + "-keycloak.json");
		if( existingRealm.exists() )
		{
			throw new Exception(String.format("Realm '%s' already exists.", realmName));
		}
		else
		{
			String template = getTemplate();
			template = template.replace("{realm-name}", realmName);

			File newRealm = new File(REALMS_DIRECTORY + File.separator + realmName + "-keycloak.json");
			FileWriter fw = new FileWriter(newRealm);
			fw.write(template);
			fw.close();
			System.out.println(String.format("Auth server config file for realm %s written to %s.", realmName,
					newRealm.getAbsolutePath()));
		}
	}
}
