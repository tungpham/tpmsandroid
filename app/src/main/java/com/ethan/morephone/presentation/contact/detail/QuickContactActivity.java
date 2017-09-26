package com.ethan.morephone.presentation.contact.detail;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.BidiFormatter;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.morephone.data.entity.contact.Contact;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.CompatUtils;
import com.ethan.morephone.utils.ContactDisplayUtils;
import com.ethan.morephone.utils.ImageViewDrawableSetter;
import com.ethan.morephone.utils.ImplicitIntentsUtil;
import com.ethan.morephone.utils.MaterialColorMapUtils;
import com.ethan.morephone.utils.SchedulingUtils;
import com.ethan.morephone.utils.TouchPointManager;
import com.ethan.morephone.utils.ViewUtil;
import com.ethan.morephone.widget.ExpandingEntryCardView;
import com.ethan.morephone.widget.MultiShrinkScroller;
import com.ethan.morephone.widget.QuickContactImageView;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by truongnguyen on 9/26/17.
 */

public class QuickContactActivity extends ContactsActivity {

    /**
     * QuickContacts immediately takes up the full screen. All possible information is shown.
     * This value for {@link ContactsContract.QuickContact#EXTRA_MODE}
     * should only be used by the Contacts app.
     */
    public static final int MODE_FULLY_EXPANDED = 4;

    /** Used to pass the screen where the user came before launching this Activity. */
    public static final String EXTRA_PREVIOUS_SCREEN_TYPE = "previous_screen_type";

    private static final String TAG = "QuickContact";

    private static final String KEY_THEME_COLOR = "theme_color";
    private static final String KEY_IS_SUGGESTION_LIST_COLLAPSED = "is_suggestion_list_collapsed";
    private static final String KEY_SELECTED_SUGGESTION_CONTACTS = "selected_suggestion_contacts";
    private static final String KEY_PREVIOUS_CONTACT_ID = "previous_contact_id";
    private static final String KEY_SUGGESTIONS_AUTO_SELECTED = "suggestions_auto_seleted";

    private static final int ANIMATION_STATUS_BAR_COLOR_CHANGE_DURATION = 150;
    private static final int REQUEST_CODE_CONTACT_EDITOR_ACTIVITY = 1;
    private static final int SCRIM_COLOR = Color.argb(0xC8, 0, 0, 0);
    private static final int REQUEST_CODE_CONTACT_SELECTION_ACTIVITY = 2;
    private static final String MIMETYPE_SMS = "vnd.android-dir/mms-sms";

    /** This is the Intent action to install a shortcut in the launcher. */
    private static final String ACTION_INSTALL_SHORTCUT =
            "com.android.launcher.action.INSTALL_SHORTCUT";

    @SuppressWarnings("deprecation")
    private static final String LEGACY_AUTHORITY = android.provider.Contacts.AUTHORITY;

    private static final String MIMETYPE_GPLUS_PROFILE =
            "vnd.android.cursor.item/vnd.googleplus.profile";
    private static final String GPLUS_PROFILE_DATA_5_ADD_TO_CIRCLE = "addtocircle";
    private static final String GPLUS_PROFILE_DATA_5_VIEW_PROFILE = "view";
    private static final String MIMETYPE_HANGOUTS =
            "vnd.android.cursor.item/vnd.googleplus.profile.comm";
    private static final String HANGOUTS_DATA_5_VIDEO = "hangout";
    private static final String HANGOUTS_DATA_5_MESSAGE = "conversation";
    private static final String CALL_ORIGIN_QUICK_CONTACTS_ACTIVITY =
            "com.ethan.morephone.presentation.contact.quickcontact.QuickContactActivity";

    /**
     * The URI used to load the the Contact. Once the contact is loaded, use Contact#getLookupUri()
     * instead of referencing this URI.
     */
    private Uri mLookupUri;
    private String[] mExcludeMimes;
    private int mExtraMode;
    private String mExtraPrioritizedMimeType;
    private int mStatusBarColor;
    private boolean mHasAlreadyBeenOpened;
    private boolean mOnlyOnePhoneNumber;
    private boolean mOnlyOneEmail;

    private QuickContactImageView mPhotoView;
    private ExpandingEntryCardView mContactCard;
    private ExpandingEntryCardView mNoContactDetailsCard;
    private ExpandingEntryCardView mRecentCard;
    private ExpandingEntryCardView mAboutCard;

    // Suggestion card.
    private CardView mCollapsedSuggestionCardView;
    private CardView mExpandSuggestionCardView;
    private View mCollapasedSuggestionHeader;
    private TextView mCollapsedSuggestionCardTitle;
    private TextView mExpandSuggestionCardTitle;
    private ImageView mSuggestionSummaryPhoto;
    private TextView mSuggestionForName;
    private TextView mSuggestionContactsNumber;
    private LinearLayout mSuggestionList;
    private Button mSuggestionsCancelButton;
    private Button mSuggestionsLinkButton;
    private boolean mIsSuggestionListCollapsed;
    private boolean mSuggestionsShouldAutoSelected = true;
    private long mPreviousContactId = 0;

    private MultiShrinkScroller mScroller;
//    private SelectAccountDialogFragmentListener mSelectAccountFragmentListener;
//    private AsyncTask<Void, Void, Cp2DataCardModel> mEntriesAndActionsTask;
    private AsyncTask<Void, Void, Void> mRecentDataTask;

//    private AggregationSuggestionEngine mAggregationSuggestionEngine;
//    private List<Suggestion> mSuggestions;

