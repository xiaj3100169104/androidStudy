package com.style.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.style.framework.R;
import com.style.manager.LogManager;
import com.style.manager.ToastManager;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseListAdapter<E> extends BaseAdapter {
    public List<E> list;
    public Context mContext;
    public LayoutInflater mInflater;
    private OnItemClickListener mListener;

    public abstract Integer getItemViewResId();

    public abstract BaseViewHolder onCreateViewHolder(View convertView);

    public abstract void onBindViewHolder(BaseViewHolder viewHolder, int position, E data);

    public BaseListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public BaseListAdapter(Context context, E[] s) {
        this.mContext = context;
        setData(s);
        mInflater = LayoutInflater.from(context);
    }

    public BaseListAdapter(Context context, List<E> list) {
        this.mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void add(E e) {
        this.list.add(e);
        notifyDataSetChanged();
    }

    public void setData(E[] data) {
        if (data != null) {
            setList(Arrays.asList(data));
        }
    }

    public void addData(E[] data) {
        if (data != null && data.length > 0) {
            addAll(Arrays.asList(data));
        }
    }

    public void addAll(List<E> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public void remove(E e) {
        this.list.remove(e);
        notifyDataSetChanged();
    }

    public void update(int position, E e) {
        list.set(position, e);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int ItemId) {
        return ItemId;
    }

    public boolean isSetOnItemClickListener() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        if (null == convertView) {
            convertView = mInflater.inflate(getItemViewResId(), parent, false);
        }
        BaseViewHolder holder = onCreateViewHolder(convertView);
        E data = list.get(position);
        onBindViewHolder(holder, position, data);
        if (isSetOnItemClickListener())
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(index, list.get(index));
                }
            });
        return holder.itemView;
    }

    public static class BaseViewHolder {
        public View itemView;

        public BaseViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        if (mListener != null)
            this.mListener = mListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

    public void showToast(String str) {
        ToastManager.showToast(mContext, str);
    }

    public void showToast(int resId) {
        ToastManager.showToast(mContext, resId);
    }

    public void showToast(JSONObject response) {
        ToastManager.showToast(mContext, response.optString("msg"));
    }

    public void showToastRequestFailure() {
        showToast(R.string.request_fail);
    }


    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getItemHeight() {
        int itemHeight = 0;
        View view = LayoutInflater.from(mContext).inflate(getItemViewResId(), null);
        if (view != null) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                    .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            itemHeight = view.getMeasuredHeight();
        }

        return itemHeight;
    }

    public int getAllItemHeight() {
        return this.list.size() * getItemHeight();
    }
}
