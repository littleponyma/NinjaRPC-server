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

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/aweme/api/v1")
public class AwemeApi {
    private TaskController taskController = new TaskController();

    @RequestMapping(value = "/sign6", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @Async
    public CompletableFuture<String> sign6(@RequestBody String json) {
        if (json == null) {
            return CompletableFuture.completedFuture(Result.parameterErro());
        }
        if (WebSocketSever.getDYDeviceList().isEmpty()) {
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
        jsonObject.put("method", "sign6");
        taskController.pushTaskOne(jsonObject);
        return taskController.waitMessage(uuid, "sign6");
    }


    @RequestMapping(value = "/sign6auth", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @Async
    public CompletableFuture<String> sign6Auth(@RequestBody String json) {
        if (json == null) {
            return CompletableFuture.completedFuture(Result.parameterErro());
        }
        if (WebSocketSever.getDYDeviceList().isEmpty()) {
            throw new RuntimeException("");
//            return CompletableFuture.completedFuture(Result.noneDevice());
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
        JSONObject header = jsonObject.getJSONObject("header");
//        header.put("multi_login", ("1"));
        header.put("sdk-version", ("2"));
        header.put("x-bd-kmsv", ("1"));
        header.put("x-vc-bdturing-sdk-version", ("3.1.0.cn"));
        header.put("passport-sdk-version", ("20374"));
        jsonObject = new JSONObject();
        jsonObject.put("uuid", uuid);
        jsonObject.put("url", url);
        jsonObject.put("did", did);
        jsonObject.put("iid", iid);
        jsonObject.put("header", header);
        jsonObject.put("method", "sign6");
        taskController.pushTaskOne(jsonObject);
        return taskController.waitMessage(uuid, "sign6");
    }

}
