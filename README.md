# Learn Korean Language – Complete Android Application

**Developer:** Shahab Millizai  
**Android Application ID:** `com.shahabmillizai.learnkorean`  
**Platform:** Android (Kotlin, Jetpack Compose, Material 3, Java 17)  
**Monetization:** Official Google Mobile Ads (AdMob) SDK

---

## 1. Project Overview & Folder Structure

This repository contains the complete, production-ready native Android Studio project for **Learn Korean Language**, featuring:
- **Comprehensive 4-Level Curriculum:** Beginner, Elementary, Intermediate, Advanced with Hangul, Romanization, English, and Urdu translations.
- **Audio Pronunciation:** Powered by native Android `TextToSpeech` with Korean locale (`ko-KR`).
- **Interactive Practice:** Hangul Alphabet Trainer, 3D Flippable Flashcards, Multiple-Choice Quizzes, Word Match Game, and TOPIK Mock Practice.
- **Offline Persistence:** Local structured assets & SharedPreferences for XP, Streaks, Completed Lessons, and Bookmarked Vocabulary.
- **AdMob Monetization:** Pre-configured with official Google Mobile Ads SDK, Google UMP Consent flow, and proper lifecycle management.

```
/android
├── build.gradle.kts                   // Root build script with Kotlin 2.0 & AGP
├── settings.gradle.kts                // Dependency resolution management & repositories
├── gradle.properties                  // JVM and AndroidX configuration
├── gradlew & gradlew.bat              // Gradle wrapper execution scripts
├── gradle/
│   ├── libs.versions.toml             // Standard version catalog
│   └── wrapper/
│       └── gradle-wrapper.properties  // Gradle 8.9 wrapper configuration
├── app/
│   ├── build.gradle.kts               // App module: dependencies, build types (test vs real AdMob)
│   ├── proguard-rules.pro             // Proguard rules for Ads, Gson, Compose
│   └── src/main/
│       ├── AndroidManifest.xml        // Internet permissions, AdMob App ID, Application
│       ├── assets/
│       │   └── curriculum.json        // Offline 4-level Korean curriculum & dictionary
│       ├── java/com/shahabmillizai/learnkorean/
│       │   ├── LearnKoreanApp.kt      // Application class: MobileAds & AppOpenAd init
│       │   ├── MainActivity.kt        // Main Compose Activity, TTS, UMP consent
│       │   ├── ads/
│       │   │   ├── AdConfig.kt        // Test vs Real AdMob IDs toggle
│       │   │   ├── BannerAdView.kt    // Adaptive banner ad wrapper
│       │   │   ├── InterstitialAdManager.kt // Capped transition ads (never in quiz)
│       │   │   ├── RewardedInterstitialAdManager.kt // Bonus XP/phrases rewards
│       │   │   ├── AppOpenAdManager.kt // Lifecycle-aware app return ads
│       │   │   ├── NativeAdView.kt    // Official native ad template with AD badge
│       │   │   └── ConsentManager.kt  // Google User Messaging Platform (UMP)
│       │   ├── data/
│       │   │   ├── model/Models.kt    // Curriculum, Lesson, Quiz, Dict data classes
│       │   │   ├── local/UserPreferences.kt // Offline progress & bookmarks
│       │   │   └── repository/KoreanRepository.kt // Asset loading & dictionary search
│       │   ├── tts/KoreanTtsManager.kt // Android TextToSpeech engine
│       │   ├── ui/
│       │   │   ├── theme/             // Korean flag palette (Red, Blue, White, Black)
│       │   │   ├── navigation/        // Bottom navigation (5 tabs) & NavGraph
│       │   │   └── screens/           // Home, Lessons, LessonDetail, Practice, Dict, Profile
│       │   └── res/                   // Strings, Colors, Themes, Vector Icons
└── .github/workflows/
    └── android-build.yml              // Automated CI/CD build & APK generation
```

---

## 2. Google AdMob Integration Confirmation

The application is pre-configured with the exact AdMob IDs provided:

