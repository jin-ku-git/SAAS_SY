package com.youwu.shouyinsaas.ui.money;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityCashierBinding;
import com.youwu.shouyinsaas.db.bean.OrderNumberBean;
import com.youwu.shouyinsaas.jianpan.MyCustKeybords;
import com.youwu.shouyinsaas.queue.LogTask;
import com.youwu.shouyinsaas.queue.TaskPriority;
import com.youwu.shouyinsaas.sunmi.PrintBean;
import com.youwu.shouyinsaas.ui.bean.VipBean;
import com.youwu.shouyinsaas.ui.main.adapter.CouponListRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.main.bean.CouponBean;
import com.youwu.shouyinsaas.ui.money.adapter.GoodsListRecycleAdapter;
import com.youwu.shouyinsaas.ui.money.bean.OrderDetailed;
import com.youwu.shouyinsaas.ui.money.bean.PayBean;
import com.youwu.shouyinsaas.ui.vip.SouSuoVipActivity;
import com.youwu.shouyinsaas.util.MainAboutUtils;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.BigDecimalUtils;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;
import com.youwu.shouyinsaas.utils_view.TimeUtil;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

import static com.youwu.shouyinsaas.app.AppApplication.getStrCunt;
import static com.youwu.shouyinsaas.ui.main.MainActivity.printerPresenter;

/**
 * 结算页面
 * 2022/03/25
 */
public class CashierActivity extends BaseActivity<ActivityCashierBinding, CashierViewModel> {


    //购物车recyclerveiw的适配器
    private GoodsListRecycleAdapter mGoodsListRecycleAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> GoodsEntityList = new ArrayList<CommunityBean>();

    //优惠券recyclerveiw的适配器
    private CouponListRecycleAdapter mCouponListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CouponBean> CouponEntityList = new ArrayList<>();
    private CouponBean couponBean;//选择的优惠券


    VipBean vipBean;

    List<Integer> pay_mode = new ArrayList<>();

    int total_number;//共多少件商品
    String paid_in;//总额
    String AmountReceivable;//实收金额

    String discount_price;//优惠金额
    boolean vip_bool;

    String beforeText = "";
    Intent intent;


    PayBean payList = new PayBean();
    int payType = 1;//第一种支付方式  余额
    int payTypeTwo;//第二种支付方式
    String dynamic_id;//支付宝/微信  码


    int discount_reset=0;


    private OrderNumberBean orderNumberBean;

    private static int counter = 0;

