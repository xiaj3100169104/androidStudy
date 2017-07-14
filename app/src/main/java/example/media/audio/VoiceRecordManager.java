package example.media.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xiajun on 2017/3/19.
 */

public class VoiceRecordManager {
    public static final String FILE_DIR = Environment.getExternalStorageDirectory() + "/AudioMine";
    private static final double FREQUENCY = 500; //Hz，标准频率（这里分析的是500Hz）
    private static final double RESOLUTION = 10; //Hz，误差
    private static final long RECORD_TIME = 2000;
    private static final int audioSource = MediaRecorder.AudioSource.MIC;//
    private static final int sampleRateInHz = 44100;//Hz，采样频率
    private static final int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static final String FILE_PCM_NAME = "audio.pcm";
    private File mSampleFile;
    private int minBufferSize = 0;
    private AudioRecord mAudioRecord;


    private static VoiceRecordManager instance;

    public synchronized static VoiceRecordManager getInstance() {
        if (instance == null) {
            instance = new VoiceRecordManager();
        }
        return instance;
    }

    public void startRecord() {
        try {
            File dir = new File(FILE_DIR);
            dir.mkdirs();
            mSampleFile = new File(dir, FILE_PCM_NAME);
            if (mSampleFile.exists()) {
                if (!mSampleFile.delete()) {
                    return;
                }
            }
            if (!mSampleFile.createNewFile()) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //为了方便，这里只录制单声道
        //如果是双声道，得到的数据是一左一右，注意数据的保存和处理
        minBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, minBufferSize);
        mAudioRecord.startRecording();
        new Thread(new AudioRecordThread()).start();
    }

    private class AudioRecordThread implements Runnable {
        @Override
        public void run() {
            DataOutputStream fos = null;
            try {
                fos = new DataOutputStream(new FileOutputStream(mSampleFile));
                //定义缓冲
                short[] buffer = new short[minBufferSize / 2];
                int readSize;
                while (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    readSize = mAudioRecord.read(buffer, 0, buffer.length);
                    if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                        for (int i = 0; i < readSize; i++) {
                            fos.writeShort(buffer[i]);            //将录音数据写入文件
                            fos.flush();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //在这里release
                release();
            }
        }
    }

    public void stopRecording() {
        if (mAudioRecord != null)
            mAudioRecord.stop();
    }
    public void release() {
        if (mAudioRecord != null)
            mAudioRecord.release();
        mAudioRecord = null;
    }
    //对录音文件进行分析
    private void frequencyAnalyse() {
        if (mSampleFile == null) {
            return;
        }
        try {
            DataInputStream inputStream = new DataInputStream(new FileInputStream(mSampleFile));
            //16bit采样，因此用short[]
            //如果是8bit采样，这里直接用byte[]
            //从文件中读出一段数据，这里长度是SAMPLE_RATE，也就是1s采样的数据
            short[] buffer = new short[sampleRateInHz];
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = inputStream.readShort();
            }
            short[] data = new short[FFT.FFT_N];

            //为了数据稳定，在这里FFT分析只取最后的FFT_N个数据
            System.arraycopy(buffer, buffer.length - FFT.FFT_N,
                    data, 0, FFT.FFT_N);

            //FFT分析得到频率
            double frequence = FFT.GetFrequency(data);
            if (Math.abs(frequence - FREQUENCY) < RESOLUTION) {
                //测试通过
            } else {
                //测试失败
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 这里得到可播放的音频文件
    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = sampleRateInHz;
        int channels = 2;
        long byteRate = 16 * sampleRateInHz * channels / 8;
        byte[] data = new byte[minBufferSize];
        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。
     * <p>
     * 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav
     * <p>
     * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有
     * <p>
     * 自己特有的头文件。
     */

    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }
}
