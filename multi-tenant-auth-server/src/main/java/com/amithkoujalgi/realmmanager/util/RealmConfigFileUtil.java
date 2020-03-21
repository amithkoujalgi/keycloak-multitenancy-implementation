package com.amithkoujalgi.realmmanager.util;

import java.io.*;

public class RealmConfigFileUtil {

	private static String realmsDir =
			System.getProperty("user.home") + File.separator + "keycloak" + File.separator + "realms";

	public static void create( String realmName ) throws Exception
	{
		File dir = new File(realmsDir);
		if( !dir.exists() )
		{
			dir.mkdirs();
		}
		File existingRealm = new File(realmsDir + File.separator + realmName + "-keycloak.json");
		if( existingRealm.exists() )
		{
			throw new Exception(String.format("Realm '%s' already exists.", realmName));
		}
		else
		{
			String template = getTemplate();
			template = template.replace("{realm-name}", realmName);

			// Create auth server JSON config for a Keycloak realm
			File newRealm = new File(realmsDir + File.separator + realmName + "-keycloak.json");
			FileWriter fw = new FileWriter(newRealm);
			fw.write(template);
			fw.close();
			System.out.println(String.format("Auth server config file for realm %s written to %s.", realmName,
					newRealm.getAbsolutePath()));
		}
	}

	public static void delete( String realmName ) throws IOException
	{
		File f = new File(realmsDir + File.separator + realmName + "-keycloak.json");
		if( f.exists() )
		{
			f.delete();
		}
		System.out.println(String.format("Auth server config file for realm %s deleted.", realmName));
	}

	private static String getTemplate() throws IOException
	{
		InputStream is = RealmConfigFileUtil.class.getResourceAsStream("/keycloak-auth-server-config-template.json");
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
}
