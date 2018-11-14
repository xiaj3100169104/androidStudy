package com.style.view.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.style.framework.R;


/**
 * Created by xiajun on 2016/12/30.
 */

public class DrawView extends View {

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 创建画笔
        Paint p = new Paint();
        //画点
        p.reset();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        canvas.drawPoint(60, 390, p);
        //画线
        p.reset();
        p.setColor(Color.GREEN);
        p.setStrokeWidth(5);
        canvas.drawLine(60, 40, 300, 40, p);
        //画矩形
        p.reset();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);//设置填满
        canvas.drawRect(60, 80, 100, 120, p);// 正方形
        canvas.drawRect(150, 80, 260, 120, p);// 长方形
        p.setStyle(Paint.Style.STROKE);//设置空心
        p.setStrokeWidth(5);
        canvas.drawRect(300, 80, 460, 120, p);// 长方形
        //画圆
        p.reset();
        p.setColor(Color.RED);
        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        canvas.drawCircle(120, 170, 40, p);
        //圆环
        p.setStyle(Paint.Style.STROKE);//设置空心
        p.setStrokeWidth(5);
        canvas.drawCircle(250, 170, 40, p);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.GREEN);
        canvas.drawCircle(250, 170, 38, p);
        //椭圆
        p.reset();
        p.setAntiAlias(true);
        p.setColor(Color.BLUE);
        RectF oval = new RectF(60, 250, 300, 350);// 设置个新的长方形，扫描测量
        canvas.drawOval(oval, p);
        p.setStyle(Paint.Style.STROKE);//设置空心
        p.setStrokeWidth(5);
        RectF oval4 = new RectF(360, 250, 600, 350);// 设置个新的长方形，扫描测量
        canvas.drawOval(oval4, p);
        //画弧
        p.reset();
        p.setColor(Color.RED);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);//设置空心
        RectF oval16 = new RectF(60, 370, 180, 470);
        canvas.drawArc(oval16, 0, 225, false, p);//当笔为空心时，true表示加半径线
        RectF oval1 = new RectF(200, 370, 320, 470);
        canvas.drawArc(oval1, 0, 225, true, p);
        p.setStyle(Paint.Style.FILL);
        RectF oval15 = new RectF(340, 370, 460, 470);
        canvas.drawArc(oval15, 0, 225, false, p);//当笔为实心时，false表示忽略中心区域
        RectF oval7 = new RectF(480, 370, 600, 470);
        canvas.drawArc(oval7, 0, 225, true, p);
        //画扇形
        p.reset();
        Shader mShader = new LinearGradient(0, 0, 100, 100,
                new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.LTGRAY}, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        p.setShader(mShader);
        RectF oval2 = new RectF(60, 500, 180, 600);// 设置个新的长方形，扫描测量
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        canvas.drawArc(oval2, 200, 130, true, p);

        //画三角形
        p.reset();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(80, 550);// 此点为多边形的起点
        path.lineTo(80, 700);
        path.lineTo(200, 700);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, p);
        // 你可以绘制很多任意多边形，比如下面画六连形
        p.setStyle(Paint.Style.STROKE);
        Path path1 = new Path();
        path1.moveTo(280, 550);
        path1.lineTo(400, 550);
        path1.lineTo(510, 635);
        path1.lineTo(400, 720);
        path1.lineTo(280, 720);
        path1.lineTo(170, 635);
        path1.close();//封闭
        canvas.drawPath(path1, p);
        //画圆角矩形
        p.reset();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);//充满
        p.setAntiAlias(true);// 设置画笔的锯齿效果
        RectF oval3 = new RectF(80, 760, 280, 900);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 15, 15, p);//第二个参数是x半径，第三个参数是y半径

        //画图片，就是贴图
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(bitmap, 60, 920, p);
    }
}
