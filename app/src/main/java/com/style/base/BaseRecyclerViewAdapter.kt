package com.style.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.toast.ToastManager

import com.style.utils.LogManager
import com.style.utils.DeviceInfoUtil

import java.util.ArrayList

/**
 * Created by XiaJun on 2015/7/2.
 */
abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    fun addData(e: T) {
        this.list.add(e)
        notifyDataSetChanged()
    }

    fun addData(list: ArrayList<T>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun getData(position: Int): T {
        return list[position]
    }

    fun removeData(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeData(e: T) {
        this.list.remove(e)
        notifyDataSetChanged()
    }

    fun updateData(position: Int, e: T) {
        list[position] = e
        notifyItemChanged(position)
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

    fun showToast(str: CharSequence) {
        ToastManager.showToast(context, str)
    }

    fun showToast(@StringRes resId: Int) {
        showToast(context.getText(resId))
    }

    fun logE(tag: String, msg: String) {
        LogManager.logE(tag, msg)
    }

    protected fun dp2px(dpValue: Float): Int {
        return DeviceInfoUtil.dp2px(context, dpValue)
    }

}