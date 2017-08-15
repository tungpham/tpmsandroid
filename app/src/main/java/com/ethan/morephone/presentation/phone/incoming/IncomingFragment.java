package com.ethan.morephone.presentation.phone.incoming;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.widget.MyTextView;

/**
 * Created by Ethan on 4/11/17.
 */

public class IncomingFragment extends BaseFragment implements View.OnClickListener {

    public static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";
    public static final String BUNDLE_FROM_PHONE_NUMBER = "BUNDLE_FROM_PHONE_NUMBER";

    public static IncomingFragment getInstance(String fromPhoneNumber, String toPhoneNumber) {
        IncomingFragment incomingFragment = new IncomingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_FROM_PHONE_NUMBER, fromPhoneNumber);
        bundle.putString(BUNDLE_TO_PHONE_NUMBER, toPhoneNumber);
        incomingFragment.setArguments(bundle);
        return incomingFragment;
    }

    private IncomingListener mIncomingListener;
    private boolean isClick = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incoming, container, false);

        String phoneNumberIncoming = getArguments().getString(BUNDLE_FROM_PHONE_NUMBER);

        MyTextView textPhoneNumber = (MyTextView) view.findViewById(R.id.text_incoming_phone_number);
        textPhoneNumber.setText(phoneNumberIncoming);

        view.findViewById(R.id.floating_button_incoming_decline).setOnClickListener(this);
        view.findViewById(R.id.floating_button_incoming_accept).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_button_incoming_decline:
                if (mIncomingListener != null && !isClick) {
                    mIncomingListener.decline();
                    isClick = !isClick;
                }
                break;
            case R.id.floating_button_incoming_accept:
                if (mIncomingListener != null && !isClick) {
                    mIncomingListener.accept();
                    isClick = !isClick;
                }
                break;
            default:
                break;
        }
    }

    public void setIncomingListener(IncomingListener incomingListener) {
        mIncomingListener = incomingListener;
    }

    public interface IncomingListener {
        void decline();

        void accept();
    }


}
