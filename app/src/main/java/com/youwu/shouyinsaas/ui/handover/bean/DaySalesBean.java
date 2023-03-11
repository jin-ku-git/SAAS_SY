package com.youwu.shouyinsaas.ui.handover.bean;

import java.io.Serializable;

public class DaySalesBean implements Serializable {

    /**
     * total_amount : 0
     * pay_amount : 0
     * total_amount_list : {"balance_total_amount":"0.00","wx_total_amount":"0.00","zfb_total_amount":"0.00","xj_total_amount":"0.00","wm_total_amount":"0.00","zh_total_amount":"0.00"}
     * amount_list : {"balance_amount":"0.00","wx_amount":"0.00","zfb_amount":"0.00","xj_amount":"0.00","wm_amount":"0.00"}
     */

    private String total_amount;
    private String pay_amount;
    private TotalAmountListBean total_amount_list;
    private AmountListBean amount_list;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public TotalAmountListBean getTotal_amount_list() {
        return total_amount_list;
    }

    public void setTotal_amount_list(TotalAmountListBean total_amount_list) {
        this.total_amount_list = total_amount_list;
    }

    public AmountListBean getAmount_list() {
        return amount_list;
    }

    public void setAmount_list(AmountListBean amount_list) {
        this.amount_list = amount_list;
    }

    public static class TotalAmountListBean {
        /**
         * balance_total_amount : 0.00
         * wx_total_amount : 0.00
         * zfb_total_amount : 0.00
         * xj_total_amount : 0.00
         * wm_total_amount : 0.00
         * zh_total_amount : 0.00
         */

        private String balance_total_amount;
        private String wx_total_amount;
        private String zfb_total_amount;
        private String xj_total_amount;
        private String wm_total_amount;
        private String zh_total_amount;

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

        public String getZh_total_amount() {
            return zh_total_amount;
        }

        public void setZh_total_amount(String zh_total_amount) {
            this.zh_total_amount = zh_total_amount;
        }
    }

    public static class AmountListBean {
        /**
         * balance_amount : 0.00
         * wx_amount : 0.00
         * zfb_amount : 0.00
         * xj_amount : 0.00
         * wm_amount : 0.00
         */

        private String balance_amount;
        private String wx_amount;
        private String zfb_amount;
        private String xj_amount;
        private String wm_amount;

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
}
