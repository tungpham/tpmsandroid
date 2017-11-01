package com.ethan.morephone.presentation.buy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 12/11/16.
 */

public class AlertGetCountryDialog extends DialogFragment {



    public static AlertGetCountryDialog getInstance() {
        return new AlertGetCountryDialog();
    }

    private AlertGetCountryListener mAlertGetCountryListener;

    @Override
    public void show(FragmentManager manager, String tag) {

        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException ignored) {
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.get_country_dialog_title));
        builder.setMessage(getString(R.string.get_country_dialog_message));
        builder.setPositiveButton(getString(R.string.get_country_dialog_load), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            if(mAlertGetCountryListener != null) mAlertGetCountryListener.onLoadCountry();
            }
        });

        return builder.create();
    }

    public void setAlertGetCountryListener(AlertGetCountryListener recordListener) {
        mAlertGetCountryListener = recordListener;
    }

    public interface AlertGetCountryListener {
        void onLoadCountry();
    }

}
