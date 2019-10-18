package eu.doppel_helix.dev.pluginportaldemo.resources;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import eu.doppel_helix.dev.pluginportaldemo.OAuthConfiguration;
import eu.doppel_helix.dev.pluginportaldemo.PluginPortalDemoConfiguration;
import eu.doppel_helix.dev.pluginportaldemo.api.OauthProvider;
import eu.doppel_helix.dev.pluginportaldemo.client.GenericUserinfo;
import eu.doppel_helix.dev.pluginportaldemo.client.TokenResponse;
import eu.doppel_helix.dev.pluginportaldemo.client.Userinfo;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.logging.LoggingFeature;
import org.hibernate.validator.constraints.NotEmpty;

@Path("/oauth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    private static final Logger LOG = Logger.getLogger(AuthenticationResource.class.getName());

    @Inject
    private PluginPortalDemoConfiguration configuration;

    private final Map<String, OAuthConfiguration> configurationMap = new HashMap<>();
    private final List<OauthProvider> availableProviders = new ArrayList<>();
    private final Random random = new Random();

    private final static Cache<String, String> authCache = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build();

    public AuthenticationResource() {
    }

    @PostConstruct
    public void init() {
        for (OAuthConfiguration config : configuration.getOauthConfig()) {
            configurationMap.put(config.getId(), config);
            OauthProvider provider = new OauthProvider();
            provider.setId(config.getId());
            provider.setName(config.getName());
            availableProviders.add(provider);
        }
    }

    @GET
    public List<OauthProvider> availableConfigs() {
        return availableProviders;
    }

    @Path("start/{id}")
    @GET
    public Response loginStart(@PathParam("id") String id) {
        OAuthConfiguration config = configurationMap.get(id);
        if (config == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("Unknown authentication provider")
                .build();
        } else {
            byte[] stateBytes = new byte[64];
            random.nextBytes(stateBytes);
            String state = Base64.getEncoder().encodeToString(stateBytes);
            authCache.put(state, id);
            UriBuilder uriBuilder = UriBuilder.fromUri(config.getType().getAuthenticationUrl());
            uriBuilder.queryParam("client_id", config.getClientId());
            uriBuilder.queryParam("state", state);
            if (config.getType().getRequiredScopes() != null && (!config.getType().getRequiredScopes().isEmpty())) {
                uriBuilder.queryParam("scope", config.getType().getRequiredScopes().stream().collect(Collectors.joining(" ")));
            }
            uriBuilder.queryParam("response_type", "code");
            uriBuilder.queryParam("redirect_uri", configuration.getOauthUrl().toASCIIString());
            LOG.log(Level.INFO, "Handed out Secret: {0}", state);
            return Response.seeOther(uriBuilder.build()).build();
        }
    }

    @Path("code")
    @GET
    public Response codeResponse(@QueryParam("state") @NotEmpty String state, @QueryParam("code") @NotEmpty String code) {
        LOG.log(Level.INFO, String.format("Received State: '%s', Code: '%s'", state, code));

        String configId = authCache.getIfPresent(state);
        if (configId == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        OAuthConfiguration config = configurationMap.get(configId);
        if (config == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Client client = ClientBuilder.newClient();
        client.register(new LoggingFeature(LOG, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 65000));
        TokenResponse response = client.target(config.getType().getTokenUrl())
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(config.getType().getTokenRequest(code, configuration, config), MediaType.APPLICATION_JSON), TokenResponse.class);

        LOG.log(Level.INFO, String.format("Received Token: '%s'", response));

        if (response.getAccessToken() == null || (!"bearer".equalsIgnoreCase(response.getTokenType()))) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No token received or not expected type").build();
        }

        Userinfo user = client.target(config.getType().getUserinfoUrl())
            .request()
            .header("Authorization", "Bearer " + response.getAccessToken())
            .get(config.getType().getUserinfoType());

        LOG.log(Level.INFO, "Received user: {0}", user);

        GenericUserinfo gui = user.toGenericUserinfo(config.getId());
        return Response.ok().entity(gui).build();
    }
}
