package com.youwu.shouyinsaas.ui.money.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.money.bean.RechargeBean;

import java.util.List;

/**
 * 充值记录适配器
 */
public class RechargeRecordAdapter extends RecyclerView.Adapter<RechargeRecordAdapter.myViewHodler> {
    private Context context;
    private List<RechargeBean> rechargeBeans;
    private int currentIndex = 0;

    //创建构造函数
    public RechargeRecordAdapter(Context context, List<RechargeBean> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.rechargeBeans = goodsEntityList;//实体类数据ArrayList
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = View.inflate(context, R.layout.item_recharge_record_layout, null);
        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final myViewHodler holder, @SuppressLint("RecyclerView") final int position) {

        //根据点击位置绑定数据
         RechargeBean data = rechargeBeans.get(position);
//        holder.mItemGoodsImg;
        holder.serial_number.setText((position+1)+"");//获取实体类中的name字段并设置
        holder.user_name.setText(data.getUser_name());//会员信息
        holder.pay_price.setText("￥"+data.getPay_price());//充值金额
        holder.coupon.setText(data.getCoupon());//优惠券信息
        holder.pay_type_name.setText(data.getPay_type_name());//充值方式
        holder.cashier_name.setText(data.getCashier_name());//收银员
        holder.created_at.setText(data.getCreated_at());//充值时间


    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return rechargeBeans.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

        private TextView serial_number,user_name;//编号，会员信息
        private TextView pay_price,coupon;//充值金额，获赠优惠券/张
        private TextView pay_type_name,cashier_name,created_at;//充值方式，收银员，充值时间





        public myViewHodler(View itemView) {
            super(itemView);

            serial_number = (TextView) itemView.findViewById(R.id.serial_number);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            pay_price = (TextView) itemView.findViewById(R.id.pay_price);
            coupon = (TextView) itemView.findViewById(R.id.coupon);
            pay_type_name = (TextView) itemView.findViewById(R.id.pay_type_name);
            cashier_name = (TextView) itemView.findViewById(R.id.cashier_name);
            created_at = (TextView) itemView.findViewById(R.id.created_at);



            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
                    //Toast.makeText(context,"点击了xxx",Toast.LENGTH_SHORT).show();
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, rechargeBeans.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });


        }

    }


    //删除的监听的回调
    public interface OnDeleteListener {
        void onDelete(CommunityBean lists, int position);
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mDeleteListener = listener;
    }

    private OnDeleteListener mDeleteListener;



    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        public void OnItemClick(View view, RechargeBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}