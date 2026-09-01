# 🔐 CyberGuard — Master Plan

> **Complete Android Phone Protection App plan — 69 features, AMOLED black design, full git strategy.**

| Field | Value |
|-------|-------|
| **App Name** | CyberGuard |
| **Project/Folder** | `NIA` |
| **Package Name** | `com.nastech.nia` |
| **Git Repo** | `github.com/nastech/cyberguard` |
| **Platform** | Android (minSdk 26, targetSdk 35) |
| **Language** | Kotlin 2.x |
| **UI** | Jetpack Compose + Material 3 |
| **UI Style** | **AMOLED Pure Black** theme |
| **Architecture** | MVVM + Clean Architecture |
| **DI** | Hilt |
| **Database** | Room |
| **Background** | WorkManager + Foreground Service |
| **Target Audience** | Beginner learning project |
| **Goal** | Full security suite — antivirus, anti-theft, app lock, privacy |

---

## 1. App Identity & Branding

| Item | Value |
|------|-------|
| App Name | **CyberGuard** |
| Tagline | *"Your phone. Protected."* |
| Package | `com.nastech.nia` |
| Repo Name | `cyberguard` |
| Icon Theme | Neon cyan shield on pure black |
| Primary Color | Neon Cyan `#00E5FF` |
| Accent Color | Electric Purple `#A020F0` / `#9F00FF` |
| Danger Color | Neon Red `#FF1744` |
| Success Color | Neon Green `#00E676` |
| Background | AMOLED Pure Black `#000000` |
| Surface | `#121212` (Material elevation) |
| Text Primary | `#FFFFFF` |
| Text Secondary | `#B3B3B3` |

---

## 2. The 69 Features

### 🦠 ANTIVIRUS & MALWARE (Features 1–15)
| # | Feature | Difficulty |
|---|---------|------------|
| 1 | App Scanner — scan all installed apps | Medium |
| 2 | Quick Scan — one-tap running apps | Easy |
| 3 | Full Device Scan — storage + SD card | Hard |
| 4 | On-Install Scanner — auto-scan on install | Medium |
| 5 | File/Folder Scanner — scan specific paths | Easy |
| 6 | Signature-Based Detection — hash database | Medium |
| 7 | Heuristic Analysis — behavioral/AI engine | Hard |
| 8 | Adware Detector — aggressive ad apps | Medium |
| 9 | PUA Detection — potentially unwanted apps | Medium |
| 10 | Quarantine System — isolate, don't delete | Medium |
| 11 | Malware Removal — safe uninstall guidance | Medium |
| 12 | Scheduled Scans — auto via WorkManager | Easy |
| 13 | Definition Updates — refresh signatures | Medium |
| 14 | Scan History & Reports — audit log | Easy |
| 15 | Security Score Dashboard — health score | Easy |

### 🛡️ ANTI-THEFT & DEVICE PROTECTION (Features 16–28)
| # | Feature | Difficulty |
|---|---------|------------|
| 16 | Remote Locate — GPS on map | Medium |
| 17 | Remote Lock — Device Admin lock | Medium |
| 18 | Remote Wipe — erase data remotely | Hard |
| 19 | Mugshot Capture — front camera on fail | Medium |
| 20 | SIM Change Alert — notify owner | Medium |
| 21 | Remote Alarm — loud ring remotely | Easy |
| 22 | Remote Message — text on lock screen | Easy |
| 23 | Location History — movement trail | Medium |
| 24 | Panic Button — alert emergency contacts | Easy |
| 25 | Theft Prevention Mode — motion auto-lock | Hard |
| 26 | Intruder Selfie — photo on wrong PIN | Medium |
| 27 | Device Admin Control — remote management | Medium |
| 28 | Web Dashboard — manage via web | Hard |

### 🔐 APP LOCK & ACCESS CONTROL (Features 29–37)
| # | Feature | Difficulty |
|---|---------|------------|
| 29 | PIN Lock — numeric unlock | Easy |
| 30 | Pattern Lock — draw pattern | Easy |
| 31 | Fingerprint Unlock — biometric | Medium |
| 32 | Face Unlock — face recognition | Medium |
| 33 | Auto-Lock on Launch — every open | Easy |
| 34 | Selective App Lock — pick apps | Easy |
| 35 | Time-Based Unlock — timed session | Easy |
| 36 | Lock Settings Protection — guard settings | Medium |
| 37 | PIN Recovery — reset via account | Easy |

### 👁️ PRIVACY PROTECTION (Features 38–47)
| # | Feature | Difficulty |
|---|---------|------------|
| 38 | Privacy Advisor — permission audit | Medium |
| 39 | Permission Manager — view/revoke | Easy |
| 40 | Camera/Mic Monitor — access tracking | Medium |
| 41 | Stalkerware Detection — hidden spies | Hard |
| 42 | Bluetooth Tracker Detector — AirTag alert | Hard |
| 43 | App Data Usage Monitor — traffic map | Medium |
| 44 | Notification Privacy — hide content | Easy |
| 45 | Photo Vault — encrypted gallery | Medium |
| 46 | File Vault — encrypted documents | Medium |
| 47 | Clipboard Cleaner — auto-clear | Easy |

### 🌐 WEB & SCAM PROTECTION (Features 48–56)
| # | Feature | Difficulty |
|---|---------|------------|
| 48 | Safe Browsing Shield — block phishing | Hard |
| 49 | Anti-Phishing — fake login detection | Medium |
| 50 | Link Scanner — SMS/chat URL scan | Medium |
| 51 | Call Blocker — spam call blocking | Medium |
| 52 | SMS Spam Filter — phishing texts | Medium |
| 53 | Secure QR Scanner — check before open | Easy |
| 54 | Payment Protection — bank overlay | Medium |
| 55 | URL Typo Fixer — fix misspelled links | Easy |
| 56 | Scam Alerts Feed — live warnings | Easy |

