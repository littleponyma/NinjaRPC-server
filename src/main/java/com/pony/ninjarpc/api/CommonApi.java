package com.pony.ninjarpc.api;

import com.pony.ninjarpc.socket.WebSocketSever;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/common/api/v1")
public class CommonApi {
    /**
     * 查看设备在线数量
     *
     * @return 返回存储设备信息的map
     */
    @RequestMapping(value = "/dy_online", produces = "application/json;charset=UTF-8")
    public Object queryDYDeviceOnline() {
        return WebSocketSever.getDYDeviceList();
    }

    @RequestMapping(value = "/hs_online", produces = "application/json;charset=UTF-8")
    public Object queryHSDeviceOnline() {
        return WebSocketSever.getHSDeviceList();
    }
}
