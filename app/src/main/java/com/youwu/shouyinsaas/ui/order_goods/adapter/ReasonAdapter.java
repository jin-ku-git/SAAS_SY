package com.youwu.shouyinsaas.ui.order_goods.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.order_goods.bean.ReasonBean;
import com.youwu.shouyinsaas.utils_view.BigDecimalUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * 原因适配器
 */
public class ReasonAdapter extends RecyclerView.Adapter<ReasonAdapter.MyViewHolder> {
    private Context context;

    private List<ReasonBean> list;

    private int etFocisPosition;


    public ReasonAdapter(Context context, List<ReasonBean> list) {
        this.context = context;
        this.list = list;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_reason, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ReasonBean  reasonBean = list.get(position);

        holder.number_text.setText(BigDecimalUtils.formatZero(reasonBean.getNumber(),0));
        holder.NameText.setText(reasonBean.getName());

        if ((position+1)==list.size()){
            holder.bottom_view.setVisibility(View.VISIBLE);
        }

        //添加editText的监听事件
        holder.number_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {

                    reasonBean.setNumber(s.toString());
                    list.get(position).setNumber(s.toString());

            }
        });

    }

    public void determine() {

        EventBus.getDefault().post(list);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView NameText;
        View bottom_view;
        EditText number_text;//输入的数量
        //因为删除有可能会删除中间条目，然后会造成角标越界，所以必须整体刷新一下！
        public MyViewHolder(View view) {
            super(view);

            NameText=(TextView)view.findViewById(R.id.NameText);
            number_text=(EditText)view.findViewById(R.id.number_text);
            bottom_view=(View)view.findViewById(R.id.bottom_view);


        }
    }





    //扫描的回调
    public interface OnScanningListener {
        void onScanning(int position);
    }

    public void setOnScanningListener(OnScanningListener listener) {
        mScanningListener = listener;
    }

    private OnScanningListener mScanningListener;


}