package cn.style.socket.chat;

import android.text.TextUtils;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by xiajun on 2017/3/10.
 */

public class SocketUtil {
    private final static String TAG = "SocketUtil";

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

    /**
     * 发送文本
     *
     * @param body
     */
    public static void sendMsg(final String body) {
        if (TextUtils.isEmpty(body)) {
            Log.e(TAG, "message not be empty!");
            return;
        }
       new Thread(new Runnable() {
           @Override
           public void run() {
               Socket socket = null;
               DataOutputStream dataOutputStream = null;
               DataInputStream dataInputStream = null;
               try {
                   socket = new Socket();
                   socket.connect(new InetSocketAddress(SOCKET_HOST, SOCKET_PORT));
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
                   socket.shutdownOutput();

                   dataInputStream = new DataInputStream(socket.getInputStream());
                   // read result length value
                   byte[] resultLengthBytes = new byte[4];
                   dataInputStream.read(resultLengthBytes, 0, 1);
                   int resultLength = resultLengthBytes[0];//bytesToInt(resultLengthBytes, 0);
                   Log.e(TAG, "result length==" + resultLength);
                   // read result
                   byte[] resultBytes = new byte[resultLength];
                   dataInputStream.read(resultBytes, 0, resultLength);
                   socket.shutdownInput();
                   socket.close();
                   String result = new String(resultBytes);
                   Log.e(TAG, "result：" + result);

               } catch (IOException e) {
                   e.printStackTrace();
               } finally {
                   try {
                       if (dataOutputStream != null)
                           dataOutputStream.close();
                       if (dataInputStream != null)
                           dataInputStream.close();
                       if (socket != null)
                           socket.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
       }).start();
    }

    /**
     * 发送文件
     *
     * @param path
     */
    public static void sendFile(String path) {
        final File file = new File(path); // 要传输的文件路径
        if (!file.exists()) {
            Log.e(TAG, "文件不存在");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                DataOutputStream dataOutputStream = null;
                FileInputStream fileInputStream = null;
                DataInputStream dataInputStream = null;

                try {
                    long fileLength = file.length();
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(SOCKET_HOST, SOCKET_PORT));
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    int fileNameLength = file.getName().getBytes().length;
                    Log.e(TAG, "文件名字节数==" + fileNameLength);
                    dataOutputStream.write(fileNameLength);
                    dataOutputStream.write(file.getName().getBytes());

                    fileInputStream = new FileInputStream(file);
                    int length;
                    double sumL = 0;
                    byte[] sendBytes = new byte[1024];
                    while ((length = fileInputStream.read(sendBytes, 0, sendBytes.length)) > 0) {
                        sumL += length;
                        Log.e(TAG, "已传输：" + ((sumL / fileLength) * 100) + "%");
                        dataOutputStream.write(sendBytes, 0, length);
                        dataOutputStream.flush();//刷新数据输出流。强制将任何缓冲数据写入到流里面。
                    }
                    socket.shutdownOutput();

                    dataInputStream = new DataInputStream(socket.getInputStream());
                    // read result length value
                    byte[] resultLengthBytes = new byte[4];
                    dataInputStream.read(resultLengthBytes, 0, 1);
                    int resultLength = resultLengthBytes[0];//bytesToInt(resultLengthBytes, 0);
                    Log.e(TAG, "result length==" + resultLength);
                    // read result
                    byte[] resultBytes = new byte[resultLength];
                    dataInputStream.read(resultBytes, 0, resultLength);
                    socket.shutdownInput();
                    socket.close();
                    String result = new String(resultBytes);
                    Log.e(TAG, "result：" + result);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (dataOutputStream != null)
                            dataOutputStream.close();
                        if (dataInputStream != null)
                            dataInputStream.close();
                        if (fileInputStream != null)
                            fileInputStream.close();
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24) | ((src[offset + 1] & 0xFF) << 16) | ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
        return value;
    }
}
