# 🎨 CyberGuard — AMOLED Black Design System

> **Design spec for the pure-black UI theme. Battery-friendly, modern, high-contrast.**

---

## 1. Design Philosophy

- **Pure black backgrounds** (true `#000000`) — pixels turned off on OLED = battery savings
- **Neon accents** that pop on black (cyan, purple, red, green)
- **Dark-elevation surfaces** via `#121212` bleed for depth
- **High contrast text** for accessibility
- **Smooth glow effects** with Material 3 tonal overlays
- **Glassmorphism touches** where supported

---

## 2. Color Palette

### AMOLED Core Colors

| Token | Color | Hex | Usage |
|-------|-------|-----|-------|
| `background` | Pure Black | `#000000` | Main screen background |
| `surface` | Dark Gray | `#121212` | Cards, sheets, drawers |
| `surfaceVariant` | Elevated | `#1E1E1E` | Elevated cards, inputs |
| `onBackground` | White | `#FFFFFF` | Primary text on black |
| `onSurface` | White | `#FFFFFF` | Text on surfaces |
| `textSecondary` | Soft Gray | `#B3B3B3` | Secondary/labels |
| `outline` | Dim Gray | `#2A2A2A` | Borders, dividers |
| `disabled` | Dark Gray | `#4D4D4D` | Disabled elements |

### Neon Accent Colors

| Token | Color | Hex | Usage |
|-------|-------|-----|-------|
| `primary` | Neon Cyan | `#00E5FF` | Main brand, active states, links |
| `primaryContainer` | Cyan 10% | `#00E5FF1A` | Tinted chips, selected states |
| `accent` | Electric Purple | `#9F00FF` | Highlights, gradients |
| `success` | Neon Green | `#00E676` | Safe, scan passed, secure |
| `warning` | Amber | `#FFAB00` | Medium risk, mild threats |
| `danger` | Neon Red | `#FF1744` | Threats, critical, block |
| `info` | Neon Blue | `#2979FF` | Informational |

### Gradient Definitions

| Name | Gradient | Usage |
|------|----------|-------|
| `cyberGradient` | `#00E5FF → #9F00FF` | Hero card, logo, scan button |
| `dangerGradient` | `#FF1744 → #9F00FF` | Threat alerts, emergency |
| `successGradient` | `#00E676 → #00E5FF` | All-clear, scan complete |
| `surfaceGradient` | `#121212 → #000000` | Card depth |

---

## 3. Typography

| Style | Font | Size | Weight | Usage |
|-------|------|------|--------|-------|
| Display | Sora / Poppins | 34sp | Bold | Dashboard title |
| Headline | Poppins | 24sp | SemiBold | Screen titles |
| Title | Poppins | 18sp | Medium | Card headers |
| Body | Inter | 14sp | Regular | Content |
| Label | Inter | 12sp | Medium | Chips, badges |
| Caption | Inter | 11sp | Regular | Footnotes |

**Recommended fonts (free, offline):**
- **Sora** — techy geometric (display)
- **Poppins** — clean modern
- **Inter** — readable body

> **Font files:** place TTF/OTF in `app/src/main/res/font/`, reference via Compose `FontFamily`.

---

## 4. Layout & Spacing

| Token | dp |
|-------|----|
| spacing-xs | 4dp |
| spacing-sm | 8dp |
| spacing-md | 16dp |
| spacing-lg | 24dp |
| spacing-xl | 32dp |
| screen-margin | 20dp |
| card-padding | 16dp |
| card-corner | 16dp |
| button-corner | 12dp |
| icon-size | 24dp |

### Elevation (surfaces)
| Level | Surface Color | Alpha |
|-------|---------------|-------|
| 0 | `#000000` | 1.0 |
| 1 | `#121212` | 1.0 |
| 2 | `#1A1A1A` | 1.0 |
| 3 | `#1E1E1E` | 1.0 |

---

## 5. Component Specifications

### Bottom Navigation
```
Active: Neon Cyan icon + label, subtle glow
Inactive: Gray #B3B3B3
Background: #121212 with top border #2A2A2A
Height: 64dp + navigation bar inset
```

