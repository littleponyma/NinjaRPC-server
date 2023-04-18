package com.pony.ninjarpcserver.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pony.ninjarpcserver.controller.TaskController;
import com.pony.ninjarpcserver.socket.WebSocketSever;
import com.pony.ninjarpcserver.utils.MessageUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/common/api/v1")
public class CommonApi {
    private TaskController taskController = new TaskController();

    /***
     *
     * @param msg 一个json，里面可以任意diy xposed的api参数
     * @return 固定数据
     */
    @RequestMapping(value = "/callAll",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String commandAllVoid(String msg) {
        taskController.pushTaskAllDevice(msg);
        return MessageUtils.sendErroMsg(0, "执行成功");
    }

    /***
     *
     * @param msg 一个json，里面可以任意diy xposed的api参数
     * @param did 设备id
     * @return 固定数据
     */
    @RequestMapping(value = "/call",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String command2DeviceVoid(String msg, String did) {
        taskController.pushTask2Device(did, msg);
        return MessageUtils.sendErroMsg(0, "执行成功");
    }

    /***
     *
     * @param msg 一个json，里面可以任意diy xposed的api参数
     * @param did 设备id
     * @return 固定数据
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/call obj",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String command2DeviceObj(String msg, String did) throws ExecutionException, InterruptedException {
        String uuid = UUID.randomUUID().toString();
        JSONObject jsonObject = JSON.parseObject(msg);
        jsonObject.put("message_id", uuid);
        taskController.pushTask2Device(did, jsonObject.toJSONString());
        return taskController.waitMessage(uuid).get();
    }

    /**
     * 查看设备在线数量
     * @return 返回存储设备信息的map
     */
    @RequestMapping(value = "/online",produces = "application/json;charset=UTF-8")
    public Object queryDeviceOnline() {
        return WebSocketSever.getDeviceList();
    }
}
