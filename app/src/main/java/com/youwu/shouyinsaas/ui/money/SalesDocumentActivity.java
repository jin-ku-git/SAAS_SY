package com.youwu.shouyinsaas.ui.money;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivitySalesDocumentBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.sunmi.PrintBean;
import com.youwu.shouyinsaas.ui.bean.VipBean;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.money.adapter.RefundImageAdapter;
import com.youwu.shouyinsaas.ui.money.adapter.SaleBillAdapter;
import com.youwu.shouyinsaas.ui.money.adapter.SalesAdapter;
import com.youwu.shouyinsaas.ui.money.bean.SaleBillBean;
import com.youwu.shouyinsaas.ui.order_goods.view.PlaceholderFragment;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;
import com.youwu.shouyinsaas.utils_view.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

import static com.youwu.shouyinsaas.ui.main.MainActivity.printerPresenter;

/**
 * 销售单据页面
 * 2022/03/28
 */
public class SalesDocumentActivity extends BaseActivity<ActivitySalesDocumentBinding, SalesDocumentViewModel> {
    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;


    //订单记录recyclerveiw的适配器
    private SaleBillAdapter mSaleBillAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<SaleBillBean> mSaleBillBeans = new ArrayList<>();

    //单据详情recyclerveiw的适配器
    private SalesAdapter mSalesAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<SaleBillBean.GoodsListBean> mSaleBeans = new ArrayList<>();


    //单据详情recyclerveiw的适配器
    private SalesAdapter mSalesTwoAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<SaleBillBean.GoodsListBean> mSaleTwoBeans = new ArrayList<>();


    //退款凭证recyclerveiw的适配器
    private RefundImageAdapter mRefundImageAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<SaleBillBean.GoodsListBean> List = new ArrayList<>();
    int page=1;

    private TimePickerView pvCustomTime;//时间选择器

    private int time_state;//1 开始 2 结束
    private String order_sn;
    private String delivery_method="0";//配送方式

    private PrintBean printBean=new PrintBean();

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sales_document;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SalesDocumentViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SalesDocumentViewModel.class);
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
                    case 3://反结帐
                        shownDialog();
                        break;
                    case 4:
                        page=1;
                        initOrderList(page);
                        break;

                    case 5://打印小票
                        if (printerPresenter == null) {
                            AppApplication.mSpeechSynthesizer.speak("请连接打印机");
                            return;
                        }

                        //打印代码
                        if (printBean != null) {
                            int printType = AppApplication.spUtils.getInt("printType", 0);

                            if (printType == 0) {
                                printerPresenter.print(printBean);
                            } else if (printType == 1) {
                                printerPresenter.print2(printBean,2);
                            } else if (printType == 2) {
                                printerPresenter.print3(printBean);
                            }
                        } else {
                            RxToast.normal("没有可以打印的订单！");
                        }
                        break;
                    case 6:
                        dialog.dismiss();
                        break;
                }
            }
        });

        /**
         * 订单列表返回结果
         */
        viewModel.OrderList.observe(this, new Observer<ArrayList<SaleBillBean>>() {
            @Override
            public void onChanged(ArrayList<SaleBillBean> saleBillBeans) {
                if (page==1){
                    mSaleBillBeans.clear();
                }

                mSaleBillBeans.addAll(saleBillBeans);

                if (page!=1){
                    initRecyclerView();
                    if (mSaleBillBeans.size()!=0){
                        order_sn=mSaleBillBeans.get(0).getOrder_sn();
                        initOrderDetails(mSaleBillBeans.get(0).getOrder_sn());
                    }
                }else {
                    initRecyclerView();
                    if (saleBillBeans.size()!=0){
                        order_sn=mSaleBillBeans.get(0).getOrder_sn();
                        initOrderDetails(saleBillBeans.get(0).getOrder_sn());
                    }
                }
            }
        });
        /**
         * 订单详情返回结果
         */
        viewModel.saleBillBeanSingleLiveEvent.observe(this, new Observer<SaleBillBean>() {
            @Override
            public void onChanged(SaleBillBean saleBillBean) {
                if (mSaleBeans.size()!=0){
                    mSaleBeans.clear();
                }
                mSaleBeans.addAll(saleBillBean.getGoods_list());
                initBinding(saleBillBean);
                initRecyclerViewTow();


                printBean.setPick_code(saleBillBean.getPick_code());
                printBean.setOrder_sn(saleBillBean.getOrder_sn());
                VipBean vipBean=null;
                if (saleBillBean.getMember_name()!=null){
                     vipBean=new VipBean();

                    vipBean.setName(saleBillBean.getMember_name());
                }


                if (vipBean!=null){//vip信息
                    printBean.setBean(vipBean);
                }
                ArrayList<CommunityBean> list=new ArrayList<>();
                for (int i=0;i<saleBillBean.getGoods_list().size();i++){
                    CommunityBean communityBean=new CommunityBean();
                    communityBean.setGoods_price(saleBillBean.getGoods_list().get(i).getGoods_price()+"");
                    communityBean.setGoods_number(saleBillBean.getGoods_list().get(i).getGoods_number());
                    communityBean.setGoods_name(saleBillBean.getGoods_list().get(i).getGoods_name());
                    list.add(communityBean);
                }
                printBean.setPaytype(saleBillBean.getPay_type());
                printBean.setShopCarYWGoodBeans(list);
                printBean.setCashier(AppApplication.spUtils.getString("Name"));
                printBean.setPaymoney(saleBillBean.getAmount()+"");
                printBean.setCreatTime(saleBillBean.getCreated_at());
                printBean.setMoney(saleBillBean.getTotal_amount() + "");
                printBean.setRemarks(saleBillBean.getMark());
                printBean.setFreight("0");
                printBean.setDeliveryType(saleBillBean.getDelivery_method());
                if ("外卖".equals(saleBillBean.getDelivery_method())){
                    printBean.setOrderAddress(saleBillBean.getPickup_address());
                }
                printBean.setTableware_number(saleBillBean.getTableware_number());

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
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd");
       String time= formater.format(new Date());

       viewModel.OrderAndChargeback.set(true);

       viewModel.start_time.set(time);
       viewModel.end_time.set(time);
        initTab();
        //获取订单列表
       initOrderList(page);

        initCustomTimePicker();
        //刷新
        binding.scSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSaleBillBeans.clear();
                page=1;
                //获取订单列表
                initOrderList(page);
                refreshLayout.finishRefresh(true);
            }
        });
        //加载
        binding.scSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                //获取订单列表
                initOrderList(page);
                refreshLayout.finishLoadMore(true);//加载完成
            }
        });
