package com.itisamazing.server.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类管理所有和客户端通讯的线程
 */
public class ServerConnectClientThreadManager {
    // 用来保存服务器下的所有线程对象
    private static HashMap<String, ServerConnectClientThread> map = new HashMap<>();

    /**
     * 将线程加入到集合中
     * @param userId 用户id
     * @param thread 服务器跟客户端通讯的单个线程
     */
    public static void addThread(String userId, ServerConnectClientThread thread) {
        map.put(userId, thread);
    }

    public static void removeThread(String userId) {
        map.remove(userId);
    }

    public static HashMap<String, ServerConnectClientThread> getMap() {
        return map;
    }

    /**
     * 查找线程
     * @param userId 用户id
     * @return 此用户id的对应线程
     */
    public static ServerConnectClientThread getThread(String userId) {
        if (map.containsKey(userId)) {
            return map.get(userId);
        }
        return null;
    }

    /**
     * 返回当前客户端管理下的在线用户列表
     */
    public static String getOnlineUser() {
        // 遍历当前集合下的keys
        Iterator<String> iterator = map.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }
}
