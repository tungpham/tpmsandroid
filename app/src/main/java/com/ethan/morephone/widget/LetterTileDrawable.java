
package com.ethan.morephone.widget;

import android.accounts.Account;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ethan.morephone.R;
import com.ethan.morephone.utils.MoreContactUtils;

import junit.framework.Assert;

/**
 * A drawable that encapsulates all the functionality needed to display a letter tile to
 * represent a contact image.
 */
public class LetterTileDrawable extends Drawable {

    private final String TAG = LetterTileDrawable.class.getSimpleName();

    private final Paint mPaint;

    /**
     * Letter tile
     */
    private static TypedArray sColors;
    private static int sDefaultColor;
    private static int sTileFontColor;
    private static float sLetterToTileRatio;
    private static Bitmap DEFAULT_PERSON_AVATAR;
    private static Bitmap DEFAULT_BUSINESS_AVATAR;
    private static Bitmap DEFAULT_VOICEMAIL_AVATAR;
    private static Bitmap DEFAULT_SIM_PERSON_AVATAR;

    /**
     * Reusable components to avoid new allocations
     */
    private static final Paint sPaint = new Paint();
    private static final Rect sRect = new Rect();
    private static final char[] sFirstChar = new char[1];

    /**
     * Contact type constants
     */
    public static final int TYPE_PERSON = 1;
    public static final int TYPE_BUSINESS = 2;
    public static final int TYPE_VOICEMAIL = 3;
    public static final int TYPE_DEFAULT = TYPE_PERSON;

    private int mContactType = TYPE_DEFAULT;
    private float mScale = 1.0f;
    private float mOffset = 0.0f;
    private Account mAccount;
    private Context mContext;
    private boolean mIsCircle = false;

    private int mColor;
    private Character mLetter = null;

    public LetterTileDrawable(final Resources res) {
        this(res, null, null);
    }

    public LetterTileDrawable(final Resources res, final Context context, int index) {
        if (sColors == null) {
            sColors = res.obtainTypedArray(R.array.letter_tile_colors);
            sDefaultColor = res.getColor(R.color.letter_tile_default_color);
            sTileFontColor = res.getColor(R.color.letter_tile_font_color);
            sLetterToTileRatio = res.getFraction(R.dimen.letter_to_tile_ratio, 1, 1);
            DEFAULT_PERSON_AVATAR = BitmapFactory.decodeResource(res,
                    R.drawable.ic_person_white_120dp);
            DEFAULT_BUSINESS_AVATAR = BitmapFactory.decodeResource(res,
                    R.drawable.ic_business_white_120dp);
            DEFAULT_VOICEMAIL_AVATAR = BitmapFactory.decodeResource(res,
                    R.drawable.ic_voicemail_avatar);
            DEFAULT_SIM_PERSON_AVATAR = BitmapFactory.decodeResource(res,
                    R.drawable.ic_contact_picture_sim);

            sPaint.setTextAlign(Align.CENTER);
            sPaint.setAntiAlias(true);
        }
        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mColor = sColors.getColor(index, sDefaultColor);
        mContext = context;
    }

    public LetterTileDrawable(final Resources res, final Context context, final Account account) {
        if (sColors == null) {
            sColors = res.obtainTypedArray(R.array.letter_tile_colors);
            sDefaultColor = res.getColor(R.color.letter_tile_default_color);
            sTileFontColor = res.getColor(R.color.letter_tile_font_color);
            sLetterToTileRatio = res.getFraction(R.dimen.letter_to_tile_ratio, 1, 1);
            DEFAULT_PERSON_AVATAR = BitmapFactory.decodeResource(res,
                    R.drawable.ic_person_white_120dp);
            DEFAULT_BUSINESS_AVATAR = BitmapFactory.decodeResource(res,
                    R.drawable.ic_business_white_120dp);
            DEFAULT_VOICEMAIL_AVATAR = BitmapFactory.decodeResource(res,
                    R.drawable.ic_voicemail_avatar);
            DEFAULT_SIM_PERSON_AVATAR = BitmapFactory.decodeResource(res,
                    R.drawable.ic_contact_picture_sim);

            sPaint.setTextAlign(Align.CENTER);
            sPaint.setAntiAlias(true);
        }
        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mColor = sDefaultColor;
        mAccount = account;
        mContext = context;
    }

    @Override
    public void draw(final Canvas canvas) {
        final Rect bounds = getBounds();
        if (!isVisible() || bounds.isEmpty()) {
            return;
        }
        // Draw letter tile.
        drawLetterTile(canvas);
    }

