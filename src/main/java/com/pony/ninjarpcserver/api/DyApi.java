package com.pony.ninjarpcserver.api;

import com.pony.ninjarpcserver.controller.TaskController;
import com.pony.ninjarpcserver.socket.WebSocketSever;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/dy/api/v1")
public class DyApi {

    private TaskController taskController= new TaskController();

    @RequestMapping(value = "/search",produces = "application/json;charset=UTF-8")
    public Object searchVideoList(String keyword, String cursor, String filtertype, String time,String did) throws ExecutionException, InterruptedException {
        String uuid = UUID.randomUUID().toString();
        HashMap<String,Object> paramMap=new HashMap<>();
        paramMap.put("keyword",keyword);
        paramMap.put("cursor",cursor);
        paramMap.put("filtertype",filtertype);
        paramMap.put("time",time);
        paramMap.put("message_id",uuid);
        TaskController.getInstance().pushTask2Device(did,paramMap);
        return taskController.waitMessage(uuid).get();
    }
}
