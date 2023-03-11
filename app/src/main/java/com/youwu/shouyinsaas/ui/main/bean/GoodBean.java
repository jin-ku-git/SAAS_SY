package com.youwu.shouyinsaas.ui.main.bean;

import java.io.Serializable;

public class GoodBean implements Serializable {

    /**
     * goods_id : 1123
     * goods_sku : 123546
     * goods_name : 测试商品
     * goods_price : 1
     * image : 123
     * stock : 10
     * group_id : 1
     * group_name : 新鲜蔬菜
     * group_sort : 100
     * group_img : 11
     */

    private int goods_id;
    private String goods_sku;
    private String goods_name;
    private String goods_price;
    private String image;
    private int stock;
    private int group_id;
    private String group_name;
    private int group_sort;
    private String group_img;


    private boolean com_number_state;//当加入到购物车时显示
    private int com_number;//商品数量
    private String goods_peibi;//商品配比

    public String getGoods_peibi() {
        return goods_peibi;
    }

    public void setGoods_peibi(String goods_peibi) {
        this.goods_peibi = goods_peibi;
    }

    public int getCom_number() {
        return com_number;
    }

    public void setGoods_number(int com_number) {
        this.com_number = com_number;
    }

    public boolean isCom_number_state() {
        return com_number_state;
    }

    public void setCom_number_state(boolean com_number_state) {
        this.com_number_state = com_number_state;
    }

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

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getGroup_sort() {
        return group_sort;
    }

    public void setGroup_sort(int group_sort) {
        this.group_sort = group_sort;
    }

    public String getGroup_img() {
        return group_img;
    }

    public void setGroup_img(String group_img) {
        this.group_img = group_img;
    }
}
