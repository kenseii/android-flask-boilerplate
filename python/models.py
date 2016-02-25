from sqlalchemy import Column,Integer,String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship, sessionmaker
from sqlalchemy import create_engine
import random, string
from itsdangerous import(TimedJSONWebSignatureSerializer as Serializer, BadSignature, SignatureExpired)
from config import SECRET_KEY, DATABASE_URL

Base = declarative_base()

class User(Base):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True)
    username = Column(String(32), index=True)
    picture = Column(String)
    email = Column(String)
    
    def generate_auth_token(self, expiration=600):
    	s = Serializer(SECRET_KEY, expires_in = expiration)
    	return s.dumps({'id': self.id })

    @staticmethod
    def verify_auth_token(token):
    	s = Serializer(SECRET_KEY)
    	try:
    		data = s.loads(token)
    	except SignatureExpired:
    		return None
    	except BadSignature:
    		return None
    	user_id = data['id']
    	return user_id


engine = create_engine(DATABASE_URL)
Base.metadata.create_all(engine)
    