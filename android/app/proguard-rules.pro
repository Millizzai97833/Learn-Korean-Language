
# Project-specific ProGuard rules

# Google Mobile Ads SDK
-keep public class com.google.android.gms.ads.** {
    public *;
}

-keep class com.google.android.gms.ads.nativead.** { *; }

# Start.io SDK - protect SDK classes in Release builds
-keep class com.startapp.** { *; }
-dontwarn com.startapp.**

# Keep Gson data models
-keepclassmembers class com.shahabmillizai.learnkorean.data.model.** {
    <fields>;
}

-keep class com.shahabmillizai.learnkorean.data.model.** {
    *;
}

# Keep ViewModel constructors
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
