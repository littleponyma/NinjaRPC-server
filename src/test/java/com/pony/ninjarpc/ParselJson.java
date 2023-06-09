package com.pony.ninjarpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class ParselJson {

    public static void main(String[] args) {
        String json="{\"url\":\"https://webcast.huoshan.com/webcast/room/enter/?luckydog_base=kL61JjwrA3bjhwtQjrwT961MjbksURDc0Uq888cLt9ncF8mB9SXVBLWXcuLKGO-uGa6vrg000crQEff5MJwC2PtmgiTh3j-NZYvMgU-89zoJhurqVmhh3gn6uBlK3XGa1nhivWaJ19Plgpik_9j3mgVj6435M6FPpM8b-86akxk&luckydog_token=vGwG3eItLthTKN36zGZGW5WUJYz3bVfs6C2AeoR9YSL4Vg18kn_A_ibtUrvmh3_gbQx1aWaASOmmwf7p5x2qCA&luckydog_data=tVrggETFyXUdT7OrpLX1rg&webcast_sdk_version=2730&webcast_language=zh&webcast_locale=zh_CN&webcast_gps_access=2&current_network_quality_info=%7B%7D&is_pad=false&is_android_pad=0&is_landscape=false&carrier_region=CN&live_sdk_version=150300&need_personal_recommend=1&iid=1269275336119278&device_id=2905346602711534&ac=wifi&channel=360_1112_0322&aid=1112&app_name=live_stream&version_code=150300&version_name=15.3.0&device_platform=android&os=android&ssmix=a&device_type=2203121C&device_brand=Xiaomi&language=zh&os_api=28&os_version=9&manifest_version_code=150300&resolution=1080*1920&dpi=280&update_version_code=15030003&_rticket=1684314321355&tab_mode=3&client_version_code=150300&mcc_mnc=46000&hs_location_permission=0&cpu_support64=true&host_abi=armeabi-v7a&rom_version=XIAOMI_unknown&cdid=4866ebb5-c3ff-4549-815f-bd9bd5956da3&new_nav=0&screen_width=617&ws_status=ConnectionState%7BState%3D1%7D&settings_version=24&last_update_time=1684314315741&cpu_model=placeholder&ts=1684314321\",\"did\":\"2905346602711534\",\"iid\":\"1269275336119278\",\"header\":{\"x-ss-stub\":\"1908D20BF6EDE89CBB15A01468FC65D6\",\"x-ss-xxt\":\"1908D20BF6EDE89CBB15A01468FC65D6\"}}";
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject header = jsonObject.getJSONObject("header");
        System.out.println(jsonObject.toJSONString());
        Iterator<String> iterator = header.keySet().iterator();
        HashMap<String,Object> map=new HashMap<>();
        while (iterator.hasNext()){
            String next = iterator.next();
            map.put(next, Collections.singletonList(header.getString(next)));
        }
        System.out.println(map);

    }
}
