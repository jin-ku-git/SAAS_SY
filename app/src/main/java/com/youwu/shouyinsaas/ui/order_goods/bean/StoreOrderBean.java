package com.youwu.shouyinsaas.ui.order_goods.bean;

import java.io.Serializable;

public class StoreOrderBean implements Serializable {


    /**
     * user_id : 1
     * user_name : 收银员1
     * user_tel : 15806906223
     * arrival_date : 2022-06-01
     * end_time : 2022-05-31 12:00:00
     * template_id : 1
     * dot_number :
     */

    private int user_id;
    private String user_name;
    private String user_tel;
    private String arrival_date;
    private String end_time;
    private int template_id;
    private String dot_number;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public String getDot_number() {
        return dot_number;
    }

    public void setDot_number(String dot_number) {
        this.dot_number = dot_number;
    }
}
