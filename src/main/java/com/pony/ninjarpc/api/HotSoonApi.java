package com.pony.ninjarpc.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pony.ninjarpc.controller.TaskController;
import com.pony.ninjarpc.model.Result;
import com.pony.ninjarpc.socket.WebSocketSever;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/hot_soon/api/v1")
public class HotSoonApi {
    private TaskController taskController = new TaskController();

    @RequestMapping(value = "/sign4", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @Async
    public CompletableFuture<String> sign4(@RequestBody String json) throws ExecutionException, InterruptedException {
        if (json == null) {
            return CompletableFuture.completedFuture(Result.parameterErro());
        }
        if (WebSocketSever.getHSDeviceList().isEmpty()) {
            return CompletableFuture.completedFuture(Result.noneDevice());
        }
        JSONObject jsonObject = JSON.parseObject(json);
        if (!jsonObject.containsKey("url") || jsonObject.getString("url") == null) {
            return CompletableFuture.completedFuture(Result.parameterErro());
        }
        if (!jsonObject.containsKey("did") || jsonObject.getString("did") == null) {
            return CompletableFuture.completedFuture(Result.parameterErro());
        }
        if (!jsonObject.containsKey("iid") || jsonObject.getString("iid") == null) {
            return CompletableFuture.completedFuture(Result.parameterErro());
        }
        if (!jsonObject.containsKey("header") || jsonObject.getString("header") == null) {
            return CompletableFuture.completedFuture(Result.parameterErro());
        }
        String uuid = UUID.randomUUID().toString();
        String url = jsonObject.getString("url");
        String did = jsonObject.getString("did");
        String iid = jsonObject.getString("iid");
        String header = jsonObject.getString("header");
        jsonObject = new JSONObject();
        jsonObject.put("uuid", uuid);
        jsonObject.put("url", url);
        jsonObject.put("did", did);
        jsonObject.put("iid", iid);
        jsonObject.put("header", header);
        jsonObject.put("method", "sign4");
        taskController.pushTaskOne(jsonObject);
        return taskController.waitMessage(uuid, "sign4");
    }


}
