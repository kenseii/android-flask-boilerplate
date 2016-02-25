Android Flask Boilerplate
=========================
Boilerplate code for creating an Android app that authenticates to a Flask backend using the Google API Client and an OAuth2 Authentication Token.

I assume that you:
+ ...are trying to build an authenticated Android client with a Flask back end.
+ ...want to use Google sign on as the identity provider, and want to avoid having to maintain a username password/reset/2fa system.
+ ...want to use Retrofit to build out your client (there is nothing to say that you couldn't use another REST client, but the boilerplate already uses Retrofit for the few REST calls that it performs, so it makes sense to build the rest of the API client using it.)

Set Up The Backend
==================
+ Go to console.developers.google.com and register your backend with Google. Download the client_secrets.json file and store it in the root of the project.
```
mv ~/Downloads/client_secrets.json android-flask-boilerplate
```
+ Copy environments.example to myenvironment, Change the secret keys to something long and complex, and the path to the above client_secrets.json to point to the right place.
``` 
source myenvironment
```
+ create a virtual machine for the python backend and activate it
```
mkvirtualenv android-flask-boilerplate
```

+ Install libffi for crpyto
```
brew install pkg-config libffi
```

+ install the required Python dependencies
```
pip install -r python/requirements.txt
```
+ Run the server
```
workon android-flask-boilerplate ;; if you haven't already activated the virtual env
cd android-flask-boilerplate/python
python views.py
```

Set Up The Android Client
=========================

+ Sign up for Google Signon at https://developers.google.com/identity/sign-in/android/start-integrating and download the google-services.json configuration file.
```
 mv ~/Downloads/google-services.json android/BolerplateApplication/app
```
+ Make sure the host and port in com.example.boilerplateapplication.Constants point to the right backend.
```
private static String host = "10.0.2.2";
private static int port = 5000;
```
+ Put the server client ID from your servers client_secrets.json (above) in a string resource
```
cp example.server_client_id.xml android/BoilerplateApplication/app/src/main/res/values/server_client_id.xml
```




TODO
====
Arguably it is more secure to read the SECRET_KEY from a file rather than the environment, as you can't alway predict when libraries or external programs will accidentally dump the environment to the user.  (Modulo disk access on the system you are deploying to: for example a file based system might be annoying on Heroku)

See Also
========
https://developers.google.com/identity/sign-in/android/start-integrating

https://developers.google.com/identity/sign-in/android/backend-auth

https://www.udacity.com/course/designing-restful-apis--ud388