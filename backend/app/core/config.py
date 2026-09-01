"""Application settings for the CyberGuard backend."""

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    """Runtime configuration loaded from environment or .env."""

    app_name: str = "CyberGuard Backend"
    version: str = "0.1.0"

    model_config = SettingsConfigDict(env_prefix="CYBERGUARD_", env_file=".env", extra="ignore")


settings = Settings()