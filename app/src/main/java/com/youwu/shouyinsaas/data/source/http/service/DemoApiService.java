package com.youwu.shouyinsaas.data.source.http.service;

import com.youwu.shouyinsaas.entity.DemoEntity;
import com.youwu.shouyinsaas.ui.login.bean.UpDateBean;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by goldze on 2017/6/15.
 */

public interface DemoApiService {
    @GET("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<DemoEntity>> demoGet();

    @FormUrlEncoded
    @POST("action/apiv2/banner")
    Observable<BaseResponse<DemoEntity>> demoPost(@Field("catalog") String catalog);

    /**
     * 检查更新
     *
     * @return
     */
    @GET("app_version")
    Observable<BaseBean<UpDateBean>> GET_APP_VERSION();

    /**
     * 门店信息
     *
     * @param cashier_id 收银员id
     * @param store_id   门店id
     * @param type       门店数据库类型 1.早餐2.中餐厅3.餐饮SssS
     * @return
     */
    @FormUrlEncoded
    @POST("api/store_info")
    Observable<BaseBean<Object>> STORE_INFO(@Field("cashier_id") String cashier_id, @Field("store_id") String store_id, @Field("type") String type);

    /**
     * 登录
     *
     * @param tel      账号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("auth/login")
    Observable<BaseBean<Object>> LOGIN(@Field("tel") String tel, @Field("password") String password, @Field("type") String type);

    /**
     * 获取群组
     *
     * @param store_id 门店id
     * @return
     */
    @FormUrlEncoded
    @POST("goods/goods_group")
    Observable<BaseBean<Object>> GOODS_GROUP(@Field("store_id") String store_id);

    /**
     * 获取商品
     *
     * @param store_id 门店id
     * @param group_id 群组id
     * @return
     */
    @FormUrlEncoded
    @POST("goods/goods_list")
    Observable<BaseBean<Object>> GOODS_List(@Field("store_id") String store_id, @Field("group_id") String group_id, @Field("page") String page, @Field("limit") String limit);

    /**
     * 获取盘点、沽清商品
     *
     * @param store_id    门店id
     * @param category_id 分类id
     * @return
     */
    @FormUrlEncoded
    @POST("goods/stock_goods_list")
    Observable<BaseBean<Object>> GET_STOCK_GOODS_List(@Field("store_id") String store_id, @Field("category_id") String category_id, @Field("page") String page, @Field("limit") String limit, @Field("type") String type);

    /**
     * 获取个人信息
     *
     * @return
     */

    @POST("auth/me")
    Observable<BaseBean<Object>> GET_ME();

    /**
     * 待处理小程序订单数量
     *
     * @return
     */

    @POST("order/xcx_order_count")
    Observable<BaseBean<Object>> XCX_ORDER_COUNT();

    /**
     * 小程序订单
     *
     * @param type 1.待接单2.待出餐3.带退款
     * @return
     */
    @FormUrlEncoded
    @POST("order/xcx_order_list")
    Observable<BaseBean<Object>> XCX_ORDER_LIST(@Field("type") String type);

    /**
     * 接单、拒单、出餐
     *
     * @param order_sn 订单编号
     * @param status   2接单3.拒单4.出餐'
     * @return
     */
    @FormUrlEncoded
    @POST("order/edit_order_status")
    Observable<BaseBean<Object>> DEIT_ORDER_STATUS(@Field("order_sn") String order_sn, @Field("status") String status);

    /**
     * @param goods_list       商品列表
     * @param store_id         门店id
     * @param user_id          用户id
     * @param delivery_method  配送方式 堂食 外卖 自提
     * @param tableware_number 餐具数量
     * @param packing_fee      打包费
     * @param mal              抹零金额
     * @param payList          支付类型 1.余额2.微信3.支付宝4.现金5.外卖
     * @param coupon_id        优惠券id
     * @param user_coupon_id        优惠券id
     * @param discount         折扣比率
     * @param mark              备注
     * @return
     */
    @FormUrlEncoded
    @POST("order/add_order")
    Observable<BaseBean<Object>> Add_ORDER(@Field("goods_list") String goods_list, @Field("store_id") String store_id, @Field("user_id") String user_id,
                                           @Field("delivery_method") String delivery_method, @Field("tableware_number") String tableware_number,
                                           @Field("packing_fee") String packing_fee, @Field("mal") String mal, @Field("pay_list") String payList,
                                           @Field("coupon_id") String coupon_id,@Field("user_coupon_id") String user_coupon_id,@Field("discount") String discount,@Field("dynamic_id") String dynamic_id,@Field("mark") String mark);

    /**
     * 订单列表
     *
     * @param start           开始时间
     * @param end             结束时间
     * @param page            页数
     * @param delivery_method 配送方式
     * @param tel             手机号
     * @param store_id             店铺id
     * @return
     */
    @FormUrlEncoded
    @POST("order/order_list")
    Observable<BaseBean<Object>> ORDER_List(@Field("start") String start, @Field("end") String end, @Field("page") int page, @Field("limit") String limit, @Field("delivery_method") String delivery_method, @Field("tel") String tel, @Field("store_id") String store_id);

    /**
     * 订单列表
     *
     * @param order_sn 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("order/order_details")
    Observable<BaseBean<Object>> ORDER_DETAILS(@Field("order_sn") String order_sn);

    /**
     * 小程序订单列表
     *
     * @param pay_sn 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("order/order_details")
    Observable<BaseBean<Object>> XCX_ORDER_DETAILS(@Field("pay_sn") String pay_sn);

    /**
     * 反结帐
     *
     * @param order_sn 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("refund/refund")
    Observable<BaseBean<Object>> REFUND(@Field("order_sn") String order_sn);

    /**
     * 查询交接班
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    @FormUrlEncoded
    @POST("shift_change")
    Observable<BaseBean<Object>> SHIFT_CHANGE(@Field("start") String start, @Field("end") String end);

    /**
     * 添加交接班
     *
     * @param total_amount_list
     * @param number_list
     * @param amount_list
     * @param other_money_list
     * @param cash_amount
     * @param cash_total_amount
     * @return
     */
    @FormUrlEncoded
    @POST("add_shift_change")
    Observable<BaseBean<Object>> ADD_SHIFT_CHANGE(@Field("total_amount_list") String total_amount_list, @Field("number_list") String number_list, @Field("amount_list") String amount_list,
                                                  @Field("other_money_list") String other_money_list, @Field("cash_amount") String cash_amount, @Field("cash_total_amount") String cash_total_amount);

    /**
     * 搜索会员
     *
     * @param tel 手机号
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_info")
    Observable<BaseBean<Object>> USER_INFO(@Field("tel") String tel,@Field("user_id") String user_id);

    /**
     * 获取盘点分类
     *
     * @return
     */

    @POST("goods/goods_category")
    Observable<BaseBean<Object>> GOODS_CATEGORY();

    /**
     * 获取小程序群组
     *
     * @return
     */

    @POST("goods/get_mini_group")
    Observable<BaseBean<Object>> GET_MINI_GROUP();

    /**
     * 获取原因
     *
     * @return
     */

    @POST("stock/reason")
    Observable<BaseBean<Object>> GET_REASON();

    /**
     * 盘点
     *
     * @param goods_list 商品列表
     * @return
     */
    @FormUrlEncoded
    @POST("stock/sorting_inventory")
    Observable<BaseBean<Object>> SORTING_INVENTORY(@Field("goods_list") String goods_list);

    /**
     * 沽清
     *
     * @param goods_list 商品列表
     * @return
     */
    @FormUrlEncoded
    @POST("stock/out_stock")
    Observable<BaseBean<Object>> OUT_STOCK(@Field("goods_list") String goods_list);

    /**
     * 核销
     *
     * @param order_sn 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("order/close_order")
    Observable<BaseBean<Object>> CLOSE_ORDER(@Field("order_sn") String order_sn);
    /**
     * 申请订货
     *
     * @param store_id 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("cargo/add_order")
    Observable<BaseBean<Object>> ADD_ORDER(@Field("store_id") String store_id,@Field("arrival_time") String arrival_time,@Field("goods_list") String goods_list);
    /**
     * 获取订货商品列表
     *
     * @param store_id 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("cargo/goods_list")
    Observable<BaseBean<Object>> ORDER_INFO(@Field("store_id") String store_id);

    /**
     * 获取订单列表
     * @param store_id 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("cargo/order_list")
    Observable<BaseBean<Object>> ORDER_LIST(@Field("store_id") String store_id);

    /**
     * 退货商品列表
     * @param order_sn 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("return_cargo/goods_list")
    Observable<BaseBean<Object>> RETURN_ORDER_LIST(@Field("order_sn") String order_sn);

    /**
     * 退货详情
     * @param order_sn 订单编号
     * @return
     */
    @FormUrlEncoded
    @POST("return_cargo/return_cargo_list")
    Observable<BaseBean<Object>> RETURN_CARGO_LIST(@Field("order_sn") String order_sn);

    /**
     * 小程序订单审核
     * @param type
     * @param order_sn
     * @param refund_reason
     * @param modify_stock
     * @return
     */
    @FormUrlEncoded
    @POST("order/audit_order_refund")
    Observable<BaseBean<Object>> AUDIT_ORDER_REFUND(@Field("type") int type,@Field("order_sn") String order_sn,@Field("refund_reason") String refund_reason,@Field("modify_stock") String modify_stock );

    /**
     * 创建会员信息
     *
     * @param tel       手机号
     * @param user_name 昵称
     * @param remark    备注
     * @return
     */
    @FormUrlEncoded
    @POST("user/create_user")
    Observable<BaseBean<Object>> CREATE_USER(@Field("tel") String tel, @Field("user_name") String user_name, @Field("remark") String remark);

    /**
     * 获取充值金额活动列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("activity/recharge_list")
    Observable<BaseBean<Object>> RECHARGE_LIST(@Field("store_id") String store_id);

    /**
     * 会员充值
     *
     * @param activity_id       活动id
     * @param activity_price_id id
     * @param user_id           用户id
     * @param type              1现金2微信3支付宝
     * @param is_customize      是否自定义充值 0是 1不是
     * @param dynamic_id        用户支付码
     * @return
     */
    @FormUrlEncoded
    @POST("activity/recharge")
    Observable<BaseBean<Object>> RECHARGE(@Field("activity_id") String activity_id, @Field("activity_price_id") String activity_price_id, @Field("user_id") String user_id, @Field("type") String type, @Field("is_customize") String is_customize, @Field("money") String money, @Field("dynamic_id") String dynamic_id);

    /**
     * 门店充值记录
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    @FormUrlEncoded
    @POST("activity/recharge_log")
    Observable<BaseBean<Object>> RECHARGE_LOG(@Field("start") String start, @Field("end") String end);

    /**
     * 获取销售概况
     *
     * @param type 1.今日 2.本周 3.本月 4.本季度
     * @return
     */
    @FormUrlEncoded
    @POST("sales_situation")
    Observable<BaseBean<Object>> SALES_SITUATION(@Field("type") String type,@Field("store_id") String store_id);

    /**
     * 日结信息
     *
     * @return
     */
    @POST("day_sales")
    Observable<BaseBean<Object>> DAY_SALES();

    /**
     * 添加日结
     *
     * @param total_amount      总销售金额
     * @param pay_amount        实收金额
     * @param total_amount_list 总销售金额列表
     * @param amount_list       总实收金额列表
     * @return
     */
    @FormUrlEncoded
    @POST("add_day_sales")
    Observable<BaseBean<Object>> ADD_DAY_SALES(@Field("total_amount") String total_amount, @Field("pay_amount") String pay_amount, @Field("total_amount_list") String total_amount_list, @Field("amount_list") String amount_list);

    /**
     * 查询优惠券
     *
     * @param member_id 用户id
     * @param store_id  店铺id
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_coupon")
    Observable<BaseBean<Object>> USER_COUPON(@Field("member_id") String member_id, @Field("store_id") String store_id);

    /**
     * 添加商品
     *
     * @param name        商品名称
     * @param goods_code  条码
     * @param main_image  商品图片
     * @param unit        单位
     * @param category_id 分类id
     * @param stock       库存
     * @param sale_price  销售价格
     * @param cost_price  进货价
     * @param group_id    群组id
     * @param details     商品描述
     * @param status      上架状态
     * @return
     */
    @FormUrlEncoded
    @POST("goods/add_goods")
    Observable<BaseBean<Object>> ADD_GOODS(@Field("name") String name, @Field("goods_code") String goods_code, @Field("main_image") String main_image, @Field("unit") String unit, @Field("category_id") String category_id,
                                           @Field("stock") String stock, @Field("sale_price") String sale_price, @Field("cost_price") String cost_price, @Field("group_id") String group_id, @Field("details") String details,
                                           @Field("status") String status);

    /**
     * 添加套餐
     *
     * @param name       套餐名称
     * @param goods_code 条码
     * @param unit       单位
     * @param sale_price 销售价格
     * @param group_id   群组id
     * @param desc       商品描述
     * @param status     上架状态
     * @param goods_list 商品信息
     * @return
     */
    @FormUrlEncoded
    @POST("goods/add_package")
    Observable<BaseBean<Object>> ADD_PACKAGE(@Field("name") String name, @Field("code") String goods_code, @Field("unit") String unit, @Field("sale_price") String sale_price, @Field("group_id") String group_id,
                                             @Field("desc") String desc, @Field("status") String status, @Field("goods_list") String goods_list);

    /**
     * 更新门店自动接单
     *
     * @param is_order       是都自动接单 1自动接单 2 手动接单
     * @param start         开始时间
     * @param end           结束时间
     * @return
     */
    @FormUrlEncoded
    @POST("update_store")
    Observable<BaseBean<Object>> UPDATE_STORE(@Field("is_order") String is_order, @Field("start") String start, @Field("end") String end);

    /**
     * 退货
     *
     * @param order_sn
     * @param store_id         店铺id
     * @param goods_list       商品信息
     * @return
     */
    @FormUrlEncoded
    @POST("return_cargo/return_cargo")
    Observable<BaseBean<Object>> RETURN_CARGO(@Field("order_sn") String order_sn, @Field("store_id") String store_id, @Field("goods_list") String goods_list);
    /**
     * 收货
     *
     * @param goods_list       商品信息
     * @return
     */
    @FormUrlEncoded
    @POST("cargo/receive")
    Observable<BaseBean<Object>> RECEIVE(@Field("order_list") String goods_list);
    /**
     * 门店列表设置
     *
     * @return
     */

    @POST("setting_list")
    Observable<BaseBean<Object>> SETTING_LIST();

    /**
     * 反结帐（审核退款订单）2023/03/08加
     * @param order_sn      订单编号
     * @param store_id      门店id
     * @return
     */
    @FormUrlEncoded
    @POST("order/new_refund_order")
    Observable<BaseBean<Object>> NEW_REFUNF_ORDER(@Field("store_id") String store_id, @Field("order_sn") String order_sn);

    /**
     * 日志列表     2023/03/15加
     * @param store_id      门店id
     * @return
     */
    @FormUrlEncoded
    @POST("report/new_day_sales")
    Observable<BaseBean<Object>> NEW_DAY_SALES(@Field("store_id") String store_id);

    /**
     * 销售概况  2023/03/15加
     *
     * @param start                     开始日期
     * @param end                       结束日期
     * @param breakfast_start           早餐开始时间
     * @param breakfast_end             早餐结束时间
     * @param lunch_start               午餐开始时间
     * @param lunch_end                 午餐结束时间
     * @param dinner_start              晚餐开始时间
     * @param dinner_end                晚餐结束时间
     */
    @FormUrlEncoded
    @POST("report/new_sales_info")
    Observable<BaseBean<Object>> NEW_SALES_INFO(@Field("start") String start,@Field("end") String end,@Field("breakfast_start") String breakfast_start,@Field("breakfast_end") String breakfast_end,@Field("lunch_start") String lunch_start,
                                                @Field("lunch_end") String lunch_end,@Field("dinner_start") String dinner_start,@Field("dinner_end") String dinner_end);

    /**
     * 提交日结  2023/03/15加
     *
     * @param total_amount
     * @param total_orders
     * @param pay_amount
     * @param reduced_amount
     * @param cash_list
     */
    @FormUrlEncoded
    @POST("report/new_update_day_sales")
    Observable<BaseBean<Object>> NEW_UPDATE_DAY_SALES(@Field("total_amount") String total_amount,@Field("total_orders") String total_orders,@Field("pay_amount") String pay_amount,@Field("reduced_amount") String reduced_amount,
                                                      @Field("cash_list") String cash_list);
}


