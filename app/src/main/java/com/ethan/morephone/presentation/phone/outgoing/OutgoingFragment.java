package com.ethan.morephone.presentation.phone.outgoing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.phone.service.PhoneService;
import com.ethan.morephone.widget.MyTextView;

/**
 * Created by Ethan on 4/12/17.
 */

public class OutgoingFragment extends BaseFragment implements View.OnClickListener {


    public static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";
    public static final String BUNDLE_FROM_PHONE_NUMBER = "BUNDLE_FROM_PHONE_NUMBER";

    public static OutgoingFragment getInstance(String fromPhoneNumber, String toPhoneNumber) {
        OutgoingFragment fragment = new OutgoingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_FROM_PHONE_NUMBER, fromPhoneNumber);
        bundle.putString(BUNDLE_TO_PHONE_NUMBER, toPhoneNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String mFromPhoneNumber;
    private String mToPhoneNumber;

    private ImageView mImageMute;
    private ImageView mImageSpeaker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outgoing, container, false);

        mFromPhoneNumber = getArguments().getString(BUNDLE_FROM_PHONE_NUMBER);
        mToPhoneNumber = getArguments().getString(BUNDLE_TO_PHONE_NUMBER);

        MyTextView textPhoneNumber = (MyTextView) view.findViewById(R.id.text_outgoing_phone_number);
        textPhoneNumber.setText(mToPhoneNumber);

        view.findViewById(R.id.floating_button_outgoing_hang_up).setOnClickListener(this);

        mImageMute = (ImageView) view.findViewById(R.id.image_outgoing_mute);
        mImageMute.setOnClickListener(this);

        mImageSpeaker = (ImageView) view.findViewById(R.id.image_outgoing_speaker);
        mImageSpeaker.setOnClickListener(this);

        changeMuteMicrophone();
        changeSpeakerPhone();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_button_outgoing_hang_up:
                PhoneService.startServiceWithAction(getContext(), PhoneService.ACTION_HANG_UP, mFromPhoneNumber, mToPhoneNumber);
                break;
            case R.id.image_outgoing_mute:
                MyPreference.setMuteMicrophone(getContext(), !MyPreference.getMuteMicrophone(getContext()));
                changeMuteMicrophone();
                PhoneService.startServiceWithAction(getContext(), PhoneService.ACTION_MUTE_MICOPHONE, mFromPhoneNumber, mToPhoneNumber);
                break;
            case R.id.image_outgoing_speaker:
                MyPreference.setSpeakerphone(getContext(), !MyPreference.getSpeakerphone(getContext()));
                changeSpeakerPhone();
                PhoneService.startServiceWithAction(getContext(), PhoneService.ACTION_SPEAKER_PHONE, mFromPhoneNumber, mToPhoneNumber);
                break;
            default:
                break;
        }
    }

    private void changeMuteMicrophone() {
        if (MyPreference.getMuteMicrophone(getContext())) {
            mImageMute.setImageResource(R.drawable.ic_mic_off);
        } else {
            mImageMute.setImageResource(R.drawable.ic_mic_on);
        }
    }

    private void changeSpeakerPhone() {
        if (MyPreference.getSpeakerphone(getContext())) {
            mImageSpeaker.setImageResource(R.drawable.ic_speaker_on);
        } else {
            mImageSpeaker.setImageResource(R.drawable.ic_speaker_off);
        }
    }

}
