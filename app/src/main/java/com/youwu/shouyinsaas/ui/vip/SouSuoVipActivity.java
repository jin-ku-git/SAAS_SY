package com.youwu.shouyinsaas.ui.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;

import com.youwu.shouyinsaas.databinding.ActivitySouSuoVipBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.ui.bean.VipBean;

import com.youwu.shouyinsaas.ui.money.VipRechargeActivity;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 搜索会员页面
 * 2022/03/23
 */
public class SouSuoVipActivity extends BaseActivity<ActivitySouSuoVipBinding, SouSuoVipViewModel> {
    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    private int type;//1 搜索会员 2 显示会员信息
    private int  pay_type;//1 点击余额支付来的
    Intent intent;
    VipBean vipBean;
    @Override
    public void initParam() {
        super.initParam();
        type = getIntent().getIntExtra("type",0);
        pay_type = getIntent().getIntExtra("pay_type",0);
        vipBean= (VipBean) getIntent().getSerializableExtra("VipBean");
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sou_suo_vip;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SouSuoVipViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SouSuoVipViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://选择会员

                        if (vipBean!=null){
                            intent = new Intent();
                            intent.putExtra("vipBean", vipBean);
                            setResult(RESULT_OK, intent);
                        }
                        finish();

                        break;
                    case 2://充值

//                        RxToast.normal("敬请期待");
                        intent = new Intent(SouSuoVipActivity.this, VipRechargeActivity.class);
                        intent.putExtra("user_id",viewModel.entity.get().getId()+"");
                        startActivityForResult(intent,300);
                        break;
                    case 3://搜索
                        viewModel.vip_details_state.set(true);
                        break;
                }
            }
        });

        viewModel.vipBeanSingleLiveEvent.observe(this, new Observer<VipBean>() {
            @Override
            public void onChanged(VipBean vipBean) {
                if (type==1){

                    vipBean.setType_state(1);
                    intent = new Intent();
                    intent.putExtra("vipBean", vipBean);
                    if (pay_type==1){
                        intent.putExtra("pay_type", 1);
                    }

                    setResult(RESULT_OK, intent);
                    finish();
                }else {

                    vipBean.setType_state(2);
                    intent = new Intent();
                    intent.putExtra("vipBean", vipBean);
                    if (pay_type==1){
                        intent.putExtra("pay_type", 1);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        int displays= AppApplication.spUtils.getInt("displays");
        if (displays>1){
            initDisplay();
        }
        viewModel.type_state.set(type);

        // 可以调用该方法，设置不允许滑动退出
        setSwipeBackEnable(false);


        if (vipBean!=null){
            viewModel.entity.set(vipBean);
            viewModel.vip_tel.set(vipBean.getUser_name());
        }
        if (type==1){
            viewModel.vip_details_state.set(false);
        }else {
            viewModel.vip_details_state.set(true);
        }

        binding.souSuoEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_GO){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    viewModel.user_info(binding.souSuoEditText.getText().toString());

                    return true;
                }
                return false;
            }
        });

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

    /**
     * 接收传递的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 300://从选择会员页面传的数据
//                    VipBean vipBean = (VipBean) data.getSerializableExtra("vipBean");

                    viewModel.user_info(viewModel.vip_tel.get());
                    break;
            }
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
