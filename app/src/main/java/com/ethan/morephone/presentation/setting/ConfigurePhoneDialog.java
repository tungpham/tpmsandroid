package com.ethan.morephone.presentation.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;

/**
 * Created by Ethan on 12/28/16.
 */

public class ConfigurePhoneDialog extends DialogFragment {

    private static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static ConfigurePhoneDialog getInstance(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        ConfigurePhoneDialog configurePhoneDialog = new ConfigurePhoneDialog();
        configurePhoneDialog.setArguments(bundle);
        return configurePhoneDialog;
    }

    private ConfigurePhoneListener mConfigurePhoneListener;

    @Override
    public void show(FragmentManager manager, String tag) {

        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            DebugTool.logD("Exception" + e.getMessage());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.configure_phone_dialog_title));

        final AppCompatEditText input = new AppCompatEditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.leftMargin = 16;
        lp.rightMargin = 16;
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);
        input.setText(getArguments().getString(BUNDLE_PHONE_NUMBER));

        builder.setPositiveButton(getString(R.string.configure_phone_dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = input.getText().toString();
//                if (!TextUtils.isEmpty(phoneNumber)) {
                if (mConfigurePhoneListener != null) {
                    mConfigurePhoneListener.configurePhone(phoneNumber);
                }
                dismiss();
//                } else {
//                    Toast.makeText(getContext(), getString(R.string.configure_phone_dialog_empty), Toast.LENGTH_SHORT).show();
//                }
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

    public void setConfigurePhoneListener(ConfigurePhoneListener configurePhoneListener) {
        mConfigurePhoneListener = configurePhoneListener;
    }

    public interface ConfigurePhoneListener {
        void configurePhone(String phoneNumber);
    }
}
