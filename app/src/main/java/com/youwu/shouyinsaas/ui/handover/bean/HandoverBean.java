package com.youwu.shouyinsaas.ui.handover.bean;

import java.io.Serializable;

public class HandoverBean implements Serializable {

    /**
     * recharge_amount : 0
     * total_amount_list : {"total_amount":"9.30","balance_total_amount":"4.10","wx_total_amount":"5.20","zfb_total_amount":"0.00","xj_total_amount":"0.00","wm_total_amount":"0.00"}
     * number_list : {"total_num":2,"store_num":3,"xcx_num":0,"wm_num":0}
     * amount_list : {"amount":"9.30","balance_amount":"4.10","wx_amount":"5.20","zfb_amount":"0.00","xj_amount":"0.00","wm_amount":"0.00"}
     * other_money_list : {"reduced_amount":"0.00","discount_amount":"0.00","mal":"0.20","order_amount":"0.00"}
     */

    private String recharge_amount;//今日门店充值金额
    private TotalAmountListBean total_amount_list;//今日总销售额
    private NumberListBean number_list;//今日销售单数
    private AmountListBean amount_list;//今日总实收金额
    private OtherMoneyListBean other_money_list;//其他金额

    public String getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(String recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public TotalAmountListBean getTotal_amount_list() {
        return total_amount_list;
    }

    public void setTotal_amount_list(TotalAmountListBean total_amount_list) {
        this.total_amount_list = total_amount_list;
    }

    public NumberListBean getNumber_list() {
        return number_list;
    }

    public void setNumber_list(NumberListBean number_list) {
        this.number_list = number_list;
    }

    public AmountListBean getAmount_list() {
        return amount_list;
    }

    public void setAmount_list(AmountListBean amount_list) {
        this.amount_list = amount_list;
    }

    public OtherMoneyListBean getOther_money_list() {
        return other_money_list;
    }

    public void setOther_money_list(OtherMoneyListBean other_money_list) {
        this.other_money_list = other_money_list;
    }

    public static class TotalAmountListBean {
        /**
         * total_amount : 9.30
         * balance_total_amount : 4.10
         * wx_total_amount : 5.20
         * zfb_total_amount : 0.00
         * xj_total_amount : 0.00
         * wm_total_amount : 0.00
         */

        private String total_amount;
        private String balance_total_amount;
        private String wx_total_amount;
        private String zfb_total_amount;
        private String xj_total_amount;
        private String wm_total_amount;
        private String zh_total_amount;

        public String getZh_total_amount() {
            return zh_total_amount;
        }

        public void setZh_total_amount(String zh_total_amount) {
            this.zh_total_amount = zh_total_amount;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getBalance_total_amount() {
            return balance_total_amount;
        }

        public void setBalance_total_amount(String balance_total_amount) {
            this.balance_total_amount = balance_total_amount;
        }

        public String getWx_total_amount() {
            return wx_total_amount;
        }

        public void setWx_total_amount(String wx_total_amount) {
            this.wx_total_amount = wx_total_amount;
        }

        public String getZfb_total_amount() {
            return zfb_total_amount;
        }

        public void setZfb_total_amount(String zfb_total_amount) {
            this.zfb_total_amount = zfb_total_amount;
        }

        public String getXj_total_amount() {
            return xj_total_amount;
        }

        public void setXj_total_amount(String xj_total_amount) {
            this.xj_total_amount = xj_total_amount;
        }

        public String getWm_total_amount() {
            return wm_total_amount;
        }

        public void setWm_total_amount(String wm_total_amount) {
            this.wm_total_amount = wm_total_amount;
        }
    }

    public static class NumberListBean {
        /**
         * total_num : 2
         * store_num : 3
         * xcx_num : 0
         * wm_num : 0
         */

        private String total_num;
        private String store_num;
        private String xcx_num;
        private String wm_num;

        public String getTotal_num() {
            return total_num;
        }

        public void setTotal_num(String total_num) {
            this.total_num = total_num;
        }

        public String getStore_num() {
            return store_num;
        }

        public void setStore_num(String store_num) {
            this.store_num = store_num;
        }

        public String getXcx_num() {
            return xcx_num;
        }

        public void setXcx_num(String xcx_num) {
            this.xcx_num = xcx_num;
        }

        public String getWm_num() {
            return wm_num;
        }

        public void setWm_num(String wm_num) {
            this.wm_num = wm_num;
        }
    }

    public static class AmountListBean {
        /**
         * amount : 9.30
         * balance_amount : 4.10
         * wx_amount : 5.20
         * zfb_amount : 0.00
         * xj_amount : 0.00
         * wm_amount : 0.00
         */

        private String amount;
        private String balance_amount;
        private String wx_amount;
        private String zfb_amount;
        private String xj_amount;
        private String wm_amount;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBalance_amount() {
            return balance_amount;
        }

        public void setBalance_amount(String balance_amount) {
            this.balance_amount = balance_amount;
        }

        public String getWx_amount() {
            return wx_amount;
        }

        public void setWx_amount(String wx_amount) {
            this.wx_amount = wx_amount;
        }

        public String getZfb_amount() {
            return zfb_amount;
        }

        public void setZfb_amount(String zfb_amount) {
            this.zfb_amount = zfb_amount;
        }

        public String getXj_amount() {
            return xj_amount;
        }

        public void setXj_amount(String xj_amount) {
            this.xj_amount = xj_amount;
        }

        public String getWm_amount() {
            return wm_amount;
        }

        public void setWm_amount(String wm_amount) {
            this.wm_amount = wm_amount;
        }
    }

    public static class OtherMoneyListBean {
        /**
         * reduced_amount : 0.00
         * discount_amount : 0.00
         * mal : 0.20
         * order_amount : 0.00
         */

        private String reduced_amount;
        private String discount_amount;
        private String mal;
        private String order_amount;

        public String getReduced_amount() {
            return reduced_amount;
        }

        public void setReduced_amount(String reduced_amount) {
            this.reduced_amount = reduced_amount;
        }

        public String getDiscount_amount() {
            return discount_amount;
        }

        public void setDiscount_amount(String discount_amount) {
            this.discount_amount = discount_amount;
        }

        public String getMal() {
            return mal;
        }

        public void setMal(String mal) {
            this.mal = mal;
        }

        public String getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }
    }
}
