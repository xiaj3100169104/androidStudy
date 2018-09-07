package example.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.FragmentHome1Binding

import example.activity.MyRadioGroupActivity
import example.customview.activity.CustomViewMainActivity
import example.gesture.DispatchGestureActivity
import example.gesture.SimpleGestureActivity
import example.gesture.TestGestureActivity
import example.dialog.WheelActivity
import example.address.AddressActivity
import example.album.SelectLocalPictureActivity
import example.dialog.DialogActivity
import example.drag.DragActivity
import example.filedown.FileDownActivity
import example.music.MusicListActivity
import example.music.remote.RemotePlayActivity
import example.softInput.StatusBarStyleMainActivity
import example.tablayout.TabLayoutActivity
import example.vlayout.MultiTypeActivity
import fussen.cc.barchart.activity.CustomScrollMainActivity


class HomeFragment1 : BaseFragment() {

    private lateinit var bd: FragmentHome1Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_home_1, container, false)
        return bd.root
    }

    override fun initData() {
        bd.onItemClickListener = OnItemClickListener()
        bd.refreshLayout.setOnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
        }
        bd.refreshLayout.setOnLoadMoreListener { refreshLayout ->
            refreshLayout.finishLoadMore(2000/*,false*/)//传入false表示加载失败
        }
    }

    inner class OnItemClickListener {
        fun skip01(v: View) {
            skip(CustomViewMainActivity::class.java)
        }

        fun skip02(v: View) {
            skip(CustomScrollMainActivity::class.java)
        }

        fun skip42(v: View) {
            skip(MultiTypeActivity::class.java)
        }

        fun skip1(v: View) {
            skip(SelectLocalPictureActivity::class.java)
        }

        fun skip2(v: View) {
            skip(AddressActivity::class.java)
        }

        fun skip3(v: View) {
            skip(MyRadioGroupActivity::class.java)
        }

        fun skip31(v: View) {
            skip(DialogActivity::class.java)
        }

        fun skip4(v: View) {
            skip(WheelActivity::class.java)
        }

        fun skip6(v: View) {
            skip(FileDownActivity::class.java)
        }

        fun skip9(v: View) {
            skip(SimpleGestureActivity::class.java)
        }

        fun skip91(v: View) {
            skip(DispatchGestureActivity::class.java)
        }

        fun skip10(v: View) {
            skip(TestGestureActivity::class.java)
        }

        fun skip11(v: View) {
            skip(StatusBarStyleMainActivity::class.java)
        }

        fun skip12(v: View) {
            skip(TabLayoutActivity::class.java)
        }

        fun skip13(v: View) {
            skip(DragActivity::class.java)
        }

        fun skip14(v: View) {
            skip(MusicListActivity::class.java)
        }

        fun skip15(v: View) {

            skip(RemotePlayActivity::class.java)

        }
    }
}