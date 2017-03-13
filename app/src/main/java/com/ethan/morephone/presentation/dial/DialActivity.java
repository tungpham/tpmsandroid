package com.ethan.morephone.presentation.dial;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.dial.keyboard.LatinKeyboard;
import com.ethan.morephone.presentation.dial.keyboard.LatinKeyboardView;

/**
 * Created by Ethan on 3/13/17.
 */

public class DialActivity extends BaseActivity {

    private LatinKeyboard mQwertyKeyboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dial);
        LatinKeyboardView latinKeyboardView = (LatinKeyboardView) findViewById(R.id.keyboard);
        mQwertyKeyboard = new LatinKeyboard(this, R.xml.qwerty);
        latinKeyboardView.setKeyboard(mQwertyKeyboard);
    }
}
