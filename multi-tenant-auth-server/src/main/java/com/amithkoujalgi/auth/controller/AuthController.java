package com.amithkoujalgi.auth.controller;

import com.amithkoujalgi.auth.abstraction.AbstractController;
import com.amithkoujalgi.auth.model.Token;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping( "/" )
public class AuthController extends AbstractController {

	public static synchronized String getTokenRedirectionPrefix( String redirectURI, String token )
	{
		return "redirect:" + redirectURI + "?app-auth-token=" + token;
	}

	@GetMapping( path = "/authenticate" )
	@ModelAttribute
	public ModelAndView auth(
			@RequestParam( "app_redirect_uri" )
					String redirectURI, HttpServletRequest request, HttpServletResponse response )
			throws ServletException
	{
		try
		{
			final KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request
					.getUserPrincipal();
			//			System.out.println(keycloakAuthenticationToken.getName());

			final KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) keycloakAuthenticationToken.getPrincipal();
			final IDToken idToken = keycloakPrincipal.getKeycloakSecurityContext().getIdToken();

			System.out.println(
					"Login successful from Keycloak. Token created: " + keycloakPrincipal.getKeycloakSecurityContext()
							.getTokenString());

			response.addHeader("user-status", "success");

			String redirectTo = getTokenRedirectionPrefix(redirectURI,
					keycloakPrincipal.getKeycloakSecurityContext().getTokenString());

			return new ModelAndView(redirectTo);
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return new ModelAndView("redirect:/error");
		}
	}

	@GetMapping( path = "/is-authenticated" )
	@ModelAttribute
	public ResponseEntity isAuthenticated( HttpServletRequest request, HttpServletResponse response )
			throws ServletException
	{
		final KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request
				.getUserPrincipal();
		if( keycloakAuthenticationToken == null )
		{
			return buildErrorResponse(
					"Sorry, there's nothing available here for you unless you authenticate yourself! If you'd like to do so, head over to /authenticate?bb_redirect_uri=/home.html");
		}
		else
		{
			return buildResponse("Authorized!");
		}
	}

	@GetMapping( path = "/get-user" )
	@ModelAttribute
	public ResponseEntity getUser( HttpServletRequest request, HttpServletResponse response ) throws ServletException
	{
		final KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request
				.getUserPrincipal();
		if( keycloakAuthenticationToken == null )
		{
			return buildErrorResponse(
					"Sorry, there's nothing available here for you unless you authenticate yourself! If you'd like to do so, head over to /authenticate?bb_redirect_uri=/home.html");
		}
		else
		{
			final KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) keycloakAuthenticationToken.getPrincipal();
			final IDToken idToken = keycloakPrincipal.getKeycloakSecurityContext().getIdToken();
			return buildResponse(new Token(keycloakPrincipal.getKeycloakSecurityContext().getTokenString(), idToken));
		}
	}

	/**
	 * Invalidate session. Implement single sign-out. i.e., When sessions are killed from Keycloak
	 * server, automatically destroy their respective sessions in all instances of auth server.
	 *
	 * @param request
	 * @param principal
	 * @return
	 * @throws ServletException
	 * @throws Exception
	 * @See <a href= 'http://lists.jboss.org/pipermail/keycloak-user/2016-March/005479.html'>More
	 * info</a> and <a href=
	 * 'https://github.com/keycloak/keycloak/blob/master/adapters/oidc/spring-security/src/main/java/org/keycloak/adapters/springsecurity/filter/KeycloakPreAuthActionsFilter.java'>here</a>
	 */
	@GetMapping( path = "/logout" )
	public ModelAndView logout( Principal principal, HttpServletRequest request, HttpServletResponse response )
			throws ServletException
	{
		request.logout();
		return new ModelAndView("redirect:/");

		//		final KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request
		//				.getUserPrincipal();
		//		HttpFacade facade = new SimpleHttpFacade(request, response);
		//		KeycloakDeployment deployment = adapterDeploymentContext.resolveDeployment(facade);
		//		adapterTokenStoreFactory.createAdapterTokenStore(deployment, request, response).logout();
		//		RefreshableKeycloakSecurityContext session = (RefreshableKeycloakSecurityContext) keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext();
		//		session.logout(deployment);
	}

}

