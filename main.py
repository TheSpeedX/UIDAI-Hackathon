from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import RedirectResponse
from routes.auth import auth
from routes.user import user
from routes.verifier import verifier

app = FastAPI(title="UIDAI Application")

origins = ['*']

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/", response_class=RedirectResponse)
async def home():
    return "/docs"


app.include_router(auth, prefix="/user")
app.include_router(user, prefix="/user")
app.include_router(verifier, prefix="/verifier")
