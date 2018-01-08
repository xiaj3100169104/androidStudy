package example.tablayout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.framework.R;
import com.style.framework.databinding.FragmentTablayoutBinding;


public class TabLayoutFragment extends Fragment {

    FragmentTablayoutBinding bd;
    private String param = "null";

    public static final TabLayoutFragment newInstance(String i) {
        TabLayoutFragment instance = new TabLayoutFragment();
        Bundle b = new Bundle();
        b.putString("num", i);
        instance.setArguments(b);
        return instance;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(param, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        param = getArguments().getString("num");
        Log.e(param, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(param, "onCreateView");
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_tablayout, container, false);
        return bd.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e(param, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        bd.tvContent.setText(param);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(param, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(param, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(param, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(param, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(param, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(param, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(param, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(param, "onDetach");
    }

}
