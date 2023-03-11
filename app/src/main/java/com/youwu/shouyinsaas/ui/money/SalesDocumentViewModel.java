package com.youwu.shouyinsaas.ui.money;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.main.bean.RowsOrdersBean;
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
 * 2022/03/28
 */

public class SalesDocumentViewModel extends BaseViewModel<DemoRepository> {


    //封装一个界面发生改变的观察者
    public UIChangeObservable refund = new UIChangeObservable();


    //开始时间的绑定
    public ObservableField<String> start_time = new ObservableField<>("");
    //结束时间的绑定
    public ObservableField<String> end_time = new ObservableField<>("");

    //手机号获订单的绑定
    public ObservableField<String> tel = new ObservableField<>("");

    //订单还是退单
    public ObservableField<Boolean> OrderAndChargeback = new ObservableField<>();
    //有没有订单
    public SingleLiveEvent<Boolean> OrderListBoolean = new SingleLiveEvent<>();
    //有没有退款
    public SingleLiveEvent<Boolean> refundBoolean = new SingleLiveEvent<>();

    //反结帐状态的绑定
    public ObservableField<Boolean> check_state = new ObservableField<>();

    //订单编号的绑定
    public ObservableField<String> order_sn = new ObservableField<>("");
    //会员名称的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //订单来源的绑定
    public ObservableField<String> OrderSource = new ObservableField<>("");
    //自提时间的绑定
    public ObservableField<String> appointment_time = new ObservableField<>("");
    //下单时间的绑定
    public ObservableField<String> create_time = new ObservableField<>("");
    //配送方式的绑定
    public ObservableField<String> OrderType = new ObservableField<>("");
    //配送方式的绑定 1、自提 2、堂食
    public ObservableField<Integer> PickedUpAndEatIn = new ObservableField<>();
    //订单状态的绑定
    public ObservableField<String> OrderState = new ObservableField<>("");
    //取餐号的绑定
    public ObservableField<String> TakeMealNumber = new ObservableField<>("");
    //备注的绑定
    public ObservableField<String> remarks_content = new ObservableField<>("");

    //商品数量+商品总额+打包费的绑定
    public ObservableField<String> GoodsDetails = new ObservableField<>("");

    //合计的绑定
    public ObservableField<String> copeWithPrick = new ObservableField<>("");
    //实收金额的绑定
    public ObservableField<String> paid_in_prick = new ObservableField<>("");
    //优惠折扣金额的绑定
    public ObservableField<String> discount_prick = new ObservableField<>("");
    //抹零的绑定
    public ObservableField<String> wipe_zero = new ObservableField<>("");
    //支付方式的绑定
    public ObservableField<String> pay_mode = new ObservableField<>("");

    public class UIChangeObservable {
        //退款类型
        public ObservableField<String> RefundType = new ObservableField<>();
        //退款原因
        public ObservableField<String> RefundReason = new ObservableField<>();
        //申请时间
        public ObservableField<String> ApplyTime = new ObservableField<>();
        //申请描述
        public ObservableField<String> ApplyDescribe = new ObservableField<>();
        //商品数量+商品总额+打包费的绑定
        public ObservableField<String> RefundDetails = new ObservableField<>("");
    }


    //订单列表
    public SingleLiveEvent<ArrayList<SaleBillBean>> OrderList = new SingleLiveEvent<>();
    //订单详情
    public SingleLiveEvent<SaleBillBean> saleBillBeanSingleLiveEvent = new SingleLiveEvent<>();

    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public SalesDocumentViewModel(@NonNull Application application, DemoRepository repository) {
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

    //打印小票的点击事件
    public BindingCommand PrintOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);

        }
    });
    //复用订单的点击事件
    public BindingCommand CopyOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("复用订单");
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

    //反结帐的点击事件
    public BindingCommand DeClosingOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });
    //反结帐的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);
        }
    });
    //订单的点击事件
    public BindingCommand OrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            OrderAndChargeback.set(true);

        }
    });
    //退单的点击事件
    public BindingCommand ChargebackOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            OrderAndChargeback.set(false);
        }
    });


    //退单同意的点击事件
    public BindingCommand RefundAgreeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("同意");
        }
    });
    //退单拒绝的点击事件
    public BindingCommand RefundRefuseOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("拒绝");
        }
    });






    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 订单列表
     * @param start
     * @param end
     */
    public void order_list(String start, String end, final int page, String delivery_method, String tel, String store_id) {
        model.ORDER_List(start,end,page,delivery_method,tel,store_id)
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

                            RowsOrdersBean rowsOrdersBean = JSON.parseObject(toPrettyFormat(JsonData), RowsOrdersBean.class);
                            ArrayList<SaleBillBean> list=  rowsOrdersBean.getRows();

                            OrderList.setValue(list);
                            if (page==1&&list.size()==0){
                                OrderListBoolean.setValue(false);
                            }else {
                                OrderListBoolean.setValue(true);
                            }

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
                                saleBillBeanSingleLiveEvent.setValue(saleBillBean);



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
     * 反结帐
     * @param order_sn  订单id
     * @param refund_price   退款金额
     */
    public void refund(String order_sn, String refund_price) {
        model.REFUND(order_sn,refund_price)
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
                            RxToast.normal("退款成功");
                            refundBoolean.setValue(true);
                            IntegerEvent.setValue(6);
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
     * 反结帐（审核退款订单）2023/03/08加
     * @param order_sn  订单id
     */
    public void new_audit_refund(String store_id,String order_sn) {
        model.NEW_REFUNF_ORDER(store_id,order_sn)
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
                            RxToast.normal("退款成功");
                            refundBoolean.setValue(true);
                            IntegerEvent.setValue(6);
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
