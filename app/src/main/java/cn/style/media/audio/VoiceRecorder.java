package cn.style.media.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

/**
 * Created by xiajun on 2017/3/19.
 */

public class VoiceRecorder {
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
        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig_in, audioFormat, minBufferSize);
        mAudioRecord.startRecording();
        minBufferSize2 = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig_out, audioFormat);
        //实例AudioTrack
        audioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig_out, audioFormat, minBufferSize2, mode);
        //开始播放
        audioTrack.play();
        new Thread(new AudioRecordThread()).start();
    }

    private class AudioRecordThread implements Runnable {
        @Override
        public void run() {

            //定义缓冲
            short[] buffer = new short[minBufferSize / 4];
            short[] buffer2 = new short[minBufferSize2 / 4];

            int readSize;
            while (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                readSize = mAudioRecord.read(buffer, 0, buffer.length);
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                    //然后将数据写入到AudioTrack中
                    audioTrack.write(buffer, 0, buffer.length);

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

}
