package com.youwu.shouyinsaas.ui.order_goods;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.money.bean.SaleBillBean;
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
 * 2022/06/08
 */

public class TakeOrderViewModel extends BaseViewModel<DemoRepository> {


    //手机号获订单的绑定
    public ObservableField<String> tel = new ObservableField<>("");


    //有没有订单
    public SingleLiveEvent<Boolean> Null = new SingleLiveEvent<>();

    //反结帐状态的绑定
    public ObservableField<Boolean> check_state = new ObservableField<>();


    //会员名称的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");


    //取餐号的绑定
    public ObservableField<Boolean> displayBoolean = new ObservableField<>();


    //商品数量+商品总额+打包费的绑定
    public ObservableField<String> GoodsDetails = new ObservableField<>("");


    //订单列表
    public SingleLiveEvent<ArrayList<SaleBillBean>> OrderList = new SingleLiveEvent<>();
    //订单详情
    public SingleLiveEvent<SaleBillBean> saleBillBeanSingleLiveEvent = new SingleLiveEvent<>();

    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public TakeOrderViewModel(@NonNull Application application, DemoRepository repository) {
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



    //去收款的点击事件
    public BindingCommand ToCollectOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
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
