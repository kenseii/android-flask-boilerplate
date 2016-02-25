package com.example.boilerplateapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boilerplateapplication.REST.POJO.AuthToken;
import com.example.boilerplateapplication.REST.POJO.DemoEndpointResponse;
import com.example.boilerplateapplication.REST.POJO.OAuthToken;
import com.example.boilerplateapplication.REST.RetrofitClient;
import com.example.boilerplateapplication.Utils.Prefs;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.auth_token)
    TextView authToken;
    @Bind(R.id.display_name)
    TextView displayName;
    @Bind(R.id.api_result)
    TextView apiResult;
    @Bind(R.id.sign_out)
    Button signOut;
    @Bind(R.id.invalidate_auth_token)
    Button invalidateAuthToken;
    @Bind(R.id.hit_api)
    Button hitApi;

    private Prefs mPrefs;
    RetrofitClient.Endpoints mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = new Prefs(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mApiService = RetrofitClient.getApiService(mPrefs);
        displayName.setText(mPrefs.getDisplayName());
        authToken.setText(mPrefs.getAuthToken());
        hitApi.setOnClickListener(this);
        signOut.setOnClickListener(this);
        invalidateAuthToken.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hit_api:
                hitApi();
                break;
            case R.id.sign_out:
                signOut();
                break;
            case R.id.invalidate_auth_token:
                invalidateAuthTokenForDebugging();
                break;
        }
    }


    private void hitApi() {
        Call<DemoEndpointResponse> call = mApiService.getDemoEndpoint();
        call.enqueue(new Callback<DemoEndpointResponse>() {
            @Override
            public void onResponse(Call<DemoEndpointResponse> call, Response<DemoEndpointResponse> response) {
                try {
                    DemoEndpointResponse o = response.body();
                    apiResult.setText(o.getData());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error hitting API, not authorized?", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<DemoEndpointResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "hitApi(): onFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signOut() {
        mPrefs.removeAuthToken();
        startActivity(new Intent(this, SignOnActivity.class));
    }

    private void invalidateAuthTokenForDebugging() {
        mPrefs.setAuthToken("hacker!");
        Toast.makeText(this, "Auth token invalidated.", Toast.LENGTH_LONG).show();
    }
}
