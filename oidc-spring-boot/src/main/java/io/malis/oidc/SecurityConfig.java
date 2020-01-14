package io.malis.oidc;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

/**
 * Created by malis.io on 2019-08-07.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final OAuth2AuthorizedClientService clientService;

  public SecurityConfig(OAuth2AuthorizedClientService clientService) {
    this.clientService = clientService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .oauth2Login()
        .and()
        .logout()
        .addLogoutHandler(new OidcLogoutHandler(clientService));
  }

}