    private TreeSet<Long> mSelectedAggregationIds = new TreeSet<>();
    /**
     * The last copy of Cp2DataCardModel that was passed to {@link #populateContactAndAboutCard}.
     */
//    private Cp2DataCardModel mCachedCp2DataCardModel;
    /**
     *  This scrim's opacity is controlled in two different ways. 1) Before the initial entrance
     *  animation finishes, the opacity is animated by a value animator. This is designed to
     *  distract the user from the length of the initial loading time. 2) After the initial
     *  entrance animation, the opacity is directly related to scroll position.
     */
    private ColorDrawable mWindowScrim;
    private boolean mIsEntranceAnimationFinished;
    private MaterialColorMapUtils mMaterialColorMapUtils;
    private boolean mIsExitAnimationInProgress;
    private boolean mHasComputedThemeColor;

    /**
     * Used to stop the ExpandingEntry cards from adjusting between an entry click and the intent
     * being launched.
     */
    private boolean mHasIntentLaunched;

    private Contact mContactData;
//    private ContactLoader mContactLoader;
    private PorterDuffColorFilter mColorFilter;
    private int mColorFilterColor;

    private final ImageViewDrawableSetter mPhotoSetter = new ImageViewDrawableSetter();

//    private final ImageViewDrawableSetter mPhotoSetter = new ImageViewDrawableSetter();

//    private static final List<String> LEADING_MIMETYPES = Lists.newArrayList(
//            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
//            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
//
//    private static final List<String> SORTED_ABOUT_CARD_MIMETYPES = Lists.newArrayList(
//            ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE,
//            // Phonetic name is inserted after nickname if it is available.
//            // No mimetype for phonetic name exists.
//            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,
//            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
//            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
//            ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE,
//            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE,
//            ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
//            ContactsContract.CommonDataKinds.Identity.CONTENT_ITEM_TYPE,
//            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);

    private static final BidiFormatter sBidiFormatter = BidiFormatter.getInstance();

    /** Id for the background contact loader */
    private static final int LOADER_CONTACT_ID = 0;

    private static final String KEY_LOADER_EXTRA_PHONES =
            QuickContactActivity.class.getCanonicalName() + ".KEY_LOADER_EXTRA_PHONES";

    /** Id for the background Sms Loader */
    private static final int LOADER_SMS_ID = 1;
    private static final int MAX_SMS_RETRIEVE = 3;

    /** Id for the back Calendar Loader */
    private static final int LOADER_CALENDAR_ID = 2;
    private static final String KEY_LOADER_EXTRA_EMAILS =
            QuickContactActivity.class.getCanonicalName() + ".KEY_LOADER_EXTRA_EMAILS";
    private static final int MAX_PAST_CALENDAR_RETRIEVE = 3;
    private static final int MAX_FUTURE_CALENDAR_RETRIEVE = 3;
    private static final long PAST_MILLISECOND_TO_SEARCH_LOCAL_CALENDAR =
            1L * 24L * 60L * 60L * 1000L /* 1 day */;
    private static final long FUTURE_MILLISECOND_TO_SEARCH_LOCAL_CALENDAR =
            7L * 24L * 60L * 60L * 1000L /* 7 days */;

    /** Id for the background Call Log Loader */
    private static final int LOADER_CALL_LOG_ID = 3;
    private static final int MAX_CALL_LOG_RETRIEVE = 3;
    private static final int MIN_NUM_CONTACT_ENTRIES_SHOWN = 3;
    private static final int MIN_NUM_COLLAPSED_RECENT_ENTRIES_SHOWN = 3;
    private static final int CARD_ENTRY_ID_EDIT_CONTACT = -2;


    private static final int[] mRecentLoaderIds = new int[]{
            LOADER_SMS_ID,
            LOADER_CALENDAR_ID,
            LOADER_CALL_LOG_ID};
    /**
     * ConcurrentHashMap constructor params: 4 is initial table size, 0.9f is
     * load factor before resizing, 1 means we only expect a single thread to
     * write to the map so make only a single shard
     */
//    private Map<Integer, List<ContactInteraction>> mRecentLoaderResults =
//            new ConcurrentHashMap<>(4, 0.9f, 1);

    private static final String FRAGMENT_TAG_SELECT_ACCOUNT = "select_account_fragment";
    private boolean simOneLoadComplete = false;
    private boolean simTwoLoadComplete = false;

    private Context mContext;
    private boolean mEnablePresence = false;

    final View.OnClickListener mEntryClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Object entryTagObject = v.getTag();
            if (entryTagObject == null || !(entryTagObject instanceof ExpandingEntryCardView.EntryTag)) {
                Log.w(TAG, "EntryTag was not used correctly");
                return;
            }
            final ExpandingEntryCardView.EntryTag entryTag = (ExpandingEntryCardView.EntryTag) entryTagObject;
            final Intent intent = entryTag.getIntent();
            final int dataId = entryTag.getId();

            if (dataId == CARD_ENTRY_ID_EDIT_CONTACT) {
                editContact();
                return;
            }

