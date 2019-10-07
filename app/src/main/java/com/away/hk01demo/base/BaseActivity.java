package com.away.hk01demo.base;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.away.hk01demo.R;
import com.away.hk01demo.common.CommonLoadingDialog;
import com.away.hk01demo.common.CommonTouchListener;
import com.away.hk01demo.common.Constant;
import com.away.hk01demo.utils.RuntimeRationale;
import com.away.hk01demo.utils.ToastUtil;
import com.away.hk01demo.utils.http.CHttpUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.bakumon.statuslayoutmanager.library.DefaultOnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 *
 * desc:BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public ToastUtil toast;
    private Unbinder mUnBinder;
    protected boolean isActivityFinish;
    public StatusLayoutManager mStatusLayoutManager;
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    private View decorView;

    private List<CommonTouchListener> touchListeners = new ArrayList<>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     */
    public void registerCommonTouchListener(CommonTouchListener listener) {
        touchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     */
    public void unRegisterMyTouchListener(CommonTouchListener listener) {
        touchListeners.remove(listener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationBarColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            disableAutoFill();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        windowSetting();
        setContentView(getLayoutResId());
        toast = new ToastUtil(this);
        com.away.hk01demo.base.BaseApplication.getInstance().addActivity(this);
        mUnBinder = ButterKnife.bind(this);


        setListener();
        initEvent();
        savedInstanceState(savedInstanceState);
    }

    public void windowSetting() {

    }

    public void savedInstanceState(Bundle savedInstanceState) {

    }
    protected abstract int getLayoutResId();

    public void setListener() {

    }

    protected abstract void initEvent();

    public void onMultiClick(View view) {

    }

    @Override
    public void onClick(View view) {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            lastClickTime = curClickTime;
            onMultiClick(view);
        }
    }

    protected void bindingRecyclerView(RecyclerView recyclerView) {
        mStatusLayoutManager = new StatusLayoutManager.Builder(recyclerView)
                /* .setLoadingLayout(R.layout.layout_status_loading)
                 .setEmptyLayout(R.layout.layout_status_empty)
                 .setErrorLayout(R.layout.layout_status_error)*/
                //.setErrorClickViewID(R.id.tv_refresh)
                .setOnStatusChildClickListener(new DefaultOnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {
                        BaseActivity.this.onEmptyChildClick(view);
                    }

                    @Override
                    public void onErrorChildClick(View view) {
                        BaseActivity.this.onErrorChildClick(view);
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                        BaseActivity.this.onCustomerChildClick(view);
                    }
                })
                .build();
    }

    protected void onEmptyChildClick(View view) {

    }

    protected void onErrorChildClick(View view) {

    }

    protected void onCustomerChildClick(View view) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (CommonTouchListener listener : touchListeners) {
            listener.onTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void disableAutoFill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    protected void requestPermission(String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(granted)
                .onDenied(denied)
                .start();
    }

    protected void requestPermission(String[]... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(granted)
                .onDenied(denied)
                .start();
    }


    private Action<List<String>> granted = permissions -> {
        List<String> permissionText = Permission.transformText(BaseActivity.this, permissions);
        onPermissionGrant(permissionText);
    };
    private Action<List<String>> denied = permissions -> {
        if (AndPermission.hasAlwaysDeniedPermission(BaseActivity.this, permissions)) {
            showSettingDialog(BaseActivity.this, permissions);
        }
    };

    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = null;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < permissionNames.size(); i++) {
            String permission = permissionNames.get(i);
            if (i == permissionNames.size() - 1) {
                buffer.append(permission);
            } else {
                buffer.append(permission + ",");
            }
        }
        message = context.getString(R.string.message_permission_always_failed, buffer.toString().trim());
        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton(R.string.setting, (dialog, which) -> setPermission())
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                })
                .show();
    }

    private void setPermission() {
        AndPermission.with(this).runtime().setting().start(Constant.REQUEST_CODE_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    public void onPermissionGrant(List<String> permissions) {

    }

    public void showLoading(String loadingText, boolean canBack) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constant.TAG_LOADING_DIALOG);
        if (fragment == null) {
            fragment = CommonLoadingDialog.newInstance(loadingText, canBack);
        }
        CommonLoadingDialog dialog = (CommonLoadingDialog) fragment;
        if (!dialog.isAdded()) {
            dialog.show(getSupportFragmentManager(), Constant.TAG_LOADING_DIALOG);
        } else {
            dialog.dismissAllowingStateLoss();
        }
    }

    public void hideLoading() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constant.TAG_LOADING_DIALOG);
        if (fragment == null || !(fragment instanceof CommonLoadingDialog)) return;
        CommonLoadingDialog dialog = (CommonLoadingDialog) fragment;
        dialog.dismissAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        CHttpUtils.getInstance().cancelRequest(this);
        isActivityFinish = true;
        com.away.hk01demo.base.BaseApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 获取String资源文件
     *
     * @param ids String id
     * @return
     */
    public String $$(int ids) {
        return getResources().getString(ids);
    }


}
