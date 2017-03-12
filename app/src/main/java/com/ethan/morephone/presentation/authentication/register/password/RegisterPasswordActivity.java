package com.ethan.morephone.presentation.authentication.register.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;


/**
 * Created by truongnguyen on 10/17/16.
 */

public class RegisterPasswordActivity extends BaseActivity implements
        RegisterPasswordContract.View,
        View.OnClickListener {

    private FloatingActionButton mButtonNextStep;
    private RegisterPasswordContract.Presenter mPresenter;

    private AppCompatEditText mEditTextPassword;

    private CoordinatorLayout mCoordinatorLayout;

//    private boolean mIsShow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RegisterPasswordPresenter(this);
        setContentView(R.layout.fragment_register_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(toolbar, false);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_register_password);

        mButtonNextStep = (FloatingActionButton) findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(this);
        setEnableNextStep(false);

        mEditTextPassword = (AppCompatEditText) findViewById(R.id.edit_text_password);
        mEditTextPassword.setText(MyPreference.getPassword(getApplicationContext()));
        mPresenter.checkMissingInfo(mEditTextPassword);
        mPresenter.validateInfo(mEditTextPassword);


        findViewById(R.id.text_register_password_show).setOnClickListener(this);
    }

    @Override
    public void setPresenter(RegisterPasswordContract.Presenter presenter) {
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

    @Override
    public void registerSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setEnableNextStep(boolean isEnable) {
        mButtonNextStep.setEnabled(isEnable);

        if (isEnable) mButtonNextStep.setAlpha(1f);
        else mButtonNextStep.setAlpha(0.5f);
    }

    @Override
    public void registerFailure(int status, String message) {
        if (status == 409) {
            Snackbar snackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.register_password_snack_bar), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           finish();
                        }
                    });
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void setLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_next_step:
                if (!TextUtils.isEmpty(mEditTextPassword.getText().toString())) {
                    mPresenter.createUser(getApplicationContext(), mEditTextPassword.getText().toString());
                }
                break;

            case R.id.text_register_password_show:

//                if (mIsShow) {
                mEditTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                } else {
//                    mEditTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//                mIsShow = !mIsShow;

//                DebugTool.logD("");

                break;

            default:
                break;
        }
    }

}
