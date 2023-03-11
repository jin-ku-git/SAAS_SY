package com.youwu.shouyinsaas.ui.order_goods;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityOrderGoodsBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;

import com.youwu.shouyinsaas.ui.order_goods.adapter.ApplyGoodsAdapter;
import com.youwu.shouyinsaas.ui.order_goods.adapter.GoodsDetailsAdapter;
import com.youwu.shouyinsaas.ui.order_goods.adapter.OrderGoodsAdapter;
import com.youwu.shouyinsaas.ui.order_goods.adapter.ReceivingAdapter;
import com.youwu.shouyinsaas.ui.order_goods.adapter.ReturnsAdapter;
import com.youwu.shouyinsaas.ui.order_goods.adapter.ReturnsDetailsAdapter;
import com.youwu.shouyinsaas.ui.order_goods.bean.OrderGoodsBean;
import com.youwu.shouyinsaas.ui.order_goods.bean.OrderItemBean;
import com.youwu.shouyinsaas.ui.order_goods.bean.SAASOrderBean;
import com.youwu.shouyinsaas.ui.order_goods.view.PlaceholderFragment;
import com.youwu.shouyinsaas.utils_view.BigDecimalUtils;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;
import com.youwu.shouyinsaas.utils_view.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 申请订货页面
 * 2022/03/28
 */
public class OrderGoodsActivity extends BaseActivity<ActivityOrderGoodsBinding, OrderGoodsViewModel> {

    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    //订单记录recyclerveiw的适配器
    private OrderGoodsAdapter mOrderGoodsAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<OrderGoodsBean> mSaleBillBeans = new ArrayList<>();
    /**
     * 订货商品
     */
    //订货记录详情recyclerveiw的适配器
    private ApplyGoodsAdapter mApplyGoodsAdapter;
    //订单商品数据
    private ArrayList<CommunityBean> mCommunityBeanList = new ArrayList<>();
    int pageNo=1;
    /**
     * 退货数据
     */
    private ReturnsAdapter mReturnsAdapter;
    //退货数据
    private List<OrderItemBean> mReturnsBeanList = new ArrayList<>();
    /**
     * 退货详情数据
     */
    private ReturnsDetailsAdapter mReturnsDetailsAdapter;
    //退货详情数据
    private List<OrderGoodsBean.DetailsBean> mReturnsDetailsBeanList = new ArrayList<>();
    /**
     * 收货数据
     */
    private ReceivingAdapter mReceivingAdapter;
    //收货数据
    private List<OrderGoodsBean.DetailsBean> mReceivingBeanList = new ArrayList<>();

    private List<String> tabs = new ArrayList<>();
    Bundle mBundle;

    List<SAASOrderBean> saasOrderBean;
    //订货详情recyclerveiw的适配器
    private GoodsDetailsAdapter goodsDetailsAdapter;
    List<OrderGoodsBean.DetailsBean> OrderDetails;//订单详情
    OrderGoodsBean orderGoodsBean;//订单详情

    int order_list_type=0;