            // Pass the touch point through the intent for use in the InCallUI
            if (Intent.ACTION_CALL.equals(intent.getAction())) {
                if (TouchPointManager.getInstance().hasValidPoint()) {
                    Bundle extras = new Bundle();
                    extras.putParcelable(TouchPointManager.TOUCH_POINT,
                            TouchPointManager.getInstance().getPoint());
                    intent.putExtra(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, extras);
                }
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mHasIntentLaunched = true;
            try {
                ImplicitIntentsUtil.startActivityInAppIfPossible(QuickContactActivity.this, intent);
            } catch (SecurityException ex) {
                Toast.makeText(QuickContactActivity.this, R.string.missing_app,
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "QuickContacts does not have permission to launch "
                        + intent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(QuickContactActivity.this, R.string.missing_app,
                        Toast.LENGTH_SHORT).show();
            }

            // Default to USAGE_TYPE_CALL. Usage is summed among all types for sorting each data id
            // so the exact usage type is not necessary in all cases
            String usageType = ContactsContract.DataUsageFeedback.USAGE_TYPE_CALL;

            final Uri intentUri = intent.getData();
//            if ((intentUri != null && intentUri.getScheme() != null &&
//                    intentUri.getScheme().equals(ContactsUtils.SCHEME_SMSTO)) ||
//                    (intent.getType() != null && intent.getType().equals(MIMETYPE_SMS))) {
//                usageType = ContactsContract.DataUsageFeedback.USAGE_TYPE_SHORT_TEXT;
//            }

            // Data IDs start at 1 so anything less is invalid
            if (dataId > 0) {
                final Uri dataUsageUri = ContactsContract.DataUsageFeedback.FEEDBACK_URI.buildUpon()
                        .appendPath(String.valueOf(dataId))
                        .appendQueryParameter(ContactsContract.DataUsageFeedback.USAGE_TYPE, usageType)
                        .build();
                try {
                    final boolean successful = getContentResolver().update(
                            dataUsageUri, new ContentValues(), null, null) > 0;
                    if (!successful) {
                        Log.w(TAG, "DataUsageFeedback increment failed");
                    }
                } catch (SecurityException ex) {
                    Log.w(TAG, "DataUsageFeedback increment failed", ex);
                }
            } else {
                Log.w(TAG, "Invalid Data ID");
            }
        }
    };

    final ExpandingEntryCardView.ExpandingEntryCardViewListener mExpandingEntryCardViewListener
            = new ExpandingEntryCardView.ExpandingEntryCardViewListener() {
        @Override
        public void onCollapse(int heightDelta) {
            mScroller.prepareForShrinkingScrollChild(heightDelta);
        }

        @Override
        public void onExpand() {
            mScroller.setDisableTouchesForSuppressLayout(/* areTouchesDisabled = */ true);
        }

        @Override
        public void onExpandDone() {
            mScroller.setDisableTouchesForSuppressLayout(/* areTouchesDisabled = */ false);
        }
    };

//    @Override
//    public void onAggregationSuggestionChange() {
//        if (mAggregationSuggestionEngine == null) {
//            return;
//        }
//        mSuggestions = mAggregationSuggestionEngine.getSuggestions();
//        mCollapsedSuggestionCardView.setVisibility(View.GONE);
//        mExpandSuggestionCardView.setVisibility(View.GONE);
//        mSuggestionList.removeAllViews();
//
//        if (mContactData == null) {
//            return;
//        }
//
//        final String suggestionForName = mContactData.getDisplayName();
//        final int suggestionNumber = mSuggestions.size();
//
//        if (suggestionNumber <= 0) {
//            mSelectedAggregationIds.clear();
//            return;
//        }
//
//        ContactPhotoManager.DefaultImageRequest
//                request = new ContactPhotoManager.DefaultImageRequest(
//                suggestionForName, mContactData.getLookupKey(), ContactPhotoManager.TYPE_DEFAULT,
//                /* isCircular */ true );
//        final long photoId = mContactData.getPhotoId();
//        final byte[] photoBytes = mContactData.getThumbnailPhotoBinaryData();
//        if (photoBytes != null) {
//            ContactPhotoManager.getInstance(this).loadThumbnail(mSuggestionSummaryPhoto, photoId,
//                /* darkTheme */ false , /* isCircular */ true , request);
//        } else {
//            ContactPhotoManager.DEFAULT_AVATAR.applyDefaultImage(mSuggestionSummaryPhoto,
//                    -1, false, request);
//        }
//
//        final String suggestionTitle = getResources().getQuantityString(
//                R.plurals.quickcontact_suggestion_card_title, suggestionNumber, suggestionNumber);
//        mCollapsedSuggestionCardTitle.setText(suggestionTitle);
//        mExpandSuggestionCardTitle.setText(suggestionTitle);
//
//        mSuggestionForName.setText(suggestionForName);
//        final int linkedContactsNumber = mContactData.getRawContacts().size();
//        final String contactsInfo;
//        final String accountName = mContactData.getRawContacts().get(0).getAccountName();
//        if (linkedContactsNumber == 1 && accountName == null) {
//            mSuggestionContactsNumber.setVisibility(View.INVISIBLE);
//        }
//        if (linkedContactsNumber == 1 && accountName != null) {
//            contactsInfo = getResources().getString(R.string.contact_from_account_name,
//                    accountName);
//        } else {
//            contactsInfo = getResources().getString(
//                    R.string.quickcontact_contacts_number, linkedContactsNumber);
//        }
//        mSuggestionContactsNumber.setText(contactsInfo);
//
//        final Set<Long> suggestionContactIds = new HashSet<>();
//        for (Suggestion suggestion : mSuggestions) {
//            mSuggestionList.addView(inflateSuggestionListView(suggestion));
//            suggestionContactIds.add(suggestion.contactId);
//        }
//
//        if (mIsSuggestionListCollapsed) {
//            collapseSuggestionList();
//        } else {
//            expandSuggestionList();
//        }
//
//        // Remove contact Ids that are not suggestions.
//        final Set<Long> selectedSuggestionIds = com.google.common.collect.Sets.intersection(
//                mSelectedAggregationIds, suggestionContactIds);
//        mSelectedAggregationIds = new TreeSet<>(selectedSuggestionIds);
//        if (!mSelectedAggregationIds.isEmpty()) {
//            enableLinkButton();
//        }
//    }

