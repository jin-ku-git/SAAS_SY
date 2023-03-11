package com.youwu.shouyinsaas.ui.order_goods;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.main.MainActivity;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2022/04/03
 */

public class OrderSettlementViewModel extends BaseViewModel {


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //应付价格的绑定
    public ObservableField<String> paid_in_prick = new ObservableField<>("");
    //商品数量的绑定
    public ObservableField<String> goods_number = new ObservableField<>("");
    //商品种类的绑定
    public ObservableField<String> goods_type_number = new ObservableField<>("");
    //订货数量的绑定
    public ObservableField<String> total_goods_number = new ObservableField<>("");

    public OrderSettlementViewModel(@NonNull Application application, DemoRepository repository) {
        super(application,repository);
    }

    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //回到首页的点击事件
    public BindingCommand MainOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(MainActivity.class);
        }
    });



}
