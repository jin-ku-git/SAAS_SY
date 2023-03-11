package com.youwu.shouyinsaas.ui.order_goods;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.order_goods.bean.OrderGoodsBean;
import com.youwu.shouyinsaas.ui.order_goods.bean.OrderItemBean;
import com.youwu.shouyinsaas.ui.order_goods.bean.SAASOrderBean;
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
 * 2022/03/30
 */

public class OrderGoodsViewModel extends BaseViewModel<DemoRepository> {


    //反结帐状态的绑定
    public ObservableField<Boolean> check_state = new ObservableField<>();

    //群组还是订单群组的绑定
    public ObservableField<String> group_state = new ObservableField<>("");

    //订单编号的绑定
    public ObservableField<String> Order_sn = new ObservableField<>("");
    //订单状态的绑定
    public ObservableField<String> Order_state = new ObservableField<>("");
    //有没有订单
    public SingleLiveEvent<Boolean> OrderListBoolean = new SingleLiveEvent<>();

    //订单状态的绑定 1订货详情 2申请订货
    public ObservableField<Integer> OrderState = new ObservableField<>();

    //订单状态的绑定
    public ObservableField<Integer> OrderStateType = new ObservableField<>();
    //订单状态的绑定 1已退货 2 未退货
    public ObservableField<Integer> OrderAudit = new ObservableField<>();
    //退货状态 1 已退货  2  未退货
    public ObservableField<Integer> is_return_order = new ObservableField<>();

    //
    public ObservableField<String> OrderStateName = new ObservableField<>("");

    //1 显示 2不显示
    public ObservableField<Integer> OrderDisplay = new ObservableField<>();

    //订货日期的绑定
    public ObservableField<String> OrderTime = new ObservableField<>("");

    //合计订货数量的绑定
    public ObservableField<String> total_goods_number = new ObservableField<>("");
    //合计订货金额的绑定
    public ObservableField<String> total_paid_in_prick = new ObservableField<>("");

    //预计到货时间的绑定
    public ObservableField<String> estimate_time = new ObservableField<>("");
    //商品数量的绑定
    public ObservableField<String> goods_number = new ObservableField<>("");
    //实收金额的绑定
    public ObservableField<String> paid_in_prick = new ObservableField<>("");


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<ArrayList<SAASOrderBean>> OrderListBean = new SingleLiveEvent<>();
    //订单列表
    public SingleLiveEvent<ArrayList<OrderGoodsBean>> OrderGoodsBean = new SingleLiveEvent<>();
    //退货订单列表
    public SingleLiveEvent<ArrayList<OrderItemBean>> OrderItemBean = new SingleLiveEvent<>();
    //退货详情
    public SingleLiveEvent<OrderGoodsBean> OrderGoodsEvent = new SingleLiveEvent<>();


    public OrderGoodsViewModel(@NonNull Application application, DemoRepository repository) {
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

    //复用订单的点击事件
    public BindingCommand FYOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);

        }
    });
    //再次提交的点击事件
    public BindingCommand ResubmitOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);

        }
    });
    //修改订单的点击事件
    public BindingCommand UpdateOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
//            RxToast.normal("修改订单");
        }
    });
    //取消订单的点击事件
    public BindingCommand CancelOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("取消订单");
        }
    });

    //新建订单的点击事件
    public BindingCommand NewOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            OrderListBoolean.setValue(true);
            OrderState.set(2);
//            startActivity(NewOrderGoodsActivity.class);
        }
    });
    //去退货和退货详情的点击事件
    public BindingCommand ToReturnsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (OrderStateType.get()==3){//已签收
                if (OrderAudit.get()==1){//已退货
                    IntegerEvent.setValue(8);
                }else {//未退货
                    IntegerEvent.setValue(3);
                }
            }else {
                IntegerEvent.setValue(7);
//                if (is_return_order.get()==2){//未退货
//                    IntegerEvent.setValue(3);
//
//                }else {//已退货
//                    IntegerEvent.setValue(8);
//
//                }

            }
        }
    });

    //确认的点击事件
    public BindingCommand ConfirmOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);

        }
    });

    //确认的点击事件
    public BindingCommand ResetOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(6);
        }
    });





    //打印的点击事件
    public BindingCommand DeClosingOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);
        }
    });


    //选择时间的点击事件
    public BindingCommand choiceTimeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(10);
        }
    });





    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 申请订货
     * @param storeId
     * @param saasList
     */
    public void add_order(String storeId, String saasList) {
        model.ADD_ORDER(storeId,estimate_time.get(),saasList)
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
                            IntegerEvent.setValue(9);

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
     * 获取订货商品列表
     * @param store_id
     */
    public void initOrder_info(String store_id) {
        model.ORDER_INFO(store_id)
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
                            ArrayList<SAASOrderBean> saasOrderBean = AppApplication.getObjectList(JsonData, SAASOrderBean.class);
                            OrderListBean.setValue(saasOrderBean);
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
     * 获取订单列表
     * @param store_id
     */
    public void order_list(String store_id) {
        model.ORDER_LIST(store_id)
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
                            ArrayList<OrderGoodsBean> orderGoodsBean = AppApplication.getObjectList(JsonData, OrderGoodsBean.class);
                            OrderGoodsBean.setValue(orderGoodsBean);
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
     * 退货商品列表
     * @param order_sn
     */
    public void return_order_list(String order_sn) {
        model.RETURN_ORDER_LIST(order_sn)
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
                            ArrayList<OrderItemBean>  orderItemBeans=AppApplication.getObjectList(JsonData,OrderItemBean.class);
                            OrderItemBean.setValue(orderItemBeans);

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
     * 退货详情
     */
    public void return_cargo_list() {
        model.RETURN_CARGO_LIST(Order_sn.get())
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

                            OrderGoodsBean returnsDetailsBean = JSON.parseObject(toPrettyFormat(JsonData), OrderGoodsBean.class);
                            OrderGoodsEvent.setValue(returnsDetailsBean);
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
     * 退货
     * @param orderSn
     * @param store_id
     * @param submitJson
     */
    public void Returns(String orderSn, String store_id, String submitJson) {
        model.RETURN_CARGO(orderSn,store_id,submitJson)
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
                            RxToast.normal("退货成功");
                            IntegerEvent.setValue(11);
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
     * 收货
     * @param submitJson
     */
    public void receive(String submitJson) {
        model.RECEIVE(submitJson)
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
                            RxToast.normal("收货成功");
                            IntegerEvent.setValue(12);
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