//        initczjl();

        initTestList();


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
     * 测试数据
     */
    private void initTestList() {
        for (int i=0;i<10;i++){
            SaleBillBean.GoodsListBean  goodsListBean=new SaleBillBean.GoodsListBean();

            List.add(goodsListBean);

            mSaleTwoBeans.add(goodsListBean);
        }

        initRecyclerViewThree();
        initRecyclerViewRefundGoods();

        viewModel.refund.RefundDetails.set("共3件商品，退款金额：￥10.00");
    }

    /**
     * 获取订单列表
     */
    private void initOrderList(int page) {
        viewModel.order_list(TimeUtil.getStart(viewModel.start_time.get()),TimeUtil.getEnd(viewModel.end_time.get()),page,delivery_method,viewModel.tel.get(),AppApplication.spUtils.getString("StoreId"));
    }
    /**
     * 查询订单详情
     * @param order_sn
     */
    private void initOrderDetails(String order_sn) {
        viewModel.order_details(order_sn);
    }


    /**
     * 绑定
     */
    private void initBinding(SaleBillBean saleBillBean) {

        viewModel.OrderSource.set(saleBillBean.getShipping_type());//订单来源
        viewModel.TakeMealNumber.set(saleBillBean.getPick_code());//取餐号
        viewModel.appointment_time.set(saleBillBean.getAppointment_time());//自提时间
        viewModel.create_time.set(saleBillBean.getCreated_at());//下单时间
        viewModel.OrderType.set(saleBillBean.getDelivery_method());//订单类型
        if ("自提".equals(saleBillBean.getDelivery_method())){
            viewModel.PickedUpAndEatIn.set(1);
        }else {
            viewModel.PickedUpAndEatIn.set(2);
        }
        viewModel.OrderState.set(saleBillBean.getOrder_status());//订单状态
        if ("已退款".equals(saleBillBean.getOrder_status())){
            viewModel.refundBoolean.setValue(true);
        }else {
            viewModel.refundBoolean.setValue(false);
        }
        viewModel.discount_prick.set(saleBillBean.getReduced_amount()+"");//优惠金额
        viewModel.copeWithPrick.set(saleBillBean.getTotal_amount()+"");//合计金额
        viewModel.GoodsDetails.set("共"+saleBillBean.getGoods_list().size()+"件商品，商品总额：￥"+saleBillBean.getGoods_amount()+"，打包费：￥"+saleBillBean.getPacking_fee());//应收金额
        viewModel.order_sn.set(saleBillBean.getOrder_sn());//订单编号
        viewModel.paid_in_prick.set(saleBillBean.getAmount()+"");//实收金额
        viewModel.remarks_content.set(saleBillBean.getMark());//备注
        viewModel.vip_name.set(saleBillBean.getMember_name());//会员名称
        viewModel.wipe_zero.set(saleBillBean.getMal()+"");//抹零
        viewModel.pay_mode.set(saleBillBean.getPay_type());//支付方式
    }


    /**
     * 门店记录
     */
    private void initRecyclerView() {
        //创建adapter
        mSaleBillAdapter = new SaleBillAdapter(this, mSaleBillBeans);
        //给RecyclerView设置adapter
        binding.czjlRecyclerview.setAdapter(mSaleBillAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.czjlRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.czjlRecyclerview.getItemDecorationCount()==0) {
            binding.czjlRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mSaleBillAdapter.setOnClickListener(new SaleBillAdapter.OnClickListener() {
            @Override
            public void onClick(SaleBillBean lists, int position) {
                order_sn=lists.getOrder_sn();
                initOrderDetails(lists.getOrder_sn());
//                mSaleBeans=lists.getGoods_list();
//                initBinding(lists);
//                initRecyclerViewTow();
            }
        });

    }

    /**
     * 订单详情记录
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mSalesAdapter = new SalesAdapter(this, mSaleBeans);
        //给RecyclerView设置adapter
        binding.xqRecyclerview.setAdapter(mSalesAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.xqRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.xqRecyclerview.getItemDecorationCount()==0) {
            binding.xqRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }

    /**
     * 退款凭证
     */
    private void initRecyclerViewThree() {
        //创建adapter
        mRefundImageAdapter = new RefundImageAdapter(this, List);
        //给RecyclerView设置adapter
        binding.imageRecyclerView.setAdapter(mRefundImageAdapter);

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.imageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
        //设置item的分割线
        if (binding.imageRecyclerView.getItemDecorationCount()==0) {
            binding.imageRecyclerView.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.HORIZONTAL));
        }
    }
    /**
     * 订单详情记录
     */
    private void initRecyclerViewRefundGoods() {
        //创建adapter
        mSalesTwoAdapter = new SalesAdapter(this, mSaleTwoBeans);
        //给RecyclerView设置adapter
        binding.orderRecyclerView.setAdapter(mSalesTwoAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.orderRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.orderRecyclerView.getItemDecorationCount()==0) {
            binding.orderRecyclerView.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
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
//                birthday.setText(getTime(date));
                if (time_state==1){
                    viewModel.start_time.set(getTime(date));
                }else {
                    viewModel.end_time.set(getTime(date));
                }
                if (mSaleBillBeans.size()!=0){
                    mSaleBillBeans.clear();
                }
                //获取订单列表
                initOrderList(page);

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


     Dialog dialog;
    /**
     * 反结帐弹窗
     */
    private void shownDialog() {

        dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_de_closing, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        TextView fan_price = (TextView) dialogView.findViewById(R.id.fan_price);
        TextView de_closing = (TextView) dialogView.findViewById(R.id.de_closing);
        fan_price.setText(viewModel.paid_in_prick.get());
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //反结帐
        de_closing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.new_audit_refund(AppApplication.spUtils.getString("StoreId"),order_sn);
//                viewModel.refund(order_sn,viewModel.paid_in_prick.get());
//                RxToast.normal("反结帐");
            }
        });


    }

    private List<String> tabs = new ArrayList<>();
    //初始化tab
    private void initTab() {
        tabs.add("全部");
        tabs.add("自提");
        tabs.add("堂食");
        tabs.add("外卖");
        tabs.add("配送到柜");

        //设置TabLayout的模式
        binding.tab.setTabMode(TabLayout.MODE_FIXED);

        binding.viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        //关联ViewPager和TabLayout
        binding.tab.setupWithViewPager(binding.viewPager);


        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //0 全部 1 自提 2 堂食 3 外卖 4 配送到柜
//                order_status= tab.getPosition();
                switch (tab.getPosition()){
                    case 0:
                        delivery_method="0";
                        break;
                    case 1:
                        delivery_method="3";
                        break;
                    case 2:
                        delivery_method="4";
                        break;
                    case 3:
                        delivery_method="5";
                        break;
                    case 4:
                        delivery_method="6";
                        break;
                }
                //获取订单列表
                initOrderList(page);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class TabAdapter extends FragmentPagerAdapter {
        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(tabs.get(position));
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        //显示标签上的文字
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }

    private String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间
        return time;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }
}
