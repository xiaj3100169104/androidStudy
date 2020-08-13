package com.style.base

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.toast.ToastManager

import com.style.utils.LogManager

import java.util.ArrayList

/**
 * Created by XiaJun on 2015/7/2.
 */
abstract class BaseRecyclerViewAdapter<T> : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder> {
    protected var TAG = javaClass.simpleName

    private var context: Context
    var list: ArrayList<T>

    private var mInflater: LayoutInflater
    private var onItemClickListener: OnItemClickListener<T>? = null
    private var onItemLongClickListener: OnItemLongClickListener<T>? = null

    constructor(context: Context?, list: ArrayList<T>) : super() {
        this.context = context!!
        this.list = list
        this.mInflater = LayoutInflater.from(context)
    }

    fun getContext(): Context {
        return context
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun <T : ViewDataBinding> getBinding(@LayoutRes layoutId: Int, parent: ViewGroup?): T {
        return DataBindingUtil.bind(mInflater.inflate(layoutId, parent, false))!!
    }

    fun setData(list: ArrayList<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun getData(position: Int): T {
        return list[position]
    }

    fun clearData() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(holder: View, position: Int) {
        if (onItemClickListener != null) {
            holder.setOnClickListener {
                onItemClickListener?.onItemClick(position, getData(position))
            }
        }
    }

    fun setOnItemLongClickListener(holder: View, position: Int) {
        if (onItemLongClickListener != null) {
            holder.setOnLongClickListener { v ->
                onItemLongClickListener?.onItemLongClick(v, position, getData(position))
                true
            }
        }
    }

    fun setOnItemClickListener(mListener: OnItemClickListener<T>?) {
        if (mListener != null)
            this.onItemClickListener = mListener
    }

    fun getOnItemClickListener(): OnItemClickListener<T>? {
        return this.onItemClickListener
    }

    interface OnItemClickListener<T> {
        fun onItemClick(position: Int, data: T)
    }

    fun setOnItemLongClickListener(mListener: OnItemLongClickListener<T>?) {
        if (mListener != null)
            this.onItemLongClickListener = mListener
    }

    interface OnItemLongClickListener<T> {
        fun onItemLongClick(itemView: View, position: Int, data: T)
    }

    fun logE(tag: String, msg: String) {
        LogManager.logE(tag, msg)
    }

    protected fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics()).toInt()
    }

}