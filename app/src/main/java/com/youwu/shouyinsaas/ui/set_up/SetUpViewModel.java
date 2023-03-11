package com.youwu.shouyinsaas.ui.set_up;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.login.LoginActivity;
import com.youwu.shouyinsaas.ui.set_up.bean.SystemMessageEvent;
import com.youwu.shouyinsaas.utils_view.RxToast;

import org.greenrobot.eventbus.EventBus;

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

public class SetUpViewModel extends BaseViewModel<DemoRepository> {
    //版本号的绑定
    public ObservableField<String> Version_number = new ObservableField<>("");
    //当前账号的绑定
    public ObservableField<String> My_Account = new ObservableField<>("");

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public SetUpViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        My_Account.set("我的当前账户："+model.getUserName());
    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });

    //数据同步的点击事件
    public BindingCommand DataTBOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            RxToast.normal("数据同步");
            int sign = 1;
            EventBus.getDefault().post(new SystemMessageEvent(sign,""));
            finish();

        }
    });
    //账号注销的点击事件
    public BindingCommand AccountClearOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            model.SignOutAccount();
            startActivity(LoginActivity.class);


        }
    });

    public BindingCommand AccountTBOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("账号同步");

        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
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
