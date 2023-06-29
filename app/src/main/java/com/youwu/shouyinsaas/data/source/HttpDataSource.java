package com.youwu.shouyinsaas.data.source;

import com.youwu.shouyinsaas.entity.DemoEntity;
import com.youwu.shouyinsaas.ui.login.bean.UpDateBean;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * 2022/03/21
 */
public interface HttpDataSource {


    Observable<BaseResponse<DemoEntity>> demoGet();

    Observable<BaseResponse<DemoEntity>> demoPost(String catalog);

    //检查更新
    Observable<BaseBean<UpDateBean>> GET_APP_VERSION();

    //门店信息
    Observable<BaseBean<Object>> STORE_INFO(String cashier_id, String store_id, String type);

    //登录
    Observable<BaseBean<Object>> LOGIN(String name, String password,String type);

    //获取商品群组
    Observable<BaseBean<Object>> GOODS_GROUP(String store_id);

    //获取商品
    Observable<BaseBean<Object>> GOODS_List(String store_id, String group_id, String page, String limit);

    //获取盘点、沽清商品
    Observable<BaseBean<Object>> GET_STOCK_GOODS_List(String store_id, String category_id, String page, String limit,String type);

    //获取个人信息
    Observable<BaseBean<Object>> GET_ME();

    //待处理小程序订单数量
    Observable<BaseBean<Object>> XCX_ORDER_COUNT();

    //小程序订单列表
    Observable<BaseBean<Object>> XCX_ORDER_LIST(String type);

    //接单、拒单、出餐
    Observable<BaseBean<Object>> DEIT_ORDER_STATUS(String order_sn, String status);

    //下单
    Observable<BaseBean<Object>> Add_ORDER(String goods_list, String store_id, String user_id, String delivery_method, String tableware_number, String packing_fee, String mal, String payList, String coupon_id,String user_coupon_id, String discount, String dynamic_id,String mark);

    //订单列表
    Observable<BaseBean<Object>> ORDER_List(String start, String end, int page, String delivery_method, String tel,String store_id);

    //订单详情
    Observable<BaseBean<Object>> ORDER_DETAILS(String order_sn);
    //小程序订单详情
    Observable<BaseBean<Object>> XCX_ORDER_DETAILS(String pay_sn);

    //反结帐
    Observable<BaseBean<Object>> REFUND(String order_sn, String refund_price);

    //查询交接班
    Observable<BaseBean<Object>> SHIFT_CHANGE(String start, String end);

    //添加交接班
    Observable<BaseBean<Object>> ADD_SHIFT_CHANGE(String total_amount_list, String number_list, String amount_list, String other_money_list, String cash_amount, String cash_total_amount);

    //搜索会员
    Observable<BaseBean<Object>> USER_INFO(String tel,String user_id);

    //获取盘点分类
    Observable<BaseBean<Object>> GOODS_CATEGORY();

    //获取小程序群组
    Observable<BaseBean<Object>> GET_MINI_GROUP();

    //获取原因
    Observable<BaseBean<Object>> GET_REASON();

    //盘点
    Observable<BaseBean<Object>> SORTING_INVENTORY(String goods_list);

    //沽清
    Observable<BaseBean<Object>> OUT_STOCK(String goods_list);

    //核销
    Observable<BaseBean<Object>> CLOSE_ORDER(String order_sn);
    //申请订货
    Observable<BaseBean<Object>> ADD_ORDER(String storeId,String arrival_time,String saasList);
    //获取订货商品列表
    Observable<BaseBean<Object>> ORDER_INFO(String store_id);
    //获取订单列表
    Observable<BaseBean<Object>> ORDER_LIST(String store_id);
    //退货商品列表
    Observable<BaseBean<Object>> RETURN_ORDER_LIST(String order_sn);
    //退货详情
    Observable<BaseBean<Object>> RETURN_CARGO_LIST(String order_sn);

    //创建会员信息
    Observable<BaseBean<Object>> CREATE_USER(String tel, String user_name, String remark);
    //小程序订单审核
    Observable<BaseBean<Object>> AUDIT_ORDER_REFUND(int status, String order_sn, String refund_reason, String modify_stock);

    //获取充值金额列表
    Observable<BaseBean<Object>> RECHARGE_LIST(String store_id);

    //会员充值
    Observable<BaseBean<Object>> RECHARGE(String activity_id, String activity_price_id, String user_id, String type, String is_customize, String money, String dynamic_id);

    //门店充值记录
    Observable<BaseBean<Object>> RECHARGE_LOG(String start, String end);

    //获取销售概况
    Observable<BaseBean<Object>> SALES_SITUATION(String type,String store_id);

    //日结信息
    Observable<BaseBean<Object>> DAY_SALES();

    //添加日结
    Observable<BaseBean<Object>> ADD_DAY_SALES(String total_amount, String pay_amount, String total_amount_list, String amount_list);

    //查询优惠券
    Observable<BaseBean<Object>> USER_COUPON(String member_id, String store_id);

    //添加商品
    Observable<BaseBean<Object>> ADD_GOODS(String name, String goods_code, String main_image, String unit, String category_id, String stock, String sale_price, String cost_price, String group_id, String details, String status);

    //添加套餐
    Observable<BaseBean<Object>> ADD_PACKAGE(String name, String code, String unit, String sale_price, String group_id, String desc, String status, String goods_list);
    //更新门店自动接单
    Observable<BaseBean<Object>> UPDATE_STORE(String is_order, String start, String end);
    //退货
    Observable<BaseBean<Object>> RETURN_CARGO(String orderSn, String store_id, String goods_list);
    //收货
    Observable<BaseBean<Object>> RECEIVE(String goods_list);
    //门店设置列表
    Observable<BaseBean<Object>> SETTING_LIST();
    //反结帐（审核退款订单）
    Observable<BaseBean<Object>> NEW_REFUNF_ORDER(String store_id,String order_sn);
    //日志列表
    Observable<BaseBean<Object>> NEW_DAY_SALES(String store_id);
    //销售概况
    Observable<BaseBean<Object>> NEW_SALES_INFO(String start,String end,String breakfast_start,String breakfast_end,String lunch_start,String lunch_end,String dinner_start,String dinner_end);
    //提交日结
    Observable<BaseBean<Object>> NEW_UPDATE_DAY_SALES(String total_amount, String total_orders, String pay_amount, String reduced_amount, String cash_list);
}
