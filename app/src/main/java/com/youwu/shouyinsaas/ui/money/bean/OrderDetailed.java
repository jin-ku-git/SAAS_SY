package com.youwu.shouyinsaas.ui.money.bean;

import java.util.List;

/**
 * @author: Administrator
 * @date: 2022/8/19
 */
public class OrderDetailed {


    /**
     * id : 48
     * company_id : 1
     * store_id : 1
     * meal_address_id : 0
     * cabinet_id : 0
     * table_id : 0
     * payment_sn : 100101010499991660895366
     * order_sn : 100101010499981660895366
     * member_id : 4
     * total_amount : 11
     * item_amount : 11
     * reduced_amount : 0
     * pick_type : 1
     * tableware_number : 1
     * packing_fee : 0
     * shipping_type : 4
     * shipping_fee : 0
     * pay_amount : 11
     * pay_channel : 3
     * order_status : 1
     * store_time_id : 0
     * cart_ids :
     * created_at : 2022-08-19 15:49:26
     * updated_at : 2022-08-19 15:49:26
     * mark :
     * order_taking_status : 4
     * order_source : 1
     * orderdetails : [{"id":64,"order_sn":"100101010499981660895366","payment_sn":"100101010499991660895366","goods_id":0,"package_id":1,"activity_id":0,"goods_thumb":"https://images2.youwuu.com/avatar/445665390434063356.jpg","goods_sku":"","goods_name":"美味早餐","goods_price":11,"goods_pay_price":11,"goods_cost_price":0,"goods_quantity":1,"goods_amount":11,"created_at":"2022-08-19 15:49:26","updated_at":"2022-08-19 15:49:26"}]
     * deliveryinfo : {"id":42,"order_sn":"100101010499981660895366","pickup_status":2,"appointment_time":"2022-08-19 00:00:00","appointment_hour":"15:49:26","pickup_time":"2022-08-19 15:49:26","pickup_address":"临沂市","pickup_name":"餘生","pickup_phone":"13468214148","created_at":"2022-08-19 15:49:26","updated_at":"2022-08-19 15:49:26","mark":"门店下单","pick_code":"E003"}
     */

    private int id;
    private int company_id;
    private int store_id;
    private int jm_store_id;
    private int store_type;
    private int cashier_id;
    private int meal_address_id;
    private int cabinet_id;
    private int table_id;
    private int system_table_id;
    private String payment_sn;
    private String order_sn;
    private String member_id;
    private String total_amount;
    private String item_amount;
    private String reduced_amount;
    private int pick_type;
    private int tableware_number;
    private String packing_fee;
    private String delivery_method_id;
    private int shipping_type;
    private String shipping_fee;
    private String pay_amount;
    private String pay_channel;
    private String channel_id;
    private int order_status;
    private int store_time_id;
    private String cart_ids;
    private String created_at;
    private String updated_at;
    private String mark;
    private int order_taking_status;
    private int is_booking;
    private int is_package;
    private int is_supplement;
    private String dining_code;
    private String is_group_buying_order;
    private int order_source;
    private DeliveryinfoBean deliveryinfo;
    private List<OrderdetailsBean> orderdetails;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(String item_amount) {
        this.item_amount = item_amount;
    }

    public String getReduced_amount() {
        return reduced_amount;
    }

    public void setReduced_amount(String reduced_amount) {
        this.reduced_amount = reduced_amount;
    }

    public String getPacking_fee() {
        return packing_fee;
    }

    public void setPacking_fee(String packing_fee) {
        this.packing_fee = packing_fee;
    }

    public String getDelivery_method_id() {
        return delivery_method_id;
    }

