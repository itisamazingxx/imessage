package com.itisamazing.common;

import java.io.Serializable;

/**
 * 在网络通信中, 客户端用户信息已对象的形式传输给服务端
 * 此类表示一个用户的信息
 * 为了进行网络间的传输, User类需要进行序列化操作
 */
public class User implements Serializable {

    private static final long serialVersionUTD = 1L;
    private String userId; // 用户id
    private String password; // 用户密码

    public User() {

    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
