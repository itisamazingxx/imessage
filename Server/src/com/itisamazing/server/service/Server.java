package com.itisamazing.server.service;

import com.itisamazing.common.Message;
import com.itisamazing.common.MessageType;
import com.itisamazing.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 此类代表通讯系统的服务端
 * 监听端口9999, 等待客户端的连接, 并保持通讯
 */
public class Server {

    private ServerSocket serverSocket; // 用来在服务器端监听9999端口

    // 存放所有的合法用户
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    static {
        validUsers.put("100", new User("100", "123"));
        validUsers.put("200", new User("200", "123"));
        validUsers.put("300", new User("300", "123"));
        validUsers.put("400", new User("400", "123"));
    }

    /**
     * 验证登录用户是否存在于当前服务器端系统
     * @param userId
     * @param pwd
     * @return 如果用户存在, 返回true
     */
    private boolean checkUser(String userId, String pwd) {
        User user = validUsers.get(userId);
        if (user == null) {
            return false;
        }
        if (user.getPassword().equals(pwd )) {
            return true;
        }
        return false;
    }


    public Server() {
        try {
            System.out.println("服务器端在9999端口监听");
            new Thread(new SendNewsToAll()).start(); // 开启推送消息的线程
            this.serverSocket = new ServerSocket(9999);// 首先监听端口

            while (true) { // 当和某个客户端连接时, 会继续监听, while循环

                Socket socket = serverSocket.accept(); // 等待客户端连接请求
                // 如果没有客户端连接会阻塞在此处

                // 得到socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 读取客户端发送的User对象
                User user = (User) ois.readObject();
                // 创建要向客户端发送的输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message = new Message();

                // 在服务端(数据库)验证用户是否合法
                // 此通讯软件不另配置数据库, 我们只用一个合法user做测试
                if (checkUser(user.getUserId(), user.getPassword())) {
                    // 验证通过, 登录成功
                    // 向客户端发送Message对象
                    message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    // 需要启动一个新线程用来保持跟客户端的通讯, 该线程需要持有socket
                    ServerConnectClientThread thread = new ServerConnectClientThread(socket, user.getUserId());
                    // 启动该线程
                    thread.start();
                    // 把该线程对象放入集合中进行管理
                    ServerConnectClientThreadManager.addThread(user.getUserId(), thread);
                } else { // 登录失败
                    // 设置消息类型
                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAILED);
                    // 也要向客户端返回失败消息对象
                    oos.writeObject(message);
                    // 关闭socket
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 如果服务器端退出了while循环
            // 说明不再持续监听, 关闭server socket
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
