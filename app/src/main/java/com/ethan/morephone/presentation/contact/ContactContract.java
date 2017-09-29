package com.ethan.morephone.presentation.contact;

import android.content.Context;

import com.android.morephone.data.entity.contact.Contact;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;
import com.ethan.morephone.presentation.contact.editor.ContactEditorContract;

import java.util.List;

/**
 * Created by truongnguyen on 9/29/17.
 */

public interface ContactContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showAllContact(List<Contact> contacts);
    }

    interface Presenter extends BasePresenter {
        void loadContact(Context context, String phoneNumberId);
    }

}
