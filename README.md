
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
