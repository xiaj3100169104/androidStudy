package example.vlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.style.app.LogManager;
import com.style.app.ToastManager;
import com.style.utils.CommonUtil;
import com.style.utils.DeviceInfoUtil;

import java.util.ArrayList;

/**
 * Created by XiaJun on 2015/7/2.
 */
public abstract class BaseDelegateAdapter<T> extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {
    protected String TAG = getClass().getSimpleName();

    public Context mContext;
    public LayoutInflater mInflater;
    public ArrayList<T> list;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseDelegateAdapter(Context context, ArrayList<T> dataList) {
        this.list = dataList;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public Context getContext() {
        return mContext;
    }
    public ArrayList<T> getList() {
        return list;
    }

    public void setData(ArrayList<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(T e) {
        this.list.add(e);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public T getData(int position) {
        return list.get(position);
    }

    public void removeData(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
    }

    public void removeData(T e) {
        this.list.remove(e);
        notifyDataSetChanged();
    }

    public void updateData(int position, T e) {
        list.set(position, e);
        notifyItemChanged(position);
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(View holder, final int position) {
        if (onItemClickListener != null) {
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(position, getData(position));
                }
            });
        }
    }

    public void setOnItemLongClickListener(View holder, final int position) {
        if (onItemLongClickListener != null) {
            holder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, position, getData(position));
                    return true;
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        if (mListener != null)
            this.onItemClickListener = mListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mListener) {
        if (mListener != null)
            this.onItemLongClickListener = mListener;
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View itemView, int position, T data);
    }

    public void showToast(String str) {
        ToastManager.showToast(mContext, str);
    }

    public void showToast(int resId) {
        ToastManager.showToast(mContext, resId);
    }

    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    protected int dp2px(float dpValue) {
        return DeviceInfoUtil.dp2px(mContext, dpValue);
    }

}