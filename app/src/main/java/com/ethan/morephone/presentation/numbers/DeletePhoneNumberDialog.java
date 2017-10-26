package com.ethan.morephone.presentation.numbers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 4/17/17.
 */

public class DeletePhoneNumberDialog extends DialogFragment {

    public static final String BUNDLE_POSITION_PHONE_NUMBER = "BUNDLE_POSITION_PHONE_NUMBER";

    public static DeletePhoneNumberDialog getInstance(int pos){
        DeletePhoneNumberDialog deletePhoneNumberDialog = new DeletePhoneNumberDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_POSITION_PHONE_NUMBER, pos);
        deletePhoneNumberDialog.setArguments(bundle);
        return deletePhoneNumberDialog;
    }

    private DeletePhoneNumberListener mDeletePhoneNumberListener;

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

        final int position = getArguments().getInt(BUNDLE_POSITION_PHONE_NUMBER);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete_phone_number_dialog_title));
        builder.setMessage(getString(R.string.delete_phone_number_dialog_message));
        builder.setPositiveButton(getString(R.string.delete_phone_number_dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mDeletePhoneNumberListener != null) mDeletePhoneNumberListener.onDelete(position);
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }

    public void setDeletePhoneNumberListener(DeletePhoneNumberListener deletePhoneNumberListener) {
        mDeletePhoneNumberListener = deletePhoneNumberListener;
    }

    public interface DeletePhoneNumberListener {
        void onDelete(int pos);
    }
}
