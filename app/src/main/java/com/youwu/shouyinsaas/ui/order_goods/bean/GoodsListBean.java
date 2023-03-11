package com.youwu.shouyinsaas.ui.order_goods.bean;

import java.io.Serializable;
import java.util.List;

public class GoodsListBean implements Serializable {

    /**
     * order_sn : 419148415327410384
     * arrival_date : 2022-05-30
     * total_price : 3.22
     * number : 1
     * item_list : [{"item_id":401367210750252765,"item_sku":"401367211329065171:401367211710747972","item_name":"手抓1长箱","price":1.5,"moq":"1.00","self_num":1,"addition_num":0}]
     */

    private String order_sn;
    private String arrival_date;
    private double total_price;
    private int number;
    private List<ItemListBean> item_list;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<ItemListBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemListBean> item_list) {
        this.item_list = item_list;
    }

    public static class ItemListBean {
        /**
         * item_id : 401367210750252765
         * item_sku : 401367211329065171:401367211710747972
         * item_name : 手抓1长箱
         * price : 1.5
         * moq : 1.00
         * self_num : 1
         * addition_num : 0
         */

        private long item_id;
        private String item_sku;
        private String item_name;
        private double price;
        private String moq;
        private int self_num;
        private int addition_num;
        private int goods_type;

        private int is_standard;//1 标品 2 非标品
        private String sku_name;

        private String qt_num;//退货数量
        private int gs_num;
        private int sc_num;
        private int zl_num;
        private int zx_num;
        private String remarks;//备注

        public String getSku_name() {
            return sku_name;
        }

        public void setSku_name(String sku_name) {
            this.sku_name = sku_name;
        }

        public int getIs_standard() {
            return is_standard;
        }

        public void setIs_standard(int is_standard) {
            this.is_standard = is_standard;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getQt_num() {
            return qt_num;
        }

        public void setQt_num(String qt_num) {
            this.qt_num = qt_num;
        }

        public int getGs_num() {
            return gs_num;
        }

        public void setGs_num(int gs_num) {
            this.gs_num = gs_num;
        }

        public int getSc_num() {
            return sc_num;
        }

        public void setSc_num(int sc_num) {
            this.sc_num = sc_num;
        }

        public int getZl_num() {
            return zl_num;
        }

        public void setZl_num(int zl_num) {
            this.zl_num = zl_num;
        }

        public int getZx_num() {
            return zx_num;
        }

        public void setZx_num(int zx_num) {
            this.zx_num = zx_num;
        }

        public int getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(int goods_type) {
            this.goods_type = goods_type;
        }

        public long getItem_id() {
            return item_id;
        }

        public void setItem_id(long item_id) {
            this.item_id = item_id;
        }

        public String getItem_sku() {
            return item_sku;
        }

        public void setItem_sku(String item_sku) {
            this.item_sku = item_sku;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getMoq() {
            return moq;
        }

        public void setMoq(String moq) {
            this.moq = moq;
        }

        public int getSelf_num() {
            return self_num;
        }

        public void setSelf_num(int self_num) {
            this.self_num = self_num;
        }

        public int getAddition_num() {
            return addition_num;
        }

        public void setAddition_num(int addition_num) {
            this.addition_num = addition_num;
        }
    }
}