    /**
     * Draw the bitmap onto the canvas at the current bounds taking into account the current scale.
     */
    private void drawBitmap(final Bitmap bitmap, final int width, final int height,
                            final Canvas canvas) {
        // The bitmap should be drawn in the middle of the canvas without changing its width to
        // height ratio.
        final Rect destRect = copyBounds();

        // Crop the destination bounds into a square, scaled and offset as appropriate
        final int halfLength = (int) (mScale * Math.min(destRect.width(), destRect.height()) / 2);

        destRect.set(destRect.centerX() - halfLength,
                (int) (destRect.centerY() - halfLength + mOffset * destRect.height()),
                destRect.centerX() + halfLength,
                (int) (destRect.centerY() + halfLength + mOffset * destRect.height()));

        // Source rectangle remains the entire bounds of the source bitmap.
        sRect.set(0, 0, width, height);

        canvas.drawBitmap(bitmap, sRect, destRect, mPaint);
    }

    private void drawLetterTile(final Canvas canvas) {
        // Draw background color.
        sPaint.setColor(mColor);

        sPaint.setAlpha(mPaint.getAlpha());
        final Rect bounds = getBounds();
        final int minDimension = Math.min(bounds.width(), bounds.height());

        if (mIsCircle) {
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), minDimension / 2, sPaint);
        } else {
            canvas.drawRect(bounds, sPaint);
        }

        // Draw letter/digit only if the first character is an english letter or there's a override

        // Draw the default image if there is no letter/digit to be drawn
        final Bitmap bitmap = getBitmapForContactType(mContactType,
                mAccount, mContext);
        drawBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                canvas);
    }

    public int getColor() {
        return mColor;
    }

    /**
     * Returns a deterministic color based on the provided contact identifier string.
     */
    private int pickColor(final String identifier) {
//        if (TextUtils.isEmpty(identifier) || mContactType == TYPE_VOICEMAIL) {
//            return sDefaultColor;
//        }
        // String.hashCode() implementation is not supposed to change across java versions, so
        // this should guarantee the same email address always maps to the same color.
        // The email should already have been normalized by the ContactRequest.
        final int color = Math.abs(identifier.hashCode()) % sColors.length();
        return sColors.getColor(color, sDefaultColor);
    }

    private static Bitmap getBitmapForContactType(int contactType,
                                                  Account account, Context context) {
        if (context != null) {
            return DEFAULT_SIM_PERSON_AVATAR;
        }
        switch (contactType) {
            case TYPE_PERSON:
                return DEFAULT_PERSON_AVATAR;
            case TYPE_BUSINESS:
                return DEFAULT_BUSINESS_AVATAR;
            case TYPE_VOICEMAIL:
                return DEFAULT_VOICEMAIL_AVATAR;
            default:
                return DEFAULT_PERSON_AVATAR;
        }
    }

    private static boolean isEnglishLetter(final char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z');
    }

    @Override
    public void setAlpha(final int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(final ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return android.graphics.PixelFormat.OPAQUE;
    }

    /**
     * Scale the drawn letter tile to a ratio of its default size
     *
     * @param scale The ratio the letter tile should be scaled to as a percentage of its default
     *              size, from a scale of 0 to 2.0f. The default is 1.0f.
     */
    public LetterTileDrawable setScale(float scale) {
        mScale = scale;
        return this;
    }

    /**
     * Assigns the vertical offset of the position of the letter tile to the ContactDrawable
     *
     * @param offset The provided offset must be within the range of -0.5f to 0.5f.
     *               If set to -0.5f, the letter will be shifted upwards by 0.5 times the height of the canvas
     *               it is being drawn on, which means it will be drawn with the center of the letter starting
     *               at the top edge of the canvas.
     *               If set to 0.5f, the letter will be shifted downwards by 0.5 times the height of the canvas
     *               it is being drawn on, which means it will be drawn with the center of the letter starting
     *               at the bottom edge of the canvas.
     *               The default is 0.0f.
     */
    public LetterTileDrawable setOffset(float offset) {
        Assert.assertTrue(offset >= -0.5f && offset <= 0.5f);
        mOffset = offset;
        return this;
    }

    public LetterTileDrawable setLetter(Character letter) {
        mLetter = letter;
        return this;
    }

    public LetterTileDrawable setColor(int color) {
        mColor = color;
        return this;
    }

    public LetterTileDrawable setLetterAndColorFromContactDetails() {

        final int color = 54 % sColors.length();
        mColor = sColors.getColor(color, sDefaultColor);
//        if (displayName != null && displayName.length() > 0
//                && isEnglishLetter(displayName.charAt(0))) {
//            mLetter = Character.toUpperCase(displayName.charAt(0));
//        } else {
//            mLetter = null;
//        }
//        mColor = pickColor(identifier);
        return this;
    }

    public LetterTileDrawable setLetterAndColorFromContactDetails(final String displayName,
                                                                  final String identifier) {

        if (displayName != null && displayName.length() > 0
                && isEnglishLetter(displayName.charAt(0))) {
            mLetter = Character.toUpperCase(displayName.charAt(0));
        } else {
            mLetter = null;
        }
        mColor = pickColor(identifier);
        return this;
    }

    public LetterTileDrawable setContactType(int contactType) {
        mContactType = contactType;
        return this;
    }

    public LetterTileDrawable setIsCircular(boolean isCircle) {
        mIsCircle = isCircle;
        return this;
    }
}