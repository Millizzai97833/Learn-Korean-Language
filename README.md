# Learn Korean Language — Android App by Shahab Millizai

**Application ID:** `com.shahabmillizai.learnkorean`  
**Developer:** Shahab Millizai  
**Platform:** Android (Jetpack Compose, Kotlin 2.2, Java 17, AGP 8.9)  
**Ad Network:** Start.io (StartApp) In-App SDK 5.4.0  
**Start.io App ID:** `210777317`

---

## 🚀 Key Start.io Integration Details

### 1. Initialization Architecture
- **Single code initialization path:** `StartAppSDK.initParams(context, AdConfig.START_IO_APP_ID).setReturnAdsEnabled(false).setCallback(Runnable { ... }).init()`
- `StartAppInitProvider` is explicitly removed from `AndroidManifest.xml` via `tools:node="remove"` so no automatic premature initialization occurs.
- `StartIoSdkState.isReady` gates all ad requests. The SDK is **only** marked ready once the callback confirms initialization.
- If initialization throws an exception, `StartIoSdkState.markFailed(reason)` is called and ads are never loaded.

### 2. Large, Tall Banner on Home Screen (Medium Rectangle - MREC)
- Placement: Bottom of `HomeScreen`, centered horizontally, responsive to screen width.
- Implementation: `com.startapp.sdk.ads.banner.Mrec`
- Dimensions: **300dp width × 250dp height** (full height without clipping or distortion).
- Non-overlapping: `LazyColumn` in `HomeScreen` has `weight(1f)` so all lesson content scrolls smoothly above the banner. Scaffold bottom navigation bar remains accessible without any obstruction.
- Lifecycle: `showBanner()` on `ON_RESUME`, `hideBanner()` on `ON_PAUSE` / `ON_DESTROY`.
- Recomposition protection: View is created once in `AndroidView(factory = ...)` and never reloaded inside `update`.

### 3. Rewarded Video Ads
- Mode: `StartAppAd.AdMode.REWARDED_VIDEO`
- Reward: Unlocks **+150 XP** and the **Bonus TOPIK Vocabulary Pack** on the Home screen.
- Callback: Tracks completion with `VideoListener.onVideoCompleted()` before awarding XP.
- Preloading: Preloads next rewarded ad in the background after dismissal.

### 4. Interstitial Ads
- Displayed every 3 completed lessons outside of quiz flows.
- Frequency capped and deferred until SDK is confirmed ready.

---

## 🛠 Building the APK

```bash
# Debug APK
./gradlew assembleDebug

# Signed Release APK (requires release keystore environment variables)
./gradlew assembleRelease
```
