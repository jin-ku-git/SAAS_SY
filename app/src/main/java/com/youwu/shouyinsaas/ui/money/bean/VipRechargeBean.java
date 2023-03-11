package com.youwu.shouyinsaas.ui.money.bean;

import java.io.Serializable;

/**
 * 充值金额表
 */
public class VipRechargeBean implements Serializable {

    /**
     * {
     *             "activity_id": 42,
     *             "recharge_amount": 15,
     *             "recharge_name": "7元优惠券礼包+0元余额"
     *         }
     */

    private String activity_id;
    private String activity_price_id;
    private String recharge_amount;//充值金额
    private String recharge_name;//赠送内容

    public String getActivity_price_id() {
        return activity_price_id;
    }

    public void setActivity_price_id(String activity_price_id) {
        this.activity_price_id = activity_price_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(String recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public String getRecharge_name() {
        return recharge_name;
    }

    public void setRecharge_name(String recharge_name) {
        this.recharge_name = recharge_name;
    }
}
