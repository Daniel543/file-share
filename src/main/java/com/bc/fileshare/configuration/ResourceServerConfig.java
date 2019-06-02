package com.bc.fileshare.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	//TODO maybe add additional validation for which resource was requested

	/*@Override
	public void configure(HttpSecurity http) throws Exception {
		http.
			anonymous().disable()
			.authorizeRequests()
			.antMatchers("/files/**").access("hasRole('ADMIN')")
			.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}*/
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.requestMatchers()
				.requestMatchers(
						new NegatedRequestMatcher(
								new OrRequestMatcher(
										new AntPathRequestMatcher("/login"),
										new AntPathRequestMatcher("/logout"),
										new AntPathRequestMatcher("/images/**"),
										new AntPathRequestMatcher("/"),
										new AntPathRequestMatcher("/*.js"),
										new AntPathRequestMatcher("/*.ico"),
										new AntPathRequestMatcher("/*.css"),
										new AntPathRequestMatcher("/*.png"),
										new AntPathRequestMatcher("/*.html"),
										new AntPathRequestMatcher("/oauth/authorize"),
										new AntPathRequestMatcher("/oauth/confirm_access"),
										new AntPathRequestMatcher("/users")
								)
						)
				)
				.and()
				.authorizeRequests().anyRequest().authenticated()
				.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}


}
