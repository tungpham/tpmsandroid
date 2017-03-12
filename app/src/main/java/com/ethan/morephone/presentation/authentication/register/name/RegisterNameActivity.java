package com.ethan.morephone.presentation.authentication.register.name;

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
import com.ethan.morephone.presentation.authentication.register.email.RegisterEmailActivity;


/**
 * Created by truongnguyen on 10/15/16.
 */

public class RegisterNameActivity extends BaseActivity implements RegisterNameContract.View, View.OnClickListener {

    private final int REQUEST_RESULT = 100;

    private RegisterNameContract.Presenter mPresenter;

    private FloatingActionButton mButtonNextStep;

    private AppCompatEditText mEditTextFirstName;
    private AppCompatEditText mEditTextLastName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RegisterNamePresenter(this);

        setContentView(R.layout.fragment_register_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(toolbar, false);

        mButtonNextStep = (FloatingActionButton) findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(this);

        mEditTextFirstName = (AppCompatEditText) findViewById(R.id.edit_text_first_name);
        mEditTextLastName = (AppCompatEditText) findViewById(R.id.edit_text_last_name);
        mEditTextFirstName.setText(MyPreference.getFirstName(getApplicationContext()));
        mEditTextLastName.setText(MyPreference.getLastName(getApplicationContext()));
        mPresenter.checkMissingInfo(mEditTextFirstName, mEditTextLastName);
        mPresenter.validateInfo(mEditTextFirstName, mEditTextLastName);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
        mPresenter.checkMissingInfo(mEditTextFirstName, mEditTextLastName);
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
    public void showNextStep() {
        MyPreference.setFirstName(getApplicationContext(), mEditTextFirstName.getText().toString());
        MyPreference.setLastName(getApplicationContext(), mEditTextLastName.getText().toString());
        startActivityForResult(new Intent(this, RegisterEmailActivity.class), REQUEST_RESULT);
    }

    @Override
    public void setEnableNextStep(boolean isEnable) {
        mButtonNextStep.setEnabled(isEnable);

        if (isEnable) mButtonNextStep.setAlpha(1f);
        else mButtonNextStep.setAlpha(0.5f);
    }

    @Override
    public void setPresenter(RegisterNameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_next_step:
                mPresenter.nextStep();
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
