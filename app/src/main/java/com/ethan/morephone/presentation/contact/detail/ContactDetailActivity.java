package com.ethan.morephone.presentation.contact.detail;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.morephone.data.entity.contact.Contact;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.SchedulingUtils;
import com.ethan.morephone.widget.QuickContactImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by truongnguyen on 9/27/17.
 */

public class ContactDetailActivity extends BaseActivity {

    private View mPhotoViewContainer;
    private int mMaximumPortraitHeaderHeight;
    private View mCoordinatorLayout;
    private QuickContactImageView mImageContact;

    private TypedArray mColorArray;

    private Contact mContactData;
    private ImageView mImageCall;
    private ImageView mImageMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        mImageCall = (ImageView) findViewById(R.id.image_call);
        mImageMessage = (ImageView) findViewById(R.id.image_message);

        mCoordinatorLayout = findViewById(R.id.main_content);
        mPhotoViewContainer = findViewById(R.id.appbar);
        mImageContact = (QuickContactImageView) findViewById(R.id.backdrop);

        mColorArray = getResources().obtainTypedArray(R.array.letter_tile_colors);

        int index = Math.abs(new Random().nextInt() % mColorArray.length());
        final int mainColor = mColorArray.getColor(index, Color.GREEN);
        mImageCall.setColorFilter(mainColor, PorterDuff.Mode.SRC_ATOP);
        mImageMessage.setColorFilter(mainColor, PorterDuff.Mode.SRC_ATOP);

        SchedulingUtils.doOnPreDraw(mCoordinatorLayout, /* drawNextFrame = */ false, new Runnable() {
            @Override
            public void run() {
                // We never want the height of the photo view to exceed its width.
                mMaximumPortraitHeaderHeight = mPhotoViewContainer.getWidth();
                setHeaderHeight(mMaximumPortraitHeaderHeight);

                mImageContact.setBackgroundColor(mainColor);

            }
        });

        mContactData = new Contact("dsf"
                ,"Tai sao", "+84974878244", "asdf", "df", "hanoi", "coderdaudat@gmail.com", "asf", "asdf", "asfd");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        enableActionBar(toolbar, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quickcontact, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (mContactData != null) {
            final MenuItem starredMenuItem = menu.findItem(R.id.menu_star);
//            ContactDisplayUtils.configureStarredMenuItem(starredMenuItem,
//                    mContactData.isDirectoryEntry(), mContactData.isUserProfile(),
//                    mContactData.getStarred());
            // Configure edit MenuItem
            final MenuItem editMenuItem = menu.findItem(R.id.menu_edit);
            editMenuItem.setVisible(true);
//            if (DirectoryContactUtil.isDirectoryContact(mContactData) || InvisibleContactUtil
//                    .isInvisibleAndAddable(mContactData, this)) {
//                editMenuItem.setIcon(R.drawable.ic_person_add_tinted_24dp);
//                editMenuItem.setTitle(R.string.menu_add_contact);
//            } else if (isContactEditable()) {
                editMenuItem.setIcon(R.drawable.ic_create_24dp);
                editMenuItem.setTitle(R.string.menu_editContact);
//            } else {
//                editMenuItem.setVisible(false);
//            }

            final MenuItem refreshMenuItem = menu.findItem(R.id.menu_refresh);
            refreshMenuItem.setVisible(false);

            final MenuItem deleteMenuItem = menu.findItem(R.id.menu_delete);
//            deleteMenuItem.setVisible(isContactEditable() && !mContactData.isUserProfile());

            final MenuItem shareMenuItem = menu.findItem(R.id.menu_share);
//            shareMenuItem.setVisible(isContactShareable());

//            final MenuItem helpMenu = menu.findItem(R.id.menu_help);
//            helpMenu.setVisible(HelpUtils.isHelpAndFeedbackAvailable());
//            String accoutName = null;
//            String accoutType = null;



            return true;
//        }
//        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_star:
//                toggleStar(item);
                return true;
            case R.id.menu_edit:
//                if (DirectoryContactUtil.isDirectoryContact(mContactData)) {
//                    // This action is used to launch the contact selector, with the option of
//                    // creating a new contact. Creating a new contact is an INSERT, while selecting
//                    // an exisiting one is an edit. The fields in the edit screen will be
//                    // prepopulated with data.
//
//                    final Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
//                    intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
//
//                    ArrayList<ContentValues> values = mContactData.getContentValues();
//
//                    // Only pre-fill the name field if the provided display name is an nickname
//                    // or better (e.g. structured name, nickname)
//                    if (mContactData.getDisplayNameSource() >= ContactsContract.DisplayNameSources.NICKNAME) {
//                        intent.putExtra(ContactsContract.Intents.Insert.NAME, mContactData.getDisplayName());
//                    } else if (mContactData.getDisplayNameSource()
//                            == ContactsContract.DisplayNameSources.ORGANIZATION) {
//                        // This is probably an organization. Instead of copying the organization
//                        // name into a name entry, copy it into the organization entry. This
//                        // way we will still consider the contact an organization.
//                        final ContentValues organization = new ContentValues();
//                        organization.put(ContactsContract.CommonDataKinds.Organization.COMPANY, mContactData.getDisplayName());
//                        organization.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
//                        values.add(organization);
//                    }
//
//                    // Last time used and times used are aggregated values from the usage stat
//                    // table. They need to be removed from data values so the SQL table can insert
//                    // properly
//                    for (ContentValues value : values) {
//                        value.remove(ContactsContract.Data.LAST_TIME_USED);
//                        value.remove(ContactsContract.Data.TIMES_USED);
//                    }
//                    intent.putExtra(ContactsContract.Intents.Insert.DATA, values);
//
//                    // If the contact can only export to the same account, add it to the intent.
//                    // Otherwise the ContactEditorFragment will show a dialog for selecting an
//                    // account.
//                    if (mContactData.getDirectoryExportSupport() ==
//                            ContactsContract.Directory.EXPORT_SUPPORT_SAME_ACCOUNT_ONLY) {
//                        intent.putExtra(ContactsContract.Intents.Insert.EXTRA_ACCOUNT,
//                                new Account(mContactData.getDirectoryAccountName(),
//                                        mContactData.getDirectoryAccountType()));
//                        intent.putExtra(ContactsContract.Intents.Insert.EXTRA_DATA_SET,
//                                mContactData.getRawContacts().get(0).getDataSet());
//                    }
//
//                    // Add this flag to disable the delete menu option on directory contact joins
//                    // with local contacts. The delete option is ambiguous when joining contacts.
//                    intent.putExtra(ContactEditorFragment.INTENT_EXTRA_DISABLE_DELETE_MENU_OPTION,
//                            true);
//
//                    startActivityForResult(intent, REQUEST_CODE_CONTACT_SELECTION_ACTIVITY);
//                } else if (InvisibleContactUtil.isInvisibleAndAddable(mContactData, this)) {
//                    InvisibleContactUtil.addToDefaultGroup(mContactData, this);
//                } else if (isContactEditable()) {
//                    editContact();
//                }
                return true;
            case R.id.menu_refresh:
//                reFreshContact();
                return true;
            case R.id.menu_delete:
//                if (isContactEditable()) {
//                    deleteContact();
//                }
                return true;
            case R.id.menu_share:
//                if (isContactShareable()) {
//                    shareContact();
//                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setHeaderHeight(int height) {
        final ViewGroup.LayoutParams toolbarLayoutParams
                = mPhotoViewContainer.getLayoutParams();
        toolbarLayoutParams.height = height;
        mPhotoViewContainer.setLayoutParams(toolbarLayoutParams);
    }
}
