# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /opt/Android/Sdk/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#OkHttp
-dontwarn okio.**

-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }

#Retrofit 2.x
-dontwarn retrofit2.**
-dontwarn retrofit2.appengine.UrlFetchClient

-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}


-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod