package com.ethan.morephone.presentation.message.list;

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

public class MessageDialog extends DialogFragment {

    private static final String BUNDLE_POS = "BUNDLE_POS";

    public static MessageDialog getInstance(int pos) {
        MessageDialog dialog = new MessageDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_POS, pos);
        dialog.setArguments(bundle);
        return dialog;
    }

    private MessageDialogListener mMessageDialogListener;

    public void setListener(MessageDialogListener messageDialogListener) {
        mMessageDialogListener = messageDialogListener;
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
        builder.setTitle(getString(R.string.title_dialog_message));
        builder.setItems(R.array.message_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mMessageDialogListener != null) {
                    if (i == 0) {
                        mMessageDialogListener.onCopyText(pos);
                    } else if (i == 1) {
                        mMessageDialogListener.onDelete(pos);
                    }
                }
            }
        });
        return builder.create();
    }

    public interface MessageDialogListener {

        void onCopyText(int pos);

        void onDelete(int pos);
    }
}
