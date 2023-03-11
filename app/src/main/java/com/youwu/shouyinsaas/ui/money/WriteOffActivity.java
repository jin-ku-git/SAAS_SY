package com.youwu.shouyinsaas.ui.money;

import android.os.Bundle;
import android.view.Display;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityWriteOffBinding;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.ui.money.adapter.GoodsListAdapter;
import com.youwu.shouyinsaas.ui.money.bean.SaleBillBean;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;

import java.util.ArrayList;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 核销页面
 * 2022/05/27
 */
public class WriteOffActivity extends BaseActivity<ActivityWriteOffBinding, WriteOffViewModel> {
    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    //商品列表recyclerveiw的适配器
    private GoodsListAdapter mGoodsListAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<SaleBillBean.GoodsListBean> mSaleBeans = new ArrayList<>();
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_write_off;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public WriteOffViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(WriteOffViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.goodsListBeanSingleLiveEvent.observe(this, new Observer<ArrayList<SaleBillBean.GoodsListBean>>() {
            @Override
            public void onChanged(ArrayList<SaleBillBean.GoodsListBean> goodsListBeans) {
                mSaleBeans=goodsListBeans;
                initRecyclerView();
            }
        });

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://查询订单详情
                        viewModel.order_details(binding.souSuoEditText.getText().toString());
                        break;
                    case 2://确认核销

                        viewModel.close_order(binding.souSuoEditText.getText().toString());
                        break;
                    case 3://核销成功
                        binding.customView.circleAnimation();
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

        viewModel.type_state.set(1);

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
     * 订单详情记录
     */
    private void initRecyclerView() {
        //创建adapter
        mGoodsListAdapter = new GoodsListAdapter(this, mSaleBeans);
        //给RecyclerView设置adapter
        binding.GoodsRecyclerview.setAdapter(mGoodsListAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.GoodsRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.GoodsRecyclerview.getItemDecorationCount()==0) {
            binding.GoodsRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }


}
