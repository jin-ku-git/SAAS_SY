package com.youwu.shouyinsaas.ui.order_goods.goods;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityEaitSaleGoodsBinding;
import com.youwu.shouyinsaas.db.InventoryDao;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityListRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;

import java.util.ArrayList;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 销售商品列表页面
 * 2022/08/01
 */
public class EditSaleGoodsActivity extends BaseActivity<ActivityEaitSaleGoodsBinding, EditSaleGoodsViewModel> {

    public InventoryDao inventoryDao;

    ArrayList<GroupBean> groupList=new ArrayList<>();
    //分类recyclerveiw的适配器
    private CommunityRecycleAdapter mCommunityRecycleAdapter;

    //商品recyclerveiw的适配器
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_eait_sale_goods;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public EditSaleGoodsViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(EditSaleGoodsViewModel.class);
    }

    @Override
    public void initViewObservable() {

        //分类回调
        viewModel.GoodsList.observe(this, new Observer<ArrayList<GroupBean>>() {
            @Override
            public void onChanged(ArrayList<GroupBean> groupBeans) {
                groupList.clear();
                groupList.addAll(groupBeans);
                initRecyclerView();
                if (groupList.size()!=0){
                    initGoodsList(groupList.get(0).getId()+"");
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();

        inventoryDao = new InventoryDao(this);

        /**
         * 获取商品分类
         */
        viewModel.goods_category();

        initRecyclerView();
    }

    /**
     * 分类
     */
    private void initRecyclerView() {
        //一级分类
        //创建adapter
        mCommunityRecycleAdapter = new CommunityRecycleAdapter(this, groupList);
        //给RecyclerView设置adapter
        binding.recyclerviewCommunity.setAdapter(mCommunityRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.recyclerviewCommunity.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.recyclerviewCommunity.getItemDecorationCount()==0) {
            binding.recyclerviewCommunity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCommunityRecycleAdapter.setOnScanningListener(new CommunityRecycleAdapter.OnScanningListener() {
            @Override
            public void onScanning(GroupBean groupBean) {
                initGoodsList(groupBean.getId()+"");

            }
        });
    }
    //    刷新商品列表
    private void initGoodsList(String goods_id) {
        //        刷新一下实际库存，因为如果客户没买，内存中的库存可能会有改变。但是并没有真正成交
        CabinetEntityList.clear();

        CabinetEntityList.addAll(inventoryDao.getGoodListByCategoryId(goods_id));

        initRecyclerViewTow();


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
        binding.recyclerviewCommodity.setLayoutManager(new StaggeredGridLayoutManager(6, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if ( binding.recyclerviewCommodity.getItemDecorationCount()==0) {
            binding.recyclerviewCommodity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCabinetListRecycleAdapter.setOnItemClickListener(new CommunityListRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CommunityBean data, int position) {

                Bundle mBundle =new Bundle();
                mBundle.putInt("type",1);
                mBundle.putSerializable("CommunityBean",data);
                startActivity(AddGoodsActivity.class,mBundle);

            }
        });


    }
}
