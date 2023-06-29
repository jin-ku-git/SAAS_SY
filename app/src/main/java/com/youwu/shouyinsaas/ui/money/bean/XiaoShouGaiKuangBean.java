package com.youwu.shouyinsaas.ui.money.bean;

import java.io.Serializable;

/**
 * 销售概况Bean
 */

public class XiaoShouGaiKuangBean implements Serializable {


    /**
     * order : {"total_amount":591.2999999999998,"total_pay_amount":568.06,"total_orders":41,"day_pay_amount":81.15,"day_customer_price":13.86,"refund_order_number":5}
     * breakfast : {"total_amount":154.7,"total_pay_amount":141.95999999999998,"total_orders":9,"day_pay_amount":25.78,"day_customer_price":15.77,"refund_order_number":2}
     * lunch : {"total_amount":65.7,"total_pay_amount":63.2,"total_orders":6,"day_pay_amount":10.95,"day_customer_price":10.53,"refund_order_number":1}
     * dinner : {"total_amount":370.9,"total_pay_amount":362.9,"total_orders":26,"day_pay_amount":61.82,"day_customer_price":13.96,"refund_order_number":2}
     */

    private OrderBean order;
    private BreakfastBean breakfast;
    private LunchBean lunch;
    private DinnerBean dinner;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public BreakfastBean getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(BreakfastBean breakfast) {
        this.breakfast = breakfast;
    }

    public LunchBean getLunch() {
        return lunch;
    }

    public void setLunch(LunchBean lunch) {
        this.lunch = lunch;
    }

    public DinnerBean getDinner() {
        return dinner;
    }

    public void setDinner(DinnerBean dinner) {
        this.dinner = dinner;
    }

    public static class OrderBean implements Serializable {
        /**
         * total_amount : 591.2999999999998
         * total_pay_amount : 568.06
         * total_orders : 41
         * day_pay_amount : 81.15
         * day_customer_price : 13.86
         * refund_order_number : 5
         */

        private double total_amount=0;
        private double total_pay_amount=0;
        private int total_orders=0;
        private double day_pay_amount=0;
        private double day_customer_price=0;
        private int refund_order_number=0;

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getTotal_pay_amount() {
            return total_pay_amount;
        }

        public void setTotal_pay_amount(double total_pay_amount) {
            this.total_pay_amount = total_pay_amount;
        }

        public int getTotal_orders() {
            return total_orders;
        }

        public void setTotal_orders(int total_orders) {
            this.total_orders = total_orders;
        }

        public double getDay_pay_amount() {
            return day_pay_amount;
        }

        public void setDay_pay_amount(double day_pay_amount) {
            this.day_pay_amount = day_pay_amount;
        }

        public double getDay_customer_price() {
            return day_customer_price;
        }

        public void setDay_customer_price(double day_customer_price) {
            this.day_customer_price = day_customer_price;
        }

        public int getRefund_order_number() {
            return refund_order_number;
        }

        public void setRefund_order_number(int refund_order_number) {
            this.refund_order_number = refund_order_number;
        }
    }


    public static class BreakfastBean implements Serializable {
        /**
         * total_amount : 154.7
         * total_pay_amount : 141.95999999999998
         * total_orders : 9
         * day_pay_amount : 25.78
         * day_customer_price : 15.77
         * refund_order_number : 2
         */

        private double total_amount;
        private double total_pay_amount;
        private int total_orders;
        private double day_pay_amount;
        private double day_customer_price;
        private int refund_order_number;

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getTotal_pay_amount() {
            return total_pay_amount;
        }

        public void setTotal_pay_amount(double total_pay_amount) {
            this.total_pay_amount = total_pay_amount;
        }

        public int getTotal_orders() {
            return total_orders;
        }

        public void setTotal_orders(int total_orders) {
            this.total_orders = total_orders;
        }

        public double getDay_pay_amount() {
            return day_pay_amount;
        }

        public void setDay_pay_amount(double day_pay_amount) {
            this.day_pay_amount = day_pay_amount;
        }

        public double getDay_customer_price() {
            return day_customer_price;
        }

        public void setDay_customer_price(double day_customer_price) {
            this.day_customer_price = day_customer_price;
        }

        public int getRefund_order_number() {
            return refund_order_number;
        }

        public void setRefund_order_number(int refund_order_number) {
            this.refund_order_number = refund_order_number;
        }
    }


    public static class LunchBean implements Serializable {
        /**
         * total_amount : 65.7
         * total_pay_amount : 63.2
         * total_orders : 6
         * day_pay_amount : 10.95
         * day_customer_price : 10.53
         * refund_order_number : 1
         */

        private double total_amount;
        private double total_pay_amount;
        private int total_orders;
        private double day_pay_amount;
        private double day_customer_price;
        private int refund_order_number;

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getTotal_pay_amount() {
            return total_pay_amount;
        }

        public void setTotal_pay_amount(double total_pay_amount) {
            this.total_pay_amount = total_pay_amount;
        }

        public int getTotal_orders() {
            return total_orders;
        }

        public void setTotal_orders(int total_orders) {
            this.total_orders = total_orders;
        }

        public double getDay_pay_amount() {
            return day_pay_amount;
        }

        public void setDay_pay_amount(double day_pay_amount) {
            this.day_pay_amount = day_pay_amount;
        }

        public double getDay_customer_price() {
            return day_customer_price;
        }

        public void setDay_customer_price(double day_customer_price) {
            this.day_customer_price = day_customer_price;
        }

        public int getRefund_order_number() {
            return refund_order_number;
        }

        public void setRefund_order_number(int refund_order_number) {
            this.refund_order_number = refund_order_number;
        }
    }


    public static class DinnerBean implements Serializable {
        /**
         * total_amount : 370.9
         * total_pay_amount : 362.9
         * total_orders : 26
         * day_pay_amount : 61.82
         * day_customer_price : 13.96
         * refund_order_number : 2
         */

        private double total_amount;
        private double total_pay_amount;
        private int total_orders;
        private double day_pay_amount;
        private double day_customer_price;
        private int refund_order_number;

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getTotal_pay_amount() {
            return total_pay_amount;
        }

        public void setTotal_pay_amount(double total_pay_amount) {
            this.total_pay_amount = total_pay_amount;
        }

        public int getTotal_orders() {
            return total_orders;
        }

        public void setTotal_orders(int total_orders) {
            this.total_orders = total_orders;
        }

        public double getDay_pay_amount() {
            return day_pay_amount;
        }

        public void setDay_pay_amount(double day_pay_amount) {
            this.day_pay_amount = day_pay_amount;
        }

        public double getDay_customer_price() {
            return day_customer_price;
        }

        public void setDay_customer_price(double day_customer_price) {
            this.day_customer_price = day_customer_price;
        }

        public int getRefund_order_number() {
            return refund_order_number;
        }

        public void setRefund_order_number(int refund_order_number) {
            this.refund_order_number = refund_order_number;
        }
    }
}
