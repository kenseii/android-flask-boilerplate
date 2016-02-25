package com.example.boilerplateapplication;

/**
 * Created by sqh on 2/24/16.
 */
public class Constants {
    private static String HOST = "10.0.2.2";
    private static int PORT = 5000;
    private static String PROTOCOL = "http://";  //USE HTTPS IN PRODUCTION!!
    public static String BASE_URL = PROTOCOL + HOST + ":" + PORT;
    public static String AUTH_STATUS_OK = "OK";
}
