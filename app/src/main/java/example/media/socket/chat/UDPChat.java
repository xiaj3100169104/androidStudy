package example.media.socket.chat;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import example.media.socket.ThreadPoolUtil;

/**
 * Created by xiajun on 2017/4/12.
 */

public class UDPChat {
    private final static String TAG = "UDPChat";

    private static int PORT_LISTEN = 7654;
    private static int PORT_TARGET = 4567;
    private static String HOST_TARGET = "192.168.1.102";
    private static DatagramSocket sender;

    public static void send(String msg) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName(HOST_TARGET);
                    String str = "hello";
                    byte data[] = str.getBytes();
                    // 创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
                    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, PORT_TARGET);
                    // 调用socket对象的send方法，发送数据
                    sender.send(packet);
                    //socket.setReuseAddress()
                    //close();
                    //socket.disconnect();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public static void init() {
        try {
            sender = new DatagramSocket();
            startListen();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void startListen() {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket(PORT_LISTEN);
                    while (true) {
                        byte data[] = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);//阻塞
                        String result = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        String hostAddress = packet.getAddress().getHostAddress();
                        int port = packet.getPort();
                        Log.d(TAG, hostAddress + ":" + port + "--->" + result);
                        //send(socket.getInetAddress().getHostAddress(), socket.getPort(), result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    close();
                }
            }

            private void close() {
                if (sender != null && !sender.isClosed())
                    sender.close();
            }
        });
    }
}
