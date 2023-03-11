package com.youwu.shouyinsaas.ui.set_up;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityStoreSetUpBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.ui.set_up.bean.SettingBean;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * @author: Administrator
 * @date: 2022/8/12
 */
public class StoreSetUpActivity extends BaseActivity<ActivityStoreSetUpBinding, StoreSetUpViewModel> {
    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    private int type;
    private TimePickerView pvCustomTime;//时间选择器

    private int time_state;//1 开始 2 结束
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_store_set_up;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public StoreSetUpViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(StoreSetUpViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://开始时间点击事件
                        time_state=1;
                        pvCustomTime.show(); //弹出自定义时间选择器
                        break;
                    case 2://结束时间点击事件
                        time_state=2;
                        pvCustomTime.show(); //弹出自定义时间选择器
                        break;
                    case 3://点击确认
                        KLog.d("是否自动接单："+((type==1)?"是":"否")+"\n营业时间："+viewModel.start_time.get()+"至"+viewModel.end_time.get());
                        viewModel.update_store(type);
                    case 4://
                        RxToast.showTipToast(StoreSetUpActivity.this, "更新成功");

                        break;
                    case 5:

                        setHide_radius();
                        break;

                }
            }
        });
        viewModel.singleLiveEvent.observe(this, new Observer<SettingBean>() {
            @Override
            public void onChanged(SettingBean settingBean) {
                type=settingBean.getIs_order();
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

        viewModel.setting_list();

        setHide_radius();

        binding.hideRadiusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.hide_radius_yes:
                        type=1;
                        break;

                    case R.id.hide_radius_no:
                        type=2;
                        break;

                    default:
                        break;
                }
            }
        });

        initCustomTimePicker();
        setHide_radius();
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

    private void setHide_radius() {
        if (type==1){
            binding.hideRadiusYes.setChecked(true);
        }else {
            binding.hideRadiusNo.setChecked(true);
        }
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:00");
        return format.format(date);
    }


    private void initCustomTimePicker() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2009, 0, 1);
        Calendar endDate = Calendar.getInstance();
//        endDate.set(2299, 11, 31);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                if (time_state==1){
                    viewModel.start_time.set(getTime(date));
                }else {
                    viewModel.end_time.set(getTime(date));
                }


            }
        })
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})//分别对应年月日时分秒，默认全部显示
                .setContentTextSize(28)//滚轮文字大小
                .setTitleSize(26)//标题文字大小
                .setLineSpacingMultiplier(2.0f)//设置间距倍数
                .setItemVisibleCount(7)//设置最大可见数目
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFfafafa)//滚轮背景颜色 Night mode
//                .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isDialog(true)//是否显示为对话框样式
                .build();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (ScanUtils.getInstance().isInputFromScanner(StoreSetUpActivity.this, event)) {
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
        if (resultStr.startsWith("xzks")) {
            String order_sn=resultStr.replace("xzks_","");
            viewModel.close_order(order_sn, StoreSetUpActivity.this);

        }
    }
}