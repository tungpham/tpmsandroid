package com.ethan.morephone.presentation.message.compose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.message.CreateMessage;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.widget.AutoCompleteContactView;

/**
 * Created by Ethan on 2/22/17.
 */

public class ComposeFragment extends BaseFragment implements View.OnClickListener{


    public static ComposeFragment getInstance() {
        return new ComposeFragment();
    }

    private AutoCompleteContactView mEditTextTo;
    private AppCompatEditText mEditTextBody;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_compose, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setTitleActionBar(toolbar, getString(R.string.message_compose_label));

        mEditTextTo = (AutoCompleteContactView) view.findViewById(R.id.auto_complete_recipient);
        mEditTextBody = (AppCompatEditText) view.findViewById(R.id.edit_text_msg);
        view.findViewById(R.id.image_send).setOnClickListener(this);

        setHasOptionsMenu(true);



        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_send:
                UseCaseHandler useCaseHandler = Injection.providerUseCaseHandler();
                CreateMessage createMessage = Injection.providerCreateMessage(getContext());
                CreateMessage.RequestValue requestValue = new CreateMessage.RequestValue(mEditTextTo.getText().toString(), "+18052284394", mEditTextBody.getText().toString());
                useCaseHandler.execute(createMessage, requestValue, new UseCase.UseCaseCallback<CreateMessage.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateMessage.ResponseValue response) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {

                    }
                });
                mEditTextBody.setText("");
                break;
        }
    }
}
