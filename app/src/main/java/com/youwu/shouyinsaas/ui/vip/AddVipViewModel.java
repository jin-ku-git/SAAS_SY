package com.youwu.shouyinsaas.ui.vip;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.utils_view.RxToast;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 2022/03/23
 */

public class AddVipViewModel extends BaseViewModel<DemoRepository> {
    //用户名手机号的绑定
    public ObservableField<String> vip_tel = new ObservableField<>("");
    //用户名的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //开卡日期的绑定
    public ObservableField<String> vip_start_time = new ObservableField<>("");
    //备注的绑定
    public ObservableField<String> vip_remarks = new ObservableField<>("");

    //是否查询到
    public ObservableField<Integer> type_state = new ObservableField<>();


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public AddVipViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层

    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });
    //取消的点击事件
    public BindingCommand cancelOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });
    //创建会员信息的点击事件
    public BindingCommand AddVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });

    //返回首页的点击事件
    public BindingCommand MainOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();

        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 创建会员信息
     */
    public void create_user() {
        model.CREATE_USER(vip_tel.get(),vip_name.get(),vip_remarks.get())
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {
                        if (response.isOk()){
                            type_state.set(2);
                            IntegerEvent.setValue(2);
//                            RxToast.normal("创建成功");

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }
    /**
     * 核销
     * @param order_sn
     * @param activity
     */
    public void close_order(String order_sn, final Activity activity) {

        model.CLOSE_ORDER(order_sn)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {

                        if (response.isOk()){
                            RxToast.showTipToast(activity,"核销成功！");

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }
}
