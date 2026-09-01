# 🌿 CyberGuard — Git & Repository Setup Plan

> **Version control strategy for the `NIA` project. Hosted on GitHub.**

---

## 1. Repository Overview

| Field | Value |
|-------|-------|
| Repo Name | `cyberguard` |
| Owner | `nastech` |
| URL | `https://github.com/nastech/cyberguard` |
| Visibility | Public |
| License | MIT |
| Default Branch | `main` |
| Primary Dev | `develop` |

---

## 2. Repository Setup Commands

```bash
# From NIA project root
cd ~/NIA

# Initialize git
git init -b main

# Add remote
git remote add origin https://github.com/nastech/cyberguard.git

# Configure identity (one-time)
git config user.name  "nastech"
git config user.email "you@nastech.com"

# Create initial commit
git add .
git commit -m "chore: initial project scaffold with full 69-feature plan"

# Push main
git push -u origin main

# Create develop branch
git checkout -b develop
git push -u origin develop

# Protect main (GHzHauge.org settings)
# → Require PR before merge, require 1 approval
```

---

## 3. Branch Strategy

```
main          ← stable, tagged releases only
  └── develop ← integration branch, all PRs land here
       ├── feature/phase1-foundation     ← App Lock + Dashboard
       ├── feature/phase2-antivirus      ← Malware Scanner
       ├── feature/phase3-antitheft      ← Anti-Theft
       ├── feature/phase4-applock        ← App Lock enhancements
       ├── feature/phase5-privacy        ← Privacy Shield
       ├── feature/phase6-web-network    ← Web & Wi-Fi
       └── feature/phase7-performance    ← Optimization
```

### Workflow
1. Branch off `develop`: `git checkout -b feature/xyz develop`
2. Commit small changes with convention
3. Push, open PR → `develop`
4. After review + CI pass → merge
5. Releases: merge `develop` → `main`, tag `vX.Y.Z`

---

## 4. Commit Convention

| Prefix | Use |
|--------|-----|
| `feat:` | New feature |
| `fix:` | Bug fix |
| `docs:` | Documentation |
| `refactor:` | Cleanup / restructure |
| `style:` | Formatting, spacing |
| `test:` | Add/update tests |
| `chore:` | Build, deps, tooling |
| `perf:` | Performance |

### Recommended format
```
type(scope): short description

Example:
feat(applock): add biometric unlock to protected apps
fix(scanner): correct false-positive on system apps
docs(readme): add build instructions
```

---

## 5. Tagging / Releases

| Tag | Meaning |
|-----|---------|
| `v0.1.0` | Phase 1 foundation complete |
| `v0.2.0` | Phase 2 antivirus complete |
| `v0.3.0` | Phase 3 anti-theft complete |
| `v0.4.0` | Phase 4 privacy complete |
| `v0.5.0` | Phase 5 web/network complete |
| `v0.6.0` | Phase 6 identity complete |
| `v1.0.0` | Full 69-feature stable release |

```bash
git tag -a v0.1.0 -m "Phase 1: Foundation + App Lock"
git push origin v0.1.0
```

---

## 6. .gitignore (Key Entries)

```gitignore
# Android / Gradle
.gradle/
build/
local.properties
*.apk
*.aab
*.keystore
*.jks

# IDE
.idea/
*.iml
.DS_Store

# Logs
*.log

# Environment
captures/
.externalNativeBuild/
.cxx/
```

---

## 7. GitHub Configuration

### Repository Settings
- Protect `main`: require pull request review, 1 approval
- Require status checks pass (CI build)
- Enable branch auto-deletion after merge

### CI/CD (`.github/workflows/build.yml`)
```yaml
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [develop]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17
      - name: Grant execute permission
        run: chmod +x gradlew
      - name: Build APK
        run: ./gradlew assembleDebug
      - name: Run unit tests
        run: ./gradlew testDebugUnitTest
      - name: Upload APK artifact
        uses: actions/upload-artifact@v4
        with:
          name: debug-apk
          path: app/build/outputs/apk/debug/
```

### Release Workflow (`.github/release.yml`)
Tagged commit on `main` → auto-build signed release APK → GitHub Release draft with changelog.

---

## 8. README.md Outline

```markdown
# CyberGuard 🔐
Your phone. Protected.

## Features (69)
[table of all 69 features by category]

## Screenshots
[AMOLED dark UI images]

## Tech Stack
Kotlin, Compose, Hilt, Room, WorkManager

## Getting Started
Prereqs + build instructions

## Build
./gradlew assembleDebug

## Architecture
MVVM + Clean Architecture diagram

## Design System
Link to docs/01-design-system.md (AMOLED)

## Roadmap
Phase 1–7 timeline

## License
MIT
```

---

## 9. Docs Index

```
NIA/
├── docs/
│   ├── 00-master-plan.md       ← this repo's full roadmap
│   ├── 01-design-system.md     ← AMOLED black design
│   ├── 02-git-plan.md          ← this doc
├── .gitignore
├── README.md
├── LICENSE
└── AGENTS.md
```