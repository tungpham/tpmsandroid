package com.ethan.morephone.presentation.phone.incall;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.phone.dial.view.DialpadImageButton;

/**
 * Created by Ethan on 4/10/17.
 */

public class InCallFragment extends BaseFragment implements View.OnClickListener,
        DialpadImageButton.OnPressedListener {

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

    private RelativeLayout mRelativeDialpad;
    private TextView mTextHideDialpad;

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

        mRelativeDialpad = (RelativeLayout) view.findViewById(R.id.relative_in_call_dialpad);

        mImageSpeaker = (ImageView) view.findViewById(R.id.image_in_call_speaker);
        mImageSpeaker.setOnClickListener(this);

        mTextHideDialpad = (TextView) view.findViewById(R.id.text_in_call_hide_dialpad);
        mTextHideDialpad.setOnClickListener(this);

        view.findViewById(R.id.floating_button_in_call_hang_up).setOnClickListener(this);
        view.findViewById(R.id.image_in_call_dialpad).setOnClickListener(this);

        audioManager.setSpeakerphoneOn(speakerPhone);

        setUpKeypad(view);

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

    private void changeVisibleDialpad() {
        if (mRelativeDialpad.getVisibility() == View.VISIBLE) {
            mRelativeDialpad.setVisibility(View.GONE);
            mTextHideDialpad.setVisibility(View.GONE);
        } else {
            mRelativeDialpad.setVisibility(View.VISIBLE);
            mTextHideDialpad.setVisibility(View.VISIBLE);
        }
    }

    public void setInCallListener(InCallListener inCallListener) {
        mInCallListener = inCallListener;
    }

    @Override
    public void onPressed(View view, boolean pressed) {
        if (pressed) {
            switch (view.getId()) {
                case R.id.one: {
                    mInCallListener.sendDigit(1);
                    keyPressed(KeyEvent.KEYCODE_1);
                    break;
                }
                case R.id.two: {
                    mInCallListener.sendDigit(2);
                    keyPressed(KeyEvent.KEYCODE_2);
                    break;
                }
                case R.id.three: {
                    keyPressed(KeyEvent.KEYCODE_3);
                    break;
                }
                case R.id.four: {
                    keyPressed(KeyEvent.KEYCODE_4);
                    break;
                }
                case R.id.five: {
                    keyPressed(KeyEvent.KEYCODE_5);
                    break;
                }
                case R.id.six: {
                    keyPressed(KeyEvent.KEYCODE_6);
                    break;
                }
                case R.id.seven: {
                    keyPressed(KeyEvent.KEYCODE_7);
                    break;
                }
                case R.id.eight: {
                    keyPressed(KeyEvent.KEYCODE_8);
                    break;
                }
                case R.id.nine: {
                    keyPressed(KeyEvent.KEYCODE_9);
                    break;
                }
                case R.id.zero: {
                    keyPressed(KeyEvent.KEYCODE_0);
                    break;
                }
                case R.id.pound: {
                    keyPressed(KeyEvent.KEYCODE_POUND);
                    break;
                }
                case R.id.star: {
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

    private void setUpKeypad(View view){
        int[] buttonIds = new int[]{R.id.one, R.id.two, R.id.three, R.id.four, R.id.five,
                R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.star, R.id.pound};
        for (int id : buttonIds) {
            ((DialpadImageButton) view.findViewById(id)).setOnPressedListener(this);
        }
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
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
//        mEditTextDigits.onKeyDown(keyCode, event);

        // If the cursor is at the end of the text we hide it.
//        final int length = mEditTextDigits.length();
//        if (length == mEditTextDigits.getSelectionStart() && length == mEditTextDigits.getSelectionEnd()) {
//            mEditTextDigits.setCursorVisible(false);
//        }
    }


    public interface InCallListener {
        void hangUp();
        void sendDigit(int digit);
    }


}
