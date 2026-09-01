"""Health and version endpoints."""

from fastapi import APIRouter

from app.backends.schemas import ServiceInfo

router = APIRouter(tags=["system"])


@router.get("/health", response_model=ServiceInfo)
def health() -> ServiceInfo:
    """Lightweight liveness probe consumed by the Android app and monitors."""
    return ServiceInfo(service="cyberguard-backend", status="ok")


@router.get("/version", response_model=ServiceInfo)
def version() -> ServiceInfo:
    """Version + status probe used during app->backend pairing."""
    return ServiceInfo(service="cyberguard-backend", status="ok", version="0.1.0")