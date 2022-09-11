package example.address;

import android.Manifest;
import android.annotation.SuppressLint;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.telephony.TelephonyManager;
import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityAddressBinding;
import com.style.view.diviver.DividerItemDecoration;
import com.style.view.sort.FloatingBarItemDecoration;
import com.style.view.sort.IndexBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;


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
        bd = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setTitleBarTitle("通讯录");

        mPresenter = new ViewModelProvider(this).get(AddressViewModel.class);
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
        requestReadContactsPermissions();
        View v2 = bd.commonLoadingLayout.setEmptyView(R.layout.common_loading_layout_empty_2);
        v2.findViewById(R.id.common_loading_layout_tv_empty_2).setOnClickListener(v -> bd.commonLoadingLayout.showLoading());
        bd.btn1.setOnClickListener(v -> bd.commonLoadingLayout.showLoading());
        bd.btn2.setOnClickListener(v -> bd.commonLoadingLayout.showContent());
        bd.btn3.setOnClickListener(v -> bd.commonLoadingLayout.showEmpty());
        bd.btn4.setOnClickListener(v -> bd.commonLoadingLayout.showNetworkError());
        bd.commonLoadingLayout.setOnClickRetryListener(() -> bd.commonLoadingLayout.showContent());
        bd.commonLoadingLayout.showLoading();

        bd.btnImei.setOnClickListener(v -> isNotificationEnabled());
        getImei(this);
    }

    private void isNotificationEnabled() {
        boolean is = NotificationManagerCompat.from(this).areNotificationsEnabled();
        if (!is)
            requestNotify(this);
    }

    private void hideLetterHint() {
        bd.tvDialog.setVisibility(View.INVISIBLE);
    }

    private void showLetterHint(String s) {
        bd.tvDialog.setText(s);
        bd.tvDialog.setVisibility(View.VISIBLE);
    }

    @SuppressLint("CheckResult")
    public void requestReadContactsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.READ_CONTACTS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(grated -> {
                        if (grated) {
                            getData();
                        } else {
                            showToast(R.string.please_open_contacts_permission);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                    });
        } else {
            getData();
        }
    }

    public void getData() {
        mPresenter.getData();
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
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
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

    /**
     * 跳到通知栏设置界面
     *
     * @param context
     */
    public static void requestNotify(Context context) {
        Intent localIntent = new Intent();
        //跳转到应用通知设置的代码
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        context.startActivity(localIntent);
    }


    @SuppressLint("CheckResult")
    private void getImei(Context context) {
        //64位数字（表示为十六进制字符串）是在用户首次设置设备时随机生成的
        //如果在设备上执行恢复出厂设置或APK签名密钥更改，则该值可能会更改。
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //实例化TelephonyManager对象
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = telephonyManager.getClass();
        Method getImei = null;
        try {
            getImei = clazz.getDeclaredMethod("getImei", int.class);//(int slotId)
            String m = (String) getImei.invoke(telephonyManager, 0); //卡1
            getImei.invoke(telephonyManager, 1); // 卡2
            //Log.e(TAG, "IMEI1 : "+getImei.invoke(telephonyManager, 0)); //卡1
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(grated -> {
                        if (grated) {
                            String deviceId = telephonyManager.getDeviceId();
                            String imei = telephonyManager.getImei();
                            //bd.btnImei.setText(imei);
                        } else {
                            showToast(R.string.error_no_external_storage_permission);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                    });
        }
    }

}
