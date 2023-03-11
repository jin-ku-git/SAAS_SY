package com.youwu.shouyinsaas.ui.order_goods.goods.bean;

import java.io.Serializable;
import java.util.List;

public class GoodsGroupBean implements Serializable {

 private String GroupName;
// private List<Spinner> spinnerList;
 private List<String> spinnerList;

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public List<String> getSpinnerList() {
        return spinnerList;
    }

    public void setSpinnerList(List<String> spinnerList) {
        this.spinnerList = spinnerList;
    }

    public  static class Spinner{
     private int id;
     private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
