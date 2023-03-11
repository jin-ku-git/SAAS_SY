package com.youwu.shouyinsaas.ui.main.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.lxj.xpopup.core.CenterPopupView;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.utils_view.RightMarkView;

/**
 * 提示弹窗
 */

public class TipsDialog extends CenterPopupView {



    Context mContext;
    String mContent;//提示内容
    int type;//1 确定 2 确定、取消
    private String oneText,TwoText;//取消按钮文字、确定按钮文字


    TextView tvContent;//
    TextView cancel;//取消
    TextView confirm_text;//确认

    private RightMarkView activity_right_mark_rmv;

    public TipsDialog(@NonNull Context context, String content) {
        super(context);
        this.mContext=context;
        this.mContent=content;
    }
    /**
     * 设置显示确定还是显示确定、取消
     */
    public void setType(int type) {
        this.type = type;
    }
    /**
     * 设置取消按钮文字
     */
    public void setOneText(String oneText) {
        this.oneText = oneText;
    }
    /**
     * 设置确定按钮文字
     */
    public void setTwoText(String TwoText) {
        this.TwoText = TwoText;
    }
    /**
     * 关闭弹窗
     */
    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_tips;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        tvContent=findViewById(R.id.tv_content);
        activity_right_mark_rmv=findViewById(R.id.activity_right_mark_rmv);
        cancel=findViewById(R.id.cancel);
        confirm_text=findViewById(R.id.confirm_text);
        if (type==1){
            cancel.setVisibility(GONE);
        }else if (type==2){
            confirm_text.setVisibility(GONE);
        }
        if (mContent!=null){
            tvContent.setText(mContent);
        }
        if (oneText!=null){
            cancel.setText(oneText);
        }
        if (TwoText!=null){
            confirm_text.setText(TwoText);
        }


        // 设置开始和结束两种颜色
        activity_right_mark_rmv.setColor(getResources().getColor(R.color.color_blue), getResources().getColor(R.color.color_blue));
        // 设置画笔粗细
        activity_right_mark_rmv.setStrokeWidth(5f);

        activity_right_mark_rmv.startAnimator();
        //取消按钮点击事件
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeatListener!=null){
                    mHeatListener.onHeat(1);
                }
            }
        });
        //确定按钮点击事件
        confirm_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeatListener!=null){
                    mHeatListener.onHeat(2);
                }
            }
        });

    }



    //确定的回调
    public interface OnHeatListener {
        void onHeat(int type);
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
