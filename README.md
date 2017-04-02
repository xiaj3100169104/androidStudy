# CustomNotifyView
通用小圆点通知view

# Screenshots
  -------------
<img src="screenshots/Screenshot_2017-01-14-13-37-28.png" height="400" alt="Screenshot"/>

#layout

```xml
 <CustomNotifyView
        android:layout_width="20dp"
        android:layout_height="20dp"
        android:layout_centerInParent="true"
        custom:notifyText="99" />
```
##attr

```xml
 <attr name="notifyBackgroundColor" format="color" />
    <attr name="notifyText" format="string" />
    <attr name="notifyTextColor" format="color" />
    <attr name="notifyTextSize" format="dimension" />

    <declare-styleable name="CustomNotifyView">
        <attr name="notifyBackgroundColor" />
        <attr name="notifyText" />
        <attr name="notifyTextColor" />
        <attr name="notifyTextSize" />
    </declare-styleable>
```

# HorizontalProgressBar
水平条形进度条，个人认为条形和环形是两种很大不同的view，应该分开

# Screenshots
  -------------
<img src="screenshots/Screenshot_20170331-180522_副本.png" height="400" alt="Screenshot"/>

#layout

```xml
  <com.style.common.view.HorizontalProgressBar
         android:layout_width="match_parent"
         android:layout_height="5dp"
         android:layout_marginTop="50dp"
         android:background="@android:color/darker_gray"
         custom:horizontalProgressColor="@color/colorAccent"
         custom:horizontalProgress="30" />
```
##attr

```xml

    <attr name="horizontalProgressColor" format="color" />
    <attr name="horizontalProgress" format="integer" />

    <declare-styleable name="HorizontalProgressBar">
        <attr name="horizontalProgressColor" />
        <attr name="horizontalProgress" />
    </declare-styleable>
```
# CircleProgress
几种圆形进度条

# Screenshots
  -------------
<img src="screenshots/Screenshot_dsfs.png" height="400" alt="Screenshot"/>
#layout

```xml

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="change" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:gravity="center">

        <com.github.xiajun.circleprogress.CirclePercentView
            android:id="@+id/circleView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            custom:centerTextSize="25sp"
            custom:percent="15"
            custom:radius="50dp"
            custom:stripeWidth="10dp" />

        <com.github.xiajun.circleprogress.CirclePercentView
            android:id="@+id/circleView2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="50dp"
            custom:centerTextSize="35sp"
            custom:percent="75"
            custom:radius="60dp"
            custom:stripeWidth="10dp" />
    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp">

        <com.github.xiajun.circleprogress.ArcProgress
            android:id="@+id/arc_progress"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginLeft="50dp"
            android:background="#214193"
            custom:arc_bottom_text="MEMORY"
            custom:arc_progress="10" />

        <com.github.xiajun.circleprogress.ArcProgress
            android:id="@+id/arc_progress2"
            android:layout_width="80dp"
            android:layout_height="80dp"
            android:layout_marginLeft="50dp"
            android:background="#214193"
            custom:arc_bottom_text="STORAGE"
            custom:arc_finished_color="#ff0000"
            custom:arc_progress="30"
            custom:arc_suffix_text="#"
            custom:arc_text_color="#ffffff"
            custom:arc_unfinished_color="#ffffff" />
    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp">

        <com.github.xiajun.circleprogress.CircleProgress
            android:id="@+id/circle_progress"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginLeft="50dp"
            custom:circle_progress="10" />

        <com.github.xiajun.circleprogress.CircleProgress
            android:id="@+id/circle_progress2"

            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginLeft="50dp"
            custom:circle_finished_color="#ff0000"
            custom:circle_progress="25"
            custom:circle_suffix_text="$"
            custom:circle_text_color="#ffffff"
            custom:circle_text_size="16sp"
            custom:circle_unfinished_color="#00ff00" />
    </LinearLayout>
```
#attr

```xml
 <declare-styleable name="CirclePercentView">
         <attr name="radius" format="dimension" />      <!--最大半径-->
         <attr name="stripeWidth" format="dimension" /> <!--色带宽度-->
         <attr name="percent" format="integer" />       <!--百分比 最大值为100-->
         <attr name="smallColor" format="color" />      <!--内圈颜色-->
         <attr name="bigColor" format="color" />        <!--外圈颜色-->
         <attr name="finishColor" format="color" />     <!--进度完成颜色-->
         <attr name="textColor" format="color" />       <!--中心百分比文字颜色-->
         <attr name="centerTextSize" format="dimension" />     <!--中心百分比字体大小-->
     </declare-styleable>

     <declare-styleable name="CircleProgress">
         <attr name="circle_progress" format="integer"/>
         <attr name="circle_max" format="integer"/>

         <attr name="circle_unfinished_color" format="color"/>
         <attr name="circle_finished_color" format="color"/>

         <attr name="circle_text_size" format="dimension"/>
         <attr name="circle_text_color" format="color"/>

         <attr name="circle_prefix_text" format="string"/>
         <attr name="circle_suffix_text" format="string"/>
     </declare-styleable>

     <declare-styleable name="ArcProgress">
         <attr name="arc_progress" format="integer"/>
         <attr name="arc_angle" format="float"/>
         <attr name="arc_stroke_width" format="dimension"/>
         <attr name="arc_max" format="integer"/>

         <attr name="arc_unfinished_color" format="color"/>
         <attr name="arc_finished_color" format="color"/>

         <attr name="arc_text_size" format="dimension"/>
         <attr name="arc_text_color" format="color"/>

         <attr name="arc_suffix_text" format="string"/>
         <attr name="arc_suffix_text_size" format="dimension"/>
         <attr name="arc_suffix_text_padding" format="dimension"/>

         <attr name="arc_bottom_text" format="string"/>
         <attr name="arc_bottom_text_size" format="dimension"/>
     </declare-styleable>

     <declare-styleable name="Themes">
         <attr name="circleProgressStyle" format="reference"/>
         <attr name="donutProgressStyle" format="reference"/>
         <attr name="arcProgressStyle" format="reference"/>
     </declare-styleable>
```
#code

```java
        mCirclePercentView = (CirclePercentView) findViewById(R.id.circleView);
        mCirclePercentView2 = (CirclePercentView) findViewById(R.id.circleView2);

        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        circleProgress2 = (CircleProgress) findViewById(R.id.circle_progress2);
        arcProgress2 = (ArcProgress) findViewById(R.id.arc_progress2);
        mButton = (Button) findViewById(R.id.button);
        mCirclePercentView2.setCurPercent(0);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = (int)(Math.random()*100);

                mCirclePercentView.setCurPercent(n);
                arcProgress.setProgress(n);
                circleProgress.setProgress(n);

                mCirclePercentView2.setPercentWithAnimation(n);
                arcProgress2.setPercentWithAnimation(n);
                circleProgress2.setPercentWithAnimation(n);
            }
        });
```