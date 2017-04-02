package com.style.lib.media.audio;

import android.util.Log;

/**
 * Created by xiajun on 2017/3/19.
 */

public class FFT {
    public static final int FFT_N = 4096;
    public static final int SAMPLE_RATE = 44100; //HZ

    //快速傅里叶变换
    public static Complex[] getFFT(Complex[] data){
        int N = data.length;
        if(N==1){
            return new Complex[]{data[0]};
        }
        if(N%2 != 0){
            throw new RuntimeException("N is not a power of 2");
        }

        //fft of even/odd terms
        Complex[] even = new Complex[N/2];
        Complex[] odd = new Complex[N/2];
        for(int k = 0;k<N/2;k++){
            even[k] = data[2*k];
            odd[k] = data[2*k+1];
        }
        Complex[] q=  getFFT(even);
        Complex[] r = getFFT(odd);

        Complex[] y = new Complex[N];
        for (int k = 0;k<N/2;k++){
            double kth = -2*k*Math.PI/N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].add(wk.multiply(r[k]));
            y[k+N/2] = q[k].minus(wk.multiply(r[k]));
        }

        return y;
    }

    //================================================================
    public static double GetFrequency(short[] data){
        Log.i("FFT","GetFrequency");
        if(data.length<FFT_N){
            throw new RuntimeException("Data length lower than "+FFT_N);
        }
        Complex[]  f = new Complex[FFT_N];
        for(int i=0;i<FFT_N;i++){
            f[i] = new Complex(data[i],0); //实部为正弦波FFT_N点采样，赋值为1
            //虚部为0
        }

        f = getFFT(f);                                        //进行快速福利叶变换
//        String str = "";
//        for(int i = 0;i<FFT_N;i++){
//            str+=f[i].toString()+" ";
//        }
//        Log.i("FFT","fft: "+str);
        double[]  s = new double[FFT_N/2];
//        str = "";
        for(int i=0;i<FFT_N/2;i++){
            s[i] = f[i].getMod();
//            str += ""+s[i]+" ";
        }
//        Log.i("FFT","s: "+str);

        int fmax=0;
        for(int i=1;i<FFT_N/2;i++){  //利用FFT的对称性，只取前一半进行处理
            if(s[i]>s[fmax])
                fmax=i;                          //计算最大频率的序号值
        }
//        Log.i("FFT","max index:"+fmax+" fft:"+f[fmax]+" s:"+s[fmax]);
        double fre = fmax*(double)SAMPLE_RATE / FFT_N;
        Log.i("FFT","fre:"+fre);
        return fre;
    }
}