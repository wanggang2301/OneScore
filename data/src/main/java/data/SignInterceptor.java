package data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import data.utils.L;
import data.utils.Sign;
import data.utils.StaticValues;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @anthor   wangg
 * @name     SignInterceptor
 * @created  2017/6/12 16:12
 * @desc     签名的Interceptor
 */

public class SignInterceptor implements okhttp3.Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl oldUrl = oldRequest.url();

        Map<String, String> map = new HashMap<>();

        boolean isSign = false;

        if (oldUrl.queryParameterNames().contains(StaticValues.SIGN)) {  //存在需要签名的sign
            for (String name : oldUrl.queryParameterNames()) {
                if (!StaticValues.SIGN.equals(name)) {
                    map.put(name, oldUrl.queryParameter(name));
                }
            }
            isSign = true;
        }


        HttpUrl url;

        if (isSign) {
            url = oldUrl.newBuilder()
                    .setQueryParameter(StaticValues.SIGN, Sign.getSign(oldUrl.encodedPath(), map))
                    .build();

        } else {
            url = oldUrl.newBuilder().build();
        }

        L.d("signurl", "____" + url);


        Request.Builder requestBuilder = oldRequest.newBuilder().url(url);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
