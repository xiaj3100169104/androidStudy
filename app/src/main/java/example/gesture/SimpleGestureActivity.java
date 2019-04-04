package example.gesture;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.style.base.activity.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.GestureSimpleTestBinding;

import org.jetbrains.annotations.Nullable;

public class SimpleGestureActivity extends BaseDefaultTitleBarActivity {

    GestureSimpleTestBinding bd;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.gesture_simple_test);
        bd = getBinding();
        setToolbarTitle("区分上、下、左、右滑动");
        bd.textView3.setOnClickListener(v -> logE(getTAG(), "textView3"));
        bd.textView4.setOnClickListener(v -> logE(getTAG(), "textView4"));

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public static final float FLING_MIN_VELOCITY = 2000f;
            public static final float FLING_MIN_DISTANCE = 200f;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                logE(getTAG(), "distance  " + (e2.getX() - e1.getX()) + "  velocityX  " + velocityX);
                if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    logE(getTAG(), "向左手势");
                } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    logE(getTAG(), "向右手势");
                } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    logE(getTAG(), "向上手势");
                } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    logE(getTAG(), "向下手势");
                }
                return false;
            }
        });
        bd.tvContent1.setLongClickable(true);
        /*bd.tvContent1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                logE(TAG, "onTouch   " + event.getAction());
                bd.nestedScrollView.requestDisallowInterceptTouchEvent(true);
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });*/
        bd.tvContent1.setOnTouchListener(new MyGestureListener(bd.nestedScrollView) {
            @Override
            public void onSlideLeft() {
                super.onSlideLeft();
            }

            @Override
            public void onSlideRight() {
                super.onSlideRight();
            }
        });
    }

    /* @Override
     public boolean onTouchEvent(MotionEvent event) {
         return mGestureDetector.onTouchEvent(event);
     }*/



    /*private int getLowBloodPressureByHeartRate(int paramInt) {
        float f1 = 0.3F;
        if (paramInt <= 0) {
            int i = 0;
            return i;
        }
        int i = (paramInt - 80) * 2 / 3 + 80 + 5;
        float f2 = this.cardioView.getKValueAverge();
        if ((f2 <= 0.05F) || (f2 > 1.0F)) {
            return 0;
        }
        if ((f2 > 0.05F) && (f2 < 0.4F)) {
            if (f2 >= 0.3F) {
                break label270;
            }
        }
        for (; ; ) {
            int j = (int) (i - (40.0F - f1 * 100.0F) * (40.0F - f1 * 100.0F) / 8.5F);
            label107:
            if ((f1 < 0.32F) && (j > 80)) {
                i = 80 - (int) (32.0F - f1 * 100.0F);
            }

            for (; ; ) {
                paramInt = getLowBloodPressureByHeartRate_Fix(paramInt);
                if (paramInt <= 0) {
                    break;
                }
                return paramInt;
                f1 = f2;
                if (f2 > 0.65F) {
                    f1 = 0.65F;
                }
                j = (int) (i + (f1 * 100.0F - 48.0F) * (f1 * 100.0F - 48.0F) / 8.5F);
                break label107;
                if ((f1 < 0.335F) && (j > 85)) {
                    i = 85 - (int) (33.5F - f1 * 100.0F);
                } else {
                    i = j;
                    if (f1 < 0.35F) {
                        i = j;
                        if (j > 90) {
                            i = 90 - (int) (0.35F - f1 * 100.0F);
                        }
                    }
                }
            }
            label270:
            f1 = f2;
        }
    }

    private String mFixStr = "";

    private int getLowBloodPressureByHeartRate_Fix(int paramInt) {
        float f2 = 0.3F;
        paramInt += 4;
        label246:
        label248:
        for (; ; ) {
            try {
                if (!TextUtils.isEmpty(this.mFixStr)) {
                    String[] arrayOfString = this.mFixStr.split("/");
                    int i = Integer.parseInt(arrayOfString[2]);
                    int j = Integer.parseInt(arrayOfString[1]);
                    if (paramInt <= 0) {
                        return 0;
                    }
                    float f1;
                    if (paramInt < i) {
                        paramInt = j - (int) (Math.pow((i - paramInt) / 10.0F, 0.6000000238418579D) * 7.0D);
                        f1 = this.cardioView.getKValueAverge();
                        if ((f1 > 0.05F) && (f1 < 0.4F)) {
                            if (f1 >= 0.3F) {
                                break label248;
                            }
                            f1 = f2;
                            return (int) (paramInt - (40.0F - f1 * 100.0F) * (40.0F - f1 * 100.0F) / 13.0F);
                        }
                    } else {
                        double d = Math.pow((paramInt - i) / 10.0F, 0.6000000238418579D);
                        paramInt = j + (int) (d * 6.0D);
                        continue;
                    }
                    if (f1 <= 0.5F) {
                        break label246;
                    }
                    f2 = f1;
                    if (f1 > 0.65F) {
                        f2 = 0.65F;
                    }
                    return (int) (paramInt + (f2 * 100.0F - 48.0F) * (f2 * 100.0F - 48.0F) / 13.0F);
                }
            } catch (Exception localException) {
            }
            return paramInt;
        }
        return 0;
    }*/
}
