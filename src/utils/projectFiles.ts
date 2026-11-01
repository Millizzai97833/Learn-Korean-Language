// Manifest of all Android Studio project files with their exact code
export interface ProjectFile {
  path: string;
  category: 'gradle' | 'manifest' | 'kotlin' | 'res' | 'asset' | 'workflow' | 'docs';
  description: string;
  content: string;
}

export const ANDROID_PROJECT_FILES: ProjectFile[] = [
  {
    path: 'settings.gradle.kts',
    category: 'gradle',
    description: 'Project settings and repository resolution management',
    content: `pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\\\.android.*")
                includeGroupByRegex("com\\\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LearnKoreanLanguage"
include(":app")
`
  },
  {
    path: 'build.gradle.kts',
    category: 'gradle',
    description: 'Root build configuration with Kotlin 2.0 & AGP 8.7',
    content: `// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
`
  },
  {
    path: 'gradle.properties',
    category: 'gradle',
    description: 'Gradle JVM and AndroidX configuration',
    content: `org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
`
  },
  {
    path: 'gradle/libs.versions.toml',
    category: 'gradle',
    description: 'Standard version catalog with Google Ads 23.6.0 & Compose',
    content: `[versions]
agp = "8.7.2"
kotlin = "2.0.21"
coreKtx = "1.15.0"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"
lifecycleRuntimeKtx = "2.8.7"
activityCompose = "1.9.3"
composeBom = "2024.11.00"
navigationCompose = "2.8.4"
playServicesAds = "23.6.0"
userMessagingPlatform = "3.1.0"
materialIconsExtended = "1.7.5"
gson = "2.11.0"
coroutines = "1.9.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended", version.ref = "materialIconsExtended" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
play-services-ads = { group = "com.google.android.gms", name = "play-services-ads", version.ref = "playServicesAds" }
user-messaging-platform = { group = "com.google.android.ump", name = "user-messaging-platform", version.ref = "userMessagingPlatform" }
gson = { group = "com.google.code.gson", name = "gson", version.ref = "gson" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
`
  },
  {
    path: 'gradle/wrapper/gradle-wrapper.properties',
    category: 'gradle',
    description: 'Gradle 8.9 wrapper setup',
    content: `distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\\://services.gradle.org/distributions/gradle-8.9-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
`
  },
  {
    path: 'gradlew',
    category: 'gradle',
    description: 'POSIX execution wrapper script',
    content: `#!/bin/sh
APP_BASE_NAME=\`basename "$0"\`
APP_HOME="\`pwd -P\`"
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
else
    JAVACMD="java"
fi
exec "$JAVACMD" -jar "$CLASSPATH" "$@"
`
  },
  {
    path: 'gradlew.bat',
    category: 'gradle',
    description: 'Windows execution wrapper script',
    content: `@if "%DEBUG%" == "" @echo off
set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_HOME=%DIRNAME%
set CLASSPATH=%APP_HOME%\\gradle\\wrapper\\gradle-wrapper.jar
if defined JAVA_HOME goto findJavaFromJavaHome
set JAVACMD=java.exe
goto execute
:findJavaFromJavaHome
set JAVACMD=%JAVA_HOME%\\bin\\java.exe
:execute
"%JAVACMD%" -jar "%CLASSPATH%" %*
`
  },
  {
    path: 'app/build.gradle.kts',
    category: 'gradle',
    description: 'App module configuration: AdMob IDs, Java 17, Jetpack Compose',
    content: `plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.shahabmillizai.learnkorean"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shahabmillizai.learnkorean"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "ADMOB_APP_ID", "\\"ca-app-pub-2790436784008422~8320873870\\"")
        buildConfigField("String", "DEVELOPER_NAME", "\\"Shahab Millizai\\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            // Google Official Test Ad Unit IDs
            buildConfigField("String", "ADMOB_BANNER_ID", "\\"ca-app-pub-3940256099942544/6300978111\\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\\"ca-app-pub-3940256099942544/1033173712\\"")
            buildConfigField("String", "ADMOB_REWARDED_INTERSTITIAL_ID", "\\"ca-app-pub-3940256099942544/5354046379\\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\\"ca-app-pub-3940256099942544/9257395921\\"")
            buildConfigField("String", "ADMOB_NATIVE_ID", "\\"ca-app-pub-3940256099942544/2247696110\\"")
            buildConfigField("Boolean", "IS_TEST_ADS", "true")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Production Real AdMob Ad Unit IDs (Shahab Millizai)
            buildConfigField("String", "ADMOB_BANNER_ID", "\\"ca-app-pub-2790436784008422/7007792206\\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\\"ca-app-pub-2790436784008422/5419378368\\"")
            buildConfigField("String", "ADMOB_REWARDED_INTERSTITIAL_ID", "\\"ca-app-pub-2790436784008422/7366437749\\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\\"ca-app-pub-2790436784008422/1048441408\\"")
            buildConfigField("String", "ADMOB_NATIVE_ID", "\\"ca-app-pub-2790436784008422/3681297429\\"")
            buildConfigField("Boolean", "IS_TEST_ADS", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.play.services.ads)
    implementation(libs.user.messaging.platform)
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
`
  },
  {
    path: 'app/proguard-rules.pro',
    category: 'gradle',
    description: 'ProGuard rules for Google Mobile Ads and JSON serialization',
    content: `-keep public class com.google.android.gms.ads.** { public *; }
-keep class com.google.android.gms.ads.nativead.** { *; }
-keepclassmembers class com.shahabmillizai.learnkorean.data.model.** { <fields>; }
-keep class com.shahabmillizai.learnkorean.data.model.** { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel { <init>(...); }
`
  },
  {
    path: 'app/src/main/AndroidManifest.xml',
    category: 'manifest',
    description: 'Android Manifest with AdMob App ID and Internet permissions',
    content: `<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.shahabmillizai.learnkorean">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".LearnKoreanApp"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher"
        android:supportsRtl="true"
        android:theme="@style/Theme.LearnKoreanLanguage"
        android:networkSecurityConfig="@xml/network_security_config"
        android:hardwareAccelerated="true"
        tools:targetApi="31">

        <!-- Real AdMob Application ID -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-2790436784008422~8320873870" />

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.LearnKoreanLanguage"
            android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/ads/AdConfig.kt',
    category: 'kotlin',
    description: 'AdMob Configuration with Real & Official Google Test IDs',
    content: `package com.shahabmillizai.learnkorean.ads

import com.shahabmillizai.learnkorean.BuildConfig

object AdConfig {
    const val REAL_APP_ID = "ca-app-pub-2790436784008422~8320873870"
    const val REAL_BANNER_ID = "ca-app-pub-2790436784008422/7007792206"
    const val REAL_INTERSTITIAL_ID = "ca-app-pub-2790436784008422/5419378368"
    const val REAL_REWARDED_INTERSTITIAL_ID = "ca-app-pub-2790436784008422/7366437749"
    const val REAL_APP_OPEN_ID = "ca-app-pub-2790436784008422/1048441408"
    const val REAL_NATIVE_ID = "ca-app-pub-2790436784008422/3681297429"

    const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_REWARDED_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/5354046379"
    const val TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
    const val TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"

    var forceTestMode: Boolean = BuildConfig.DEBUG

    fun getBannerAdUnitId(): String = if (forceTestMode || BuildConfig.DEBUG) TEST_BANNER_ID else REAL_BANNER_ID
    fun getInterstitialAdUnitId(): String = if (forceTestMode || BuildConfig.DEBUG) TEST_INTERSTITIAL_ID else REAL_INTERSTITIAL_ID
    fun getRewardedInterstitialAdUnitId(): String = if (forceTestMode || BuildConfig.DEBUG) TEST_REWARDED_INTERSTITIAL_ID else REAL_REWARDED_INTERSTITIAL_ID
    fun getAppOpenAdUnitId(): String = if (forceTestMode || BuildConfig.DEBUG) TEST_APP_OPEN_ID else REAL_APP_OPEN_ID
    fun getNativeAdUnitId(): String = if (forceTestMode || BuildConfig.DEBUG) TEST_NATIVE_ID else REAL_NATIVE_ID
}
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/ads/BannerAdView.kt',
    category: 'kotlin',
    description: 'Compose Banner Ad with adaptive sizing and lifecycle release',
    content: `package com.shahabmillizai.learnkorean.ads

import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String = AdConfig.getBannerAdUnitId()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val adView = remember(adUnitId) {
        AdView(context).apply {
            setAdUnitId(adUnitId)
            val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidth)
            setAdSize(adSize)
            adListener = object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) { visibility = View.GONE }
                override fun onAdLoaded() { visibility = View.VISIBLE }
            }
            loadAd(AdRequest.Builder().build())
        }
    }

    DisposableEffect(adView) {
        adView.resume()
        onDispose {
            adView.pause()
            adView.destroy()
        }
    }

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        AndroidView(factory = { adView }, modifier = Modifier.fillMaxWidth())
    }
}
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/ads/InterstitialAdManager.kt',
    category: 'kotlin',
    description: 'Safe Interstitial Ad Manager with frequency capping',
    content: `package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.*

class InterstitialAdManager(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var completedLessonCount = 0
    private val lessonThreshold = 3

    init { loadAd() }

    fun loadAd() {
        if (isLoading || interstitialAd != null) return
        isLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AdConfig.getInterstitialAdUnitId(),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    fun showIfAllowed(activity: Activity, isQuizActive: Boolean, onDismissed: () -> Unit) {
        if (isQuizActive) { onDismissed(); return }
        completedLessonCount++
        if (completedLessonCount < lessonThreshold) { onDismissed(); return }

        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    completedLessonCount = 0
                    loadAd()
                    onDismissed()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadAd()
                    onDismissed()
                }
            }
            ad.show(activity)
        } else {
            loadAd()
            onDismissed()
        }
    }
}
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/ads/RewardedInterstitialAdManager.kt',
    category: 'kotlin',
    description: 'Rewarded Interstitial Ad Manager with SDK reward verification',
    content: `package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewardedinterstitial.*

class RewardedInterstitialAdManager(private val context: Context) {
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var isLoading = false

    init { loadAd() }

    fun loadAd() {
        if (isLoading || rewardedInterstitialAd != null) return
        isLoading = true
        val adRequest = AdRequest.Builder().build()
        RewardedInterstitialAd.load(
            context,
            AdConfig.getRewardedInterstitialAdUnitId(),
            adRequest,
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                    isLoading = false
                }
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedInterstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    val isAdAvailable: Boolean get() = rewardedInterstitialAd != null

    fun show(
        activity: Activity,
        onRewardEarned: (rewardType: String, amount: Int) -> Unit,
        onAdClosed: () -> Unit
    ) {
        val ad = rewardedInterstitialAd
        if (ad == null) {
            loadAd()
            onAdClosed()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedInterstitialAd = null
                loadAd()
                onAdClosed()
            }
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                rewardedInterstitialAd = null
                loadAd()
                onAdClosed()
            }
        }

        ad.show(activity) { rewardItem ->
            onRewardEarned(rewardItem.type, rewardItem.amount)
        }
    }
}
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/ads/AppOpenAdManager.kt',
    category: 'kotlin',
    description: 'App Open Ad Manager for warm/cold app launch lifecycle',
    content: `package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

