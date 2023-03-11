package com.youwu.shouyinsaas.ui.money.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.money.bean.SaleBillBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 退款凭证适配器
 */
public class RefundImageAdapter extends RecyclerView.Adapter<RefundImageAdapter.myViewHodler> {
    private Context context;
    private ArrayList<SaleBillBean.GoodsListBean> rechargeBeans;
    private int currentIndex = 0;

    //创建构造函数
    public RefundImageAdapter(Context context, ArrayList<SaleBillBean.GoodsListBean> goodsEntityList) {
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
        View itemView = View.inflate(context, R.layout.item_refund_image_layout, null);
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
        SaleBillBean.GoodsListBean data = rechargeBeans.get(position);
//        holder.mItemGoodsImg;

        String image="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242332225H9-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661745628&t=57cbd66472ad062f85381d3ad527d5cc";

        Glide.with(context).load(image).into(holder.refund_image);

        final List<Object> list=new ArrayList<>();

        for (int i=0;i<rechargeBeans.size();i++){
            list.add(image);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  XPopup.Builder(context).asImageViewer(holder.refund_image, position, list, new OnSrcViewUpdateListener() {
                    @Override
                    public void onSrcViewUpdate(@NonNull ImageViewerPopupView popupView, int position) {

                        popupView.updateSrcView((ImageView)holder.refund_image );
                    }
                },new ImageLoader())
                        .show();
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
        return rechargeBeans.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

        private ImageView refund_image;

        public myViewHodler(View itemView) {
            super(itemView);

            refund_image = (ImageView) itemView.findViewById(R.id.refund_image);





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
        void onDelete(SaleBillBean.GoodsListBean lists, int position);
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
        public void OnItemClick(View view, SaleBillBean.GoodsListBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // 图片加载器，我不负责加载图片，需要你实现一个图片加载器传给我，这里以 Glide 为例。
    class ImageLoader implements XPopupImageLoader {

        @Override
        public void loadImage(int position, @NonNull Object uri, @NonNull ImageView imageView) {
            Glide.with(imageView).load(uri).into(imageView);
        }

        //必须实现这个方法，返回 uri 对应的缓存文件，可参照下面的实现，内部保存图片会用到。
        @Override
        public File getImageFile(@NonNull Context context, @NonNull Object uri) {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}