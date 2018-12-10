package example.viewPagerCards.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.style.framework.R;

import example.viewPagerCards.CardAdapter;


public class CardFragment extends Fragment {

    private CardView mCardView;
    private Button mBtn;
    private int i = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i = getArguments().getInt("data");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_cards_fragments_fragment, container, false);
        mCardView = view.findViewById(R.id.cardView);
        mBtn = view.findViewById(R.id.iv_btn);
        float elevation = mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR;
        mCardView.setMaxCardElevation(elevation);
        mBtn.setText(i + "");
        mBtn.setOnClickListener(v -> Log.e("CardFragment", i + ""));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public CardView getCardView() {
        return mCardView;
    }
}
