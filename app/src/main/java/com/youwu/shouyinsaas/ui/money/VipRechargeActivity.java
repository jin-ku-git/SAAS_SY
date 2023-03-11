package com.youwu.shouyinsaas.ui.money;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityVipRechargeBinding;
import com.youwu.shouyinsaas.jianpan.MyCustKeybords;
import com.youwu.shouyinsaas.ui.money.adapter.VipRechargeRecycleAdapter;
import com.youwu.shouyinsaas.ui.money.bean.VipRechargeBean;
import com.youwu.shouyinsaas.util.MainAboutUtils;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 会员充值页面
 * 2022/03/28
 */
public class VipRechargeActivity extends BaseActivity<ActivityVipRechargeBinding, VipRechargeViewModel> {

    //充值金额recyclerveiw的适配器
    private VipRechargeRecycleAdapter mVipRechargeRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<VipRechargeBean> rechargeList = new ArrayList<>();

    private String Recharge_price;//充值金额
    private String activity_id;//充值id
    private String activity_price_id;//活动id
    private String user_id;//用户id
    private String dynamic_id ;//扫描的二维码id

    @Override
    public void initParam() {
        super.initParam();
        user_id= getIntent().getStringExtra("user_id");
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_vip_recharge;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VipRechargeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(VipRechargeViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                switch (integer){
                    case 1://自定义充值
                        Animation topAnim = AnimationUtils.loadAnimation(
                                VipRechargeActivity.this, R.anim.activity_down_in);
                        //切换特效
                        binding.customLayout.startAnimation(topAnim);
                        break;
                        case 2://现金充值

                            Recharge("1","");

                        break;
                        case 3://返回充值列表
                            Animation topAnimTow = AnimationUtils.loadAnimation(
                                    VipRechargeActivity.this, R.anim.activity_down_out);
                            //切换特效
                            binding.customLayout.startAnimation(topAnimTow);
                        break;
                    case 4://充值成功
                        binding.customView.circleAnimation();

                        break;
                    case 5:
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });

        viewModel.arrayListObservableField.observe(this, new Observer<ArrayList<VipRechargeBean>>() {
            @Override
            public void onChanged(ArrayList<VipRechargeBean> vipRechargeBeans) {
                rechargeList=vipRechargeBeans;
                if (rechargeList.size()>0){
                    Recharge_price=rechargeList.get(0).getRecharge_amount();
                    activity_id=rechargeList.get(0).getActivity_id();
                    activity_price_id=rechargeList.get(0).getActivity_price_id();
                }
                initRecyclerViewFive();
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd");
        String time= formater.format(new Date());
//        viewModel.TOP_TITLE.set("会员充值");
        //默认没充值
        viewModel.type_state.set(1);
        //测试充值成功动画
//        binding.customView.circleAnimation();

        viewModel.custom_bool.set(false);
        viewModel.recharge_list(AppApplication.spUtils.getString("StoreId"));


        binding.customKeyboard.bindEditText(binding.moneyCz);
        disableShowSoftInput(binding.moneyCz);
        //结账
        binding.customKeyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {


                Recharge("1","");

            }
        });



    }

    /**
     * 充值
     * @param type
     */
    private void Recharge(String type,String dynamic_id) {

        if (viewModel.custom_bool.get()){
            viewModel.recharge("0","0",user_id,type,"0",viewModel.Custom_price.get(),dynamic_id);

        }else {

            viewModel.recharge(subZeroAndDot(activity_id),subZeroAndDot(activity_price_id),user_id,type,"1","0",dynamic_id);

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


    /**
     * 优惠券列表
     */
    private void initRecyclerViewFive() {
        //创建adapter
        mVipRechargeRecycleAdapter = new VipRechargeRecycleAdapter(this, rechargeList);
        //给RecyclerView设置adapter
        binding.prickRecyclerview.setAdapter(mVipRechargeRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.prickRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.prickRecyclerview.getItemDecorationCount()==0) {
            binding.prickRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mVipRechargeRecycleAdapter.setOnCouPonListener(new VipRechargeRecycleAdapter.OnCouPonListener() {
            @Override
            public void onCouPon(VipRechargeBean data, int position) {
                Recharge_price=data.getRecharge_amount();
                activity_id=data.getActivity_id();
                activity_price_id=data.getActivity_price_id();
            }
        });

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (ScanUtils.getInstance().isInputFromScanner(VipRechargeActivity.this, event)) {

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

        KLog.a("扫描信息："+resultStr);

        if (MainAboutUtils.isWXPayCode(resultStr)) {//微信
            Log.e("scanToWork", "微信:" + resultStr);

            Recharge("2",resultStr);
        } else if (MainAboutUtils.isAlipayCode(resultStr)) {//支付宝

            Recharge("3",resultStr);
        }

    }
}
