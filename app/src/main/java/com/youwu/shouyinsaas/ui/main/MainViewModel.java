package com.youwu.shouyinsaas.ui.main;

import android.app.Application;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.bean.VipBean;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.ui.main.bean.RowsBean;
import com.youwu.shouyinsaas.ui.main.bean.UserBean;
import com.youwu.shouyinsaas.ui.main.bean.XXCOrderBean;
import com.youwu.shouyinsaas.ui.main.bean.XXCOrderCountBean;
import com.youwu.shouyinsaas.ui.money.SalesOverviewTwoActivity;
import com.youwu.shouyinsaas.ui.money.bean.SaleBillBean;
import com.youwu.shouyinsaas.ui.set_up.SetUpActivity;
import com.youwu.shouyinsaas.ui.set_up.SettingsPrintActivity;
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
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static com.youwu.shouyinsaas.app.AppApplication.toPrettyFormat;

/**
 * 2022/03/21
 */

public class MainViewModel extends BaseViewModel<DemoRepository> {

    public ObservableField<VipBean> entity = new ObservableField<>();

    public ObservableField<Integer> shoppingList = new ObservableField<>();
    public Drawable drawableImg;
    //门店名称的绑定
    public ObservableField<String> store_name = new ObservableField<>("");

    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public SingleLiveEvent<VipBean> ViewBean = new SingleLiveEvent<>();


    //vip信息的显示和隐藏
    public ObservableField<Boolean> vip_bool = new ObservableField<>();

    //用户的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //用户余额的绑定
    public ObservableField<String> vip_money = new ObservableField<>("");
    //应付价格的绑定
    public ObservableField<String> paid_in = new ObservableField<>("");
    //优惠金额的绑定
    public ObservableField<String> discount_prick = new ObservableField<>("");

    //小程序待处理数量的绑定
    public ObservableField<String> xxc_order = new ObservableField<>("");
    

    //商品群组列表
    public SingleLiveEvent<ArrayList<GroupBean>> groupList = new SingleLiveEvent<>();

    //商品群组列表
    public SingleLiveEvent<ArrayList<CommunityBean>> goodList = new SingleLiveEvent<>();

    //小程序订单列表
    public SingleLiveEvent<ArrayList<XXCOrderBean>> xxc_order_list = new SingleLiveEvent<>();
    public SingleLiveEvent<XXCOrderCountBean> xxc_order_count = new SingleLiveEvent<>();
    //用户
    public SingleLiveEvent<UserBean> userEvent = new SingleLiveEvent<>();


    public SingleLiveEvent<SaleBillBean> saleBillField = new SingleLiveEvent<>();



    public MainViewModel(@NonNull Application application, DemoRepository repository) {
        super(application,repository);
        //从本地取得数据绑定到View层
        drawableImg = ContextCompat.getDrawable(getApplication(), R.mipmap.loading);
    }

