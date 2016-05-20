package com.hhly.mlottery.util.net;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;

import java.util.HashMap;
import java.util.Map;

/**
 * Volley封装类
 */
public class VolleyContentFast {

    public final static String TAG = "VolleyContentFast";

    public final static int ERROR_CODE_RESPONSE_NULL = 1001;
    public final static int ERROR_CODE_JSON_PARSE = 1002;
    public final static int ERROR_CODE_VOLLEY_ERROR = 1003;

    private static RequestQueue mQueue;

    /**
     * 初始化，在Application上初始化，只初始化一次赛
     *
     * @param context
     */
    public static void init(Context context) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
    }

    /**
     * 网络请求，GET方式，返回json对应实体类(无参数)
     *
     * @param url             链接
     * @param successlistener 成功回调接口
     * @param errorListener   错误回掉接口
     * @param clazz           json对应实体类
     * @param <T>             泛型
     */
    public static <T> void requestJsonByGet(String url, final ResponseSuccessListener<T> successlistener, final ResponseErrorListener errorListener, final Class clazz) {
        Map<String, String> params = new HashMap<>();
        requestJsonByGet(url, params, successlistener, errorListener, clazz);
    }


    /**
     * 网络请求，GET方式，返回json对应实体类
     *
     * @param url             链接
     * @param params          参数
     * @param successlistener 成功回调接口
     * @param errorListener   错误回掉接口
     * @param clazz           json对应实体类
     * @param <T>             泛型
     */
    public static <T> void requestJsonByGet(final String url, Map<String, String> params, final ResponseSuccessListener<T> successlistener, final ResponseErrorListener errorListener, final Class clazz) {

        String urlLanguage = appendLanguage(url);

        if (params != null) {
            for (String key : params.keySet()) {
                if (urlLanguage.contains("?")) {
                    urlLanguage += ("&" + key + "=" + params.get(key));
                } else {
                    urlLanguage += ("?" + key + "=" + params.get(key));
                }
            }
        }

        L.e(TAG, "request url = " + urlLanguage);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLanguage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.e(TAG, "request success.");
                L.e(TAG, "response = " + response);

                if (response == null) {
                    VolleyException volleyException = new VolleyException();
                    volleyException.setErrorCode(ERROR_CODE_RESPONSE_NULL);
                    errorListener.onErrorResponse(volleyException);
                    return;
                }
                T object = null;
                try {
                    object = (T) JSON.parseObject(response, clazz);
                } catch (Exception e) {
                    L.e(TAG, e.getMessage());
                    VolleyException volleyException = new VolleyException();
                    volleyException.setErrorCode(ERROR_CODE_JSON_PARSE);
                    errorListener.onErrorResponse(volleyException);
                    return;
                }

                successlistener.onResponse(object);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                L.e(TAG, "request " + url + " error,error msg = " + volleyError.getMessage() + ".");
                L.e(TAG, "request " + url + " error,error msg = " + volleyError.getMessage() + ".");

                VolleyException volleyException = new VolleyException();
                volleyException.setErrorCode(ERROR_CODE_VOLLEY_ERROR);
                volleyException.setVolleyError(volleyError);
                errorListener.onErrorResponse(volleyException);
            }
        });


        mQueue.add(stringRequest);
    }

    /**
     * 网络请求，POST方式，返回json对应实体类(无参数)
     *
     * @param url             链接
     * @param successlistener 成功回调接口
     * @param errorListener   错误回掉接口
     * @param clazz           json对应实体类
     * @param <T>             泛型
     */
    public static <T> void requestJsonByPost(String url, final ResponseSuccessListener<T> successlistener, final ResponseErrorListener errorListener, final Class clazz) {

        Map<String, String> params = new HashMap<>();
        requestJsonByPost(url, params, successlistener, errorListener, clazz);
    }

    /**
     * 网络请求，POST方式，返回json对应实体类
     *
     * @param url             链接
     * @param params          参数
     * @param successlistener 成功回调接口
     * @param errorListener   错误回掉接口
     * @param clazz           json对应实体类
     * @param <T>             泛型
     */
    public static <T> void requestJsonByPost(final String url, final Map<String, String> params, final ResponseSuccessListener<T> successlistener, final ResponseErrorListener errorListener, final Class clazz) {

        appendMapLanguage(params);
        L.d(TAG, "method post url = " + url);
        if (params != null && params.size() != 0) {
            for (String key : params.keySet()) {
                L.d(TAG, "key = " + key + " param = " + params.get(key));
            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.e(TAG, "request success.");
                L.e(TAG, "response = " + response);

                if (response == null) {
                    VolleyException volleyException = new VolleyException();
                    volleyException.setErrorCode(ERROR_CODE_RESPONSE_NULL);
                    errorListener.onErrorResponse(volleyException);
                    return;
                }
                Object object = null;
                try {
                    object = JSON.parseObject(response, clazz);

                } catch (Exception e) {
                    L.e(TAG, e.getMessage());
                    VolleyException volleyException = new VolleyException();
                    volleyException.setErrorCode(ERROR_CODE_JSON_PARSE);
                    errorListener.onErrorResponse(volleyException);
                    return;
                }

                successlistener.onResponse((T) object);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e(TAG, "request " + url + " error,error msg = " + volleyError.getMessage() + ".");
                VolleyException volleyException = new VolleyException();
                volleyException.setErrorCode(ERROR_CODE_VOLLEY_ERROR);
                volleyException.setVolleyError(volleyError);
                errorListener.onErrorResponse(volleyException);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        mQueue.add(stringRequest);
    }

    /**
     * 网络请求，GET方式，返回没有转化的string（无参）
     *
     * @param url
     * @param successlistener
     * @param errorListener
     */
    public static void requestStringByGet(String url, final ResponseSuccessListener<String> successlistener, final ResponseErrorListener errorListener) {
        requestStringByGet(url, null, successlistener, errorListener);
    }

    /**
     * 网络请求，GET方式，返回没有转化的string
     *
     * @param url             链接
     * @param params          参数
     * @param successlistener 成功回调接口
     * @param errorListener   错误回掉接口
     */
    public static void requestStringByGet(final String url, Map<String, String> params, final ResponseSuccessListener<String> successlistener, final ResponseErrorListener errorListener) {
        String tempUrl = appendLanguage(url);
        if (params != null) {
            for (String key : params.keySet()) {
                if (tempUrl.contains("?")) {
                    tempUrl += ("&" + key + "=" + params.get(key));
                } else {
                    tempUrl += ("?" + key + "=" + params.get(key));
                }
            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.e(TAG, "request success.");
                L.e(TAG, "response = " + response);

                if (response == null) {
                    VolleyException volleyException = new VolleyException();
                    volleyException.setErrorCode(ERROR_CODE_RESPONSE_NULL);
                    errorListener.onErrorResponse(volleyException);
                    return;
                }


                successlistener.onResponse(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e(TAG, "request " + url + " error,error msg = " + volleyError.getMessage() + ".");
                VolleyException volleyException = new VolleyException();
                volleyException.setErrorCode(ERROR_CODE_VOLLEY_ERROR);
                volleyException.setVolleyError(volleyError);
                errorListener.onErrorResponse(volleyException);
            }
        });
        mQueue.add(stringRequest);
    }

    /**
     * 网络请求，POST方式，返回没有转化的string
     *
     * @param url             链接
     * @param params          参数
     * @param successlistener 成功回调接口
     * @param errorListener   错误回掉接口
     */
    public static void requestStringByPost(String url, final Map<String, String> params, final ResponseSuccessListener<String> successlistener, final ResponseErrorListener errorListener) {
        appendMapLanguage(params);
        L.d(TAG, "method post url = " + url);
        if (params != null && params.size() != 0) {
            for (String key : params.keySet()) {
                L.d(TAG, "key = " + key + " param = " + params.get(key));
            }
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.e(TAG, "request success.");
                L.e(TAG, "response = " + response);
                if (response == null) {
                    VolleyException volleyException = new VolleyException();
                    volleyException.setErrorCode(ERROR_CODE_RESPONSE_NULL);
                    errorListener.onErrorResponse(volleyException);
                    return;
                }

                successlistener.onResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyException volleyException = new VolleyException();
                volleyException.setErrorCode(ERROR_CODE_VOLLEY_ERROR);
                volleyException.setVolleyError(volleyError);
                errorListener.onErrorResponse(volleyException);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        mQueue.add(stringRequest);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param listener      成功回掉接口
     * @param maxWidth      图片最大宽度，图片超过最大宽度则压缩，传0就不进行压缩
     * @param maxHeight     图片最大高度，图片超过最大高度则压缩，传0就不进行压缩
     * @param decodeConfig  编码设置
     * @param errorListener 失败回调接口
     */
    public static void requestImage(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        ImageRequest imageRequest = new ImageRequest(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);

        mQueue.add(imageRequest);
    }

    /**
     * 添加语言类型
     *
     * @param url
     * @return
     */
    private static String appendLanguage(String url) {

        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            url = url + "?" + BaseURLs.LANGUAGE_PARAM + "=" + BaseURLs.LANGUAGE_SWITCHING_CN;
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            url = url + "?" + BaseURLs.LANGUAGE_PARAM + "=" + BaseURLs.LANGUAGE_SWITCHING_TW;
        } else if (MyApp.isLanguage.equals("rEN")) {
            // 如果是英文环境
            url = url + "?" + BaseURLs.LANGUAGE_PARAM + "=" + BaseURLs.LANGUAGE_SWITCHING_EN;
        } else if (MyApp.isLanguage.equals("rKO")) {
            // 如果是韩语环境
            url = url + "?" + BaseURLs.LANGUAGE_PARAM + "=" + BaseURLs.LANGUAGE_SWITCHING_KO;
        } else if (MyApp.isLanguage.equals("rID")) {
            // 如果是印尼语
            url = url + "?" + BaseURLs.LANGUAGE_PARAM + "=" + BaseURLs.LANGUAGE_SWITCHING_ID;
        } else if (MyApp.isLanguage.equals("rTH")) {
            // 如果是泰语
            url = url + "?" + BaseURLs.LANGUAGE_PARAM + "=" + BaseURLs.LANGUAGE_SWITCHING_TH;
        } else if (MyApp.isLanguage.equals("rVI")) {
            // 如果是越南语
            url = url + BaseURLs.LANGUAGE_PARAM + "=" + BaseURLs.LANGUAGE_SWITCHING_VI;
        }

        return url;
    }

    /**
     * 添加语言类型
     *
     * @param params 参数
     * @return
     */
    private static void appendMapLanguage(Map params) {
        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            params.put("lang", BaseURLs.LANGUAGE_SWITCHING_CN);
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            params.put("lang", BaseURLs.LANGUAGE_SWITCHING_TW);
        } else if (MyApp.isLanguage.equals("rEN")) {
            // 如果是英文环境
            params.put("lang", BaseURLs.LANGUAGE_SWITCHING_EN);
        } else if (MyApp.isLanguage.equals("rKO")) {
            // 如果是韩语环境
            params.put("lang", BaseURLs.LANGUAGE_SWITCHING_KO);
        } else if (MyApp.isLanguage.equals("rID")) {
            // 如果是印尼语
            params.put("lang", BaseURLs.LANGUAGE_SWITCHING_ID);
        } else if (MyApp.isLanguage.equals("rTH")) {
            // 如果是泰语
            params.put("lang", BaseURLs.LANGUAGE_SWITCHING_TH);
        } else if (MyApp.isLanguage.equals("rVI")) {
            // 如果是越南语
            params.put("lang", BaseURLs.LANGUAGE_SWITCHING_VI);
        }

    }


    public interface ResponseSuccessListener<T> {
        public void onResponse(T jsonObject);
    }


    public interface ResponseErrorListener {

        public void onErrorResponse(VolleyException exception);
    }


    /**
     * 网络异常类
     */
    public static class VolleyException {

        private int errorCode;//错误码
        private VolleyError volleyError;//volley自身抛出的异常，使用时注意只有在错误码等于ERROR_CODE_VOLLEY_ERROR时，该变量才不为空。

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public VolleyError getVolleyError() {
            return volleyError;
        }

        public void setVolleyError(VolleyError volleyError) {
            this.volleyError = volleyError;
        }

        public int getErrorCode() {
            return errorCode;
        }
    }

}
