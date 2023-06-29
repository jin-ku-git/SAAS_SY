package com.youwu.shouyinsaas.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.youwu.shouyinsaas.data.source.HttpDataSource;
import com.youwu.shouyinsaas.data.source.LocalDataSource;
import com.youwu.shouyinsaas.entity.DemoEntity;
import com.youwu.shouyinsaas.ui.login.bean.UpDateBean;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 * Created by goldze on 2019/3/26.
 */
public class DemoRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static DemoRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private DemoRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static DemoRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (DemoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DemoRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }



    @Override
    public Observable<BaseResponse<DemoEntity>> demoGet() {
        return mHttpDataSource.demoGet();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoPost(String catalog) {
        return mHttpDataSource.demoPost(catalog);
    }

    /**
     * 账户
     * @param userName
     */
    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    /**
     * 密码
     * @param password
     */
    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    /**
     * 获取账户
     * @return
     */
    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    /**
     * 获取密码
     * @return
     */
    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }

    /**
     * 退出账户
     * @return
     */
    @Override
    public void SignOutAccount() {
        mLocalDataSource.SignOutAccount();
    }


    /**
     * 检查更新
     * @return
     */
    public Observable<BaseBean<UpDateBean>> GET_APP_VERSION() {
        return mHttpDataSource.GET_APP_VERSION();
    }

    /**
     * 门店信息
     * @param cashier_id
     * @param store_id
     * @param type
     * @return
     */
    public Observable<BaseBean<Object>> STORE_INFO(String cashier_id, String store_id,String type) {
        return mHttpDataSource.STORE_INFO(cashier_id,store_id,type);
    }

    /**
     *登录
     * @return
     * @param name 手机号
     * @param password 密码
     */
    public Observable<BaseBean<Object>> LOGIN(String name, String password,String type) {
        return mHttpDataSource.LOGIN(name,password,type);
    }

    /**
     *获取群组
     * @return
     * @param store_id 门店id
     */
    @Override
    public Observable<BaseBean<Object>> GOODS_GROUP(String store_id) {
        return mHttpDataSource.GOODS_GROUP(store_id);
    }
    /**
     *获取商品
     * @return
     * @param store_id 门店id
     * @param group_id 群组id
     */
    @Override
    public Observable<BaseBean<Object>> GOODS_List(String store_id,String group_id,String page,String limit) {
        return mHttpDataSource.GOODS_List(store_id,group_id,page,limit);
    }

    /**
     *获取盘点、沽清商品
     * @return
     * @param store_id 门店id
     * @param category_id 分类id
     */
    @Override
    public Observable<BaseBean<Object>> GET_STOCK_GOODS_List(String store_id,String category_id,String page,String limit,String type) {
        return mHttpDataSource.GET_STOCK_GOODS_List(store_id,category_id,page,limit,type);
    }

    /**
     *获取个人信息
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GET_ME() {
        return mHttpDataSource.GET_ME();
    }

    /**
     *待处理小程序订单数量
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> XCX_ORDER_COUNT() {
        return mHttpDataSource.XCX_ORDER_COUNT();
    }

    /**
     *小程序订单列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> XCX_ORDER_LIST(String type) {
        return mHttpDataSource.XCX_ORDER_LIST(type);
    }
    /**
     * 接单、拒单、出餐
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> DEIT_ORDER_STATUS(String order_sn, String status) {
        return mHttpDataSource.DEIT_ORDER_STATUS(order_sn,status);
    }

    /**
     *下单
     *
     * @param goods_list 商品列表
     * @param store_id   门店id
     * @param user_id    用户id
     * @param delivery_method  配送方式 堂食 外卖 自提
     * @param tableware_number  餐具数量
     * @param packing_fee       打包费
     * @param mal               抹零金额
     * @param coupon_id               优惠券id
     * @param user_coupon_id               优惠券id
     * @param payList             支付类型 1.余额2.微信3.支付宝4.现金5.外卖
     * @return
     */
    public Observable<BaseBean<Object>> Add_ORDER(String goods_list, String store_id, String user_id, String delivery_method, String tableware_number, String packing_fee, String mal, String payList,String coupon_id,String user_coupon_id,String discount,String dynamic_id,String mark) {
        return mHttpDataSource.Add_ORDER(goods_list,store_id,user_id,delivery_method,tableware_number,packing_fee,mal,payList,coupon_id,user_coupon_id,discount,dynamic_id,mark);
    }

    /**
     * 订单列表
     * @param start  开始时间戳
     * @param end     结束时间戳
     * @return
     */
    public Observable<BaseBean<Object>> ORDER_List(String start,String end,int page,String delivery_method,String tel,String store_id) {
        return mHttpDataSource.ORDER_List(start,end,page,delivery_method,tel,store_id);
    }
    /**
     * 订单详情
     * @param order_sn  订单编号
     * @return
     */
    public Observable<BaseBean<Object>> ORDER_DETAILS(String order_sn) {
        return mHttpDataSource.ORDER_DETAILS(order_sn);
    }
    /**
     * 小程序订单详情
     * @param pay_sn  订单编号
     * @return
     */
    public Observable<BaseBean<Object>> XCX_ORDER_DETAILS(String pay_sn) {
        return mHttpDataSource.XCX_ORDER_DETAILS(pay_sn);
    }
    /**
     * 反结帐
     * @param order_sn  订单编号
     * @param refund_price  退款金额
     * @return
     */
    public Observable<BaseBean<Object>> REFUND(String order_sn,String refund_price) {
        return mHttpDataSource.REFUND(order_sn,refund_price);
    }

    /**
     * 查询交接班
     *
     * @param start
     * @param end
     * @return
     */
    public Observable<BaseBean<Object>> SHIFT_CHANGE(String start, String end) {
        return mHttpDataSource.SHIFT_CHANGE(start,end);
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
    public Observable<BaseBean<Object>> ADD_SHIFT_CHANGE(String total_amount_list, String number_list, String amount_list, String other_money_list, String cash_amount, String cash_total_amount) {
        return mHttpDataSource.ADD_SHIFT_CHANGE(total_amount_list,number_list,amount_list,other_money_list,cash_amount,cash_total_amount);
    }

    /**
     * 搜索会员
     *
     * @param tel
     * @return
     */
    public Observable<BaseBean<Object>> USER_INFO(String tel,String user_id) {
        return mHttpDataSource.USER_INFO(tel,user_id);
    }

    /**
     *获取盘点分类
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GOODS_CATEGORY() {
        return mHttpDataSource.GOODS_CATEGORY();
    }
    /**
     *获取小程序群组
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GET_MINI_GROUP() {
        return mHttpDataSource.GET_MINI_GROUP();
    }

    /**
     *获取原因
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GET_REASON() {
        return mHttpDataSource.GET_REASON();
    }
    /**
     * 盘点
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> SORTING_INVENTORY(String goods_list) {
        return mHttpDataSource.SORTING_INVENTORY(goods_list);
    }

    /**
     * 沽清
     * @param goods_list
     * @return
     */
    public Observable<BaseBean<Object>> OUT_STOCK(String goods_list) {
        return mHttpDataSource.OUT_STOCK(goods_list);
    }

    /**
     * 核销
     * @param order_sn  订单编号
     * @return
     */
    public Observable<BaseBean<Object>> CLOSE_ORDER(String order_sn) {
        return mHttpDataSource.CLOSE_ORDER(order_sn);
    }
    /**
     * 申请订货
     * @param storeId  订单编号
     * @param saasList  订货内容
     * @return
     */
    public Observable<BaseBean<Object>> ADD_ORDER(String storeId,String arrival_time,String saasList) {
        return mHttpDataSource.ADD_ORDER(storeId,arrival_time,saasList);
    }

    /**
     * 获取商品信息
     * @param store_id  订单编号
     * @return
     */
    public Observable<BaseBean<Object>> ORDER_INFO(String store_id) {
        return mHttpDataSource.ORDER_INFO(store_id);
    }
    /**
     * 获取订单列表
     * @param store_id  订单编号
     * @return
     */
    public Observable<BaseBean<Object>> ORDER_LIST(String store_id) {
        return mHttpDataSource.ORDER_LIST(store_id);
    }
    /**
     * 退货商品列表
     * @param order_sn  订单编号
     * @return
     */
    public Observable<BaseBean<Object>> RETURN_ORDER_LIST(String order_sn) {
        return mHttpDataSource.RETURN_ORDER_LIST(order_sn);
    }
    /**
     * 退货详情
     * @param order_sn  订单编号
     * @return
     */
    public Observable<BaseBean<Object>> RETURN_CARGO_LIST(String order_sn) {
        return mHttpDataSource.RETURN_CARGO_LIST(order_sn);
    }
    /**
     * 小程序订单审核
     * @param status
     * @param order_sn
     * @param refund_reason
     * @param modify_stock
     * @return
     */
    public Observable<BaseBean<Object>> AUDIT_ORDER_REFUND(int status, String order_sn, String refund_reason, String modify_stock ) {
        return mHttpDataSource.AUDIT_ORDER_REFUND(status,order_sn,refund_reason,modify_stock);
    }

    /**
     * 创建会员信息
     * @param tel           手机号
     * @param user_name     昵称
     * @param remark        备注
     * @return
     */
    public Observable<BaseBean<Object>> CREATE_USER(String tel,String user_name,String remark) {
        return mHttpDataSource.CREATE_USER(tel,user_name,remark);
    }
    /**
     * 获取充值金额列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RECHARGE_LIST(String store_id) {
        return mHttpDataSource.RECHARGE_LIST(store_id);
    }
    /**
     * 会员充值
     * @param type              1现金2微信3支付宝
     * @param is_customize      是否自定义充值 0是 1不是
     * @param dynamic_id        用户支付码
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> RECHARGE(String activity_id,String activity_price_id,String user_id,String type,String is_customize,String money,String dynamic_id) {
        return mHttpDataSource.RECHARGE(activity_id,activity_price_id,user_id,type,is_customize,money,dynamic_id);
    }

    /**
     * 门店充值记录
     * @param start
     * @param end
     * @return
     */
    public Observable<BaseBean<Object>> RECHARGE_LOG(String start, String end) {
        return mHttpDataSource.RECHARGE_LOG(start,end);
    }
    /**
     * 获取销售概况
     * @param type   1.今日 2.本周 3.本月 4.本季度
     * @return
     */
    public Observable<BaseBean<Object>> SALES_SITUATION(String type,String store_id) {
        return mHttpDataSource.SALES_SITUATION(type,store_id);
    }
    /**
     * 日结信息
     * @return
     */
    public Observable<BaseBean<Object>> DAY_SALES() {
        return mHttpDataSource.DAY_SALES();
    }
    /**
     * 添加日结
     * @return
     */
    public Observable<BaseBean<Object>> ADD_DAY_SALES(String total_amount,String pay_amount,String total_amount_list,String amount_list) {
        return mHttpDataSource.ADD_DAY_SALES(total_amount,pay_amount,total_amount_list,amount_list);
    }
    /**
     * 查询优惠券
     * @return
     */
    public Observable<BaseBean<Object>> USER_COUPON(String member_id,String store_id) {
        return mHttpDataSource.USER_COUPON(member_id,store_id);
    }

    /**
     * 添加商品
     * @return
     */
    public Observable<BaseBean<Object>> ADD_GOODS(String name,String goods_code,String main_image,String unit,String category_id,String stock,String sale_price,String cost_price,String group_id,String details,String status) {
        return mHttpDataSource.ADD_GOODS(name,goods_code,main_image,unit,category_id,stock,sale_price,cost_price,group_id,details,status);
    }

    /**
     *  添加套餐
     * @return
     */
    public Observable<BaseBean<Object>> ADD_PACKAGE(String name, String code, String unit, String sale_price, String group_id, String desc, String status, String goods_list) {
        return mHttpDataSource.ADD_PACKAGE(name,code,unit,sale_price,group_id,desc,status,goods_list);
    }
    /**
     *  更新门店自动接单
     * @return
     */
    public Observable<BaseBean<Object>> UPDATE_STORE(String is_order, String start, String end) {
        return mHttpDataSource.UPDATE_STORE(is_order,start,end);
    }

    /**
     * 退货
     * @param orderSn
     * @param store_id
     * @param goods_list
     * @return
     */
    public Observable<BaseBean<Object>> RETURN_CARGO(String orderSn, String store_id, String goods_list) {
        return mHttpDataSource.RETURN_CARGO(orderSn,store_id,goods_list);
    }

    /**
     * 收货
     * @param goods_list
     * @return
     */
    public Observable<BaseBean<Object>> RECEIVE( String goods_list) {
        return mHttpDataSource.RECEIVE(goods_list);
    }
    /**
     * 门店设置列表
     * @return
     */
    public Observable<BaseBean<Object>> SETTING_LIST() {
        return mHttpDataSource.SETTING_LIST();
    }

    /**
     * 反结帐（审核退款订单）2023/03/08加
     * @param store_id      门店id
     * @param order_sn      订单编号
     * @return
     */
    public Observable<BaseBean<Object>> NEW_REFUNF_ORDER(String store_id,String order_sn) {
        return mHttpDataSource.NEW_REFUNF_ORDER(store_id,order_sn);
    }

    /**
     * 日志列表
     *
     * @param store_id
     * @return
     */
    public Observable<BaseBean<Object>> NEW_DAY_SALES(String store_id) {
        return mHttpDataSource.NEW_DAY_SALES(store_id);
    }


    /**
     * 销售概况
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
    public Observable<BaseBean<Object>> NEW_SALES_INFO(String start,String end,String breakfast_start,String breakfast_end,String lunch_start,String lunch_end,String dinner_start,String dinner_end) {
        return mHttpDataSource.NEW_SALES_INFO(start,end,breakfast_start,breakfast_end,lunch_start,lunch_end,dinner_start,dinner_end);
    }

    /**
     *  提交日结 2023/03/15 加
     * @param total_amount
     * @param total_orders
     * @param pay_amount
     * @param reduced_amount
     * @param cash_list
     */
    public Observable<BaseBean<Object>> NEW_UPDATE_DAY_SALES(String total_amount, String total_orders, String pay_amount, String reduced_amount, String cash_list) {
        return mHttpDataSource.NEW_UPDATE_DAY_SALES(total_amount,total_orders,pay_amount,reduced_amount,cash_list);
    }
}
