"""Tests for CyberGuard backend endpoints."""

import pytest
from fastapi.testclient import TestClient

from app.backends import devices
from app.main import create_app


@pytest.fixture()
def client() -> TestClient:
    """Provide a test client with a clean in-memory registry."""
    devices._registry.clear()
    return TestClient(create_app())


def test_health_ok(client: TestClient) -> None:
    resp = client.get("/health")
    assert resp.status_code == 200
    body = resp.json()
    assert body["service"] == "cyberguard-backend"
    assert body["status"] == "ok"


def test_version_ok(client: TestClient) -> None:
    resp = client.get("/version")
    assert resp.status_code == 200
    assert resp.json()["version"] == "0.1.0"


def test_register_device_returns_token(client: TestClient) -> None:
    resp = client.post("/devices/register", json={"device_id": "device12345"})
    assert resp.status_code == 201
    body = resp.json()
    assert body["registered"] is True
    assert len(body["sync_token"]) >= 32


def test_register_requires_min_length(client: TestClient) -> None:
    resp = client.post("/devices/register", json={"device_id": "short"})
    assert resp.status_code == 422


def test_sync_rejects_unregistered(client: TestClient) -> None:
    resp = client.post(
        "/devices/sync",
        json={"device_id": "device12345", "items": [{"kind": "scan", "payload": {"n": 1}}]},
    )
    assert resp.status_code == 401


def test_sync_accepts_registered(client: TestClient) -> None:
    client.post("/devices/register", json={"device_id": "device12345"})
    resp = client.post(
        "/devices/sync",
        json={
            "device_id": "device12345",
            "items": [{"kind": "scan", "payload": {"n": 1}}, {"kind": "wifi"}],
        },
    )
    assert resp.status_code == 200
    body = resp.json()
    assert body["accepted"] == 2
    assert body["total"] == 2