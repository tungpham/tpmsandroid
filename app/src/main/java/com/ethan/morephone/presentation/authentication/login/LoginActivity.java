package com.ethan.morephone.presentation.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.authentication.login.forgot.ForgotPasswordActivity;
import com.ethan.morephone.utils.Utils;


/**
 * Created by Ethan on 11/3/16.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {

    private AppCompatEditText mEditTextEmail;
    private AppCompatEditText mEditTextPassword;

    private LoginContract.Presenter mPresenter;
    private FloatingActionButton mButtonNextStep;

    private CoordinatorLayout mCoordinatorLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoginPresenter(this);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(toolbar, false);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_login);

        mEditTextEmail = (AppCompatEditText) findViewById(R.id.edit_text_email);
        mEditTextPassword = (AppCompatEditText) findViewById(R.id.edit_text_password);
        mEditTextEmail.setText(MyPreference.getUserEmail(getApplicationContext()));
        mEditTextPassword.setText(MyPreference.getPassword(getApplicationContext()));
        mPresenter.checkMissingInfo(mEditTextEmail, mEditTextPassword);

        mButtonNextStep = (FloatingActionButton) findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(this);

        mEditTextEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mEditTextPassword.requestFocus();
                }
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.menu_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next_step:
                if (Utils.isInternetAvailable(getApplicationContext())) {
                    mPresenter.doLogin(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_error_lost_internet), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void loginSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void loginError(int status, String message) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void setLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void setEnableNextStep(boolean isEnable) {
        mButtonNextStep.setEnabled(isEnable);

        if (isEnable) mButtonNextStep.setAlpha(1f);
        else mButtonNextStep.setAlpha(0.5f);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
