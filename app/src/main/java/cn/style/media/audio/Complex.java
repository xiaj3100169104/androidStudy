package cn.style.media.audio;

/**
 * Created by xiajun on 2017/3/19.
 */
public class Complex {

    public double real, imag;

    public Complex(double real,double im){
        this.real = real;
        this.imag = im;
    }

    public Complex(){
        this(0,0);
    }

    public Complex(Complex c){
        this(c.real,c.imag);
    }

    @Override
    public String toString() {
        return "("+this.real+"+"+this.imag +"i)";
    }

    //加法
    public final Complex add(Complex c){
        return new Complex(this.real+c.real,this.imag +c.imag);
    }

    //减法
    public final Complex minus(Complex c){
        return new Complex(this.real-c.real,this.imag -c.imag);
    }

    //求模值
    public final double getMod(){
        return Math.sqrt(this.real * this.real+this.imag * this.imag);
    }

    //乘法
    public final Complex multiply(Complex c){
        return new Complex(
                this.real*c.real - this.imag *c.imag,
                this.real*c.imag + this.imag *c.real);
    }
}