
package eu.doppel_helix.dev.pluginportaldemo.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUser implements Userinfo {
    private int id;
    private String email;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        gui.setSubject(Integer.toString(id));
        return gui;
    }
}
