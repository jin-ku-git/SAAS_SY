package com.youwu.shouyinsaas.ui.main;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.qrcode.encoder.QRCode;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.app.UserUtils;
import com.youwu.shouyinsaas.databinding.ActivityDemoBinding;
import com.youwu.shouyinsaas.db.GoodsDao;
import com.youwu.shouyinsaas.db.OrdersDao;
import com.youwu.shouyinsaas.db.bean.OrderNumberBean;
import com.youwu.shouyinsaas.db.bean.RestingInfoBean;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.jianpan.MyCustKeybords;
import com.youwu.shouyinsaas.mqtt.service.IGetMessageCallBack;
import com.youwu.shouyinsaas.mqtt.service.MQTTService;
import com.youwu.shouyinsaas.mqtt.service.MQTTServiceConnction;
import com.youwu.shouyinsaas.queue.LogTask;
import com.youwu.shouyinsaas.queue.TaskPriority;
import com.youwu.shouyinsaas.sunmi.PrintBean;
import com.youwu.shouyinsaas.sunmi.PrinterPresenter;
import com.youwu.shouyinsaas.ui.bean.VipBean;
import com.youwu.shouyinsaas.ui.handover.HandoverActivity;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityListRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.adapter.CouponListRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.adapter.ShoppingRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.adapter.WMOrderAdapter;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.main.bean.CouponBean;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.ui.main.bean.MqttBean;
import com.youwu.shouyinsaas.ui.main.bean.UserBean;
import com.youwu.shouyinsaas.ui.main.bean.XXCOrderBean;
import com.youwu.shouyinsaas.ui.main.bean.XXCOrderCountBean;
import com.youwu.shouyinsaas.ui.money.CashierActivity;
import com.youwu.shouyinsaas.ui.money.SalesDocumentActivity;
import com.youwu.shouyinsaas.ui.money.bean.SaleBillBean;
import com.youwu.shouyinsaas.ui.order_goods.OrderGoodsActivity;
import com.youwu.shouyinsaas.ui.order_goods.TakeOrderActivity;
import com.youwu.shouyinsaas.ui.set_up.MoreActivity;
import com.youwu.shouyinsaas.ui.set_up.bean.SystemMessageEvent;
import com.youwu.shouyinsaas.ui.vip.AddVipActivity;
import com.youwu.shouyinsaas.ui.vip.SouSuoVipActivity;
import com.youwu.shouyinsaas.util.DisplayUtil;
import com.youwu.shouyinsaas.util.FnString;
import com.youwu.shouyinsaas.util.NewDialog;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.BigDecimalUtils;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;
import com.youwu.shouyinsaas.utils_view.TimeUtil;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.PosPrinterDev;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 首页
 * 2022/03/21
 */

public class MainActivity extends BaseActivity<ActivityDemoBinding, MainViewModel> implements IGetMessageCallBack {

    //分类recyclerveiw的适配器
    private CommunityRecycleAdapter mCommunityRecycleAdapter;
    //定义以CommunityEntityList实体类为对象的数据集合
    private ArrayList<GroupBean> CommunityEntityList = new ArrayList<>();

    //商品recyclerveiw的适配
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();

    //购物车recyclerveiw的适配器
    private ShoppingRecycleAdapter mShoppingRecycleAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();


    //优惠券recyclerveiw的适配器
    private CouponListRecycleAdapter mCouponListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CouponBean> CouponEntityList = new ArrayList<>();

    ArrayList<CommunityBean> SouSuoList = new ArrayList<CommunityBean>();

    Intent intent;

    private CouponBean couponBean;//选择的优惠券

    int order_wait_count;//待接单数量
    int order_mak_count;//待出餐数量
    int order_refund_count;//退款数量

    //    数据库
    private OrdersDao ordersDao;
    public GoodsDao goodsDao;
    //    正常情况下的顺序单号
    private OrderNumberBean orderNumberBean;
    //打印服务
    private SunmiPrinterService woyouService = null;//商米标准打印 打印服务
    public static PrinterPresenter printerPresenter;

    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    int widths;//屏幕长
    int height;//屏幕宽

    private int page = 1;
    private int limit = 100;

    private int type_Push=1;

    private int xcx_status;//2 接单
    private String xcx_pay_sn;//小程序的pay_sn

    /**
     * MQTT
     */
    private MQTTServiceConnction serviceConnection;

    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public MainViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_demo;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
        if (woyouService != null) {
            try {
                InnerPrinterManager.getInstance().unBindService(this,
                        innerPrinterCallback);
            } catch (InnerPrinterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
//        controlBottomNavigation(false);
        DisplayUtil.hideNavBar(MainActivity.this);
        //连接打印服务
        connectPrintService();
        //初始化挂单数据库
        ordersDao = new OrdersDao(this);
        //        初始化 goodsBean 数据库
        goodsDao = new GoodsDao(this);
        boolean dataExist = goodsDao.isDataExist();
        if (dataExist) {
            goodsDao.deleteAllData();
        }
        viewModel.shoppingList.set(0);

        // 可以调用该方法，设置不允许滑动退出
        setSwipeBackEnable(false);

        viewModel.vip_bool.set(false);//默认没选择会员

        //获取收银员信息
        viewModel.getMe();
//        viewModel.getxcx_order_count();


        //芯烨打印机初始化
        try {
            //芯烨打印机初始化
            Intent intent = new Intent(this, PosprinterService.class);
            bindService(intent, mSerconnection, BIND_AUTO_CREATE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connectUSB();
                }
            }).start();
        } catch (Exception e) {
            RxToast.normal("未连接芯烨USB打印机");
        }

        //初始化
        initAndroidMQTT();
        /**
         * 优惠券模拟数据
         */
        initCouPonList();
        //EventBus初始化
        EventBus.getDefault().register(this);
        //获取屏幕长和高
        getScreenSize();

//        String pay_sn="100101010199891660808771";
//
//        viewModel.xcx_order_details(pay_sn);


