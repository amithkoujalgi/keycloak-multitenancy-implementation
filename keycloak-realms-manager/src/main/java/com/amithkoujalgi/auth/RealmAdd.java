package com.amithkoujalgi.auth;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class RealmAdd {

	public static void main( String[] args ) throws Exception
	{
		//https://www.n-k.de/2016/08/keycloak-admin-client.html
		//https://www.keycloak.org/docs/latest/securing_apps/index.html#java-adapters

		createRealm("test1");
		createRealm("test2");
	}

	public static Properties getConfig() throws IOException
	{
		InputStream input = RealmAdd.class.getResourceAsStream("/application.properties");
		Properties prop = new Properties();
		prop.load(input);
		return prop;
	}

	public static String getConfigProperty( String key ) throws IOException
	{
		return getConfig().getProperty(key);
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

			// Create auth server JSON config for a Keycloak realm
			File newRealm = new File(REALMS_DIRECTORY + File.separator + realmName + "-keycloak.json");
			FileWriter fw = new FileWriter(newRealm);
			fw.write(template);
			fw.close();
			System.out.println(String.format("Auth server config file for realm %s written to %s.", realmName,
					newRealm.getAbsolutePath()));

			// Initialize the Keycloak client
			Keycloak kc = KeycloakBuilder.builder().serverUrl(getConfigProperty("keycloak.server.admin.url"))
					.realm(getConfigProperty("keycloak.server.admin.master-realm"))
					.username(getConfigProperty("keycloak.server.admin.username"))
					.password(getConfigProperty("keycloak.server.admin.password"))
					.clientId(getConfigProperty("keycloak.server.admin.client-id"))
					.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();

			// Create a new realm in Keycloak
			RealmRepresentation realm = new RealmRepresentation();
			realm.setRealm(realmName);

			// Create a new OpenID client in Keycloak realm
			String clientRootURL = getConfigProperty("keycloak.custom-realm.root-url")
					.replace("{realm-name}", realmName);

			ClientRepresentation client = new ClientRepresentation();
			client.setClientId(realmName + "-auth");
			client.setRedirectUris(Arrays.asList(new String[] { clientRootURL + "/*" }));
			client.setRootUrl(clientRootURL);

			realm.setClients(Arrays.asList(new ClientRepresentation[] { client }));
			realm.setEnabled(true);
			kc.realms().create(realm);

			System.out.println(String.format("Keycloak realm %s created.", realmName));

			//Create a new user in the created realm
			UserRepresentation user = new UserRepresentation();
			user.setUsername("admin");
			user.setEmailVerified(true);
			user.setEnabled(true);

			CredentialRepresentation creds = new CredentialRepresentation();
			creds.setTemporary(true);
			creds.setValue("admin");
			user.setCredentials(Arrays.asList(new CredentialRepresentation[] { creds }));
			kc.realm(realmName).users().create(user);
			System.out.println(String.format("Admin user for Keycloak realm %s created.", realmName));
		}
	}
}
