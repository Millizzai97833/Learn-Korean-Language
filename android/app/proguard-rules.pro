# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/.../Library/Android/sdk/tools/proguard/proguard-android.txt

# Google Play Services Ads Proguard Rules
-keep public class com.google.android.gms.ads.** {
   public *;
}

# For Google Mobile Ads SDK Native Ads
-keep class com.google.android.gms.ads.nativead.** { *; }

# Keep data models serialized by Gson
-keepclassmembers class com.shahabmillizai.learnkorean.data.model.** { <fields>; }
-keep class com.shahabmillizai.learnkorean.data.model.** { *; }

# For Kotlin Coroutines & Jetpack Compose
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
