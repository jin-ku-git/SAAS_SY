package com.youwu.shouyinsaas.ui.bean;

import java.io.Serializable;

/**
 * vip信息表
 */
public class VipBean implements Serializable {
    private int id;
    private String nick_name;//会员名称
    private String tel;//手机号
    private String head_img;//头像
    private String money;//余额
    private String add_time;//创建时间
    private String update_time;//更新时间
    private String balance;//余额
    private int type_state;//1 选择该vip  2取消
    /**
     * id : 258004.0
     * user_name : 13468214148
     * openid : ood895UUir8JbYHPswv7UjbLp_4k
     * status : 1.0
     * portrait_img :
     * add_time : 1.633943137E9
     * update_time : 1.652952291E9
     * unionid : o--Vr05brf4gX0QRWFk4oe7xlo18
     * wx_openid :
     * is_coupon : 1.0
     * last_buy_time : 1.636886955E9
     * store_id : 0.0
     * cashier_id : 0.0
     * xmsh_openid :
     * type : 1.0
     * remark :
     * name :
     * balance : 136.11
     * resequity : {"id":236312,"u_id":258004,"payment_code_num":"134682141483602","payment_code_img":"static/user_barcode/1/134682141483602.png","balance":136.11,"balance_update_time":1.654154919E9,"integral":484,"integral_update_time":1.647493689E9,"balance_pay_type":1}
     */

    private String user_name;
    private String openid;
    private double status;
    private String portrait_img;

    private String unionid;
    private String wx_openid;
    private double is_coupon;
    private double last_buy_time;
    private double store_id;
    private double cashier_id;
    private String xmsh_openid;
    private double type;
    private String remark;
    private String name;

    private ResequityBean resequity;


    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

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

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public String getPortrait_img() {
        return portrait_img;
    }

    public void setPortrait_img(String portrait_img) {
        this.portrait_img = portrait_img;
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

    public double getIs_coupon() {
        return is_coupon;
    }

    public void setIs_coupon(double is_coupon) {
        this.is_coupon = is_coupon;
    }

    public double getLast_buy_time() {
        return last_buy_time;
    }

    public void setLast_buy_time(double last_buy_time) {
        this.last_buy_time = last_buy_time;
    }

    public double getStore_id() {
        return store_id;
    }

    public void setStore_id(double store_id) {
        this.store_id = store_id;
    }

    public double getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(double cashier_id) {
        this.cashier_id = cashier_id;
    }

    public String getXmsh_openid() {
        return xmsh_openid;
    }

    public void setXmsh_openid(String xmsh_openid) {
        this.xmsh_openid = xmsh_openid;
    }

    public double getType() {
        return type;
    }

    public void setType(double type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public ResequityBean getResequity() {
        return resequity;
    }

    public void setResequity(ResequityBean resequity) {
        this.resequity = resequity;
    }

    public static class ResequityBean implements Serializable{
        /**
         * id : 236312.0
         * u_id : 258004.0
         * payment_code_num : 134682141483602
         * payment_code_img : static/user_barcode/1/134682141483602.png
         * balance : 136.11
         * balance_update_time : 1.654154919E9
         * integral : 484.0
         * integral_update_time : 1.647493689E9
         * balance_pay_type : 1.0
         */


        private double id;
        private double u_id;
        private String payment_code_num;
        private String payment_code_img;
        private double balance;
        private double balance_update_time;
        private double integral;
        private double integral_update_time;
        private double balance_pay_type;


        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getU_id() {
            return u_id;
        }

        public void setU_id(double u_id) {
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


        public double getBalance_update_time() {
            return balance_update_time;
        }

        public void setBalance_update_time(double balance_update_time) {
            this.balance_update_time = balance_update_time;
        }

        public double getIntegral() {
            return integral;
        }

        public void setIntegral(double integral) {
            this.integral = integral;
        }

        public double getIntegral_update_time() {
            return integral_update_time;
        }

        public void setIntegral_update_time(double integral_update_time) {
            this.integral_update_time = integral_update_time;
        }

        public double getBalance_pay_type() {
            return balance_pay_type;
        }

        public void setBalance_pay_type(double balance_pay_type) {
            this.balance_pay_type = balance_pay_type;
        }
    }
}
