package com.itisamazing.client.service;

import com.itisamazing.common.Message;
import com.itisamazing.common.MessageType;

import java.io.*;

/**
 * 该类完成文件传输服务
 */
public class FileClientService {

    /**
     * 该方法表示单独发送文件给另一个用户
     * @param src 源文件
     * @param des 文件要发送到的位置
     * @param receiver 接收方
     * @param sender 发送方
     */
    public void privateFileTransfer(String src, String des, String receiver, String sender) {
        // 读取src文件
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_FILE_MES);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSrc(src);
        message.setDes(des);

        // 读取文件
        // 创建一个文件输入流
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];

        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes); // 将src文件存入到程序的字节数组
            // 将字节数组重新设置回给message
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // 打印提示信息
        System.out.println("\n" + sender + "给" + receiver + "发送了文件到" + des);

        // 转发文件给服务器端, 等待服务器端处理
        ClientConnectServerThread senderThread = ClientConnectServerThreadManager.getThread(sender);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(senderThread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
