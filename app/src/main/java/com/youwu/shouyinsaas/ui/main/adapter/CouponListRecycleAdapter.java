package com.youwu.shouyinsaas.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.main.bean.CouponBean;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class CouponListRecycleAdapter extends RecyclerView.Adapter<CouponListRecycleAdapter.myViewHodler> {
    private Context context;
    private List<CouponBean> goodsEntityList;
    private int currentIndex = 0;

    //创建构造函数
    public CouponListRecycleAdapter(Context context, List<CouponBean> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.goodsEntityList = goodsEntityList;//实体类数据ArrayList
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
        View itemView = View.inflate(context, R.layout.item_counpon_layout, null);
        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(myViewHodler holder, @SuppressLint("RecyclerView") final int position) {

        //根据点击位置绑定数据
        final CouponBean data = goodsEntityList.get(position);
//        holder.bindData(goodsEntityList.get(position), position, currentIndex);

        holder.cou_name.setText(data.getCoupon_name());//获取实体类中的name字段并设置
        holder.cou_price.setText(data.getReduce_price()+"");
        holder.cou_time.setText("使用时间："+data.getStart_time()+"~"+data.getEnd_time());
        holder.img_choice.setVisibility(data.isSelect()?View.VISIBLE:View.GONE);



        if (data.getCoupon_type()==1){
            holder.FullMinus.setVisibility(View.VISIBLE);
            holder.discountLayout.setVisibility(View.GONE);
        }else if (data.getCoupon_type()==2){
            holder.FullMinus.setVisibility(View.GONE);
            holder.discountLayout.setVisibility(View.VISIBLE);
            holder.reduction_name.setText(data.getIs_reduction_name());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setCurrentIndex(position);

                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long end = 0L;
                Long start = 0L;
                try {
                    end = sf.parse(data.getEnd_time()).getTime();// 日期转换为时间戳
                    start = sf.parse(data.getStart_time()).getTime();// 日期转换为时间戳
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() > end) {
                    RxToast.normal("优惠券已经过期！");
                    return;
                } else if (System.currentTimeMillis() < start) {
                    RxToast.normal("优惠券活动还未开始！");
                    return;
                }

                CouponBean item = goodsEntityList.get(position);
                if (item.isSelect()) {
                    item.setSelect(false);
                } else {
                    for (int i = 0; i < goodsEntityList.size(); i++) {
                        goodsEntityList.get(i).setSelect(false);
                    }
                    item.setSelect(true);
                }
                notifyDataSetChanged();

                /**
                 * 选择的优惠券
                 */
                if (mCouPonListener!=null){
                    if (item.isSelect()){
                        mCouPonListener.onCouPon(data,position);
                    }else {
                        mCouPonListener.onCouPon(null,position);
                    }

                }

            }
        });

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
        return goodsEntityList.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

        private TextView cou_name,cou_price;//商品名称,商品价格
        private TextView cou_time;//使用时间
        private ImageView img_choice;//
        private TextView reduction_name;
        private LinearLayout FullMinus;//满减券
        private LinearLayout discountLayout;//折扣券


        public myViewHodler(View itemView) {
            super(itemView);

            cou_name = (TextView) itemView.findViewById(R.id.cou_name);
            cou_price = (TextView) itemView.findViewById(R.id.cou_price);
            cou_time = (TextView) itemView.findViewById(R.id.cou_time);
            img_choice = (ImageView) itemView.findViewById(R.id.img_choice);
            FullMinus =itemView.findViewById(R.id.FullMinus);
            discountLayout =itemView.findViewById(R.id.discountLayout);
            reduction_name =itemView.findViewById(R.id.reduction_name);

            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
                    //Toast.makeText(context,"点击了xxx",Toast.LENGTH_SHORT).show();
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, goodsEntityList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });


        }
        public void bindData(CouponBean data, int position, int currentIndex) {
            if (position == currentIndex) {
                img_choice.setVisibility(View.VISIBLE);
            } else {
                img_choice.setVisibility(View.INVISIBLE);
            }
        }
    }

    //图片的监听的回调
    public interface OnCouPonListener {
        void onCouPon(CouponBean data, int position);
    }

    public void setOnCouPonListener(OnCouPonListener listener) {
        mCouPonListener = listener;
    }

    private OnCouPonListener mCouPonListener;



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
        public void OnItemClick(View view, CouponBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}