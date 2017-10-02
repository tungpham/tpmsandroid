package com.ethan.morephone.presentation.contact.editor;

import android.content.Context;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.phonenumbers.AvailableCountry;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberContract;

import java.util.List;

/**
 * Created by truongnguyen on 9/29/17.
 */

public interface ContactEditorContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void createContactSuccess(Contact contact);

        void createContactFail();
    }

    interface Presenter extends BasePresenter {
        void createContact(Context context, Contact contact);
    }
}
