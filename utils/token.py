from fastapi import Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer
from datetime import datetime, timedelta
from config.variables import JWT_CONFIG
from config.database import db
# from main import oauth2_scheme
import jwt


oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/token")


credentials_exception = HTTPException(
    status_code=401,
    detail="Invalid Token Sent",
    headers={"WWW-Authenticate": "Bearer"},
)


def create_token(data: dict, expires_in: int = 30):
    to_encode = data.copy()
    now = datetime.utcnow()
    expire = now + timedelta(minutes=expires_in)
    to_encode.update({"exp": expire, "iat": now})
    return jwt.encode(
        to_encode,
        JWT_CONFIG["SECRET_KEY"],
        algorithm=JWT_CONFIG["ALGORITHM"])


def generate_guest_access_token(username: str):
    return create_token(
        {"guest": username},
        expires_in=JWT_CONFIG["GUEST_TOKEN_EXPIRE_MINUTES"])


def generate_access_token(username: str):
    return create_token(
        {"user_id": username},
        expires_in=JWT_CONFIG["ACCESS_TOKEN_EXPIRE_MINUTES"])


def generate_refresh_token(username: str):
    return create_token(
        {"sub": username},
        expires_in=JWT_CONFIG["REFRESH_TOKEN_EXPIRE_MINUTES"])


async def decode_access_token(token: str):
    payload = jwt.decode(
        token,
        JWT_CONFIG["SECRET_KEY"],
        algorithms=JWT_CONFIG["ALGORITHM"])
    username: str = payload.get("user_id")
    if username is None:
        raise credentials_exception
    return username


async def get_new_access_token(refresh_token: str = Depends(oauth2_scheme)):
    try:
        payload = jwt.decode(
            refresh_token,
            JWT_CONFIG["SECRET_KEY"],
            algorithms=JWT_CONFIG["ALGORITHM"])
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
        return generate_access_token(username)
    except jwt.exceptions.ExpiredSignatureError:
        raise HTTPException(status_code=403, detail="Token Expired")
    except Exception:
        raise credentials_exception


async def get_current_user(token: str = Depends(oauth2_scheme)):
    try:
        username = await decode_access_token(token)
    except jwt.exceptions.ExpiredSignatureError:
        raise HTTPException(status_code=403, detail="Token Expired")
    except Exception:
        raise credentials_exception
    user = await db.users.find_one({"username": username})
    if user is None:
        raise credentials_exception
    return user


async def get_current_username(token: str = Depends(oauth2_scheme)):
    try:
        return await decode_access_token(token)
    except jwt.exceptions.ExpiredSignatureError:
        raise HTTPException(status_code=403, detail="Token Expired")
    except Exception:
        raise credentials_exception


async def get_optional_username(token: str = Depends(oauth2_scheme)):
    payload = jwt.decode(
        token,
        JWT_CONFIG["SECRET_KEY"],
        algorithms=JWT_CONFIG["ALGORITHM"])
    username: str = payload.get("user_id")
    tokenType = "user"
    if username is None:
        username: str = payload.get("guest")
        tokenType = "guest"
    if username is None:
        raise credentials_exception
    return {"id": username, "tokenType": tokenType}
