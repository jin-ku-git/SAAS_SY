package com.youwu.shouyinsaas.ui.money;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
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
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivitySalesOverviewBinding;
import com.youwu.shouyinsaas.databinding.ActivitySalesOverviewTwoBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.rili.CalendarUtil;
import com.youwu.shouyinsaas.rili.DateInfo;
import com.youwu.shouyinsaas.rili.DatePopupWindow;
import com.youwu.shouyinsaas.ui.money.bean.SalesOverviewBean;
import com.youwu.shouyinsaas.util.ColorUtil;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.CalendarView;
import com.youwu.shouyinsaas.utils_view.Constant;
import com.youwu.shouyinsaas.utils_view.DateUtils;
import com.youwu.shouyinsaas.utils_view.PieChartView;
import com.youwu.shouyinsaas.utils_view.RxToast;
import com.youwu.shouyinsaas.utils_view.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 销售概况页面
 * 2022/06/01
 */
public class SalesOverviewTwoActivity extends BaseActivity<ActivitySalesOverviewTwoBinding, SalesOverviewViewModel> {

    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    private String StartTime;//开始时间时间戳
    private String EndTime;//结束时间时间戳

    private int time_state;//1、早餐开始 2、早餐结束 3、午餐开始 4、午餐结束 5、晚餐开始 6、晚餐结束

    private TimePickerView pvCustomTime;//时间选择器



    private String starTime;    //开始时间文字
    private String endTime;     //结束时间文字
    private boolean isSelecgOk = false;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sales_overview_two;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SalesOverviewViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SalesOverviewViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        int displays= AppApplication.spUtils.getInt("displays");
        if (displays>1){
            initDisplay();
        }

        //设置默认值
        viewModel.morning_start_time.set("5:30");
        viewModel.morning_end_time.set("11:00");
        viewModel.afternoon_start_time.set("11:00");
        viewModel.afternoon_end_time.set("15:00");
        viewModel.evening_start_time.set("15:00");
        viewModel.evening_end_time.set("22:00");

        StartTime= (TimeUtil.getDayBegin().getTime()/1000)+"";
        EndTime= (TimeUtil.getDayEnd().getTime()/1000)+"";
        KLog.d("今日:StartTime:"+StartTime+";EndTime:"+EndTime);

        viewModel.state.set(1);

//        viewModel.sales_situation(viewModel.state.get()+"");



        NewSalesInfo();

        initView();
        initCustomTimePicker();

    }



    private void initView() {


    }

    /**
     * 获取数据
     */
    private void NewSalesInfo() {
        viewModel.new_sales_info(StartTime,EndTime);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://早餐开始时间
                        time_state=1;
                        pvCustomTime.show();
                        break;
                    case 2://早餐结束时间
                        time_state=2;
                        pvCustomTime.show();
                        break;
                    case 3://午餐开始时间
                        time_state=3;
                        pvCustomTime.show();
                        break;
                    case 4://午餐结束时间
                        time_state=4;
                        pvCustomTime.show();
                        break;
                    case 5://晚餐开始时间
                        time_state=5;
                        pvCustomTime.show();
                        break;
                    case 6://晚餐结束时间
                        time_state=6;
                        pvCustomTime.show();
                        break;
                    case 7://今日
                        StartTime= (TimeUtil.getDayBegin().getTime()/1000)+"";
                        EndTime= (TimeUtil.getDayEnd().getTime()/1000)+"";
                        KLog.d("今日:StartTime:"+StartTime+";EndTime:"+EndTime);
                        NewSalesInfo();
                        break;
                    case 8://本周
                        StartTime= (TimeUtil.getBeginDayOfWeek().getTime()/1000)+"";
                        EndTime= (TimeUtil.getEndDayOfWeek().getTime()/1000)+"";
                        KLog.d("本周:StartTime:"+StartTime+";EndTime:"+EndTime);
                        NewSalesInfo();
                        break;
                    case 9://本月
                        StartTime= (TimeUtil.getBeginDayOfMonth().getTime()/1000)+"";
                        EndTime= (TimeUtil.getEndDayOfMonth().getTime()/1000)+"";
                        KLog.d("本月:StartTime:"+StartTime+";EndTime:"+EndTime);
                        NewSalesInfo();
                        break;
                    case 10:
                        showTimeDialog();
                        break;
                    case 11://搜索
                        NewSalesInfo();
                        break;
                }
            }
        });
    }

    private TextView tv_startime,tv_endtime;


    /**
     * 时间弹窗
     */
    private void showTimeDialog() {



       Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_start_end_time, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);//点击外部消失弹窗
        dialog.show();

        //初始化控件
        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);//取消
        TextView tv_define = (TextView) dialogView.findViewById(R.id.tv_define);//确定
        CalendarView calendarview=dialogView.findViewById(R.id.calendarview);
         tv_startime=dialogView.findViewById(R.id.tv_startime);
         tv_endtime=dialogView.findViewById(R.id.tv_endtime);


        calendarview.setETimeSelListener(new CalendarView.CalendatEtimSelListener() {
            @Override
            public void onETimeSelect(Date date) {
                KLog.d("endTime:"+date.getTime());
                if (date != null) {
                    endTime = DateUtils.formatData(date, Constant.TFORMATE_YMD);
                    viewModel.EndTime.set(endTime);
                    tv_endtime.setText(endTime);
                    EndTime=TimeUtil.getTimeStemp(endTime,"yyyy-MM-dd")/1000+"";
                }else {
                    endTime = null;
                }
            }
        });
        calendarview.setSTimeSelListener(new CalendarView.CalendarSTimeSelListener() {
            @Override
            public void onSTimeSelect(Date date) {
                KLog.d("starTime:"+date.getTime());
                if (date != null) {
                    starTime = DateUtils.formatData(date, Constant.TFORMATE_YMD);
                    viewModel.StartTime.set(starTime);
                    tv_startime.setText(starTime);

                    StartTime=TimeUtil.getTimeStemp(starTime,"yyyy-MM-dd")/1000+"";
                }else {
                    starTime = null;
                }
            }
        });
        calendarview.setCalendaSelListener(new CalendarView.CalendaSelListener() {
            @Override
            public void selectStatus(boolean isOk) {

                isSelecgOk = isOk;
                KLog.d("isSelecgOk:"+isSelecgOk);
            }
        });

        //返回
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //交接班并退出
        tv_define.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelecgOk){
                    RxToast.normal("请选择结束时间");
                    return;
                }
                dialog.dismiss();
                viewModel.state.set(4);

                viewModel.fatalism.set((TimeUtil.getDays(endTime,starTime)+1)+"");

                NewSalesInfo();
            }
        });

    }




    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
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

                switch (time_state){
                    case 1:
                        viewModel.morning_start_time.set(getTime(date));
                        break;
                    case 2:
                        viewModel.morning_end_time.set(getTime(date));
                        break;
                    case 3:
                        viewModel.afternoon_start_time.set(getTime(date));
                        break;
                    case 4:
                        viewModel.afternoon_end_time.set(getTime(date));
                        break;
                    case 5:
                        viewModel.evening_start_time.set(getTime(date));
                        break;
                    case 6:
                        viewModel.evening_end_time.set(getTime(date));
                        break;
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
    protected void onDestroy() {
        super.onDestroy();
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }

}