
package eu.doppel_helix.dev.pluginportaldemo.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubTokenRequest {
    private String code;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;

    public GithubTokenRequest() {
    }

    public GithubTokenRequest(String code, String clientId, String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "TokenRequest{" + "code=" + code + ", clientId=" + clientId + ", clientSecret=" + clientSecret + '}';
    }
}
