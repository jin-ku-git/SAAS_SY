package com.youwu.shouyinsaas.db.bean;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;


import java.lang.reflect.Type;

public class TypeResultDeserializer implements JsonDeserializer<TypeResult> {

    @Override
    public TypeResult deserialize(JsonElement arg0, Type arg1,
                                  JsonDeserializationContext arg2) throws JsonParseException {

        JsonArray asJsonArray = arg0.getAsJsonArray();
        TypeResult result = new TypeResult();
        for (JsonElement jsonElement : asJsonArray) {
            JsonObject jsonOb = jsonElement.getAsJsonObject();
            //111

            CommunityBean ywGoodBean = new CommunityBean();
                Log.d("取单123",jsonOb.toString());

                ywGoodBean.setGoods_id(jsonOb.get("goods_id").getAsInt());

                ywGoodBean.setGoods_name(jsonOb.get("goods_name").getAsString());
                ywGoodBean.setGoods_sku(jsonOb.get("goods_sku").getAsString());
                ywGoodBean.setGoods_id_sku(jsonOb.get("goods_id_sku").getAsString());

                ywGoodBean.setGoods_price(jsonOb.get("goods_price").getAsString());
                ywGoodBean.setGoods_number(jsonOb.get("goods_number").getAsInt());
                ywGoodBean.setGoods_cost_price(jsonOb.get("goods_cost_price").getAsString());
                ywGoodBean.setGoods_img(jsonOb.get("goods_img").getAsString());
                ywGoodBean.setCom_number_state(jsonOb.get("com_number_state").getAsInt());
                ywGoodBean.setDetails(jsonOb.get("details").getAsString());
                ywGoodBean.setPackage_id(jsonOb.get("package_id").getAsInt());
                ywGoodBean.setStock(jsonOb.get("stock").getAsInt());

                ywGoodBean.setGroup_id(jsonOb.get("group_id").getAsInt());
                ywGoodBean.setGroup_name(jsonOb.get("group_name").getAsString());
                ywGoodBean.setGroup_sort(jsonOb.get("group_sort").getAsInt());
                ywGoodBean.setGroup_img(jsonOb.get("group_img").getAsString());
                ywGoodBean.setType(jsonOb.get("type").getAsInt());
                result.data.add(ywGoodBean);

        }
        return result;
    }
}