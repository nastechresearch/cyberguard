# 🔐 CyberGuard

**Your phone. Protected.**

An Android phone protection app — antivirus, anti-theft, app lock, privacy shield, web & network security, performance optimization, and identity protection. **69 features** in one AMOLED-black security suite.

## ✨ Features

| Category | Features | Count |
|----------|----------|-------|
| 🦠 Antivirus & Malware | App scanner, quick/full scans, heuristic engine, quarantine, scheduled scans & more | 15 |
| 🛡️ Anti-Theft & Device | Remote locate/lock/wipe, mugshot capture, SIM alerts, panic button & more | 13 |
| 🔐 App Lock & Access | PIN, pattern, biometric lock, selective locking, settings protection & more | 9 |
| 👁️ Privacy Protection | Permission audit, camera/mic monitor, stalkerware detection, photo vault & more | 10 |
| 🌐 Web & Scam | Safe browsing, anti-phishing, link/call/sms filtering, payment protection & more | 9 |
| 📡 Network & Wi-Fi | Wi-Fi scanner, rogue AP detection, home monitor, speed test & more | 6 |
| ⚡ Performance | Junk cleaner, duplicate finder, battery saver, startup manager & more | 5 |
| 🔑 Identity & Breach | Data breach checker, password analyzer | 2 |

**Total: 69 features**

## 🎨 Design

Pure **AMOLED black** theme (`#000000`) with neon cyan/purple accents. Battery-friendly, high-contrast, dark-only. Full spec: [`docs/01-design-system.md`](docs/01-design-system.md).

## 🧰 Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM + Clean Architecture
- **DI:** Hilt
- **Database:** Room
- **Background:** WorkManager + Foreground Services
- **Auth:** BiometricPrompt
- **Device Control:** Device Admin API

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest)
- JDK 17+
- Android SDK 35
- A device or emulator (API 26+)

### Build
```bash
./gradlew assembleDebug
```

### Test
```bash
./gradlew test
```

## 📁 Project Structure

```
app/src/main/java/com/nastech/nia/
├── ui/          # Compose screens & theme
├── data/        # Room, repositories
├── domain/      # Use cases
├── service/     # Background engines
├── receiver/    # Broadcast receivers
├── worker/      # WorkManager workers
├── di/          # Hilt modules
├── util/        # Helpers
└── core/        # Core engine
```

## 🗺️ Roadmap

| Phase | Focus | Duration |
|-------|-------|----------|
| 1 | Foundation + App Lock | Weeks 1–3 |
| 2 | Antivirus Engine | Weeks 4–6 |
| 3 | Anti-Theft | Weeks 7–9 |
| 4 | Privacy Shield | Weeks 10–11 |
| 5 | Web & Network | Weeks 12–14 |
| 6 | Identity | Week 15 |
| 7 | Polish & Release | Weeks 16–18 |

## 📄 License

MIT