    private void collapseSuggestionList() {
        mCollapsedSuggestionCardView.setVisibility(View.VISIBLE);
        mExpandSuggestionCardView.setVisibility(View.GONE);
        mIsSuggestionListCollapsed = true;
    }

    private void expandSuggestionList() {
        mCollapsedSuggestionCardView.setVisibility(View.GONE);
        mExpandSuggestionCardView.setVisibility(View.VISIBLE);
        mIsSuggestionListCollapsed = false;
    }

    private void editContact() {
        mHasIntentLaunched = true;
//        mContactLoader.cacheResult();
//        startActivityForResult(getEditContactIntent(), REQUEST_CODE_CONTACT_EDITOR_ACTIVITY);
    }

//    private View inflateSuggestionListView(final Suggestion suggestion) {
//        final LayoutInflater layoutInflater = LayoutInflater.from(this);
//        final View suggestionView = layoutInflater.inflate(
//                R.layout.quickcontact_suggestion_contact_item, null);
//
//        ContactPhotoManager.DefaultImageRequest
//                request = new ContactPhotoManager.DefaultImageRequest(
//                suggestion.name, suggestion.lookupKey, ContactPhotoManager.TYPE_DEFAULT, /*
//                isCircular */ true);
//        final ImageView photo = (ImageView) suggestionView.findViewById(
//                R.id.aggregation_suggestion_photo);
//        if (suggestion.photo != null) {
//            ContactPhotoManager.getInstance(this).loadThumbnail(photo, suggestion.photoId,
//                   /* darkTheme */ false, /* isCircular */ true, request);
//        } else {
//            ContactPhotoManager.DEFAULT_AVATAR.applyDefaultImage(photo, -1, false, request);
//        }
//
//        final TextView name = (TextView) suggestionView.findViewById(R.id.aggregation_suggestion_name);
//        name.setText(suggestion.name);
//
//        final TextView accountNameView = (TextView) suggestionView.findViewById(
//                R.id.aggregation_suggestion_account_name);
//        final String accountName = suggestion.rawContacts.get(0).accountName;
//        if (!TextUtils.isEmpty(accountName)) {
//            accountNameView.setText(
//                    getResources().getString(R.string.contact_from_account_name, accountName));
//        } else {
//            accountNameView.setVisibility(View.INVISIBLE);
//        }
//
//        final CheckBox checkbox = (CheckBox) suggestionView.findViewById(R.id.suggestion_checkbox);
//        final int[][] stateSet = new int[][] {
//                new int[] { android.R.attr.state_checked },
//                new int[] { -android.R.attr.state_checked }
//        };
//        final int[] colors = new int[] { mColorFilterColor, mColorFilterColor };
//        if (suggestion != null && suggestion.name != null) {
//            checkbox.setContentDescription(suggestion.name + " " +
//                    getResources().getString(R.string.contact_from_account_name, accountName));
//        }
//        checkbox.setButtonTintList(new ColorStateList(stateSet, colors));
//        checkbox.setChecked(mSuggestionsShouldAutoSelected ||
//                mSelectedAggregationIds.contains(suggestion.contactId));
//        if (checkbox.isChecked()) {
//            mSelectedAggregationIds.add(suggestion.contactId);
//        }
//        checkbox.setTag(suggestion.contactId);
//        checkbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final CheckBox checkBox = (CheckBox) v;
//                final Long contactId = (Long) checkBox.getTag();
//                if (mSelectedAggregationIds.contains(mContactData.getId())) {
//                    mSelectedAggregationIds.remove(mContactData.getId());
//                }
//                if (checkBox.isChecked()) {
//                    mSelectedAggregationIds.add(contactId);
//                    if (mSelectedAggregationIds.size() >= 1) {
//                        enableLinkButton();
//                    }
//                } else {
//                    mSelectedAggregationIds.remove(contactId);
//                    mSuggestionsShouldAutoSelected = false;
//                    if (mSelectedAggregationIds.isEmpty()) {
//                        disableLinkButton();
//                    }
//                }
//            }
//        });
//
//        return suggestionView;
//    }

//    private void enableLinkButton() {
//        mSuggestionsLinkButton.setClickable(true);
//        mSuggestionsLinkButton.getBackground().setColorFilter(mColorFilter);
//        mSuggestionsLinkButton.setTextColor(
//                ContextCompat.getColor(this, android.R.color.white));
//        mSuggestionsLinkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Join selected contacts.
//                if (!mSelectedAggregationIds.contains(mContactData.getId())) {
//                    mSelectedAggregationIds.add(mContactData.getId());
//                }
//                JoinContactsDialogFragment.start(
//                        QuickContactActivity.this, mSelectedAggregationIds);
//            }
//        });
//    }

//    @Override
//    public void onContactsJoined() {
//        disableLinkButton();
//    }
//
//    private void disableLinkButton() {
//        mSuggestionsLinkButton.setClickable(false);
//        mSuggestionsLinkButton.getBackground().setColorFilter(
//                ContextCompat.getColor(this, R.color.disabled_button_background),
//                PorterDuff.Mode.SRC_ATOP);
//        mSuggestionsLinkButton.setTextColor(
//                ContextCompat.getColor(this, R.color.disabled_button_text));
//    }

    private interface ContextMenuIds {
        static final int COPY_TEXT = 0;
        static final int CLEAR_DEFAULT = 1;
        static final int SET_DEFAULT = 2;
    }

    private final View.OnCreateContextMenuListener mEntryContextMenuListener =
            new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    if (menuInfo == null) {
                        return;
                    }
                    final ExpandingEntryCardView.EntryContextMenuInfo info = (ExpandingEntryCardView.EntryContextMenuInfo) menuInfo;
                    menu.setHeaderTitle(info.getCopyText());
                    menu.add(ContextMenu.NONE, ContextMenuIds.COPY_TEXT,
                            ContextMenu.NONE, getString(R.string.copy_text));

