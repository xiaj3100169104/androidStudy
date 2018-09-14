package example.address;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityAddressBinding;
import com.style.threadpool.GeneralThreadPoolManager;
import com.style.threadpool.callback.CustomFutureTask;
import com.style.utils.HanyuToPinyin;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AddressActivity extends BaseTitleBarActivity {

    private static int REQUEST_READ_CONTACTS = 5;
    ActivityAddressBinding bd;
    private ArrayList<UploadPhone> dataList;
    private LinearLayoutManager layoutManager;
    private UploadPhoneAdapter adapter;
    private AddressPresenter mPresenter;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_address;
    }

    @Override
    public void initData() {
        bd = getBinding();
        mPresenter = getViewModel(AddressPresenter.class);
        mPresenter.contacts.observe(this, uploadPhones -> {
            setData(uploadPhones);
        });

        setToolbarTitle("通讯录");
        bd.sidebar.setTextView(bd.tvDialog);
        dataList = new ArrayList<>();
        adapter = new UploadPhoneAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((position, data) -> {
            UploadPhone up = (UploadPhone) data;
            //AppDataHelper.openEditSms(PhoneActivity.this, up.getTelephone());
        });
        // 设置右侧触摸监听
        bd.sidebar.setOnTouchingLetterChangedListener(s -> {
            // 该字母首次出现的位置
            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                layoutManager.smoothScrollToPosition(bd.recyclerView, null, position);
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
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stopPlay();
    }

    private void getData2() {
        GeneralThreadPoolManager.getInstance().runTask(getTAG(), new CustomFutureTask<List<UploadPhone>>() {
            @Override
            public List<UploadPhone> doInBackground() {
                List<UploadPhone> list = ContactHelper.getContacts(getContext());
                if (null != list) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        String sortLetter = HanyuToPinyin.hanziToCapital(list.get(i).getName());
                        list.get(i).setSortLetters(sortLetter);
                    }
                }
                // 根据a-z进行排序源数据
                Collections.sort(list, new UploadPhoneComparator());
                return list;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<UploadPhone> data) {
                Log.e(AddressActivity.this.getTAG(), "OnSuccess");
                dismissProgressDialog();
                if (data != null) {
                    setData(data);
                }
            }

            @Override
            public void onFailed(String message) {
                dismissProgressDialog();

            }
        });
    }
}
