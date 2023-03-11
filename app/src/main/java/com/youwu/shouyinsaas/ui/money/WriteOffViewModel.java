package com.youwu.shouyinsaas.ui.money;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
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

import static com.youwu.shouyinsaas.app.AppApplication.toPrettyFormat;

/**
 * 2022/05/27
 */

public class WriteOffViewModel extends BaseViewModel<DemoRepository> {


    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    //券码的绑定
    public ObservableField<String> CouponCode = new ObservableField<>("");
    //会员昵称的绑定
    public ObservableField<String> order_sn = new ObservableField<>("");
    //下单时间的绑定
    public ObservableField<String> orderTime = new ObservableField<>("");

    //商品数量+商品总额+打包费的绑定
    public ObservableField<String> GoodsDetails = new ObservableField<>("");

    //是否查询到
    public ObservableField<Integer> type_state = new ObservableField<>();

    //
    public ObservableField<SaleBillBean> entity = new ObservableField<>();

    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //
    public SingleLiveEvent<ArrayList<SaleBillBean.GoodsListBean>> goodsListBeanSingleLiveEvent = new SingleLiveEvent<>();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public WriteOffViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);

    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });

    //确认核销的点击事件
    public BindingCommand ConfirmWriteOffOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (entity.get().getPickup_status()!=2){
                IntegerEvent.setValue(2);
            }
        }
    });
    //搜索的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
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



    /**
     * 订单详情
     * @param order_sn
     */
    public void order_details(String order_sn) {
        model.ORDER_DETAILS(order_sn)
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

                            SaleBillBean saleBillBean = JSON.parseObject(toPrettyFormat(JsonData), SaleBillBean.class);

                            if (saleBillBean!=null){
                                type_state.set(2);
                                entity.set(saleBillBean);
                                goodsListBeanSingleLiveEvent.setValue(saleBillBean.getGoods_list());
                                GoodsDetails.set("共"+saleBillBean.getGoods_list().size()+"件商品，商品总额：￥"+saleBillBean.getGoods_amount()+"，打包费：￥"+saleBillBean.getPacking_fee());//应收金额
                            }


                        }else {
                            type_state.set(3);
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
     */
    public void close_order(String order_sn) {

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
                            type_state.set(4);
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
