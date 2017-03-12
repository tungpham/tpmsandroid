package com.ethan.morephone.presentation.authentication.login.forgot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;


/**
 * Created by truongnguyen on 10/17/16.
 */

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordContract.View, View.OnClickListener {

    private ForgotPasswordContract.Presenter mPresenter;
    private AppCompatEditText mEditTextEmail;
    private FloatingActionButton mButtonNextStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ForgotPasswordPresenter(this);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(toolbar, false);

        mEditTextEmail = (AppCompatEditText) findViewById(R.id.edit_text_forgot_password_email_address);
        mPresenter.checkMissingInfo(mEditTextEmail);

        mButtonNextStep = (FloatingActionButton) findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(this);
        mButtonNextStep.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next_step:
                mPresenter.resetPassword(mEditTextEmail.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void showNextStep() {
        finish();
    }

    @Override
    public void setEnableNextStep(boolean isEnable) {
        mButtonNextStep.setEnabled(isEnable);

        if (isEnable) mButtonNextStep.setAlpha(1f);
        else mButtonNextStep.setAlpha(0.5f);
    }

    @Override
    public void setLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showResultResetPassword(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(getApplicationContext(), getString(R.string.message_reset_success), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.message_reset_fail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(ForgotPasswordContract.Presenter presenter) {
        mPresenter = presenter;
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

}