### 📡 NETWORK & WI-FI (Features 57–62)
| # | Feature | Difficulty |
|---|---------|------------|
| 57 | Wi-Fi Scanner — network risk analysis | Medium |
| 58 | Unsecured Network Alert — open Wi-Fi | Easy |
| 59 | Network Inspector — attack detection | Hard |
| 60 | Smart Home Monitor — device list | Medium |
| 61 | Rogue AP Detection — evil-twin alert | Hard |
| 62 | Wi-Fi Speed Test — performance check | Easy |

### ⚡ PERFORMANCE & OPTIMIZATION (Features 63–67)
| # | Feature | Difficulty |
|---|---------|------------|
| 63 | Junk File Cleaner — cache/temp clean | Easy |
| 64 | Duplicate Photo Finder — dedupe media | Medium |
| 65 | Battery Saver Scanner — drain detection | Medium |
| 66 | Weak Settings Detector — insecure config | Medium |
| 67 | Startup Manager — boot app control | Easy |

### 🔑 IDENTITY & BREACH (Features 68–69)
| # | Feature | Difficulty |
|---|---------|------------|
| 68 | Data Breach Checker — hacked accounts | Medium |
| 69 | Password Strength Analyzer — weak check | Easy |

---

## 3. Development Phases

| Phase | Content | Features | Duration |
|-------|---------|----------|----------|
| Phase 1 | Foundation: Project, Theme, Dashboard, App Lock | 15, 29–37, 63–67 | Weeks 1–3 |
| Phase 2 | Antivirus Engine | 1–15 | Weeks 4–6 |
| Phase 3 | Anti-Theft | 16–28 | Weeks 7–9 |
| Phase 4 | Privacy | 38–47 | Weeks 10–11 |
| Phase 5 | Web & Network | 48–62 | Weeks 12–14 |
| Phase 6 | Identity | 68–69 | Week 15 |
| Phase 7 | Polish, Tests, Release | All | Weeks 16–18 |

---

## 4. Tech Stack & Dependencies

```kotlin
[versions]
kotlin = "2.1.0"
composeBom = "2025.01.00"
room = "2.6.1"
hilt = "2.53.1"
work = "2.10.0"
navigation = "2.8.5"
biometric = "1.2.0"

[libraries]
material3 = compose-bom
compose-ui = compose-bom
compose-icons-extended = compose-bom
room-runtime / room-ktx / room-compiler
hilt-android / hilt-compiler / hilt-navigation-compose
work-runtime-ktx
navigation-compose
biometric
camera-core / camera-camera2 / camera-lifecycle
gson / retrofit / okhttp
```

---

## 5. Architecture

```
UI Layer (Compose)
   │  StateFlow / ViewModel
Domain Layer (UseCases)
   │  Repository interfaces
Data Layer (Room, Repos, Engines)
   │
Services: Scanner, AppLock, AntiTheft, Wifi
Workers: ScheduledScan, DefinitionUpdate
Receivers: Boot, Install, SmsCommand
DI: Hilt modules (App, Database, Repository, Service)
```

```
app/src/main/java/com/nastech/nia/
├── CyberGuardApp.kt
├── MainActivity.kt
├── ui/        { theme, screens, components, navigation }
├── data/      { local, repository, model }
├── domain/    { usecase, model }
├── service/   { scanner, antitheft, applock, privacy, network }
├── receiver/  { BootReceiver, InstallReceiver, SmsCommandReceiver }
├── worker/    { ScheduledScanWorker, DefinitionUpdateWorker }
├── di/        { AppModule, DatabaseModule, RepositoryModule, ServiceModule }
├── util/      { Constants, Extensions, PermissionHelper, CryptoUtils, NotificationHelper }
└── core/      { SecurityEngine, DeviceInfo }
```

---

## 6. Git Strategy

### Branching
```
main (protected)
└── develop
    ├── feature/phase1-foundation
    ├── feature/phase2-antivirus
    ├── feature/phase3-antitheft
    ├── feature/phase4-applock
    ├── feature/phase5-privacy
    └── feature/phase6-identity
```

### Commit Convention
```
feat:      new feature
fix:       bug fix
docs:      documentation
refactor:  code cleanup
style:     formatting
test:      tests
chore:     build/tooling
```

### Release Tags
```
v0.1.0     Phase 1 complete
v0.2.0     Phase 2 complete
...        (bump minor per phase)
v1.0.0     Full 69-feature release
```

---

## 7. Android Permissions

```xml
<!-- Antivirus -->
QUERY_ALL_PACKAGES, READ_EXTERNAL_STORAGE

<!-- Anti-Theft -->
RECEIVE_BOOT_COMPLETED, READ_SMS, SEND_SMS,
ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, CAMERA

<!-- App Lock -->
SYSTEM_ALERT_WINDOW, USE_BIOMETRIC, PACKAGE_USAGE_STATS

<!-- Network -->
ACCESS_WIFI_STATE, CHANGE_WIFI_STATE

<!-- System -->
POST_NOTIFICATIONS, FOREGROUND_SERVICE, INTERNET
```

---

## 8. Deliverables per Phase

Each phase ships:
- ✅ Working features
- ✅ Unit tests for core logic
- ✅ UI screens in AMOLED black theme
- ✅ Git tag + change log entry
- ✅ Updated README