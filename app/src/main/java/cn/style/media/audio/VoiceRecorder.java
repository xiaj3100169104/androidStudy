package cn.style.media.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import cn.style.media.socket.ThreadPoolUtil;

/**
 * Created by xiajun on 2017/3/19.
 */

public class VoiceRecorder {
    private final static String TAG = "VoiceRecorder";

    private static final int audioSource = MediaRecorder.AudioSource.MIC;//
    private static final int sampleRateInHz = 44100;//Hz，采样频率
    private static final int channelConfig_in = AudioFormat.CHANNEL_IN_MONO;
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int minBufferSize = 0;
    private int minBufferSize2 = 0;

    private AudioRecord mAudioRecord;

    private static final int streamType = AudioManager.STREAM_MUSIC;//
    private static final int sampleRateInHz2 = 44100;//Hz，采样频率
    private static final int channelConfig_out = AudioFormat.CHANNEL_OUT_MONO;
    private static final int audioFormat2 = AudioFormat.ENCODING_PCM_16BIT;
    private static final int mode = AudioTrack.MODE_STREAM;
    private AudioTrack audioTrack;

    private static VoiceRecorder instance;

    public synchronized static VoiceRecorder getInstance() {
        if (instance == null) {
            instance = new VoiceRecorder();
        }
        return instance;
    }

    public void startRecord() {

        //为了方便，这里只录制单声道
        //如果是双声道，得到的数据是一左一右，注意数据的保存和处理
        minBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig_in, audioFormat);
        //音频数据录制实例
        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig_in, audioFormat, minBufferSize);
        mAudioRecord.startRecording();
        minBufferSize2 = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig_out, audioFormat);
        //音频数据播放实例
        audioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig_out, audioFormat, minBufferSize2, mode);
        //开始播放
        audioTrack.play();
        new Thread(new AudioRecordThread()).start();
    }

    private class AudioRecordThread implements Runnable {
        @Override
        public void run() {

            //init();
            //定义缓冲
            byte[] buffer = new byte[minBufferSize / 4];
            short[] buffer2 = new short[minBufferSize2 / 4];

            int readSize;
            while (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                readSize = mAudioRecord.read(buffer, 0, buffer.length);
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                    //发送
                    try {
                    InetAddress serverAddress = InetAddress.getByName(HOST_TARGET);
                    //String str = "hello";
                    byte[] data = buffer;//str.getBytes();
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
                    //然后将数据写入到AudioTrack中
                    //audioTrack.write(buffer, 0, buffer.length);

                }
            }
            //在这里release
            release();
        }
    }

    public void release() {
        if (mAudioRecord != null)
            mAudioRecord.release();
        mAudioRecord = null;
        if (audioTrack != null)
            audioTrack.release();
        audioTrack = null;
    }

    //在这里stop的时候先不要release
    public void stopRecording() {
        if (mAudioRecord != null)
            mAudioRecord.stop();
        if (audioTrack != null)
            audioTrack.stop();

    }
    public void init() {
        try {
            sender = new DatagramSocket();
            startListen();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    private static int PORT_LISTEN = 7654;
    private static int PORT_TARGET = 4567;
    private static String HOST_TARGET = "192.168.0.101";
    private static DatagramSocket sender;

    public void startListen() {
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
                        byte[] buffer = packet.getData();
                        //String result = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        String hostAddress = packet.getAddress().getHostAddress();
                        int port = packet.getPort();
                        //Log.d(TAG, hostAddress + ":" + port + "--->" + result);
                        //send(socket.getInetAddress().getHostAddress(), socket.getPort(), result);
                        audioTrack.write(buffer, 0, buffer.length);
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