    //打开钱箱的点击事件
    public BindingCommand OpenDrawOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
    //添加会员的点击事件
    public BindingCommand AddVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //交接班的点击事件
    public BindingCommand HandoverOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });

    //销售单据的点击事件
    public BindingCommand SalesDocumentOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);
        }
    });
    //申请订货的点击事件
    public BindingCommand OrderGoodsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);
        }
    });
    //更多的点击事件
    public BindingCommand moreOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(6);
        }
    });
    //选择VIP的点击事件
    public BindingCommand choiceVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(7);
        }
    });

    //清除按钮的点击事件
    public BindingCommand clearOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(10);
        }
    });

    //选择会员的点击事件
    public BindingCommand choiceOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (vip_bool.get()){
                IntegerEvent.setValue(12);
            }else {
                IntegerEvent.setValue(7);
            }
        }
    });
    //选择优惠券的点击事件
    public BindingCommand CouponOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(11);
        }
    });
    //点击vip信息的点击事件
    public BindingCommand VipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(12);
        }
    });

    //收银的点击事件
    public BindingCommand CashierOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(13);
        }
    });

    //订单的点击事件
    public BindingCommand takeFoodOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(14);
        }
    });

    //百度语言测试的点击事件
    public BindingCommand BDOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(15);
        }
    });

    //挂单的点击事件
    public BindingCommand PendingOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(16);
        }
    });
    //连接打印的点击事件
    public BindingCommand PrintOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(17);
        }
    });


    //刷新商品的点击事件
    public BindingCommand RefreshOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(22);
        }
    });

    //通用设置的点击事件
    public BindingCommand SetUpOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(SetUpActivity.class);
        }
    });

    //打印设置的点击事件
    public BindingCommand SetPrintOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(SettingsPrintActivity.class);
        }
    });

    //销售概况的点击事件
    public BindingCommand XiaoShouGaiKuangOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(SalesOverviewTwoActivity.class);
        }
    });

    /**
     * 获取群组
     * @param store_id 门店id
     */
    public void getGoodsGroup(String store_id){

        model.GOODS_GROUP(store_id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .subscribe(new DisposableObserver<BaseBean<ArrayList<GroupBean>>>() {
                    @Override
                    public void onNext(BaseBean<ArrayList<GroupBean>> response) {

                        if (response.isOk()){
                            String submitJsonData = new Gson().toJson(response.data);
                            ArrayList<GroupBean> list=  AppApplication.getObjectList(submitJsonData,GroupBean.class);

                            groupList.setValue(list);

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {

                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取商品
     * @param store_id 门店id
     * @param group_id 群组id
     */
    public void getGoodsGroup(String store_id,String group_id,String page,String limit){

        model.GOODS_List(store_id,group_id,page,limit)
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
                        //关闭对话框
                        dismissDialog();
                        if (response.isOk()){
                            String submitJsonData = new Gson().toJson(response.data);

                            RowsBean rowsBean=JSON.parseObject(toPrettyFormat(submitJsonData), RowsBean.class);

                            for (int i=0;i<rowsBean.getRows().size();i++){
                                rowsBean.getRows().get(i).setGoods_number(0);
                            }

                            goodList.setValue(rowsBean.getRows());

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
     * 获取个人信息
     */
    public void getMe() {
        model.GET_ME()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {
                        if (response.isOk()){
                            String submitJsonData = new Gson().toJson(response.data);

                            UserBean userBean= JSON.parseObject(toPrettyFormat(submitJsonData), UserBean.class);

                            AppApplication.spUtils.put("StoreId", userBean.getStore_id()+"");
                            AppApplication.spUtils.put("StoreName", userBean.getStore_name());
                            AppApplication.spUtils.put("StoreAddress", userBean.getStore_name());
                            AppApplication.spUtils.put("Id", userBean.getId()+"");
                            AppApplication.spUtils.put("Name", userBean.getName());
                            AppApplication.spUtils.put("topic", userBean.getTopic());

                            AppApplication.spUtils.put("is_order", userBean.getIs_order());
                            AppApplication.spUtils.put("start_time", userBean.getStart());
                            AppApplication.spUtils.put("end_time", userBean.getEnd());


                            store_name.set(userBean.getStore_name());
                            userEvent.setValue(userBean);
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
//                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
//                        dismissDialog();
                    }
                });
    }


    /**
     * 待处理小程序订单数量
     */
    public void getxcx_order_count() {
        model.XCX_ORDER_COUNT()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {

                        if (response.isOk()){
                            String submitJsonData = new Gson().toJson(response.data);

                            XXCOrderCountBean xxcOrderCountBean= JSON.parseObject(toPrettyFormat(submitJsonData), XXCOrderCountBean.class);
                            Log.e("返回结果-----已解析：",xxcOrderCountBean.getOrder_count());
                            if ("0".equals(xxcOrderCountBean.getOrder_count())){
                                xxc_order.set(null);
                            }else {
                                xxc_order.set(xxcOrderCountBean.getOrder_count());
                            }

                            xxc_order_count.setValue(xxcOrderCountBean);


                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
//                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
//                        dismissDialog();
                    }
                });
    }

    /**
     * 小程序订单列表
     * @param type  //状态 1 待接单 2 待出餐 3 待退款
     */
    public void xcx_order_list(String type,int type_Push) {
        model.XCX_ORDER_LIST(type)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

//                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {

                        if (response.isOk()){
                            String JsonData = new Gson().toJson(response.data);
                            ArrayList<XXCOrderBean> vipRechargeBean = AppApplication.getObjectList(JsonData, XXCOrderBean.class);

                            xxc_order_list.setValue(vipRechargeBean);

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            RxToast.normal(((ResponseThrowable) throwable).message);
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
     * 接单、拒单、出餐
     * @param order_sn
     * @param status
     */
    public void edit_order_status(String order_sn, String status) {
        model.DEIT_ORDER_STATUS(order_sn,status)
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
                        //关闭对话框
                        dismissDialog();
                        if (response.isOk()){
                            String JsonData = new Gson().toJson(response.data);
                            IntegerEvent.setValue(18);

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
     * @param pay_sn
     */
    public void xcx_order_details(String pay_sn) {
        model.XCX_ORDER_DETAILS(pay_sn)
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

                        //关闭对话框
                        dismissDialog();
                        if (response.isOk()){
                            String JsonData = new Gson().toJson(response.data);

                            SaleBillBean saleBillBean = JSON.parseObject(toPrettyFormat(JsonData), SaleBillBean.class);

                            if (saleBillBean!=null){

                                saleBillField.setValue(saleBillBean);
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
                            IntegerEvent.setValue(19);

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
     * 小程序订单审核
     * @param status
     * @param order_sn
     * @param refund_reason
     * @param modify_stock
     * @param dialog
     */
    public void audit_order_refund(final int status, String order_sn, String refund_reason, String modify_stock, final Dialog dialog) {
        model.AUDIT_ORDER_REFUND(status,order_sn,refund_reason,modify_stock)
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
                            dialog.dismiss();
                            if (status==1){
                                IntegerEvent.setValue(20);
                            }else {
                                IntegerEvent.setValue(21);
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
     * 搜索会员
     * @param tel  手机号
     * @param user_id  用户id
     */
    public void user_info(String tel,String user_id) {
        model.USER_INFO(tel,user_id)
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
                            ViewBean.setValue(vipBean);

                            KLog.d("返回数据"+JsonData);
//                            IntegerEvent.setValue(3);
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