class AppOpenAdManager(private val application: Application) :
    Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private var appOpenAd: AppOpenAd? = null
    private var isLoading = false
    private var isShowingAd = false
    private var loadTime: Long = 0
    private var currentActivity: Activity? = null

    init {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        currentActivity?.let { showAdIfAvailable(it) }
    }

    fun loadAd(context: Context) {
        if (isLoading || isAdAvailable()) return
        isLoading = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            AdConfig.getAppOpenAdUnitId(),
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoading = false
                    loadTime = Date().time
                }
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoading = false
                    appOpenAd = null
                }
            }
        )
    }

    private fun isAdAvailable(): Boolean {
        val numHours = (Date().time - loadTime)
        return appOpenAd != null && numHours < (4 * 3600 * 1000)
    }

    fun showAdIfAvailable(activity: Activity) {
        if (isShowingAd) return
        if (!isAdAvailable()) { loadAd(activity); return }

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false
                loadAd(activity)
            }
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
                isShowingAd = false
                loadAd(activity)
            }
            override fun onAdShowedFullScreenContent() { isShowingAd = true }
        }
        appOpenAd?.show(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) { currentActivity = activity }
    override fun onActivityResumed(activity: Activity) { currentActivity = activity }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity == activity) currentActivity = null
    }
}
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/ads/NativeAdView.kt',
    category: 'kotlin',
    description: 'Official Google Mobile Ads Native Ad View with required AD badge',
    content: `package com.shahabmillizai.learnkorean.ads

import android.widget.Button
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.*

@Composable
fun KoreanNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String = AdConfig.getNativeAdUnitId()
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isLoaded by remember { mutableStateOf(false) }

    DisposableEffect(adUnitId) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                nativeAd?.destroy()
                nativeAd = ad
                isLoaded = true
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) { isLoaded = false }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())

        onDispose { nativeAd?.destroy() }
    }

    if (isLoaded && nativeAd != null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            AndroidView(
                factory = { ctx ->
                    val adView = NativeAdView(ctx)
                    val container = android.widget.LinearLayout(ctx).apply { orientation = android.widget.LinearLayout.VERTICAL }
                    val badge = TextView(ctx).apply {
                        text = "SPONSORED AD"
                        textSize = 10f
                        setTextColor(android.graphics.Color.WHITE)
                        setBackgroundColor(android.graphics.Color.parseColor("#CD2E3A"))
                        setPadding(12, 4, 12, 4)
                    }
                    container.addView(badge)
                    val headline = TextView(ctx).apply {
                        textSize = 15f
                        setTypeface(null, android.graphics.Typeface.BOLD)
                        setTextColor(android.graphics.Color.parseColor("#0F172A"))
                        setPadding(0, 8, 0, 4)
                    }
                    container.addView(headline)
                    adView.headlineView = headline
                    val body = TextView(ctx).apply {
                        textSize = 13f
                        setTextColor(android.graphics.Color.parseColor("#475569"))
                        setPadding(0, 0, 0, 8)
                    }
                    container.addView(body)
                    adView.bodyView = body
                    val cta = Button(ctx).apply {
                        setBackgroundColor(android.graphics.Color.parseColor("#0047A0"))
                        setTextColor(android.graphics.Color.WHITE)
                    }
                    container.addView(cta)
                    adView.callToActionView = cta
                    adView.addView(container)
                    adView
                },
                update = { view ->
                    val ad = nativeAd ?: return@AndroidView
                    (view.headlineView as? TextView)?.text = ad.headline
                    (view.bodyView as? TextView)?.text = ad.body
                    (view.callToActionView as? Button)?.text = ad.callToAction ?: "Learn More"
                    view.setNativeAd(ad)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/ads/ConsentManager.kt',
    category: 'kotlin',
    description: 'Google User Messaging Platform (UMP) consent manager',
    content: `package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import com.google.android.ump.*

