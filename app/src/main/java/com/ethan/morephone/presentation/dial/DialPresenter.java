package com.ethan.morephone.presentation.dial;

import android.support.annotation.NonNull;

/**
 * Created by Ethan on 3/13/17.
 */

public class DialPresenter implements DialContract.Presenter {

    private final DialContract.View mView;

    public DialPresenter(@NonNull DialContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
