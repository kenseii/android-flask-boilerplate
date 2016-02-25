from models import Base, User
from flask import Flask, jsonify, request, url_for, abort, g, render_template, make_response
from flask.ext.httpauth import HTTPBasicAuth
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship, sessionmaker
from sqlalchemy import create_engine
import json
from oauth2client import client
from oauth2client.client import flow_from_clientsecrets, FlowExchangeError
from oauth2client import crypt
import httplib2
import requests
import os
from datetime import datetime

from config import DEBUG, PORT, HOST, CLIENT_SECRETS_PATH, CLIENT_ID, ANDROID_CLIENT_ID, IOS_CLIENT_ID, WEB_CLIENT_ID, OAUTH_KEY, TOKEN_KEY, APPS_DOMAIN_NAME, DATABASE_URL

auth = HTTPBasicAuth()
engine = create_engine(DATABASE_URL)

Base.metadata.bind = engine
DBSession = sessionmaker(bind=engine)
session = DBSession()
app = Flask(__name__)


@auth.verify_password
def verify_password(token, not_used):
    debug("token is: " + token)
    user_id = User.verify_auth_token(token)
    if user_id:
        user = session.query(User).filter_by(id = user_id).one()
        g.user = user
        return True
    else:
        return False


@app.route('/auth/google', methods = ['POST'])
def login():
    try:
        auth_token = request.json.get(OAUTH_KEY)
        debug(auth_token)
    except AttributeError:
        response = make_response(json.dumps("No token provided"), 401)
        response.headers['Content-Type'] = 'application/json'
        return response
        
    err = ''
    idinfo = None
    try:
        idinfo = client.verify_id_token(auth_token, CLIENT_ID)
        debug(idinfo)
        # If multiple clients access the backend server:
        if idinfo['aud'] not in [ANDROID_CLIENT_ID, IOS_CLIENT_ID, WEB_CLIENT_ID]:
            err = "Unrecognized client."
            raise crypt.AppIdentityError(err)
        if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
            err = "Wrong issuer"
            raise crypt.AppIdentityError(err)
        if idinfo['hd'] != APPS_DOMAIN_NAME:
            err = "Wrong hosted domain"
            raise crypt.AppIdentityError(err)
    except crypt.AppIdentityError:
        # Invalid token
        err =  err if err else "Failed to verify token"
        debug("error was: " + err)
        response = make_response(json.dumps("Invalid token: {}".format(err)), 401)
        response.headers['Content-Type'] = 'application/json'
        return response
        
    '''
This was commented out in UDACITY-REST... need to review.
        # stored_credentials = login_session.get('credentials')
        # stored_gplus_id = login_session.get('gplus_id')
        # if stored_credentials is not None and gplus_id == stored_gplus_id:
        #     response = make_response(json.dumps('Current user is already connected.'), 200)
        #     response.headers['Content-Type'] = 'application/json'
        #     return response
    '''

    debug("Step 2 Complete! Access Token : %s " % auth_token)


    ##TODO.
    if not idinfo["email_verified"]:
        pass

    #see if user exists, if it doesn't make a new one
    user = session.query(User).filter_by(email=idinfo['email']).first()
    if not user:
        user = User(
            username = idinfo['name'], 
            picture = idinfo['picture'], 
            email = idinfo['email'])
        session.add(user)
        session.commit()

    token = user.generate_auth_token(600)
    return jsonify({TOKEN_KEY: token.decode('ascii')})
        

@app.route('/api/resource')
@auth.login_required
def get_resource():
    dt = datetime.now()
    return jsonify({ 'data': 'Hello, {}! {}'.format(g.user.username, dt.strftime("%B %d %Y %H:%M:%s")) })

@app.route('/auth/check_token')
@auth.login_required
def check_token():
    return jsonify({'status': 'OK'})

def debug(s):
    if DEBUG:
        print(s)


if __name__ == '__main__':
    app.debug = DEBUG
    app.run(host=HOST, port=PORT)