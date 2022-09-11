package com.style.dialog;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.GeneralListPopupBinding;
import com.style.framework.databinding.GeneralPopupWindowAdapterItemBinding;

import java.util.ArrayList;

/**
 * Created by xiajun on 2018/7/16.
 */

public class GeneralListPopup extends PopupWindow {
    public static final String TAG = "GeneralListPopup";
    private final Context context;
    private ArrayList<String> dataList;
    private SimpleStringAdapter adapter;
    private GeneralListPopupBinding bd;

    public GeneralListPopup(@NonNull Context context) {
        super(context);
        this.context = context;
        initData();
    }

    public Context getContext() {
        return context;
    }

    private void initData() {
        bd = GeneralListPopupBinding.inflate(LayoutInflater.from(context));
        setContentView(bd.getRoot());
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(20);
        }
        setAnimationStyle(R.style.Animations_GeneralListPopup);

        dataList = new ArrayList<>();
        dataList.add("0");
        dataList.add("1");
        dataList.add("2");
        adapter = new SimpleStringAdapter(getContext(), dataList);
        bd.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //bd.recyclerView.addItemDecoration(new DividerItemDecoration(this));
        bd.recyclerView.setAdapter(adapter);
    }

    public void setOnItemClickListener(BaseRecyclerViewAdapter.OnItemClickListener<String> mListener) {
        if (adapter != null)
            this.adapter.setOnItemClickListener(mListener);
    }

    public static class SimpleStringAdapter extends BaseRecyclerViewAdapter<String> {
        public SimpleStringAdapter(Context context, ArrayList<String> list) {
            super(context, list);
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GeneralPopupWindowAdapterItemBinding bd = GeneralPopupWindowAdapterItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ViewHolder(bd);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ViewHolder holder = (ViewHolder) viewHolder;
            String s = getList().get(position);
            holder.bd.name.setText(s);
            super.setOnItemClickListener(holder.itemView, position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            GeneralPopupWindowAdapterItemBinding bd;

            ViewHolder(GeneralPopupWindowAdapterItemBinding bd) {
                super(bd.getRoot());
                this.bd = bd;

            }
        }
    }

}
