package com.style.lib.media.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by xiajun on 2017/3/19.
 */

public class VoicePlayManager {
    private static final int streamType = AudioManager.STREAM_MUSIC;//
    private static final int sampleRateInHz = 44100;//Hz，采样频率
    private static final int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static final int mode = AudioTrack.MODE_STREAM;
    private AudioTrack audioTrack;

    private static VoicePlayManager instance;

    public synchronized static VoicePlayManager getInstance() {
        if (instance == null) {
            instance = new VoicePlayManager();
        }
        return instance;
    }

    public void play(String fileName) {
        File file = new File(VoiceRecordManager.FILE_DIR, fileName);
        //定义输入流，将音频写入到AudioTrack类中，实现播放
        DataInputStream dis = null;
        AudioTrack track = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            int minBufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
            short[] buffer = new short[minBufferSize / 4];
            //实例AudioTrack
            track = new AudioTrack(streamType, sampleRateInHz, channelConfig, audioFormat, minBufferSize, mode);
            //开始播放
            track.play();
            //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
            while (dis.available() > 0) {
                int i = 0;
                while (dis.available() > 0 && i < buffer.length) {
                    buffer[i] = dis.readShort();
                    i++;
                }
                //然后将数据写入到AudioTrack中
                track.write(buffer, 0, buffer.length);
            }
            //播放结束
            track.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //在这里release
            if (track != null)
                track.release();
        }
    }

    public void stop() {
        if (audioTrack != null)
            audioTrack.stop();
    }

    public void release() {
        if (audioTrack != null)
            audioTrack.release();
    }
}
