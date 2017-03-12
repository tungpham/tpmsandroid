package com.ethan.morephone.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Ethan on 12/22/16.
 */

public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf;
            if (getTypeface() != null) {
                int mode = getTypeface().getStyle();
                switch (mode) {
                    case Typeface.BOLD:
                        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/CircularAir-Bold.otf");
                        break;

                    case Typeface.ITALIC:
                        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/CircularAir-Book.otf");
                        break;

                    case Typeface.BOLD_ITALIC:
                        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/CircularAir-Bold.otf");
                        break;

                    default:
                        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/CircularAir-Light.otf");
                        break;
                }

            } else {
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/CircularAir-Light.otf");
            }

            setTypeface(tf);
        }
    }
}
