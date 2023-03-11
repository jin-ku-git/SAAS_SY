package com.youwu.shouyinsaas.ui.order_goods.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.order_goods.bean.OrderGoodsBean;

import java.util.List;

/**
 * 退货详情适配器
 */
public class ReturnsDetailsAdapter extends RecyclerView.Adapter<ReturnsDetailsAdapter.myViewHodler> {
    private Context context;
    private List<OrderGoodsBean.DetailsBean> rechargeBeans;
    private int currentIndex = 0;

    //创建构造函数
    public ReturnsDetailsAdapter(Context context, List<OrderGoodsBean.DetailsBean> goodsEntityList) {
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
        View itemView = View.inflate(context, R.layout.item_returns_details_layout, null);
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
        final OrderGoodsBean.DetailsBean data = rechargeBeans.get(position);
//        holder.mItemGoodsImg;
        holder.goods_name.setText(data.getGoods_name());//商品名称
        holder.goods_price.setText(data.getOrder_price()+"");//商品单价
//        holder.base.setText(data.getTotal_price());//基数
        holder.SerialNumber.setText((position+1)+"");//序号
        holder.goods_number.setText(data.getOrder_quantity()+"");//订货数量

        holder.BiaoPin.setText("是");//标品
        holder.returns_number.setText(data.getReturn_order_quantity()+"");//退货数量
        holder.Remarks.setText(data.getMark()+"");
        if ((position+1)==rechargeBeans.size()){
            holder.bottom_view.setVisibility(View.VISIBLE);
        }

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

        private TextView goods_name;//商品名称
        private TextView goods_price;//订货单价
        private TextView goods_number;//订货数量
        private TextView base;//小计
        private TextView SerialNumber;//序号
        private TextView Remarks;//备注
        private TextView BiaoPin;//标品
        private TextView returns_number;//退货数量


        View bottom_view;


        public myViewHodler(View itemView) {
            super(itemView);

            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
            returns_number =  itemView.findViewById(R.id.returns_number);
            Remarks =  itemView.findViewById(R.id.Remarks);
            goods_price = (TextView) itemView.findViewById(R.id.goods_price);
            goods_number = (TextView) itemView.findViewById(R.id.goods_number);
            base = (TextView) itemView.findViewById(R.id.base);
            SerialNumber = (TextView) itemView.findViewById(R.id.SerialNumber);
            BiaoPin = (TextView) itemView.findViewById(R.id.BiaoPin);
            bottom_view=(View)itemView.findViewById(R.id.bottom_view);



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
        void onDelete(OrderGoodsBean.DetailsBean lists, int position);
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
        public void OnItemClick(View view, OrderGoodsBean.DetailsBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}