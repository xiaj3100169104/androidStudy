package example.home;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created by xiajun on 2018/9/27.
 */

public class CustomClassicsHeader extends ClassicsHeader {
    public CustomClassicsHeader(Context context) {
        super(context);
    }

    public CustomClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomClassicsHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
