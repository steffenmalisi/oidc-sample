package io.malis.oidc;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by malis.io on 2019-05-28.
 */
@Controller
public class RootController {

  @GetMapping("/")
  public String index(
      Model model,
      @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
      @AuthenticationPrincipal OidcUser oidcUser,
      OAuth2AuthenticationToken currentUser) {

    String userName = oidcUser.getPreferredUsername();
    String idToken = oidcUser.getIdToken().getTokenValue();
    String accessToken = authorizedClient.getAccessToken().getTokenValue();
    String refreshToken = authorizedClient.getRefreshToken().getTokenValue();

    model.addAttribute("userName", userName);
    model.addAttribute("idToken", idToken);
    model.addAttribute("accessToken", accessToken);
    model.addAttribute("refreshToken", refreshToken);

    return "index.html";
  }


}
