package com.ethan.morephone.presentation.contact.editor;

import android.content.Context;

import com.android.morephone.data.entity.contact.Contact;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by truongnguyen on 9/29/17.
 */

public interface ContactEditorContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void createContactSuccess(Contact contact);

        void createContactFail();

        void updateContactSuccess(Contact contact);

        void updateContactFail();

        void getContactSuccess(Contact contact);

        void getContactFail();
    }

    interface Presenter extends BasePresenter {
        void createContact(Context context, Contact contact);

        void updateContact(Context context, Contact contact);

        void getContact(Context context, String contactId);
    }
}
