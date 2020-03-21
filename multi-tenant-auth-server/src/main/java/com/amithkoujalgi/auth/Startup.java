package com.amithkoujalgi.auth;

import com.amithkoujalgi.realmmanager.RealmManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Startup implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private RealmManager realmManager;

	@Override
	public void onApplicationEvent( ContextRefreshedEvent event )
	{
		try
		{
			setupTenants();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void setupTenants() throws Exception
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
				try
				{
					realmManager.createRealm(tenant);
				}
				catch( Exception e )
				{
					e.printStackTrace();
					realmManager.deleteRealm(tenant);
				}
			}
		}
		else
		{
			String tenant = tenants;
			try
			{
				realmManager.createRealm(tenant);
			}
			catch( Exception e )
			{
				e.printStackTrace();
				realmManager.deleteRealm(tenant);
			}
		}
	}
}
