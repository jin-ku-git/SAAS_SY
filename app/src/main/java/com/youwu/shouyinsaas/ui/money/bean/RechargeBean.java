package com.youwu.shouyinsaas.ui.money.bean;

import java.io.Serializable;

public class RechargeBean implements Serializable {
    private int id;//商品id

    /**
     * order_sn : 10299911653807011
     * user_name : 哟哟哟哟哟哟(16619912724)
     * pay_price : 15
     * coupon : 满10-5/1张:满5-2/1张
     * pay_type : 1
     * pay_type_name : 微信
     * cashier_name : 收银员1
     * created_at : 2022-05-29 14:50:11
     */

    private String order_sn;
    private String user_name;
    private String pay_price;
    private String coupon;
    private int pay_type;
    private String pay_type_name;
    private String cashier_name;
    private String created_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPay_price() {
        return pay_price;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_type_name() {
        return pay_type_name;
    }

    public void setPay_type_name(String pay_type_name) {
        this.pay_type_name = pay_type_name;
    }

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
