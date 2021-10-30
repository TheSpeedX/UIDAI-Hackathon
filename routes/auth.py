from fastapi import APIRouter, HTTPException, Depends
from datetime import datetime
from config.database import db
from config.variables import JWT_CONFIG
from models.user import User, LoginUser
from utils.xmlparser import parse_xml
from utils.password import get_password_hash, verify_password
from utils.token import (generate_access_token,
                         generate_refresh_token,
                         get_new_access_token)

from fastapi.security import OAuth2PasswordRequestForm

auth = APIRouter()


@auth.post('/register', tags=["auth"])
async def create_user(user: User):
    vid = user.password
    user.password = get_password_hash(user.password)
    user.isActive = True
    user.createdAt = datetime.utcnow()
    user.lastLogin = datetime.utcnow()
    record = await db.users.find_one({"username": user.username})
    user = dict(user)
    user["vid"] = vid
    user.update(parse_xml(user.get("eKYCXML")))
    if record is None:
        await db.users.insert_one(user)
        return {"success": True}
    return {"success": False, "error": "Device ID Already Exists"}


@auth.post('/login', tags=["auth"])
async def login_user(login: LoginUser):
    user = await db.users.find_one({"username": login.username})
    if user:
        if verify_password(login.password, user.get("password")):
            await db.users.find_one_and_update(
                {"username": login.username},
                {"$set": {"lastLogin": datetime.utcnow()}})
            username = user.get("username")
            return {
                "access_token": generate_access_token(username),
                "refresh_token": generate_refresh_token(username),
                "token_type": "Bearer",
                "access_token_expire":
                JWT_CONFIG["ACCESS_TOKEN_EXPIRE_MINUTES"]*60,
                "refresh_token_expire":
                JWT_CONFIG["REFRESH_TOKEN_EXPIRE_MINUTES"]*60
            }
        raise HTTPException(status_code=401, detail="Invalid Virtual Id")
    raise HTTPException(status_code=401, detail="Device ID Does Not Exist")


@auth.get('/refresh', tags=["auth"])
async def refresh_token(
        access_token: str = Depends(get_new_access_token)):
    return {
        "access_token": access_token,
        "token_type": "Bearer",
        "access_token_expire": JWT_CONFIG["ACCESS_TOKEN_EXPIRE_MINUTES"]*60,
    }


@auth.post('/token', tags=["dev"])
async def get_token(login: OAuth2PasswordRequestForm = Depends()):
    user = await db.users.find_one({"username": login.username})
    if user:
        if verify_password(login.password, user.get("password")):
            await db.users.find_one_and_update(
                {"username": login.username},
                {"$set": {"lastLogin": datetime.utcnow()}})
            username = user.get("username")
            return {
                "access_token": generate_access_token(username),
                "refresh_token": generate_refresh_token(username),
                "token_type": "Bearer",
                "access_token_expire":
                JWT_CONFIG["ACCESS_TOKEN_EXPIRE_MINUTES"]*60,
                "refresh_token_expire":
                JWT_CONFIG["REFRESH_TOKEN_EXPIRE_MINUTES"]*60
            }
        raise HTTPException(status_code=401, detail="Invalid Device Id")
    raise HTTPException(status_code=401, detail="Virtual ID Does Not Exist")
