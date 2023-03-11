package com.youwu.shouyinsaas.ui.order_goods.bean;

import java.io.Serializable;
import java.util.List;

public class ReturnsDetailsBean implements Serializable {

    /**
     * return_order_sn : 419194602969504390
     * order_sn : 419182764907174480
     * return_order_date : 2022-05-31 18:09:41
     * total_money : 3
     * total_num : 2
     * status : 3
     * status_name : 待审核
     * item : [{"item_id":401367210750252740,"item_name":"手抓1","item_sku":"401367211329065171:401367211710747972","sku_name":"长:箱","order_price":1.5,"zx_money":0,"gs_money":0,"sc_money":0,"zl_money":0,"qt_money":3,"sum_money":3,"self_num":2,"zx_num":0,"gs_num":0,"sc_num":0,"zl_num":0,"qt_num":"2","sum_num":2,"mark":"","is_standard":1,"is_standard_name":"标品"}]
     */

    private long return_order_sn;
    private String order_sn;
    private String return_order_date;
    private int total_money;
    private int total_num;
    private int status;
    private String status_name;
    private List<ItemBean> item;

    public long getReturn_order_sn() {
        return return_order_sn;
    }

    public void setReturn_order_sn(long return_order_sn) {
        this.return_order_sn = return_order_sn;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getReturn_order_date() {
        return return_order_date;
    }

    public void setReturn_order_date(String return_order_date) {
        this.return_order_date = return_order_date;
    }

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * item_id : 401367210750252740
         * item_name : 手抓1
         * item_sku : 401367211329065171:401367211710747972
         * sku_name : 长:箱
         * order_price : 1.5
         * zx_money : 0
         * gs_money : 0
         * sc_money : 0
         * zl_money : 0
         * qt_money : 3
         * sum_money : 3
         * self_num : 2
         * zx_num : 0
         * gs_num : 0
         * sc_num : 0
         * zl_num : 0
         * qt_num : 2
         * sum_num : 2
         * mark :
         * is_standard : 1
         * is_standard_name : 标品
         */

        private long item_id;
        private String item_name;
        private String item_sku;
        private String sku_name;
        private double order_price;
        private int zx_money;
        private int gs_money;
        private int sc_money;
        private int zl_money;
        private int qt_money;
        private int sum_money;
        private int self_num;
        private int zx_num;
        private int gs_num;
        private int sc_num;
        private int zl_num;
        private String qt_num;
        private int sum_num;
        private String mark;
        private int is_standard;
        private String is_standard_name;

        public long getItem_id() {
            return item_id;
        }

        public void setItem_id(long item_id) {
            this.item_id = item_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getItem_sku() {
            return item_sku;
        }

        public void setItem_sku(String item_sku) {
            this.item_sku = item_sku;
        }

        public String getSku_name() {
            return sku_name;
        }

        public void setSku_name(String sku_name) {
            this.sku_name = sku_name;
        }

        public double getOrder_price() {
            return order_price;
        }

        public void setOrder_price(double order_price) {
            this.order_price = order_price;
        }

        public int getZx_money() {
            return zx_money;
        }

        public void setZx_money(int zx_money) {
            this.zx_money = zx_money;
        }

        public int getGs_money() {
            return gs_money;
        }

        public void setGs_money(int gs_money) {
            this.gs_money = gs_money;
        }

        public int getSc_money() {
            return sc_money;
        }

        public void setSc_money(int sc_money) {
            this.sc_money = sc_money;
        }

        public int getZl_money() {
            return zl_money;
        }

        public void setZl_money(int zl_money) {
            this.zl_money = zl_money;
        }

        public int getQt_money() {
            return qt_money;
        }

        public void setQt_money(int qt_money) {
            this.qt_money = qt_money;
        }

        public int getSum_money() {
            return sum_money;
        }

        public void setSum_money(int sum_money) {
            this.sum_money = sum_money;
        }

        public int getSelf_num() {
            return self_num;
        }

        public void setSelf_num(int self_num) {
            this.self_num = self_num;
        }

        public int getZx_num() {
            return zx_num;
        }

        public void setZx_num(int zx_num) {
            this.zx_num = zx_num;
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

        public String getQt_num() {
            return qt_num;
        }

        public void setQt_num(String qt_num) {
            this.qt_num = qt_num;
        }

        public int getSum_num() {
            return sum_num;
        }

        public void setSum_num(int sum_num) {
            this.sum_num = sum_num;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public int getIs_standard() {
            return is_standard;
        }

        public void setIs_standard(int is_standard) {
            this.is_standard = is_standard;
        }

        public String getIs_standard_name() {
            return is_standard_name;
        }

        public void setIs_standard_name(String is_standard_name) {
            this.is_standard_name = is_standard_name;
        }
    }
}