        //设置侧滑栏的宽度
        ViewGroup.LayoutParams params = binding.navView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels * 2 / 3;
        binding.navView.setLayoutParams(params);



    }

    private void initAndroidMQTT() {
        serviceConnection = new MQTTServiceConnction();
        serviceConnection.setIGetMessageCallBack(this);
        //用Intent方式创建并启用Service
        Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void setMessage(String message) {
//        RxToast.normal("收到的推送数据：" + message);
        KLog.a("收到的推送数据：" + message);

//        viewModel.getxcx_order_count();

        initWMData(order_status);

        if (message!=null){
            MqttBean mqttBean = JSON.parseObject(toPrettyFormat(message), MqttBean.class);

            KLog.d("pay_sn:"+mqttBean.getPay_sn());
            KLog.d("type:"+mqttBean.getType());
            KLog.d("shipping_type:"+mqttBean.getShipping_type());

            if (mqttBean.getOrder_status()==1){
                if (mqttBean.getType()==1){//自动接单
                    viewModel.xcx_order_details(mqttBean.getPay_sn());
                }
                switch (mqttBean.getShipping_type()){
                    case 1:
                        new LogTask("DEFAULT","您有一个外卖订单，请及时处理！")
                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                .enqueue(); //入队
                        break;
                    case 2:
                        new LogTask("DEFAULT","您有一个门店自提订单，请及时处理！")
                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                .enqueue(); //入队
                        break;
                    case 3:
                        new LogTask("DEFAULT","您有一个取餐点自提订单，请及时处理！")
                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                .enqueue(); //入队
                        break;
                    case 4:
                        new LogTask("DEFAULT","您有一个堂食订单，请及时处理！")
                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                .enqueue(); //入队
                        break;
                    case 5:
                        new LogTask("DEFAULT","您有一个即食订单，请及时处理！")
                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                .enqueue(); //入队
                        break;
                    case 6:
                        new LogTask("DEFAULT","您有一个预约订单，请及时处理！")
                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                .enqueue(); //入队
                        break;
                }

            }else if(mqttBean.getOrder_status()==2){
                new LogTask("DEFAULT","您有一个申请退款订单，请及时处理！")
                        .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                        .enqueue(); //入队
            }
        }
    }

    /**
     * 推送打印
     */
    private void TestPrint(SaleBillBean saleBillBean) {

        //TODO  打印
        PrintBean printBean = new PrintBean();

        if (saleBillBean.getMember_name()!=null){
            VipBean vipBean1 = new VipBean();
            vipBean1.setName(saleBillBean.getMember_name());
            printBean.setBean(vipBean1);
        }


        printBean.setCouponBean(null);

        OrderNumberBean orderNumberBean1 = new OrderNumberBean();

        orderNumberBean1.setOrderNumber(4);
        printBean.setOrderNumberBean(orderNumberBean1);
        ArrayList<CommunityBean> list=new ArrayList<>();

        for (int i=0;i<saleBillBean.getGoods_list().size();i++){
            CommunityBean communityBean=new CommunityBean();
            communityBean.setGoods_number(saleBillBean.getGoods_list().get(i).getGoods_number());
            communityBean.setGoods_price(saleBillBean.getGoods_list().get(i).getGoods_price()+"");
            communityBean.setGoods_name(saleBillBean.getGoods_list().get(i).getGoods_name());

            list.add(communityBean);
        }
        printBean.setPick_code(saleBillBean.getPick_code());
        printBean.setShopCarYWGoodBeans(list);
        printBean.setOrder_sn(saleBillBean.getOrder_sn());
        printBean.setPaymoney(saleBillBean.getTotal_amount()+"");
        printBean.setCashier(UserUtils.getLogoName());
        printBean.setFreight(saleBillBean.getPacking_fee()+"");
        printBean.setCreatTime(saleBillBean.getCreated_at());
        printBean.setRemarks(saleBillBean.getMark());
        printBean.setMoney(saleBillBean.getTotal_amount()+"");
        printBean.setDeliveryType(saleBillBean.getDelivery_method());
        if ("外卖".equals(saleBillBean.getDelivery_method())){
            printBean.setOrderAddress(saleBillBean.getPickup_address());
        }
        printBean.setTableware_number(saleBillBean.getTableware_number());



        printBean.setPaytype(saleBillBean.getPay_type());
       int printType = AppApplication.spUtils.getInt("printType", 0);
        if (printerPresenter == null) {
            AppApplication.mSpeechSynthesizer.speak("请连接打印机");
            return;
        }

        if (printType == 0) {
            printerPresenter.print(printBean);
        } else if (printType == 1) {
            printerPresenter.print2(printBean, 2);
        } else {
            printerPresenter.print3(printBean);
        }


    }
    private String formatData(long nowTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return time.format(nowTime);
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

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case 1://打开钱箱
                        AppApplication.getInstance().openDraw();
                        RxToast.showTipToast(MainActivity.this, "打开钱箱");
                        break;
                    case 2://添加会员
//                        RxToast.normal("敬请期待");
                        startActivity(AddVipActivity.class);
                        break;
                    case 3://交接班
//                        RxToast.normal("敬请期待");
                        startActivity(HandoverActivity.class);
                        break;
                    case 4://销售单据
                        startActivity(SalesDocumentActivity.class);
                        break;
                    case 5://申请订货
                        startActivity(OrderGoodsActivity.class);
                        break;
                    case 6://更多弹窗

                        startActivity(MoreActivity.class);

//                        FunctionDialog dialog_cabinet =new FunctionDialog(MainActivity.this);
//
//                        new  XPopup.Builder(MainActivity.this)
//                                .maxWidth((int) (widths * 0.7))
//                                .maxHeight((int) (height*0.8))
//                                .autoOpenSoftInput(false)
//                                .asCustom(dialog_cabinet)
//                                .show();
                        break;

                    case 7://跳转到选择会员
                        intent = new Intent(MainActivity.this, SouSuoVipActivity.class);
                        intent.putExtra("type", 1);
                        startActivityForResult(intent, 200);
                        break;

                    case 10://清除购物车
                        ShoppingEntityList.clear();
                        for (int i = 0; i < CabinetEntityList.size(); i++) {
                            CabinetEntityList.get(i).setGoods_number(1);
                            CabinetEntityList.get(i).setCom_number_state(0);

                        }

                        initRecyclerViewTow();
                        //更新UI
                        updateView();
                        break;

                    case 11://优惠券弹窗

                        showCouponDialog();
                        break;
                    case 12://点击vip信息
                        intent = new Intent(MainActivity.this, SouSuoVipActivity.class);
                        intent.putExtra("type", 2);
                        intent.putExtra("VipBean", viewModel.entity.get());
                        startActivityForResult(intent, 200);
                        break;
                    case 13://跳转到收银
                        if (ShoppingEntityList.size() == 0) {
                            RxToast.normal("请选择商品!");
                        } else {

                            Intent intent = new Intent(getBaseContext(), CashierActivity.class);
                            intent.putExtra("ShoppingEntityList", ShoppingEntityList);
                            intent.putExtra("paid_in", viewModel.paid_in.get());
                            intent.putExtra("discount_price", viewModel.discount_prick.get());
                            intent.putExtra("vip_bool", viewModel.vip_bool.get());
                            intent.putExtra("VipBean", viewModel.entity.get());
                            startActivityForResult(intent, 666);
                        }

                        break;
                    //侧边栏
                    case 14:
                        order_status=1;
//                        showJournalDialog();

                        binding.drawerLayout.openDrawer(GravityCompat.END);

                        break;
                    case 15://语言测试
                        new LogTask("DEFAULT","德玛西亚，人在塔在")
                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                .enqueue(); //入队
//                        AppApplication.mSpeechSynthesizer.speak("德玛西亚，人在塔在");
                        break;
                    case 16:
                        if (viewModel.shoppingList.get() == 0) {
                            //跳转到取单页
                            intent = new Intent(MainActivity.this, TakeOrderActivity.class);
                            startActivityForResult(intent, 223);

                        } else {

                            orderNumberBean = AppApplication.gson.fromJson(AppApplication.spUtils.getString("OrderNumberBean", AppApplication.gson.toJson(new OrderNumberBean())), OrderNumberBean.class);

                            if (!DateUtils.isToday(orderNumberBean.getCreatTime())) {
                                orderNumberBean = new OrderNumberBean();
                                orderNumberBean.setOrderNumber(1);
                            } else {//加入时间
                                orderNumberBean.setOrderNumber(orderNumberBean.getOrderNumber() + 1);
                            }
                            // 单号，购物车商品列表  会员
                            RestingInfoBean restingInfoBean = new RestingInfoBean();
                            restingInfoBean.setBean(viewModel.entity.get());
                            orderNumberBean.setCreatTime(System.currentTimeMillis());
                            orderNumberBean.setOrder_price(viewModel.paid_in.get());

                            restingInfoBean.setOrderNumberBean(orderNumberBean);
                            restingInfoBean.setShopCarYWGoodBeans(ShoppingEntityList);


                            String JsonData = new Gson().toJson(restingInfoBean);
                            KLog.d("存入的数据："+JsonData);
                            // 把挂单的数据存到数据库中
                            ordersDao.addOrder(restingInfoBean);

                            AppApplication.spUtils.put("OrderNumberBean", AppApplication.gson.toJson(orderNumberBean));


                            ShoppingEntityList.clear();
                            viewModel.vip_bool.set(false);
                            viewModel.entity.set(null);

                            for (int i = 0; i < CabinetEntityList.size(); i++) {
                                CabinetEntityList.get(i).setGoods_number(1);
                                CabinetEntityList.get(i).setCom_number_state(0);

                            }
                            initRecyclerViewTow();
                            //更新UI
                            updateView();

                        }
                        break;

                    case 17:
                        //连接打印服务
                        connectPrintService();
                        break;
                    case 18://刷新 待处理小程序订单数量
//                        viewModel.getxcx_order_count();
                        if (xcx_status==2){
                            viewModel.xcx_order_details(xcx_pay_sn);
                        }
                        initWMData(order_status);

                        break;
                    case 19:
                        RxToast.showTipToast(MainActivity.this,"核销成功！");
                        break;
                    case 20:
                        initWMData(order_status);
                        RxToast.showTipToast(MainActivity.this,"已同意！");
                        break;
                    case 21:
                        initWMData(order_status);
                        RxToast.showTipToast(MainActivity.this,"已拒绝！");
                        break;
                    case 22://刷新商品
                        //账号同步 数据同步
                        goodsDao.deleteAllData();
                        //获取收银员信息
                        viewModel.getMe();
//                        viewModel.getxcx_order_count();
                        ShoppingEntityList.clear();
                        binding.shoppingNum.setVisibility(View.GONE);
                        break;

                }
            }
        });
        //会员信息
        viewModel.ViewBean.observe(this, new Observer<VipBean>() {
            @Override
            public void onChanged(VipBean vipBean) {
                if (vipBean!=null){

                        viewModel.vip_bool.set(true);
                        viewModel.entity.set(vipBean);

                    if (showShopingDisplay != null) {
                        showShopingDisplay.setvip(vipBean);
                    }
                }
            }
        });
        //获取收银员信息
        viewModel.userEvent.observe(this, new Observer<UserBean>() {
            @Override
            public void onChanged(UserBean userBean) {
                /**
                 * 获取群组
                 */
                initGoodsGroup();
            }
        });
        //商品群组返回数据
        viewModel.groupList.observe(this, new Observer<ArrayList<GroupBean>>() {
            @Override
            public void onChanged(ArrayList<GroupBean> groupBeans) {
                if (CommunityEntityList.size() > 0) {
                    CommunityEntityList.clear();
                }
                CommunityEntityList.addAll(groupBeans);

                initRecyclerView();
                if (CommunityEntityList.size() > 0) {
                    viewModel.getGoodsGroup(AppApplication.spUtils.getString("StoreId"), "0", page + "", limit + "");
                }

            }
        });
        //商品返回数据
        viewModel.goodList.observe(this, new Observer<ArrayList<CommunityBean>>() {
            @Override
            public void onChanged(ArrayList<CommunityBean> goodBeans) {
                if (CabinetEntityList.size() > 0) {
                    CabinetEntityList.clear();
                }

                goodsDao.initTable(goodBeans);

                if (goodBeans.size() == limit) {
                    page++;
                    viewModel.getGoodsGroup(AppApplication.spUtils.getString("StoreId"), "0", page + "", limit + "");
                    return;
                }
                //关闭对话框
                dismissDialog();
                initGoodsList(CommunityEntityList.get(0).getId() + "");


            }
        });

        //小程序订单列表返回
        viewModel.xxc_order_list.observe(this, new Observer<ArrayList<XXCOrderBean>>() {
            @Override
            public void onChanged(ArrayList<XXCOrderBean> xxcOrder) {
                xxcOrderBeans = xxcOrder;

                if (xxcOrderBeans.size() == 0) {
                    if (null_view!=null){
                        null_view.setVisibility(View.VISIBLE);
                    }
                    if (wm_recyclerView!=null){
                        wm_recyclerView.setVisibility(View.GONE);
                    }

                } else {
                    if (null_view!=null){
                        null_view.setVisibility(View.GONE);
                    }
                    if (wm_recyclerView!=null){
                        wm_recyclerView.setVisibility(View.VISIBLE);
                    }

                }
                if (null_view!=null){
                    initWMRecyclerView();
                }

            }
        });
        //小程序订单数量返回
        viewModel.xxc_order_count.observe(this, new Observer<XXCOrderCountBean>() {
            @Override
            public void onChanged(XXCOrderCountBean xxcOrderCountBean) {
                order_wait_count = xxcOrderCountBean.getOrder_wait_count();
                order_mak_count = xxcOrderCountBean.getOrder_mak_count();
                order_refund_count = xxcOrderCountBean.getOrder_refund_count();
                if (receivingText!=null){
                    initassignment();
                }

            }
        });
        //订单详情
        viewModel.saleBillField.observe(this, new Observer<SaleBillBean>() {
            @Override
            public void onChanged(SaleBillBean saleBillBean) {

                TestPrint(saleBillBean);
            }
        });
    }

    //    刷新商品列表
    private void initGoodsList(String goods_id) {

        CabinetEntityList.clear();

        CabinetEntityList.addAll(goodsDao.getGoodListByGroupId(goods_id));

        for (int i = 0; i < CabinetEntityList.size(); i++) {
            for (int j = 0; j < ShoppingEntityList.size(); j++) {
                if (CabinetEntityList.get(i).getGoods_id_sku().equals(ShoppingEntityList.get(j).getGoods_id_sku())) {
                    CabinetEntityList.get(i).setGoods_number(ShoppingEntityList.get(j).getGoods_number());
                    CabinetEntityList.get(i).setCom_number_state(1);
                }
            }
        }

        initRecyclerViewTow();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    /**
     * 获取群组
     */
    private void initGoodsGroup() {
        viewModel.getGoodsGroup(AppApplication.spUtils.getString("StoreId"));
    }


    private TabLayout tabLayout;


    RecyclerView wm_recyclerView;
    WMOrderAdapter wmOrderAdapter;
    TextView OneClickOrder;//一键接单
    private ArrayList<XXCOrderBean> xxcOrderBeans = new ArrayList<>();

    int order_status = 1;//状态 1 待接单 2 待出餐 3 待退款

    TextView receivingText;
    TextView DiningOutText;
    TextView refundText;
    LinearLayout null_view;

    /**
     * 抽屉弹窗
     */
    private void showJournalDialog() {

        final NewDialog dialog = new NewDialog(this, R.style.BottomDialog);


        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;
        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        //将界面填充到AlertDiaLog容器

        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.6);
        layoutParams.height = (int) (height);

        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.RIGHT);
        dialog.setCancelable(true);//点击外部消失弹窗
        DisplayUtil.focusNotAle(dialog.getWindow());
        dialog.show();
        DisplayUtil.clearFocusNotAle(dialog.getWindow());

        tabLayout = dialogView.findViewById(R.id.tabLayout);

        wm_recyclerView = dialogView.findViewById(R.id.wm_recyclerView);
        receivingText = dialogView.findViewById(R.id.receivingText);
        DiningOutText = dialogView.findViewById(R.id.DiningOutText);
        refundText = dialogView.findViewById(R.id.refundText);
        OneClickOrder = dialogView.findViewById(R.id.OneClickOrder);
        null_view = dialogView.findViewById(R.id.null_view);


        initassignment();


        initTabData();


        initWMData(order_status);

        //一键接单
        OneClickOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_sn = "";
                if (xxcOrderBeans.size() != 0) {
                    for (int i = 0; i < xxcOrderBeans.size(); i++) {
                        order_sn += xxcOrderBeans.get(i).getOrder_sn() + ",";
                    }
                    KLog.d("一键接单order_sn:" + order_sn.substring(0, order_sn.length() - 1));
                    viewModel.edit_order_status(order_sn.substring(0, order_sn.length() - 1), "2");
                }


            }
        });


    }

    private void initassignment() {

        if (order_wait_count == 0) {
            receivingText.setVisibility(View.GONE);
        } else {
            receivingText.setVisibility(View.VISIBLE);
            receivingText.setText(order_wait_count + "");
        }
        if (order_mak_count == 0) {
            DiningOutText.setVisibility(View.GONE);
        } else {
            DiningOutText.setVisibility(View.VISIBLE);
            DiningOutText.setText(order_mak_count + "");
        }
        if (order_refund_count == 0) {
            refundText.setVisibility(View.GONE);
        } else {
            refundText.setVisibility(View.VISIBLE);
            refundText.setText(order_refund_count + "");
        }
    }


    /**
     * 小程序订单
     */
    private void initWMRecyclerView() {


        //创建adapter
        wmOrderAdapter = new WMOrderAdapter(this, xxcOrderBeans);
        //给RecyclerView设置adapter
        wm_recyclerView.setAdapter(wmOrderAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        wm_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (wm_recyclerView.getItemDecorationCount() == 0) {
            wm_recyclerView.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        wmOrderAdapter.setOnClickListener(new WMOrderAdapter.OnClickListener() {
            @Override
            public void onClick(final XXCOrderBean data, int position, final int status) {
                xcx_status=status;
                if (status==3){
                    new  XPopup.Builder(MainActivity.this)
                            .maxWidth((int) (widths * 0.5))
                            .maxHeight((int) (height*0.4))
                            .asConfirm("提示", "拒单会退款，是否拒单?", "取消", "确认", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    viewModel.edit_order_status(data.getOrder_sn(), status + "");
                                }
                            }, null,false)
                            .show();
                }else if (status==4){
                    new  XPopup.Builder(MainActivity.this)
                            .maxWidth((int) (widths * 0.7))
                            .maxHeight((int) (height*0.4))
                            .asConfirm("提示", "是否出餐？", "取消", "确认", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    viewModel.edit_order_status(data.getOrder_sn(), status + "");
                                }
                            }, null,false)
                            .show();

                }else {
                    if (status==2){
                        xcx_pay_sn=data.getPay_sn();

                    }
                    viewModel.edit_order_status(data.getOrder_sn(), status + "");
                }


            }
        });

        wmOrderAdapter.setSelectionStatusOnClickListener(new WMOrderAdapter.SelectionStatusOnClickListener() {
            @Override
            public void SelectionStatusonClick(XXCOrderBean lists, int position, int status) {
                switch (status) {
                    case 1://拒绝

                        break;
                    case 2://同意

                        break;
                }
                SelectionDialog(lists.getOrder_sn(),status);
            }
        });

    }
    SmoothCheckBox yes_check;
    SmoothCheckBox no_check;
    /**
     * 退款拒绝还是同意弹窗
     */
    private void SelectionDialog(final String order_sn, final int status) {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_selection, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height * 0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//返回
        TextView selection_name = (TextView) dialogView.findViewById(R.id.selection_name);//
        TextView content = (TextView) dialogView.findViewById(R.id.content);//
        LinearLayout refund_layout = (LinearLayout) dialogView.findViewById(R.id.refund_layout);//
        LinearLayout agree_layout = (LinearLayout) dialogView.findViewById(R.id.agree_layout);//
        final EditText reason = (EditText) dialogView.findViewById(R.id.reason);//
        final TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);//取消

          yes_check = dialog.findViewById(R.id.yes_check);
          no_check = dialog.findViewById(R.id.no_check);
        yes_check.setChecked(true);
        no_check.setChecked(false);

        final TextView confirm = (TextView) dialogView.findViewById(R.id.confirm);//确定
        if (status == 1) {
            selection_name.setText("同意");
            agree_layout.setVisibility(View.VISIBLE);
            refund_layout.setVisibility(View.GONE);

        } else {
            selection_name.setText("拒绝");
            agree_layout.setVisibility(View.GONE);
            refund_layout.setVisibility(View.VISIBLE);
        }
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes_check.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

                if (isChecked){
                    no_check.setChecked(false);
                }
                KLog.d("同意：" + yes_check.isChecked() + "\n拒绝：" + no_check.isChecked());
            }
        });
        no_check.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked){
                    yes_check.setChecked(false);
                }
                KLog.d("同意：" + yes_check.isChecked() + "\n拒绝：" + no_check.isChecked());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String refund_reason;
                refund_reason=reason.getText().toString();
                if (refund_reason==null){
                    refund_reason="";
                }
                viewModel.audit_order_refund(status,order_sn,refund_reason,yes_check.isChecked()?"1":"2",dialog);

                KLog.d("同意：" + yes_check.isChecked() + "\n拒绝：" + no_check.isChecked());

            }
        });

    }


    private void initTabData() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //1 待接单 2 待出餐 3 待退货

                order_status = tab.getPosition() + 1;
