package com.youwu.shouyinsaas.ui.vip.bean;

import java.io.Serializable;

public class VipBean implements Serializable {

    /**
     * id : 38
     * user_name : 16619912724
     * nick_name : 哟哟哟哟哟哟
     * openid : ood895ZSBccd8eZS8912u1QrVm9U
     * head_img : https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLuHpzkOcuqH57sZic8Q7fQibFoBn2xWyU6ibGM          BqLdVEmsZBJRw624qOg6pzicGQlhfnBViacp6YwXhKw/132
     * status : 1
     * portrait_img :
     * add_time : 1585452840
     * update_time : 1640010671
     * unionid : o--Vr09rveVr5UMTqvdjq0_02TOQ
     * wx_openid :
     * is_coupon : 1
     * last_buy_time : 1612005370
     * store_id : 0
     * cashier_id : 0
     * xmsh_openid : oNYVX457eppVedUMc3heJdvmEwkU
     * zc_openid :
     * type : 1
     * remark : null
     * name :
     * balance : 0.27
     * equity : {"id":38,"u_id":38,"payment_code_num":"166199127244808","payment_code_img":"static/user_barcode/1/166199127244808.png","balance":0.27,"balance_update_time":1628355791,"integral":3145,"integral_update_time":1644973420,"plus_vip_start_time":0,"plus_vip_end_time":0,"breakfast_vip_start_time":0,"breakfast_vip_end_time":0,"commission":0,"total_commission":0,"commission_update_time":1594734764}
     */

    private int id;
    private String user_name;
    private String nick_name;
    private String openid;
    private String head_img;
    private int status;
    private String portrait_img;
    private int add_time;
    private int update_time;
    private String unionid;
    private String wx_openid;
    private int is_coupon;
    private int last_buy_time;
    private int store_id;
    private int cashier_id;
    private String xmsh_openid;
    private String zc_openid;
    private int type;
    private Object remark;
    private String name;
    private double balance;
    private EquityBean equity;

    private int type_state;//1 选择该vip  2取消

    public int getType_state() {
        return type_state;
    }

    public void setType_state(int type_state) {
        this.type_state = type_state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPortrait_img() {
        return portrait_img;
    }

    public void setPortrait_img(String portrait_img) {
        this.portrait_img = portrait_img;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getWx_openid() {
        return wx_openid;
    }

    public void setWx_openid(String wx_openid) {
        this.wx_openid = wx_openid;
    }

    public int getIs_coupon() {
        return is_coupon;
    }

    public void setIs_coupon(int is_coupon) {
        this.is_coupon = is_coupon;
    }

    public int getLast_buy_time() {
        return last_buy_time;
    }

    public void setLast_buy_time(int last_buy_time) {
        this.last_buy_time = last_buy_time;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(int cashier_id) {
        this.cashier_id = cashier_id;
    }

    public String getXmsh_openid() {
        return xmsh_openid;
    }

    public void setXmsh_openid(String xmsh_openid) {
        this.xmsh_openid = xmsh_openid;
    }

    public String getZc_openid() {
        return zc_openid;
    }

    public void setZc_openid(String zc_openid) {
        this.zc_openid = zc_openid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public EquityBean getEquity() {
        return equity;
    }

    public void setEquity(EquityBean equity) {
        this.equity = equity;
    }

    public static class EquityBean {
        /**
         * id : 38
         * u_id : 38
         * payment_code_num : 166199127244808
         * payment_code_img : static/user_barcode/1/166199127244808.png
         * balance : 0.27
         * balance_update_time : 1628355791
         * integral : 3145
         * integral_update_time : 1644973420
         * plus_vip_start_time : 0
         * plus_vip_end_time : 0
         * breakfast_vip_start_time : 0
         * breakfast_vip_end_time : 0
         * commission : 0
         * total_commission : 0
         * commission_update_time : 1594734764
         */

        private int id;
        private int u_id;
        private String payment_code_num;
        private String payment_code_img;
        private double balance;
        private int balance_update_time;
        private int integral;
        private int integral_update_time;
        private int plus_vip_start_time;
        private int plus_vip_end_time;
        private int breakfast_vip_start_time;
        private int breakfast_vip_end_time;
        private int commission;
        private int total_commission;
        private int commission_update_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getU_id() {
            return u_id;
        }

        public void setU_id(int u_id) {
            this.u_id = u_id;
        }

        public String getPayment_code_num() {
            return payment_code_num;
        }

        public void setPayment_code_num(String payment_code_num) {
            this.payment_code_num = payment_code_num;
        }

        public String getPayment_code_img() {
            return payment_code_img;
        }

        public void setPayment_code_img(String payment_code_img) {
            this.payment_code_img = payment_code_img;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public int getBalance_update_time() {
            return balance_update_time;
        }

        public void setBalance_update_time(int balance_update_time) {
            this.balance_update_time = balance_update_time;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getIntegral_update_time() {
            return integral_update_time;
        }

        public void setIntegral_update_time(int integral_update_time) {
            this.integral_update_time = integral_update_time;
        }

        public int getPlus_vip_start_time() {
            return plus_vip_start_time;
        }

        public void setPlus_vip_start_time(int plus_vip_start_time) {
            this.plus_vip_start_time = plus_vip_start_time;
        }

        public int getPlus_vip_end_time() {
            return plus_vip_end_time;
        }

        public void setPlus_vip_end_time(int plus_vip_end_time) {
            this.plus_vip_end_time = plus_vip_end_time;
        }

        public int getBreakfast_vip_start_time() {
            return breakfast_vip_start_time;
        }

        public void setBreakfast_vip_start_time(int breakfast_vip_start_time) {
            this.breakfast_vip_start_time = breakfast_vip_start_time;
        }

        public int getBreakfast_vip_end_time() {
            return breakfast_vip_end_time;
        }

        public void setBreakfast_vip_end_time(int breakfast_vip_end_time) {
            this.breakfast_vip_end_time = breakfast_vip_end_time;
        }

        public int getCommission() {
            return commission;
        }

        public void setCommission(int commission) {
            this.commission = commission;
        }

        public int getTotal_commission() {
            return total_commission;
        }

        public void setTotal_commission(int total_commission) {
            this.total_commission = total_commission;
        }

        public int getCommission_update_time() {
            return commission_update_time;
        }

        public void setCommission_update_time(int commission_update_time) {
            this.commission_update_time = commission_update_time;
        }
    }
}
