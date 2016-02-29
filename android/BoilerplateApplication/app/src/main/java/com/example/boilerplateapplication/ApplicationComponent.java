package com.example.boilerplateapplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sqh on 2/29/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(BoilerplateApplication target);
    void inject(MainActivity target);
    void inject(SignOnActivity target);
}
