package com.youwu.shouyinsaas.ui.main.popup;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.lxj.xpopup.core.CenterPopupView;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.money.RechargeRecordActivity;
import com.youwu.shouyinsaas.ui.money.SalesOverviewActivity;
import com.youwu.shouyinsaas.ui.money.WriteOffActivity;
import com.youwu.shouyinsaas.ui.order_goods.SellOutActivity;
import com.youwu.shouyinsaas.ui.order_goods.StocktakingActivity;
import com.youwu.shouyinsaas.ui.order_goods.goods.AddGoodsActivity;
import com.youwu.shouyinsaas.ui.order_goods.goods.AddSetMealActivity;
import com.youwu.shouyinsaas.ui.set_up.SetUpActivity;
import com.youwu.shouyinsaas.ui.set_up.SettingsPrintActivity;
import com.youwu.shouyinsaas.utils_view.RxToast;


import static com.blankj.utilcode.util.ActivityUtils.startActivity;


public class FunctionDialog extends CenterPopupView {



    Context mContext;


    public FunctionDialog(@NonNull Context context) {
        super(context);
        this.mContext=context;

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_function;
    }


    @Override
    protected void onCreate() {
        super.onCreate();


        //初始化控件
        TextView close_text = (TextView) findViewById(R.id.close_text);
        final Button button_1 = (Button) findViewById(R.id.button_1);
        final Button button_2 = (Button) findViewById(R.id.button_2);
        final Button button_3 = (Button) findViewById(R.id.button_3);
        final Button button_4 = (Button) findViewById(R.id.button_4);
        final Button button_5 = (Button) findViewById(R.id.button_5);
        final Button button_6 = (Button) findViewById(R.id.button_6);
        final Button button_7 = (Button) findViewById(R.id.button_7);
        final Button button_8 = (Button) findViewById(R.id.button_8);
        final Button button_9 = (Button) findViewById(R.id.button_9);
        final Button button_10 = (Button) findViewById(R.id.button_10);
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //门店充值记录
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(RechargeRecordActivity.class);

            }
        });
        //设置
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //跳转到设置
                startActivity(SetUpActivity.class);
//                RxToast.normal("设置");
            }
        });
        //盘点
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //跳转到盘点
                startActivity(StocktakingActivity.class);

            }
        });
        //核销
        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //跳转到核销
                startActivity(WriteOffActivity.class);

            }
        });
        //沽清
        button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //跳转到沽清
                startActivity(SellOutActivity.class);
            }
        });
        //销售概况
        button_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(SalesOverviewActivity.class);
            }
        });
        //打印设置
        button_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(SettingsPrintActivity.class);
            }
        });
        //添加商品
        button_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(AddGoodsActivity.class);
            }
        });

        //添加套餐
        button_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(AddSetMealActivity.class);
            }
        });        //敬请期待
        button_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.normal("敬请期待");
            }
        });



    }


    //确定的回调
    public interface OnHeatListener {
        void onHeat(Integer bean);
    }

    public void setOnBeanListener(OnHeatListener listener) {
        mHeatListener = listener;
    }

    private OnHeatListener mHeatListener;




    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
