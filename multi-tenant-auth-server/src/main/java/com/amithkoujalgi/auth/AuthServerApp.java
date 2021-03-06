package com.amithkoujalgi.auth;

import com.amithkoujalgi.realmmanager.RealmManager;
import org.keycloak.adapters.springboot.MultitenantConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
@ImportAutoConfiguration( MultitenantConfiguration.class )
//@EnableAutoConfiguration
public class AuthServerApp {

	public static String REALMS_DIRECTORY;

	public static void main( String[] args ) throws Exception
	{
		REALMS_DIRECTORY = System.getProperty("user.home") + File.separator + "keycloak" + File.separator + "realms";
		File realmsDir = new File(REALMS_DIRECTORY);
		realmsDir.mkdirs();

		SpringApplication.run(AuthServerApp.class, args);
	}

	@Bean
	public RealmManager realmManager()
	{
		return new RealmManager();
	}
}