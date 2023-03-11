package com.youwu.shouyinsaas.ui.order_goods.goods.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.order_goods.goods.bean.GoodsGroupBean;

import java.util.List;

/**
 * 商品群组适配器
 */
public class GoodsGroupAdapter extends RecyclerView.Adapter<GoodsGroupAdapter.myViewHodler> {
    private Context context;
    private List<GoodsGroupBean> groupBeans;
    private int currentIndex = 0;

    //创建构造函数
    public GoodsGroupAdapter(Context context, List<GoodsGroupBean> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.groupBeans = goodsEntityList;//实体类数据ArrayList
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
        View itemView = View.inflate(context, R.layout.item_goods_group_layout, null);
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
        final GoodsGroupBean data = groupBeans.get(position);
//        holder.mItemGoodsImg;
        holder.GroupName.setText(data.getGroupName());//群组名称
        holder.spinner.setItems(data.getSpinnerList());

        if(groupBeans.size()%2==0) { //用x除以2看他的余数是否为0，为0则是偶数，不为零则为奇数
            if ((position+1)==groupBeans.size()||(position+2)==groupBeans.size()){
                holder.bottom_layout.setVisibility(View.GONE);
            }
        }else {
            if ((position+1)==groupBeans.size()){
                holder.bottom_layout.setVisibility(View.GONE);
            }
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
        return groupBeans.size();
    }



    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

        private TextView GroupName;//群组名称
        private MaterialSpinner spinner;


        View bottom_layout;



        public myViewHodler(View itemView) {
            super(itemView);

            GroupName = (TextView) itemView.findViewById(R.id.GroupName);
            spinner =  itemView.findViewById(R.id.spinner);
            bottom_layout =  itemView.findViewById(R.id.bottom_layout);




            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
                    //Toast.makeText(context,"点击了xxx",Toast.LENGTH_SHORT).show();
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, groupBeans.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });


        }

    }


    //监听的回调
    public interface OnDeleteListener {
        void onDelete(GoodsGroupBean lists, int position);
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
        public void OnItemClick(View view, GoodsGroupBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}