
package eu.doppel_helix.dev.pluginportaldemo.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmazonUser implements Userinfo {
    @JsonProperty("user_id")
    private String userId;
    private String email;
    private String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public GenericUserinfo toGenericUserinfo(String providerId) {
        GenericUserinfo gui = new GenericUserinfo();
        gui.setEmail(email);
        gui.setName(name);
        gui.setProviderId(providerId);
        gui.setSubject(userId);
        return gui;
    }
}
