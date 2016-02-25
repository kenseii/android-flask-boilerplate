package com.example.boilerplateapplication.REST.POJO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sqh on 2/25/16.
 */
public class AuthToken {



        @SerializedName("auth_token")
        @Expose
        private String authToken;

        /**
         *
         * @return
         * The authToken
         */
        public String getAuthToken() {
            return authToken;
        }

        /**
         *
         * @param authToken
         * The auth_token
         */
        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

    }
