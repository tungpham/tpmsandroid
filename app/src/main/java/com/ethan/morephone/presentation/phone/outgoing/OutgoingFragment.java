package com.ethan.morephone.presentation.phone.outgoing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.widget.MyTextView;

/**
 * Created by Ethan on 4/12/17.
 */

public class OutgoingFragment extends BaseFragment implements View.OnClickListener {


    public static final String BUNDLE_PHONE_NUMBER_OUTGOING = "BUNDLE_PHONE_NUMBER_OUTGOING";

    public static OutgoingFragment getInstance(String phoneNumberOutgoing) {
        OutgoingFragment fragment = new OutgoingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER_OUTGOING, phoneNumberOutgoing);
        fragment.setArguments(bundle);
        return fragment;
    }

    private OutgoingFragmentListener mOutgoingFragmentListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outgoing, container, false);

        String phoneNumberOutgoing = getArguments().getString(BUNDLE_PHONE_NUMBER_OUTGOING);

        MyTextView textPhoneNumber = (MyTextView) view.findViewById(R.id.text_outgoing_phone_number);
        textPhoneNumber.setText(phoneNumberOutgoing);

        view.findViewById(R.id.floating_button_outgoing_hang_up).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_button_outgoing_hang_up:
                if(mOutgoingFragmentListener != null) mOutgoingFragmentListener.onHangup();
                break;
            default:
                break;
        }
    }

    public void setOutGoingFragmentListener(OutgoingFragmentListener outGoingFragmentListener) {
        mOutgoingFragmentListener = outGoingFragmentListener;
    }

    public interface OutgoingFragmentListener {
        void onHangup();
    }
}