                    // Don't allow setting or clearing of defaults for non-editable contacts
                    if (!isContactEditable()) {
                        return;
                    }

                    final String selectedMimeType = info.getMimeType();

                    // Defaults to true will only enable the detail to be copied to the clipboard.
                    boolean onlyOneOfMimeType = true;

                    // Only allow primary support for Phone and Email content types
                    if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equals(selectedMimeType)) {
                        onlyOneOfMimeType = mOnlyOnePhoneNumber;
                    } else if (ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE.equals(selectedMimeType)) {
                        onlyOneOfMimeType = mOnlyOneEmail;
                    }

                    // Checking for previously set default
                    if (info.isSuperPrimary()) {
                        menu.add(ContextMenu.NONE, ContextMenuIds.CLEAR_DEFAULT,
                                ContextMenu.NONE, getString(R.string.clear_default));
                    } else if (!onlyOneOfMimeType) {
                        menu.add(ContextMenu.NONE, ContextMenuIds.SET_DEFAULT,
                                ContextMenu.NONE, getString(R.string.set_default));
                    }
                }
            };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandingEntryCardView.EntryContextMenuInfo menuInfo;
        try {
            menuInfo = (ExpandingEntryCardView.EntryContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e(TAG, "bad menuInfo", e);
            return false;
        }

        switch (item.getItemId()) {
            case ContextMenuIds.COPY_TEXT:
//                ClipboardUtils.copyText(this, menuInfo.getCopyLabel(), menuInfo.getCopyText(),
//                        true);
                return true;
            case ContextMenuIds.SET_DEFAULT:
//                final Intent setIntent = ContactSaveService.createSetSuperPrimaryIntent(this,
//                        menuInfo.getId());
//                this.startService(setIntent);
                return true;
            case ContextMenuIds.CLEAR_DEFAULT:
//                final Intent clearIntent = ContactSaveService.createClearPrimaryIntent(this,
//                        menuInfo.getId());
//                this.startService(clearIntent);
                return true;
            default:
                throw new IllegalArgumentException("Unknown menu option " + item.getItemId());
        }
    }
//
//    /**
//     * Headless fragment used to handle account selection callbacks invoked from
//     * {@link DirectoryContactUtil}.
//     */
//    public static class SelectAccountDialogFragmentListener extends Fragment
//            implements SelectAccountDialogFragment.Listener {
//
//        private QuickContactActivity mQuickContactActivity;
//
//        public SelectAccountDialogFragmentListener() {}
//
//        @Override
//        public void onAccountChosen(AccountWithDataSet account, Bundle extraArgs) {
//            DirectoryContactUtil.createCopy(mQuickContactActivity.mContactData.getContentValues(),
//                    account, mQuickContactActivity);
//        }
//
//        @Override
//        public void onAccountSelectorCancelled() {}
//
//        /**
//         * Set the parent activity. Since rotation can cause this fragment to be used across
//         * more than one activity instance, we need to explicitly set this value instead
//         * of making this class non-static.
//         */
//        public void setQuickContactActivity(QuickContactActivity quickContactActivity) {
//            mQuickContactActivity = quickContactActivity;
//        }
//    }

    final MultiShrinkScroller.MultiShrinkScrollerListener mMultiShrinkScrollerListener
            = new MultiShrinkScroller.MultiShrinkScrollerListener() {
        @Override
        public void onScrolledOffBottom() {
            finish();
        }

        @Override
        public void onEnterFullscreen() {
            updateStatusBarColor();
        }

        @Override
        public void onExitFullscreen() {
            updateStatusBarColor();
        }

        @Override
        public void onStartScrollOffBottom() {
            mIsExitAnimationInProgress = true;
        }

        @Override
        public void onEntranceAnimationDone() {
            mIsEntranceAnimationFinished = true;
        }

        @Override
        public void onTransparentViewHeightChange(float ratio) {
            if (mIsEntranceAnimationFinished) {
                mWindowScrim.setAlpha((int) (0xFF * ratio));
            }
        }
    };


    /**
     * Data items are compared to the same mimetype based off of three qualities:
     * 1. Super primary
     * 2. Primary
     * 3. Times used
     */
