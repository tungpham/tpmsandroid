package com.ethan.morephone.presentation.authentication.register.email;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.authentication.register.password.RegisterPasswordActivity;


/**
 * Created by truongnguyen on 10/15/16.
 */

public class RegisterEmailActivity extends BaseActivity implements RegisterEmailContract.View,
        View.OnClickListener{

    private final int REQUEST_RESULT = 100;

    private FloatingActionButton mButtonNextStep;
    private RegisterEmailContract.Presenter mPresenter;

    private AppCompatEditText mEditTextEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RegisterEmailPresenter(this);

        setContentView(R.layout.fragment_register_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(toolbar, false);

        mButtonNextStep = (FloatingActionButton) findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(this);

        mEditTextEmail = (AppCompatEditText) findViewById(R.id.edit_text_email);
        mEditTextEmail.setText(MyPreference.getUserEmail(getApplicationContext()));
        mPresenter.checkMissingInfo(mEditTextEmail);
        mPresenter.validateInfo(mEditTextEmail);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
    }


    @Override
    public void setPresenter(RegisterEmailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNextScreen() {
        MyPreference.setUserEmail(getApplicationContext(), mEditTextEmail.getText().toString());
        startActivityForResult(new Intent(this, RegisterPasswordActivity.class), REQUEST_RESULT);
    }

    @Override
    public void setEnableNextStep(boolean isEnable) {
        mButtonNextStep.setEnabled(isEnable);

        if (isEnable) mButtonNextStep.setAlpha(1f);
        else mButtonNextStep.setAlpha(0.5f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next_step:
                mPresenter.onSaveDataAndNext(getApplicationContext(), mEditTextEmail.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_RESULT && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
