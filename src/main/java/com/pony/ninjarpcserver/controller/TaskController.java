package com.pony.ninjarpcserver.controller;

import com.alibaba.fastjson.JSON;
import com.pony.ninjarpcserver.common.Constant;
import com.pony.ninjarpcserver.socket.WebSocketSever;
import com.pony.ninjarpcserver.utils.FileUtils;
import com.pony.ninjarpcserver.utils.MessageUtils;
import com.pony.ninjarpcserver.utils.VLog;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;

public class TaskController {
    private static TaskController instance;
    public SimpleDateFormat filesdf = new SimpleDateFormat("yyyy-MM-dd");

    public static TaskController getInstance() {
        if (instance == null) {
            synchronized (TaskController.class) {
                instance = new TaskController();
            }
        }
        return instance;
    }

    public void pushTask2Device(String deviceId, Object obj) {
        WebSocketSever.sendMessageByDeviceId(deviceId, JSON.toJSONString(obj));
    }

    public void pushTaskAllDevice(Object obj) {
        WebSocketSever.sendAllMessage(JSON.toJSONString(obj));
    }


    @Async("doSomethingExecutor")
    public CompletableFuture<String> waitMessage(String id) {
        int time = 0;
        do {
            try {
                time += 1;
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //回传的数据会存在jsonMap中，每秒读一次

            String data = FileUtils.readToString(Constant.CACHE_PATH + filesdf.format(System.currentTimeMillis()) + File.separator + id + ".txt");
            if (data != null) {
                //读取成功后则移除数据并通过接口返回
                VLog.e("success "+id);
                return CompletableFuture.completedFuture(data);
            }
        } while (time <= 1500);
        return CompletableFuture.completedFuture(MessageUtils.sendErroMsg(-1001, "请求超时请重试"));
    }
}
