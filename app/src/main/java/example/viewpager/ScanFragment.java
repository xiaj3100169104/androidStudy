package example.viewpager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmcbig.mediapicker.entity.Media;
import com.style.framework.R;
import com.style.framework.databinding.FragmentImageScanBinding;
import com.style.glide.ImageLoader;

/**
 * Created by dmcBig on 2017/8/16.
 */

public class ScanFragment extends Fragment {
    FragmentImageScanBinding bd;

    public static ScanFragment newInstance(Media media, String label) {
        ScanFragment f = new ScanFragment();
        Bundle b = new Bundle();
        b.putParcelable("media", media);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_image_scan, container, false);
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Media media = getArguments().getParcelable("media");
        //jvm能申请的最大内存
        Log.e("maxMemory", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
        //jvm已经申请到的内存
        Log.e("totalMemory", Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
        //jvm剩余空闲内存
        Log.e("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M");
        //Glide.get(getContext()).clearMemory();
        ImageLoader.load(this, media.path, bd.iv);
        //skipMemoryCache(true) ，跳过内存缓存。
        //diskCacheStrategy(DiskCacheStrategy.NONE) ，不要在disk硬盘中缓存。

        // 这两个函数同时联合使用，使得Glide针对这一次的资源加载放弃内存缓存和硬盘缓存，相当于一次全新的请求。这样就迫使Glide从给定的资源地址发起全新的数据加载，而非从旧有的缓存中取缓存使用。
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
