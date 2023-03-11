package com.youwu.shouyinsaas.ui.money;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityCouponPushBinding;
import com.youwu.shouyinsaas.ui.main.bean.CouponBean;
import com.youwu.shouyinsaas.ui.money.adapter.CouponPushRecycleAdapter;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.util.ArrayList;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 优惠券推送页面
 * 2022/04/04
 */
public class CouponPushActivity extends BaseActivity<ActivityCouponPushBinding, CouponPushViewModel> {


    //优惠券recyclerveiw的适配器
    private CouponPushRecycleAdapter mCouponListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CouponBean> CouponEntityList = new ArrayList<>();

    ArrayList<CouponBean> mCouponBean=new ArrayList<>();

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_coupon_push;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CouponPushViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(CouponPushViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://确认推送

                        if (mCouponBean.size()!=0){
                            mCouponBean.clear();
                        }
                        for (int i=0;i<CouponEntityList.size();i++){
                            if (CouponEntityList.get(i).isSelect()){
                                mCouponBean.add(CouponEntityList.get(i));
                            }
                        }
                        if (mCouponBean.size()!=0){
                            String submitJson = new Gson().toJson(mCouponBean);
                            RxToast.normal("解析数据：\n"+submitJson);
                        }else {
                            RxToast.normal("请选择优惠券");
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

        viewModel.vip_name.set("张三");
        viewModel.vip_tel.set("15953912345");
        viewModel.vip_money.set("666");

        initList();


    }

    private void initList() {
        for (int i=0;i<10;i++) {
            CouponBean couponBean=new CouponBean();
            couponBean.setName("全品类商品无门槛"+i);
            couponBean.setCou_money(Double.parseDouble("1"+i));
            couponBean.setStart_time("2022-03-24");
            couponBean.setEnd_time("2022-03-25");
            couponBean.setCunpon_number(1);
            CouponEntityList.add(couponBean);
        }
        initRecyclerView();
    }

    /**
     * 优惠券列表
     */
    private void initRecyclerView() {
        //创建adapter
        mCouponListRecycleAdapter = new CouponPushRecycleAdapter(this, CouponEntityList);
        //给RecyclerView设置adapter
        binding.couponRecyclerview.setAdapter(mCouponListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        binding.couponRecyclerview.setScrollbarFadingEnabled(false);
        binding.couponRecyclerview.setScrollBarFadeDuration(0);

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.couponRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.couponRecyclerview.getItemDecorationCount()==0) {
            binding.couponRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCouponListRecycleAdapter.setOnCouPonListener(new CouponPushRecycleAdapter.OnCouPonListener() {
            @Override
            public void onCouPon(CouponBean data, int position) {
                CouponEntityList.set(position,data);
            }
        });
    }

}
