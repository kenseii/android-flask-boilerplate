package com.example.boilerplateapplication.REST.POJO;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sqh on 2/25/16.
 */
@Generated("org.jsonschema2pojo")
public class OAuthToken {


    @SerializedName("oauth_token")
    @Expose
    private String oauthToken;

    /**
     *
     * @return
     * The oauthToken
     */
    public String getOauthToken() {
        return oauthToken;
    }

    /**
     *
     * @param oauthToken
     * The oauth_token
     */
    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

}
