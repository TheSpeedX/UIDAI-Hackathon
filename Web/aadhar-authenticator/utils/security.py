import hashlib
import hmac
import os
import secrets


def generate_api_key() -> str:
    """Generate a URL-safe verifier API key."""
    return secrets.token_urlsafe(32)


def hash_api_key(api_key: str) -> str:
    """Store only a deterministic SHA-256 digest of an API key."""
    return hashlib.sha256(api_key.encode("utf-8")).hexdigest()


def verify_api_key(api_key: str, digest: str) -> bool:
    """Compare an API key digest using constant-time comparison."""
    return hmac.compare_digest(hash_api_key(api_key), digest)


def mask_identifier(value: str | None, visible: int = 4) -> str | None:
    """Mask identifiers in logs and API responses."""
    if value is None:
        return None
    if len(value) <= visible:
        return "*" * len(value)
    return "*" * (len(value) - visible) + value[-visible:]


def get_or_create_request_id(request_id: str | None = None) -> str:
    return request_id or secrets.token_hex(16)


def constant_time_equal(left: str, right: str) -> bool:
    return hmac.compare_digest(left.encode("utf-8"), right.encode("utf-8"))
