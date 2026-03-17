# Dynamic Island v2

**Dynamic Island v2** is a focused Android app (Kotlin + Jetpack Compose) that delivers a practical iPhone-style Dynamic Island overlay experience on Android.

Developer: **Maks**

## What this app does

- Shows a top-center dark capsule overlay near the front camera area.
- Runs the island from a foreground service for reliability.
- Includes a first-launch setup flow for required permissions.
- Provides a guaranteed **Show Test Island** action.
- Reacts to posted notifications via `NotificationListenerService`.
- Supports compact idle state and expanded notification state.
- Includes small, essential settings persisted with DataStore.

## Required permissions

1. **Draw over other apps** (`SYSTEM_ALERT_WINDOW`)
   - Needed to render the Dynamic Island overlay through `WindowManager`.
2. **Notification access** (`NotificationListenerService` binding)
   - Needed to react to incoming notifications.
3. **Battery optimization exemption** (recommended)
   - Helps keep the foreground overlay service alive.

The app includes direct setup actions that open the exact Android settings pages.

## Overlay behavior

- Overlay type: `TYPE_APPLICATION_OVERLAY`
- Position: top-center with clamped safe offset
- Visual style: black capsule with rounded corners and subtle shadow
- States:
  - Idle
  - Test (manual)
  - Notification
  - Expanded (long press)
- Tap collapses, long press expands.

## Current limitations

- Android does not allow a third-party app to universally replace all system notification UI.
- Behavior depends on OEM battery policies (Samsung/Pixel may differ).
- Media state is intentionally minimal in this v2 and not a full lockscreen replacement.

## Build locally

```bash
./gradlew assembleDebug
```

Output APK path:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## GitHub Actions APK build

Workflow: `.github/workflows/android-debug-apk.yml`

Triggers:
- `push` to `main`
- `pull_request` to `main`
- `workflow_dispatch`

Artifact name:
- `dynamic-island-v2-debug-apk`

## Real-device testing checklist (Samsung/Pixel)

1. Install debug APK.
2. Open app and complete setup cards:
   - Overlay permission
   - Notification access
   - Battery optimization
3. Tap **Start Overlay Service**.
4. Tap **Show Test Island** and confirm top-center visible island.
5. Send a notification from another app and confirm island expansion.
6. Long-press island to confirm expanded state.
7. Use **Reset Position** if needed.
