package com.example.boilerplateapplication.REST;

import android.util.Base64;
import android.util.Log;

import com.example.boilerplateapplication.Constants;
import com.example.boilerplateapplication.REST.POJO.AuthToken;
import com.example.boilerplateapplication.REST.POJO.CheckTokenResponse;
import com.example.boilerplateapplication.REST.POJO.DemoEndpointResponse;
import com.example.boilerplateapplication.REST.POJO.OAuthToken;
import com.example.boilerplateapplication.Utils.Prefs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by sqh on 2/25/16.
 */
public class RetrofitClient {
    private static final String TAG = "RetrofitClient";

    public static Endpoints getApiService(final Prefs prefs) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                // TODO: probably prefs should be cached here.
                String credentials = String.format("%s:blank", prefs.getAuthToken());
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP))
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

       return retrofit.create(Endpoints.class);
    }

    public interface Endpoints {
        @POST("auth/google")
        Call<AuthToken> getAuthToken (@Body OAuthToken oauthToken);

        @GET("auth/check_token")
        Call<CheckTokenResponse> checkToken();

        @GET("api/resource")
        Call<DemoEndpointResponse> getDemoEndpoint();
    }

}

