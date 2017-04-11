package com.ethan.morephone.presentation.phone.incall;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;

/**
 * Created by Ethan on 4/10/17.
 */

public class InCallFragment extends BaseFragment implements View.OnClickListener {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    public static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";

    public static InCallFragment getInstance(String fromPhoneNumber, String toPhoneNumber) {
        InCallFragment fragment = new InCallFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, fromPhoneNumber);
        bundle.putString(BUNDLE_TO_PHONE_NUMBER, toPhoneNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_INVALID;
    private boolean speakerPhone;

    private ImageView mImageSpeaker;

    private InCallListener mInCallListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_call, container, false);

        TextView textToPhoneNumber = (TextView) view.findViewById(R.id.text_in_call_phone_number);

        String toPhoneNumber = getArguments().getString(BUNDLE_TO_PHONE_NUMBER);
        textToPhoneNumber.setText(toPhoneNumber);


        mImageSpeaker = (ImageView) view.findViewById(R.id.image_in_call_speaker);
        mImageSpeaker.setOnClickListener(this);

        view.findViewById(R.id.floating_button_in_call_hang_up).setOnClickListener(this);

        audioManager.setSpeakerphoneOn(speakerPhone);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_in_call_speaker:
                speakerPhone = !speakerPhone;

//                setAudioFocus(true);
                audioManager.setSpeakerphoneOn(speakerPhone);

//                if (speakerPhone) {
//                    mImageSpeaker.
//                } else {
//                    mImageSpeaker.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_speaker_off_black_24dp));
//                }

                break;
            case R.id.floating_button_in_call_hang_up:
                if (mInCallListener != null) mInCallListener.hangUp();
                break;
            default:
                break;
        }
    }

    public void setInCallListener(InCallListener inCallListener) {
        mInCallListener = inCallListener;
    }

    public interface InCallListener {
        void hangUp();
    }


}
