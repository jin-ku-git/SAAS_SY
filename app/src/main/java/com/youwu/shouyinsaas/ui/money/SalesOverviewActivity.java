package com.youwu.shouyinsaas.ui.money;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivitySalesOverviewBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.ui.money.bean.SalesOverviewBean;
import com.youwu.shouyinsaas.util.ColorUtil;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.PieChartView;

import java.util.List;
import java.util.Random;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 销售概况页面
 * 2022/06/01
 */
public class SalesOverviewActivity extends BaseActivity<ActivitySalesOverviewBinding, SalesOverviewViewModel> {

    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sales_overview;
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
    public void initViewObservable() {

        viewModel.entity_List.observe(this, new Observer<SalesOverviewBean>() {
            @Override
            public void onChanged(SalesOverviewBean salesOverviewBean) {
                binding.pieCh1.ClearItemType();
                binding.pieCh2.ClearItemType();
                binding.pieCh3.ClearItemType();
                // 初始化饼图
                initPieChart1(salesOverviewBean.getDelivery_method());
                initPieChart2(salesOverviewBean.getTime_list());
                initPieChart3(salesOverviewBean.getGoods_details_quantity());
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
        viewModel.state.set(1);

        viewModel.sales_situation(viewModel.state.get()+"");




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
    private void initPieChart1(List<SalesOverviewBean.DeliveryMethodBean> delivery_method) {


        for (int i=0;i<delivery_method.size();i++){
            binding.pieCh1.addItemType(new PieChartView.ItemType(delivery_method.get(i).getName(), delivery_method.get(i).getQuantity(), ColorUtil.MAIN_COLORS[i],delivery_method.get(i).getQuantity()+""));
        }
        if (delivery_method.size()==0){
            binding.pieCh1.addItemType(new PieChartView.ItemType("无", 1, ColorUtil.MAIN_COLORS[0],"0"));
        }


        binding.pieCh1.setCell(0);
        binding.pieCh1.setInnerRadius(0f);
        binding.pieCh1.setBackGroundColor(getResources().getColor(R.color.color_hui));
        binding.pieCh1.setItemTextSize(15);
        binding.pieCh1.setTextPadding(10);

    }
    private void initPieChart2(SalesOverviewBean.TimeListBean time_list) {


        if (time_list.getMorn()==0){
            binding.pieCh2.addItemType(new PieChartView.ItemType("晚", 1, getResources().getColor(R.color.color_molv),time_list.getMorn()+""));
        }else {
            binding.pieCh2.addItemType(new PieChartView.ItemType("晚", time_list.getMorn(), getResources().getColor(R.color.color_molv),time_list.getMorn()+""));
        }

        if (time_list.getNoon()==0){
            binding.pieCh2.addItemType(new PieChartView.ItemType("早", 1, getResources().getColor(R.color.color_molan),time_list.getNoon()+""));
        }else {
            binding.pieCh2.addItemType(new PieChartView.ItemType("早", time_list.getNoon(), getResources().getColor(R.color.color_molan),time_list.getNoon()+""));
        }
        if (time_list.getNight()==0){
            binding.pieCh2.addItemType(new PieChartView.ItemType("中", 1, getResources().getColor(R.color.color_molv_d7),time_list.getNight()+""));
        }else {
            binding.pieCh2.addItemType(new PieChartView.ItemType("中", time_list.getNight(), getResources().getColor(R.color.color_molv_d7),time_list.getNight()+""));
        }


        binding.pieCh2.setCell(0);
        binding.pieCh2.setInnerRadius(0f);
        binding.pieCh2.setBackGroundColor(getResources().getColor(R.color.color_hui));
        binding.pieCh2.setItemTextSize(15);
        binding.pieCh2.setTextPadding(10);

    }
    private void initPieChart3(List<SalesOverviewBean.GoodsDetailsQuantityBean> goods_details_quantity) {
        if (goods_details_quantity.size()==0){
            binding.pieCh3.addItemType(new PieChartView.ItemType("无", 1, ColorUtil.MAIN_COLORS[0],"0"));
        }
        for (int i=0;i<goods_details_quantity.size();i++){
            binding.pieCh3.addItemType(new PieChartView.ItemType(goods_details_quantity.get(i).getGoods_name(), goods_details_quantity.get(i).getGoods_quantity(), ColorUtil.MAIN_COLORS[i],goods_details_quantity.get(i).getGoods_quantity()+""));
        }


        binding.pieCh3.setCell(0);
        binding.pieCh3.setInnerRadius(0f);
        binding.pieCh3.setBackGroundColor(getResources().getColor(R.color.color_hui));
        binding.pieCh3.setItemTextSize(15);
        binding.pieCh3.setTextPadding(10);

    }

    /**
     * 生成随机颜色
     * @return
     */
    public static int randomColor(){
        Random random = new Random();
        int red = random.nextInt(200);
        int green = random.nextInt(200);
        int blue = random.nextInt(200);
        return Color.rgb(red,green,blue);
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

        if (ScanUtils.getInstance().isInputFromScanner(SalesOverviewActivity.this, event)) {
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
            viewModel.close_order(order_sn, SalesOverviewActivity.this);

        }
    }
}
