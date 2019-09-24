package example.dialog;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;

import com.style.base.activity.BaseTitleBarActivity;
import com.style.dialog.GeneralListPopup;
import com.style.dialog.LoadingDialog;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.R;
import com.style.framework.databinding.DialogActivityDialogBinding;
import com.style.lib_common_ui.dialog.MaterialProgressDialog;

import org.jetbrains.annotations.Nullable;

import example.activity.SafeKeyboardActivity;

/**
 * Created by xiajun on 2016/10/8.
 */
public class DialogActivity extends BaseTitleBarActivity {
    DialogActivityDialogBinding bd;
    private FragmentManager fm;
    private FragmentTransaction bt;
    private MaterialProgressDialog materialDialog;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dialog_activity_dialog);
        bd = getBinding();
        setTitleBarTitle("弹出框");
        fm = getSupportFragmentManager();
        materialDialog = new MaterialProgressDialog(this);
        materialDialog.show();

        bd.viewSystemPromptDialog.setOnClickListener(v -> showSystemPromptDialog());
        bd.viewCustomLoadingDialog.setOnClickListener(v -> showCustomLoadingDialog());
        bd.viewSelectAvatarDialog.setOnClickListener(v -> showSelectAvatar());
        bd.viewPopupMenu.setOnClickListener(v -> showPopupMenu(v));
        bd.viewListPopupWindow.setOnClickListener(v -> showListPopup(v));
        bd.viewSpinner.setOnClickListener(v -> showSpinner());
        bd.viewBottomSheetDialog.setOnClickListener(v -> showBottomSheetDialog());
        bd.viewNumberSafeKeyboard.setOnClickListener(v -> skip(SafeKeyboardActivity.class));
        bd.ivLogo.setOnClickListener(v -> showPopupWindow(v));
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(getLayoutInflater().inflate(R.layout.fragment_home_3, null));
        dialog.show();
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


    public void showSystemPromptDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("系统默认风格")
                .setMessage("这是提示信息")
                .setPositiveButton(R.string.ok, (dialog, which) -> {

                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {

                }).create();
        alertDialog.show();
    }

    public void showCustomLoadingDialog() {
        LoadingDialog loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
    }

    public void showSelectAvatar() {
        SelAvatarDialog selAvatarDialog = new SelAvatarDialog(getContext());
        selAvatarDialog.show();
    }

}