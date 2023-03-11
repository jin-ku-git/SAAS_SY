package com.youwu.shouyinsaas.ui.money;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.Drawable;

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
import com.youwu.shouyinsaas.ui.main.bean.CouponBean;
import com.youwu.shouyinsaas.ui.money.bean.OrderDetailed;
import com.youwu.shouyinsaas.ui.money.bean.PayBean;
import com.youwu.shouyinsaas.ui.money.bean.TestOrder;
import com.youwu.shouyinsaas.utils_view.BigDecimalUtils;
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
 * 2022/03/23
 */

public class CashierViewModel extends BaseViewModel<DemoRepository> {
    public ObservableField<VipBean> entity = new ObservableField<>();

    public SingleLiveEvent<VipBean> ViewBean = new SingleLiveEvent<>();
    public Drawable drawableImg;
    //开始时间的绑定
    public ObservableField<String> start_time = new ObservableField<>("");
    //开始时间的绑定
    public ObservableField<String> end_time = new ObservableField<>("");

    //总共多少件商品的绑定
    public ObservableField<String> total_number = new ObservableField<>("");
    //余额状态的绑定
    public ObservableField<Boolean> YE_state = new ObservableField<>();
    //现金状态的绑定
    public ObservableField<Boolean> XJ_state = new ObservableField<>();
    //微信状态的绑定
    public ObservableField<Boolean> WX_state = new ObservableField<>();
    //支付宝状态的绑定
    public ObservableField<Boolean> ZFB_state = new ObservableField<>();
    //外卖状态的绑定
    public ObservableField<Boolean> WM_state = new ObservableField<>();

    //vip信息的显示和隐藏
    public ObservableField<Boolean> vip_bool = new ObservableField<>();
    //用户名的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //用户名余额的绑定
    public ObservableField<String> vip_money = new ObservableField<>("");

    //是否是堂食
    public ObservableField<Boolean> eat_bool = new ObservableField<>();
    //优惠方式
    public ObservableField<Boolean> CouponBool = new ObservableField<>();
    //打包费用的绑定
    public ObservableField<String> packing_fee = new ObservableField<>();
    //选择的优惠券的绑定
    public ObservableField<String> SelectCoupon = new ObservableField<>();

    //商品价格
    public ObservableField<String> goods_prick = new ObservableField<>();
    public ObservableField<String> addOrderText = new ObservableField<>();


    //是否组合支付状态的绑定
    public ObservableField<Boolean> pay_state = new ObservableField<>();

    //第二个价格是否显示的绑定
    public ObservableField<Boolean> pay_Tow_state = new ObservableField<>();

    //第一个支付方式的绑定
    public ObservableField<String> pay_one_text = new ObservableField<>("");
    //第一个支付方式金额的绑定
    public ObservableField<String> pay_one_prick = new ObservableField<>("");
    //第二个支付方式的绑定
    public ObservableField<String> pay_Tow_text = new ObservableField<>("");
    //第二个支付方式金额的绑定
    public ObservableField<String> pay_Tow_prick = new ObservableField<>("");

    //折扣率的绑定
    public ObservableField<String> pay_discount_rate = new ObservableField<>("");
    //折后金额的绑定
    public ObservableField<String> pay_discount_prick = new ObservableField<>("");
    //应收金额的绑定
    public ObservableField<String> receivable_prick = new ObservableField<>("");
    //用餐人数的绑定
    public ObservableField<String> pay_diners_number = new ObservableField<>("");

    //应付金额的绑定
    public ObservableField<String> paid_in = new ObservableField<>("");
    //优惠金额的绑定
    public ObservableField<String> discount_price = new ObservableField<>("");


    //备注的绑定
    public ObservableField<String> remarks = new ObservableField<>("");
    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();
    //下单完成后的详情
    public SingleLiveEvent<OrderDetailed> OrderEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<ArrayList<CouponBean>> couponEvent = new SingleLiveEvent<>();


    public CashierViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        drawableImg = ContextCompat.getDrawable(getApplication(), R.mipmap.loading);
    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });
    //余额的点击事件
    public BindingCommand YEOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
    //现金的点击事件
    public BindingCommand XJOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //外卖的点击事件
    public BindingCommand WMOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);
        }
    });
    //微信的点击事件
    public BindingCommand WXOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });
    //支付宝的点击事件
    public BindingCommand ZFBOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);
        }
    });
    //选择VIP的点击事件
    public BindingCommand choiceVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(10);
        }
    });
    //点击vip信息的点击事件
    public BindingCommand VipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(6);
        }
    });

    //堂食的点击事件
    public BindingCommand HallFoodOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            eat_bool.set(true);
            IntegerEvent.setValue(7);
        }
    });
    //自提的点击事件
    public BindingCommand SelfMentionOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            eat_bool.set(false);
