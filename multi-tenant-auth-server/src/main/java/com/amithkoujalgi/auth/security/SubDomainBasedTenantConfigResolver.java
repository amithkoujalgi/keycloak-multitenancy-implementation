package com.amithkoujalgi.auth.security;

import com.amithkoujalgi.auth.AuthServerApp;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCHttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;

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

	public String getDomainName( String url ) throws URISyntaxException
	{
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	@Override
	public KeycloakDeployment resolve( OIDCHttpFacade.Request request )
	{
		String url = null;
		try
		{
			url = getDomainName(request.getURI());
		}
		catch( URISyntaxException e )
		{
			e.printStackTrace();
		}
		//split the URL by dot. i.e., extract the subdomain. For example, xxx is the subdomain entity in the URL xxx.app.com
		String realm = url.split("\\.")[0];
		KeycloakDeployment deployment = cache.get(realm);
		if( null == deployment )
		{
			// if not found on the cache, try to load it from the file system
			try
			{
				FileInputStream is = new FileInputStream(
						AuthServerApp.REALMS_DIRECTORY + File.separator + realm + "-keycloak.json");
				if( is == null )
				{
					throw new IllegalStateException("Not able to find the file /" + realm + "-keycloak.json");
				}
				deployment = KeycloakDeploymentBuilder.build(is);
				cache.put(realm, deployment);
			}
			catch( FileNotFoundException e )
			{
				//				e.printStackTrace();
				System.out.println("[ERROR]: Invalid tenant '" + realm + "' or tenant config not found!");
			}
		}
		return deployment;
	}

}