//    private final Comparator<DataItem> mWithinMimeTypeDataItemComparator =
//            new Comparator<DataItem>() {
//                @Override
//                public int compare(DataItem lhs, DataItem rhs) {
//                    if (!lhs.getMimeType().equals(rhs.getMimeType())) {
//                        Log.wtf(TAG, "Comparing DataItems with different mimetypes lhs.getMimeType(): " +
//                                lhs.getMimeType() + " rhs.getMimeType(): " + rhs.getMimeType());
//                        return 0;
//                    }
//
//                    if (lhs.isSuperPrimary()) {
//                        return -1;
//                    } else if (rhs.isSuperPrimary()) {
//                        return 1;
//                    } else if (lhs.isPrimary() && !rhs.isPrimary()) {
//                        return -1;
//                    } else if (!lhs.isPrimary() && rhs.isPrimary()) {
//                        return 1;
//                    } else {
//                        final int lhsTimesUsed =
//                                lhs.getTimesUsed() == null ? 0 : lhs.getTimesUsed();
//                        final int rhsTimesUsed =
//                                rhs.getTimesUsed() == null ? 0 : rhs.getTimesUsed();
//
//                        return rhsTimesUsed - lhsTimesUsed;
//                    }
//                }
//            };
//
//    /**
//     * Sorts among different mimetypes based off:
//     * 1. Whether one of the mimetypes is the prioritized mimetype
//     * 2. Number of times used
//     * 3. Last time used
//     * 4. Statically defined
//     */
//    private final Comparator<List<DataItem>> mAmongstMimeTypeDataItemComparator =
//            new Comparator<List<DataItem>> () {
//                @Override
//                public int compare(List<DataItem> lhsList, List<DataItem> rhsList) {
//                    final DataItem lhs = lhsList.get(0);
//                    final DataItem rhs = rhsList.get(0);
//                    final String lhsMimeType = lhs.getMimeType();
//                    final String rhsMimeType = rhs.getMimeType();
//
//                    // 1. Whether one of the mimetypes is the prioritized mimetype
//                    if (!TextUtils.isEmpty(mExtraPrioritizedMimeType) && !lhsMimeType.equals(rhsMimeType)) {
//                        if (rhsMimeType.equals(mExtraPrioritizedMimeType)) {
//                            return 1;
//                        }
//                        if (lhsMimeType.equals(mExtraPrioritizedMimeType)) {
//                            return -1;
//                        }
//                    }
//
//                    // 2. Number of times used
//                    final int lhsTimesUsed = lhs.getTimesUsed() == null ? 0 : lhs.getTimesUsed();
//                    final int rhsTimesUsed = rhs.getTimesUsed() == null ? 0 : rhs.getTimesUsed();
//                    final int timesUsedDifference = rhsTimesUsed - lhsTimesUsed;
//                    if (timesUsedDifference != 0) {
//                        return timesUsedDifference;
//                    }
//
//                    // 3. Last time used
//                    final long lhsLastTimeUsed =
//                            lhs.getLastTimeUsed() == null ? 0 : lhs.getLastTimeUsed();
//                    final long rhsLastTimeUsed =
//                            rhs.getLastTimeUsed() == null ? 0 : rhs.getLastTimeUsed();
//                    final long lastTimeUsedDifference = rhsLastTimeUsed - lhsLastTimeUsed;
//                    if (lastTimeUsedDifference > 0) {
//                        return 1;
//                    } else if (lastTimeUsedDifference < 0) {
//                        return -1;
//                    }
//
//                    // 4. Resort to a statically defined mimetype order.
//                    if (!lhsMimeType.equals(rhsMimeType)) {
//                        for (String mimeType : LEADING_MIMETYPES) {
//                            if (lhsMimeType.equals(mimeType)) {
//                                return -1;
//                            } else if (rhsMimeType.equals(mimeType)) {
//                                return 1;
//                            }
//                        }
//                    }
//                    return 0;
//                }
//            };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            TouchPointManager.getInstance().setPoint((int) ev.getRawX(), (int) ev.getRawY());
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Trace.beginSection("onCreate()");
        super.onCreate(savedInstanceState);
//        mContext = this;
//        if (RequestPermissionsActivity.startPermissionActivity(this) ||
//                RequestDesiredPermissionsActivity.startPermissionActivity(this)) {
//            return;
//        }
//
//        final int previousScreenType = getIntent().getIntExtra
//                (EXTRA_PREVIOUS_SCREEN_TYPE, ScreenType.UNKNOWN);
//        Logger.logScreenView(this, ScreenType.QUICK_CONTACT, previousScreenType);

        if (CompatUtils.isLollipopCompatible()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }

//        processIntent(getIntent());

        // Show QuickContact in front of soft input
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        setContentView(R.layout.quickcontact_activity);


        mMaterialColorMapUtils = new MaterialColorMapUtils(getResources());

        mScroller = (MultiShrinkScroller) findViewById(R.id.multiscroller);

        mContactCard = (ExpandingEntryCardView) findViewById(R.id.communication_card);
        mNoContactDetailsCard = (ExpandingEntryCardView) findViewById(R.id.no_contact_data_card);
        mRecentCard = (ExpandingEntryCardView) findViewById(R.id.recent_card);
        mAboutCard = (ExpandingEntryCardView) findViewById(R.id.about_card);

        mCollapsedSuggestionCardView = (CardView) findViewById(R.id.collapsed_suggestion_card);
        mExpandSuggestionCardView = (CardView) findViewById(R.id.expand_suggestion_card);
        mCollapasedSuggestionHeader = findViewById(R.id.collapsed_suggestion_header);
        mCollapsedSuggestionCardTitle = (TextView) findViewById(
                R.id.collapsed_suggestion_card_title);
        mExpandSuggestionCardTitle = (TextView) findViewById(R.id.expand_suggestion_card_title);
        mSuggestionSummaryPhoto = (ImageView) findViewById(R.id.suggestion_icon);
        mSuggestionForName = (TextView) findViewById(R.id.suggestion_for_name);
        mSuggestionContactsNumber = (TextView) findViewById(R.id.suggestion_for_contacts_number);
        mSuggestionList = (LinearLayout) findViewById(R.id.suggestion_list);
        mSuggestionsCancelButton= (Button) findViewById(R.id.cancel_button);
        mSuggestionsLinkButton = (Button) findViewById(R.id.link_button);
        if (savedInstanceState != null) {
            mIsSuggestionListCollapsed = savedInstanceState.getBoolean(
                    KEY_IS_SUGGESTION_LIST_COLLAPSED, true);
            mPreviousContactId = savedInstanceState.getLong(KEY_PREVIOUS_CONTACT_ID);
            mSuggestionsShouldAutoSelected = savedInstanceState.getBoolean(
                    KEY_SUGGESTIONS_AUTO_SELECTED, true);
            mSelectedAggregationIds = (TreeSet<Long>)
                    savedInstanceState.getSerializable(KEY_SELECTED_SUGGESTION_CONTACTS);
        } else {
            mIsSuggestionListCollapsed = true;
            mSelectedAggregationIds.clear();
        }
