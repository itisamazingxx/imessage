package com.itisamazing.client.service;

import java.util.HashMap;

/**
 * 该类管理所有和服务器端通讯的线程
 */
public class ClientConnectServerThreadManager {
    // 用来保存线程的集合
    // key为userId, 线程为该用户下对应的线程
    private static HashMap<String, ClientConnectServerThread> map = new HashMap<>();

    /**
     * 将线程加入到集合中
     * @param userId 用户id
     * @param thread 客户端连接服务器端的单个线程
     */
    public static void addThread(String userId, ClientConnectServerThread thread) {
        map.put(userId, thread);
    }

    /**
     * 查找线程
     * @param userId 用户id
     * @return 此用户id的对应线程
     */
    public static ClientConnectServerThread getThread(String userId) {
        if (map.containsKey(userId)) {
            return map.get(userId);
        }
        return null;
    }


}
