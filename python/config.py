import os
import json

DATABASE_URL = os.environ["AFB_DATABASE_URL"]
CLIENT_SECRETS_PATH = os.environ["AFB_CLIENT_SECRETS_PATH"]
SECRET_KEY = os.environ["AFB_SECRET_KEY"]
if (os.environ["AFB_DEBUG"] == "True"):
	DEBUG = True
else:
	DEBUG = False
PORT = int(os.environ["AFB_PORT"])
HOST = os.environ["AFB_HOST"]

CLIENT_ID = json.loads(
    open(CLIENT_SECRETS_PATH, 'r').read())['web']['client_id']

ANDROID_CLIENT_ID = os.environ["AFB_ANDROID_CLIENT_ID"]
WEB_CLIENT_ID = os.environ.get("AFB_ANDROID_CLIENT_ID", None)
IOS_CLIENT_ID = os.environ.get("AFB_ANDROID_CLIENT_ID", None)

APPS_DOMAIN_NAME = os.environ["AFB_APPS_DOMAIN_NAME"]

OAUTH_KEY = 'oauth_token'
TOKEN_KEY = 'auth_token'