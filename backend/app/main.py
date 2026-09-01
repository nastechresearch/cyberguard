"""FastAPI application factory for CyberGuard backend."""

from fastapi import FastAPI

from app.backends import devices, health
from app.core.config import settings


def create_app() -> FastAPI:
    """Build and configure the FastAPI application."""
    app = FastAPI(title=settings.app_name, version=settings.version)
    app.include_router(health.router)
    app.include_router(devices.router)
    return app


app = create_app()