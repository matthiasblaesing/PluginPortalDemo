
package eu.doppel_helix.dev.pluginportaldemo;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class OAuthConfiguration {
    @NotEmpty
    @NotNull
    private String id;
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String clientId;
    @NotEmpty
    @NotNull
    private String clientSecret;
    @NotNull
    private OAuthType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public OAuthType getType() {
        return type;
    }

    public void setType(OAuthType type) {
        this.type = type;
    }
}
