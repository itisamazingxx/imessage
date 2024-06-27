package com.itisamazing.server.service;

import com.itisamazing.common.Message;
import com.itisamazing.common.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类表示一个客户端连接服务器的线程
 * 每个线程持有一个Socket对象, 该类的对象与某个客户端保持通讯
 */
public class ServerConnectClientThread extends Thread {

    private Socket socket;
    private String userId; // 连接到服务端的用户id

    /**
     * 构造方法 初始化Socket对象和用户ID
     * @param socket 连接客户端的Socket对象
     * @param userId 连接到服务端的用户id
     */
    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return this.socket;
    }

    /**
     * 线程运行方法 保持与客户端的通讯
     * 在线程启动时执行 持续接收客户端发送的消息
     */
    @Override
    public void run() {
        while (true) {
            System.out.println("服务器和客户端" + userId + "保持通讯, 服务器等待从客户端发来的内容...");
            try {

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 实际通讯中, 客户端在登陆成功后会发送各种消息(聊天信息, 文件传输请求)
                // 这些消息被封装成Message对象发送到服务器, 等待读取
                Message message = (Message) ois.readObject();

                // 根据message类型做相应的业务处理
                if (message.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    // 如果收到客户端请求拉取在线用户
                    System.out.println(message.getSender() + "请求获取在线用户列表");
                    String onlineUsers = ServerConnectClientThreadManager.getOnlineUser();
                    // 返回的是一个message对象, 返回给客户端
                    Message message2 = new Message();
                    message2.setMsgType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUsers);
                    message2.setReceiver(message.getSender());
                    // 返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);

                } else if (message.getMsgType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    // 如果接收到的请求是退出系统
                    // 获取到当前用户下对应的线程, 然后关闭
                    System.out.println(message.getSender() + "退出系统");
                    ServerConnectClientThreadManager.removeThread(message.getSender());
                    socket.close(); // 关闭连接
                    break; // 退出线程

                } else if (message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)) {
                    // 如果从服务器端接收到的消息是普通私聊消息
                    // 根据当前message拿到receiver的线程(由服务端管理)
                    ServerConnectClientThread receiver = ServerConnectClientThreadManager.getThread(message.getReceiver());
                    // 向该服务器发送私聊消息
                    ObjectOutputStream oos = new ObjectOutputStream(receiver.getSocket().getOutputStream());
                    oos.writeObject(message); // 服务端对消息进行转发

                } else if (message.getMsgType().equals(MessageType.MESSAGE_TO_ALL)) {
                    // 从服务器端接收到群聊消息的发送申请
                    // 需要遍历管理线程的集合, 得到所有线程的socket, 然后把消息进行转发
                    HashMap<String, ServerConnectClientThread> map = ServerConnectClientThreadManager.getMap();

                    // 开始遍历
                    Iterator<String> iterator = map.keySet().iterator();
                    while (iterator.hasNext()) {
                        // 取出在线userid
                        String onlineUserId = iterator.next().toString();
                        if (!onlineUserId.equals(message.getSender())) {
                            // 给除了sender以外的所以在线用户发送消息
                            // 转发
                            ObjectOutputStream oos = new ObjectOutputStream
                                    (map.get(onlineUserId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }

                    }

                } else if (message.getMsgType().equals(MessageType.MESSAGE_FILE_MES)) {
                    // 如果接收到的信息是要传输文件, 需要将文件传给指定客户端
                    ServerConnectClientThread receiverThread = ServerConnectClientThreadManager.getThread(message.getReceiver());
                    // 转发
                    ObjectOutputStream oos = new ObjectOutputStream(receiverThread.getSocket().getOutputStream());
                    oos.writeObject(message);

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
