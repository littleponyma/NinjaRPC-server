package com.pony.ninjarpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GeneralCmd {
    public static void main(String[] args) {
        JSONObject data=new JSONObject();
        data.put("className","android.util.Log");
        data.put("method","e");
        JSONArray jsonArray=new JSONArray();
        jsonArray.add("NinjaRPC");
        jsonArray.add("test");
        data.put("params",jsonArray);
        JSONObject global = new JSONObject();
        global.put("data",data);
        System.out.println(JSON.toJSONString(global));
    }
}
