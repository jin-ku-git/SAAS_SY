package com.youwu.shouyinsaas.ui.money.bean;

import java.io.Serializable;
import java.util.List;

public class PayBean {
    private String type;//支付方式：1余额、2微信、3支付宝、4现金、5外卖
    private String money;//金额
    private List<Details> details;//

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public static class Details implements Serializable {
        private String type;//支付方式：1余额、2微信、3支付宝、4现金、5外卖
        private String money;//金额

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