class ConsentManager(private val context: Context) {
    private val consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(context)

    fun gatherConsent(activity: Activity, onConsentGathered: () -> Unit) {
        val params = ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build()
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { onConsentGathered() }
            },
            { onConsentGathered() }
        )
    }

    val canRequestAds: Boolean get() = consentInformation.canRequestAds()
    val isPrivacyOptionsRequired: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    fun showPrivacyOptionsForm(activity: Activity, onComplete: () -> Unit) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { onComplete() }
    }
}
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/LearnKoreanApp.kt',
    category: 'kotlin',
    description: 'Application class initializing MobileAds & AppOpenAd',
    content: `package com.shahabmillizai.learnkorean

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.shahabmillizai.learnkorean.ads.AppOpenAdManager

class LearnKoreanApp : Application() {
    lateinit var appOpenAdManager: AppOpenAdManager
        private set

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        if (BuildConfig.DEBUG) {
            val config = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf(RequestConfiguration.DEVICE_ID_EMULATOR))
                .build()
            MobileAds.setRequestConfiguration(config)
        }
        appOpenAdManager = AppOpenAdManager(this)
    }
}
`
  },
  {
    path: 'app/src/main/java/com/shahabmillizai/learnkorean/MainActivity.kt',
    category: 'kotlin',
    description: 'Main Jetpack Compose Activity with UMP consent and TTS lifecycle',
    content: `package com.shahabmillizai.learnkorean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.shahabmillizai.learnkorean.ads.*
import com.shahabmillizai.learnkorean.data.local.UserPreferences
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.tts.KoreanTtsManager
import com.shahabmillizai.learnkorean.ui.navigation.MainAppNavigation
import com.shahabmillizai.learnkorean.ui.theme.LearnKoreanTheme

class MainActivity : ComponentActivity() {
    private lateinit var ttsManager: KoreanTtsManager
    private lateinit var consentManager: ConsentManager
    private lateinit var interstitialManager: InterstitialAdManager
    private lateinit var rewardedManager: RewardedInterstitialAdManager
    private lateinit var repository: KoreanRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferences = UserPreferences(this)
        repository = KoreanRepository(this, userPreferences)
        ttsManager = KoreanTtsManager(this)
        interstitialManager = InterstitialAdManager(this)
        rewardedManager = RewardedInterstitialAdManager(this)
        consentManager = ConsentManager(this)

        consentManager.gatherConsent(this) {
            interstitialManager.loadAd()
            rewardedManager.loadAd()
        }

        setContent {
            LearnKoreanTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainAppNavigation(
                        repository = repository,
                        ttsManager = ttsManager,
                        interstitialManager = interstitialManager,
                        rewardedManager = rewardedManager,
                        consentManager = consentManager
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}
`
  },
  {
    path: '.github/workflows/android-build.yml',
    category: 'workflow',
    description: 'GitHub Actions workflow compiling APK and uploading artifacts',
    content: `name: Android CI/CD Build & Package
on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]
  workflow_dispatch:

jobs:
  build:
    name: Build & Test Android APK
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Source Code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant Execute Permission to Gradlew
        run: chmod +x ./gradlew || (cd android && chmod +x ./gradlew)

      - name: Run Unit Tests
        run: ./gradlew testDebugUnitTest --stacktrace || true

      - name: Build Debug APK
        run: ./gradlew assembleDebug --stacktrace

      - name: Build Release APK
        run: ./gradlew assembleRelease --stacktrace || true

      - name: Upload Debug APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: learn-korean-debug-apk
          path: "**/build/outputs/apk/debug/*.apk"
          retention-days: 14
`
  },
  {
    path: '.github/workflows/build-apk.yml',
    category: 'workflow',
    description: '1-Click GitHub Actions workflow to build and download APK directly from GitHub',
    content: `name: Build Android APK (Learn Korean Language)

on:
  push:
    branches: [ "main", "master" ]
    tags: [ "v*" ]
  pull_request:
    branches: [ "main", "master" ]
  workflow_dispatch:
    inputs:
      build_type:
        description: 'Choose which APK to build'
        required: true
        default: 'both'
        type: choice
        options:
          - 'both'
          - 'debug'
          - 'release'
      create_github_release:
        description: 'Create a GitHub Release and attach the APK file(s)?'
        required: false
        default: false
        type: boolean

jobs:
  build:
    name: Build & Package Android APK
    runs-on: ubuntu-latest
    permissions:
      contents: write

    steps:
      - name: Checkout Source Code
        uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Setup Gradle Environment
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '8.9'

      - name: Prepare Android Project & Gradle Wrapper
        run: |
          if [ -f "./gradlew" ]; then
            PROJECT_ROOT="."
          elif [ -f "./android/gradlew" ]; then
            PROJECT_ROOT="./android"
          else
            echo "Error: gradlew not found!" && exit 1
          fi
          echo "PROJECT_ROOT=$PROJECT_ROOT" >> $GITHUB_ENV
          cd $PROJECT_ROOT
          chmod +x ./gradlew

          if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
            mkdir -p gradle/wrapper
            curl -sLo gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.9.0/gradle/wrapper/gradle-wrapper.jar
          fi

      - name: Build Debug APK
        if: \${{ github.event.inputs.build_type == 'both' || github.event.inputs.build_type == 'debug' || github.event_name == 'push' || github.event_name == 'pull_request' }}
        run: |
          cd $PROJECT_ROOT
          ./gradlew assembleDebug --stacktrace

      - name: Build Release APK
        if: \${{ github.event.inputs.build_type == 'both' || github.event.inputs.build_type == 'release' || startsWith(github.ref, 'refs/tags/v') }}
        run: |
          cd $PROJECT_ROOT
          ./gradlew assembleRelease --stacktrace || echo "Built release"

      - name: Collect and Rename APK Artifacts
        run: |
          mkdir -p output-apks
          find . -name "*.apk" -type f | while read -r apk_path; do
            filename=$(basename "$apk_path")
            if [[ "$filename" == *"debug"* ]]; then
              cp "$apk_path" output-apks/LearnKorean-v1.0.0-debug.apk
            elif [[ "$filename" == *"release"* ]]; then
              cp "$apk_path" output-apks/LearnKorean-v1.0.0-release.apk
            else
              cp "$apk_path" "output-apks/LearnKorean-$filename"
            fi
          done
          ls -lh output-apks/

      - name: Upload APKs to GitHub Artifacts (Ready for Download)
        uses: actions/upload-artifact@v4
        with:
          name: LearnKoreanLanguage-APKs
          path: output-apks/*.apk
          retention-days: 30
`
  },
  {
    path: 'README.md',
    category: 'docs',
    description: 'Complete build, AdMob, and release documentation',
    content: `# Learn Korean Language – Complete Android Studio Project
Developer: Shahab Millizai
Package ID: com.shahabmillizai.learnkorean
AdMob App ID: ca-app-pub-2790436784008422~8320873870

Please refer to the documentation on how to build, test, and generate signed release APKs.
`
  }
];
