package com.ethan.morephone.presentation.message.reply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.TextDrawable;

/**
 * Created by Ethan on 7/28/17.
 */

public class MessageReplyActivity extends BaseActivity implements View.OnClickListener, MessageReplyContract.View{

    public static final String EXTRA_PHONE_NUMBER_TO = "EXTRA_PHONE_NUMBER_TO";
    public static final String EXTRA_MESSAGE_BODY = "EXTRA_MESSAGE_BODY";
    public static final String EXTRA_PHONE_NUMBER_FROM = "EXTRA_PHONE_NUMBER_FROM";

    private MessageReplyContract.Presenter mPresenter;

    private String mPhoneNumberFrom;
    private String mPhoneNumberTo;

    private AppCompatEditText mEditTextMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_reply);

        new MessageReplyPresenter(this, Injection.providerUseCaseHandler(), Injection.providerCreateMessage(getApplicationContext()));

        mPhoneNumberTo = getIntent().getStringExtra(EXTRA_PHONE_NUMBER_TO);
        String messageBody = getIntent().getStringExtra(EXTRA_MESSAGE_BODY);
        mPhoneNumberFrom = getIntent().getStringExtra(EXTRA_PHONE_NUMBER_FROM);

        TextView textMessageTitle = (TextView) findViewById(R.id.text_message_reply_title);
        textMessageTitle.setText(mPhoneNumberFrom);

        ImageView imageIcon = (ImageView) findViewById(R.id.image_icon_message_reply);
        TextDrawable.IBuilder drawableBuilder =  TextDrawable.builder().round();
        imageIcon.setImageDrawable(drawableBuilder.build(String.valueOf(mPhoneNumberFrom.charAt(0)), ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundAvatar)));

        TextView textMessageTime = (TextView) findViewById(R.id.text_message_reply_time);
        textMessageTime.setText(Utils.getTimeDate(System.currentTimeMillis()));

        TextView textMessageBody = (TextView) findViewById(R.id.text_message_reply_body);
        textMessageBody.setText(messageBody);

        mEditTextMessage = (AppCompatEditText) findViewById(R.id.edit_text_msg);

        findViewById(R.id.image_send).setOnClickListener(this);
        findViewById(R.id.image_message_reply_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_send:
                String body = mEditTextMessage.getText().toString();
                if (!TextUtils.isEmpty(body)) {
                    mPresenter.createMessage(MyPreference.getUserId(getApplicationContext()), mPhoneNumberFrom, mPhoneNumberTo, body, 0);
                    finish();
                }
                break;

            case R.id.image_message_reply_close:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(MessageReplyContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void createMessageSuccess(MessageItem messageItem) {
        Toast.makeText(getApplicationContext(), getString(R.string.message_send_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createMessageError() {
        Toast.makeText(getApplicationContext(), getString(R.string.message_send_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean isActive) {

    }
}
