package com.youwu.shouyinsaas.ui.money;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityRechargeRecordBinding;

import com.youwu.shouyinsaas.ui.money.adapter.RechargeRecordAdapter;
import com.youwu.shouyinsaas.ui.money.bean.RechargeBean;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 门店充值记录页面
 * 2022/03/23
 */
public class RechargeRecordActivity extends BaseActivity<ActivityRechargeRecordBinding, RechargeRecordViewModel> {


    //充值记录recyclerveiw的适配器
    private RechargeRecordAdapter mRechargeRecordAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<RechargeBean> RechargeBeans = new ArrayList<>();
    int pageNo=1;

    private TimePickerView pvCustomTime;//时间选择器

    private int time_state;//1 开始 2 结束
    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_recharge_record;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public RechargeRecordViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(RechargeRecordViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1:
                        time_state=1;
                        pvCustomTime.show(); //弹出自定义时间选择器
                        break;
                    case 2:
                        time_state=2;
                        pvCustomTime.show(); //弹出自定义时间选择器
                        break;
                    case 3://获取数据
                        /**
                         * 获取数据
                         */
                        initDataList();
                        break;
                }
            }
        });

        /**
         * 查询门店充值记录结果
         */
        viewModel.rechargeBeanSingleLiveEvent.observe(this, new Observer<ArrayList<RechargeBean>>() {
            @Override
            public void onChanged(ArrayList<RechargeBean> rechargeBeans) {
                RechargeBeans=rechargeBeans;
                initRecyclerView();
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd");
       String time= formater.format(new Date());

       viewModel.start_time.set(time);
       viewModel.end_time.set(time);

        initCustomTimePicker();
        //刷新
        binding.scSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                RechargeBeans.clear();
                pageNo=1;
                /**
                 * 获取数据
                 */
                initDataList();
                refreshLayout.finishRefresh(true);
            }
        });
//        //加载
//        binding.scSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                pageNo++;
//                /**
//                 * 获取数据
//                 */
//                initDataList();
//                refreshLayout.finishLoadMore(true);//加载完成
//            }
//        });
        /**
         * 获取数据
         */
        initDataList();

    }

    private void initDataList() {
        viewModel.recharge_log(TimeUtil.getStart(viewModel.start_time.get()),TimeUtil.getStart(viewModel.end_time.get()));
    }



    /**
     * 门店记录
     */
    private void initRecyclerView() {
        //创建adapter
        mRechargeRecordAdapter = new RechargeRecordAdapter(this, RechargeBeans);
        //给RecyclerView设置adapter
        binding.czjlRecyclerview.setAdapter(mRechargeRecordAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.czjlRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.czjlRecyclerview.getItemDecorationCount()==0) {
            binding.czjlRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
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

                if (time_state==1){
                    viewModel.start_time.set(getTime(date));
                }else {
                    viewModel.end_time.set(getTime(date));
                }
                initDataList();

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



}
