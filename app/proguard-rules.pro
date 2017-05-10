# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk-windows/tools/proguard/proguard-android.txt
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
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-obfuscationdictionary dictionary.txt
-classobfuscationdictionary dictionary.txt
-packageobfuscationdictionary dictionary.txt

# Native
-keepclasseswithmembernames class * {
    native <methods>;
}

# Bean and Paricelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class com.hhly.mlottery.bean.** { *; }

# rx
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Wechat
-keep class com.hhly.mlottery.wxapi.** { *; }
# tencent
-dontwarn com.tencent.**
-keep class com.tencent.mm.sdk.** { *; }

-dontnote org.apache.http.**
-dontwarn org.apache.http.**
-dontnote android.net.http.**
-dontwarn android.net.http.**

-keep class java.net.** { *; }

# fastjson
-dontwarn com.alibaba.fastjson.**
-dontnote com.alibaba.fastjson.**

# eventbus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-keepclassmembers class ** {
    public void onEvent*(**);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

########################## 友盟推送 Start
-dontwarn com.ut.mini.**
-dontwarn okio.**
-dontwarn com.xiaomi.**
-dontwarn com.squareup.wire.**
-dontwarn android.support.v4.**

-keepattributes *Annotation*

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

-keep class okio.** {*;}
-keep class com.squareup.wire.** {*;}

-keep class com.umeng.message.protobuffer.* {
    public <fields>;
    public <methods>;
}

-keep class com.umeng.message.* {
    public <fields>;
        public <methods>;
}

-keep class org.android.agoo.impl.* {
    public <fields>;
    public <methods>;
}

-keep class org.android.agoo.service.* { *; }

-keep class org.android.spdy.**{ *; }

-keep public class **.R$* {
    public static final int *;
}

-dontwarn org.apache.http.**
-dontwarn android.webkit.**
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.codec.** { *; }
-keep class org.apache.commons.logging.** { *; }
-keep class android.net.compatibility.** { *; }
-keep class android.net.http.** { *; }
-keep class org.apache.http.{*;}
########################## 友盟推送 End

# License
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

-keep class org.android.spdy.** { *; }

#融云
-keep public class io.rong.** { *; }
-dontwarn io.rong.**

#pldroid
-keep public class com.pili.pldroid.** { *; }
-dontwarn com.pili.pldroid.**

#retrofit
-keep public class retrofit.appengine.** { *; }
-dontwarn retrofit.appengine.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn retrofit.**
-keep class retrofit.** { *; }

#畅言
-keep public class com.sohu.cyan.** { *; }
-dontwarn com.sohu.cyan.**

#JS调用WebView接口
-keep class com.hhly.mlottery.activity.WebActivity$YBFJavascriptHandler { *;}

-keep class pl.droidsonroids.gif.** { *;}

#--------------- BEGIN: okhttp ----------
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
#--------------- END: okhttp ----------

#--------------- BEGIN: okio ----------
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
#--------------- END: okio ----------

-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**


##retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

##okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**


-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}



## 视频播放
-keep class com.pili.pldroid.player.** { *; }
-keep class tv.danmaku.ijk.media.player.** {*;}

##模糊搜索

-keep class com.hhly.mlottery.activity.FootballMatchSearchActivity{ *;}
-keep class com.hhly.mlottery.activity.FootballInformationSerachActivity{ *;}
-keep class com.hhly.mlottery.activity.BasketballInformationSerachActivity{ *;}
-keep class com.hhly.mlottery.activity.SnookerInformationSerachActivity{ *;}
-keep class com.hhly.mlottery.callback.SearchService { *; }
-keep class com.hhly.mlottery.callback.BasketSearchservice{ *; }
-keep class com.hhly.mlottery.callback.SnookerSearchService{ *; }
-keep class com.hhly.mlottery.callback.FootballMatchSearchService{ *; }

##篮球指数
-keep class com.hhly.mlottery.frame.cpifrag.basketballtask.data.ApiService{ *; }


##Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

## butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

##-----TalkingData统计 begin-----
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
-keep class dice.** {*; }
-dontwarn dice.**
##-----TalkingData统计 end-----

##-----支付宝 begin-----
-libraryjars libs/alipaySingle-20161222.jar

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}
##-----支付宝 end-----