package com.style.address;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.rxAndroid.RXTaskManager;
import com.style.rxAndroid.callback.RXTaskCallBack;
import com.style.threadpool.CachedThreadPoolManager;
import com.style.threadpool.callback.MyTaskCallBack;
import com.style.utils.HanyuToPinyin;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;


public class AddressActivity extends BaseToolBarActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_dialog)
    TextView tvDialog;
    @Bind(R.id.sidebar)
    SideBar sidebar;
    private List dataList;
    private LinearLayoutManager layoutManager;
    private UploadPhoneAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_address;
        super.onCreate(arg0);
    }

    @Override
    public void initData() {
        setToolbarTitle("通讯录");

        sidebar.setTextView(tvDialog);
        dataList = new ArrayList<>();
        adapter = new UploadPhoneAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                UploadPhone up = (UploadPhone) data;
                //AppDataHelper.openEditSms(PhoneActivity.this, up.getTelephone());
            }
        });
        // 设置右侧触摸监听
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    layoutManager.smoothScrollToPosition(recyclerView, null, position);
                }
            }
        });

        getData();
    }

    private void getData() {
        showProgressDialog();
        //custom();
        RXTaskManager.getInstance().runTask(TAG, new RXTaskCallBack<List<UploadPhone>>(){
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
            public void onSuccess(List<UploadPhone> data) {
                Log.e(AddressActivity.this.TAG, "OnSuccess");
                dismissProgressDialog();
                if (data != null) {
                    //List<UploadPhone> response = (List<UploadPhone>) data;
                    Log.e(AddressActivity.this.TAG, data.toString());
                    dataList.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RXTaskManager.getInstance().removeTask(TAG);
    }

    private void custom() {
        CachedThreadPoolManager.getInstance().runTask(TAG,new MyTaskCallBack() {
            @Override
            public Object doInBackground() {
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
            public void onSuccess(Object data) {
                Log.e(AddressActivity.this.TAG, "OnSuccess");
                dismissProgressDialog();
                if (data != null) {
                    List<UploadPhone> response = (List<UploadPhone>) data;
                    Log.e(AddressActivity.this.TAG, response.toString());
                    dataList.addAll(response);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(String message) {
                dismissProgressDialog();

            }
        });
    }
}
