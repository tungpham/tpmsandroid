
package com.ethan.morephone.presentation.phone.dial.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ethan.morephone.R;


public class SmartDialTextView extends AppCompatTextView {

    private final float mPadding;
    private final float mExtraPadding;

    public SmartDialTextView(Context context) {
        this(context, null);
    }

    public SmartDialTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPadding = getResources().getDimension(R.dimen.smartdial_suggestions_padding);
        mExtraPadding = getResources().getDimension(R.dimen.smartdial_suggestions_extra_padding);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        rescaleText(getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rescaleText(w);
    }

    private void rescaleText(int w) {
        if (w == 0) {
            return;
        }
        setTextScaleX(1);
        final Paint paint = getPaint();
        float width = w - 2 * mPadding - 2 * mExtraPadding;

        float ratio = width / paint.measureText(getText().toString());
        TextUtils.TruncateAt ellipsizeAt = null;
        if (ratio < 1.0f) {
            if (ratio < 0.8f) {
                // If the text is too big to fit even after scaling to 80%, just ellipsize it
                // instead.
                ellipsizeAt = TextUtils.TruncateAt.END;
                setTextScaleX(0.8f);
            } else {
                setTextScaleX(ratio);
            }
        }
        setEllipsize(ellipsizeAt);
    }
}
