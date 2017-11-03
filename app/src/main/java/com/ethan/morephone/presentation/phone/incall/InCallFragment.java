package com.ethan.morephone.presentation.phone.incall;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.phone.dial.view.DialpadImageButton;
import com.ethan.morephone.presentation.phone.service.PhoneService;

/**
 * Created by Ethan on 4/10/17.
 */

public class InCallFragment extends BaseFragment implements View.OnClickListener,
        DialpadImageButton.OnPressedListener {

    public static final String BUNDLE_FROM_PHONE_NUMBER = "BUNDLE_FROM_PHONE_NUMBER";
    public static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";

    public static InCallFragment getInstance(String fromPhoneNumber, String toPhoneNumber) {
        InCallFragment fragment = new InCallFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_FROM_PHONE_NUMBER, fromPhoneNumber);
        bundle.putString(BUNDLE_TO_PHONE_NUMBER, toPhoneNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    //    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_INVALID;
    private boolean speakerPhone;

    private ImageView mImageMute;
    private ImageView mImageSpeaker;

    private RelativeLayout mRelativeDialpad;
    private TextView mTextHideDialpad;

    private Chronometer mChronometerTime;

    private String mFromPhoneNumber;
    private String mToPhoneNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_call, container, false);

        TextView textPhoneNumber = (TextView) view.findViewById(R.id.text_in_call_phone_number);

        mFromPhoneNumber = getArguments().getString(BUNDLE_FROM_PHONE_NUMBER);
        mToPhoneNumber = getArguments().getString(BUNDLE_TO_PHONE_NUMBER);

        if (MyPreference.getPhoneNumber(getContext()).equals(mFromPhoneNumber)) {
            textPhoneNumber.setText(mToPhoneNumber);
        } else {
            textPhoneNumber.setText(mFromPhoneNumber);
        }

        mRelativeDialpad = (RelativeLayout) view.findViewById(R.id.relative_in_call_dialpad);

        mImageSpeaker = (ImageView) view.findViewById(R.id.image_in_call_speaker);
        mImageSpeaker.setOnClickListener(this);

        mImageMute = (ImageView) view.findViewById(R.id.image_in_call_mute);
        mImageMute.setOnClickListener(this);

        mTextHideDialpad = (TextView) view.findViewById(R.id.text_in_call_hide_dialpad);
        mTextHideDialpad.setOnClickListener(this);

        mChronometerTime = (Chronometer) view.findViewById(R.id.chronometer_in_call_time);
        mChronometerTime.setBase(SystemClock.elapsedRealtime());
        mChronometerTime.start();

        view.findViewById(R.id.floating_button_in_call_hang_up).setOnClickListener(this);
        view.findViewById(R.id.image_in_call_dialpad).setOnClickListener(this);

//        audioManager.setSpeakerphoneOn(speakerPhone);

        changeMuteMicrophone();
        changeSpeakerPhone();

        setUpKeypad(view);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_in_call_speaker:
//                speakerPhone = !speakerPhone;

//                setAudioFocus(true);
//                audioManager.setSpeakerphoneOn(speakerPhone);

//                if (speakerPhone) {
//                    mImageSpeaker.
//                } else {
//                    mImageSpeaker.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_speaker_off_black_24dp));
//                }

                MyPreference.setSpeakerphone(getContext(), !MyPreference.getSpeakerphone(getContext()));
                changeSpeakerPhone();
                PhoneService.startServiceWithAction(getContext(), PhoneService.ACTION_SPEAKER_PHONE, mFromPhoneNumber, mToPhoneNumber);

                break;

            case R.id.image_in_call_mute:
                MyPreference.setMuteMicrophone(getContext(), !MyPreference.getMuteMicrophone(getContext()));
                changeMuteMicrophone();
                PhoneService.startServiceWithAction(getContext(), PhoneService.ACTION_MUTE_MICROPHONE, mFromPhoneNumber, mToPhoneNumber);
                break;

            case R.id.floating_button_in_call_hang_up:
                PhoneService.startServiceWithAction(getContext(), PhoneService.ACTION_HANG_UP, mFromPhoneNumber, mToPhoneNumber);
                break;

            case R.id.image_in_call_dialpad:
                changeVisibleDialpad();
                break;
            case R.id.text_in_call_hide_dialpad:
                changeVisibleDialpad();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChronometerTime.stop();
    }

    private void changeVisibleDialpad() {
        if (mRelativeDialpad.getVisibility() == View.VISIBLE) {
            mRelativeDialpad.setVisibility(View.GONE);
            mTextHideDialpad.setVisibility(View.GONE);
        } else {
            mRelativeDialpad.setVisibility(View.VISIBLE);
            mTextHideDialpad.setVisibility(View.VISIBLE);
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

    @Override
    public void onPressed(View view, boolean pressed) {
        if (pressed) {
            switch (view.getId()) {
                case R.id.one: {
                    sendDigit("1");
                    keyPressed(KeyEvent.KEYCODE_1);
                    break;
                }
                case R.id.two: {
                    sendDigit("2");
                    keyPressed(KeyEvent.KEYCODE_2);
                    break;
                }
                case R.id.three: {
                    sendDigit("3");
                    keyPressed(KeyEvent.KEYCODE_3);
                    break;
                }
                case R.id.four: {
                    sendDigit("4");
                    keyPressed(KeyEvent.KEYCODE_4);
                    break;
                }
                case R.id.five: {
                    sendDigit("5");
                    keyPressed(KeyEvent.KEYCODE_5);
                    break;
                }
                case R.id.six: {
                    sendDigit("6");
                    keyPressed(KeyEvent.KEYCODE_6);
                    break;
                }
                case R.id.seven: {
                    sendDigit("7");
                    keyPressed(KeyEvent.KEYCODE_7);
                    break;
                }
                case R.id.eight: {
                    sendDigit("8");
                    keyPressed(KeyEvent.KEYCODE_8);
                    break;
                }
                case R.id.nine: {
                    sendDigit("9");
                    keyPressed(KeyEvent.KEYCODE_9);
                    break;
                }
                case R.id.zero: {
                    sendDigit("0");
                    keyPressed(KeyEvent.KEYCODE_0);
                    break;
                }
                case R.id.pound: {
                    sendDigit("#");
                    keyPressed(KeyEvent.KEYCODE_POUND);
                    break;
                }
                case R.id.star: {
                    sendDigit("*");
                    keyPressed(KeyEvent.KEYCODE_STAR);
                    break;
                }
                default: {
                    break;
                }
            }
//            mDialpadPressCount++;
        } else {
            view.jumpDrawablesToCurrentState();
//            mDialpadPressCount--;
//            if (mDialpadPressCount < 0) {
//                // e.g.
//                // - when the user action is detected as horizontal swipe, at which only
//                //   "up" event is thrown.
//                // - when the user long-press '0' button, at which dialpad will decrease this count
//                //   while it still gets press-up event here.
////                if (DEBUG) Log.d(TAG, "mKeyPressCount become negative.");
//                stopTone();
//                mDialpadPressCount = 0;
//            } else if (mDialpadPressCount == 0) {
//                stopTone();
//            }
        }
    }

    private void setUpKeypad(View view) {
        int[] buttonIds = new int[]{R.id.one, R.id.two, R.id.three, R.id.four, R.id.five,
                R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.star, R.id.pound};
        for (int id : buttonIds) {
            ((DialpadImageButton) view.findViewById(id)).setOnPressedListener(this);
        }
    }

    private void sendDigit(String digit){
        PhoneService.startServiceWithAction(getContext(), PhoneService.ACTION_SEND_DIGITS, mFromPhoneNumber, mToPhoneNumber, String.valueOf(digit));
    }

    private void keyPressed(int keyCode) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_1:
//                playTone(ToneGenerator.TONE_DTMF_1, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_2:
//                playTone(ToneGenerator.TONE_DTMF_2, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_3:
//                playTone(ToneGenerator.TONE_DTMF_3, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_4:
//                playTone(ToneGenerator.TONE_DTMF_4, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_5:
//                playTone(ToneGenerator.TONE_DTMF_5, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_6:
//                playTone(ToneGenerator.TONE_DTMF_6, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_7:
//                playTone(ToneGenerator.TONE_DTMF_7, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_8:
//                playTone(ToneGenerator.TONE_DTMF_8, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_9:
//                playTone(ToneGenerator.TONE_DTMF_9, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_0:
//                playTone(ToneGenerator.TONE_DTMF_0, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_POUND:
//                playTone(ToneGenerator.TONE_DTMF_P, TONE_LENGTH_INFINITE);
//                break;
//            case KeyEvent.KEYCODE_STAR:
//                playTone(ToneGenerator.TONE_DTMF_S, TONE_LENGTH_INFINITE);
//                break;
//            default:
//                break;
//        }

//        mHaptic.vibrate();
//        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
//        mEditTextDigits.onKeyDown(keyCode, event);

        // If the cursor is at the end of the text we hide it.
//        final int length = mEditTextDigits.length();
//        if (length == mEditTextDigits.getSelectionStart() && length == mEditTextDigits.getSelectionEnd()) {
//            mEditTextDigits.setCursorVisible(false);
//        }
    }

}
