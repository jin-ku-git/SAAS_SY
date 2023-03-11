package com.youwu.shouyinsaas.ui.handover;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.handover.bean.DaySalesBean;
import com.youwu.shouyinsaas.ui.handover.bean.HandoverBean;
import com.youwu.shouyinsaas.ui.login.LoginActivity;
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

public class HandoverViewModel extends BaseViewModel<DemoRepository> {

    //收银员的绑定
    public ObservableField<String> logo_name = new ObservableField<>("");

    //登录时间的绑定
    public ObservableField<String> logo_time = new ObservableField<>("");


    public ObservableField<HandoverBean> entity = new ObservableField<>();


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<HandoverBean> handoverBeanSingleLiveEvent = new SingleLiveEvent<>();

    //使用LiveData
    public SingleLiveEvent<DaySalesBean> daySalesBeanSingleLiveEvent = new SingleLiveEvent<>();


    public HandoverViewModel(@NonNull Application application, DemoRepository repository) {
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
    //销售商品列表的点击事件
    public BindingCommand TowOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //盘点现金的点击事件
    public BindingCommand OneOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
    //盘点现金的点击事件
    public BindingCommand ThreeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取交接班详情
     * @param start
     * @param end
     */
    public void shift_change(String start, String end) {
        model.SHIFT_CHANGE(start,end)
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

                            HandoverBean handoverBean = JSON.parseObject(toPrettyFormat(JsonData), HandoverBean.class);
                            handoverBeanSingleLiveEvent.setValue(handoverBean);
                            entity.set(handoverBean);
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
     *添加交接班
     * @param total_amount_list
     * @param number_list
     * @param amount_list
     * @param other_money_list
     * @param cash_amount
     * @param cash_total_amount
     */
    public void add_shift_change(String total_amount_list, String number_list, String amount_list, String other_money_list, String cash_amount, String cash_total_amount) {

        model.ADD_SHIFT_CHANGE(total_amount_list,number_list,amount_list,other_money_list,cash_amount,cash_total_amount)
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
                            KLog.d("返回数据"+JsonData);
                            model.SignOutAccount();
                            startActivity(LoginActivity.class);
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
     * 获取日结信息
     */
    public void day_sales() {
        model.DAY_SALES()
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

                            DaySalesBean daySalesBean=JSON.parseObject(JsonData,DaySalesBean.class);

                            daySalesBeanSingleLiveEvent.setValue(daySalesBean);
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
     * 添加日志
     * @param daySalesBean
     */
    public void add_day_sales(DaySalesBean daySalesBean) {

        String total_amount_list = new Gson().toJson(daySalesBean.getTotal_amount_list());
        String amount_list = new Gson().toJson(daySalesBean.getAmount_list());

        model.ADD_DAY_SALES(daySalesBean.getTotal_amount(),daySalesBean.getPay_amount(),total_amount_list,amount_list)
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
