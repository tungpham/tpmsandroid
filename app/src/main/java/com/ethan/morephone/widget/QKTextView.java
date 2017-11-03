package com.ethan.morephone.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import com.ethan.morephone.common.FontManager;


public class QKTextView extends AppCompatTextView {
    private final String TAG = "QKTextView";

    private Context mContext;
    private int mType = FontManager.TEXT_TYPE_PRIMARY;
    private boolean mOnColorBackground = false;

    public QKTextView(Context context) {
        super(context);

        if (!isInEditMode()) {
            init(context, null);
        }
    }

    public QKTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public QKTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;


//        if (attrs != null) {
////            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QKTextView);
////            mType = array.getInt(R.styleable.QKTextView_type, FontManager.TEXT_TYPE_PRIMARY);
////            array.recycle();
//        }

        setTextColor(FontManager.getTextColor(mContext, mType));
        setText(getText());

        setType(mType);
    }

    public void setType(int type) {
        mType = type;

        // Register for theme updates if we're text that changes color dynamically.
//        if (mType == FontManager.TEXT_TYPE_CATEGORY) {
//            LiveViewManager.registerView(MorePhonePreference.THEME, this, key ->
//                    setTextColor(FontManager.getTextColor(mContext, mType)));
//        }
//
//        LiveViewManager.registerView(MorePhonePreference.FONT_FAMILY, this, key -> {
//            setTypeface(FontManager.getFont(mContext, type));
//        });
//
//        LiveViewManager.registerView(MorePhonePreference.FONT_WEIGHT, this, key -> {
//            setTypeface(FontManager.getFont(mContext, type));
//        });
//
//        LiveViewManager.registerView(MorePhonePreference.FONT_SIZE, this, key -> {
//            setTextSize(TypedValue.COMPLEX_UNIT_SP, FontManager.getTextSize(mContext, mType));
//        });
//
//        LiveViewManager.registerView(MorePhonePreference.BACKGROUND, this, key -> {
//            setTextColor(FontManager.getTextColor(mContext, mType));
//        });
//
//        LiveViewManager.registerView(MorePhonePreference.TEXT_FORMATTING, this, key -> {
//            setText(getText(), BufferType.NORMAL);
//        });
    }

    public void setOnColorBackground(boolean onColorBackground) {
        if (onColorBackground != mOnColorBackground) {
            mOnColorBackground = onColorBackground;

            if (onColorBackground) {
                if (mType == FontManager.TEXT_TYPE_PRIMARY) {
                    setTextColor(ThemeManager.getTextOnColorPrimary());
                    setLinkTextColor(ThemeManager.getTextOnColorPrimary());
                } else if (mType == FontManager.TEXT_TYPE_SECONDARY ||
                        mType == FontManager.TEXT_TYPE_TERTIARY) {
                    setTextColor(ThemeManager.getTextOnColorSecondary());
                }
            } else {
                if (mType == FontManager.TEXT_TYPE_PRIMARY) {
                    setTextColor(ThemeManager.getTextOnBackgroundPrimary());
                    setLinkTextColor(ThemeManager.getColor());
                } else if (mType == FontManager.TEXT_TYPE_SECONDARY ||
                        mType == FontManager.TEXT_TYPE_TERTIARY) {
                    setTextColor(ThemeManager.getTextOnBackgroundSecondary());
                }
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        if (mType == FontManager.TEXT_TYPE_DIALOG_BUTTON) {
            text = text.toString().toUpperCase();
        }
        if (text == null || text.length() <= 0 || Build.VERSION.SDK_INT >= 19) {
            super.setText(text, BufferType.EDITABLE);
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        super.setText(builder, BufferType.EDITABLE);

    }
}
