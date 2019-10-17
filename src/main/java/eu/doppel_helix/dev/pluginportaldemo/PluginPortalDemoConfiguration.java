package eu.doppel_helix.dev.pluginportaldemo;

import io.dropwizard.Configuration;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriBuilder;
import org.hibernate.validator.constraints.NotEmpty;

public class PluginPortalDemoConfiguration extends Configuration {
    @NotNull
    @NotEmpty
    @Valid
    private List<OAuthConfiguration> oauthConfig = new ArrayList<>();

    public List<OAuthConfiguration> getOauthConfig() {
        return oauthConfig;
    }

    public void setOauthConfig(List<OAuthConfiguration> oauthConfig) {
        this.oauthConfig = oauthConfig;
    }

    private URI baseUrl;

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public URI getOauthUrl() {
        UriBuilder build = UriBuilder.fromUri(getBaseUrl());
        build.path("/oauth/code");
        return build.build();
    }
}
