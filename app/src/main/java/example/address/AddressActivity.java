package example.address;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.style.base.activity.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityAddressBinding;
import com.style.lib_common_ui.loading.CommonLoadingLayout;
import com.style.view.diviver.DividerItemDecoration;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class AddressActivity extends BaseTitleBarActivity {

    private static int REQUEST_READ_CONTACTS = 5;
    ActivityAddressBinding bd;
    private ArrayList<UploadPhone> dataList;
    private LinkedHashMap<Integer, String> mHeaderList = new LinkedHashMap<>();
    private LinearLayoutManager layoutManager;
    private UploadPhoneAdapter adapter;
    private AddressViewModel mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_address);
        setTitleBarTitle("通讯录");
        bd = getBinding();
        mPresenter = getViewModel(AddressViewModel.class);
        mPresenter.contacts.observe(this, uploadPhones -> {
            setData(uploadPhones);
        });
        dataList = new ArrayList<>();
        adapter = new UploadPhoneAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.addItemDecoration(new FloatingBarItemDecoration(this, mHeaderList));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((position, data) -> {
            //AppDataHelper.openEditSms(PhoneActivity.this, up.getTelephone());
            call(data.getTelephone());
        });
        // 设置右侧触摸监听
        bd.indexBar.setOnTouchingLetterChangedListener(new IndexBar.OnTouchingLetterChangeListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                showLetterHint(s);
                for (Integer position : mHeaderList.keySet()) {
                    if (mHeaderList.get(position).equals(s)) {
                        //layoutManager.smoothScrollToPosition(bd.recyclerView, null, position);
                        //移动到屏幕顶端， 距离屏幕顶端offset
                        layoutManager.scrollToPositionWithOffset(position, 0);
                        return;
                    }
                }
            }

            @Override
            public void onTouchingStart(String s) {
                showLetterHint(s);
            }

            @Override
            public void onTouchingEnd(String s) {
                hideLetterHint();
            }
        });
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            logE(getTAG(), "检查权限");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                logE(getTAG(), "上次拒绝");
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);

            } else {
                logE(getTAG(), "请求权限");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            getData();
        }
        View v2 = bd.commonLoadingLayout.setEmptyView(R.layout.common_loading_layout_empty_2);
        v2.findViewById(R.id.common_loading_layout_tv_empty_2).setOnClickListener(v -> bd.commonLoadingLayout.showLoading());
        bd.btn1.setOnClickListener(v -> bd.commonLoadingLayout.showLoading());
        bd.btn2.setOnClickListener(v -> bd.commonLoadingLayout.showContent());
        bd.btn3.setOnClickListener(v -> bd.commonLoadingLayout.showEmpty());
        bd.btn4.setOnClickListener(v -> bd.commonLoadingLayout.showNetworkError());
        bd.commonLoadingLayout.setOnClickRetryListener(() -> bd.commonLoadingLayout.showContent());
        bd.commonLoadingLayout.showLoading();
    }

    private void hideLetterHint() {
        bd.tvDialog.setVisibility(View.INVISIBLE);
    }

    private void showLetterHint(String s) {
        bd.tvDialog.setText(s);
        bd.tvDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logE(getTAG(), "权限允许");
                getData();
            } else {
                logE(getTAG(), "权限拒绝");
                // Permission Denied
                showToast("Permission Denied");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getData() {
        mPresenter.getData();
        //getData2();
    }

    public void setData(List<UploadPhone> data) {
        dataList.addAll(data);
        mHeaderList.clear();
        addHeaderToList(0, dataList.get(0).getSortLetters());
        for (int i = 1; i < dataList.size(); i++) {
            if (!dataList.get(i - 1).getSortLetters().equalsIgnoreCase(dataList.get(i).getSortLetters())) {
                addHeaderToList(i, dataList.get(i).getSortLetters());
            }
        }
        adapter.notifyDataSetChanged();

        bd.indexBar.setNavigators(new ArrayList<>(mHeaderList.values()));
    }

    private void addHeaderToList(int index, String header) {
        mHeaderList.put(index, header);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stopPlay();
    }

    private void call(String number) {
        new android.support.v7.app.AlertDialog.Builder(getContext())
                .setTitle("拨号")
                .setMessage("拨打客服电话" + number)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    openDial(number);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                }).create().show();

    }

    private void openDial(String number) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));//跳转到拨号界面，同时传递电话号码
        startActivity(dialIntent);
    }
}