    private TimePickerView pvCustomTime;//时间选择器

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_order_goods;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public OrderGoodsViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(OrderGoodsViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://复用订单
                        mBundle = new Bundle();
                        mBundle.putSerializable("mCommunityBeanList", mCommunityBeanList);//订货列表
                        startActivity(NewOrderGoodsActivity.class,mBundle);
                        break;
                    case 2:
                        mBundle = new Bundle();
                        mBundle.putSerializable("ShoppingEntityList", mCommunityBeanList);//订货列表
                        mBundle.putString("goods_number", viewModel.goods_number.get());//订货数量
                        mBundle.putString("goods_type", mCommunityBeanList.size()+"");//订货种类
                        startActivity(ConfirmOrderActivity.class,mBundle);
                        break;

                    case 3://退货弹窗
                        shownDialog();
                        break;
                    case 4://确定订货
                        if (viewModel.estimate_time.get()==null||"".equals(viewModel.estimate_time.get())){
                         RxToast.normal("请选择预计收货时间");
                            return;
                        }
                        List<OrderItemBean> saasOrderList=new ArrayList<>();

                        for (int i=0;i<saasOrderBean.size();i++){

                            OrderItemBean orderItemBean=new OrderItemBean();
                            orderItemBean.setGoods_id(saasOrderBean.get(i).getGoods_id()+"");
                            orderItemBean.setGoods_sku(saasOrderBean.get(i).getGoods_sku());
                            if (saasOrderBean.get(i).getOrder_quantity()==null){
                                orderItemBean.setOrder_quantity(0);

                            }else {
                                orderItemBean.setOrder_quantity(Integer.parseInt(saasOrderBean.get(i).getOrder_quantity()));
                            }

                            orderItemBean.setOrder_price(saasOrderBean.get(i).getOrder_price()+"");
                            orderItemBean.setGoods_name(saasOrderBean.get(i).getGoods_name());

                            if (orderItemBean.getOrder_quantity()!=0){
                                saasOrderList.add(orderItemBean);
                            }

                        }
                        String saasList = new Gson().toJson(saasOrderList);

                        viewModel.add_order(store_id,saasList);
//                        add_order();
                        break;
                    case 5://打印
                        RxToast.normal("打印");
                        break;
                    case 6://重置
                        for (int i=0;i<saasOrderBean.size();i++){
                            saasOrderBean.get(i).setOrder_quantity("0");
                        }
                        initRecyclerViewTow();
                        cull();
                        break;
                    case 7://收货
                        shownReceivingDialog();
                        break;
                        case 8://退货详情弹窗
                            shownDetailsDialog();
                        break;
                    case 9:
                        RxToast.showTipToast(OrderGoodsActivity.this, "下单成功");
                        viewModel.OrderState.set(1);

                        initorder_list(0);
                        break;
                    case 10://选择预计收获时间
                        pvCustomTime.show(); //弹出自定义时间选择器
                        break;
                    case 11://退货成功
                        initorder_list(0);
                        dialog.dismiss();
                        break;
                    case 12://收货成功
                        mSaleBillBeans.clear();
                        initorder_list(0);
                        receive_dialog.dismiss();
                        break;
                }
            }
        });
        //订单详情
        viewModel.OrderListBean.observe(this, new Observer<ArrayList<SAASOrderBean>>() {
            @Override
            public void onChanged(ArrayList<SAASOrderBean> saasOrderBeans) {
                saasOrderBean = saasOrderBeans;
                initRecyclerViewTow();
            }
        });
        //订单列表
        viewModel.OrderGoodsBean.observe(this, new Observer<ArrayList<OrderGoodsBean>>() {
            @Override
            public void onChanged(ArrayList<OrderGoodsBean> orderGoodsBeans) {

                mSaleBillBeans =orderGoodsBeans;

                if (order_list_type==1){
                    int pos=mSaleBillBeans.size()-1;

                    orderGoodsBean=mSaleBillBeans.get(pos);

                    initRecyclerView(1);
                    OrderDetails=mSaleBillBeans.get(pos).getDetails();
                    initRecyclerViewDetails();

                    initText(mSaleBillBeans.get(pos));

                }else if (mSaleBillBeans.size()!=0){

                    OrderDetails=mSaleBillBeans.get(0).getDetails();

                    orderGoodsBean=mSaleBillBeans.get(0);
                    initRecyclerView(0);
                    initRecyclerViewDetails();
                    initText(mSaleBillBeans.get(0));

                }

                if (mSaleBillBeans.size()==0&&viewModel.OrderState.get()==0){
                    viewModel.OrderListBoolean.setValue(false);
                }else {

                    viewModel.OrderListBoolean.setValue(true);
                }
            }
        });
        //退货订单列表
        viewModel.OrderItemBean.observe(this, new Observer<ArrayList<OrderItemBean>>() {
            @Override
            public void onChanged(ArrayList<OrderItemBean> orderItemBeans) {
                mReturnsBeanList.clear();
                mReturnsBeanList.addAll(orderItemBeans);

                initReturnsRecyclerView();
            }
        });

        //退货详情
        viewModel.OrderGoodsEvent.observe(this, new Observer<OrderGoodsBean>() {
            @Override
            public void onChanged(OrderGoodsBean orderGoodsBean) {
                mReturnsDetailsBeanList=orderGoodsBean.getDetails();
                initDetailsRecyclerView();
                total_num.setText(orderGoodsBean.getTotal_quantity()+"");
                total_money.setText(orderGoodsBean.getTotal_price()+"");
                return_order_sn_text.setText(orderGoodsBean.getCargo_order_sn()+"");
                return_order_date.setText(orderGoodsBean.getCreated_at());
                status_name.setText(orderGoodsBean.getStatus_name());
            }
        });

    }


    String store_id;//门店id

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();

        int displays= AppApplication.spUtils.getInt("displays");
        if (displays>1){
            initDisplay();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        viewModel.estimate_time.set(format.format(TimeUtil.getBeginDayOfTomorrow()));

        initCustomTimePicker();

        store_id=AppApplication.spUtils.getString("StoreId");

        viewModel.OrderState.set(0);

        viewModel.goods_number.set("0");
        viewModel.paid_in_prick.set("0");

        for (int i=0;i<10;i++){
            tabs.add("测试"+i);
        }
        viewModel.group_state.set("群组");
        initTab();

        binding.switchBtnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchBtnStatus.isChecked()) {
                   viewModel.group_state.set("群组");
//                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.main_color)));
                } else {
                    viewModel.group_state.set("门店群组");
//                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.color_hui)));

                }
            }
        });

        //刷新
        binding.scSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSaleBillBeans.clear();
                pageNo=1;
                initorder_list(0);
                refreshLayout.finishRefresh(true);
            }
        });
        //加载
        binding.scSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                initorder_list(0);
                refreshLayout.finishLoadMore(true);//加载完成
            }
        });
        //获取商品信息
        viewModel.initOrder_info(store_id);


        initorder_list(0);

        EventBus.getDefault().register(this);
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
     * 订货列表记录
     */
    private void initRecyclerView(int type) {
        if (mSaleBillBeans.size()>0){
            viewModel.OrderState.set(1);
        }

        //创建adapter
        mOrderGoodsAdapter = new OrderGoodsAdapter(this, mSaleBillBeans,type);
        //给RecyclerView设置adapter
        binding.orderRecyclerview.setAdapter(mOrderGoodsAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.orderRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.orderRecyclerview.getItemDecorationCount()==0) {
            binding.orderRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mOrderGoodsAdapter.setOnClickListener(new OrderGoodsAdapter.OnClickListener() {
            @Override
            public void onClick(OrderGoodsBean lists, int position) {

                    viewModel.OrderState.set(1);


                viewModel.total_goods_number.set(lists.getTotal_quantity()+"");
                viewModel.total_paid_in_prick.set(lists.getTotal_price()+"");
                viewModel.Order_sn.set(lists.getOrder_sn()+"");
                viewModel.Order_state.set(lists.getStatus_name());

                OrderDetails=lists.getDetails();

                orderGoodsBean=lists;
                initRecyclerViewDetails();
                initText(lists);
            }
        });

    }

    /**
     * 获取订货列表
     */
    private void initorder_list(final int type) {

        order_list_type=type;
        viewModel.order_list(store_id);
    }

    private void initText(OrderGoodsBean orderGoodsBean){
        viewModel.total_goods_number.set(orderGoodsBean.getTotal_quantity()+"");

        viewModel.Order_sn.set(orderGoodsBean.getOrder_sn()+"");
        viewModel.Order_state.set(orderGoodsBean.getStatus_name());
        viewModel.OrderStateType.set(orderGoodsBean.getStatus());
        viewModel.OrderAudit.set(orderGoodsBean.getAudit());
        viewModel.OrderTime.set(orderGoodsBean.getCreated_at());
        if(orderGoodsBean.getStatus()==3){
            if (orderGoodsBean.getAudit()==1){
                viewModel.OrderStateName.set("退货详情");
            }else {
                viewModel.OrderStateName.set("去退货");
            }
            viewModel.total_paid_in_prick.set(orderGoodsBean.getActual_total_price()+"");
            viewModel.OrderDisplay.set(1);
        }else  if(orderGoodsBean.getStatus()==2){//已发货
            viewModel.total_paid_in_prick.set(orderGoodsBean.getTotal_price()+"");
            viewModel.OrderStateName.set("收货");
            viewModel.OrderDisplay.set(1);
        }else {//未发货
            viewModel.OrderDisplay.set(2);
        }
    }



    /**
     * 订单详情记录
     */
    private void initRecyclerViewDetails() {
        //创建adapter
        goodsDetailsAdapter = new GoodsDetailsAdapter(this, OrderDetails);
        //给RecyclerView设置adapter
        binding.xqRecyclerview.setAdapter(goodsDetailsAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.xqRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.xqRecyclerview.getItemDecorationCount()==0) {
            binding.xqRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }

    //获取从ApplyGoodsAdapter传递的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SAASBUSEvbus(String message) {

        List<SAASOrderBean> list=  AppApplication.getObjectList(message,SAASOrderBean.class);
        saasOrderBean=list;

        cull();

        String submitJson = new Gson().toJson(saasOrderBean);
        KLog.d("解析数据："+submitJson);
    }


    /**
     * 计算订货金额
     */
    private void cull() {

        double number=0.0;
        double prick=0.0;
        for (int i=0;i<saasOrderBean.size();i++){

            if (saasOrderBean.get(i).getOrder_quantity()!=null){
                number+=Double.parseDouble(saasOrderBean.get(i).getOrder_quantity());
                prick+=saasOrderBean.get(i).getOrder_price()*Double.parseDouble(saasOrderBean.get(i).getOrder_quantity());
            }
        }


        viewModel.goods_number.set(number+"");
        viewModel.paid_in_prick.set(BigDecimalUtils.format(prick,2)+"");
    }

    /**
     * 订单商品列表记录
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mApplyGoodsAdapter = new ApplyGoodsAdapter(this, saasOrderBean);
        //给RecyclerView设置adapter
        binding.dhRecyclerview.setAdapter(mApplyGoodsAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.dhRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.dhRecyclerview.getItemDecorationCount()==0) {
            binding.dhRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }


    //初始化tab
    private void initTab() {

        //设置TabLayout的模式
        binding.tab.setTabMode(TabLayout.MODE_SCROLLABLE);

        binding.viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        //关联ViewPager和TabLayout
        binding.tab.setupWithViewPager(binding.viewPager);


        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //0 待存货 1 待取货 2 已完成
//                order_status= tab.getPosition();

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


    //获取从ReceivingAdapter传递的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceivingEvbus(List<OrderGoodsBean.DetailsBean> itemListBeans) {

        if (itemListBeans!=null){
            return;
        }

            mReceivingBeanList=itemListBeans;
            String submitJson = new Gson().toJson(mReturnsBeanList);
            KLog.d("收货订单解析数据："+submitJson);

    }


    RecyclerView returns_recyclerview;

    TextView TotalOrderNumber;//退货数量
    TextView TotalReturnsPrice;//退货金额
    Dialog dialog;
    /**
     * 退货弹窗
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
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_returns, null);
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
        LinearLayout layout_view = (LinearLayout) dialogView.findViewById(R.id.layout_view);
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);//取消
        TextView confirm_text = (TextView) dialogView.findViewById(R.id.confirm_text);//确认

        TotalOrderNumber=dialogView.findViewById(R.id.TotalOrderNumber);
        TotalReturnsPrice=dialogView.findViewById(R.id.TotalReturnsPrice);


        returns_recyclerview=dialogView.findViewById(R.id.returns_recyclerview);

        //点击空白关闭软键盘
        layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInput();
            }
        });

        initReturn_order_list(viewModel.Order_sn.get());

        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirm_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String submitJson = new Gson().toJson(mReturnsBeanList);
                initReturns(viewModel.Order_sn.get(),store_id,submitJson,dialog);
            }
        });
    }

    /**
     * 退货商品列表
     * @param order_sn
     */
    private void initReturn_order_list( String order_sn) {

        viewModel.return_order_list(order_sn);

    }

    /**
     *  退货
     * @param orderSn
     * @param store_id
     * @param submitJson
     * @param dialog
     */
    private void initReturns(String orderSn, String store_id, String submitJson, final Dialog dialog) {


        viewModel.Returns(orderSn,store_id,submitJson);

//        showDialog("请稍候");
//        //访问网络
//        String url = Constant.URL_ZONGS + "return_cargo/return_cargo";
//        Log.i("退货", url);
//
//        MyHashMap<String> mParams = new MyHashMap<String>();
//        mParams.put("order_sn", orderSn);
//        mParams.put("store_id", store_id);
//        mParams.put("goods_list", submitJson);
//
//        //访问网络
//        HttpHelper.obtain().post(url,
//                mParams, new HttpCallback<Bean>() {
//                    @Override
//                    public void onSuccess(Bean expressBean) {
//                        dismissDialog();
//                        if (expressBean.code == 200) {
//                            RxToast.normal("退货成功");
//                            initorder_list(0);
//                            dialog.dismiss();
//
//                        }else {
//                            RxToast.normal(expressBean.message);
//                        }
//                    }
//                    @Override
//                    public void onFailed(String string) {
//                        dismissDialog();
//                        RxToast.normal("网络请求失败，请检查网络");
//
//                    }
//                });
    }
    //获取从ReturnsAdapter传递的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReturnsEvbus(List<OrderItemBean> itemListBeans) {


            mReturnsBeanList=itemListBeans;

            String submitJson = new Gson().toJson(mReturnsBeanList);
            KLog.d("退货订单解析数据："+submitJson);
            ReturnsCull();

    }
    /**
     * 计算退货金额
     */
    private void ReturnsCull() {

        double number=0.0;
        double prick=0.0;
        for (int i=0;i<mReturnsBeanList.size();i++){

            if (mReturnsBeanList.get(i).getReturn_order_quantity()!=0){
                number+=Double.parseDouble(mReturnsBeanList.get(i).getReturn_order_quantity()+"");
                prick+=Double.parseDouble(mReturnsBeanList.get(i).getOrder_price())*Double.parseDouble(mReturnsBeanList.get(i).getReturn_order_quantity()+"");
            }
        }

        TotalOrderNumber.setText(number+"");
        TotalReturnsPrice.setText(prick+"");
    }

    /**
     * 退货订单列表
     */
    private void initReturnsRecyclerView() {
        //创建adapter
        mReturnsAdapter = new ReturnsAdapter(this, mReturnsBeanList);
        //给RecyclerView设置adapter
        returns_recyclerview.setAdapter(mReturnsAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        returns_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (returns_recyclerview.getItemDecorationCount()==0) {
            returns_recyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }


    RecyclerView receiving_recyclerview;
    Dialog receive_dialog;
    /**
     * 收货弹窗
     */
    private void shownReceivingDialog() {

        receive_dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_receiving, null);
        //将界面填充到AlertDiaLog容器
        receive_dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        receive_dialog.getWindow().setGravity(Gravity.CENTER);
        receive_dialog.setCancelable(false);//点击外部消失弹窗
        receive_dialog.show();

        //初始化控件
        LinearLayout layout_view = (LinearLayout) dialogView.findViewById(R.id.layout_view);
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);//取消
        TextView confirm_text = (TextView) dialogView.findViewById(R.id.confirm_text);//确认

        //点击空白关闭软键盘
        layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInput();
            }
        });
        receiving_recyclerview=dialogView.findViewById(R.id.receiving_recyclerview);

        mReceivingBeanList=orderGoodsBean.getDetails();
        initReceivingRecyclerView();
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receive_dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receive_dialog.dismiss();
            }
        });

        confirm_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=0;i<mReceivingBeanList.size();i++){
                    orderGoodsBean.getDetails().get(i).setOrder_quantity(mReceivingBeanList.get(i).getOrder_quantity());
                }

                String submitJson = new Gson().toJson(orderGoodsBean);
                initReceiving(submitJson);
            }
        });


    }

    /**
     *  收货
     * @param submitJson
     */
    private void initReceiving(String submitJson) {

        viewModel.receive(submitJson);

//        showDialog("请稍候");
//        //访问网络
//        String url = Constant.URL_ZONGS + "cargo/receive";
//        Log.i("收货", url);
//
//        MyHashMap<String> mParams = new MyHashMap<String>();
//        mParams.put("order_list", submitJson);
//        //访问网络
//        HttpHelper.obtain().post(url,
//                mParams, new HttpCallback<Bean>() {
//                    @Override
//                    public void onSuccess(Bean expressBean) {
//                        dismissDialog();
//                        if (expressBean.code == 200) {
//                            RxToast.normal("收货成功");
//                            dialog.dismiss();
//                            mSaleBillBeans.clear();
//                            initorder_list(0);
//
//                        }else {
//                            RxToast.normal(expressBean.message);
//                        }
//                    }
//                    @Override
//                    public void onFailed(String string) {
//                        dismissDialog();
//                        RxToast.normal("网络请求失败，请检查网络");
//
//                    }
//                });
    }


    /**
     * 收货订单列表
     */
    private void initReceivingRecyclerView() {
        //创建adapter
        mReceivingAdapter = new ReceivingAdapter(this, mReceivingBeanList);
        //给RecyclerView设置adapter
        receiving_recyclerview.setAdapter(mReceivingAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        receiving_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (receiving_recyclerview.getItemDecorationCount()==0) {
            receiving_recyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }



    RecyclerView details_recyclerview;

    TextView total_num;//总退货数量
    TextView total_money;//总退货金额
    TextView return_order_sn_text;//退货编号
    TextView return_order_date;//申请时间
    TextView status_name;//退货状态

    /**
     * 退货详情弹窗
     */
    private void shownDetailsDialog() {

        Dialog  dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_returns_details, null);
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
        LinearLayout layout_view = (LinearLayout) dialogView.findViewById(R.id.layout_view);
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);

        total_num = (TextView) dialogView.findViewById(R.id.total_num);
        total_money = (TextView) dialogView.findViewById(R.id.total_money);
        return_order_sn_text = (TextView) dialogView.findViewById(R.id.return_order_sn);
        return_order_date = (TextView) dialogView.findViewById(R.id.return_order_date);
        status_name = (TextView) dialogView.findViewById(R.id.status_name);
        //点击空白关闭软键盘
        layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInput();
            }
        });
        details_recyclerview=dialogView.findViewById(R.id.details_recyclerview);
        initReceivingDetails();


        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }

    /**
     *  退货详情
     */
    private void initReceivingDetails() {

        viewModel.return_cargo_list();

    }


    /**
     * 退货详情商品列表
     */
    private void initDetailsRecyclerView() {
        //创建adapter
        mReturnsDetailsAdapter = new ReturnsDetailsAdapter(this, mReturnsDetailsBeanList);
        //给RecyclerView设置adapter
        details_recyclerview.setAdapter(mReturnsDetailsAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        details_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (details_recyclerview.getItemDecorationCount()==0) {
            details_recyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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

        endDate.set(Calendar.getInstance().get(Calendar.YEAR)+3, 12, 30);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                birthday.setText(getTime(date));
                KLog.d("选择时间："+getTime(date));
                KLog.d("现在时间："+getTime(Calendar.getInstance().getTime()));
                if (getTimeCompareSize(getTime(date),getTime(Calendar.getInstance().getTime()))==1){
                    RxToast.normal("结束时间小于当前时间！,请重新选择时间");

                }else {
                    viewModel.estimate_time.set(getTime(date));
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }

}
