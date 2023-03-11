package com.youwu.shouyinsaas.ui.money;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.money.bean.VipRechargeBean;
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
 * 2022/03/28
 */

public class VipRechargeViewModel extends BaseViewModel<DemoRepository> {



    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //自定义充值页面的显示和隐藏
    public ObservableField<Boolean> custom_bool = new ObservableField<>();
    //Top标题
    public ObservableField<String> TOP_TITLE = new ObservableField<>("会员充值");
    //自定义金额
    public ObservableField<String> Custom_price = new ObservableField<>("");

    //是否充值成功
    public ObservableField<Integer> type_state = new ObservableField<>();
    //充值金额列表
    public SingleLiveEvent<ArrayList<VipRechargeBean>> arrayListObservableField = new SingleLiveEvent<>();

    public VipRechargeViewModel(@NonNull Application application, DemoRepository repository) {
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
    //自定义充值的点击事件
    public BindingCommand customOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
            custom_bool.set(true);
            TOP_TITLE.set("自定义充值");
        }
    });

    //现金充值的点击事件
    public BindingCommand cashRechargeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //返回充值列表的点击事件
    public BindingCommand RechargeListOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
            custom_bool.set(false);
            TOP_TITLE.set("会员充值");
        }
    });

    //返回的点击事件
    public BindingCommand ReturnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);

        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取充值金额列表
     */
    public void recharge_list(String store_id) {
        model.RECHARGE_LIST(store_id)
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
                            ArrayList<VipRechargeBean> vipRechargeBean = AppApplication.getObjectList(JsonData, VipRechargeBean.class);

                            arrayListObservableField.setValue(vipRechargeBean);
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
     * 会员充值
     * @param activity_id       活动id
     * @param user_id           用户id
     * @param type              1现金2微信3支付宝
     * @param is_customize      是否自定义充值 0是 1不是
     * @param dynamic_id        用户支付码
     */
    public void recharge(String activity_id,String activity_price_id, String user_id, String type, String is_customize,String money,String dynamic_id) {
        model.RECHARGE(activity_id,activity_price_id,user_id+"",type,is_customize,money,dynamic_id)
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
                            IntegerEvent.setValue(4);
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
