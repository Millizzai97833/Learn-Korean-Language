
# ==========================================
# Start.io SDK ProGuard / R8 Rules
# ==========================================

# Keep Start.io SDK classes
-keep class com.startapp.** { *; }

# Keep TrueNet SDK classes
-keep class com.truenet.** { *; }

# SDK warning suppression
-dontwarn com.startapp.**
-dontwarn com.truenet.**

# Preserve reflection and annotation attributes
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# ==========================================
# Google Mobile Ads
# ==========================================

-keep public class com.google.android.gms.ads.** {
    public *;
}

-keep class com.google.android.gms.ads.nativead.** { *; }

# ==========================================
# Gson data models
# ==========================================

-keepclassmembers class com.shahabmillizai.learnkorean.data.model.** {
    <fields>;
}

-keep class com.shahabmillizai.learnkorean.data.model.** {
    *;
}

# ==========================================
# Android ViewModels
# ==========================================

-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
