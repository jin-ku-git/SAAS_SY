package com.youwu.shouyinsaas.ui.set_up.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunmi.devicemanager.cons.Cons;
import com.sunmi.devicemanager.device.Device;
import com.youwu.shouyinsaas.R;


import java.util.List;

/**
 * Created by bps .
 */
public class PrinterAdapter extends RecyclerView.Adapter<PrinterAdapter.MyHolder> {

    private static final int NORMAL = 0;
    private static final int HEADER = 1;

    private Context mContext;
    private OnItemClickListener mListener;
    private List<Device> mList;


    public PrinterAdapter(Context context, List<Device> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == NORMAL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_printer_normal, viewGroup, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_printer_header, viewGroup, false);
        }
        return new MyHolder(view, i);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {
        if (getItemViewType(position) == HEADER) {
            return;
        }
        Device device = mList.get(position - 1);
        myHolder.mName.setText(device.getNickName());
        if (Cons.TRUE.equals(device.getAttr(Cons.STATE))) {
            myHolder.mConnect.setText(mContext.getString(R.string.more_bracelet_conncet));
            myHolder.mConnect.setTextColor(0xff7ED321);
        } else {
            myHolder.mConnect.setText(mContext.getString(R.string.more_bracelet_unconncet));
            myHolder.mConnect.setTextColor(0xffD2D2D2);
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        }
        return NORMAL;
    }

    @Override
    public int getItemCount() {
        return (mList == null || mList.isEmpty()) ? 1 : mList.size() + 1;
    }

    public void replaceData(List<Device> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mName;
        private TextView mConnect;

        MyHolder(@NonNull View itemView, int type) {
            super(itemView);
            itemView.setOnClickListener(this);
            mName = itemView.findViewById(R.id.printer_name);
            if (type == HEADER) return;
            mConnect = itemView.findViewById(R.id.printer_connect_status);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mListener != null && position != RecyclerView.NO_POSITION) {
                mListener.onClick(itemView, position);
            }
        }
    }
}
