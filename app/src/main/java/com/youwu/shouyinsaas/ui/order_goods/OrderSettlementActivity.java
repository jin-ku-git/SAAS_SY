package com.youwu.shouyinsaas.ui.order_goods;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppViewModelFactory;

import com.youwu.shouyinsaas.databinding.ActivityOrderSettlementLayoutBinding;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.order_goods.adapter.OrderSettlementRecycleAdapter;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;

import java.util.ArrayList;
import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 订货结算页面
 * 2022/04/03
 */

public class OrderSettlementActivity extends BaseActivity<ActivityOrderSettlementLayoutBinding, OrderSettlementViewModel> {


    //购物车recyclerveiw的适配器
    private OrderSettlementRecycleAdapter mOrderSettlementRecycleAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();

    private String paid_in_prick;//总金额
    private String goods_number;//订货数量
    private String goods_type;//订货种类
    Intent intent;



    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //获取列表传入的实体
        ShoppingEntityList= (ArrayList<CommunityBean>) getIntent().getSerializableExtra("GoodsEntityList");
        paid_in_prick= getIntent().getStringExtra("paid_in_prick");
        goods_number= getIntent().getStringExtra("goods_number");
        goods_type= getIntent().getStringExtra("goods_type");
    }

    @Override
    public OrderSettlementViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(OrderSettlementViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_order_settlement_layout;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){

                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();

        viewModel.goods_type_number.set(goods_type);
        viewModel.total_goods_number.set(goods_number);
        viewModel.goods_number.set("共"+goods_number+"件");
        viewModel.paid_in_prick.set(paid_in_prick);


        initRecyclerView();
    }



    /**
     * 购物车列表
     */
    private void initRecyclerView() {

        binding.shoppingCartRecycler.setNestedScrollingEnabled(false);
        //创建adapter
        mOrderSettlementRecycleAdapter = new OrderSettlementRecycleAdapter(this, ShoppingEntityList);
        //给RecyclerView设置adapter
        binding.shoppingCartRecycler.setAdapter(mOrderSettlementRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.shoppingCartRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.shoppingCartRecycler.getItemDecorationCount()==0) {
            binding.shoppingCartRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

    }




}
