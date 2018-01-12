package com.mokey.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @User: jufeng
 * @Date: 18-1-4
 * @Time: 下午3:59
 **/
public class ResultItems {

    private Map<String, Object> fields = new LinkedHashMap<String, Object>();

    public boolean isEmpty(){
        return CommonUtil.isEmpty(fields);
    }


    public <T> T get(String key) {
        Object o = fields.get(key);
        if (o == null) {
            return null;
        }
        return (T) fields.get(key);
    }

    public Map<String, Object> getAll() {
        return fields;
    }

    public <T> ResultItems put(String key, T value) {
        fields.put(key, value);
        return this;
    }


    @Override
    public String toString() {
        return "ResultItems{" +
                "fields=" + fields +
                '}';
    }
}