    public void setDelivery_method_id(String delivery_method_id) {
        this.delivery_method_id = delivery_method_id;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getIs_booking() {
        return is_booking;
    }

    public void setIs_booking(int is_booking) {
        this.is_booking = is_booking;
    }

    public int getIs_package() {
        return is_package;
    }

    public void setIs_package(int is_package) {
        this.is_package = is_package;
    }

    public int getIs_supplement() {
        return is_supplement;
    }

    public void setIs_supplement(int is_supplement) {
        this.is_supplement = is_supplement;
    }

    public String getDining_code() {
        return dining_code;
    }

    public void setDining_code(String dining_code) {
        this.dining_code = dining_code;
    }

    public String getIs_group_buying_order() {
        return is_group_buying_order;
    }

    public void setIs_group_buying_order(String is_group_buying_order) {
        this.is_group_buying_order = is_group_buying_order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getMeal_address_id() {
        return meal_address_id;
    }

    public void setMeal_address_id(int meal_address_id) {
        this.meal_address_id = meal_address_id;
    }

    public int getCabinet_id() {
        return cabinet_id;
    }

    public void setCabinet_id(int cabinet_id) {
        this.cabinet_id = cabinet_id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public String getPayment_sn() {
        return payment_sn;
    }

    public void setPayment_sn(String payment_sn) {
        this.payment_sn = payment_sn;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getJm_store_id() {
        return jm_store_id;
    }

    public void setJm_store_id(int jm_store_id) {
        this.jm_store_id = jm_store_id;
    }

    public int getStore_type() {
        return store_type;
    }

    public void setStore_type(int store_type) {
        this.store_type = store_type;
    }

    public int getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(int cashier_id) {
        this.cashier_id = cashier_id;
    }

    public int getSystem_table_id() {
        return system_table_id;
    }

    public void setSystem_table_id(int system_table_id) {
        this.system_table_id = system_table_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }



    public int getPick_type() {
        return pick_type;
    }

    public void setPick_type(int pick_type) {
        this.pick_type = pick_type;
    }

    public int getTableware_number() {
        return tableware_number;
    }

    public void setTableware_number(int tableware_number) {
        this.tableware_number = tableware_number;
    }


    public int getShipping_type() {
        return shipping_type;
    }

    public void setShipping_type(int shipping_type) {
        this.shipping_type = shipping_type;
    }



    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getStore_time_id() {
        return store_time_id;
    }

    public void setStore_time_id(int store_time_id) {
        this.store_time_id = store_time_id;
    }

    public String getCart_ids() {
        return cart_ids;
    }

    public void setCart_ids(String cart_ids) {
        this.cart_ids = cart_ids;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getOrder_taking_status() {
        return order_taking_status;
    }

    public void setOrder_taking_status(int order_taking_status) {
        this.order_taking_status = order_taking_status;
    }

    public int getOrder_source() {
        return order_source;
    }

    public void setOrder_source(int order_source) {
        this.order_source = order_source;
    }

    public DeliveryinfoBean getDeliveryinfo() {
        return deliveryinfo;
    }

    public void setDeliveryinfo(DeliveryinfoBean deliveryinfo) {
        this.deliveryinfo = deliveryinfo;
    }

    public List<OrderdetailsBean> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(List<OrderdetailsBean> orderdetails) {
        this.orderdetails = orderdetails;
    }

    public static class DeliveryinfoBean {
        /**
         * id : 42
         * order_sn : 100101010499981660895366
         * pickup_status : 2
         * appointment_time : 2022-08-19 00:00:00
         * appointment_hour : 15:49:26
         * pickup_time : 2022-08-19 15:49:26
         * pickup_address : 临沂市
         * pickup_name : 餘生
         * pickup_phone : 13468214148
         * created_at : 2022-08-19 15:49:26
         * updated_at : 2022-08-19 15:49:26
         * mark : 门店下单
         * pick_code : E003
         */

        private int id;
        private String order_sn;
        private int pickup_status;
        private String appointment_time;
        private String appointment_hour;
        private String pickup_time;
        private String pickup_address;
        private String pickup_name;
        private String pickup_phone;
        private String created_at;
        private String updated_at;
        private String mark;
        private String pick_code;
        private int is_send;

        public int getIs_send() {
            return is_send;
        }

        public void setIs_send(int is_send) {
            this.is_send = is_send;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public int getPickup_status() {
            return pickup_status;
        }

        public void setPickup_status(int pickup_status) {
            this.pickup_status = pickup_status;
        }

        public String getAppointment_time() {
            return appointment_time;
        }

        public void setAppointment_time(String appointment_time) {
            this.appointment_time = appointment_time;
        }

        public String getAppointment_hour() {
            return appointment_hour;
        }

        public void setAppointment_hour(String appointment_hour) {
            this.appointment_hour = appointment_hour;
        }

        public String getPickup_time() {
            return pickup_time;
        }

        public void setPickup_time(String pickup_time) {
            this.pickup_time = pickup_time;
        }

        public String getPickup_address() {
            return pickup_address;
        }

        public void setPickup_address(String pickup_address) {
            this.pickup_address = pickup_address;
        }

        public String getPickup_name() {
            return pickup_name;
        }

        public void setPickup_name(String pickup_name) {
            this.pickup_name = pickup_name;
        }

        public String getPickup_phone() {
            return pickup_phone;
        }

        public void setPickup_phone(String pickup_phone) {
            this.pickup_phone = pickup_phone;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getPick_code() {
            return pick_code;
        }

        public void setPick_code(String pick_code) {
            this.pick_code = pick_code;
        }
    }

    public static class OrderdetailsBean {
        /**
         * id : 64
         * order_sn : 100101010499981660895366
         * payment_sn : 100101010499991660895366
         * goods_id : 0
         * package_id : 1
         * activity_id : 0
         * goods_thumb : https://images2.youwuu.com/avatar/445665390434063356.jpg
         * goods_sku :
         * goods_name : 美味早餐
         * goods_price : 11
         * goods_pay_price : 11
         * goods_cost_price : 0
         * goods_quantity : 1
         * goods_amount : 11
         * created_at : 2022-08-19 15:49:26
         * updated_at : 2022-08-19 15:49:26
         */

        private int id;
        private String order_sn;
        private String payment_sn;
        private int goods_id;
        private int store_goods_id;
        private String store_goods_sku;
        private String channel_id;
        private int package_id;
        private int activity_id;
        private String goods_thumb;
        private String goods_sku;
        private String goods_name;
        private String goods_price;
        private String goods_pay_price;
        private String goods_cost_price;
        private int goods_quantity;
        private String goods_amount;
        private String created_at;
        private String updated_at;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getPayment_sn() {
            return payment_sn;
        }

        public void setPayment_sn(String payment_sn) {
            this.payment_sn = payment_sn;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getPackage_id() {
            return package_id;
        }

        public void setPackage_id(int package_id) {
            this.package_id = package_id;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
        }

        public String getGoods_thumb() {
            return goods_thumb;
        }

        public void setGoods_thumb(String goods_thumb) {
            this.goods_thumb = goods_thumb;
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



        public int getGoods_quantity() {
            return goods_quantity;
        }

        public void setGoods_quantity(int goods_quantity) {
            this.goods_quantity = goods_quantity;
        }


        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getStore_goods_id() {
            return store_goods_id;
        }

        public void setStore_goods_id(int store_goods_id) {
            this.store_goods_id = store_goods_id;
        }

        public String getStore_goods_sku() {
            return store_goods_sku;
        }

        public void setStore_goods_sku(String store_goods_sku) {
            this.store_goods_sku = store_goods_sku;
        }

        public String getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(String channel_id) {
            this.channel_id = channel_id;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_pay_price() {
            return goods_pay_price;
        }

        public void setGoods_pay_price(String goods_pay_price) {
            this.goods_pay_price = goods_pay_price;
        }

        public String getGoods_cost_price() {
            return goods_cost_price;
        }

        public void setGoods_cost_price(String goods_cost_price) {
            this.goods_cost_price = goods_cost_price;
        }

        public String getGoods_amount() {
            return goods_amount;
        }

        public void setGoods_amount(String goods_amount) {
            this.goods_amount = goods_amount;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
