package com.youwu.shouyinsaas.ui.handover.popup;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lxj.xpopup.core.CenterPopupView;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.handover.bean.HandoverBean;
import com.youwu.shouyinsaas.ui.handover.bean.OtherMoneyList;
import com.youwu.shouyinsaas.utils_view.BigDecimalUtils;

import java.util.ArrayList;

import me.goldze.mvvmhabit.utils.KLog;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;


public class HandOverPopup extends CenterPopupView {



    Context mContext;

    EditText value_100_num;
    EditText value_50_num;
    EditText value_20_num;
    EditText value_10_num;
    EditText value_5_num;
    EditText value_1_num;
    EditText value_0_5_num;
    EditText value_0_1_num;
    TextView cash_total_money;
    String cash_total_amount;

    HandoverBean handoverBeans;

    public HandOverPopup(@NonNull Context context,HandoverBean handoverBeans) {
        super(context);
        this.mContext=context;
        this.handoverBeans=handoverBeans;

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_inventory;
    }


    @Override
    protected void onCreate() {
        super.onCreate();


        //初始化控件
        LinearLayout layout_view = (LinearLayout) findViewById(R.id.layout_view);
        TextView close_text = (TextView) findViewById(R.id.close_text);//返回
        TextView handover_text = (TextView) findViewById(R.id.handover_text);//交接班并退出
        value_100_num = (EditText) findViewById(R.id.value_100_num);
        value_50_num = (EditText) findViewById(R.id.value_50_num);
        value_20_num = (EditText) findViewById(R.id.value_20_num);
        value_10_num = (EditText) findViewById(R.id.value_10_num);
        value_5_num = (EditText) findViewById(R.id.value_5_num);
        value_1_num = (EditText) findViewById(R.id.value_1_num);
        value_0_5_num = (EditText) findViewById(R.id.value_0_5_num);
        value_0_1_num = (EditText) findViewById(R.id.value_0_1_num);
        cash_total_money = (TextView) findViewById(R.id.cash_total_money);


        value_100_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        value_50_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        value_20_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        value_10_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        value_5_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        value_1_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        value_0_5_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        value_0_1_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //交接班并退出
        handover_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(value_100_num.getText().toString())) {
                    value_100_num.setText("0");
                }
                if ("".equals(value_50_num.getText().toString())) {
                    value_50_num.setText("0");
                }
                if ("".equals(value_20_num.getText().toString())) {
                    value_20_num.setText("0");
                }
                if ("".equals(value_10_num.getText().toString())) {
                    value_10_num.setText("0");
                }
                if ("".equals(value_5_num.getText().toString())) {
                    value_5_num.setText("0");
                }
                if ("".equals(value_1_num.getText().toString())) {
                    value_1_num.setText("0");
                }
                if ("".equals(value_0_5_num.getText().toString())) {
                    value_0_5_num.setText("0");
                }
                if ("".equals(value_0_1_num.getText().toString())) {
                    value_0_1_num.setText("0");
                }

                ArrayList<OtherMoneyList> otherMoneyLists = new ArrayList<>();
                OtherMoneyList otherMoneyList = new OtherMoneyList();
                otherMoneyList.setMoney("100");
                otherMoneyList.setNumber(value_100_num.getText().toString());
                otherMoneyLists.add(otherMoneyList);


                String total_amount_list = new Gson().toJson(handoverBeans.getTotal_amount_list());
                String number_list = new Gson().toJson(handoverBeans.getNumber_list());
                String amount_list = new Gson().toJson(handoverBeans.getAmount_list());
                String other_money_list = new Gson().toJson(handoverBeans.getOther_money_list());

                String cash_amount = "[{\"money\":\"100\",\"number\":\"" + value_100_num.getText().toString() + "\"},{\"money\":\"50\",\"number\":\"" + value_50_num.getText().toString() + "\"},{\"money\":\"20\",\"number\":\"" + value_20_num.getText().toString() + "\"}" +
                        ",{\"money\":\"10\",\"number\":\"" + value_10_num.getText().toString() + "\"},{\"money\":\"5\",\"number\":\"" + value_5_num.getText().toString() + "\"},{\"money\":\"1\",\"number\":\"" + value_1_num.getText().toString() + "\"}" +
                        ",{\"money\":\"0.5\",\"number\":\"" + value_0_5_num.getText().toString() + "\"},{\"money\":\"0.1\",\"number\":\"" + value_0_1_num.getText().toString() + "\"}]";
                KLog.d("json2:" + cash_amount);


                if (mConfirmListener!=null){
                    mConfirmListener.onConfirm(total_amount_list,number_list,amount_list,other_money_list,cash_amount,cash_total_amount);
                }

            }
        });

    }

    /**
     * 计算
     */
    private void calculate(){
        Double hundred= BigDecimalUtils.multiply("100",value_100_num.getText().toString());
        Double fifty= BigDecimalUtils.multiply("50",value_50_num.getText().toString());
        Double twenty= BigDecimalUtils.multiply("20",value_20_num.getText().toString());
        Double ten= BigDecimalUtils.multiply("10",value_10_num.getText().toString());
        Double five= BigDecimalUtils.multiply("5",value_5_num.getText().toString());
        Double one= BigDecimalUtils.multiply("1",value_1_num.getText().toString());
        Double zero_five= BigDecimalUtils.multiply("0.5",value_0_5_num.getText().toString());
        Double zero_one= BigDecimalUtils.multiply("0.1",value_0_1_num.getText().toString());

        cash_total_amount=BigDecimalUtils.format((hundred+fifty+twenty+ten+five+one+zero_five+zero_one),2)+"";
        cash_total_money.setText("￥"+cash_total_amount);
    }


    //确定的回调
    public interface OnConfirmListener {
        void onConfirm(String total_amount_list,String number_list,String amount_list,String other_money_list,String cash_amount,String cash_total_amount);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        mConfirmListener = listener;
    }

    private OnConfirmListener mConfirmListener;




    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