    @Override
    public void initParam() {
        super.initParam();
        //获取列表传入的实体
        GoodsEntityList = (ArrayList<CommunityBean>) getIntent().getSerializableExtra("ShoppingEntityList");
        vipBean = (VipBean) getIntent().getSerializableExtra("VipBean");
        paid_in = getIntent().getStringExtra("paid_in");
        discount_price = getIntent().getStringExtra("discount_price");
        vip_bool = getIntent().getBooleanExtra("vip_bool", true);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_cashier;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CashierViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(CashierViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case 1://余额支付
                        pay_method(1);
                        break;
                    case 2://现金支付
                        pay_method(2);
                        break;
                    case 3://微信支付
                        pay_method(3);
                        break;
                    case 4://支付宝支付
                        pay_method(4);
                        break;
                    case 5://外卖支付
                        pay_method(5);
                        break;
                    case 6://点击vip信息
                        intent = new Intent(CashierActivity.this, SouSuoVipActivity.class);
                        intent.putExtra("type", 2);
                        intent.putExtra("VipBean", viewModel.entity.get());
                        startActivityForResult(intent, 200);
                        break;
                    case 10://跳转到选择会员
                        intent = new Intent(CashierActivity.this, SouSuoVipActivity.class);
                        intent.putExtra("type", 1);
                        startActivityForResult(intent, 200);
                        break;
                    case 7://加打包费或折扣点击事件
                        discount_reset=1;
                        //重新获取焦点
                        disableShowSoftInput(binding.nullEdit);
                        cul(1);
                        break;

                    case 9:
//                        cul(1);
                        //重新获取焦点

                        if (vipBean == null) {
                            intent = new Intent(CashierActivity.this, SouSuoVipActivity.class);
                            intent.putExtra("type", 1);
                            startActivityForResult(intent, 200);
                        } else {
                            showCouponDialog();
                        }

                        break;

                }
            }
        });
        //下单完成
        viewModel.OrderEvent.observe(this, new Observer<OrderDetailed>() {
            @Override
            public void onChanged(OrderDetailed orderDetailed) {
                KLog.d("是否打印："+(binding.journalCheck.isChecked()?"是":"否"));
                new LogTask("DEFAULT","支付成功")
                        .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                        .enqueue(); //入队
                if (binding.journalCheck.isChecked()) {
                    PrintBean printBean = new PrintBean();

                    if (vipBean != null) {//vip信息
                        vipBean.setNick_name(orderDetailed.getDeliveryinfo().getPickup_name());
                        printBean.setBean(vipBean);
                    }
                    if (couponBean != null) {//优惠券信息
                        printBean.setCouponBean(couponBean);
                    }
                    printBean.setPick_code(orderDetailed.getDining_code());
                    printBean.setOrder_sn(orderDetailed.getDeliveryinfo().getOrder_sn());
                    printBean.setOrderNumberBean(orderNumberBean);
                    printBean.setShopCarYWGoodBeans(GoodsEntityList);
                    printBean.setCashier(AppApplication.spUtils.getString("Name"));
                    printBean.setPaymoney(viewModel.pay_discount_prick.get());
                    printBean.setCreatTime(formatData(System.currentTimeMillis()));
                    printBean.setCreatTime(orderDetailed.getCreated_at());
                    printBean.setMoney(paid_in + "");
                    printBean.setFreight("0");
                    printBean.setRemarks(viewModel.remarks.get());
                    if ("1".equals(payList.getType())) {
                        printBean.setPaytype("余额支付");
                    } else if ("2".equals(payList.getType())) {
                        printBean.setPaytype("微信支付");
                    } else if ("3".equals(payList.getType())) {
                        printBean.setPaytype("支付宝支付");
                    } else if ("4".equals(payList.getType())) {
                        printBean.setPaytype("现金支付");
                    } else if ("5".equals(payList.getType())) {
                        printBean.setPaytype("外卖支付");
                    } else if ("6".equals(payList.getType())) {
                        //此处必然是两种支付方式
                        printBean.setPaytype("组合支付(" + getPayTypeStr(payList.getDetails().get(0).getType()) + payList.getDetails().get(0).getMoney()
                                + getPayTypeStr(payList.getDetails().get(1).getType()) + payList.getDetails().get(1).getMoney() + ")");
                    }


                    if(viewModel.eat_bool.get()){
                        printBean.setDeliveryType("堂食");
                    }else {
                        printBean.setDeliveryType("自提");
                    }

                    int printType = AppApplication.spUtils.getInt("printType", 0);

                    if (printerPresenter == null) {

                        new LogTask("DEFAULT","请连接打印机")
                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                .enqueue(); //入队

                        AppApplication.spUtils.put("OrderNumberBean", AppApplication.gson.toJson(orderNumberBean));

                        setResult(666);
                        finish();
                        return;
                    }

//                    if (printType == 1) {
//                        printerPresenter.print2(printBean,2);
//                    }
                    printerPresenter.print2(printBean,2);

                }

                AppApplication.spUtils.put("OrderNumberBean", AppApplication.gson.toJson(orderNumberBean));

                setResult(666);
                finish();

            }
        });

        viewModel.couponEvent.observe(this, new Observer<ArrayList<CouponBean>>() {
            @Override
            public void onChanged(ArrayList<CouponBean> couponBeans) {
                CouponEntityList = couponBeans;
                initRecyclerViewFive();
            }
        });
        //会员信息
        viewModel.ViewBean.observe(this, new Observer<VipBean>() {
            @Override
            public void onChanged(VipBean vipBeans) {
                viewModel.vip_bool.set(true);
                viewModel.entity.set(vipBeans);
                vipBean = vipBeans;

                pay_method(1);

            }
        });

    }

    private String formatData(long nowTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return time.format(nowTime);
    }

    /**
     * 接收传递的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200://从选择会员页面传的数据
                    VipBean vipBeans = (VipBean) data.getSerializableExtra("vipBean");
                    int pay_type = data.getIntExtra("pay_type",0);
                    if (vipBeans.getType_state() == 1) {
                        viewModel.vip_bool.set(true);
                        viewModel.entity.set(vipBeans);
                        vipBean = vipBeans;
                        if (pay_type==1){//当是点击余额支付返回时默认切换为余额支付
                            pay_method(1);
                        }
                    } else {
                        viewModel.vip_bool.set(false);
                        viewModel.entity.set(null);
                        vipBean = null;
                        if (pay_mode.size()==2){
                            for (int j = 0; j < pay_mode.size(); j++) {
                                if (pay_mode.get(j) == 1) {//删除余额支付
                                    initRemove(pay_mode.get(j));
                                    pay_mode.remove(j);
                                }
                            }
                            viewModel.pay_Tow_state.set(false);
                            setUI(pay_mode.get(0));
                            viewModel.pay_one_prick.set(viewModel.pay_discount_prick.get());
                            viewModel.pay_Tow_prick.set("0");

                        }else if (pay_mode.get(0)==1){
                            pay_method(2);//改为现金支付
                        }
                    }
                    //向首页传递信息
                    Intent intent = new Intent();
                    intent.putExtra("data", vipBean);
                    setResult(223, intent);
                    break;
            }
        }
    }


    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        //默认不是组合支付
        viewModel.pay_state.set(false);
        viewModel.vip_bool.set(vip_bool);
        viewModel.eat_bool.set(true);
        viewModel.CouponBool.set(true);
        viewModel.SelectCoupon.set("无");

        viewModel.goods_prick.set("商品金额："+paid_in);

        binding.journalCheck.setChecked(true);
        binding.payOneEdit.setFocusable(false);
        binding.payOneEdit.setFocusableInTouchMode(false);

        viewModel.receivable_prick.set(paid_in);

        AmountReceivable = paid_in;

        viewModel.entity.set(vipBean);
        //价格默认为0元
        viewModel.packing_fee.set("0");
        //第一个支付价格
        viewModel.pay_one_prick.set(AmountReceivable);
        //第二个价格
        viewModel.pay_Tow_prick.set("0");
        //折后金额
        viewModel.pay_discount_prick.set(subZeroAndDot(AmountReceivable));
        //默认折扣100%
        viewModel.pay_discount_rate.set("100");

        //默认用餐人数
        viewModel.pay_diners_number.set("1");
        //支付金额
        viewModel.paid_in.set(AmountReceivable);
        //优惠金额
        viewModel.discount_price.set(discount_price);


        if (vipBean != null && BigDecimalUtils.compare(vipBean.getBalance(), paid_in + "") > 0) {
            //默认余额支付
            pay_mode.add(1);
            viewModel.pay_one_text.set("余额");
            viewModel.YE_state.set(true);
            viewModel.WX_state.set(false);

        } else {
            //默认微信支付
            pay_mode.add(3);
            payType = 2;
            viewModel.pay_one_text.set("微信");
            viewModel.YE_state.set(false);
            viewModel.WX_state.set(true);
        }

        viewModel.XJ_state.set(false);

        viewModel.ZFB_state.set(false);
        viewModel.WM_state.set(false);


        for (int i = 0; i < GoodsEntityList.size(); i++) {
            total_number += GoodsEntityList.get(i).getGoods_number();
        }
        viewModel.total_number.set("共" + total_number + "件");

        initBinding();


        /**
         * 组合支付的监听
         */
        binding.paysCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (!isChecked) {
                    String prick;
                    if (!viewModel.eat_bool.get()) {
                        prick = BigDecimalUtils.add(AmountReceivable, viewModel.packing_fee.get()) + "";
                    } else {
                        prick = AmountReceivable;
                    }
                    viewModel.pay_one_prick.set(prick);
                    viewModel.pay_Tow_prick.set("0");
                    viewModel.pay_Tow_state.set(false);
                }
                viewModel.pay_state.set(isChecked);
                if (isChecked) {
                    binding.payOneEdit.setText(AmountReceivable);
                    binding.payTowEdit.setText("0");
                    //可编辑状态：
                    binding.payOneEdit.setFocusableInTouchMode(true);
                    binding.payOneEdit.setFocusable(true);
                    binding.payOneEdit.requestFocus();
                } else {
                    //不可编辑状态：
                    binding.payOneEdit.setFocusable(false);
                    binding.payOneEdit.setFocusableInTouchMode(false);


                    KLog.d("pay_mode:" + pay_mode.get(0));
                    initPayMode(pay_mode.get(0));
                    setUI(pay_mode.get(0));

                }
            }
        });

        binding.customKeyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {

                if ("".equals(binding.comPrice.getText().toString())){
                    RxToast.normal("请输入付款金额!");
                    return;
                }


                if (viewModel.pay_state.get()) {

                    if (payType == 2 || payType == 3 || payTypeTwo == 2 || payTypeTwo == 3) {
                        RxToast.normal("请扫码完成收款");
                        return;
                    }
                    if (vipBean != null && payType == 1 && BigDecimalUtils.compare(vipBean.getBalance(), AmountReceivable) < 0) {
                        RxToast.normal("账户余额不足");
                        return;
                    }

                    if (payList.getDetails() != null && payList.getDetails().size() != 0) {
                        payList.getDetails().clear();
                    }
                    List<PayBean.Details> details = new ArrayList<>();
                    //但是组合支付时  设置为6
                    payList.setType("6");
//                    payList.setMoney(BigDecimalUtils.add(AmountReceivable, viewModel.discount_price.get()) + "");//应收金额加优惠金额
                    payList.setMoney(paid_in);//应收金额
                    PayBean.Details payBean = new PayBean.Details();
                    payBean.setType(payType + "");
                    payBean.setMoney(viewModel.pay_one_prick.get());
                    details.add(payBean);

                    PayBean.Details payBean1 = new PayBean.Details();
                    payBean1.setType(payTypeTwo + "");
                    payBean1.setMoney(viewModel.pay_Tow_prick.get());
                    details.add(payBean1);

                    payList.setDetails(details);
                } else {
                    if (vipBean != null && payType == 1 && BigDecimalUtils.compare(vipBean.getBalance(), AmountReceivable) < 0) {
                        RxToast.normal("账户余额不足");
                        return;
                    }
                    if (payType == 2 || payType == 3) {
                        RxToast.normal("请扫码完成收款");
                        return;
                    }
                    payList.setType(payType + "");
//                    payList.setMoney(viewModel.pay_one_prick.get());
                    payList.setMoney(paid_in);
                }

                String delivery_method;//配送方式
                if (viewModel.eat_bool.get()) {//3、自提 4、堂食

                    delivery_method = "4";
                } else {

                    delivery_method = "3";
                }
                KLog.d("配送方式:" + delivery_method);

                String store_id = AppApplication.spUtils.getString("StoreId") + "";

                String packing_fee = viewModel.packing_fee.get();

                String coupon_id;//优惠券id
                int user_coupon_id;//优惠券id
                String discount;//折扣比率
                String user_id;//用户id
                if (viewModel.entity.get() == null) {//如果用户信息为空
                    user_id = "";
                } else {
                    user_id = viewModel.entity.get().getId() + "";
                }

                if (viewModel.CouponBool.get()) {//如果使用优惠券
                    if (couponBean == null) {//如果没选择优惠券
                        coupon_id = "0";
                        user_coupon_id=0;
                    } else {
                        coupon_id = couponBean.getCoupon_id();
                        user_coupon_id=couponBean.getId();
                    }
                    discount = "0";
                } else {//折扣比率
                    discount = viewModel.pay_discount_rate.get();
                    coupon_id = "0";
                    user_coupon_id=0;
                }
                //下单
                viewModel.AddOrder(GoodsEntityList, store_id, user_id, delivery_method, "0", packing_fee, "0", payList, subZeroAndDot(coupon_id),user_coupon_id, discount, "0");


            }
        });
        initRecyclerViewThree();


        initOrderNumber();
    }


    //    初始化单号 获取的是下一个单号
    private String initOrderNumber() {
        orderNumberBean = AppApplication.gson.fromJson(AppApplication.spUtils.getString("OrderNumberBean", AppApplication.gson.toJson(new OrderNumberBean())), OrderNumberBean.class);
        if (!DateUtils.isToday(orderNumberBean.getCreatTime())) {
            orderNumberBean = new OrderNumberBean();
        } else {
            orderNumberBean.setOrderNumber(orderNumberBean.getOrderNumber() + 1);
        }
        Log.e("orderNumberBean", orderNumberBean.toString());
        return orderNumberBean.getOrderNumberStr();
    }

    /**
     * 多选支付方式
     *
     * @param pay_type
     */
    private void pay_method(int pay_type) {
        if (viewModel.pay_state.get()) {//判断是否为组合支付

            if (pay_mode.size() == 2) {
                if (pay_mode.get(0) == pay_type || pay_mode.get(1) == pay_type) {//判断是否已原则

                    KLog.d("包含：" + pay_type);
                    return;
                }
                //支付宝和微信不能同时选择
                if (pay_type == 3) {//点击微信判断是否含有支付宝 有则删除
                    for (int j = 0; j < pay_mode.size(); j++) {
                        if (pay_mode.get(j) == 4) {//删除支付宝
                            initRemove(pay_mode.get(j));
                            pay_mode.remove(j);
                        }
                    }
                } else if (pay_type == 4) {//点击支付宝判断是否含有微信 有则删除
                    for (int j = 0; j < pay_mode.size(); j++) {
                        if (pay_mode.get(j) == 3) {//删除微信
                            initRemove(pay_mode.get(j));
                            pay_mode.remove(j);
                        }
                    }
                }
                if (pay_type == 1 && vipBean == null) {
                    intent = new Intent(CashierActivity.this, SouSuoVipActivity.class);
                    intent.putExtra("type", 1);
                    startActivityForResult(intent, 200);
                    return;
                }
                //如果还有两种支付方式去掉第一个支付方式
                if (pay_mode.size() == 2) {
                    initRemove(pay_mode.get(0));
                    pay_mode.remove(0);
                }
            } else {
                //添加点击的支付方式
                if (pay_type == 1 && vipBean == null) {
                    intent = new Intent(CashierActivity.this, SouSuoVipActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("pay_type", 1);
                    startActivityForResult(intent, 200);
                    return;
                } else {
                    if (pay_mode.get(0) == 3 && pay_type == 4) {
                        initRemove(pay_mode.get(0));
                        pay_mode.remove(0);
                    } else if (pay_mode.get(0) == 4 && pay_type == 3) {
                        initRemove(pay_mode.get(0));
                        pay_mode.remove(0);
                    }
                }
            }
            pay_mode.add(pay_type);
            switch (pay_type) {
                case 1:
                    viewModel.YE_state.set(true);
                    break;
                case 2:
                    viewModel.XJ_state.set(true);
                    break;
                case 3:
                    viewModel.WX_state.set(true);
                    break;
                case 4:
                    viewModel.ZFB_state.set(true);
                    break;
                case 5:
                    viewModel.WM_state.set(true);
                    break;
            }

            KLog.d("pay_mode.size:" + pay_mode.size());

            //更新ui
            if (pay_mode.size() == 2) {
                initpayText(pay_mode.get(0), pay_mode.get(1));
            } else {
                setUI(pay_type);
            }

        } else {
            if (pay_type == 1 && vipBean == null) {
                intent = new Intent(CashierActivity.this, SouSuoVipActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("pay_type", 1);
                startActivityForResult(intent, 200);
                return;
            } else {
                initPayMode(pay_type);
            }

            setUI(pay_type);

        }
    }

    private void setUI(int pay_type) {
        switch (pay_type) {
            case 1:
                viewModel.YE_state.set(true);
                viewModel.pay_one_text.set("余额");
                payType = 1;
                break;
            case 2:
                viewModel.XJ_state.set(true);
                viewModel.pay_one_text.set("现金");
                payType = 4;
                break;
            case 3:
                viewModel.WX_state.set(true);
                viewModel.pay_one_text.set("微信");
                payType = 2;
                break;
            case 4:
                viewModel.ZFB_state.set(true);
                viewModel.pay_one_text.set("支付宝");
                payType = 3;
                break;
            case 5:
                viewModel.WM_state.set(true);
                viewModel.pay_one_text.set("外卖");
                payType = 5;
                break;
        }
    }

    //支付方式赋值
    private void initpayText(Integer integer, Integer integer1) {
        viewModel.pay_Tow_state.set(true);
        switch (integer) {
            case 1:
                viewModel.pay_one_text.set("余额");
                payType = 1;
                break;
            case 2:
                viewModel.pay_one_text.set("现金");
                payType = 4;
                break;
            case 3:
                viewModel.pay_one_text.set("微信");
                payType = 2;
                break;
            case 4:
                viewModel.pay_one_text.set("支付宝");
                payType = 3;
                break;
            case 5:
                viewModel.pay_one_text.set("外卖");
                payType = 5;
                break;
        }
        switch (integer1) {
            case 1:
                viewModel.pay_Tow_text.set("余额");
                payTypeTwo = 1;
                break;
            case 2:
                viewModel.pay_Tow_text.set("现金");
                payTypeTwo = 4;
                break;
            case 3:
                viewModel.pay_Tow_text.set("微信");
                payTypeTwo = 2;
                break;
            case 4:
                viewModel.pay_Tow_text.set("支付宝");
                payTypeTwo = 3;
                break;
            case 5:
                viewModel.pay_Tow_text.set("外卖");
                payTypeTwo = 5;
                break;
        }
    }

    private void initRemove(int j) {
        switch (j) {
            case 1://余额
                viewModel.YE_state.set(false);
                break;
            case 2://现金
                viewModel.XJ_state.set(false);
                break;
            case 3://微信
                viewModel.WX_state.set(false);
                break;
            case 4://支付宝
                viewModel.ZFB_state.set(false);
                break;
            case 5://外卖
                viewModel.WM_state.set(false);
                break;
        }
    }

    /**
     * 支付方式
     */
    private void initPayMode(int pay) {
        //初始化
        itAllFalse();
        if (pay_mode.size() != 0) {//全部删除
            pay_mode.clear();
        }
        //重新添加
        pay_mode.add(pay);
    }

    /**
     * 键盘绑定
     */
    private void initBinding() {

        disableShowSoftInput(binding.nullEdit);
        disableShowSoftInput(binding.payDiscountRate);
        disableShowSoftInput(binding.comPrice);
        disableShowSoftInput(binding.payOneEdit);
        disableShowSoftInput(binding.payTowEdit);

        binding.nullEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.nullEdit);
                }
            }
        });
        //折扣率
        binding.payDiscountRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.payDiscountRate);
                }
            }
        });
        binding.payDiscountRate.setSelection(binding.payDiscountRate.getText().length());

        //实付价格
        binding.comPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.comPrice);
                }
            }
        });

        //第一个价格
        binding.payOneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.payOneEdit);
                }
            }
        });
        //第二价格
        binding.payTowEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.payTowEdit);
                }
            }
        });
        //折后率监听
        binding.payDiscountRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //判断数据不为空，数据有改变，数据首位不为点
                if (!TextUtils.isEmpty(s.toString())
                        && !s.toString().equals(beforeText)
                        && !s.toString().equals(".")) {
                    if (binding.payDiscountRate.hasFocus()) {
                        viewModel.pay_discount_rate.set(subZeroAndDot(binding.payDiscountRate.getText().toString()));
                        cul(2);
                    }
                } else if (TextUtils.isEmpty(s.toString())) {//如果数据为空 则折扣率改为0
                    viewModel.pay_discount_rate.set("0");
                    cul(2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


                String editStr = editable.toString().trim();
                int posDot = editStr.indexOf(".");

                if (posDot < 0) {
                    return;
                }else if (posDot == 0) {
                    RxToast.normal("首位不可为小数点");
                    //首位是点，去掉点
                    editable.delete(posDot, posDot + 1);
                }

                //不允许输入3位小数,超过三位就删掉
                if (editStr.length() - posDot - 1 > 2) {
                    RxToast.normal("最多输入小数点后两位");
                    editable.delete(posDot + 3, posDot + 4);
                }
                //最多输入一位小数点，多的删除
                int posDotTow = editable.toString().trim().lastIndexOf(".");
                if (getStrCunt(editable.toString().trim(),".")>1){
                    RxToast.normal("最多输入一个小数点");
                    editable.delete(posDotTow, posDotTow + 1);
                }
            }
        });
        binding.payDiscountRate.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        binding.comPrice.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    binding.comPrice.setSelectAllOnFocus(true);
                    binding.comPrice.selectAll();

                }

                return false;

            }

        });

        binding.comPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (binding.comPrice.hasFocus()){
                    discount_reset=0;
                    counter=0;
                    //判断数据不为空，数据有改变，数据首位不为点
                    if (!TextUtils.isEmpty(s.toString())
                            && !s.toString().equals(beforeText)
                            && !s.toString().equals(".")) {
                        if ("".equals(binding.comPrice.getText().toString())){
                            binding.comPrice.setText("0");
                        }
                        if (binding.comPrice.hasFocus()&&viewModel.CouponBool.get()){
                            viewModel.CouponBool.set(false);
                        }
                        KLog.d("comPrice:"+s.toString().trim());
//                    KLog.d(".的个数："+countStr(s.toString().trim(),"."));

                        if (countStr(s.toString().trim(),".")==2){
                            String str=s.toString().trim();
                            binding.comPrice.setText(str.substring(0,str.length()-1));

                        }
                        KLog.d("comPrice1:"+binding.comPrice.toString());

                        //如果是自提
                        if (!viewModel.eat_bool.get()){
                            //输入的大于应收金额+打包费
                            if (Double.parseDouble(binding.comPrice.getText().toString())>BigDecimalUtils.add(paid_in,viewModel.packing_fee.get())){
                                binding.comPrice.setText(BigDecimalUtils.add(paid_in,viewModel.packing_fee.get())+"");

                                viewModel.pay_discount_rate.set("100");
                                viewModel.discount_price.set("0");
                            }else {
                                KLog.d("折扣率111111："+BigDecimalUtils.formatRoundUp(BigDecimalUtils.divide(binding.comPrice.getText().toString(),BigDecimalUtils.add(paid_in,viewModel.packing_fee.get())+""),2)*100);
                                viewModel.pay_discount_rate.set(subZeroAndDot(BigDecimalUtils.formatRoundUp(BigDecimalUtils.divide(binding.comPrice.getText().toString(),BigDecimalUtils.add(paid_in,viewModel.packing_fee.get())+"")*100,2)+""));
                                viewModel.discount_price.set(BigDecimalUtils.subtract(BigDecimalUtils.add(paid_in,viewModel.packing_fee.get())+"",binding.comPrice.getText().toString())+"");
                            }
                        }else {
                            KLog.d("当事堂食时：comPrice："+binding.comPrice.getText().toString()+",paid_in:"+paid_in);
                            //输入的大于应收金额+打包费
                            if (Double.parseDouble(binding.comPrice.getText().toString())>Double.parseDouble(paid_in)){
                                binding.comPrice.setText(paid_in);
                                viewModel.pay_discount_rate.set("100");
                                viewModel.discount_price.set("0");
                            }else {
                                KLog.d("折扣率222222："+BigDecimalUtils.formatRoundUp(BigDecimalUtils.divide(binding.comPrice.getText().toString(),paid_in),2)*100+"");
                                viewModel.pay_discount_rate.set(subZeroAndDot(BigDecimalUtils.formatRoundUp(BigDecimalUtils.divide(binding.comPrice.getText().toString(),paid_in)*100,2)+""));
                                viewModel.discount_price.set(BigDecimalUtils.subtract(paid_in,binding.comPrice.getText().toString())+"");
                            }
                        }

                        AmountReceivable=binding.comPrice.getText().toString();

                        KLog.d("aaaaaaa:"+AmountReceivable);
                        if (binding.paysCheck.isChecked()){
                            binding.payOneEdit.setText(AmountReceivable);

                            binding.payTowEdit.setText("0");
                        }
                    }

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                String editStr = editable.toString().trim();
                int posDot = editStr.indexOf(".");

                if (posDot < 0) {
                    return;
                }else if (posDot == 0) {
                    RxToast.normal("首位不可为小数点");
                    //首位是点，去掉点
                    editable.delete(posDot, posDot + 1);
                }

                //不允许输入3位小数,超过三位就删掉
                if (editStr.length() - posDot - 1 > 2) {
                    RxToast.normal("最多输入小数点后两位");
                    editable.delete(posDot + 3, posDot + 4);
                }
                //最多输入一位小数点，多的删除
                int posDotTow = editable.toString().trim().lastIndexOf(".");
                if (getStrCunt(editable.toString().trim(),".")>1){
                    RxToast.normal("最多输入一个小数点");
                    editable.delete(posDotTow, posDotTow + 1);
                }

            }
        });
        binding.comPrice.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


        //第一个价格监听
        binding.payOneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //判断数据不为空，数据有改变，数据首位不为点
                if (!TextUtils.isEmpty(s.toString())
                        && !s.toString().equals(beforeText)
                        && !s.toString().equals(".")) {
                    try {
                        String prick = "0";

                        if (viewModel.pay_one_text.get().equals("余额") && vipBean != null) {
                            Double subtract = BigDecimalUtils.subtract(vipBean.getBalance() + "", binding.payOneEdit.getText().toString());
                            if (subtract < 0) {
                                binding.payOneEdit.setText(vipBean.getBalance());
                                return;
                            }
                        }
                        Double subtract = BigDecimalUtils.subtract(AmountReceivable + "", binding.payOneEdit.getText().toString());
                        if (subtract < 0) {
                            binding.payOneEdit.setText(AmountReceivable + "");
                            return;
                        }
                        if (binding.payOneEdit.hasFocus()) {
                            cul(2);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String editStr = editable.toString().trim();
                int posDot = editStr.indexOf(".");

                if (posDot < 0) {
                    return;
                }else if (posDot == 0) {
                    RxToast.normal("首位不可为小数点");
                    //首位是点，去掉点
                    editable.delete(posDot, posDot + 1);
                }

                //不允许输入3位小数,超过三位就删掉
                if (editStr.length() - posDot - 1 > 2) {
                    RxToast.normal("最多输入小数点后两位");
                    editable.delete(posDot + 3, posDot + 4);
                }
                //最多输入一位小数点，多的删除
                int posDotTow = editable.toString().trim().lastIndexOf(".");
                if (getStrCunt(editable.toString().trim(),".")>1){
                    RxToast.normal("最多输入一个小数点");
                    editable.delete(posDotTow, posDotTow + 1);
                }
            }
        });
        binding.payOneEdit.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        //第二个价格监听
        binding.payTowEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //判断数据不为空，数据有改变，数据首位不为点
                if (!TextUtils.isEmpty(s.toString())
                        && !s.toString().equals(beforeText)
                        && !s.toString().equals(".")) {
                    try {
                        if (viewModel.pay_Tow_text.get().equals("余额") && vipBean != null) {
                            Double subtract = BigDecimalUtils.subtract(vipBean.getBalance() + "", binding.payTowEdit.getText().toString());
                            if (subtract < 0) {
                                binding.payTowEdit.setText(vipBean.getBalance());
                                return;
                            }
                        }
                        Double subtract = BigDecimalUtils.subtract(AmountReceivable + "", binding.payTowEdit.getText().toString());
                        if (subtract < 0 && viewModel.pay_state.get()) {
                            binding.payTowEdit.setText(AmountReceivable + "");
                            return;
                        }
                        if (binding.payTowEdit.hasFocus()) {
                            cul(2);
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String editStr = editable.toString().trim();
                int posDot = editStr.indexOf(".");

                if (posDot < 0) {
                    return;
                }else if (posDot == 0) {
                    RxToast.normal("首位不可为小数点");
                    //首位是点，去掉点
                    editable.delete(posDot, posDot + 1);
                }

                //不允许输入3位小数,超过三位就删掉
                if (editStr.length() - posDot - 1 > 2) {
                    RxToast.normal("最多输入小数点后两位");
                    editable.delete(posDot + 3, posDot + 4);
                }
                //最多输入一位小数点，多的删除
                int posDotTow = editable.toString().trim().lastIndexOf(".");
                if (getStrCunt(editable.toString().trim(),".")>1){
                    RxToast.normal("最多输入一个小数点");
                    editable.delete(posDotTow, posDotTow + 1);
                }
            }
        });
        binding.payTowEdit.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    /**
     *
     * @param type  1 点击折扣或打包费事件 2 监听事件
     */
    private void cul(int type) {
        culDisount();
        if (type==1){
            binding.payOneEdit.setText(AmountReceivable);
            binding.payTowEdit.setText("0");
        }

        if (type==2){
            String prick = "0";//应收金额
            if (binding.nullEdit.hasFocus()) {
                    KLog.d("payDiscountRate11");
                prick = BigDecimalUtils.formatRoundUp(AmountReceivable, 2) + "";
                //应付金额
                viewModel.pay_discount_prick.set(subZeroAndDot(AmountReceivable));
                viewModel.pay_one_prick.set(prick);
                viewModel.pay_Tow_prick.set("0");
            } else if (binding.payDiscountRate.hasFocus()) {
                KLog.d("payDiscountRate11");
                prick = BigDecimalUtils.formatRoundUp(AmountReceivable, 2) + "";
                //应付金额
                viewModel.pay_discount_prick.set(subZeroAndDot(AmountReceivable));
                viewModel.pay_one_prick.set(prick);
                viewModel.pay_Tow_prick.set("0");
            } else if (binding.comPrice.hasFocus()) {



                Double multiply = BigDecimalUtils.multiply(binding.comPrice.getText().toString(), "100");
                Double divide;
                //是否是堂食
                if (viewModel.eat_bool.get()){
                    divide = BigDecimalUtils.divide(multiply, Double.parseDouble(paid_in));
                }else {
                    divide = BigDecimalUtils.divide(BigDecimalUtils.subtract(multiply+"",viewModel.packing_fee.get())+"", paid_in);
                }
                KLog.d("divide："+divide);
                if (discount_reset!=1){
                    KLog.d("折扣率222222："+BigDecimalUtils.formatRoundUp(divide*100, 2)*100);
                    viewModel.pay_discount_rate.set(subZeroAndDot(BigDecimalUtils.formatRoundUp(divide*100, 2) + ""));
                }
                Double s = BigDecimalUtils.multiply(AmountReceivable, viewModel.pay_discount_rate.get());
                KLog.d("计算价格："+s);

                AmountReceivable = BigDecimalUtils.formatRoundUp(BigDecimalUtils.divide(s, 100), 2) + "";

                prick = BigDecimalUtils.formatRoundUp(AmountReceivable, 2) + "";
                //应付金额
                viewModel.pay_discount_prick.set(subZeroAndDot(prick));
                viewModel.pay_one_prick.set(prick);
                viewModel.pay_Tow_prick.set("0");
            }else if (binding.payOneEdit.hasFocus()) {

                prick = BigDecimalUtils.formatRoundUp(AmountReceivable, 2) + "";
                Double subtract = BigDecimalUtils.subtract(prick + "", binding.payOneEdit.getText().toString());
                if (pay_mode.size() > 1) {
                    if ("".equals(binding.payOneEdit.getText().toString())) {//当第一个价格等于空时第二个价格等于实付价格
                        binding.payTowEdit.setText(BigDecimalUtils.formatRoundUp(prick, 2) + "");
                    } else {
                        if (BigDecimalUtils.compare(binding.payOneEdit.getText().toString(), prick + "") > 0) {
                            binding.payOneEdit.setText(BigDecimalUtils.formatRoundUp(prick, 2) + "");
                        }
                        if (subtract <= 0) {//当支付价格减去第一个价格等于或小于0时  把实付价格付给第一个价格 第二个价格等于0
                            binding.payTowEdit.setText("0");
                        } else {

                            binding.payTowEdit.setText(BigDecimalUtils.formatRoundUp(subtract, 2) + "");
                        }
                    }
                }
                viewModel.pay_discount_prick.set(subZeroAndDot(BigDecimalUtils.formatRoundUp(prick, 2) + ""));
            } else if (binding.payTowEdit.hasFocus()) {
                prick = BigDecimalUtils.formatRoundUp(AmountReceivable, 2) + "";
                Double subtract = BigDecimalUtils.subtract(prick + "", binding.payTowEdit.getText().toString());
                if (pay_mode.size() > 1) {
                    if ("".equals(binding.payTowEdit.getText().toString())) {//当第一个价格等于空时第二个价格等于实付价格
                        binding.payOneEdit.setText(BigDecimalUtils.formatRoundUp(prick, 2) + "");
                    } else {
                        if (BigDecimalUtils.compare(binding.payTowEdit.getText().toString(), prick + "") > 0) {
                            binding.payTowEdit.setText(BigDecimalUtils.formatRoundUp(prick, 2) + "");
                        }
                        if (subtract <= 0) {//当支付价格减去第一个价格等于或小于0时  把实付价格付给第一个价格 第二个价格等于0
                            binding.payOneEdit.setText("0");
                        } else {
                            binding.payOneEdit.setText(BigDecimalUtils.formatRoundUp(subtract, 2) + "");
                        }
                    }
                }
                viewModel.pay_discount_prick.set(subZeroAndDot(BigDecimalUtils.formatRoundUp(prick, 2) + ""));
            } else {
                prick = BigDecimalUtils.formatRoundUp(AmountReceivable, 2) + "";
                viewModel.pay_discount_prick.set(subZeroAndDot(AmountReceivable));
                viewModel.pay_one_prick.set(prick);
                viewModel.pay_Tow_prick.set("0");
            }
        }

        double total_money;
        //消费总额+打包费-应付金额
        if (viewModel.eat_bool.get()) {
            total_money = Double.parseDouble(paid_in);
        } else {
            total_money = BigDecimalUtils.add(paid_in, viewModel.packing_fee.get());
        }
        discount_price = BigDecimalUtils.subtract(total_money + "", AmountReceivable) + "";
        //优惠金额
        viewModel.discount_price.set(discount_price);

        viewModel.pay_discount_prick.set(AmountReceivable);
    }
    //   String paid_in;//总额
    //    String AmountReceivable;//应收金额

    /**
     * 计算减去 折扣和优惠券金额
     */
    private void culDisount() {
        AmountReceivable = paid_in + "";
        //判断是堂食还是自提  true 堂食 /false 自提
        if (!viewModel.eat_bool.get()) {//堂食
            AmountReceivable = BigDecimalUtils.add(paid_in, viewModel.packing_fee.get()) + "";
        }
        //优惠方式  true 优惠券 /false 折扣
        if (viewModel.CouponBool.get()) {
            //减去优惠券金额
            if (couponBean != null) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long end = 0L;
                Long start = 0L;
                try {
                    end = sf.parse(couponBean.getEnd_time()).getTime();// 日期转换为时间戳
                    start = sf.parse(couponBean.getStart_time()).getTime();// 日期转换为时间戳
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() > end) {
                    RxToast.normal("优惠券已经过期！");
                    couponBean = null;
                    viewModel.SelectCoupon.set("无");
                    return;
//                    showMenu();
                } else if (System.currentTimeMillis() < start) {
                    RxToast.normal("优惠券活动还未开始！");
                    viewModel.SelectCoupon.set("无");
                    return;
                } else {
                    switch (couponBean.getCoupon_type()) {
                        case 1://满减券
                            if (BigDecimalUtils.compare(couponBean.getFull_price() + "", AmountReceivable + "") > 0) {
                                RxToast.normal("商品总金额小于满减金额！");
                                couponBean = null;
                                viewModel.SelectCoupon.set("无");
                            } else {

                                if (BigDecimalUtils.compare(couponBean.getReduce_price() + "", AmountReceivable + "") > 0){
                                    AmountReceivable = "0";
                                }else {
                                    AmountReceivable = BigDecimalUtils.subtract(AmountReceivable, couponBean.getReduce_price() + "") + "";
                                }

                                viewModel.SelectCoupon.set(couponBean.getCoupon_name());
                            }
                            break;
                        case 2://折扣券
                            //
//                            if (couponBean.getHighest_price() == 0 || BigDecimalUtils.compare(couponBean.getHighest_price() + "", BigDecimalUtils.subtract(AmountReceivable, BigDecimalUtils.multiply(AmountReceivable, couponBean.getReduce_price() + "") + "") + "") >= 0) {
//                                AmountReceivable = BigDecimalUtils.multiply(AmountReceivable, couponBean.getReduce_price() + "") + "";
//                                viewModel.SelectCoupon.set(couponBean.getCoupon_name());
//                                KLog.d("符合");
//                            } else {
//                                AmountReceivable = BigDecimalUtils.subtract(AmountReceivable, couponBean.getHighest_price() + "") + "";
//                                viewModel.SelectCoupon.set(couponBean.getCoupon_name());
//                            }
                            AmountReceivable = BigDecimalUtils.multiply(AmountReceivable, couponBean.getDiscount_price() + "") + "";
                            viewModel.SelectCoupon.set(couponBean.getCoupon_name());

                            break;
                        case 3://配送券

                            break;
                    }


                }
            } else {

                viewModel.SelectCoupon.set("无");
            }

        } else {
            Double s = BigDecimalUtils.multiply(AmountReceivable, viewModel.pay_discount_rate.get())/100;
            AmountReceivable = BigDecimalUtils.formatRoundUp(s, 2) + "";

            KLog.d("AmountReceivable=======:" + AmountReceivable);
        }

    }

    private void itAllFalse() {
        viewModel.YE_state.set(false);
        viewModel.XJ_state.set(false);
        viewModel.WX_state.set(false);
        viewModel.ZFB_state.set(false);
        viewModel.WM_state.set(false);
    }


    /**
     * 购物车列表
     */
    private void initRecyclerViewThree() {

        //创建adapter
        mGoodsListRecycleAdapter = new GoodsListRecycleAdapter(this, GoodsEntityList);
        //给RecyclerView设置adapter
        binding.goodsRecyclerview.setAdapter(mGoodsListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.goodsRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.goodsRecyclerview.getItemDecorationCount() == 0) {
            binding.goodsRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

    }

    /**
     * 禁止Edittext弹出软件盘，光标依旧正常显示。
     */
    public void disableShowSoftInput(EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
        }

        try {
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
        }
    }


    private RecyclerView coupon_recycler;
    private LinearLayout dialog_null_view;

    /**
     * 优惠券弹窗
     */
    private void showCouponDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_coupon, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height * 0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//返回
        TextView use_coupon = (TextView) dialogView.findViewById(R.id.use_coupon);//确认使用优惠券
        coupon_recycler = (RecyclerView) dialogView.findViewById(R.id.coupon_recycler);
        dialog_null_view=(LinearLayout) dialogView.findViewById(R.id.dialog_null_view);

        if (CouponEntityList.size() == 0) {
            initCouPonList();

        } else {
            initRecyclerViewFive();
        }


        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //确认使用优惠券
        use_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.CouponBool.set(true);
                binding.nullEdit.requestFocus();
                disableShowSoftInput(binding.nullEdit);
                cul(1);

                dialog.dismiss();
