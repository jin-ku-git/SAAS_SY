package com.youwu.shouyinsaas.ui.order_goods;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityConfirmOrderBinding;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;

import com.youwu.shouyinsaas.ui.order_goods.adapter.ConfirmOrderAdapter;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.util.ArrayList;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 确认订货页面
 * 2022/04/02
 */
public class ConfirmOrderActivity extends BaseActivity<ActivityConfirmOrderBinding, ConfirmOrderViewModel> {



    //单据详情recyclerveiw的适配器
    private ConfirmOrderAdapter mConfirmOrderAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> GoodsEntityList = new ArrayList<CommunityBean>();

    private String paid_in;//总金额
    private String goods_number;//订货数量
    private String goods_type;//订货种类
    int pageNo=1;

    @Override
    public void initParam() {
        super.initParam();
        //获取列表传入的实体
        GoodsEntityList= (ArrayList<CommunityBean>) getIntent().getSerializableExtra("ShoppingEntityList");
        paid_in= getIntent().getStringExtra("paid_in");
        goods_number= getIntent().getStringExtra("goods_number");
        goods_type= getIntent().getStringExtra("goods_type");
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_confirm_order;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ConfirmOrderViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(ConfirmOrderViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://确认收货
                        if (GoodsEntityList.size()==0){
                            RxToast.normal("请选择商品!");
                        }else {

                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("GoodsEntityList", GoodsEntityList);//订货列表
                            mBundle.putString("goods_number", viewModel.order_number.get());//订货数量
                            mBundle.putString("goods_type", viewModel.goods_type.get());//订货种类
                            mBundle.putString("paid_in_prick", viewModel.paid_in_prick.get());//总金额
                            startActivity(OrderSettlementActivity.class,mBundle);
                        }
                        break;
                    case 2:

                        break;

                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();


        viewModel.goods_type.set(goods_type);
        viewModel.order_number.set(goods_number);

        initRecyclerViewTow();


        countMoney();
    }

    /**
     * 计算价格
     */
    private void countMoney() {
        Double total_price=0.00;//总价
        int order_number=0;//订货数
        for (int i=0;i<GoodsEntityList.size();i++){
                //商品价格*购买的数量
            total_price+=Double.parseDouble(GoodsEntityList.get(i).getGoods_price())*GoodsEntityList.get(i).getGoods_number();
            order_number+=GoodsEntityList.get(i).getGoods_number();

        }
        viewModel.paid_in_prick.set(total_price+"");
        viewModel.order_number.set(order_number+"");
        viewModel.goods_type.set(GoodsEntityList.size()+"");

    }



    /**
     * 订单详情记录
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mConfirmOrderAdapter = new ConfirmOrderAdapter(this, GoodsEntityList);
        //给RecyclerView设置adapter
        binding.xqRecyclerview.setAdapter(mConfirmOrderAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.xqRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.xqRecyclerview.getItemDecorationCount()==0) {
            binding.xqRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        //删除的回调
        mConfirmOrderAdapter.setOnDeleteListener(new ConfirmOrderAdapter.OnDeleteListener() {
            @Override
            public void onDelete(CommunityBean data, int position) {

                countMoney();
            }
        });
    }


}