### Dashboard Hero Card
```
Gradient: cyberGradient (cyan→purple)
Security Score: giant number in white, glow ring
Status text: "Protected" in green / "Action needed" in red
```

### Feature Cards
```
Surface: #121212, corner 16dp, thickness elevation 1
Icon: neon color on #00E5FF1A tinted circle
Title: white, body: #B3B3B3
State badge: green check / amber alert / red threat
```

### Scan Progress
```
Circular ring: cyan gradient
Background track: #1E1E1E
Center text: white percentage + label
```

### Permission Items
```
Icon colored: granted=cyan, runFormat=amber, denied=red
Toggle switch: Material 3, cyan when on
Risky permission badge: red outline pill
```

### Buttons
```
Primary: cyberGradient background, white text, corner 12dp
Secondary: #1E1E1E surface, cyan border, white text
Danger: dangerGradient background
```

### App Lock Screen
```
Full black overlay, lock icon in cyan glow,
PIN dots in cyan, pattern grid cyan on black,
Biometric prompt with fingerprint icon
```

### Anti-Theft Dashboard
```
Alarm / Lock / Locate / Wipe — 4 neon action buttons in 2×2 grid
Status banner green "Armed" / red "Disarmed"
Map panel: dark cards
```

---

## 6. Dark Mode Mapping (Compose)

```kotlin
private val AmoledDarkColors = darkColorScheme(
    primary = Cyan,
    onPrimary = Black,
    primaryContainer = Cyan10,
    onPrimaryContainer = Cyan,
    secondary = Purple,
    onSecondary = White,
    background = PureBlack,
    onBackground = White,
    surface = DarkSurface,
    onSurface = White,
    surfaceVariant = ElevatedSurface,
    surfaceContainer = DarkSurface,
    surfaceContainerHigh = ElevatedSurface,
    surfaceContainerHighest = #2A2A2A,
    onSurfaceVariant = TextSecondary,
    outline = DimGray,
    error = NeonRed,
    onError = White,
)

val AmoledLightColors = AmoledDarkColors.drop(background) // force dark always
```

> **Design decision:** App is **dark-only** — no light theme. AMOLED identity is the brand.

---

## 7. Icon & Branding

### Launcher Icon
```
Shape: rounded square
Background: pure black gradient (#000000 → #0A0A0A)
Core: neon cyan shield silhouette
Accent: purple lightning bolt through shield
Glow: outer cyan glow ring
```

### Splash Screen (Android 12+)
```
Background: black
Logo: shield with cyan glow zoom/fade animation
Text: CYBERGUARD in Poppins bold below logo
```

### Status Bar
```
Transparent, icons in white/cyan
Navigation bar: black
```

---

## 8. Motion & Animation

| Element | Animation |
|---------|-----------|
| Screen transitions | Crossfade + slide (120ms–200ms) |
| Scan animation | Rotating radar rings + glow pulse |
| Security score | Number count-up (500ms) |
| Status change | Color morph green↔red |
| Lock overlay | Fast clip + fade (150ms) |
| Threat alert | Red pulse + vibration |

---

## 9. Accessibility

- Contrast > 4.5:1 for body text (white on black = 21:1 ✅)
- Neon colors used sparingly, never for long-form text
- All interactive elements ≥ 48dp touch target
- Support `Monotype`-style dynamic font scale
- Content descriptions on all icons
- Notifications vibrate + sound options

---

## 10. Asset Checklist

```
res/font/sora_regular.ttf, sora_bold.ttf
res/font/poppins_regular.ttf, poppins_semibold.ttf, poppins_bold.ttf
res/font/inter_regular.ttf, inter_medium.ttf
res/drawable/ic_launcher_foreground.xml (shield vector)
res/mipmap-anydpi-v26/ic_launcher.xml
res/values/colors.xml (raw color definitions)
res/values/themes.xml (splash + activity theme)
res/values/strings.xml (all UI strings, app name)
```