//                if (order_status == 1) {
//                    OneClickOrder.setVisibility(View.VISIBLE);
//                } else {
//                    OneClickOrder.setVisibility(View.GONE);
//                }
                viewModel.getxcx_order_count();
                initWMData(order_status);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 获取小程序订单列表
     *
     * @param type
     */
    private void initWMData(int type) {
        type_Push++;
        viewModel.xcx_order_list(type + "",type_Push);
    }


    /**
     * 分类
     */
    private void initRecyclerView() {
        //一级分类
        //创建adapter
        mCommunityRecycleAdapter = new CommunityRecycleAdapter(this, CommunityEntityList);
        //给RecyclerView设置adapter
        binding.recyclerviewCommunity.setAdapter(mCommunityRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.recyclerviewCommunity.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.recyclerviewCommunity.getItemDecorationCount() == 0) {
            binding.recyclerviewCommunity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCommunityRecycleAdapter.setOnScanningListener(new CommunityRecycleAdapter.OnScanningListener() {
            @Override
            public void onScanning(GroupBean groupBean) {
                initGoodsList(groupBean.getId() + "");
//                viewModel.getGoodsGroup(AppApplication.spUtils.getString("StoreId"),groupBean.getId()+"");
            }
        });
    }

    /**
     * 商品列表
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mCabinetListRecycleAdapter = new CommunityListRecycleAdapter(this, CabinetEntityList);
        //给RecyclerView设置adapter
        binding.recyclerviewCommodity.setAdapter(mCabinetListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.recyclerviewCommodity.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.recyclerviewCommodity.getItemDecorationCount() == 0) {
            binding.recyclerviewCommodity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCabinetListRecycleAdapter.setOnItemClickListener(new CommunityListRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CommunityBean data, int position) {
                data.setCom_number_state(1);
                KLog.e("data1:" + data.getGoods_number());
                /**
                 * 添加
                 */
                addShopping(data, position);

                /**
                 * 更新商品UI
                 */
                updateGoodsView(data, position);

            }
        });
        mCabinetListRecycleAdapter.setOnPopupListener(new CommunityListRecycleAdapter.OnPopupListener() {
            @Override
            public void onPopup(CommunityBean data, int position) {
                data.setGoods_number(1);
                /**
                 * 商品详情弹窗
                 */
                showDialog(data, position);
            }
        });

    }

    /**
     * 更新商品UI
     */
    private void updateGoodsView(CommunityBean data, int position) {
//        CabinetEntityList.get(position).setGoods_number(CabinetEntityList.get(position).getGoods_number()+1);

        KLog.e("data3:" + data.getGoods_number());

//        mCabinetListRecycleAdapter.notifyDataSetChanged();

        mCabinetListRecycleAdapter.notifyItemChanged(position, mCabinetListRecycleAdapter.getItemId(R.id.shopping_num));
    }


    /**
     * 购物车列表
     */
    private void initRecyclerViewThree() {

        binding.shoppingCartRecycler.setNestedScrollingEnabled(false);
        //创建adapter
        mShoppingRecycleAdapter = new ShoppingRecycleAdapter(this, ShoppingEntityList);
        //给RecyclerView设置adapter
        binding.shoppingCartRecycler.setAdapter(mShoppingRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.shoppingCartRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.shoppingCartRecycler.getItemDecorationCount() == 0) {
            binding.shoppingCartRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        /**
         * 加减
         */
        mShoppingRecycleAdapter.setOnChangeListener(new ShoppingRecycleAdapter.OnChangeListener() {
            @Override
            public void onChange(CommunityBean data, int position) {
                updateShopping(data, position);

                /**
                 * 更新商品UI
                 */
                updateGoodsView(data, data.getPosition());
            }
        });
        /**
         * 删除
         */
        mShoppingRecycleAdapter.setOnDeleteListener(new ShoppingRecycleAdapter.OnDeleteListener() {
            @Override
            public void onDelete(CommunityBean data, int position) {
                DeleteShopping(data, position);
                /**
                 * 更新商品UI
                 */
                updateGoodsView(data, data.getPosition());

            }
        });
    }


    /**
     * 添加到购物车  +1
     *
     * @param data
     * @param position
     */
    private void addShopping(CommunityBean data, int position) {
        int hasInShopCar = -1;
        for (int i = 0; i < ShoppingEntityList.size(); i++) {
            if (data.getGoods_id_sku().equals(ShoppingEntityList.get(i).getGoods_id_sku())) {
                //如果是修改直接中断循环
                hasInShopCar = i;
                break;
            }
        }
        KLog.e("data211:" + data.getGoods_number());
        if (hasInShopCar == -1) {
            data.setGoods_number(1);
            ShoppingEntityList.add(data);

        } else {
            ShoppingEntityList.get(hasInShopCar).setGoods_number(ShoppingEntityList.get(hasInShopCar).getGoods_number() + 1);

        }

        KLog.e("data2:" + data.getGoods_number());
        //更新UI
        updateView();
    }

    /**
     * 添加到购物车
     *
     * @param data
     * @param position
     */
    private void addUpShopping(CommunityBean data, int position) {
        int hasInShopCar = -1;
        for (int i = 0; i < ShoppingEntityList.size(); i++) {
            if (data.getGoods_id() == ShoppingEntityList.get(i).getGoods_id()) {
                //如果是修改直接中断循环
                hasInShopCar = i;
                break;
            }
        }
        if (hasInShopCar == -1) {
            ShoppingEntityList.add(data);
            CabinetEntityList.get(position).setGoods_number(data.getGoods_number());
        } else {
            ShoppingEntityList.get(hasInShopCar).setGoods_number(data.getGoods_number());
            CabinetEntityList.get(position).setGoods_number(data.getGoods_number());
        }
        //更新UI
        updateView();
    }

    /**
     * 更新UI
     */
    private void updateView() {
        if (ShoppingEntityList.size() == 0) {
            binding.shoppingNum.setVisibility(View.GONE);
            binding.nullView.setVisibility(View.VISIBLE);
            binding.eliminate.setVisibility(View.INVISIBLE);
            viewModel.shoppingList.set(0);

            //初始化副屏
            if (showShopingDisplay != null) {
                showShopingDisplay.cleanShopCar();
                showShopingDisplay.initBannnerView();
            }
        } else {
            binding.nullView.setVisibility(View.GONE);
            binding.shoppingNum.setVisibility(View.VISIBLE);
            binding.eliminate.setVisibility(View.VISIBLE);
            viewModel.shoppingList.set(1);
            int num = 0;
            for (int i = 0; i < ShoppingEntityList.size(); i++) {
                num += ShoppingEntityList.get(i).getGoods_number();
            }

            binding.shoppingNum.setText(num + "");
            //但购物车有商品时显示副屏
            if (showShopingDisplay != null) {
                showShopingDisplay.setList(ShoppingEntityList);
            }



            for (int i = 0; i < CabinetEntityList.size(); i++) {
                CabinetEntityList.get(i).setCom_number_state(0);
                if (ShoppingEntityList.size()!=0){
                    for (int j = 0; j < ShoppingEntityList.size(); j++) {

                        if (CabinetEntityList.get(i).getGoods_id_sku().equals(ShoppingEntityList.get(j).getGoods_id_sku())) {
                            CabinetEntityList.get(i).setGoods_number(ShoppingEntityList.get(j).getGoods_number());
                            CabinetEntityList.get(i).setCom_number_state(1);
                        }
                    }
                }

            }
        }
        initRecyclerViewTow();
        initRecyclerViewThree();
        //计算价格
        countMoney();


    }

    /**
     * 计算价格
     */
    private void countMoney() {
        Double total_price = 0.00;//总价
        Double discount_price = 0.00;//优惠价格

        if (couponBean != null) {//判断优惠券不等于空

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long end = 0L;
            Long start = 0L;
            try {
                end = sf.parse((couponBean.getEnd_time() + " 23:59:59")).getTime();// 日期转换为时间戳
                start = sf.parse((couponBean.getStart_time() + " 00:00:00")).getTime();// 日期转换为时间戳
            } catch (ParseException e) {
                e.printStackTrace();
            }
            KLog.d("结束时间：" + end);
            KLog.d("开始时间：" + start);
            KLog.d("当前时间：" + System.currentTimeMillis());

            if (System.currentTimeMillis() > end) {
                RxToast.normal("优惠券已经过期！");
                couponBean = null;
                for (int i = 0; i < ShoppingEntityList.size(); i++) {

                    total_price += (Double.parseDouble(ShoppingEntityList.get(i).getGoods_price())) * ShoppingEntityList.get(i).getGoods_number();

                }

            } else if (System.currentTimeMillis() < start) {
                RxToast.normal("优惠券活动还未开始！");
                couponBean = null;
                for (int i = 0; i < ShoppingEntityList.size(); i++) {

                    total_price += (Double.parseDouble(ShoppingEntityList.get(i).getGoods_price())) * ShoppingEntityList.get(i).getGoods_number();

                }
            } else {
                for (int i = 0; i < ShoppingEntityList.size(); i++) {

                    //（商品价格-商品优惠价格）*购买的数量
                    total_price += (Double.parseDouble(ShoppingEntityList.get(i).getGoods_price())) * ShoppingEntityList.get(i).getGoods_number();

                }
                //减去优惠券价格
                if (total_price - couponBean.getCou_money() < 0.0) {
                    //加上total_price价格
                    discount_price = discount_price + total_price;
                    total_price = 0.0;
                } else {
                    total_price = total_price - couponBean.getCou_money();
                    //加上优惠券价格
                    discount_price = discount_price + couponBean.getCou_money();
                }
            }
        } else {
            for (int i = 0; i < ShoppingEntityList.size(); i++) {

                total_price += (Double.parseDouble(ShoppingEntityList.get(i).getGoods_price())) * ShoppingEntityList.get(i).getGoods_number();

            }
        }


        viewModel.discount_prick.set(discount_price + "");
        viewModel.paid_in.set("" + BigDecimalUtils.formatRoundUp(total_price, 2));

        if (showShopingDisplay != null) {
            if (ShoppingEntityList.size()==0){

            }else {

            }
            int num = 0;
            for (int i = 0; i < ShoppingEntityList.size(); i++) {
                num += ShoppingEntityList.get(i).getGoods_number();
            }
            showShopingDisplay.countMoney(Double.parseDouble(viewModel.paid_in.get()), 0.0, num, Double.parseDouble(viewModel.discount_prick.get()), 0.0);
        }

    }

    /**
     * 加减操作
     *
     * @param data
     */
    private void updateShopping(CommunityBean data, int position) {
        ShoppingEntityList.set(position, data);
        //更新UI
        updateView();
    }

    /**
     * 删除操作
     *
     * @param data
     */
    private void DeleteShopping(CommunityBean data, int position) {
        data.setCom_number_state(0);
        data.setGoods_number(1);
        /**
         * 更新商品UI
         */
        updateGoodsView(data, position);
        ShoppingEntityList.remove(position);
        //更新UI
        updateView();
    }

    /**
     * 商品详情弹窗
     */
    private void showDialog(final CommunityBean data, final int position) {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_good_details, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height * 0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        final TextView goods_name = (TextView) dialogView.findViewById(R.id.goods_name);
        TextView goods_number = (TextView) dialogView.findViewById(R.id.goods_number);
        final TextView goods_price = (TextView) dialogView.findViewById(R.id.goods_price);
        ImageView iv_edit_subtract = (ImageView) dialogView.findViewById(R.id.iv_edit_subtract);//减
        ImageView iv_edit_add = (ImageView) dialogView.findViewById(R.id.iv_edit_add);//加
        TextView add_shopping = (TextView) dialogView.findViewById(R.id.add_shopping);//加入购物车
        final EditText goods_quantity = (EditText) dialogView.findViewById(R.id.goods_quantity);//商品数量
        final LinearLayout layout_view = (LinearLayout) dialogView.findViewById(R.id.layout_view);
        final MyCustKeybords custom_keyboard = (MyCustKeybords) dialogView.findViewById(R.id.custom_keyboard);//键盘


        goods_name.setText(data.getGoods_name());
        goods_number.setText("商品编号：" + data.getGoods_number());
        goods_price.setText("" + data.getGoods_price());
        goods_quantity.setText(data.getGoods_number() + "");
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //加
        iv_edit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setGoods_number(data.getGoods_number() + 1);
                goods_quantity.setText(data.getGoods_number() + "");
                goods_price.setText("" + BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getGoods_price()) * data.getGoods_number()), 2));
            }
        });
        //减
        iv_edit_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getGoods_number() == 1) {
                    RxToast.normal("无法继续减少");
                } else {
                    data.setGoods_number(data.getGoods_number() - 1);
                    goods_quantity.setText(data.getGoods_number() + "");
                    goods_price.setText("" + BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getGoods_price()) * data.getGoods_number()), 2));
                }

            }
        });
        //关闭键盘
        layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(goods_quantity);
            }
        });

        //添加editText的监听事件
        goods_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString()) || s.toString() == null) {
                    RxToast.normal("最少购买一份");
                    goods_quantity.setText("1");
                    data.setGoods_number(1);
                    goods_price.setText("" + BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getGoods_price()) * data.getGoods_number()), 2));
                } else {
                    data.setGoods_number(Integer.parseInt(s.toString()));
                    goods_price.setText("" + BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getGoods_price()) * data.getGoods_number()), 2));
                }

            }
        });
        //加入购物车
        add_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCom_number_state(1);
                /**
                 * 添加到购物车
                 */
                addUpShopping(data, position);
                /**
                 * 更新商品UI
                 */
                updateGoodsView(data, position);

                dialog.cancel();
            }
        });

