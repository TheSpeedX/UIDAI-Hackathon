from fastapi import APIRouter, HTTPException
from models.verifier import Verifier, LoginVerifier, VerifierInfo
from models.user import User
from config.database import db
from utils.password import get_password_hash, verify_password
from datetime import datetime
import uuid

verifier = APIRouter()


@verifier.post('/register', tags=["verifier"])
async def create_verifier(admin: Verifier):
    admin.password = get_password_hash(admin.password)
    admin.isActive = True
    admin.createdAt = datetime.utcnow()
    admin.lastLogin = datetime.utcnow()
    record = await db.verifiers.find_one({"username": admin.username})
    admin.api_key = str(uuid.uuid4())
    admin = dict(admin)
    if record is None:
        await db.verifiers.insert_one(admin)
        return admin
    return {"success": False, "error": "UserName Already Exists"}


@verifier.post('/login',
               response_model=Verifier,
               response_model_exclude={"password"},
               tags=["verifier"])
async def login_verifier(login: LoginVerifier):
    user = await db.verifiers.find_one({"username": login.username})
    if user:
        if verify_password(login.password, user.get("password")):
            admin = await db.verifiers.find_one_and_update(
                {"username": login.username},
                {"$set": {"lastLogin": datetime.utcnow()}})
            return admin
        raise HTTPException(status_code=401, detail="Invalid Password")
    raise HTTPException(status_code=401, detail="Username Does Not Exist")


@verifier.get('/api/{api_key}',
              response_model=User,
              response_model_include={"eKYCXML", "name", "dob", "gender"},
              tags=["verifier"])
async def fetch_user(api_key: str, vid: str):
    verifier = await db.verifiers.find_one({"api_key": api_key})
    if verifier:
        user = await db.users.find_one({"vid": vid})
        if user:
            vlog = VerifierInfo(
                vid=vid,
                api_key=api_key,
                name=verifier.get("name"),
                accessedAt=datetime.utcnow()
            )
            await db.access_logs.insert_one(vlog)
            return user
        return HTTPException(status_code=401, detail="User Does Not Exist")
    return HTTPException(status_code=401, detail="Invalid API Key")
