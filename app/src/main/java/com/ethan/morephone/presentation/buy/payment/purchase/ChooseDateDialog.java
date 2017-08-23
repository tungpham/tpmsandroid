package com.ethan.morephone.presentation.buy.payment.purchase;

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
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;

import java.util.Calendar;

/**
 * Created by Ethan on 12/28/16.
 */

public class ChooseDateDialog extends DialogFragment {

    private static final String BUNDLE_CURRENT_DATE = "BUNDLE_CURRENT_DATE";

    public static ChooseDateDialog getInstance(long date) {
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_CURRENT_DATE, date);
        ChooseDateDialog configureEmailDialog = new ChooseDateDialog();
        configureEmailDialog.setArguments(bundle);
        return configureEmailDialog;
    }

    private ChooseDateDialogListener mChooseDateDialogListener;

    private long mCurrentDate = System.currentTimeMillis();

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
        builder.setTitle(getString(R.string.configure_email_dialog_title));

        final CalendarView calendarView = new CalendarView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.leftMargin = 16;
        lp.rightMargin = 16;
        calendarView.setLayoutParams(lp);
        builder.setView(calendarView);
        calendarView.setMinDate(System.currentTimeMillis());

        mCurrentDate = getArguments().getLong(BUNDLE_CURRENT_DATE);
        calendarView.setDate(mCurrentDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                mCurrentDate = calendar.getTimeInMillis();
            }
        });

        builder.setPositiveButton(getString(R.string.configure_email_dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCurrentDate < System.currentTimeMillis()) {
                    Toast.makeText(getContext(), getString(R.string.purchase_expire_date_error), Toast.LENGTH_SHORT).show();
                }
                if (mChooseDateDialogListener != null) mChooseDateDialogListener.chooseDate(mCurrentDate);
            }
        });

        builder.setNegativeButton(getString(R.string.configure_email_dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    public void setChooseDateDialogListener(ChooseDateDialogListener chooseDateDialogListener) {
        mChooseDateDialogListener = chooseDateDialogListener;
    }

    public interface ChooseDateDialogListener {
        void chooseDate(long date);
    }

}
