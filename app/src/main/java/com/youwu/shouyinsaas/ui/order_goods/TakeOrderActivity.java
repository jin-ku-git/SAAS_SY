package com.youwu.shouyinsaas.ui.order_goods;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.gson.Gson;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityTakeOrderBinding;
import com.youwu.shouyinsaas.db.OrdersDao;
import com.youwu.shouyinsaas.db.bean.RestingInfoBean;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.order_goods.adapter.TakeOrderAdapter;
import com.youwu.shouyinsaas.ui.order_goods.adapter.TakeOrderDetailsAdapter;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.BigDecimalUtils;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 取单页面
 * 2022/03/28
 */
public class TakeOrderActivity extends BaseActivity<ActivityTakeOrderBinding, TakeOrderViewModel> {

    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    //订单记录recyclerveiw的适配器
    private TakeOrderAdapter mSaleBillAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<RestingInfoBean> mSaleBillBeans = new ArrayList<>();

    //单据详情recyclerveiw的适配器
    private TakeOrderDetailsAdapter mTakeOrderDetailsAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();
    int page=1;

    private OrdersDao ordersDao;
    private List<RestingInfoBean> allDate; //数据库中的所有订单

    private int currentSelect = 0;
    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_take_order;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public TakeOrderViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(TakeOrderViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://去收款
                        Intent intent = new Intent();
                        intent.putExtra("RestingInfoBean", (Serializable) mSaleBillBeans.get(currentSelect));
                        boolean isDelete = ordersDao.deleteOrder(mSaleBillBeans.get(currentSelect).getOrderNumberBean().getCreatTime() + "");
                        if (isDelete) {
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            RxToast.normal("删除数据库失败！");
                        }
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

        ordersDao=new OrdersDao(this);

        for (RestingInfoBean restingInfoBean : ordersDao.getAllDate()) {
            if (!DateUtils.isToday(restingInfoBean.getOrderNumberBean().getCreatTime())) {
                ordersDao.deleteOrder(restingInfoBean.getOrderNumberBean().getCreatTime() + "");
            }
        }

        //获取订单列表
       initOrderList(page);


        binding.souSuoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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
     * 获取订单列表
     */
    private void initOrderList(int page) {

        allDate = ordersDao.getAllDate();
        if(allDate.size()!=0){
            viewModel.displayBoolean.set(true);
            mSaleBillBeans.addAll(allDate);
            initRecyclerView();

            ShoppingEntityList= (ArrayList<CommunityBean>) allDate.get(0).getShopCarYWGoodBeans();

            if (ShoppingEntityList.size()>0){
                viewModel.Null.setValue(true);
            }else {
                viewModel.Null.setValue(false);
            }
            initRecyclerViewTow();
            initBinding(0);
        }else {
            viewModel.displayBoolean.set(false);
        }

    }


    /**
     * 绑定
     * @param position
     */
    private void initBinding(int position) {

        int goodsSize=0;
        double goodsPrick=0;

        for (int i=0;i<ShoppingEntityList.size();i++){
            goodsSize+=ShoppingEntityList.get(i).getGoods_number();
            goodsPrick+=Double.parseDouble(BigDecimalUtils.multiply(ShoppingEntityList.get(i).getGoods_number()+"",ShoppingEntityList.get(i).getGoods_price())+"");
        }

        CommunityBean data = (CommunityBean) allDate.get(position).getShopCarYWGoodBeans().get(0);
        viewModel.GoodsDetails.set("共"+goodsSize+"件商品，商品总额：￥"+ goodsPrick+"");//应收金额

        if (allDate.get(position).getBean()!=null){
            viewModel.vip_name.set(allDate.get(position).getBean().getUser_name());//会员名称
        }else {
            viewModel.vip_name.set("无");//会员名称
        }


    }


    /**
     * 门店记录
     */
    private void initRecyclerView() {
        //创建adapter
        mSaleBillAdapter = new TakeOrderAdapter(this, mSaleBillBeans);
        //给RecyclerView设置adapter
        binding.qdRecyclerview.setAdapter(mSaleBillAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.qdRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.qdRecyclerview.getItemDecorationCount()==0) {
            binding.qdRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mSaleBillAdapter.setOnClickListener(new TakeOrderAdapter.OnClickListener() {
            @Override
            public void onClick(RestingInfoBean lists, int position) {

                currentSelect=position;
                String JsonData = new Gson().toJson(lists);
                KLog.d("数据："+JsonData);
                ShoppingEntityList= (ArrayList<CommunityBean>) lists.getShopCarYWGoodBeans();
                initRecyclerViewTow();
                initBinding(position);
//                initOrderDetails(lists.getOrder_sn());

            }
        });

    }

    /**
     * 订单详情记录
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mTakeOrderDetailsAdapter = new TakeOrderDetailsAdapter(this,ShoppingEntityList );
        //给RecyclerView设置adapter
        binding.xqRecyclerview.setAdapter(mTakeOrderDetailsAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.xqRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.xqRecyclerview.getItemDecorationCount()==0) {
            binding.xqRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (ScanUtils.getInstance().isInputFromScanner(TakeOrderActivity.this, event)) {
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
            viewModel.close_order(order_sn, TakeOrderActivity.this);

        }
    }


}