//        if (mSelectedAggregationIds.isEmpty()) {
//            disableLinkButton();
//        } else {
//            enableLinkButton();
//        }
        mCollapasedSuggestionHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCollapsedSuggestionCardView.setVisibility(View.GONE);
                mExpandSuggestionCardView.setVisibility(View.VISIBLE);
                mIsSuggestionListCollapsed = false;
                mExpandSuggestionCardTitle.requestFocus();
                mExpandSuggestionCardTitle.sendAccessibilityEvent(
                        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            }
        });

        mSuggestionsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCollapsedSuggestionCardView.setVisibility(View.VISIBLE);
                mExpandSuggestionCardView.setVisibility(View.GONE);
                mIsSuggestionListCollapsed = true;
            }
        });

        mNoContactDetailsCard.setOnClickListener(mEntryClickHandler);
        mContactCard.setOnClickListener(mEntryClickHandler);
        mContactCard.setExpandButtonText(
                getResources().getString(R.string.expanding_entry_card_view_see_all));
        mContactCard.setOnCreateContextMenuListener(mEntryContextMenuListener);
        mEnablePresence = getResources().getBoolean(R.bool.config_presence_enabled);
        Log.e(TAG, "onCreate mEnablePresence = " + mEnablePresence);
//        if (mEnablePresence) {
//            mContactCard.disPlayVideoCallSwitch(mEnablePresence);
//            if (!ContactDisplayUtils.mIsBound) {
//                ContactDisplayUtils.bindService(mContext);
//            }
//            mContactCard.setCallBack(new VideoCallingCallback(){
//                @Override
//                public void updateContact(){
//                    if(mContactData != null){
//                        reFreshContact();
//                    }
//                }
//            });
//        }

        mRecentCard.setOnClickListener(mEntryClickHandler);
        mRecentCard.setTitle(getResources().getString(R.string.recent_card_title));

        mAboutCard.setOnClickListener(mEntryClickHandler);
        mAboutCard.setOnCreateContextMenuListener(mEntryContextMenuListener);

        mPhotoView = (QuickContactImageView) findViewById(R.id.photo);
        final View transparentView = findViewById(R.id.transparent_view);
        if (mScroller != null) {
            transparentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mScroller.scrollOffBottom();
                }
            });
        }

        // Allow a shadow to be shown under the toolbar.
        ViewUtil.addRectangularOutlineProvider(findViewById(R.id.toolbar_parent), getResources());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setTitle(null);
        // Put a TextView with a known resource id into the ActionBar. This allows us to easily
        // find the correct TextView location & size later.
        toolbar.addView(getLayoutInflater().inflate(R.layout.quickcontact_title_placeholder, null));

        mHasAlreadyBeenOpened = savedInstanceState != null;
        mIsEntranceAnimationFinished = mHasAlreadyBeenOpened;
        mWindowScrim = new ColorDrawable(SCRIM_COLOR);
        mWindowScrim.setAlpha(0);
        getWindow().setBackgroundDrawable(mWindowScrim);

        mScroller.initialize(mMultiShrinkScrollerListener, mExtraMode == MODE_FULLY_EXPANDED,
                /* maximumHeaderTextSize */ -1,
                /* shouldUpdateNameViewHeight */ true);
        // mScroller needs to perform asynchronous measurements after initalize(), therefore
        // we can't mark this as GONE.
        mScroller.setVisibility(View.INVISIBLE);

        setHeaderNameText(R.string.missing_name);