//            if ("0".equals(packing_fee.get())){
//                packing_fee.set("1");
//            }
//            IntegerEvent.setValue(7);
        }
    });

    //优惠券的点击事件
    public BindingCommand CouponOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CouponBool.set(true);
            IntegerEvent.setValue(9);
        }
    });
    //整单折扣的点击事件
    public BindingCommand DiscountOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CouponBool.set(false);
            IntegerEvent.setValue(7);
        }
    });

    //+0.5的点击事件
    public BindingCommand zeroPointFiveOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            packing_fee.set(BigDecimalUtils.add(packing_fee.get(),"0.5").toString());
            IntegerEvent.setValue(7);
        }
    });
    //+1的点击事件
    public BindingCommand AddOneOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            packing_fee.set(BigDecimalUtils.add(packing_fee.get(),"1").toString());
            IntegerEvent.setValue(7);
        }
    });
    //+2的点击事件
    public BindingCommand AddTwoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            packing_fee.set(BigDecimalUtils.add(packing_fee.get(),"2").toString());
            IntegerEvent.setValue(7);
        }
    });
    //+5的点击事件
    public BindingCommand AddFiveOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            packing_fee.set(BigDecimalUtils.add(packing_fee.get(),"5").toString());
            IntegerEvent.setValue(7);
        }
    });
    //重置的点击事件
    public BindingCommand ResetOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            packing_fee.set("1");
            IntegerEvent.setValue(7);
        }
    });

    //95%的点击事件
    public BindingCommand ninetyFiveOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pay_discount_rate.set("95");
            IntegerEvent.setValue(7);
        }
    });
    //90%的点击事件
    public BindingCommand ninetyOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pay_discount_rate.set("90");
            IntegerEvent.setValue(7);
        }
    });
    //85%的点击事件
    public BindingCommand eightyFiveOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pay_discount_rate.set("85");
            IntegerEvent.setValue(7);
        }
    });
    //75%的点击事件
    public BindingCommand seventyFiveOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pay_discount_rate.set("75");
            IntegerEvent.setValue(7);
        }
    });
    //0%的点击事件
    public BindingCommand zeroOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pay_discount_rate.set("0");
            IntegerEvent.setValue(7);
        }
    });
    //(重置)100%的点击事件
    public BindingCommand oneHundredOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pay_discount_rate.set("100");
            IntegerEvent.setValue(7);
        }
    });





    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *
     * @param goods_list    商品列表
     * @param store_id      门店id
     * @param user_id       收银员id
     * @param delivery_method       配送方式
     * @param tableware_number
     * @param packing_fee           打包费
     * @param mal
     * @param payList               支付方式
     * @param coupon_id             优惠卷id
     * @param user_coupon_id
     * @param discount              折扣率
     * @param dynamic_id             支付宝/微信
     */
    public void AddOrder(ArrayList<CommunityBean> goods_list, String store_id, String user_id, String delivery_method, String tableware_number, String packing_fee, String mal, PayBean payList,String coupon_id,int user_coupon_id,String discount,String dynamic_id) {

        TestOrder testOrder=new TestOrder();
        testOrder.setGoods_list(goods_list);
        testOrder.setStore_id(store_id);
        testOrder.setUser_id(user_id);
        testOrder.setDelivery_method(delivery_method);
        testOrder.setTableware_number(tableware_number);
        testOrder.setPacking_fee(packing_fee);
        testOrder.setMal(mal);
        testOrder.setPay_list(payList);
        testOrder.setCoupon_id(coupon_id);
        testOrder.setUser_coupon_id(user_coupon_id+"");
        testOrder.setDiscount(discount);
        testOrder.setDynamic_id(dynamic_id);
        testOrder.setMark(remarks.get());
        String toJson = new Gson().toJson(testOrder);

        KLog.d("上传信息："+toJson);

        String goodsList = new Gson().toJson(goods_list);
        String pay_list = new Gson().toJson(payList);

        addOrderText.set(toJson);

        KLog.d("支付信息："+pay_list);
        KLog.d("商品信息："+goodsList);
        KLog.d("上传参数：goods_list："+goodsList+",store_id:"+store_id+",user_id:"+user_id+",delivery_method:"+delivery_method+",tableware_number:"+
                tableware_number+",packing_fee:"+packing_fee+",mal:"+mal+",payList:"+pay_list+",coupon_id:"+coupon_id+",discount:"+discount+",dynamic_id:"+dynamic_id+",mark:"+remarks.get());


        model.Add_ORDER(goodsList,store_id,user_id,delivery_method,tableware_number,packing_fee,mal,pay_list,coupon_id,user_coupon_id+"",discount,dynamic_id,remarks.get())
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
                            String submitJsonData = new Gson().toJson(response.data);

                            OrderDetailed orderDetailed=JSON.parseObject(toPrettyFormat(submitJsonData), OrderDetailed.class);
                            OrderEvent.setValue(orderDetailed);



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
     * 查询优惠券
     * @param member_id   //用户id
     */
    public void user_coupon(String member_id,String store_id) {
        model.USER_COUPON(member_id,store_id)
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
                            KLog.d("解析数据："+JsonData);

                            ArrayList<CouponBean> list=  AppApplication.getObjectList(JsonData,CouponBean.class);

                            couponEvent.setValue(list);

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
