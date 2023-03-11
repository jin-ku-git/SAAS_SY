package com.youwu.shouyinsaas.service;

import java.util.HashMap;

public class MyHashMap<K> extends HashMap<K,String> {

    /**
     * 使用HashMap中containsKey判断key是否已经存在
     * @param key
     * @param value
     * @return
     */
    @Override
    public String put(K key, String value) {
        String newV = value;
        if (containsKey(key)) {
            String oldV = get(key);
            newV = oldV + "" + newV;
        }
        return super.put(key, newV);
    }
}