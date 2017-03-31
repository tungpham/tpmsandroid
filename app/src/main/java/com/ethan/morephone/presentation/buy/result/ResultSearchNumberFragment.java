package com.ethan.morephone.presentation.buy.result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;

/**
 * Created by Ethan on 3/31/17.
 */

public class ResultSearchNumberFragment extends BaseFragment {

    public static ResultSearchNumberFragment getInstance() {
        return new ResultSearchNumberFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_search_number, container, false);
        return view;
    }

}
