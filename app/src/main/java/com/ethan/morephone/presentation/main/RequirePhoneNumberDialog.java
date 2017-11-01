package com.ethan.morephone.presentation.main;

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
 * Created by Ethan on 4/17/17.
 */

public class RequirePhoneNumberDialog extends DialogFragment {

    public static RequirePhoneNumberDialog getInstance(){
        return new RequirePhoneNumberDialog();
    }

    private RequirePhoneNumberDialog.RequirePhoneNumberListener mRequirePhoneNumberListener;

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
        builder.setTitle(getString(R.string.require_phone_number_dialog_title));
        builder.setMessage(getString(R.string.require_phone_number_dialog_message));
        builder.setPositiveButton(getString(R.string.require_phone_number_dialog_buy), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mRequirePhoneNumberListener != null) mRequirePhoneNumberListener.onBuyPhone();
            }
        });
        return builder.create();
    }

    public void setRequirePhoneNumberListener(RequirePhoneNumberDialog.RequirePhoneNumberListener requirePhoneNumberListener) {
        mRequirePhoneNumberListener = requirePhoneNumberListener;
    }

    public interface RequirePhoneNumberListener {
        void onBuyPhone();
    }
}
