"""Pydantic schemas for backend requests and responses."""

from typing import Any, Optional

from pydantic import BaseModel, Field


class ServiceInfo(BaseModel):
    """Generic status payload."""

    service: str
    status: str
    version: Optional[str] = None


class DeviceRegisterIn(BaseModel):
    """Registration payload sent by the Android app."""

    device_id: str = Field(min_length=8, max_length=128)
    name: Optional[str] = Field(default=None, max_length=128)
    platform: str = "android"


class DeviceRegisterOut(BaseModel):
    """Result of device registration."""

    device_id: str
    registered: bool
    sync_token: str


class SyncItem(BaseModel):
    """A single typed device-context item pushed for sync."""

    kind: str = Field(max_length=64)
    payload: Any = None
    at: Optional[str] = None


class SyncBatchIn(BaseModel):
    """Batch of context items from a device."""

    device_id: str = Field(min_length=8, max_length=128)
    items: list[SyncItem] = Field(default_factory=list)


class SyncBatchOut(BaseModel):
    """Result of accepting a sync batch."""

    device_id: str
    accepted: int
    total: int