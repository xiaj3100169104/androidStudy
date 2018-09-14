package example.vlayout;

import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMultiTypeBinding;

import java.util.ArrayList;


public class MultiTypeActivity extends BaseTitleBarActivity {

    ActivityMultiTypeBinding bd;

    private String banner = "banner";
    private String address = "address";
    private String header = "header";
    private ArrayList<String> dataList;
    private ArrayList<String> dataList2;
    private ArrayList<String> dataList3;
    private ArrayList<String> dataList4;

    private BannerAdapter adapter;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_multi_type;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("复杂布局");

        dataList = new ArrayList<>();
        dataList2 = new ArrayList<>();
        dataList3 = new ArrayList<>();
        dataList4 = new ArrayList<>();

        final VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        bd.recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        viewPool.setMaxRecycledViews(1, 10);
        viewPool.setMaxRecycledViews(2, 10);
        viewPool.setMaxRecycledViews(3, 10);
        //bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        bd.recyclerView.setAdapter(delegateAdapter);
        //delegateAdapter.setAdapters(adapters);
        dataList.add(banner);
        BannerAdapter adapter = new BannerAdapter(getContext(), dataList, new SingleLayoutHelper());
        delegateAdapter.addAdapter(adapter);
        // 如果数据有变化，调用自定义 adapter 的 notifyDataSetChanged()
        adapter.notifyDataSetChanged();

        //设置Grid布局
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        //是否自动扩展
        gridLayoutHelper.setAutoExpand(false);
        //自定义设置某些位置的Item的占格数
        gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position > 13) {
                    return 2;
                }else {
                    return 1;
                }
            }
        });
        dataList2.add(header);

        HeaderAdapter adapter2 = new HeaderAdapter(getContext(), dataList2, gridLayoutHelper);
        delegateAdapter.addAdapter(adapter2);

        //设置线性布局
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        //设置Item个数
        linearLayoutHelper.setItemCount(5);
        //设置间隔高度
        linearLayoutHelper.setDividerHeight(10);
        //设置布局底部与下个布局的间隔
        linearLayoutHelper.setMarginBottom(100);
        dataList3.add(address);
        ContactAdapter adapter3 = new ContactAdapter(getContext(), dataList3, linearLayoutHelper);
        delegateAdapter.addAdapter(adapter3);

      /*  adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                UploadPhone up = (UploadPhone) data;
                //AppDataHelper.openEditSms(PhoneActivity.this, up.getTelephone());
            }
        });*/

        //getData();
    }

    private void getData() {
        showProgressDialog();
        /*CachedThreadPoolManager.getInstance().runTask(TAG, new MyTaskCallBack() {
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
        });*/
    }
}
