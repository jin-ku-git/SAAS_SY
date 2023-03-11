package com.youwu.shouyinsaas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.db.bean.OrderNumberBean;
import com.youwu.shouyinsaas.db.bean.RestingInfoBean;
import com.youwu.shouyinsaas.db.bean.TypeResult;
import com.youwu.shouyinsaas.db.bean.TypeResultDeserializer;
import com.youwu.shouyinsaas.db.bean.TypeSuper;
import com.youwu.shouyinsaas.ui.bean.VipBean;


import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.KLog;


/**
 * Created by lzzie
 * Date: 2018-8-10 17:33:43
 */
public class OrdersDao {
    public static final String TAG = "OrdersDao";
    // 列定义 (id integer primary key,creat_time text,order_number text,ordernumber_bean text,vip_bean text,shop_car_goods text)

    private final String[] ORDER_COLUMNS = new String[]{"id", "creat_time", "ordernumber_bean", "vip_bean", "shop_car_goods", "order_number"};

    private Context context;
    private DBHelper orderDBHelper;

    public OrdersDao(Context context) {
        this.context = context;
        orderDBHelper = new DBHelper(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist() {
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = orderDBHelper.getReadableDatabase();
            // select count(Id) from Goods
            cursor = db.query(orderDBHelper.GOODS_TABLE_NAME, new String[]{"COUNT(id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    /**
     * 添加一个数据到数据库
     * {"id", "creat_time", "vip_bean", "shop_car_goods"}
     */
    public synchronized void addOrder(RestingInfoBean restingInfoBean) {

        SQLiteDatabase db = null;
        try {
            db = orderDBHelper.getWritableDatabase();
            db.beginTransaction();


            ContentValues contentValues = new ContentValues();
            contentValues.put("creat_time", restingInfoBean.getOrderNumberBean().getCreatTime() + "");
            contentValues.put("ordernumber_bean", AppApplication.gson.toJson(restingInfoBean.getOrderNumberBean()));
            contentValues.put("vip_bean", AppApplication.gson.toJson(restingInfoBean.getBean()));
            KLog.d("shop_goods------"+AppApplication.gson.toJson(restingInfoBean.getShopCarYWGoodBeans()));
            contentValues.put("shop_car_goods", AppApplication.gson.toJson(restingInfoBean.getShopCarYWGoodBeans()));
            contentValues.put("order_number", restingInfoBean.getOrderNumberBean().getOrderNumberStr() + "");

            db.insert(orderDBHelper.RESTING0RDER_TABLE_NAME, null, contentValues);

//                    db.execSQL("insert into " +  goodsDBHelper.GOODS_TABLE_NAME + " (Id, GoodsName, Repertory, Price,ImgUrl,Type) values (" + gId + "," + gName +","+ gRepertory + "," + gPrice + "," + gImg + "," + gType + ")");


            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 查询Goods数据库中所有数据
     */
    public List<RestingInfoBean> getAllDate() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<RestingInfoBean> restingInfoBeans = new ArrayList();
        try {
            db = orderDBHelper.getReadableDatabase();
            // select * from Goods
            cursor = db.query(orderDBHelper.RESTING0RDER_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    restingInfoBeans.add(parseOrder(cursor));
                }
                return restingInfoBeans;
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllData--exception", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return restingInfoBeans;
    }




    public boolean deleteOrder(String creatTime) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = orderDBHelper.getReadableDatabase();
            cursor = db.query(orderDBHelper.RESTING0RDER_TABLE_NAME, ORDER_COLUMNS, "creat_time = ?", new String[]{creatTime}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (null != cursor && cursor.getCount() > 0) {
                db.delete(orderDBHelper.RESTING0RDER_TABLE_NAME, "creat_time = ?", new String[]{creatTime});
                return true;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }


    /**
     * 将查找到的数据转换成Order类
     * {"id", "creat_time", "ordernumber_bean", "vip_bean", "shop_car_goods"}
     */
    private RestingInfoBean parseOrder(Cursor cursor) {
        RestingInfoBean restingInfoBean = new RestingInfoBean();
        restingInfoBean.setOrderNumberBean(AppApplication.gson.fromJson(cursor.getString(cursor.getColumnIndex(ORDER_COLUMNS[2])), OrderNumberBean.class));

        restingInfoBean.setBean(AppApplication.gson.fromJson(cursor.getString(cursor.getColumnIndex(ORDER_COLUMNS[3])), VipBean.class));
//List list = new ArrayList();
//        Object o = MyApplication.gson.fromJson(cursor.getString(cursor.getColumnIndex(ORDER_COLUMNS[4])), new TypeToken<List<Object>>() {
//        }.getType());

        GsonBuilder gsonb = new GsonBuilder();
        gsonb.registerTypeAdapter(TypeResult.class, new TypeResultDeserializer());
        gsonb.serializeNulls();
        Gson gson = gsonb.create();

        List<TypeSuper> item = gson.fromJson(cursor.getString(cursor.getColumnIndex(ORDER_COLUMNS[4])), TypeResult.class).data;




        restingInfoBean.setShopCarYWGoodBeans(item);

        return restingInfoBean;
    }

    public void deleteAllData() {
        SQLiteDatabase db = null;

        db = orderDBHelper.getReadableDatabase();
        try {
            db.execSQL("delete from " + orderDBHelper.RESTING0RDER_TABLE_NAME);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


}
