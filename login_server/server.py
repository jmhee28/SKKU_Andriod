from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String
USER = "postgres"
PW = "pwd"
URL = ""
PORT = ""
DB = "postgres"
engine = create_engine("postgresql://{}:{}@{}:{}/{}".format(USER, PW, URL,PORT, DB))
db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()
class User(Base):
    __tablename__ = 'users' #make table id name pwd
    id = Column(Integer, primary_key=True)
    name = Column(String(50), unique=True) #같은 이름 존재 불가 unique
    passwd = Column(String(120), unique=False)
    def __init__(self, name=None, passwd=None):
        self.name = name
        self.passwd = passwd
    def __repr__(self):
        return f'<User {self.name!r}>'
# Base.metadata.drop_all(bind=engine) data 정리
Base.metadata.create_all(bind=engine)

from flask import Flask
from flask import request
from flask import jsonify
from werkzeug.serving import WSGIRequestHandler
import json
import json
WSGIRequestHandler.protocol_version = "HTTP/1.1"
app = Flask(__name__)
@app.route("/adduser", methods=['POST'])
def add_user():
    content = request.get_json(silent=True)
    name = content["name"]
    passwd = content["passwd"]
    if db_session.query(User).filter_by(name=name).first() is None: #query ->찾고싶은 테이블 유저에서 특정 name 찾음 없다면 새로 만둚
        u = User(name=name, passwd=passwd)
        db_session.add(u)
        db_session.commit() #반영
        return jsonify(success=True)
    else:#아니면 안넣어줌
        return jsonify(success=False)
if __name__ == "__main__":
    app.run(host='localhost', port=8888)

@app.route("/login", methods=['POST'])
def login():
    content = request.get_json(silent=True)
    name = content["name"]
    passwd = content["passwd"]
    print("POST name : " + name)
    check = False
    result = db_session.query(User).all() #모든 리스트
    for i in result:
        if i.name == name and i.passwd == passwd: #데이터안의 이름이 같고 password도 같으면 true tocken
            check = True
    return jsonify(success=check)
