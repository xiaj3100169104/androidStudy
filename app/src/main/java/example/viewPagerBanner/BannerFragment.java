package example.viewPagerBanner;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.style.data.glide.ImageLoader;
import com.style.framework.R;
import com.style.framework.databinding.BannerFragmentBinding;


public class BannerFragment extends Fragment {

    BannerFragmentBinding bd;
    private int imageResId = 0;

    public static final BannerFragment newInstance(int resId) {
        BannerFragment instance = new BannerFragment();
        Bundle b = new Bundle();
        b.putInt("imageResId", resId);
        instance.setArguments(b);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageResId = getArguments().getInt("imageResId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = BannerFragmentBinding.inflate(inflater, container, false);
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //bd.ivBanner.setImageResource(imageResId);
        ImageLoader.load(this, getContext().getResources().getDrawable(imageResId), bd.ivBanner);
        bd.ivBanner.setOnClickListener(v -> Log.e(String.valueOf(imageResId), "OnClick"));
        bd.ivBanner.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                Log.e(String.valueOf(imageResId), "onPhotoTap");
            }
        });
        bd.ivBanner.setOnLongClickListener(v -> {
            Log.e(String.valueOf(imageResId), "OnLongClick");
            return true;
        });
    }
}
