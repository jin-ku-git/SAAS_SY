package com.youwu.shouyinsaas.ui.vip;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.bean.VipBean;
import com.youwu.shouyinsaas.ui.money.CouponPushActivity;
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
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static com.youwu.shouyinsaas.app.AppApplication.toPrettyFormat;

/**
 * 2022/03/23
 */

public class SouSuoVipViewModel extends BaseViewModel<DemoRepository> {

    public ObservableField<VipBean> entity = new ObservableField<>();
    public Drawable drawableImg;
    //用户名手机号的绑定
    public ObservableField<String> vip_tel = new ObservableField<>("");
    //用户名手机号的绑定
    public ObservableField<String> Edit_tel = new ObservableField<>("");


    //用户名信息展示的绑定
    public ObservableField<Boolean> vip_details_state = new ObservableField<>();

    //用户名余额的绑定
    public ObservableField<Integer> type_state = new ObservableField<>();



    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //使用LiveData
    public SingleLiveEvent<VipBean> vipBeanSingleLiveEvent = new SingleLiveEvent<>();

    public SouSuoVipViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层R.mipmap.user_image
        drawableImg = ContextCompat.getDrawable(getApplication(), R.mipmap.loading);
    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);

        }
    });

    //搜索VIP的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {


            user_info(Edit_tel.get());


        }
    });



    //推送优惠券的点击事件
    public BindingCommand PushDisOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startActivity(CouponPushActivity.class);

        }
    });
    //充值的点击事件
    public BindingCommand RechargeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);

        }
    });
    //选择会员的点击事件
    public BindingCommand ChoiceOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            vipBeanSingleLiveEvent.setValue(entity.get());
//            RxToast.normal("选择会员");

        }
    });

    /**
     * 搜索会员
     * @param tel  手机号
     */
    public void user_info(String tel) {
        model.USER_INFO(tel,"")
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
                            VipBean vipBean = JSON.parseObject(toPrettyFormat(JsonData), VipBean.class);
                            entity.set(vipBean);
                            vip_tel.set(vipBean.getUser_name());

                            KLog.d("返回数据"+JsonData);
                            IntegerEvent.setValue(3);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}




