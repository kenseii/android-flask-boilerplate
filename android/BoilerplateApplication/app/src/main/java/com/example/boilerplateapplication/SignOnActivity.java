package com.example.boilerplateapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.boilerplateapplication.Auth.GoogleAuth;
import com.example.boilerplateapplication.REST.POJO.AuthToken;
import com.example.boilerplateapplication.REST.POJO.CheckTokenResponse;
import com.example.boilerplateapplication.REST.POJO.OAuthToken;
import com.example.boilerplateapplication.REST.RetrofitClient;
import com.example.boilerplateapplication.Utils.Prefs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignOnActivity extends AppCompatActivity implements View.OnClickListener
{
    private static int GOOGLE_RC_SIGN_IN = 1;
    private static final String TAG = SignOnActivity.class.getSimpleName();
    GoogleAuth mGoogleAuthAuth;
    Prefs mPrefs;
    RetrofitClient.Endpoints mApiService;

    @Bind(R.id.sign_in_button)
    com.google.android.gms.common.SignInButton signInButton;
    @Bind(R.id.revoke_access)
    Button revokeAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signon);
        ButterKnife.bind(this);
        signInButton.setOnClickListener(this);
        revokeAccess.setOnClickListener(this);
        mGoogleAuthAuth = new GoogleAuth(this);
        mPrefs = new Prefs(this);
        mApiService = RetrofitClient.getApiService(mPrefs);
        checkForExistingAuthTokenAndRedirect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                startActivityForResult(mGoogleAuthAuth.getIntent(), GOOGLE_RC_SIGN_IN);
                break;
            case R.id.revoke_access:
                revokeAccess();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String oAuthToken = acct.getIdToken();
                mPrefs.setDisplayName(acct.getDisplayName());
                requestAuthToken(oAuthToken);
            } else {
                Toast.makeText(this, "Sign in failed, please try again",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestAuthToken(String oAuthToken) {
        OAuthToken o = new OAuthToken();
        o.setOauthToken(oAuthToken);
        Call<AuthToken> call = mApiService.getAuthToken(o);
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                AuthToken authToken = response.body();
                try {
                    mPrefs.setAuthToken(authToken.getAuthToken());
                    startActivity(new Intent(SignOnActivity.this, MainActivity.class));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                Toast.makeText(SignOnActivity.this,
                        String.format("Failed to connect to %s", Constants.BASE_URL), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleAuthAuth.getClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(SignOnActivity.this, "Access has been revoked", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkForExistingAuthTokenAndRedirect() {
        // auth token is automatically injected into the header, so if this succeeds then we are logged in.
        Call<CheckTokenResponse> call = mApiService.checkToken();
        call.enqueue(new Callback<CheckTokenResponse>() {
            @Override
            public void onResponse(Call<CheckTokenResponse> call, Response<CheckTokenResponse> response) {
                try {
                    CheckTokenResponse r = response.body();
                    if (r.getStatus().equals(Constants.AUTH_STATUS_OK)) {
                        startActivity(new Intent(SignOnActivity.this, MainActivity.class));
                        return;
                    }
                } catch (Exception e) {
                    // not authorized so no data.
                }
                Log.d(TAG, "no existing valid auth token");
            }

            @Override
            public void onFailure(Call<CheckTokenResponse> call, Throwable t) {
                Log.d(TAG, "check token failure");
            }
        });
    }
}