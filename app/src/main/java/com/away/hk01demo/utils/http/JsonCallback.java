package com.away.hk01demo.utils.http;

import android.util.Log;
import android.widget.Toast;

import com.away.hk01demo.base.BaseApplication;
import com.away.hk01demo.utils.ToolsUtil;
import com.google.gson.Gson;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;


public abstract class JsonCallback<T> extends AbsCallback<T> {

    private static final String TAG = "JsonCallback";

    private Type type;
    private Class<T> clazz;
    private boolean convertBean; // 是否转换成bean对象

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this(clazz, true);
    }

    public JsonCallback(Class<T> clazz, boolean convertBean) {
        this.clazz = clazz;
        this.convertBean = convertBean;
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        if (!convertBean) {
            return (T) body;
        }

        T data;
        Gson gson = new Gson();
        String bodyStr = body.string();
        HttpUrl requestUrl = null;
        if (response != null && response.request() != null) {
            requestUrl = response.request().url();
        }
        String jsonStr;
        if (ToolsUtil.isValidJson(bodyStr)) {
            jsonStr = bodyStr;
        } else {
            jsonStr = ToolsUtil.decryptData(bodyStr);
        }

        Log.e(TAG, "Original response -> request url: " + requestUrl + "    ->Response: " + jsonStr + "\n");
        if (type != null) {
            data = gson.fromJson(jsonStr, type);
        } else if (clazz != null) {
            data = gson.fromJson(jsonStr, clazz);
        } else {
            Type genType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            data = gson.fromJson(jsonStr, type);
        }
        processReturnData(data);
        return data;
    }

    private void processReturnData(T data) {
        if (data == null) return;
    }

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        if (response.code() != 200) {
            Toast.makeText(BaseApplication.getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
        }
    }
}
