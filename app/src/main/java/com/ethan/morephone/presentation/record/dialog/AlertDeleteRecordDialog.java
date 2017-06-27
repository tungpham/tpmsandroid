package com.ethan.morephone.presentation.record.dialog;

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

public class AlertDeleteRecordDialog extends DialogFragment {

    private static final String BUNDLE_RECORD_ID = "BUNDLE_RECORD_ID";
    private static final String BUNDLE_CALL_ID = "BUNDLE_CALL_ID";
    private static final String BUNDLE_POSITION = "BUNDLE_POSITION";

    public static AlertDeleteRecordDialog getInstance(String callSid, String recordSid, int position) {
        AlertDeleteRecordDialog alertDeleteRecordDialog = new AlertDeleteRecordDialog();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_CALL_ID, callSid);
        bundle.putString(BUNDLE_RECORD_ID, recordSid);
        bundle.putInt(BUNDLE_POSITION, position);
        alertDeleteRecordDialog.setArguments(bundle);
        return alertDeleteRecordDialog;
    }

    private AlertDeleteRecordListener mAlertDeleteRecordListener;

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

        final String recordSid = getArguments().getString(BUNDLE_RECORD_ID);
        final String callSid = getArguments().getString(BUNDLE_RECORD_ID);
        final int position = getArguments().getInt(BUNDLE_POSITION);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.alert_delete_record_dialog_title));
        builder.setMessage(getString(R.string.alert_delete_record_dialog_message));
        builder.setPositiveButton(getString(R.string.alert_delete_record_dialog_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            if(mAlertDeleteRecordListener != null) mAlertDeleteRecordListener.onDelete(callSid, recordSid, position);
            }
        });
        builder.setNegativeButton(getString(R.string.alert_delete_record_dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void setAlertDeleteRecordListener(AlertDeleteRecordListener recordListener) {
        mAlertDeleteRecordListener = recordListener;
    }

    public interface AlertDeleteRecordListener {
        void onDelete(String callSid, String recordSid, int position);
    }

}
