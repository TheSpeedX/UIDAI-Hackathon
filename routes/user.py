from fastapi import APIRouter, Depends
from models.user import User, UpdateUser
from config.database import db
from utils.token import get_current_user, get_current_username
from utils.xmlparser import parse_xml
from typing import List
from models.verifier import VerifierInfo

user = APIRouter()


@user.get('/me', response_model=User, tags=["user"])
async def current_user(current_user: User = Depends(get_current_user)):
    return current_user


@user.put('/me', tags=["user"])
async def update_user(
        user: UpdateUser,
        username: str = Depends(get_current_username)):
    userData = user.todict()
    if userData.get('password'):
        userData['vid'] = userData['password']
    if userData.get('eKYCXML'):
        userData.update(parse_xml(userData.get("eKYCXML")))
    await db.users.update_one(
        {"username": username},
        {"$set": userData})
    return {"success": True}


@user.get('/access_logs',
          response_model=List[VerifierInfo],
          response_model_exclude={"api_key", "vid"},
          tags=["user"])
async def get_access_logs(
        username: str = Depends(get_current_username)):
    user = await db.users.find_one({"username": username})
    return await db.access_logs.find(
        {"vid": user.get("vid")}).to_list(length=100)
