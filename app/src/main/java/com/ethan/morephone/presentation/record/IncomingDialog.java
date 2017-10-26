package com.ethan.morephone.presentation.record;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Ethan on 4/14/17.
 */

public class IncomingDialog extends DialogFragment {

    public static IncomingDialog newInstance() {
        return new IncomingDialog();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle("Incoming")
                .setPositiveButton("Accept",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                ((TestVoiceActivity)getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton("Reject",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                ((TestVoiceActivity)getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();
    }

}
