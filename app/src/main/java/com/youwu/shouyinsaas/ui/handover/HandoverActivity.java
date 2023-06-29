package com.youwu.shouyinsaas.ui.handover;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lxj.xpopup.XPopup;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.app.UserUtils;
import com.youwu.shouyinsaas.databinding.ActivityHandoverBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.ui.handover.bean.DaySalesBean;
import com.youwu.shouyinsaas.ui.handover.bean.HandoverBean;
import com.youwu.shouyinsaas.ui.handover.popup.HandOverPopup;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

import static com.youwu.shouyinsaas.ui.main.MainActivity.printerPresenter;

/**
 * 交接班页面
 * 2022/03/23
 */
public class HandoverActivity extends BaseActivity<ActivityHandoverBinding, HandoverViewModel> {
    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    private String store_id;//门店id

    TextView start_time;//日志报表开始时间
    TextView end_time;//日志报表结束时间

    private int time_state;//1 开始 2 结束

    private TimePickerView pvCustomTime;//时间选择器

    HandoverBean handoverBeans;

    DaySalesBean daySalesBean;//日志数据

    int widths;//屏幕长
    int height;//屏幕宽

    HandOverPopup dialog_cabinet;

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_handover;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HandoverViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(HandoverViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        int displays= AppApplication.spUtils.getInt("displays");
        if (displays>1){
            initDisplay();
        }
        //默认勾选
        binding.check.setChecked(true);

        viewModel.logo_time.set(UserUtils.getLogoTime());
        viewModel.logo_name.set(UserUtils.getLogoName());

        store_id= AppApplication.spUtils.getString("StoreId");

        initCustomTimePicker();


        String start= date2TimeStamp(viewModel.logo_time.get(),"yyyy.MM.dd HH:mm:ss");
        long end= new Date().getTime()/1000;
        KLog.d("开始时间："+start+"\n结束时间："+end);

//        viewModel.shift_change(start,end+"");


        viewModel.new_day_sales(store_id);
        getScreenSize();

    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://盘点现金

                        dialog_cabinet =new HandOverPopup(HandoverActivity.this,handoverBeans);

                        new  XPopup.Builder(HandoverActivity.this)
                                .maxWidth((int) (widths * 0.7))
                                .maxHeight((int) (height*0.8))
                                .autoOpenSoftInput(false)
                                .asCustom(dialog_cabinet)
                                .show();
                        //确认的回调
                        dialog_cabinet.setOnConfirmListener(new HandOverPopup.OnConfirmListener() {
                            @Override
                            public void onConfirm(String total_amount_list, String number_list, String amount_list, String other_money_list, String cash_amount, String cash_total_amount) {

                                //确认交接并退出
//                                viewModel.add_shift_change(total_amount_list,number_list,amount_list,other_money_list,cash_amount,cash_total_amount);
                                //提交日结
                                viewModel.new_update_day_sales(total_amount_list,number_list,amount_list,other_money_list,cash_amount);

                            }
                        });
                        break;
                        case 2://销售商品列表
                            startActivity(SaleGoodsListActivity.class);
                        break;
                        case 3://日结
                            showJournalDialog();
                        break;
                    case 4:
                        RxToast.normal("提交成功");

                        int printType = AppApplication.spUtils.getInt("printType", 0);
                        if (printerPresenter == null) {
//            AppApplication.mSpeechSynthesizer.speak("状态为空");
                            AppApplication.mSpeechSynthesizer.speak("请连接打印机");

                        }else {
                            if (printType == 1) {
                                printerPresenter.handOverDayEndPrint(daySalesBean);
                            }
                        }
                        dialog_Journal.dismiss();
                        break;
                    case 5://提交日志返回信息
                        dialog_cabinet.dismiss();
                        RxToast.showTipToast(HandoverActivity.this,"提交成功！");


                        break;

                }
            }
        });

        viewModel.handoverBeanSingleLiveEvent.observe(this, new Observer<HandoverBean>() {
            @Override
            public void onChanged(HandoverBean handoverBean) {
                handoverBeans=handoverBean;
            }
        });
        //日志报表返回数据
        viewModel.daySalesBeanSingleLiveEvent.observe(this, new Observer<DaySalesBean>() {
            @Override
            public void onChanged(DaySalesBean bean) {
                daySalesBean=bean;

                initBinding(bean);
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
     * 获取屏幕长和高
     */
    private void getScreenSize() {
        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widths = size.x;
        height = size.y;
    }

    TextView total_amount;//销售总额
    TextView pay_amount;//实收总额
    TextView wx_total_amount;//销售微信总额
    TextView zfb_total_amount;//销售支付宝总额
    TextView xj_total_amount;//销售现金总额
    TextView wm_total_amount;//销售美团总额
    TextView zh_total_amount;//销售组合总额
    TextView balance_total_amount;//销售余额总额

    TextView wx_amount;//实收微信总额
    TextView zfb_amount;//实收支付宝总额
    TextView xj_amount;//实收现金总额
    TextView wm_amount;//实收美团总额
    TextView balance_amount;//实收余额总额


    Dialog dialog_Journal;//日志弹窗
    /**
     * 日结弹窗
     */
    private void showJournalDialog() {

        dialog_Journal = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_journal, null);
        //将界面填充到AlertDiaLog容器
        dialog_Journal.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog_Journal.getWindow().setGravity(Gravity.CENTER);
        dialog_Journal.setCancelable(false);//点击外部消失弹窗
        dialog_Journal.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//返回
        TextView handover_text = (TextView) dialogView.findViewById(R.id.handover_text);//交接班并退出
         start_time = (TextView) dialogView.findViewById(R.id.start_time);
         end_time = (TextView) dialogView.findViewById(R.id.end_time);

        total_amount = (TextView) dialogView.findViewById(R.id.total_amount);
        pay_amount = (TextView) dialogView.findViewById(R.id.pay_amount);

        wx_total_amount = (TextView) dialogView.findViewById(R.id.wx_total_amount);
        zfb_total_amount = (TextView) dialogView.findViewById(R.id.zfb_total_amount);
        xj_total_amount = (TextView) dialogView.findViewById(R.id.xj_total_amount);
        wm_total_amount = (TextView) dialogView.findViewById(R.id.wm_total_amount);
        zh_total_amount = (TextView) dialogView.findViewById(R.id.zh_total_amount);
        balance_total_amount = (TextView) dialogView.findViewById(R.id.balance_total_amount);

        wx_amount = (TextView) dialogView.findViewById(R.id.wx_amount);
        zfb_amount = (TextView) dialogView.findViewById(R.id.zfb_amount);
        xj_amount = (TextView) dialogView.findViewById(R.id.xj_amount);
        wm_amount = (TextView) dialogView.findViewById(R.id.wm_amount);
        balance_amount = (TextView) dialogView.findViewById(R.id.balance_amount);




        SmoothCheckBox journal_check = (SmoothCheckBox) dialogView.findViewById(R.id.journal_check);


        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd");
        String time= formater.format(new Date());

        start_time.setText(time+" 00:00:00");
        end_time.setText(time+" 23:59:59");

        viewModel.day_sales();

        journal_check.setChecked(true);

        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Journal.dismiss();
            }
        });
        //交接班并退出
        handover_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.add_day_sales(daySalesBean);

            }
        });

    }

    /**
     * 绑定日志数据
     * @param bean
     */
    private void initBinding(DaySalesBean bean) {
        total_amount.setText(bean.getTotal_amount()+"元");
        pay_amount.setText(bean.getPay_amount()+"元");
        wx_total_amount.setText(bean.getTotal_amount_list().getWx_total_amount()+"元");
        zfb_total_amount.setText(bean.getTotal_amount_list().getZfb_total_amount()+"元");
        xj_total_amount.setText(bean.getTotal_amount_list().getXj_total_amount()+"元");
        wm_total_amount.setText(bean.getTotal_amount_list().getWm_total_amount()+"元");
        zh_total_amount.setText(bean.getTotal_amount_list().getZh_total_amount()+"元");
        balance_total_amount.setText(bean.getTotal_amount_list().getBalance_total_amount()+"元");

        balance_amount.setText(bean.getAmount_list().getBalance_amount()+"元");
        wx_amount.setText(bean.getAmount_list().getWx_amount()+"元");
        zfb_amount.setText(bean.getAmount_list().getZfb_amount()+"元");
        xj_amount.setText(bean.getAmount_list().getXj_amount()+"元");
        wm_amount.setText(bean.getAmount_list().getWm_amount()+"元");
    }



    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
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
//                birthday.setText(getTime(date));
                if (time_state==1){
                    start_time.setText(getTime(date));
                }else {
                    end_time.setText(getTime(date));
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
                .setType(new boolean[]{true, true, true, false, false, false})//分别对应年月日时分秒，默认全部显示
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
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (ScanUtils.getInstance().isInputFromScanner(HandoverActivity.this, event)) {
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
            viewModel.close_order(order_sn, HandoverActivity.this);

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
