package example.dialog;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.dialog.GeneralListPopup;
import com.style.dialog.LoadingDialog;
import com.style.dialog.MaterialProgressDialog;
import com.style.dialog.PromptDialog;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.R;
import com.style.framework.databinding.DialogActivityDialogBinding;

/**
 * Created by xiajun on 2016/10/8.
 */
public class DialogActivity extends BaseActivity {
    DialogActivityDialogBinding bd;
    private FragmentManager fm;
    private FragmentTransaction bt;
    private MaterialProgressDialog materialDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_activity_dialog;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("弹出框");
        fm = getSupportFragmentManager();
        bd.setOnItemClickListener(new OnItemClickListener());

        materialDialog = new MaterialProgressDialog(this);
        materialDialog.show();

        bd.ivLogo.setOnClickListener(v -> showPopupWindow(v));
        bd.btn3.setOnClickListener(v -> showPopupMenu(v));
        bd.btn4.setOnClickListener(v -> showListPopup(v));
        bd.btn5.setOnClickListener(v -> showSpinner());
    }

    private void showPopupWindow(View v) {
        ScaleTestWindow popWindow = new ScaleTestWindow(getContext());
        popWindow.showAsDropDown(v, 0, -v.getHeight());
    }

    private void showSpinner() {
        /*AppCompatSpinner spinner = new AppCompatSpinner(this);
        spinner.*/
    }

    private void showListPopup(View v) {
        GeneralListPopup popup = new GeneralListPopup(getContext());
        popup.setOnItemClickListener((position, data) -> {
            Log.e(getTAG(), position + "  " + data);
        });
        popup.showAsDropDown(v);
    }

    private void showPopupMenu(View v) {
        //莫法设置相对位置偏移量
        PopupMenu pop = new PopupMenu(getContext(), v);
        pop.getMenuInflater().inflate(R.menu.user_info, pop.getMenu());
        pop.show();
        pop.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    break;
                case R.id.report:
                    break;
            }
            return true;
        });

    }

    public class OnItemClickListener {

        public void skip1(View v) {
            PromptDialog promptDialog = new PromptDialog(getContext());
            promptDialog.setTitle("提示");
            promptDialog.setMessage("这是提示信息");
            promptDialog.setListener(new PromptDialog.OnPromptListener() {
                @Override
                public void onPositiveButton() {

                }

                @Override
                public void onNegativeButton() {

                }
            });
            promptDialog.show();
        }

        public void skip2(View v) {
            LoadingDialog loadingDialog = new LoadingDialog(getContext());
            loadingDialog.show();
        }

        public void skip3(View v) {
            SelAvatarDialog selAvatarDialog = new SelAvatarDialog(getContext());
            selAvatarDialog.show();
        }

        public void skip4(View v) {
            /*DialogFragment df= new DialogFragment();
            fm.beginTransaction().add(R.id.layout_root, df , "dialog").commit();
*/
        }
    }
}