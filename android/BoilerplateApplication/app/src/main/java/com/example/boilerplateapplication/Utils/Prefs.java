package com.example.boilerplateapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.boilerplateapplication.R;

/**
 * Created by sqh on 2/24/16.
 */
public class Prefs {

    private Context mContext;

    public Prefs(Context context) {
        mContext = context;
    }

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(
                mContext.getString(R.string.preference_file_key),
                mContext.MODE_PRIVATE);
    }
    public String getAuthToken() {
        return getPrefs().getString(mContext.getString(R.string.auth_token_pref), null);
    }

    public void setAuthToken(String token) {
        getPrefs().edit().putString(mContext.getString(R.string.auth_token_pref), token).commit();
    }

    public void setDisplayName(String displayName) {
        getPrefs().edit().putString(
                mContext.getString(R.string.user_display_name_pref), displayName).commit();
    }

    public String getDisplayName() {
        return getPrefs().getString(mContext.getString(R.string.user_display_name_pref), null);
    }

    public void removeAuthToken() {
        getPrefs().edit().remove(mContext.getString(R.string.auth_token_pref)).commit();
    }
}
