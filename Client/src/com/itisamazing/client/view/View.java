package com.itisamazing.client.view;

import com.itisamazing.client.service.FileClientService;
import com.itisamazing.client.service.MessageClientService;
import com.itisamazing.client.service.UserClientService;
import com.itisamazing.client.util.Utility;

/**
 * 客户端界面主菜单
 */
public class View {

    private boolean loop = true; // 控制是否显示菜单
    private String key = ""; // 用来接收用户的键盘输入
    private UserClientService userClientService = new UserClientService(); // 用于登录服务器, 用户注册等
    private MessageClientService messageClientService = new MessageClientService(); // 用于收发消息, 群聊私聊等服务
    private FileClientService fileClientService = new FileClientService(); // 文件传输类型服务

    // 测试
    public static void main(String[] args) {
        new View().mainMenu();
    }

    /**
     * 显示主菜单
     */
    private void mainMenu() {

        while (loop) {

            System.out.println("-----多用户通讯系统-----");
            System.out.println("\t 1 用户登录");
            System.out.println("\t 9 退出系统");
            System.out.print("请输入你的选择: ");

            key = Utility.readString(1); // 读取用户键盘输入

            // 根据用户输入处理不同的逻辑
            switch (key) {

                case "1":
                    // 主界面后的下一层, 用户输入用户名密码
                    System.out.print("请输入用户id: ");
                    String userId = Utility.readString(50);
                    System.out.print("请输入用户密码: ");
                    String password = Utility.readString(50);

                    // 服务器端验证user对象是否合法
                    if (userClientService.checkUser(userId, password)) {
                        // 如果登录成功, 到达通讯系统二级菜单
                        System.out.println("用户" + userId + "登陆成功");

                        while (loop) {

                            System.out.println("-----多用户通讯系统二级菜单<用户" + userId + ">登陆成功-----");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择: ");

                            key = Utility.readString(1); // 读取用户输入

                            switch (key) {
                                case "1":
                                    System.out.println("-----显示列表ing-----");
                                    userClientService.getOnlineFriend();
                                    break;
                                case "2":
                                    System.out.print("请输入想要群发送的消息: ");
                                    String groupChat = Utility.readString(100);
                                    messageClientService.sendGroupChat(userId, groupChat);
                                    break;
                                case "3":
                                    System.out.print("请输入你想聊天的用户: ");
                                    String receiverId = Utility.readString(50);
                                    System.out.print("请输入要发送的消息: ");
                                    String content = Utility.readString(100);
                                    messageClientService.privateChat(receiverId, userId, content);
                                    break;
                                case "4":
                                    System.out.println("请输入你想把文件发送给的用户: ");
                                    String id = Utility.readString(50);
                                    System.out.println("请输入你想要发送的文件地址: ");
                                    String src = Utility.readString(100);
                                    System.out.println("请输入对方要接受的路径: ");
                                    String des = Utility.readString(100);
                                    fileClientService.privateFileTransfer(src, des, id, userId);
                                    break;
                                case "9":
                                    System.out.println("退出系统ing");
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    } else {
                        // 登录失败
                        System.out.println("用户登录失败....");
                    }
                    break;
                case "9":
                    System.out.println("退出系统ing");
                    loop = false;
                    break;

            }
        }

    }
}
