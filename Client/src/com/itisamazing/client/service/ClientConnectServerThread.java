package com.itisamazing.client.service;

import com.itisamazing.common.Message;
import com.itisamazing.common.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 该类表示一个客户端连接服务器的线程
 * 每个线程持有一个Socket对象 用于与服务器进行通信
 */
public class ClientConnectServerThread extends Thread {

    // 该线程需要持有Socket
    private Socket socket;

    /**
     * 构造方法 接受一个Socket对象并初始化该线程
     * @param socket 接受一个socket对象
     */
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * 重写的run方法 表示客户端与服务器之间通信的逻辑
     * 在线程启动时执行; 启动时, 线程会一直试图从socket中读取Message对象
     * 如果没有数据, 线程会阻塞在ois.readObject()处
     */
    @Override
    public void run() {
        // 因为Thread需要在后台和服务器通信, 因此我们while循环
        while (true) {
            System.out.println("客户端线程, 等待从服务器端发送的消息...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 返回Message对象
                // 如果读取不到数据, 线程阻塞
                Message message = (Message) ois.readObject();

                // 判断消息类型, 以作后续处理
                if (message.getMsgType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    // 如果读取到的是服务器端返回在线列表
                    // 取出在线列表信息
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("====显示在线列表====");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户: " + onlineUsers[i]);
                    }

                } else if (message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)) {
                    // 接收到来自服务器端的普通消息, 说明是其他用户的私聊消息
                    System.out.println("\n" + message.getContent());

                } else if (message.getMsgType().equals(MessageType.MESSAGE_TO_ALL)) { // 群聊消息
                    System.out.println("\n" + message.getContent());

                } else if (message.getMsgType().equals(MessageType.MESSAGE_FILE_MES)) { // 如果是文件消息
                    System.out.println(message.getSender() + "给" + message.getReceiver() + "发送了文件到" + message.getDes());
                    // 取出message字节数组, 通过字节输出流写入到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDes());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("保存文件成功");

                }

                else {
                    System.out.println("暂时不处理");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
