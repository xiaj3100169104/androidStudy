package example.editLayout;

import android.os.Bundle;

import com.style.base.BaseHideStatusTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.EditLayoutMoveBottomActivityBinding;

import org.jetbrains.annotations.Nullable;


public class HideStatusBarBottomEditLayoutActivity extends BaseHideStatusTitleBarActivity {

    EditLayoutMoveBottomActivityBinding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.edit_layout_move_bottom_activity);
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("调整编辑布局");
    }

}
