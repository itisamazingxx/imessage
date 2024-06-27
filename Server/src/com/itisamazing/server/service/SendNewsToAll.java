package com.itisamazing.server.service;

import com.itisamazing.server.util.Utility;
import com.itisamazing.common.Message;
import com.itisamazing.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 单独线程处理服务器推送消息
 */
public class SendNewsToAll implements Runnable {

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        // 为了可以推送多次新闻, 使用while true
        while (true) {
            System.out.println("请输入服务器要推送的新闻/消息");
            String news = Utility.readString(100);

            if ("exit".equals(news)) {
                break;
            }

            // 构建一个消息, 群发消息
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setMsgType(MessageType.MESSAGE_TO_ALL);
            System.out.println("服务器对所有人说" + news);

            // 遍历当前所有的通讯线程
            HashMap<String, ServerConnectClientThread> map = ServerConnectClientThreadManager.getMap();
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String onlineUserId = iterator.next().toString();
                ServerConnectClientThread onlineTread = ServerConnectClientThreadManager.getThread(onlineUserId);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(onlineTread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
