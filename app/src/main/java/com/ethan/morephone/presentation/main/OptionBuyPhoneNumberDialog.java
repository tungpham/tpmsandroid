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

public class OptionBuyPhoneNumberDialog extends DialogFragment {

    public static OptionBuyPhoneNumberDialog getInstance(){
        return new OptionBuyPhoneNumberDialog();
    }

    private OptionBuyPhoneNumberDialog.OptionBuyPhoneNumberListener mOptionBuyPhoneNumberListener;

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
        builder.setTitle(getString(R.string.option_phone_number_dialog_title));
        builder.setItems(R.array.option_phone_number, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which){
                    case 0:
                        if(mOptionBuyPhoneNumberListener != null) mOptionBuyPhoneNumberListener.onOptionBuyPhoneNumber(false);
                        break;
                    case 1:
                        if(mOptionBuyPhoneNumberListener != null) mOptionBuyPhoneNumberListener.onOptionBuyPhoneNumber(true);
                        break;
                    default:
                        break;
                }
            }
        });

        return builder.create();
    }

    public void setOptionPhoneNumberListener(OptionBuyPhoneNumberDialog.OptionBuyPhoneNumberListener optionPhoneNumberListener) {
        mOptionBuyPhoneNumberListener = optionPhoneNumberListener;
    }

    public interface OptionBuyPhoneNumberListener {
        void onOptionBuyPhoneNumber(boolean pool);
    }
}
