package com.ethan.morephone.presentation.review;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.Utils;

/**
 * Created by Ethan on 12/11/16.
 */

public class AlertReviewDialog extends DialogFragment {

    public static AlertReviewDialog getInstance() {
        return new AlertReviewDialog();
    }


    @Override
    public void show(FragmentManager manager, String tag) {

        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.title_dialog_rate));
        builder.setMessage(String.format(getString(R.string.title_rate_like), MyPreference.getTimesUse(getContext())));
        builder.setPositiveButton(getString(R.string.btn_rate_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Utils.isNetworkAvailable(getContext())) {
                    MyPreference.setPrefRateNow(getContext(), true);
                    Utils.rateApp(getContext());
                } else {
                    Toast.makeText(getContext(), getString(R.string.message_error_lost_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.btn_not_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyPreference.setPrefNotNow(getContext(), true);
            }
        });

        return builder.create();
    }

}
