package com.ethan.morephone.presentation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 12/28/16.
 */

public class BaseFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ProgressDialog mProgressDialog;

    protected void showProgress() {
        if (mProgressDialog == null && isAdded()) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.message_progress_dialog));
//            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    protected void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && isAdded()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }
}
