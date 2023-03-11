package com.youwu.shouyinsaas.fu_ping.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.utils_view.BigDecimalUtils;


import java.util.List;

public class RestingGoodsAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public RestingGoodsAdapter(Context mContext, @Nullable List<Object> data) {
        super(R.layout.adapter_resting_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object it) {

        CommunityBean item = (CommunityBean) it;

            helper.setText(R.id.goods_name, item.getGoods_name());
            helper.setText(R.id.goods_price, "￥" + item.getGoods_price());
            helper.setText(R.id.goods_number, item.getGoods_number() + "");
            Double preMoney = BigDecimalUtils.multiply(item.getGoods_price(), item.getGoods_number() + "");//打折之前的金额
            helper.setText(R.id.goods_all_price, "￥" + BigDecimalUtils.formatZero(preMoney, 2));
            helper.setText(R.id.discount_price, "0");


    }


}
