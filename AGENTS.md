# CyberGuard — Agent Instructions

Guidelines for AI coding agents working in this repository.

## Stack
- Kotlin + Jetpack Compose (Material 3)
- Hilt for DI, Room for persistence
- MVVM + Clean Architecture
- Dark-only AMOLED theme (ID is `#000000`)

## Conventions
- Package root: `com.nastech.nia`
- Every feature = UI screen + ViewModel + UseCase + Repository + Room entity
- All UI must use the AMOLED design system (docs/01-design-system.md)
- No light theme, ever.
- No code comments unless asked.
- Prefer Compose Material3 over legacy XML widgets.
- Use StateFlow in ViewModels, collectAsStateWithLifecycle in UI.

## Build & Verify
```bash
./gradlew assembleDebug       # build
./gradlew testDebugUnitTest   # unit tests
./gradlew lintDebug           # lint
```

Always run build + tests after changes.

## Git
- Branch: `feature/...` off `develop`, PR into `develop`
- Commit prefix: `feat:`, `fix:`, `docs:`, `refactor:`, `chore:`, `test:`
- Never commit directly to `main`

## Design Tokens (quick ref)
| Token | Value |
|-------|-------|
| background | #000000 |
| surface | #121212 |
| primary | #00E5FF |
| accent | #9F00FF |
| success | #00E676 |
| danger | #FF1744 |

## Docs
- `docs/00-master-plan.md` — roadmap & features
- `docs/01-design-system.md` — AMOLED theme spec
- `docs/02-git-plan.md` — git/CI workflow