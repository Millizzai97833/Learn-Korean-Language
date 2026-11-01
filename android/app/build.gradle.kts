plugins {
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

        buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-2790436784008422~8320873870\"")
        buildConfigField("String", "DEVELOPER_NAME", "\"Shahab Millizai\"")

        manifestPlaceholders["admobAppId"] =
            "ca-app-pub-2790436784008422~8320873870"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            manifestPlaceholders["admobAppId"] =
                "ca-app-pub-3940256099942544~3347511713"

            buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "ADMOB_REWARDED_INTERSTITIAL_ID", "\"ca-app-pub-3940256099942544/5354046379\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\"ca-app-pub-3940256099942544/9257395921\"")
            buildConfigField("String", "ADMOB_NATIVE_ID", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("Boolean", "IS_TEST_ADS", "true")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-2790436784008422/7007792206\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-2790436784008422/5419378368\"")
            buildConfigField("String", "ADMOB_REWARDED_INTERSTITIAL_ID", "\"ca-app-pub-2790436784008422/7366437749\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\"ca-app-pub-2790436784008422/1048441408\"")
            buildConfigField("String", "ADMOB_NATIVE_ID", "\"ca-app-pub-2790436784008422/3681297429\"")
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.process)
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
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
