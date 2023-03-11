package com.youwu.shouyinsaas.ui.main.bean;

import java.io.Serializable;

/**
 * 优惠券表
 */
public class CouponBean implements Serializable {


    private String image;
    private String coupon_id;
    private int user_coupon_id;
    private int id;
    private String name;//优惠券名称

    private String type;//优惠券类型
    public int cunpon_number;//优惠券数量
    private boolean select = false;
    private double cou_money;//优惠金额
    /**
     * coupon_id : 1.0
     * coupon_name : 满10-5
     * start_time : 2022-06-03 00:00:00
     * end_time : 2022-06-10 00:00:00
     * coupon_type : 1.0
     * coupon_type_name : 满减券
     * full_price : 10.0
     * reduce_price : 5.0
     * discount_price : 0.0
     * is_reduction : 0.0
     * is_reduction_name : 未设置最高减免金额
     * highest_price : 0.0
     */

    private String coupon_name;
    private String start_time;
    private String end_time;
    private int coupon_type;
    private String coupon_type_name;
    private double full_price;
    private double reduce_price;
    private double discount_price;
    private double is_reduction;
    private String is_reduction_name;
    private double highest_price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_coupon_id() {
        return user_coupon_id;
    }

    public void setUser_coupon_id(int user_coupon_id) {
        this.user_coupon_id = user_coupon_id;
    }

    public int getCunpon_number() {
        return cunpon_number;
    }

    public void setCunpon_number(int cunpon_number) {
        this.cunpon_number = cunpon_number;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCou_money() {
        return cou_money;
    }

    public void setCou_money(double cou_money) {
        this.cou_money = cou_money;
    }


    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(int coupon_type) {
        this.coupon_type = coupon_type;
    }

    public String getCoupon_type_name() {
        return coupon_type_name;
    }

    public void setCoupon_type_name(String coupon_type_name) {
        this.coupon_type_name = coupon_type_name;
    }

    public double getFull_price() {
        return full_price;
    }

    public void setFull_price(double full_price) {
        this.full_price = full_price;
    }

    public double getReduce_price() {
        return reduce_price;
    }

    public void setReduce_price(double reduce_price) {
        this.reduce_price = reduce_price;
    }

    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    public double getIs_reduction() {
        return is_reduction;
    }

    public void setIs_reduction(double is_reduction) {
        this.is_reduction = is_reduction;
    }

    public String getIs_reduction_name() {
        return is_reduction_name;
    }

    public void setIs_reduction_name(String is_reduction_name) {
        this.is_reduction_name = is_reduction_name;
    }

    public double getHighest_price() {
        return highest_price;
    }

    public void setHighest_price(double highest_price) {
        this.highest_price = highest_price;
    }
}
