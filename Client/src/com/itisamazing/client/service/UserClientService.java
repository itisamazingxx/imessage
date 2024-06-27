package com.itisamazing.client.service;

import com.itisamazing.common.Message;
import com.itisamazing.common.MessageType;
import com.itisamazing.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 该类完成用户登录验证, 用户注册等方法
 */
public class UserClientService {

    private User user = new User();
    private Socket socket;

    /**
     * 根据用户userId, password在服务端确认用户是否存在
     * @param userId 用户id
     * @param pwd 用户密码
     * @return 用户验证成功返回True, otherwise False
     */
    public boolean checkUser(String userId, String pwd) {
        boolean check = false; // 初始化用户验证为false
        // 创建新的User对象
        user.setUserId(userId);
        user.setPassword(pwd);
        // 连接到服务器端, 发送User对象
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
            // 得到ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // 发送User对象
            oos.writeObject(user);
            // 读取从服务器端回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            // 如果用户验证成功
            if (message.getMsgType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
                // 启动一个和服务器保持通讯的线程, 让线程持有socket
                ClientConnectServerThread thread = new ClientConnectServerThread(socket);
                // 启动客户端的线程
                thread.start();
                // 将线程放入集合中管理, 方便扩展
                ClientConnectServerThreadManager.addThread(userId, thread);
                check = true;
            } else { // 登录失败
                // 如果登录失败, 无法启动客户端和服务器端通信的线程 (但有socket)
                // 关闭socket
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return check;
    }


    /**
     * 发送请求
     * 向服务器发送一个Message对象, 使客户端返回所需要的在线用户列表
     * @return
     */
    public void getOnlineFriend() {
        // 创建一个Message对象
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());
        // 发送给客户端
        try {
            // 通过管理线程的集合, 使用userId, 得到当前userId下对应的线程
            ClientConnectServerThread thread = ClientConnectServerThreadManager.getThread(user.getUserId());
            Socket socket = thread.getSocket();
            // 得到对应socket的outputStream, 把message放入oos中发送
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message); // 向服务端申请在线用户列表
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 退出客户端, 向服务器发送一个退出系统的message对象
     */
    public void logout() {
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId()); // 指定message对象的客户端名
        // 发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserId() + "退出系统");
            // 退出系统
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
