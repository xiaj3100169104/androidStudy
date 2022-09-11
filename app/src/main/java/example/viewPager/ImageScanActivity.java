package example.viewPager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.dmcbig.mediapicker.entity.Media;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.data.glide.ImageLoader;
import com.style.framework.databinding.ActivityImageScanBinding;
import com.style.framework.databinding.FragmentImageScanBinding;

import java.util.ArrayList;


public class ImageScanActivity extends AppCompatActivity {

    ActivityImageScanBinding bd;
    ArrayList<Media> preRawList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        bd = ActivityImageScanBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        preRawList = getIntent().getParcelableArrayListExtra("list");
        setView();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    private void setView() {
        bd.tvIndex.setText(1 + "/" + preRawList.size());
        ImageScanAdapter adapter = new ImageScanAdapter(this, preRawList);
        bd.viewpager2.setAdapter(adapter);
        bd.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bd.tvIndex.setText((position + 1) + "/" + preRawList.size());
            }
        });
    }

    public static class ImageScanAdapter extends BaseRecyclerViewAdapter<Media> {

        public ImageScanAdapter(Context context, ArrayList<Media> list) {
            super(context, list);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FragmentImageScanBinding bd = FragmentImageScanBinding.inflate(getLayoutInflater(), parent, false);
            return new ViewHolder(bd);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ViewHolder h = (ViewHolder) viewHolder;
            Media m = getList().get(position);
            //jvm能申请的最大内存
            Log.e("maxMemory", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
            //jvm已经申请到的内存
            Log.e("totalMemory", Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
            //jvm剩余空闲内存
            Log.e("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M");
            //Glide.get(getContext()).clearMemory();
            ImageLoader.load((Activity) this.getContext(), m.path, h.bd.iv);
            //skipMemoryCache(true) ，跳过内存缓存。
            //diskCacheStrategy(DiskCacheStrategy.NONE) ，不要在disk硬盘中缓存。

            // 这两个函数同时联合使用，使得Glide针对这一次的资源加载放弃内存缓存和硬盘缓存，相当于一次全新的请求。这样就迫使Glide从给定的资源地址发起全新的数据加载，而非从旧有的缓存中取缓存使用。

        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            FragmentImageScanBinding bd;

            ViewHolder(FragmentImageScanBinding bd) {
                super(bd.getRoot());
                this.bd = bd;
            }
        }
    }
}
