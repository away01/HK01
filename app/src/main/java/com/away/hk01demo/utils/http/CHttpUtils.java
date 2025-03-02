package com.away.hk01demo.utils.http;

import android.util.Log;

import com.away.hk01demo.base.BaseApplication;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CHttpUtils {

    private static final String TAG = "JsonCallback";

    private static CHttpUtils sInstance;

    private StringBuffer mStrBuffer;

    private CHttpUtils() {
    }

    public static CHttpUtils getInstance() {
        if (sInstance == null) {
            synchronized (CHttpUtils.class) {
                if (sInstance == null) {
                    sInstance = new CHttpUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * POST
     *
     * @param url      URL
     * @param paramMap POST JSON参数
     * @param tag      tag
     * @param callback callback
     * @param <T>
     */
    public <T> void requestDataFromServer(String url, Map<String, Object> paramMap, Object tag, JsonCallback<T> callback) {
        String reqUrl = url.contains("http") ? url : UrlConfig.BASE_URL + url;
        Map<String, Object> map = buildRequestJson(paramMap);
        Log.e(TAG, "Request url: " + reqUrl + "\n" + "Request params: " + new JSONObject(map).toString());
        PostRequest<T> params = OkGo.<T>post(reqUrl).retryCount(0).tag(tag);
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            params.params(entry.getKey(), String.valueOf(entry.getValue()));
        }
        params.execute(callback);
    }

    /**
     * GET
     *
     * @param url      URL
     * @param tag      tag
     * @param callback callback
     * @param <T>
     */
    public <T> void requestGetFormServer(String url, Object tag, JsonCallback<T> callback) {
        String reqUrl = url.contains("http") ? url : UrlConfig.BASE_URL + url;
        OkGo.<T>get(reqUrl).tag(tag).execute(callback);
    }

    private Map<String, Object> buildRequestJson(Map<String, Object> paramMap) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        return paramMap;
    }

    public void cancelRequest(Object tag) {
        OkGo.cancelTag(BaseApplication.getInstance().getOkHttpClient(), tag);
    }

    public void cancelRequestAll() {
        OkGo.cancelAll(BaseApplication.getInstance().getOkHttpClient());
    }

}
