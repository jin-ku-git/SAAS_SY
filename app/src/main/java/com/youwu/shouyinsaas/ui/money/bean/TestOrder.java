package com.youwu.shouyinsaas.ui.money.bean;

import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;

import java.util.List;

public class TestOrder {
    private List<CommunityBean> goods_list;
    private String store_id;
    private String user_id;
    private String delivery_method;
    private String tableware_number;
    private String packing_fee;
    private String mal;
    private String coupon_id;
    private String user_coupon_id;
    private String discount;
    private String dynamic_id;
    private String mark;
    private PayBean pay_list;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public List<CommunityBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<CommunityBean> goods_list) {
        this.goods_list = goods_list;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(String delivery_method) {
        this.delivery_method = delivery_method;
    }

    public String getTableware_number() {
        return tableware_number;
    }

    public void setTableware_number(String tableware_number) {
        this.tableware_number = tableware_number;
    }

    public String getPacking_fee() {
        return packing_fee;
    }

    public void setPacking_fee(String packing_fee) {
        this.packing_fee = packing_fee;
    }

    public String getMal() {
        return mal;
    }

    public void setMal(String mal) {
        this.mal = mal;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getUser_coupon_id() {
        return user_coupon_id;
    }

    public void setUser_coupon_id(String user_coupon_id) {
        this.user_coupon_id = user_coupon_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public PayBean getPay_list() {
        return pay_list;
    }

    public void setPay_list(PayBean pay_list) {
        this.pay_list = pay_list;
    }
}
