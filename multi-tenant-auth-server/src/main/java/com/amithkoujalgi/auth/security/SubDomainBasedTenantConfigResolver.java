package com.amithkoujalgi.auth.security;

import com.amithkoujalgi.auth.AuthServerApp;
import org.keycloak.adapters.*;
import org.keycloak.adapters.springsecurity.token.SpringSecurityTokenStore;
import org.keycloak.representations.adapters.config.AdapterConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubDomainBasedTenantConfigResolver implements KeycloakConfigResolver {

	private static AdapterConfig adapterConfig;

	private final Map<String, KeycloakDeployment> cache = new ConcurrentHashMap<String, KeycloakDeployment>();

	public static void setAdapterConfig( AdapterConfig adapterConfig )
	{
		SubDomainBasedTenantConfigResolver.adapterConfig = adapterConfig;
	}

	public static String getSubdomain( String requestURI )
	{
		String url = null;
		try
		{
			String domain = new URI(requestURI).getHost();
			url = domain.startsWith("www.") ? domain.substring(4) : domain;
		}
		catch( URISyntaxException e )
		{
			e.printStackTrace();
		}
		// If user is registering a new tenant, by-pass the subdomain check and let the user register a tenant

		//split the URL by dot. i.e., extract the subdomain. For example, xxx is the subdomain entity in the URL xxx.app.com
		String subdomain = url.split("\\.")[0];
		return subdomain;
	}

	@Override
	public KeycloakDeployment resolve( OIDCHttpFacade.Request request )
	{
		String subdomain = getSubdomain(request.getURI());
		KeycloakDeployment deployment = cache.get(subdomain);
		if( null == deployment )
		{
			// if not found on the cache, try to load it from the file system
			try
			{
				FileInputStream is = new FileInputStream(
						AuthServerApp.REALMS_DIRECTORY + File.separator + subdomain + "-keycloak.json");
				if( is == null )
				{
					throw new IllegalStateException("Not able to find the file /" + subdomain + "-keycloak.json");
				}
				deployment = KeycloakDeploymentBuilder.build(is);
				cache.put(subdomain, deployment);
			}
			catch( FileNotFoundException e )
			{
				//				e.printStackTrace();
				System.out.println("[ERROR]: Invalid tenant '" + subdomain + "' or tenant config not found!");
			}
		}
		return deployment;
	}

	public AdapterTokenStore createAdapterTokenStore( KeycloakDeployment deployment, HttpServletRequest request )
	{
		return new SpringSecurityTokenStore(deployment, request);
	}
}