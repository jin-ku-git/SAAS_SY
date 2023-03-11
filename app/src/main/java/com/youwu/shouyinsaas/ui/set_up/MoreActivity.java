package com.youwu.shouyinsaas.ui.set_up;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;

import androidx.lifecycle.ViewModelProviders;

import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityMoreBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.util.ScanUtils;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 更多页面
 * 2022/08/01
 */
public class MoreActivity extends BaseActivity<ActivityMoreBinding, MoreViewModel> {

    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_more;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MoreViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(MoreViewModel.class);
    }

    @Override
    public void initViewObservable() {

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();

        int displays= AppApplication.spUtils.getInt("displays");
        if (displays>1){
            initDisplay();
        }
    }

    //    初始化副屏
    public void initDisplay() {

        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            return;
        }

        screenManager.init(this);
        // 获取真实存在的副屏   初始化副屏
        Display display = screenManager.getPresentationDisplays();
        if (display != null && null == showShopingDisplay) {
            showShopingDisplay = new ShowShopingDisplay(this, display);
            showShopingDisplay.show();
            showShopingDisplay.initBannnerView();
        } else {
            if (!showShopingDisplay.isShow) {
                showShopingDisplay.show();
                showShopingDisplay.initBannnerView();
            } else if (null != showShopingDisplay) {
                showShopingDisplay.initBannnerView();
            }
        }
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (ScanUtils.getInstance().isInputFromScanner(MoreActivity.this, event)) {

            //暂时取消扫码
            ScanUtils.getInstance().setOnResultListener(scanListener);
            ScanUtils.getInstance().analysisKeyEvent(event);
            return false;
        }

        return false;
    }
    ScanUtils.OnResultListener scanListener = new ScanUtils.OnResultListener() {
        @Override
        public void onResult(final String resultStr) {
            Log.e("scanToWork", "onResult:" + resultStr);

            if (TextUtils.isEmpty(resultStr)) {
                return;
            }

            long last = AppApplication.spUtils.getLong("lastpay");
            if (System.currentTimeMillis() - last < 2000) {
                return;
            }
            AppApplication.spUtils.put("lastpay", System.currentTimeMillis());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scanToWork(resultStr);
                }
            });
        }
    };


    private void scanToWork(String resultStr) {

//        RxToast.normal("扫描信息：" + resultStr);
        if (resultStr.startsWith("xzks")) {
            String order_sn=resultStr.replace("xzks_","");
            viewModel.close_order(order_sn, MoreActivity.this);

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }
}
