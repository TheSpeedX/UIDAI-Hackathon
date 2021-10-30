from dotenv import load_dotenv
import os

load_dotenv()

MONGO_CONFIG = {
    "HOST": os.getenv("MONGO_HOST"),
    "DBNAME": os.getenv("MONGO_DBNAME"),
}

JWT_CONFIG = {
    "SECRET_KEY": os.getenv("SECRET_KEY"),
    "ALGORITHM": "HS256",
    "ACCESS_TOKEN_EXPIRE_MINUTES": 60,
    "REFRESH_TOKEN_EXPIRE_MINUTES": 360,
    "GUEST_TOKEN_EXPIRE_MINUTES": 360
}

SAMPLE_XML = '<OfflinePaperlessKyc referenceId="00000"></OfflinePaperlessKyc>'
