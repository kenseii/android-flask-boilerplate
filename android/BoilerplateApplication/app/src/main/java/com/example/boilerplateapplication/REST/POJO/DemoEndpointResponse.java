package com.example.boilerplateapplication.REST.POJO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sqh on 2/25/16.
 */
public class DemoEndpointResponse {


        @SerializedName("data")
        @Expose
        private String data;

        /**
         *
         * @return
         * The data
         */
        public String getData() {
            return data;
        }

        /**
         *
         * @param data
         * The data
         */
        public void setData(String data) {
            this.data = data;
        }

    }
