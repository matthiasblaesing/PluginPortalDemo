
package eu.doppel_helix.dev.pluginportaldemo.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUser implements Userinfo {
    private String sub;
    private String email;
    private String name;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
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
        gui.setSubject(sub);
        return gui;
    }
}
