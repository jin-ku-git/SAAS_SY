package com.youwu.shouyinsaas.ui.main.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 小程序订单
 */
public class WMOrderBean implements Serializable {
    private int id;//单据id
    private String order_sn;//单据编号
    private String order_state;//单据状态
    private int order_state_num;//单据状态
    private String paid_in_prick;//实收价格
    private String goods_num;//商品数量
    private String discount_prick;//优惠价格
    private String wipe_zero;//抹零价格
    private String receivable_price;//应收价格
    private String remarks_content;//备注
    private String cashier_name;//收银员
    private String create_time;//创建时间
    private String vip_name;//会员名称
    private String pay_mode;//支付方式
    private String RefundPrice;//退款金额
    private int order_status;//状态 0 待接单 1 待出餐 2 退款
    private ArrayList<CommunityBean> goods_list;//商品列表


    public String getRefundPrice() {
        return RefundPrice;
    }

    public void setRefundPrice(String refundPrice) {
        RefundPrice = refundPrice;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public int getOrder_state_num() {
        return order_state_num;
    }

    public void setOrder_state_num(int order_state_num) {
        this.order_state_num = order_state_num;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public String getDiscount_prick() {
        return discount_prick;
    }

    public void setDiscount_prick(String discount_prick) {
        this.discount_prick = discount_prick;
    }

    public String getWipe_zero() {
        return wipe_zero;
    }

    public void setWipe_zero(String wipe_zero) {
        this.wipe_zero = wipe_zero;
    }

    public String getReceivable_price() {
        return receivable_price;
    }

    public void setReceivable_price(String receivable_price) {
        this.receivable_price = receivable_price;
    }

    public String getRemarks_content() {
        return remarks_content;
    }

    public void setRemarks_content(String remarks_content) {
        this.remarks_content = remarks_content;
    }

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getVip_name() {
        return vip_name;
    }

    public void setVip_name(String vip_name) {
        this.vip_name = vip_name;
    }


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

    public String getPaid_in_prick() {
        return paid_in_prick;
    }

    public void setPaid_in_prick(String paid_in_prick) {
        this.paid_in_prick = paid_in_prick;
    }

    public ArrayList<CommunityBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(ArrayList<CommunityBean> goods_list) {
        this.goods_list = goods_list;
    }
}
