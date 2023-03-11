package com.youwu.shouyinsaas.ui.money;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.money.bean.RechargeBean;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.util.ArrayList;

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

public class RechargeRecordViewModel extends BaseViewModel<DemoRepository> {
    //开始时间的绑定
    public ObservableField<String> start_time = new ObservableField<>("");
    //开始时间的绑定
    public ObservableField<String> end_time = new ObservableField<>("");


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<ArrayList<RechargeBean>> rechargeBeanSingleLiveEvent = new SingleLiveEvent<>();


    public RechargeRecordViewModel(@NonNull Application application, DemoRepository repository) {
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
    //获取的点击事件
    public BindingCommand obtainOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });

    //开始时间的点击事件
    public BindingCommand StateOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
    //结束时间的点击事件
    public BindingCommand EndOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });





    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *  门店充值记录
     * @param start     开始时间
     * @param end    结束时间
     */
    public void recharge_log(String start, String end) {
        model.RECHARGE_LOG(start,end)
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
                            String JsonData = new Gson().toJson(response.data);

                            ArrayList<RechargeBean> list=  AppApplication.getObjectList(JsonData,RechargeBean.class);

                            rechargeBeanSingleLiveEvent.setValue(list);
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