//        disableShowSoftInput(goods_quantity);
//        goods_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    custom_keyboard.bindEditText(goods_quantity);
//
//                }
//            }
//        });

//        custom_keyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
//            @Override
//            public void onConfirm() {
//                    RxToast.normal("结账");
//
//            }
//        });

    }


    private RecyclerView coupon_recycler;

    /**
     * 优惠券弹窗
     */
    private void showCouponDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_coupon, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height * 0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//返回
        TextView use_coupon = (TextView) dialogView.findViewById(R.id.use_coupon);//确认使用优惠券
        coupon_recycler = (RecyclerView) dialogView.findViewById(R.id.coupon_recycler);

        initRecyclerViewFive();
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //确认使用优惠券
        use_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countMoney();

                dialog.dismiss();
//                RxToast.normal("使用优惠券");
            }
        });

    }

    /**
     * 优惠券模拟数据
     */
    private void initCouPonList() {
        if (CouponEntityList.size() != 0) {
            CouponEntityList.clear();
        }
        for (int i = 0; i < 10; i++) {
            CouponBean couponBean = new CouponBean();
            couponBean.setName("全品类商品无门槛" + i);
            couponBean.setCou_money(Double.parseDouble("1" + i));
            couponBean.setStart_time("2022-03-24");
            couponBean.setEnd_time("2022-03-25");
            CouponEntityList.add(couponBean);
        }

    }

    /**
     * 优惠券列表
     */
    private void initRecyclerViewFive() {
        //创建adapter
        mCouponListRecycleAdapter = new CouponListRecycleAdapter(this, CouponEntityList);
        //给RecyclerView设置adapter
        coupon_recycler.setAdapter(mCouponListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        coupon_recycler.setScrollbarFadingEnabled(false);
        coupon_recycler.setScrollBarFadeDuration(0);

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        coupon_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (coupon_recycler.getItemDecorationCount() == 0) {
            coupon_recycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCouponListRecycleAdapter.setOnCouPonListener(new CouponListRecycleAdapter.OnCouPonListener() {
            @Override
            public void onCouPon(CouponBean data, int position) {
                couponBean = data;

            }
        });
    }

    ArrayList<Integer> scannedCodes = new ArrayList<Integer>();

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            handleKeyCodes();
        } else {
            scannedCodes.add(event.getKeyCode());
        }

        if (ScanUtils.getInstance().isInputFromScanner(MainActivity.this, event)) {

            //暂时取消扫码
            ScanUtils.getInstance().setOnResultListener(scanListener);
            ScanUtils.getInstance().analysisKeyEvent(event);
            return false;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {

            //具体的操作代码
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示

                View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
                Toast toast = new Toast(this);
                toast.setView(toastRoot);
                TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
                tv.setText("再按一次退出程序");
                toast.setGravity(Gravity.BOTTOM, 0, 150);
                toast.show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
            return true;

        }

        return false;
    }

    private void handleKeyCodes() {
        FnString fnString = new FnString();
        String result = "";
        boolean hasShift = false;
        for (int keyCode : scannedCodes) {
            result += fnString.keyCodeToChar(keyCode, hasShift);
            hasShift = (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT);
        }
        KLog.d(result);
//        RxToast.normal(result);

        scannedCodes.clear();
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
            viewModel.close_order(order_sn);

        }else if (resultStr.length()==20){
            String time=resultStr.substring(0, 10);
            String type=resultStr.substring(10, 12);
            String id=resultStr.substring(12, 20);

            KLog.i("时间戳："+ TimeUtil.getDateToString(Long.parseLong(time)*1000,"yyyy-MM-dd HH:mm:ss"));
            KLog.i("type："+Integer.parseInt(type));
            KLog.i("id："+Integer.parseInt(id));

            long timeGetTime =new Date().getTime();

            long aaa=timeGetTime-Long.parseLong(time)*1000;
            KLog.d("相差："+aaa);
            if((timeGetTime-Long.parseLong(time)*1000)>60000){
                RxToast.normal("当前会员码已过期，请刷新会员码重新扫码");
            }else {
                viewModel.user_info("",Integer.parseInt(id)+"");
            }

        }
    }


    /**
     * 二维码信息对象
     */
    private QRCode qrCodeBean;
    /**
     * 二维码信息原始数据容器
     */
    private StringBuilder mStringBufferResult = new StringBuilder();
    private Handler mHandler = new Handler();

    private Runnable mScanningFishedRunnable = new Runnable() {
        @Override
        public void run() {

            String qrcode = mStringBufferResult.toString();
            if (!TextUtils.isEmpty(qrcode)) {
                // 扫码确定参数
                Gson gson = new Gson();
                try {
                    qrCodeBean = gson.fromJson(qrcode, QRCode.class);


                    String JsonData = new Gson().toJson(qrCodeBean);
                    KLog.d("解析数据：" + JsonData);

                    // 你的代码...
                    // 如果要支持中文，数据用可以URLEncoder/URLDecoder编解码
                } catch (JsonSyntaxException e) {
                    // 解析失败...
                } finally {
                    mStringBufferResult.setLength(0);
                }
            }
        }
    };


    public static IMyBinder myBinder;
    ServiceConnection mSerconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (IMyBinder) service;

            Log.e("myBinder", "connect");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("myBinder", "disconnect");
        }
    };

    public static boolean ISCONNECT = false;

    /**
     * 连接usb
     */
    private void connectUSB() {
        List<String> usbList = PosPrinterDev.GetUsbPathNames(this);
        if (usbList != null) {
            for (int i=0;i<usbList.size();i++){
                String usbAddress = usbList.get(i);
                if (usbAddress.equals(null) || usbAddress.equals("")) {
                    RxToast.normal("连接失败");

                } else {
                    myBinder.ConnectUsbPort(getApplicationContext(), usbAddress, new TaskCallback() {
                        @Override
                        public void OnSucceed() {
                            ISCONNECT = true;

//                        RxToast.normal("连接成功");
                        }

                        @Override
                        public void OnFailed() {
                            ISCONNECT = false;
                            RxToast.normal("连接失败");
                        }
                    });
                }
            }

        } else {
            ISCONNECT = false;
        }
    }


    //连接打印服务
    private void connectPrintService() {
        try {

            InnerPrinterManager.getInstance().bindService(this, innerPrinterCallback);

        } catch (InnerPrinterException e) {
            KLog.d("" + e.getMessage());
            e.printStackTrace();
        }
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            woyouService = service;
            printerPresenter = new PrinterPresenter(MainActivity.this, woyouService);

//            AppApplication.mSpeechSynthesizer.speak("打印机连接成功");
        }

        @Override
        protected void onDisconnected() {
//            AppApplication.mSpeechSynthesizer.speak("连接失败");
            woyouService = null;

        }
    };

    /**
     * 接收传递的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200://从选择会员页面传的数据
                    VipBean vipBean = (VipBean) data.getSerializableExtra("vipBean");
                    if (vipBean.getType_state() == 1) {//选择会员
                        viewModel.vip_bool.set(true);
                        viewModel.entity.set(vipBean);
                        if (showShopingDisplay != null) {
                            showShopingDisplay.setvip(vipBean);
                        }
                    } else {//取消选择
                        viewModel.entity.set(null);
                        viewModel.vip_bool.set(false);
                        if (showShopingDisplay != null) {
                            showShopingDisplay.canclevip();
                        }
                    }



                    break;
                case 223://从上一个界面返回的之前挂单的对象


                    RestingInfoBean restingInfoBean = (RestingInfoBean) data.getSerializableExtra("RestingInfoBean");
                    orderNumberBean = restingInfoBean.getOrderNumberBean();
                    ShoppingEntityList.clear();
                    ShoppingEntityList.addAll(restingInfoBean.getShopCarYWGoodBeans());
                    viewModel.shoppingList.set(1);


                    for (int i = 0; i < ShoppingEntityList.size(); i++) {
//                        if (ShoppingEntityList.get(i).getType()==1){//商品
//                            ShoppingEntityList.get(i).setGoods_id_sku(ShoppingEntityList.get(i).getGoods_id()+""+ShoppingEntityList.get(i).getGoods_sku()+"");
//                        }else {//套餐
//                            ShoppingEntityList.get(i).setGoods_id_sku(ShoppingEntityList.get(i).getPackage_id()+"");
//                        }

                        String goodsId = ShoppingEntityList.get(i).getGoods_id_sku();
                        int goodsNumber = ShoppingEntityList.get(i).getGoods_number();
                        for (int j = 0; j < CabinetEntityList.size(); j++) {
                            if (CabinetEntityList.get(j).getGoods_id_sku().equals(goodsId) ) {
                                CabinetEntityList.get(j).setGoods_number(goodsNumber);
                                CabinetEntityList.get(j).setCom_number_state(1);
                            }
                        }
                    }

                    String JsonDatas = new Gson().toJson(ShoppingEntityList);
                    KLog.d("ShoppingEntityList数据："+JsonDatas);

                    String JsonData = new Gson().toJson(CabinetEntityList);
                    KLog.d("CabinetEntityList数据：" + JsonData);
                    initRecyclerViewTow();

                    updateView();

                    if (restingInfoBean.getBean() != null) {
                        viewModel.vip_bool.set(true);
                        viewModel.entity.set(restingInfoBean.getBean());

                    }
                    break;

            }
        } else if (resultCode == 666) {

            RxToast.showTipToast(MainActivity.this, "支付成功");
            //支付成功返回
            //清空购物车
            ShoppingEntityList.clear();
            for (int i = 0; i < CabinetEntityList.size(); i++) {
                CabinetEntityList.get(i).setGoods_number(1);
                CabinetEntityList.get(i).setCom_number_state(0);
            }
            initRecyclerViewTow();
            viewModel.entity.set(null);
            viewModel.vip_bool.set(false);
            //更新UI
            updateView();


        } else if (resultCode == 223) {
            //收款页面返回的vip信息

            VipBean vipBean = (VipBean) data.getSerializableExtra("data");
            if (vipBean!=null){
                if (vipBean.getType_state() == 1) {
                    viewModel.vip_bool.set(true);
                    viewModel.entity.set(vipBean);
                } else {
                    viewModel.entity.set(null);
                    viewModel.vip_bool.set(false);
                }
                if (showShopingDisplay != null) {
                    showShopingDisplay.setvip(vipBean);
                }
            }

        }
    }


    //todo
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void SystemMessageEvent(final SystemMessageEvent systemMessageEvent) {
        if (null != systemMessageEvent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int sign = systemMessageEvent.message;
                    if (sign == 1) {
                        //账号同步 数据同步
                        goodsDao.deleteAllData();
                        //获取收银员信息
                        viewModel.getMe();
//                        viewModel.getxcx_order_count();
                        ShoppingEntityList.clear();

                    }

                }
            });
        }
    }


    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示

                View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
                Toast toast = new Toast(this);
                toast.setView(toastRoot);
                TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
                tv.setText("再按一次退出程序");
                toast.setGravity(Gravity.BOTTOM, 0, 150);
                toast.show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
