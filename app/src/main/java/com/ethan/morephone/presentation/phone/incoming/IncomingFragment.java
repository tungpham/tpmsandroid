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

    private static String BUNDLE_PHONE_NUMBER_INCOMING = "BUNDLE_PHONE_NUMBER_INCOMING";

    public static IncomingFragment getInstance(String phoneNumberIncoming) {
        IncomingFragment incomingFragment = new IncomingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER_INCOMING, phoneNumberIncoming);
        incomingFragment.setArguments(bundle);
        return incomingFragment;
    }

    private IncomingListener mIncomingListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incoming, container, false);

        String phoneNumberIncoming = getArguments().getString(BUNDLE_PHONE_NUMBER_INCOMING);

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
                if (mIncomingListener != null) mIncomingListener.decline();
                break;
            case R.id.floating_button_incoming_accept:
                if (mIncomingListener != null) mIncomingListener.accept();
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