//        mSelectAccountFragmentListener= (SelectAccountDialogFragmentListener) getFragmentManager()
//                .findFragmentByTag(FRAGMENT_TAG_SELECT_ACCOUNT);
//        if (mSelectAccountFragmentListener == null) {
//            mSelectAccountFragmentListener = new SelectAccountDialogFragmentListener();
//            getFragmentManager().beginTransaction().add(0, mSelectAccountFragmentListener,
//                    FRAGMENT_TAG_SELECT_ACCOUNT).commit();
//            mSelectAccountFragmentListener.setRetainInstance(true);
//        }
//        mSelectAccountFragmentListener.setQuickContactActivity(this);

        SchedulingUtils.doOnPreDraw(mScroller, /* drawNextFrame = */ true,
                new Runnable() {
                    @Override
                    public void run() {
                        if (!mHasAlreadyBeenOpened) {
                            // The initial scrim opacity must match the scrim opacity that would be
                            // achieved by scrolling to the starting position.
                            final float alphaRatio = mExtraMode == MODE_FULLY_EXPANDED ?
                                    1 : mScroller.getStartingTransparentHeightRatio();
                            final int duration = getResources().getInteger(
                                    android.R.integer.config_shortAnimTime);
                            final int desiredAlpha = (int) (0xFF * alphaRatio);
                            ObjectAnimator o = ObjectAnimator.ofInt(mWindowScrim, "alpha", 0,
                                    desiredAlpha).setDuration(duration);

                            o.start();
                        }
                    }
                });

        if (savedInstanceState != null) {
            final int color = savedInstanceState.getInt(KEY_THEME_COLOR, 0);
            SchedulingUtils.doOnPreDraw(mScroller, /* drawNextFrame = */ false,
                    new Runnable() {
                        @Override
                        public void run() {
                            // Need to wait for the pre draw before setting the initial scroll
                            // value. Prior to pre draw all scroll values are invalid.
                            if (mHasAlreadyBeenOpened) {
                                mScroller.setVisibility(View.VISIBLE);
                                mScroller.setScroll(mScroller.getScrollNeededToBeFullScreen());
                            }
                            // Need to wait for pre draw for setting the theme color. Setting the
                            // header tint before the MultiShrinkScroller has been measured will
                            // cause incorrect tinting calculations.
                            if (color != 0) {
                                setThemeColor(mMaterialColorMapUtils
                                        .calculatePrimaryAndSecondaryColor(color));
                            }
                        }
                    });
        }

        Trace.endSection();

        mContactData = new Contact("dsf"
        ,"Tai sao", "+84974878244", "asdf", "df", "hanoi", "coderdaudat@gmail.com", "asf", "asdf", "asfd");
        bindContactData();
    }

    private void updateStatusBarColor() {
        if (mScroller == null || !CompatUtils.isLollipopCompatible()) {
            return;
        }
        final int desiredStatusBarColor;
        // Only use a custom status bar color if QuickContacts touches the top of the viewport.
        if (mScroller.getScrollNeededToBeFullScreen() <= 0) {
            desiredStatusBarColor = mStatusBarColor;
        } else {
            desiredStatusBarColor = Color.TRANSPARENT;
        }
        // Animate to the new color.
        final ObjectAnimator animation = ObjectAnimator.ofInt(getWindow(), "statusBarColor",
                getWindow().getStatusBarColor(), desiredStatusBarColor);
        animation.setDuration(ANIMATION_STATUS_BAR_COLOR_CHANGE_DURATION);
        animation.setEvaluator(new ArgbEvaluator());
        animation.start();
    }

    /**
     * Returns true if it is possible to edit the current contact.
     */
    private boolean isContactEditable() {
        return true;
//        return mContactData != null && !mContactData.isDirectoryEntry();
    }

    /** Assign this string to the view if it is not empty. */
    private void setHeaderNameText(int resId) {
        if (mScroller != null) {
            mScroller.setTitle(getText(resId) == null ? null : getText(resId).toString(),
                    /* isPhoneNumber= */ false);
        }
    }

    private void setThemeColor(MaterialColorMapUtils.MaterialPalette palette) {
        // If the color is invalid, use the predefined default
        mColorFilterColor = palette.mPrimaryColor;
        mScroller.setHeaderTintColor(mColorFilterColor);
        mStatusBarColor = palette.mSecondaryColor;
        updateStatusBarColor();

        mColorFilter =
                new PorterDuffColorFilter(mColorFilterColor, PorterDuff.Mode.SRC_ATOP);
        mContactCard.setColorAndFilter(mColorFilterColor, mColorFilter);
        mRecentCard.setColorAndFilter(mColorFilterColor, mColorFilter);
        mAboutCard.setColorAndFilter(mColorFilterColor, mColorFilter);
        mSuggestionsCancelButton.setTextColor(mColorFilterColor);
    }

    private void bindContactData() {
        Trace.beginSection("bindContactData");
        invalidateOptionsMenu();

        Trace.endSection();
        Trace.beginSection("Set display photo & name");

        mPhotoView.setIsBusiness(false);
        mPhotoSetter.setupContactPhoto(mPhotoView);
//        extractAndApplyTintFromPhotoViewAsynchronously();
//        final String displayName = ContactDisplayUtils.getDisplayName(this, data).toString();
//        setHeaderNameText(mContactData.getDisplayName(), mContactData.getDisplayNameSource() == ContactsContract.DisplayNameSources.PHONE);
//        final String phoneticName = ContactDisplayUtils.getPhoneticName(this, data);
//        if (mScroller != null) {
//            // Show phonetic name only when it doesn't equal the display name.
//            if (!TextUtils.isEmpty(mContactData.getDisplayName()) && !phoneticName.equals(mContactData.getDisplayName())) {
//                mScroller.setPhoneticName(phoneticName);
//            } else {
//                mScroller.setPhoneticNameGone();
//            }
//        }
        mContactCard.setEntryContactName(mContactData.getDisplayName());
        mScroller.setPhoneticName(mContactCard.getEntryContactName());

        Trace.endSection();

//        mEntriesAndActionsTask = new AsyncTask<Void, Void, Cp2DataCardModel>() {
//
//            @Override
//            protected Cp2DataCardModel doInBackground(
//                    Void... params) {
//                return generateDataModelFromContact(data);
//            }
//
//            @Override
//            protected void onPostExecute(Cp2DataCardModel cardDataModel) {
//                super.onPostExecute(cardDataModel);
//                // Check that original AsyncTask parameters are still valid and the activity
//                // is still running before binding to UI. A new intent could invalidate
//                // the results, for example.
//                if (data == mContactData && !isCancelled()) {
//                    bindDataToCards(cardDataModel);
//                    showActivity();
//                }
//            }
//        };
//        mEntriesAndActionsTask.execute();
        showActivity();
    }

    private void showActivity() {
        if (mScroller != null) {
            mScroller.setVisibility(View.VISIBLE);
            SchedulingUtils.doOnPreDraw(mScroller, /* drawNextFrame = */ false,
                    new Runnable() {
                        @Override
                        public void run() {
                            runEntranceAnimation();
                        }
                    });
        }
    }

    private void runEntranceAnimation() {
        if (mHasAlreadyBeenOpened) {
            return;
        }
        mHasAlreadyBeenOpened = true;
        mScroller.scrollUpForEntranceAnimation((mExtraMode != MODE_FULLY_EXPANDED));
    }

//    private boolean isMultiWindowOnPhone() {
//        return MultiWindowCompat.isInMultiWindowMode(this) && PhoneCapabilityTester.isPhone(this);
//    }


}
