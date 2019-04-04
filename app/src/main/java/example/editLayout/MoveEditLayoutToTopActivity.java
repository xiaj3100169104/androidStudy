package example.editLayout;

import android.os.Bundle;

import com.style.base.activity.BaseHideStatusTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.EditLayoutMoveTopActivityBinding;

import org.jetbrains.annotations.Nullable;

public class MoveEditLayoutToTopActivity extends BaseHideStatusTitleBarActivity {

    EditLayoutMoveTopActivityBinding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.edit_layout_move_top_activity);
        bd = getBinding();
        setToolbarTitle("整体上移");
    }

}
