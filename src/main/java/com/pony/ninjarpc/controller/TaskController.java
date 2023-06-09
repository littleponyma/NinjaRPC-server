package com.pony.ninjarpc.controller;

import com.alibaba.fastjson.JSON;
import com.pony.ninjarpc.common.Constant;
import com.pony.ninjarpc.model.Result;
import com.pony.ninjarpc.socket.WebSocketSever;
import com.pony.ninjarpc.utils.FileUtils;
import com.pony.ninjarpc.utils.VLog;
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

    public void pushTaskOne(Object obj) {
        WebSocketSever.sendMessage(JSON.toJSONString(obj));
    }

    @Async("doSomethingExecutor")
    public CompletableFuture<String> waitMessage(String id, String type) {
        int time = 0;
        do {
            try {
                time += 1;
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //回传的数据会存在jsonMap中，每秒读一次

            String data = null;
            if ("sign4".equals(type)) {
                data = FileUtils.readToString(Constant.DY_SIGN4 + filesdf.format(System.currentTimeMillis()) + File.separator + id + ".txt");
            }else if ("sign6".equals(type)) {
                data = FileUtils.readToString(Constant.DY_SIGN6 + filesdf.format(System.currentTimeMillis()) + File.separator + id + ".txt");
            }
            if (data != null) {
                //读取成功后则移除数据并通过接口返回
                VLog.e("success");
                return CompletableFuture.completedFuture(data);
            }
        } while (time <= 5000);
        return CompletableFuture.completedFuture(Result.timeoutErro());
    }

}
