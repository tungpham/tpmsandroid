package com.ethan.morephone.presentation.phone.log;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;

/**
 * Created by Ethan on 3/7/17.
 */

public class CallLogDialog extends DialogFragment {

    private static final String BUNDLE_POS = "BUNDLE_POS";
    private static final String BUNDLE_CONTACT_ID = "BUNDLE_CONTACT_ID";

    public static CallLogDialog getInstance(int pos, String contactId) {
        CallLogDialog dialog = new CallLogDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_POS, pos);
        bundle.putString(BUNDLE_CONTACT_ID, contactId);
        dialog.setArguments(bundle);
        return dialog;
    }

    private CallLogDialogListener mCallLogDialogListener;

    public void setListener(CallLogDialogListener callLogDialogListener) {
        mCallLogDialogListener = callLogDialogListener;
    }

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
        final int pos = getArguments().getInt(BUNDLE_POS);
        final String contactId = getArguments().getString(BUNDLE_CONTACT_ID);
        builder.setTitle(getString(R.string.title_call_log_message));
        builder.setItems(R.array.call_log_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mCallLogDialogListener != null) {
                    if (i == 0) {
                        mCallLogDialogListener.onContact(pos, contactId);
                    } else if (i == 1) {
                        mCallLogDialogListener.onDelete(pos);
                    }
                }
            }
        });
        return builder.create();
    }

    public interface CallLogDialogListener {

        void onContact(int pos, String contactId);

        void onDelete(int pos);
    }
}
