package example.editLayout;

import android.os.Bundle;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.EditLayoutMoveBottomActivityBinding;

import org.jetbrains.annotations.Nullable;


public class HideStatusBarBottomEditLayoutActivity extends BaseTitleBarActivity {

    EditLayoutMoveBottomActivityBinding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.edit_layout_move_bottom_activity);
        setFullScreenStableDarkMode(true);

        bd = getBinding();
        setTitleBarTitle("调整编辑布局");
    }

}
