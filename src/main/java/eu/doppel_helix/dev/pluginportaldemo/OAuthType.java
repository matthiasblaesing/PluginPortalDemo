package eu.doppel_helix.dev.pluginportaldemo;

import eu.doppel_helix.dev.pluginportaldemo.client.AmazonUser;
import eu.doppel_helix.dev.pluginportaldemo.client.GithubTokenRequest;
import eu.doppel_helix.dev.pluginportaldemo.client.GithubUser;
import eu.doppel_helix.dev.pluginportaldemo.client.GoogleUser;
import eu.doppel_helix.dev.pluginportaldemo.client.OAuth2TokenRequest;
import eu.doppel_helix.dev.pluginportaldemo.client.Userinfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum OAuthType {
    github(
        "https://github.com/login/oauth/authorize",
        "https://github.com/login/oauth/access_token",
        "https://api.github.com/user",
        Arrays.asList("user:email")),
    google(
        "https://accounts.google.com/o/oauth2/v2/auth",
        "https://oauth2.googleapis.com/token",
        "https://openidconnect.googleapis.com/v1/userinfo",
        Arrays.asList("openid", "email", "profile")),
    amazon(
        "https://www.amazon.com/ap/oa",
        "https://api.amazon.com/auth/o2/token",
        "https://api.amazon.com/user/profile",
        Arrays.asList("profile"));

    private final URI authenticationUrl;
    private final URI tokenUrl;
    private final URI userinfoUrl;
    private final List<String> requiredScopes;

    private OAuthType(String authenticationUrl, String tokenUrl, String userinfoUrl, List<String> requiredScopes) {
        try {
            this.authenticationUrl = new URI(authenticationUrl);
            this.tokenUrl = new URI(tokenUrl);
            this.userinfoUrl = new URI(userinfoUrl);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        this.requiredScopes = Collections.unmodifiableList(new ArrayList<>(requiredScopes));
    }

    public URI getAuthenticationUrl() {
        return authenticationUrl;
    }

    public URI getTokenUrl() {
        return tokenUrl;
    }

    public URI getUserinfoUrl() {
        return userinfoUrl;
    }

    public List<String> getRequiredScopes() {
        return requiredScopes;
    }

    public Object getTokenRequest(String code, PluginPortalDemoConfiguration config, OAuthConfiguration oauthConfig) {
        if(this == OAuthType.github) {
            return new GithubTokenRequest(code, oauthConfig.getClientId(), oauthConfig.getClientSecret());
        } else  {
            return new OAuth2TokenRequest(code, oauthConfig.getClientId(), oauthConfig.getClientSecret(), config.getOauthUrl().toASCIIString(), "authorization_code");
        }
    }

    public Class<? extends Userinfo> getUserinfoType() {
        switch (this) {
            case amazon:
                return AmazonUser.class;
            case github:
                return GithubUser.class;
            case google:
                return GoogleUser.class;
            default:
                return null;
        }
    }
}
