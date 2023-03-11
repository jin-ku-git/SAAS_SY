package com.youwu.shouyinsaas.ui.money.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 销售单据表
 */
public class SaleBillBean implements Serializable {



    /**
     * delivery_method_name : 堂食
     * created_at : 2022-05-26 09:37:39
     * total_amount : 8.38
     * pay_amount : 0
     */

    private String delivery_method_name;
    private String created_at;//下单时间
    private double total_amount;//合计
    private String pay_amount;
    private String order_sn;//单据编号
    /**
     * member_name : 哟哟哟哟哟哟(16619912724)
     * shipping_type : 门店
     * delivery_method : 堂食
     * order_status : 待支付
     * mark :
     * goods_list : [{"goods_id":1123,"goods_sku":"123546","goods_name":"商品名称","goods_price":1.99,"goods_number":2,"goods_amount":3.98}]
     * goods_amount : 3.98
     * packing_fee : 1
     * total_amount : 8.38
     * reduced_amount : 0
     * mal : 0.23
     * amount : 8.38
     * pay_type : 组合支付
     */

    private String member_name;//会员昵称
    private String shipping_type;//订单类型
    private String delivery_method;//订单来源
    private String pickup_time;
    private String appointment_time;//自提时间
    private String order_status;//订单状态
    private String order_status_name;//订单状态
    private String mark;//备注
    private String pick_code;//取餐号
    private double goods_amount;//合计金额
    private double packing_fee;//打包费

    private double reduced_amount;//优惠金额
    private double mal;//抹零
    private double amount;//实收金额
    private String pay_type; //支付方式
    private int pickup_status;//配送状态，不等于2才可以核销
    private String print_time;
    private String pickup_address;//外卖地址
    private int tableware_number;//餐具数量

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public int getTableware_number() {
        return tableware_number;
    }

    public void setTableware_number(int tableware_number) {
        this.tableware_number = tableware_number;
    }

    public String getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(String pickup_address) {
        this.pickup_address = pickup_address;
    }

    public String getOrder_status_name() {
        return order_status_name;
    }

    public void setOrder_status_name(String order_status_name) {
        this.order_status_name = order_status_name;
    }

    public String getPick_code() {
        return pick_code;
    }

    public void setPick_code(String pick_code) {
        this.pick_code = pick_code;
    }

    public String getPrint_time() {
        return print_time;
    }

    public void setPrint_time(String print_time) {
        this.print_time = print_time;
    }

    public int getPickup_status() {
        return pickup_status;
    }

    public void setPickup_status(int pickup_status) {
        this.pickup_status = pickup_status;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    private ArrayList<GoodsListBean> goods_list;//商品列表

    public ArrayList<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(ArrayList<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }



    public String getDelivery_method_name() {
        return delivery_method_name;
    }

    public void setDelivery_method_name(String delivery_method_name) {
        this.delivery_method_name = delivery_method_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getShipping_type() {
        return shipping_type;
    }

    public void setShipping_type(String shipping_type) {
        this.shipping_type = shipping_type;
    }

    public String getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(String delivery_method) {
        this.delivery_method = delivery_method;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public double getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(double goods_amount) {
        this.goods_amount = goods_amount;
    }

    public double getPacking_fee() {
        return packing_fee;
    }

    public void setPacking_fee(double packing_fee) {
        this.packing_fee = packing_fee;
    }

    public double getReduced_amount() {
        return reduced_amount;
    }

    public void setReduced_amount(double reduced_amount) {
        this.reduced_amount = reduced_amount;
    }

    public double getMal() {
        return mal;
    }

    public void setMal(double mal) {
        this.mal = mal;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }



    public static class GoodsListBean {
        /**
         * goods_id : 1123
         * goods_sku : 123546
         * goods_name : 商品名称
         * goods_price : 1.99
         * goods_number : 2
         * goods_amount : 3.98
         */

        private int goods_id;
        private String goods_sku;
        private String goods_name;
        private double goods_price;
        private int goods_number;
        private double goods_amount;

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_sku() {
            return goods_sku;
        }

        public void setGoods_sku(String goods_sku) {
            this.goods_sku = goods_sku;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public double getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(double goods_price) {
            this.goods_price = goods_price;
        }

        public int getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(int goods_number) {
            this.goods_number = goods_number;
        }

        public double getGoods_amount() {
            return goods_amount;
        }

        public void setGoods_amount(double goods_amount) {
            this.goods_amount = goods_amount;
        }
    }
}