| Ad Format | Production Unit ID (Release) | Google Test Unit ID (Debug) |
|---|---|---|
| **App ID** | `ca-app-pub-2790436784008422~8320873870` | (In AndroidManifest.xml) |
| **Banner Ad** | `ca-app-pub-2790436784008422/7007792206` | `ca-app-pub-3940256099942544/6300978111` |
| **Interstitial Ad** | `ca-app-pub-2790436784008422/5419378368` | `ca-app-pub-3940256099942544/1033173712` |
| **Rewarded Interstitial** | `ca-app-pub-2790436784008422/7366437749` | `ca-app-pub-3940256099942544/5354046379` |
| **App Open Ad** | `ca-app-pub-2790436784008422/1048441408` | `ca-app-pub-3940256099942544/9257395921` |
| **Native Ad** | `ca-app-pub-2790436784008422/3681297429` | `ca-app-pub-3940256099942544/2247696110` |

### Key Policy Compliance Implemented:
1. **Never Click Live Ads During Testing:** `AdConfig.kt` dynamically selects Google Test IDs during Debug builds and Production IDs during Release builds.
2. **Non-Intrusive Banner Placement:** Positioned at the bottom of the screens above navigation, never overlapping buttons or text.
3. **No Interruption of Active Learning:** `InterstitialAdManager` enforces a frequency limit (at most once every 3 completed lessons) and strictly checks `isQuizActive == false` to prevent breaking user focus.
4. **Rewarded Interstitial Confirmation:** A modal prompts the user before playing the ad, clearly explaining the reward (+150 XP & Bonus TOPIK pack). The reward is unlocked only upon `onUserEarnedRewardListener` confirmation.
5. **Native Ad Formatting:** Clearly displays the `SPONSORED AD` badge, distinct headline, and call-to-action button.
6. **Privacy & Consent (UMP):** `ConsentManager.kt` initializes Google's User Messaging Platform for GDPR/EEA regulations with in-app privacy controls.

---

## 3. How to Open & Build in Android Studio

1. **Prerequisites:**
   - Install **Android Studio** (Koala, Ladybug, or later).
   - Install **JDK 17** (configured by default in Android Studio).
   - Android SDK Platform **API 35** and Build Tools.

2. **Opening the Project:**
   - Launch Android Studio.
   - Click **Open** (or `File > Open...`).
   - Navigate to the `android/` directory and select it.
   - Android Studio will automatically invoke Gradle sync.

3. **Running on Emulator or Physical Device:**
   - Connect an Android device with Developer Options & USB Debugging enabled (or start an Android Virtual Device).
   - Select the `app` run configuration in the top toolbar.
   - Press **Shift + F10** or click the green **Run** button.

---

## 4. How to Build APK Files via Command Line

Run the following commands inside the `android/` folder:

```bash
# Grant execution permissions
chmod +x ./gradlew

# Build Debug APK for testing (uses Google test AdMob IDs)
./gradlew assembleDebug

# Output location:
# app/build/outputs/apk/debug/app-debug.apk
```

To build a Release APK:
```bash
./gradlew assembleRelease

# Output location:
# app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## 5. Release Signing Instructions for Google Play Store

To publish to Google Play Store, generate a release keystore and sign your APK or Android App Bundle (AAB):

### Step A: Generate a Keystore File
```bash
keytool -genkey -v -keystore learnkorean-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias learnkorean -storepass YourSecurePassword -keypass YourSecurePassword
```

### Step B: Configure Signing in `app/build.gradle.kts`
Add the signing config inside `android { ... }`:
```kotlin
signingConfigs {
    create("release") {
        storeFile = file(System.getenv("KEYSTORE_PATH") ?: "../learnkorean-release-key.jks")
        storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "YourSecurePassword"
        keyAlias = System.getenv("KEY_ALIAS") ?: "learnkorean"
        keyPassword = System.getenv("KEY_PASSWORD") ?: "YourSecurePassword"
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        // ...
    }
}
```

### Step C: Build Signed Bundle (AAB) for Play Console
```bash
./gradlew bundleRelease
# Upload app/build/outputs/bundle/release/app-release.aab to Google Play Console!
```

---

## 6. GitHub Upload & CI/CD Setup

1. Initialize git in this directory (if not already initialized):
   ```bash
   git init
   git add .
   git commit -m "Initial commit: Learn Korean Language Android App by Shahab Millizai"
   ```
2. Create a new repository on GitHub: `https://github.com/shahabmillizai/learn-korean-android`.
3. Push to GitHub:
   ```bash
   git remote add origin https://github.com/shahabmillizai/learn-korean-android.git
   git branch -M main
   git push -u origin main
   ```
4. The included `.github/workflows/android-build.yml` workflow will automatically trigger, compile the project using Java 17, and produce ready-to-install debug APK artifacts under the **Actions** tab!
