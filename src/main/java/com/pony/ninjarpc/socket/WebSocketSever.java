package com.pony.ninjarpc.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pony.ninjarpc.common.Constant;
import com.pony.ninjarpc.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/{deviceId}")
@Component
@Slf4j
public class WebSocketSever {
    // session集合,存放对应的session
    private static ConcurrentHashMap<String, Session> hotSoonSessionPool = new ConcurrentHashMap<>();
    // concurrent包的线程安全Set,用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<WebSocketSever> hotSoonWebSocketSet = new CopyOnWriteArraySet<>();

    // session集合,存放对应的session
    private static ConcurrentHashMap<String, Session> awemeSessionPool = new ConcurrentHashMap<>();
    // concurrent包的线程安全Set,用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<WebSocketSever> awemeWebSocketSet = new CopyOnWriteArraySet<>();
    public SimpleDateFormat filesdf = new SimpleDateFormat("yyyy-MM-dd");
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    public static void sendMessage(String data) {
        try {
            if (data.contains("sign4")) {
                int index = new Random().nextInt(hotSoonWebSocketSet.size());
                List<WebSocketSever> deviceList = new ArrayList<>(hotSoonWebSocketSet);
                deviceList.get(index).session.getBasicRemote().sendText(data);
            } else {
                int index = new Random().nextInt(awemeWebSocketSet.size());
                List<WebSocketSever> deviceList = new ArrayList<>(awemeWebSocketSet);
                deviceList.get(index).session.getBasicRemote().sendText(data);
            }
        } catch (IOException e) {
            log.error("发消息发生错误：" + e.getMessage(), e);
        }
    }


    public static ConcurrentHashMap<String, Session> getHSDeviceList() {
        return hotSoonSessionPool;
    }

    public static ConcurrentHashMap<String, Session> getDYDeviceList() {
        return awemeSessionPool;
    }

    /**
     * 建立WebSocket连接
     *
     * @param session
     * @param deviceId 设备ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "deviceId") String deviceId) {
        log.info("WebSocket建立连接中,连接用户ID：{}", deviceId);
        try {
            if (deviceId.contains("hotsoon")) {
                Session historySession = hotSoonSessionPool.get(deviceId);
                // historySession不为空,说明已经有人登陆账号,应该删除登陆的WebSocket对象
                if (historySession != null) {
                    hotSoonWebSocketSet.remove(historySession);
                    historySession.close();
                }
            } else {
                Session historySession = awemeSessionPool.get(deviceId);
                // historySession不为空,说明已经有人登陆账号,应该删除登陆的WebSocket对象
                if (historySession != null) {
                    awemeWebSocketSet.remove(historySession);
                    historySession.close();
                }
            }

        } catch (IOException e) {
            log.error("重复登录异常,错误信息：" + e.getMessage(), e);
        }
        // 建立连接
        this.session = session;
        if (deviceId.contains("hotsoon")) {
            hotSoonWebSocketSet.add(this);
            hotSoonSessionPool.put(deviceId, session);
            log.info("建立连接完成,当前hotsoon在线数为：{}", hotSoonWebSocketSet.size());
        } else {
            awemeWebSocketSet.add(this);
            awemeSessionPool.put(deviceId, session);
            log.info("建立连接完成,当前aweme在线数为：{}", awemeWebSocketSet.size());
        }
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        if (awemeWebSocketSet.contains(this)){
            awemeWebSocketSet.remove(this);
            log.info("连接断开,当前在线人数为：{}", awemeWebSocketSet.size());
        }else {
            hotSoonWebSocketSet.remove(this);
            log.info("连接断开,当前在线人数为：{}", hotSoonWebSocketSet.size());
        }
    }

    /**
     * 接收客户端消息
     *
     * @param message 接收的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("收到客户端发来的消息：{}", message);
        JSONObject jsonObject = JSON.parseObject(message);
        String uuid = jsonObject.getString("uuid");
        String method = jsonObject.getString("method");
        if (method.equals("sign4")) {
            FileUtils.saveToFile(Constant.DY_SIGN4 + filesdf.format(System.currentTimeMillis()) + File.separator + uuid + ".txt", message);
        }else  if (method.equals("sign6")) {
            FileUtils.saveToFile(Constant.DY_SIGN6 + filesdf.format(System.currentTimeMillis()) + File.separator + uuid + ".txt", message);
        }
    }

}
