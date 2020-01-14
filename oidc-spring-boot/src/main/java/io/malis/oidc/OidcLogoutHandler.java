package io.malis.oidc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by malis.io on 2019-08-07.
 */
@Component
public class OidcLogoutHandler implements LogoutHandler {

  private final OAuth2AuthorizedClientService clientService;

  @Autowired
  public OidcLogoutHandler(OAuth2AuthorizedClientService clientService) {
    this.clientService = clientService;
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    Optional<OAuth2AuthorizedClient> client = getClient(authentication);

    if (client.isPresent() &&
        client.get().getAccessToken() != null &&
        client.get().getRefreshToken() != null &&
        client.get().getClientRegistration() != null &&
        client.get().getClientRegistration().getClientId() != null &&
        client.get().getClientRegistration().getClientSecret() != null &&
        getLogoutUrl(client.get()).isPresent()) {

      String accessToken = client.get().getAccessToken().getTokenValue();
      String refreshToken = client.get().getRefreshToken().getTokenValue();
      String clientId = client.get().getClientRegistration().getClientId();
      String clientSecret = client.get().getClientRegistration().getClientSecret();

      RestTemplate restTemplate = getOauth2RestTemplate(accessToken);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add("refresh_token", refreshToken);
      body.add("client_id", clientId);
      body.add("client_secret", clientSecret);

      HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

      restTemplate.exchange(
          getLogoutUrl(client.get()).get(),
          HttpMethod.POST,
          entity,
          Map.class);
    }
  }

  /**
   * @see org.springframework.security.oauth2.client.registration.ClientRegistrations#fromOidcIssuerLocation
   * @see com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata
   */
  private Optional<String> getLogoutUrl(OAuth2AuthorizedClient client){
    if (client.getClientRegistration().getProviderDetails() != null &&
        client.getClientRegistration().getProviderDetails().getConfigurationMetadata() != null){
      Object metadataLogoutUrl = client.getClientRegistration().getProviderDetails().getConfigurationMetadata().get("end_session_endpoint");
      if (metadataLogoutUrl instanceof String) {
        return Optional.of((String)metadataLogoutUrl);
      }
    }
    return Optional.empty();
  }

  private Optional<OAuth2AuthorizedClient> getClient(Authentication authentication) {
    if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
      OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
      String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
      OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
      return Optional.of(client);
    }
    return Optional.empty();
  }

  private RestTemplate getOauth2RestTemplate(String accessToken) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(getBearerTokenInterceptor(accessToken));
    return restTemplate;
  }

  private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
    return (request, bytes, execution) -> {
      request.getHeaders().add("Authorization", "Bearer " + accessToken);
      return execution.execute(request, bytes);
    };
  }

}
