package com.mabo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mabo.annotation.Alias;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONUtil {
    /**
     * JSON转实体
     */
    public static <T> T json2JavaObject(JSONObject json, Class<T> clz) throws Exception {
        JSONObject jsonObject = new JSONObject();
        //修改json的参数名称，
        // 利用  JSON.parseObject(String.valueOf(data), cls);方法对对象进行注入
        T obj = clz.newInstance();
        if(obj==null || json==null){
            return null;
        }
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }
            Alias alias = field.getAnnotation(Alias.class);
            String keyName = field.getName();
            if (alias != null) {
                keyName = alias.value();
            }
            Class<?> fieldClass = field.getType();
            Object val = json.get(keyName);
            //其他实体可以直接注入
            if (val!=null){
                Class<?> aClass = val.getClass();//获取数据的类型, json获取后的数据只有 map 和 list两种情况
                String simpleName = aClass.getSimpleName();
                if (simpleName.equals("LinkedHashMap")){
                    Map val1 = (Map) val;
                    JSONObject jsonObject1 = new JSONObject(val1);
                    Object o =null;
                    if (fieldClass.getSimpleName().equals("JSONObject")){
                        jsonObject.put(field.getName(),jsonObject1);
                    }else {
                        o = json2JavaObject(jsonObject1, fieldClass);
                        jsonObject.put(field.getName(),o);
                    }
                }
                else if (simpleName.equals("ArrayList")){
                    List val1 = (List) val;
                    JSONArray array = new JSONArray(val1);
                    List list = new ArrayList<>();
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        Type[] types = ((ParameterizedTypeImpl) field.getGenericType()).getActualTypeArguments();
                        Type type = types[0];
                        String typeName = type.getTypeName();
                        //反射获取list<T>中对象
                        Class cls = Class.forName(typeName);
                        Object o = json2JavaObject(jsonObject1, cls);
                        list.add(o);
                    }
                    jsonObject.put(field.getName(),list);
                }
                else if (simpleName.equals("JSONObject")){
                    JSONObject val1 = (JSONObject) val;
                    jsonObject.put(field.getName(),val1);
                }
                else {
                    jsonObject.put(field.getName(),val);
                }
            }
        }
        return JSON.parseObject(String.valueOf(jsonObject), clz);
    }

}
