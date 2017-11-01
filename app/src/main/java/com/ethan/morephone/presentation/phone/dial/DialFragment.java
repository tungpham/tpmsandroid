package com.ethan.morephone.presentation.phone.dial;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.phone.PhoneActivity;
import com.ethan.morephone.presentation.phone.dial.view.DialpadImageButton;
import com.ethan.morephone.presentation.phone.dial.view.UnicodeDialerKeyListener;


/**
 * Created by Ethan on 3/13/17.
 */

public class DialFragment extends BaseFragment implements
        View.OnClickListener,
        TextWatcher,
        View.OnLongClickListener,
        View.OnKeyListener,
        DialpadImageButton.OnPressedListener {

    private static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    private static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";

    public static DialFragment getInstance(String phoneNumber, String toPhoneNumber) {
        DialFragment dialFragment = new DialFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        bundle.putString(BUNDLE_TO_PHONE_NUMBER, toPhoneNumber);
        dialFragment.setArguments(bundle);
        return dialFragment;
    }


    private static final int TONE_LENGTH_INFINITE = -1;
    private static final int TONE_LENGTH_MS = 150;

    private static final int DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_DTMF;
    private static final int TONE_RELATIVE_VOLUME = 80;

    private EditText mEditTextDigits;
    private View mDelete;

    // determines if we want to playback local DTMF tones.
    private boolean mDTMFToneEnabled;

    private int mDialpadPressCount;

    private ToneGenerator mToneGenerator;
    private final Object mToneGeneratorLock = new Object();
//    private final HapticFeedback mHaptic = new HapticFeedback();

    private String mPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialpad_fragment, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);
        String mToPhoneNumber = getArguments().getString(BUNDLE_TO_PHONE_NUMBER);

        mEditTextDigits = (EditText) view.findViewById(R.id.digits);
        mEditTextDigits.setKeyListener(UnicodeDialerKeyListener.INSTANCE);
        mEditTextDigits.setOnClickListener(this);
        mEditTextDigits.setOnKeyListener(this);
        mEditTextDigits.setOnLongClickListener(this);
        mEditTextDigits.addTextChangedListener(this);

        if (!TextUtils.isEmpty(mToPhoneNumber)) {
            mEditTextDigits.setText(mToPhoneNumber);
        }

        View mDialpad = view.findViewById(R.id.dialpad);

        // In landscape we put the keyboard in phone mode.
        if (null == mDialpad) {
            mEditTextDigits.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        } else {
            mEditTextDigits.setCursorVisible(false);
        }

        View oneButton = view.findViewById(R.id.one);
        if (oneButton != null) {
            setupKeypad(view);
        }


        mDelete = view.findViewById(R.id.deleteButton);
        if (mDelete != null) {
            mDelete.setOnClickListener(this);
            mDelete.setOnLongClickListener(this);
        }

        view.findViewById(R.id.dialButton).setOnClickListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        final ContentResolver contentResolver = getActivity().getContentResolver();

        // retrieve the DTMF tone play back setting.
        mDTMFToneEnabled = Settings.System.getInt(contentResolver, Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;

        // if the mToneGenerator creation fails, just continue without it.  It is
        // a local audio signal, and is not as important as the dtmf tone itself.
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                try {
                    mToneGenerator = new ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME);
                } catch (RuntimeException e) {
                    DebugTool.logD("Exception caught while creating local tone generator: " + e);
                    mToneGenerator = null;
                }
            }
        }

        mDialpadPressCount = 0;

    }

    @Override
    public void onPause() {
        super.onPause();

        // Make sure we don't leave this activity with a tone still playing.
        stopTone();
        // Just in case reset the counter too.
        mDialpadPressCount = 0;

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator != null) {
                mToneGenerator.release();
                mToneGenerator = null;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteButton: {
                keyPressed(KeyEvent.KEYCODE_DEL);
                return;
            }
            case R.id.dialButton: {
                String toPhoneNumber = mEditTextDigits.getText().toString();
                PhoneActivity.starterOutgoing(getActivity(), mPhoneNumber, toPhoneNumber);
//                if (mDialFragmentListener != null) mDialFragmentListener.onCallNow(toPhoneNumber);
                return;
            }
            case R.id.digits: {
                if (!isDigitsEmpty()) {
                    mEditTextDigits.setCursorVisible(true);
                }
                return;
            }
            default: {
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        switch (view.getId()) {
            case R.id.digits:
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    dialButtonPressed();
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        final Editable digits = mEditTextDigits.getText();
        final int id = view.getId();
        switch (id) {
            case R.id.deleteButton: {
                digits.clear();
                // TODO: The framework forgets to clear the pressed
                // status of disabled button. Until this is fixed,
                // clear manually the pressed status. b/2133127
                mDelete.setPressed(false);
                return true;
            }
            case R.id.one: {
                // '1' may be already entered since we rely on onTouch() event for numeric buttons.
                // Just for safety we also check if the digits field is empty or not.
//                if (isDigitsEmpty() || TextUtils.equals(mEditTextDigits.getText(), "1")) {
                // We'll try to initiate voicemail and thus we want to remove irrelevant string.
//                    removePreviousDigitIfPossible();
//
//                    if (isVoicemailAvailable()) {
//                        callVoicemail();
//                    } else if (getActivity() != null) {
//                        // Voicemail is unavailable maybe because Airplane mode is turned on.
//                        // Check the current status and show the most appropriate error message.
//                        final boolean isAirplaneModeOn =
//                                Settings.System.getInt(getActivity().getContentResolver(),
//                                        Settings.System.AIRPLANE_MODE_ON, 0) != 0;
//                        if (isAirplaneModeOn) {
//                            DialogFragment dialogFragment = ErrorDialogFragment.newInstance(
//                                    R.string.dialog_voicemail_airplane_mode_message);
//                            dialogFragment.show(getFragmentManager(),
//                                    "voicemail_request_during_airplane_mode");
//                        } else {
//                            DialogFragment dialogFragment = ErrorDialogFragment.newInstance(
//                                    R.string.dialog_voicemail_not_ready_message);
//                            dialogFragment.show(getFragmentManager(), "voicemail_not_ready");
//                        }
//                    }
//                    return true;
//                }
                return false;
            }
            case R.id.zero: {
                // Remove tentative input ('0') done by onTouch().
                removePreviousDigitIfPossible();
                keyPressed(KeyEvent.KEYCODE_PLUS);

                // Stop tone immediately and decrease the press count, so that possible subsequent
                // dial button presses won't honor the 0 click any more.
                // Note: this *will* make mDialpadPressCount negative when the 0 key is released,
                // which should be handled appropriately.
                stopTone();
                if (mDialpadPressCount > 0) mDialpadPressCount--;

                return true;
            }
            case R.id.digits: {
                // Right now EditText does not show the "paste" option when cursor is not visible.
                // To show that, make the cursor visible, and return false, letting the EditText
                // show the option by itself.
                mEditTextDigits.setCursorVisible(true);
                return false;
            }
            case R.id.dialButton: {
                //                    handleDialButtonClickWithEmptyDigits();
// This event should be consumed so that onClick() won't do the exactly same
// thing.
                return isDigitsEmpty();
            }
        }
        return false;
    }

    @Override
    public void onPressed(View view, boolean pressed) {
        if (pressed) {
            switch (view.getId()) {
                case R.id.one: {
                    keyPressed(KeyEvent.KEYCODE_1);
                    break;
                }
                case R.id.two: {
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
            mDialpadPressCount++;
        } else {
            view.jumpDrawablesToCurrentState();
            mDialpadPressCount--;
            if (mDialpadPressCount < 0) {
                // e.g.
                // - when the user action is detected as horizontal swipe, at which only
                //   "up" event is thrown.
                // - when the user long-press '0' button, at which dialpad will decrease this count
                //   while it still gets press-up event here.
//                if (DEBUG) Log.d(TAG, "mKeyPressCount become negative.");
                stopTone();
                mDialpadPressCount = 0;
            } else if (mDialpadPressCount == 0) {
                stopTone();
            }
        }
    }

    private void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
                playTone(ToneGenerator.TONE_DTMF_1, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_2:
                playTone(ToneGenerator.TONE_DTMF_2, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_3:
                playTone(ToneGenerator.TONE_DTMF_3, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_4:
                playTone(ToneGenerator.TONE_DTMF_4, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_5:
                playTone(ToneGenerator.TONE_DTMF_5, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_6:
                playTone(ToneGenerator.TONE_DTMF_6, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_7:
                playTone(ToneGenerator.TONE_DTMF_7, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_8:
                playTone(ToneGenerator.TONE_DTMF_8, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_9:
                playTone(ToneGenerator.TONE_DTMF_9, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_0:
                playTone(ToneGenerator.TONE_DTMF_0, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_POUND:
                playTone(ToneGenerator.TONE_DTMF_P, TONE_LENGTH_INFINITE);
                break;
            case KeyEvent.KEYCODE_STAR:
                playTone(ToneGenerator.TONE_DTMF_S, TONE_LENGTH_INFINITE);
                break;
            default:
                break;
        }

//        mHaptic.vibrate();
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        mEditTextDigits.onKeyDown(keyCode, event);

        // If the cursor is at the end of the text we hide it.
        final int length = mEditTextDigits.length();
        if (length == mEditTextDigits.getSelectionStart() && length == mEditTextDigits.getSelectionEnd()) {
            mEditTextDigits.setCursorVisible(false);
        }
    }

    /**
     * Plays the specified tone for TONE_LENGTH_MS milliseconds.
     */
    private void playTone(int tone) {
        playTone(tone, TONE_LENGTH_MS);
    }

    /**
     * Play the specified tone for the specified milliseconds
     * <p>
     * The tone is played locally, using the audio stream for phone calls.
     * Tones are played only if the "Audible touch tones" user preference
     * is checked, and are NOT played if the device is in silent mode.
     * <p>
     * The tone length can be -1, meaning "keep playing the tone." If the caller does so, it should
     * call stopTone() afterward.
     *
     * @param tone       a tone code from {@link ToneGenerator}
     * @param durationMs tone length.
     */
    private void playTone(int tone, int durationMs) {
        // if local tone playback is disabled, just return.
        if (!mDTMFToneEnabled) {
            return;
        }

        // Also do nothing if the phone is in silent mode.
        // We need to re-check the ringer mode for *every* playTone()
        // call, rather than keeping a local flag that's updated in
        // onResume(), since it's possible to toggle silent mode without
        // leaving the current activity (via the ENDCALL-longpress menu.)
        AudioManager audioManager =
                (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        if ((ringerMode == AudioManager.RINGER_MODE_SILENT)
                || (ringerMode == AudioManager.RINGER_MODE_VIBRATE)) {
            return;
        }

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                DebugTool.logD("playTone: mToneGenerator == null, tone: " + tone);
            }

            // Start the new tone (will stop any playing tone)
//            mToneGenerator.startTone(tone, durationMs);
        }
    }

    private void removePreviousDigitIfPossible() {
        final Editable editable = mEditTextDigits.getText();
        final int currentPosition = mEditTextDigits.getSelectionStart();
        if (currentPosition > 0) {
            mEditTextDigits.setSelection(currentPosition);
            mEditTextDigits.getText().delete(currentPosition - 1, currentPosition);
        }
    }


    /**
     * Stop the tone if it is played.
     */
    private void stopTone() {
        // if local tone playback is disabled, just return.
        if (!mDTMFToneEnabled) {
            return;
        }
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                return;
            }
            mToneGenerator.stopTone();
        }
    }


    private boolean isDigitsEmpty() {
        return mEditTextDigits.length() == 0;
    }

    private void setupKeypad(View fragmentView) {
        int[] buttonIds = new int[]{R.id.one, R.id.two, R.id.three, R.id.four, R.id.five,
                R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.star, R.id.pound};
        for (int id : buttonIds) {
            ((DialpadImageButton) fragmentView.findViewById(id)).setOnPressedListener(this);
        }

        // Long-pressing one button will initiate Voicemail.
        fragmentView.findViewById(R.id.one).setOnLongClickListener(this);

        // Long-pressing zero button will enter '+' instead.
        fragmentView.findViewById(R.id.zero).setOnLongClickListener(this);

    }

    public void setDialFragmentListener(DialFragmentListener dialFragmentListener) {
        DialFragmentListener mDialFragmentListener = dialFragmentListener;
    }


    public interface DialFragmentListener {
        void onCallNow(String toPhoneNumber);
    }

}
