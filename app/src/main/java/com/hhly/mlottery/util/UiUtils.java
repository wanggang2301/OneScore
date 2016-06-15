package com.hhly.mlottery.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hhly.mlottery.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tenney
 * @ClassName: UiUtils
 * @Description: 工具类，广播，弹框..
 * @date 2015-10-15 上午11:01:15
 */
public class UiUtils {

    public static String TAG = "UiUtils";
    public static long lastTime;

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static Toast toast;

    /**
     * 显示提示信息
     */
    public static void toast(Context context, String msg) {
        if (null == toast)
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    public static void toast(Context context, String msg, int duration) {
        if (null == toast)
            toast = Toast.makeText(context, msg, duration);
        toast.setText(msg);
        toast.setDuration(duration);
        toast.show();
    }

    public static void toast(Context context, int resId) {
        toast(context, context.getString(resId));
    }

    public static void toast(Context context, int resId, int duration) {
        toast(context, context.getString(resId), duration);
    }

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 显示提示信息
     */
    public static void DisplayToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void DisplayToast(Context context, int resId) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG)
                .show();
    }

    /**
     * 打开浏览器
     */
    public static void openBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param v       输入框
     */
    public static void hideSoftInput(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 打开键盘
     *
     * @param context
     * @param v       输入框
     */
    public static void opentSoftInput(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    // 图片渐变模糊度始终
    public static void alphaAnimationShow(View view) {
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    public static void alphaAnimationHead(View view) {
        AlphaAnimation animation = new AlphaAnimation(1f, 0.1f);
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    // 加载中动画开始
    public static void lodingAnimeStart(View view, Context context) {
        view.setVisibility(View.VISIBLE);
        Animation operatingAnim = AnimationUtils.loadAnimation(context,
                R.anim.loading_dialog_round);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        view.startAnimation(operatingAnim);
    }

    public static void lodingAnimeEnd(View view) {
        view.clearAnimation();
        view.setVisibility(View.GONE);
    }

    /***
     * TYPE_FRONTCOVER : cover显示 TYPE_LISTTHUMBNAIL: listView显示小图时
     * TYPE_SEARCHCOVER: 搜索显示图片时 TYPE_DOWNLOADCOVER: 下载cover尺寸 TYPE_LARGEPHOTO:
     * 显示大图时
     *
     * @author yali
     */
    public static enum CutPhotoType {
        TYPE_FRONTCOVER, TYPE_LISTTHUMBNAIL, TYPE_SEARCHCOVER, TYPE_DOWNLOADCOVER, TYPE_LARGEPHOTO;
    }

    private static String kCutPhotoFrontCover = "?imageView2/1/w/1095/h/480";
    private static String kCutPhotoListThumbnail = "?imageView2/1/w/360/h/360";
    private static String kCutPhotoSearchCover = "?imageView2/1/w/450/h/390";
    private static String kCutPhotoDownloadCover = "?imageView2/1/w/540/h/450";
    private static String kCutPhotoThumbnail11p = "?imageMogr2/thumbnail/!11p";
    private static String kCutPhotoThumbnail21p = "?imageMogr2/thumbnail/!21p";
    private static String kCutPhotoThumbnail31p = "?imageMogr2/thumbnail/!31p";
    private static String kCutPhotoThumbnail41p = "?imageMogr2/thumbnail/!41p";
    private static String kCutPhotoThumbnail61p = "?imageMogr2/thumbnail/!61p";
    private static String kCutPhotoThumbnail71p = "?imageMogr2/thumbnail/!71p";

    /***
     * 不同布局显示不同尺寸图片
     *
     * @param type
     * @param url
     * @return
     */
    public static String cutPhoto(CutPhotoType type, String url) {
        if (type == CutPhotoType.TYPE_FRONTCOVER)
            return url + kCutPhotoFrontCover;
        else if (type == CutPhotoType.TYPE_LISTTHUMBNAIL)
            return url + kCutPhotoListThumbnail;
        else if (type == CutPhotoType.TYPE_SEARCHCOVER)
            return url + kCutPhotoSearchCover;
        else if (type == CutPhotoType.TYPE_DOWNLOADCOVER)
            return url + kCutPhotoDownloadCover;
        else if (type == CutPhotoType.TYPE_LARGEPHOTO) {
            int size = Integer.parseInt(url.substring(url.indexOf("_s") + 2,
                    url.indexOf(".jpg")));
            if (size > 1024 * 1024 * 4) // >4M
                return url + kCutPhotoThumbnail11p;
            else if (size > 1024 * 1024 * 3) // >3M
                return url + kCutPhotoThumbnail21p;
            else if (size > 1024 * 1024 * 2) // >2M
                return url + kCutPhotoThumbnail31p;
            else if (size > 1024 * 1024) // >1M
                return url + kCutPhotoThumbnail41p;
            else if (size > 1024 * 512) // > 512
                return url + kCutPhotoThumbnail61p;
            else if (size > 1024 * 256) // > 256
                return url + kCutPhotoThumbnail71p;
        }

        return url;
    }

    /**
     * 发送自定义广播
     *
     * @param broadcast
     */
    public static void sendReceiver(Context context, String broadcast) {
        sendReceiver(context, broadcast, null);
    }

    /**
     * 发送自定义广播
     *
     * @param broadcast
     * @param params    参数最好是String类型
     */
    public static void sendReceiver(Context context, String broadcast,
                                    Map<String, Object> params) {
        Intent intent = new Intent(broadcast);
        if (params != null) {
            for (String key : params.keySet()) {
                intent.putExtra(key, params.get(key) + "");
            }
        }
        context.sendBroadcast(intent);
    }

    /**
     * 注册自定义广播
     * <p>
     * 请在程序退出时销毁广播：context.unregisterReceiver(broadcastName);
     * </p>
     *
     * @param context
     * @param broadcastName 自定义广播别名
     * @param callback      广播回调函数
     */
    public static BroadcastReceiver registerReceiver(Context context,
                                                     String broadcastName, BroadcastReceiverCallback callback) {
        MyBroadcastReceiver myBroadCastReceiver = new MyBroadcastReceiver(
                callback);
        context.registerReceiver(myBroadCastReceiver, new IntentFilter(
                broadcastName));
        return myBroadCastReceiver;
    }

    /**
     * 注册自定义广播
     * <p>
     * 请在程序退出时销毁广播：context.unregisterReceiver(broadcastName);
     * </p>
     *
     * @param context
     * @param broadcastNames 自定义广播别名(多个过滤)
     * @param callback       广播回调函数
     */
    public static BroadcastReceiver registerReceiver(Context context,
                                                     BroadcastReceiverCallback callback, String... broadcastNames) {
        MyBroadcastReceiver myBroadCastReceiver = new MyBroadcastReceiver(
                callback);
        IntentFilter filter = new IntentFilter();
        for (String name : broadcastNames) {
            filter.addAction(name);
        }
        context.registerReceiver(myBroadCastReceiver, filter);
        return myBroadCastReceiver;
    }

    /**
     * 销毁广播
     *
     * @param context
     * @param receivers
     */
    public static void destroyReceiver(Context context,
                                       BroadcastReceiver... receivers) {
        if (receivers == null)
            return;
        for (BroadcastReceiver receiver : receivers) {
            if (receiver != null) {
                try {
                    context.unregisterReceiver(receiver);
                } catch (Exception e) {
                    //
                }
            }
        }
    }

    /**
     * 重新启动
     */
    public static void reStart(Context mContext) {
//		Intent intent=new Intent();
//		intent.setClassName(mContext.getPackageName(), "com.hhly.mlottery.activity.MainActivity");
//		PendingIntent pendingIntent=PendingIntent.getActivity(mContext.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//		AlarmManager mgr=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//		mgr.set(AlarmManager.RTC, System.currentTimeMillis(),pendingIntent);

        final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /**
     * 复制文本到粘贴板
     *
     * @param content
     * @param context
     */
    public static void copyTexts(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 获取当前日期和前后几天日期，numDay“0”代表今天“1”代表明天“-1”代表昨天 以此类推
     *
     * @param numDay
     * @return
     */
    public static String getDay(int numDay) {
        Calendar cal;
        String day;
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, numDay);
        day = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        return day;

    }


    /**
     * 获取网络时间
     *
     * @param daNumber 代表哪一天
     * @return
     */
    public static String getWebsiteDatetime(int daNumber, long ld) {
        Date date = new Date(ld);// 转换为标准时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);// 输出北京时间
        Calendar canlandar = Calendar.getInstance();
        canlandar.setTime(date);
        canlandar.add(canlandar.DATE, daNumber);
        canlandar.add(canlandar.MONTH, 0);
        return sdf.format(canlandar.getTime());
    }

    public static String requestByGetDay(int day) {
        String dateTime = "";
        try {
            URL url = new URL("http://www.beijing-time.org");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                long ld = urlConn.getDate();// 读取网站日期时间
                dateTime = getWebsiteDatetime(day, ld);
            } else {
                //失败关闭连接
                urlConn.disconnect();
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * 500毫秒禁止点击
     *
     * @return
     */
    public static boolean onDoubClick() {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;

        if (time > 500) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }


    /**
     * 昵称格式 ： 中文数字字母下划线
     */
    private static String REG_NICKNAME = "[\\u4e00-\\u9fa5_a-zA-Z0-9_]{1,15}";
    /**
     * 昵称长度
     **/
    private static int NICKNAME_MAX_LENGTH = 15;
    /**
     * 手机号码格式
     */
    private static String REG_PHONE = "^((14[0-9])|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    /**
     * 密码格式
     */
    private static String REG_PASSWORD = "^[0-9a-zA-Z_]{6,16}$";
    /**
     * 密码最小长度
     */
    private static final int PASSWORD_MIN_LENGTH = 6;
    /**
     * 密码最大长度
     */
    private static final int PASSWORD_MAX_LENGTH = 16;

    /**
     * 检查手机号码是否符合格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(Context ctx, String mobiles) {

        if (TextUtils.isEmpty(mobiles)) {
            toast(ctx.getApplicationContext(), R.string.phone_empty);
            return false;
        }

        Pattern p = Pattern
                .compile(REG_PHONE);
        Matcher m = p.matcher(mobiles);
        boolean match = m.matches();
        if (!match) {
            toast(ctx.getApplicationContext(), R.string.phone_error);
        }
        return match;
    }

    /**
     * 校验验证码
     *
     * @param ctx
     * @param verifycode
     * @return
     */
    public static boolean checkVerifyCode(Context ctx, String verifycode) {

        if (TextUtils.isEmpty(verifycode)) {
            toast(ctx.getApplicationContext(), R.string.verify_code);
            return false;
        }
        return true;
    }

    /**
     * 校验昵称
     *
     * @param ctx
     * @param nickName
     * @return
     */
    public static boolean checkNickname(Context ctx, String nickName) {

        boolean match = false;

        if (TextUtils.isEmpty(nickName)) {
            toast(ctx.getApplicationContext(), R.string.input_nickname);
            return match;
        }
        Pattern p = Pattern
                .compile(REG_NICKNAME);
        Matcher m = p.matcher(nickName);
        boolean temp = m.matches();
        if (temp) {
            try {
                L.i(TAG, nickName + " gbk lenght = " + nickName.getBytes("GBK").length + "");
                if (nickName.getBytes("GBK").length <= NICKNAME_MAX_LENGTH) {
                    match = true;
                }
            } catch (Exception e) {
                L.e(TAG, " 获取昵称 gbk格式字节长度 出错");
                e.printStackTrace();
            }
        }
        if (!match) {
            toast(ctx.getApplicationContext(), R.string.nickname_format);
        }
        return match;
    }

    /**
     * 校验密码
     *
     * @param ctx
     * @param pwd
     * @return
     */
    public static boolean checkPassword(Activity ctx, String pwd) {
        return checkPassword(ctx, pwd, true, "", "");
    }

    /**
     * 校验密码
     *
     * @param ctx
     * @param pwd
     * @param needDefaultToast 是否需要默认提示
     * @param emptyStrId
     * @param errorStrId
     * @return
     */
    public static boolean checkPassword(Activity ctx, String pwd, boolean needDefaultToast, int emptyStrId, int errorStrId) {

        String emptyStr = ctx.getResources().getString(emptyStrId);
        String errorStr = ctx.getResources().getString(errorStrId);

        return checkPassword(ctx, pwd, needDefaultToast, emptyStr, errorStr);
    }

    /**
     * 校验密码
     *
     * @param ctx
     * @param pwd
     * @param needDefaultToast
     * @param emptyStr
     * @param errorStr
     * @return
     */
    public static boolean checkPassword(Context ctx, String pwd, boolean needDefaultToast, String emptyStr, String errorStr) {

        boolean match = false;

        if (TextUtils.isEmpty(pwd)) {
            if (needDefaultToast) {  // 默认提示
                toast(ctx.getApplicationContext(), R.string.pwd_empty);
            } else {
                toast(ctx.getApplicationContext(), emptyStr);
            }
            return match;
        }

        Pattern p = Pattern.compile(REG_PASSWORD);
        Matcher matcher = p.matcher(pwd);
        match = matcher.matches();
        if (!match) {
            if (needDefaultToast) { // 默认提示
                toast(ctx.getApplicationContext(), R.string.pwd_format);
            } else {
                toast(ctx.getApplicationContext(), errorStr);
            }
        }
        return match;
    }


    /**
     * 只检查密码长度 6 - 16 位
     *
     * @param ctx
     * @param pwd
     * @return
     */
    public static boolean checkPassword_JustLength(Activity ctx, String pwd) {
        return checkPassword_JustLength(ctx, pwd, "");
    }

    public static boolean checkPassword_JustLength(Activity ctx, String pwd, String defaultToast) {
        boolean match = false;
        if (!TextUtils.isEmpty(pwd)) {
            if (pwd.length() >= PASSWORD_MIN_LENGTH && pwd.length() <= PASSWORD_MAX_LENGTH) {
                match = true;
            }
        }
        if (!match) {
            if (TextUtils.isEmpty(defaultToast)) {
                // 弹出默认提示
                toast(ctx.getApplicationContext(), R.string.pwd_format_just_length);
            } else {
                // 弹出传入的提示
                toast(ctx.getApplicationContext(), defaultToast);
            }
        }
        return match;
    }

    /**
     * 根据传过来的日期获取前6后7的日期
     *
     * @param currentDate 当前日期
     * @param day         ”1“代表明天
     * @return
     */
    public static String getDate(String currentDate, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date dt = sdf.parse(currentDate, new ParsePosition(0));
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DATE, day);//你要加减的日期
        Date dt1 = rightNow.getTime();
        return sdf.format(dt1);
    }
}