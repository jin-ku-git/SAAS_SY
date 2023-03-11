package com.youwu.shouyinsaas.ui.set_up;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivitySettingsPrintBinding;
import com.youwu.shouyinsaas.db.bean.OrderNumberBean;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.sunmi.PrintBean;
import com.youwu.shouyinsaas.ui.bean.VipBean;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.set_up.adapter.PrintListAdapter;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.PosPrinterDev;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

import static com.youwu.shouyinsaas.ui.main.MainActivity.ISCONNECT;
import static com.youwu.shouyinsaas.ui.main.MainActivity.printerPresenter;

/**
 * 设置打印页面
 * 2022/06/08
 */
public class SettingsPrintActivity extends BaseActivity<ActivitySettingsPrintBinding, SettingsPrintViewModel> {

        private int printType;

    PrintListAdapter adapter;
    List<String> list=new ArrayList<>();

    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_settings_print;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SettingsPrintViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SettingsPrintViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://测试打印

                            TestPrint();

                        break;
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

//        binding.spinner.setItems("商米内置打印机标准样式", "芯烨80mmUSB打印机", "商米内置打印机精简样式");
        binding.spinner.setItems( "芯烨80mmUSB打印机");
        printType = AppApplication.spUtils.getInt("printType", 0);
        binding.spinner.setSelectedIndex(0);

////为未下拉的菜单项设置点击事件
//        binding.spinner.setOnItemSelectedListener(new com.jaredrummler.materialspinner.MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(com.jaredrummler.materialspinner.MaterialSpinner view, int position, long id, Object item) {
//                RxToast.normal("Clicked " + item);
//                switch (position) {
//                    case 0:
//                        printType = 0;
//                        AppApplication.spUtils.put("printType", 0);
//                        break;
//                    case 1:
//                        if (ISCONNECT) {
//                            printType = 1;
//                            AppApplication.spUtils.put("printType", 1);
//                        } else {
//                            Log.d("打印机2", position + "");
//                            AppApplication.spUtils.put("printType", 0);
//                            AppApplication.mSpeechSynthesizer.speak("芯烨打印机连接失败，请检查连接线或重启应用");
//                        }
//                        break;
//
//                    case 2:
//                        printType = 2;
//                        AppApplication.spUtils.put("printType", 2);
//                        break;
//
//                }
//            }
//        });
        //为未下拉的菜单项设置点击事件
        binding.spinner.setOnItemSelectedListener(new com.jaredrummler.materialspinner.MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(com.jaredrummler.materialspinner.MaterialSpinner view, int position, long id, Object item) {
                RxToast.normal("Clicked " + item);
                switch (position) {

                    case 0:
                        if (ISCONNECT) {
                            printType = 1;
                            AppApplication.spUtils.put("printType", 1);
                        } else {
                            Log.d("打印机2", position + "");
                            AppApplication.spUtils.put("printType", 0);
                            AppApplication.mSpeechSynthesizer.speak("芯烨打印机连接失败，请检查连接线或重启应用");
                        }
                        break;


                }
            }
        });
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

//        getDetail();


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

    public void getDetail() {

        List<String> usbList = PosPrinterDev.GetUsbPathNames(this);


        //创建adapter
        adapter = new PrintListAdapter(this, usbList);
        //给RecyclerView设置adapter
        binding.recyclerView.setAdapter(adapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.recyclerView.getItemDecorationCount() == 0) {
            binding.recyclerView.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        adapter.setOnItemClickListener(new PrintListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, String data, int position) {

                if (data.equals(null) || data.equals("")) {
                    RxToast.normal("连接失败");

                } else {
                    myBinder.ConnectUsbPort(getApplicationContext(), data, new TaskCallback() {
                        @Override
                        public void OnSucceed() {
                            ISCONNECT = true;
                        RxToast.normal(data+"连接成功");
//                            AppApplication.mSpeechSynthesizer.speak("打印机连接成功");
                        }

                        @Override
                        public void OnFailed() {
                            ISCONNECT = false;
                            RxToast.normal("连接失败");
                        }
                    });
                }
            }
        });
    }

    /**
     * 连接usb
     */
    private void connectUSB() {
        List<String> usbList = PosPrinterDev.GetUsbPathNames(this);
        if (usbList != null) {

                String usbAddress = usbList.get(0);
                if (usbAddress.equals(null) || usbAddress.equals("")) {
                    RxToast.normal("连接失败");

                } else {
                    myBinder.ConnectUsbPort(getApplicationContext(), usbAddress, new TaskCallback() {
                        @Override
                        public void OnSucceed() {
                            ISCONNECT = true;
//                        RxToast.normal("连接成功");
                            AppApplication.mSpeechSynthesizer.speak("打印机连接成功");
                        }

                        @Override
                        public void OnFailed() {
                            ISCONNECT = false;
                            RxToast.normal("连接失败");
                        }
                    });
                }


        } else {
            ISCONNECT = false;
        }
    }


    /**
     * 测试打印
     */
    private void TestPrint() {
        //TODO  打印
        PrintBean printBean = new PrintBean();

        VipBean vipBean1 = new VipBean();
        vipBean1.setName("客户");

        printBean.setBean(vipBean1);
        printBean.setCouponBean(null);

        OrderNumberBean orderNumberBean1 = new OrderNumberBean();
        orderNumberBean1.setCreatTime(157222695);
        orderNumberBean1.setOrderNumber(4);
        printBean.setOrderNumberBean(orderNumberBean1);

        List shopCarYWGoodBeans1 = new ArrayList();

        CommunityBean yw = new CommunityBean();
        yw.setGoods_number(10);
        yw.setGoods_price("10");
        yw.setGoods_cost_price("0");
//                ywGoodBean1.setSpecsBean(specsBean);
        yw.setGoods_name("测试商品");
        shopCarYWGoodBeans1.add(yw);
        printBean.setShopCarYWGoodBeans(shopCarYWGoodBeans1);
        printBean.setOrder_sn("0007820191028134826");
        printBean.setPaymoney("100");
        printBean.setCashier("收银员001");
        printBean.setFreight("0");
        printBean.setCreatTime(formatData(System.currentTimeMillis()));

        printBean.setPaytype("余额支付");
        printBean.setDeliveryType("测试堂食");
        printType = AppApplication.spUtils.getInt("printType", 0);
        if (printerPresenter == null) {
            AppApplication.mSpeechSynthesizer.speak("状态为空");
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (ScanUtils.getInstance().isInputFromScanner(SettingsPrintActivity.this, event)) {
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
            viewModel.close_order(order_sn, SettingsPrintActivity.this);

        }
    }
}
