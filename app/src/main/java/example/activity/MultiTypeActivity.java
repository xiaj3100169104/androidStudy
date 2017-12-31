package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import example.address.ContactHelper;
import example.address.UploadPhone;
import example.address.UploadPhoneComparator;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolBarActivity;
import com.style.base.MultiTypeRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMultiTypeBinding;
import com.style.threadpool.CachedThreadPoolManager;
import com.style.threadpool.callback.MyTaskCallBack;
import com.style.utils.HanyuToPinyin;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MultiTypeActivity extends BaseToolBarActivity {

    ActivityMultiTypeBinding bd;

    private String banner = "banner";
    private String address = "address";
    private String header = "header";
    private ArrayList<UploadPhone> dataList;
    private LinearLayoutManager layoutManager;
    private MultiTypeRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_multi_type);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("复杂布局");

        dataList = new ArrayList<>();
        dataList.add(new UploadPhone(banner));
        dataList.add(new UploadPhone(address));
        dataList.add(new UploadPhone(header));
        adapter = new MultiTypeRecyclerViewAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                UploadPhone up = (UploadPhone) data;
                //AppDataHelper.openEditSms(PhoneActivity.this, up.getTelephone());
            }
        });

        getData();
    }

    private void getData() {
        showProgressDialog();
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
                Log.e(MultiTypeActivity.this.TAG, "OnSuccess");
                dismissProgressDialog();
                if (data != null) {
                    List<UploadPhone> response = (List<UploadPhone>) data;
                    Log.e(MultiTypeActivity.this.TAG, response.toString());
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
