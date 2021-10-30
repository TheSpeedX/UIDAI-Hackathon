from pydantic import BaseModel, Field
from datetime import datetime
from typing import Optional
from config.variables import SAMPLE_XML

class User(BaseModel):
    username: str = Field(max_length=64)
    password: str = Field(max_length=255)
    eKYCXML: str = Field()
    name: Optional[str] = Field()
    dob: Optional[str] = Field()
    gender: Optional[str] = Field(min_length=1, max_length=1)
    isActive: bool = True
    createdAt: datetime = datetime.utcnow()
    lastLogin: datetime = datetime.utcnow()

    class Config:
        allow_population_by_field_name = True
        schema_extra = {
            "example": {
                "username": "1234",
                "password": "12345",
                "eKYCXML": SAMPLE_XML
            }
        }


class LoginUser(BaseModel):
    username: str = Field(max_length=64)
    password: str = Field(max_length=255)

    class Config:
        schema_extra = {
            "example": {
                "username": "device Id",
                "password": "Virtual ID"
            }
        }


class UpdateUser(BaseModel):
    username: Optional[str] = Field(max_length=64)
    password: Optional[str] = Field(max_length=255)
    eKYCXML: Optional[str] = Field()

    class Config:
        schema_extra = {
            "example": {
                "username": "Device ID",
                "eKYCXML": "eKYCXML Data"
            }
        }
