package com.youwu.shouyinsaas.ui.money;

import android.app.Application;

import androidx.annotation.NonNull;

import androidx.databinding.ObservableField;


import com.youwu.shouyinsaas.data.DemoRepository;


import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;


/**
 * 2022/04/04
 */

public class CouponPushViewModel extends BaseViewModel<DemoRepository> {

    //用户名手机号的绑定
    public ObservableField<String> vip_tel = new ObservableField<>("");
    //用户名的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //用户名余额的绑定
    public ObservableField<String> vip_money = new ObservableField<>("");

    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    public CouponPushViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);

    }



    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });
    //确认推送的点击事件
    public BindingCommand PushOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
