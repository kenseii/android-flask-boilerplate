package com.example.boilerplateapplication;

import android.app.Application;

/**
 * Created by sqh on 2/29/16.
 */
public class BoilerplateApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