//                RxToast.normal("使用优惠券");
            }
        });

    }

    /**
     * 优惠券模拟数据
     */
    private void initCouPonList() {

        viewModel.user_coupon(vipBean.getId() + "", AppApplication.spUtils.getString("StoreId"));

    }

    /**
     * 优惠券列表
     */
    private void initRecyclerViewFive() {
        if (CouponEntityList.size()==0){
            dialog_null_view.setVisibility(View.VISIBLE);
        }
        //创建adapter
        mCouponListRecycleAdapter = new CouponListRecycleAdapter(this, CouponEntityList);
        //给RecyclerView设置adapter
        coupon_recycler.setAdapter(mCouponListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        coupon_recycler.setScrollbarFadingEnabled(false);
        coupon_recycler.setScrollBarFadeDuration(0);

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        coupon_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (coupon_recycler.getItemDecorationCount() == 0) {
            coupon_recycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCouponListRecycleAdapter.setOnCouPonListener(new CouponListRecycleAdapter.OnCouPonListener() {
            @Override
            public void onCouPon(CouponBean data, int position) {
                couponBean = data;

            }
        });
    }


    private void pay(String dynamic_id) {
        if (viewModel.pay_state.get()) {

            if (payList.getDetails() != null && payList.getDetails().size() != 0) {
                payList.getDetails().clear();
            }
            List<PayBean.Details> details = new ArrayList<>();
            //但是组合支付时  设置为6
            payList.setType("6");
//            payList.setMoney(BigDecimalUtils.add(AmountReceivable, viewModel.discount_price.get()) + "");//应收金额加优惠金额
            payList.setMoney(paid_in);//应收金额
            PayBean.Details payBean = new PayBean.Details();
            payBean.setType(payType + "");
            payBean.setMoney(viewModel.pay_one_prick.get());
            details.add(payBean);

            PayBean.Details payBean1 = new PayBean.Details();
            payBean1.setType(payTypeTwo + "");
            payBean1.setMoney(viewModel.pay_Tow_prick.get());
            details.add(payBean1);

            payList.setDetails(details);
        } else {
            if (vipBean != null && payType == 1 && BigDecimalUtils.compare(vipBean.getBalance(), AmountReceivable) < 0) {
                RxToast.normal("收款金额不足");
                return;
            }

            payList.setType(payType + "");
//            payList.setMoney(viewModel.pay_one_prick.get());
            payList.setMoney(paid_in);
        }

        String delivery_method;//配送方式
        if (viewModel.eat_bool.get()) {//3、自提 4、堂食
//            delivery_method = "堂食";
            delivery_method = "4";
        } else {
//            delivery_method = "自提";
            delivery_method = "3";
        }
        KLog.d("配送方式:" + delivery_method);

        String store_id = AppApplication.spUtils.getString("StoreId") + "";

        String packing_fee = viewModel.packing_fee.get();

        String coupon_id;//优惠券id
        int user_coupon_id;//优惠券id
        String discount;//折扣比率
        String user_id;//用户id
        if (viewModel.entity.get() == null) {//如果用户信息为空
            user_id = "";
        } else {
            user_id = viewModel.entity.get().getId() + "";
        }

        if (viewModel.CouponBool.get()) {//如果使用优惠券
            if (couponBean == null) {//如果没选择优惠券
                coupon_id = "0";
                user_coupon_id=0;
            } else {
                coupon_id = couponBean.getCoupon_id();
                user_coupon_id = couponBean.getId();
            }
            discount = "0";
        } else {//折扣比率
            discount = viewModel.pay_discount_rate.get();
            coupon_id = "0";
            user_coupon_id=0;
        }

        viewModel.AddOrder(GoodsEntityList, store_id, user_id, delivery_method, "0", packing_fee, "0", payList, subZeroAndDot(coupon_id),user_coupon_id, discount, dynamic_id);


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            //监听到删除按钮被按下
            String text = binding.RemarksEdit.getText().toString();
            if(text.length() > 0 ){
                //判断文本框是否有文字，如果有就去掉最后一位
                String newText = text.substring(0, text.length() - 1);
                binding.RemarksEdit.setText(newText);
                binding.RemarksEdit.setSelection(newText.length());
                //设置焦点在最后};}
            }
        }
        if (ScanUtils.getInstance().isInputFromScanner(CashierActivity.this, event)) {

            //暂时取消扫码
            ScanUtils.getInstance().setOnResultListener(scanListener);
            ScanUtils.getInstance().analysisKeyEvent(event);
            return false;
        }

        return false;
    }

    ScanUtils.OnResultListener scanListener = new ScanUtils.OnResultListener() {
        @Override
        public void onResult(final String resultStr) {
            Log.e("scanToWork", "onResult:" + resultStr);

            if (TextUtils.isEmpty(resultStr)) {
                return;
            }

            long last = AppApplication.spUtils.getLong("lastpay");
            if (System.currentTimeMillis() - last < 2000) {
                return;
            }
            AppApplication.spUtils.put("lastpay", System.currentTimeMillis());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scanToWork(resultStr);
                }
            });
        }
    };


    private void scanToWork(String resultStr) {
        if (resultStr.startsWith("xzks")) {
            String order_sn=resultStr.replace("xzks_","");
            viewModel.close_order(order_sn,CashierActivity.this);

        }else if (MainAboutUtils.isWXPayCode(resultStr)) {//微信
            Log.e("scanToWork", "微信:" + resultStr);

            if (viewModel.pay_state.get()) {//组合支付

                if (payType == 2 || payTypeTwo == 2) {
                    pay(resultStr);
                } else {
                    RxToast.normal("请选择正确的支付方式-微信" + payType + ",payTypeTwo:" + payTypeTwo);
                }

            } else {
                if (payType == 2) {
                    pay(resultStr);

                } else {
                    RxToast.normal("请选择正确的支付方式-微信");
                }
            }

        } else if (MainAboutUtils.isAlipayCode(resultStr)) {//支付宝
            if (viewModel.pay_state.get()) {//组合支付
                if (payType == 3 || payTypeTwo == 3) {
                    pay(dynamic_id);
                } else {
                    RxToast.normal("请选择正确的支付方式-支付宝");
                }
            } else {
                if (payType == 3) {
                    pay(resultStr);
                } else {
                    RxToast.normal("请选择正确的支付方式-支付宝");
                }

            }
        }else if (resultStr.length()==20){
            String time=resultStr.substring(0, 10);
            String type=resultStr.substring(10, 12);
            String id=resultStr.substring(12, 20);

            KLog.i("时间戳："+ TimeUtil.getDateToString(Long.parseLong(time)*1000,"yyyy-MM-dd HH:mm:ss"));
            KLog.i("type："+Integer.parseInt(type));
            KLog.i("id："+Integer.parseInt(id));
            long timeGetTime =new Date().getTime();

            long aaa=timeGetTime-Long.parseLong(time)*1000;
            KLog.d("相差："+aaa);
            if((timeGetTime-Long.parseLong(time)*1000)>60000){
                RxToast.normal("当前会员码已过期，请刷新会员码重新扫码");
            }else {
                viewModel.user_info("",Integer.parseInt(id)+"");
            }
        }
    }

    public String getPayTypeStr(String payType) {
        if ("1".equals(payType)) {
            return "余额";
        } else if ("2".equals(payType)) {
            return "微信";
        } else if ("3".equals(payType)) {
            return "支付宝";
        } else if ("4".equals(payType)) {
            return "现金";
        } else if ("5".equals(payType)) {
            return "外卖";
        } else {
            return "其他方式";
        }

    }

    /**
     * 判断str1中包含str2的个数
     * @param str1
     * @param str2
     * @return counter
     */

    public static int countStr(String str1, String str2) {
        if (str1.indexOf(str2) == -1) {
            return 0;
        } else if (str1.indexOf(str2) != -1) {
            counter++;
            countStr(str1.substring(str1.indexOf(str2) +
                    str2.length()), str2);
            return counter;
        }
        return 0;
    }
}
