package example.home

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet

import com.scwang.smartrefresh.layout.header.ClassicsHeader

/**
 * Created by xiajun on 2018/9/27.
 */

class CustomClassicsHeader : ClassicsHeader {
    constructor(context: Context?) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initData(context)
    }

    private fun initData(context: Context) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
}
