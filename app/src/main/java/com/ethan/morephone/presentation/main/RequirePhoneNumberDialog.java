package com.ethan.morephone.presentation.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 4/17/17.
 */

public class RequirePhoneNumberDialog extends DialogFragment {

    public static RequirePhoneNumberDialog getInstance(){
        return new RequirePhoneNumberDialog();
    }

    private RequirePhoneNumberDialog.RequirePhoneNumberListener mRequirePhoneNumberListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.require_phone_number_dialog_title));
        builder.setMessage(getString(R.string.require_phone_number_dialog_message));
        builder.setPositiveButton(getString(R.string.require_phone_number_dialog_choose), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mRequirePhoneNumberListener != null) mRequirePhoneNumberListener.onChoosePhone();
            }
        });
        builder.setNegativeButton(getString(R.string.require_phone_number_dialog_buy), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mRequirePhoneNumberListener != null) mRequirePhoneNumberListener.onBuyPhone();
            }
        });
        AppCompatDialog dialog = builder.create();
        return dialog;
    }

    public void setRequirePhoneNumberListener(RequirePhoneNumberDialog.RequirePhoneNumberListener requirePhoneNumberListener) {
        mRequirePhoneNumberListener = requirePhoneNumberListener;
    }

    public interface RequirePhoneNumberListener {
        void onChoosePhone();
        void onBuyPhone();
    }
}
