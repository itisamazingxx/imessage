package com.itisamazing.client.service;

import com.itisamazing.common.Message;
import com.itisamazing.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageClientService {


    /**
     * 准备向服务器端发送消息, 请求私聊另一个用户
     * @param receiver
     * @param content
     */
    public void privateChat(String receiver, String sender, String content) {
        // 创建新的message对象
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setMsgType(MessageType.MESSAGE_COMM_MES);
        message.setTime(new java.util.Date().toString());
        System.out.println(sender + "对" + receiver + "说" + content);

        // 发送给服务器端
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ClientConnectServerThreadManager.getThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 发送消息给每一个在线用户
     * @param sender
     * @param content
     */
    public void sendGroupChat(String sender, String content) {
        // 创建新的message对象
        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setMsgType(MessageType.MESSAGE_TO_ALL);
        message.setTime(new java.util.Date().toString());
        System.out.println(sender + "对大家说" + content);

        // 发送给服务器端
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ClientConnectServerThreadManager.getThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
