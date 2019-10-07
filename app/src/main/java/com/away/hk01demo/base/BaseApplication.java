package com.away.hk01demo.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.away.hk01demo.BuildConfig;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class BaseApplication extends MultiDexApplication {

    private static Context sContext;
    private static BaseApplication sInstance;

    private OkHttpClient mOKHttpClient;
    private List<BaseActivity> mActivityList;

    public SQLiteDatabase database;

    public static BaseApplication getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sContext = getApplicationContext();
        initOkHttp();

        LitePal.initialize(this);
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this);//初始化Stetho
        database = LitePal.getDatabase();
    }

    public OkHttpClient getOkHttpClient() {
        return mOKHttpClient;
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory,sslParams1.trustManager);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.NONE);
        loggingInterceptor.setColorLevel(Level.INFO);
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(30000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(30000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(30000, TimeUnit.MILLISECONDS);
        mOKHttpClient = builder.build();
        OkGo.getInstance().setOkHttpClient(mOKHttpClient);
    }

    public void addActivity(BaseActivity activity) {
        if (mActivityList == null) {
            mActivityList = new ArrayList<>();
        }
        mActivityList.add(activity);
    }

    public void removeActivity(BaseActivity activity) {
        if (mActivityList != null && mActivityList.contains(activity)) {
            mActivityList.remove(activity);
        }
    }

    public void clearActivityList() {
        if (mActivityList != null && !mActivityList.isEmpty()) {
            for (BaseActivity activity : mActivityList) {
                activity.finish();
            }
            mActivityList = null;
        }
    }

    public void quitApp() {
        clearActivityList();
    }

}
