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
import android.widget.TextView;

import com.android.morephone.data.entity.contact.Contact;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.contact.ContactFragment;
import com.ethan.morephone.presentation.contact.editor.ContactEditorActivity;
import com.ethan.morephone.utils.SchedulingUtils;
import com.ethan.morephone.widget.QuickContactImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by truongnguyen on 9/27/17.
 */

public class ContactDetailActivity extends BaseActivity {

    private final int REQUEST_EDITTOR_CONTACT = 100;

    private View mPhotoViewContainer;
    private int mMaximumPortraitHeaderHeight;
    private View mCoordinatorLayout;
    private QuickContactImageView mImageContact;

    private TypedArray mColorArray;

    private Contact mContactData;
    private ImageView mImageCall;
    private ImageView mImageMessage;

    private TextView mTextDisplayName;
    private TextView mTextPhoneNumber;

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

        mContactData = getIntent().getParcelableExtra(ContactFragment.EXTRA_CONTACT);

        mTextDisplayName = (TextView) findViewById(R.id.text_display_name);
        mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);

        loadData();


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

            case android.R.id.home:
                finish();
                return true;


            case R.id.menu_star:
//                toggleStar(item);
                return true;
            case R.id.menu_edit:
                Intent intent = new Intent(this, ContactEditorActivity.class);
                intent.putExtra(ContactFragment.EXTRA_CONTACT, mContactData);
                startActivityForResult(intent, REQUEST_EDITTOR_CONTACT);
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

    private void setHeaderHeight(int height) {
        final ViewGroup.LayoutParams toolbarLayoutParams = mPhotoViewContainer.getLayoutParams();
        toolbarLayoutParams.height = height;
        mPhotoViewContainer.setLayoutParams(toolbarLayoutParams);
    }

    private void loadData() {
        mTextDisplayName.setText(mContactData.getDisplayName());
        mTextPhoneNumber.setText(mContactData.getPhoneNumber());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDITTOR_CONTACT && resultCode == RESULT_OK) {
            mContactData = data.getParcelableExtra(ContactFragment.EXTRA_CONTACT);
            loadData();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ContactFragment.EXTRA_CONTACT, mContactData);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
