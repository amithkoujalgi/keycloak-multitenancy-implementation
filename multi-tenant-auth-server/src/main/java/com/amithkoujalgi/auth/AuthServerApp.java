package com.amithkoujalgi.auth;

import com.amithkoujalgi.realmmanager.RealmManager;
import org.keycloak.adapters.springboot.MultitenantConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Optional;

@SpringBootApplication
@ImportAutoConfiguration( MultitenantConfiguration.class )
//@EnableAutoConfiguration
public class AuthServerApp {

	public static String REALMS_DIRECTORY;

	public static void main( String[] args ) throws Exception
	{
		String tenants = Optional.ofNullable(System.getenv("TENANTS"))
				.orElseThrow(() -> new Exception("TENANTS env var not set!"));

		tenants = tenants.replace("\"", "");
		System.out.println(tenants);
		if( tenants.contains(",") )
		{
			String[] tenantNames = tenants.split(",");
			for( String tenant : tenantNames )
			{
				RealmManager.createRealm(tenant);
			}
		}
		else
		{
			String tenant = tenants;
			RealmManager.createRealm(tenant);
		}

		System.exit(0);

		REALMS_DIRECTORY = System.getProperty("user.home") + File.separator + "keycloak" + File.separator + "realms";
		File realmsDir = new File(REALMS_DIRECTORY);
		realmsDir.mkdirs();
		SpringApplication.run(AuthServerApp.class, args);
	}
}