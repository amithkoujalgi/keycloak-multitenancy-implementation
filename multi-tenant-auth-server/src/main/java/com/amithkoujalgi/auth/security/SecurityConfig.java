package com.amithkoujalgi.auth.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@ComponentScan( basePackageClasses = KeycloakSecurityComponents.class )
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

	private String resources = "/resources/**";

	/**
	 * Registers the KeycloakAuthenticationProvider with the authentication manager.
	 */
	@Autowired
	public void configureGlobal( AuthenticationManagerBuilder auth ) throws Exception
	{
		KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
		auth.authenticationProvider(keycloakAuthenticationProvider);
	}

	/**
	 * Sets keycloaks config resolver to use springs application.properties instead of keycloak.json (which is standard)
	 *
	 * @return
	 */
	@Bean
	public KeycloakConfigResolver KeycloakConfigResolver()
	{
		return new SubdomainBasedTenantConfigResolver();
	}

	/**
	 * Defines the session authentication strategy.
	 */
	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy()
	{
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Override
	protected void configure( HttpSecurity http ) throws Exception
	{
		super.configure(http);

		//		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().logout()
		//				.invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll();

		super.configure(http);
		http.authorizeRequests().antMatchers("/is-authenticated", "/get-user", "/js*").permitAll()
				.antMatchers("/sso/login*").permitAll().antMatchers("/sso/user/*").permitAll().antMatchers(resources)
				.permitAll().antMatchers("/token-mode").permitAll().antMatchers("/configure-session-timeout")
				.permitAll().antMatchers("/refresh-token").permitAll().antMatchers("/bigbrain/token").permitAll()
				.antMatchers("/access-token").permitAll().antMatchers("/user-authenticate").permitAll().anyRequest()
				.authenticated().and().logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll();

		http.csrf().ignoringAntMatchers("/sso/login*", "/sso/user/*", resources, "/token-mode",
				"/configure-session-timeout", "/refresh-token", "/bigbrain/token", "/access-token",
				"/user-authenticate");

	}

	@Bean
	public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean( KeycloakPreAuthActionsFilter filter )
	{
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

}
