package com.itisamazing.common;

import java.io.Serializable;

/**
 * 表示服务端和客户端通信时的消息对象
 */
public class Message implements Serializable {
    private String sender; // 发送方
    private String receiver; // 接收方
    private String content; // 消息内容
    private String time; // 发送时间
    private String msgType; // 消息类型

    // 进行扩展和文件相关的成员
    private byte[] fileBytes;
    private int fileLen = 0;
    private String des; // 文件发送目的地
    private String src; // 原文件地址

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
