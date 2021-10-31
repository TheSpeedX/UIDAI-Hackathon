from pydantic import BaseModel, Field
from datetime import datetime
from typing import Optional


class Verifier(BaseModel):
    username: str = Field(max_length=64)
    password: str = Field(max_length=255)
    name: str = Field(max_length=255)
    api_key: Optional[str] = Field(max_length=255)
    isActive: bool = True
    createdAt: datetime = datetime.utcnow()
    lastLogin: datetime = datetime.utcnow()


class LoginVerifier(BaseModel):
    username: str = Field(max_length=64)
    password: str = Field(max_length=255)


class VerifierInfo(BaseModel):
    name: str = Field(max_length=255)
    api_key: str = Field(max_length=255)
    accessedAt: datetime = datetime.utcnow()
    vid: str = Field(max_length=255)
