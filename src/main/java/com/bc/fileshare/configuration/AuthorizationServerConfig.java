package com.bc.fileshare.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	private static final String CLIENT_SECRET = "$2a$04$a95nzMYqnJASmMQoQjpMDeyIsKUeGvRuKkTfF3aB8cG3wm2I7ol1C";
	private static final String CLIENT_ID = "fileshare-angular";
	private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 3600; //1 Hour
	private static final int REFRESH_TOKEN_VALIDITY_SECONDS = 86400; //1 day

	@Value("${fileshare.redirectUriList}")
	private String redirectUriList;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("be771ax");
		return converter;
	}


	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

		configurer
			.inMemory()
			.withClient(CLIENT_ID)
			.redirectUris(redirectUriList)
			.authorizedGrantTypes("implicit")
			.secret(CLIENT_SECRET)
			.accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
			.refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS)
			.scopes("read", "write", "trust")
		;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.tokenStore(tokenStore())
			.authenticationManager(authenticationManager)
			.accessTokenConverter(accessTokenConverter());

	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

}
