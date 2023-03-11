package com.youwu.shouyinsaas.data.source.http;

import com.youwu.shouyinsaas.data.source.HttpDataSource;
import com.youwu.shouyinsaas.data.source.http.service.DemoApiService;
import com.youwu.shouyinsaas.entity.DemoEntity;
import com.youwu.shouyinsaas.ui.login.bean.UpDateBean;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private DemoApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(DemoApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(DemoApiService apiService) {
        this.apiService = apiService;
    }


    @Override
    public Observable<BaseResponse<DemoEntity>> demoGet() {
        return apiService.demoGet();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoPost(String catalog) {
        return apiService.demoPost(catalog);
    }
    /**
     * 检查更新
     * @return
     */
    @Override
    public Observable<BaseBean<UpDateBean>> GET_APP_VERSION() {
        return apiService.GET_APP_VERSION();
    }
    /**
     * 门店信息
     * @param cashier_id
     * @param store_id
     * @param type
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> STORE_INFO(String cashier_id, String store_id,String type) {
        return apiService.STORE_INFO(cashier_id,store_id,type);
    }
    /**
     * 登录
     * @param tel
     * @param password
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> LOGIN(String tel, String password,String type) {
        return apiService.LOGIN(tel,password,type);
    }
    /**
     * 获取商品群组
     * @param store_id 门店群组
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GOODS_GROUP(String store_id) {
        return apiService.GOODS_GROUP(store_id);
    }
    /**
     * 获取商品
     * @param store_id 门店群组
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GOODS_List(String store_id, String group_id,String page,String limit) {
        return apiService.GOODS_List(store_id,group_id,page,limit);
    }
    /**
     * 获取盘点、沽清商品
     * @param store_id 门店群组
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GET_STOCK_GOODS_List(String store_id, String category_id,String page,String limit,String type) {
        return apiService.GET_STOCK_GOODS_List(store_id,category_id,page,limit,type);
    }
    /**
     * 获取个人信息
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GET_ME() {
        return apiService.GET_ME();
    }
    /**
     * 待处理小程序订单数量
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> XCX_ORDER_COUNT() {
        return apiService.XCX_ORDER_COUNT();
    }
    /**
     * 小程序订单列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> XCX_ORDER_LIST(String type) {
        return apiService.XCX_ORDER_LIST(type);
    }
    /**
     * 小程序订单列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> DEIT_ORDER_STATUS(String order_sn, String status) {
        return apiService.DEIT_ORDER_STATUS(order_sn,status);
    }

    /**
     *
     * @param goods_list 商品列表
     * @param store_id   门店id
     * @param user_id    用户id
     * @param delivery_method  配送方式 堂食 外卖 自提
     * @param tableware_number  餐具数量
     * @param packing_fee       打包费
     * @param mal               抹零金额
     * @param payList             支付类型 1.余额2.微信3.支付宝4.现金5.外卖
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> Add_ORDER(String goods_list, String store_id, String user_id, String delivery_method, String tableware_number, String packing_fee, String mal, String payList,String coupon_id,String user_coupon_id,String discount,String dynamic_id,String mark) {
        return apiService.Add_ORDER(goods_list,store_id,user_id,delivery_method,tableware_number,packing_fee,mal,payList,coupon_id,user_coupon_id,discount,dynamic_id,mark);
    }
    /**
     * 订单列表
     * @param start                 开始时间
     * @param end                   结束时间
     * @param page                  页数
     * @param delivery_method       配送方式
     * @param tel                   手机号
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ORDER_List(String start,String end,int page,String delivery_method,String tel,String store_id) {
        return apiService.ORDER_List(start,end,page,"30",delivery_method,tel,store_id);
    }

    /**
     * 订单详情
     * @param order_sn  订单编号
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ORDER_DETAILS(String order_sn) {
        return apiService.ORDER_DETAILS(order_sn);
    }
    /**
     * 小程序订单详情
     * @param pay_sn  订单编号
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> XCX_ORDER_DETAILS(String pay_sn) {
        return apiService.XCX_ORDER_DETAILS(pay_sn);
    }
    /**
     * 反结帐
     * @param order_sn  订单编号
     * @param refund_price  退款金额
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> REFUND(String order_sn,String refund_price) {
        return apiService.REFUND(order_sn);
    }
    /**
     * 查询交接班
     * @param start  开始时间
     * @param end  结束时间
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> SHIFT_CHANGE(String start,String end) {
        return apiService.SHIFT_CHANGE(start,end);
    }

    /**
     *  添加交接班
     * @param total_amount_list
     * @param number_list
     * @param amount_list
     * @param other_money_list
     * @param cash_amount
     * @param cash_total_amount
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ADD_SHIFT_CHANGE(String total_amount_list, String number_list, String amount_list, String other_money_list, String cash_amount, String cash_total_amount) {
        return apiService.ADD_SHIFT_CHANGE(total_amount_list,number_list,amount_list,other_money_list,cash_amount,cash_total_amount);
    }
    /**
     * 搜索会员
     * @param tel  手机号
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> USER_INFO(String tel,String user_id) {
        return apiService.USER_INFO(tel,user_id);
    }
    /**
     * 获取盘点分类
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GOODS_CATEGORY() {
        return apiService.GOODS_CATEGORY();
    }
    /**
     * 获取小程序群组
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GET_MINI_GROUP() {
        return apiService.GET_MINI_GROUP();
    }
    /**
     * 获取原因
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GET_REASON() {
        return apiService.GET_REASON();
    }

    /**
     * 盘点
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> SORTING_INVENTORY(String goods_list) {
        return apiService.SORTING_INVENTORY(goods_list);
    }

    /**
     * 沽清
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> OUT_STOCK(String goods_list) {
        return apiService.OUT_STOCK(goods_list);
    }

    /**
     * 核销
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> CLOSE_ORDER(String order_sn) {
        return apiService.CLOSE_ORDER(order_sn);
    }
    /**
     * 申请订货
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ADD_ORDER(String storeId,String arrival_time,String saasList) {
        return apiService.ADD_ORDER(storeId,arrival_time,saasList);
    }
    /**
     * 获取订货商品列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ORDER_INFO(String store_id) {
        return apiService.ORDER_INFO(store_id);
    }
    /**
     * 获取订单列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ORDER_LIST(String store_id) {
        return apiService.ORDER_LIST(store_id);
    }
    /**
     * 退货商品列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RETURN_ORDER_LIST(String order_sn) {
        return apiService.RETURN_ORDER_LIST(order_sn);
    }

    /**
     * 退货详情
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RETURN_CARGO_LIST(String order_sn) {
        return apiService.RETURN_CARGO_LIST(order_sn);
    }

    /**
     * 小程序订单审核
     * @param status
     * @param order_sn
     * @param refund_reason
     * @param modify_stock
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> AUDIT_ORDER_REFUND(int status, String order_sn, String refund_reason, String modify_stock) {
        return apiService.AUDIT_ORDER_REFUND(status,order_sn,refund_reason,modify_stock);
    }
    /**
     * 创建会员信息
     * @param tel           手机号
     * @param user_name     昵称
     * @param remark        备注
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> CREATE_USER(String tel,String user_name,String remark) {
        return apiService.CREATE_USER(tel,user_name,remark);
    }

    /**
     * 获取充值金额列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RECHARGE_LIST(String store_id) {
        return apiService.RECHARGE_LIST(store_id);
    }

    /**
     * 会员充值
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RECHARGE(String activity_id,String activity_price_id,String user_id,String type,String is_customize,String money,String dynamic_id) {
        return apiService.RECHARGE(activity_id,activity_price_id,user_id,type,is_customize,money,dynamic_id);
    }

    /**
     * 门店充值记录
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RECHARGE_LOG(String start,String end) {
        return apiService.RECHARGE_LOG(start,end);
    }

    /**
     * 获取销售概况
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> SALES_SITUATION(String type,String store_id) {
        return apiService.SALES_SITUATION(type,store_id);
    }

    /**
     * 日结信息
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> DAY_SALES() {
        return apiService.DAY_SALES();
    }
    /**
     * 添加日结
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ADD_DAY_SALES(String total_amount,String pay_amount,String total_amount_list,String amount_list) {
        return apiService.ADD_DAY_SALES(total_amount,pay_amount,total_amount_list,amount_list);
    }
    /**
     * 查询优惠券
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> USER_COUPON(String member_id,String store_id) {
        return apiService.USER_COUPON(member_id,store_id);
    }

    /**
     * 添加商品
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ADD_GOODS(String name, String goods_code, String main_image, String unit, String category_id, String stock, String sale_price, String cost_price, String group_id, String details, String status) {
        return apiService.ADD_GOODS(name,goods_code,main_image,unit,category_id,stock,sale_price,cost_price,group_id,details,status);
    }
    /**
     * 添加套餐
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> ADD_PACKAGE(String name, String code, String unit, String sale_price, String group_id, String desc, String status, String goods_list) {
        return apiService.ADD_PACKAGE(name,code,unit,sale_price,group_id,desc,status,goods_list);
    }
    /**
     * 更新门店自动接单
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> UPDATE_STORE(String is_order, String start, String end) {
        return apiService.UPDATE_STORE(is_order,start,end);
    }
    /**
     * 退货
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RETURN_CARGO(String orderSn, String store_id, String goods_list) {
        return apiService.RETURN_CARGO(orderSn,store_id,goods_list);
    }
    /**
     * 收货
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RECEIVE(String goods_list) {
        return apiService.RECEIVE(goods_list);
    }
    /**
     * 门店列表设置
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> SETTING_LIST() {
        return apiService.SETTING_LIST();
    }

    /**
     * 反结帐（审核退款订单）2023/03/08加
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> NEW_REFUNF_ORDER(String store_id,String order_sn) {
        return apiService.NEW_REFUNF_ORDER(store_id,order_sn);
    }
}
