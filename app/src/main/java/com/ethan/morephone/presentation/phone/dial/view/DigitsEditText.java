
package com.ethan.morephone.presentation.phone.dial.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * EditText which suppresses IME show up.
 */
@SuppressLint("AppCompatCustomView")
public class DigitsEditText extends EditText {
    public DigitsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
//        setShowSoftInputOnFocus(false);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        final InputMethodManager imm = ((InputMethodManager) getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE));
//        if (imm != null && imm.isActive(this)) {
//            imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
//        }

        hideKeyboard();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean ret = super.onTouchEvent(event);
        // Must be done after super.onTouchEvent()
//        final InputMethodManager imm = ((InputMethodManager) getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE));
//        if (imm != null && imm.isActive(this)) {
//            imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
//        }
        hideKeyboard();
        return ret;
    }

    @Override
    public boolean onCheckIsTextEditor() {
        hideKeyboard();

        return super.onCheckIsTextEditor();
    }

    /**
     * This methdod is called when text selection is changed, so hide keyboard to prevent it to appear
     *
     * @param selStart
     * @param selEnd
     */
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }
}
