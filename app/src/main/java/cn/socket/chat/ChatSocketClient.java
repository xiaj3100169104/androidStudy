package cn.socket.chat;

import android.text.TextUtils;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by xiajun on 2017/3/10.
 */

public class ChatSocketClient {
    private final static String TAG = "ChatSocketClient";

    /**
     * 主机地址
     */
    private final static String SOCKET_HOST = "192.168.1.100";
    /**
     * socket端口号
     */
    private final static int SOCKET_PORT = 1314;
    private final static String HEADER = "协议：dcdcece,版本：1.0";

    public static String getHeader() {
        return HEADER;
    }
    Socket socket = null;

    public ChatSocketClient() {
        init();
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream dataInputStream = null;
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(SOCKET_HOST, SOCKET_PORT));
                   /* while (true){
                        InputStream inputStream = socket.getInputStream();
                        System.out.println("有新消息");
                        dataInputStream = new DataInputStream(inputStream);
                        // read result length value
                        byte[] resultLengthBytes = new byte[4];
                        dataInputStream.read(resultLengthBytes, 0, 1);
                        int resultLength = resultLengthBytes[0];//bytesToInt(resultLengthBytes, 0);
                        Log.e(TAG, "result length==" + resultLength);
                        // read result
                        byte[] resultBytes = new byte[resultLength];
                        dataInputStream.read(resultBytes, 0, resultLength);
                        String result = new String(resultBytes);
                        Log.e(TAG, "result：" + result);
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                     if (dataInputStream != null)
                            dataInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    /**
     * 发送文本
     *
     * @param body
     */
    public void sendMsg(final String body) {
        if (TextUtils.isEmpty(body)) {
            Log.e(TAG, "message not be empty!");
            return;
        }
       new Thread(new Runnable() {
           @Override
           public void run() {
               DataOutputStream dataOutputStream = null;
               try {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                   byte[] headerBytes = getHeader().getBytes();
                   int headerLength = headerBytes.length;
                   Log.e(TAG, "header length==" + headerLength);
                   dataOutputStream.write(headerLength);
                   dataOutputStream.write(headerBytes);

                   byte[] bodyBytes = body.getBytes();
                   int bodyLength = bodyBytes.length;
                   Log.e(TAG, "body length==" + bodyLength);
                   dataOutputStream.write(bodyLength);
                   dataOutputStream.write(bodyBytes);
                   dataOutputStream.flush();//刷新数据输出流。强制将任何缓冲数据写入到流里面。
                   //socket.shutdownOutput();

               } catch (IOException e) {
                   e.printStackTrace();
               } finally {
                 
               }
           }
       }).start();
    }
}
