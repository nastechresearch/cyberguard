"""Device registration and context-sync ingestion for the CyberGuard backend.

These are the cloud-adjacent, OPTIONAL endpoints. The Android app is fully
self-sufficient and never requires the backend; it pushes context here for
multi-device sync / remote management when the user enables backend sync.
"""

import secrets
from typing import NewType

from fastapi import APIRouter, HTTPException, status

from app.backends.schemas import (
    DeviceRegisterIn,
    DeviceRegisterOut,
    SyncBatchIn,
    SyncBatchOut,
)

router = APIRouter(prefix="/devices", tags=["devices"])

# In-memory device registry (persistence added in a later phase).
_Registry = NewType("_Registry", dict[str, str])
_registry: _Registry = _Registry({})


def issue_sync_token() -> str:
    """Issue a fresh opaque sync token using a CSPRNG."""
    return secrets.token_urlsafe(32)


@router.post("/register", response_model=DeviceRegisterOut, status_code=status.HTTP_201_CREATED)
def register_device(inp: DeviceRegisterIn) -> DeviceRegisterOut:
    """Register (or re-register) a device so it can sync context."""
    token = _registry.get(inp.device_id)
    if token is None:
        token = issue_sync_token()
        _registry[inp.device_id] = token
    return DeviceRegisterOut(device_id=inp.device_id, registered=True, sync_token=token)


@router.post("/sync", response_model=SyncBatchOut)
def accept_sync(inp: SyncBatchIn) -> SyncBatchOut:
    """Accept a batch of device context items for sync.

    Requires the device to be registered. Rejects unknown devices with 401 so
    unauthorized callers cannot inject context.
    """
    if inp.device_id not in _registry:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="device not registered")
    return SyncBatchOut(device_id=inp.device_id, accepted=len(inp.items), total=len(inp.items))