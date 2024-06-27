package com.itisamazing.common;

/**
 * 表示消息类型
 */
public interface MessageType {
    // 1. 在接口中定义了一些常量
    // 2. 不同常量的值表示不同的消息类型
    String MESSAGE_LOGIN_SUCCEED = "1"; // 表示登录成功
    String MESSAGE_LOGIN_FAILED = "2"; // 表示登录失败
    String MESSAGE_COMM_MES = "3"; // 普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4"; // 获取在线登录列表
    String MESSAGE_RET_ONLINE_FRIEND = "5"; // 返回在线登录列表
    String MESSAGE_CLIENT_EXIT = "6"; // 客户端请求退出
    String MESSAGE_TO_ALL = "7"; // 群聊消息
    String MESSAGE_FILE_MES = "8"; // 发送文件
}
