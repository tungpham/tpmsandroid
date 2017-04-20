package com.ethan.morephone.presentation.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberActivity;
import com.ethan.morephone.presentation.dashboard.DashboardFrag;
import com.ethan.morephone.presentation.dashboard.model.ClientProfile;
import com.ethan.morephone.presentation.message.compose.ComposeActivity;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersActivity;
import com.ethan.morephone.utils.ActivityUtils;
import com.twilio.client.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/4/17.
 */

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        RequirePhoneNumberDialog.RequirePhoneNumberListener {

    private static final String TOKEN_SERVICE_URL = "https://numberphone1.herokuapp.com/token";

    private final int MIC_PERMISSION_REQUEST_CODE = 11;

    private ClientProfile clientProfile;

    private Device clientDevice;

    private String mPhoneNumber;

    private final int REQUEST_INCOMING_PHONE = 100;

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(mToolbar, getString(R.string.dashboard_label));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        setUpNavigation();

        mPhoneNumber = MyPreference.getPhoneNumber(getApplicationContext());
        checkRequirePhoneNumber(false);


//
//        clientProfile = new ClientProfile(mPhoneNumber, true, true);
//
//        if (!checkPermissionForMicrophone()) {
//            requestPermissionForMicrophone();
//        } else {
//            initializeTwilioClientSDK();
//        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        checkRequirePhoneNumber(false);
    }

    private void setUpNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        /*
//         * Check if microphone permissions is granted
//         */
//        if (requestCode == MIC_PERMISSION_REQUEST_CODE && permissions.length > 0) {
//            boolean granted = true;
//            if (granted) {
//                /*
//                * Initialize the Twilio Client SDK
//                */
//                initializeTwilioClientSDK();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Microphone permissions needed. Please allow in App Settings for additional functionality.",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private boolean checkPermissionForMicrophone() {
//        int resultMic = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
//        if (resultMic == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        return false;
//    }
//
//    private void requestPermissionForMicrophone() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
//            Toast.makeText(getApplicationContext(),
//                    "Microphone permissions needed. Please allow in App Settings for additional functionality.",
//                    Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(
//                    this,
//                    new String[]{Manifest.permission.RECORD_AUDIO},
//                    MIC_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    private void initializeTwilioClientSDK() {
//
//        if (!Twilio.isInitialized()) {
//            Twilio.initialize(getApplicationContext(), new Twilio.InitListener() {
//
//                /*
//                 * Now that the SDK is initialized we can register using a Capability Token.
//                 * A Capability Token is a JSON Web Token (JWT) that specifies how an associated Device
//                 * can interact with Twilio services.
//                 */
//                @Override
//                public void onInitialized() {
////                    Twilio.setLogLevel(Log.DEBUG);
//                    /*
//                     * Retrieve the Capability Token from your own web server
//                     */
//                    retrieveCapabilityToken(clientProfile);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Toast.makeText(getContext(), "Failed to initialize the Twilio Client SDK", Toast.LENGTH_LONG).show();
//                }
//            });
//        } else {
//            DebugTool.logD("INITED");
//        }
//    }
//
//    private void retrieveCapabilityToken(final ClientProfile newClientProfile) {
//
//        // Correlate desired properties of the Device (from ClientProfile) to properties of the Capability Token
//        Uri.Builder b = Uri.parse(TOKEN_SERVICE_URL).buildUpon();
//        if (newClientProfile.isAllowOutgoing()) {
//            b.appendQueryParameter("allowOutgoing", newClientProfile.isAllowOutgoing() ? "true" : "false");
//        }
//        if (newClientProfile.isAllowIncoming() && newClientProfile.getName() != null) {
//            b.appendQueryParameter("client", newClientProfile.getName());
//        }
//
//        DebugTool.logD("NAME PHONE: " + newClientProfile.getName());
//
//        Ion.with(getContext())
//                .load(b.toString())
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String capabilityToken) {
//                        if (e == null) {
//
//                            // Update the current Client Profile to represent current properties
//                            clientProfile = newClientProfile;
//
//                            DebugTool.logD("NEW: " + clientProfile.getName());
//                            // Create a Device with the Capability Token
//                            createDevice(capabilityToken);
//                        } else {
//                            Toast.makeText(getContext(), "Error retrieving token", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//
//    /*
//     * Create a Device or update the capabilities of the current Device
//     */
//    private void createDevice(String capabilityToken) {
//        try {
//            if (clientDevice == null) {
//                clientDevice = Twilio.createDevice(capabilityToken, this);
//                clientDevice.setIncomingSoundEnabled(true);
//
//                /*
//                 * Providing a PendingIntent to the newly created Device, allowing you to receive incoming calls
//                 *
//                 *  What you do when you receive the intent depends on the component you set in the Intent.
//                 *
//                 *  If you're using an Activity, you'll want to override Activity.onNewIntent()
//                 *  If you're using a Service, you'll want to override Service.onStartCommand().
//                 *  If you're using a BroadcastReceiver, override BroadcastReceiver.onReceive().
//                 */
//
//                Intent intent = new Intent(this, InCallActivity.class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                clientDevice.setIncomingIntent(pendingIntent);
//                DebugTool.logD("CREATE DEVICE: " + clientProfile.getName());
//
//            } else {
//                clientDevice.updateCapabilityToken(capabilityToken);
//            }
//
//            EventBus.getDefault().postSticky(clientDevice);
//
//        } catch (Exception e) {
//            Toast.makeText(getContext(), "Device error", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    @Override
//    public void onStartListening(Device device) {
//        DebugTool.logD("Device has started listening for incoming connections");
//    }
//
//    @Override
//    public void onStopListening(Device device) {
//        DebugTool.logD("Device has stopped listening for incoming connections");
//    }
//
//    @Override
//    public void onStopListening(Device device, int i, String s) {
//        DebugTool.logD(String.format("Device has encountered an error and has stopped" +
//                " listening for incoming connections: %s", s));
//    }
//
//    @Override
//    public boolean receivePresenceEvents(Device device) {
//        return false;
//    }
//
//    @Override
//    public void onPresenceChanged(Device device, PresenceEvent presenceEvent) {
//
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_numbers:
                startActivityForResult(new Intent(this, IncomingPhoneNumbersActivity.class), REQUEST_INCOMING_PHONE);
                break;
            case R.id.nav_buy_number:
                startActivity(new Intent(this, SearchPhoneNumberActivity.class));
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_conversation, menu);
//
//        final MenuItem item = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(this);
//
//        MenuItemCompat.setOnActionExpandListener(item,
//                new MenuItemCompat.OnActionExpandListener() {
//                    @Override
//                    public boolean onMenuItemActionCollapse(MenuItem item) {
//                        // Do something when collapsed
////                        mConversationListAdapter.setFilter(mConversationEntities);
//                        return true; // Return true to collapse action view
//                    }
//
//                    @Override
//                    public boolean onMenuItemActionExpand(MenuItem item) {
//                        // Do something when expanded
//                        return true; // Return true to expand action view
//                    }
//                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<MessageItem> filter(List<MessageItem> models, String query) {
        query = query.toLowerCase();
        final List<MessageItem> filteredModelList = new ArrayList<>();
        for (MessageItem model : models) {
            final String text = model.body.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_new_compose:
                startActivity(new Intent(this, ComposeActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INCOMING_PHONE && resultCode == RESULT_OK) {

            boolean isVoice = false;
            if (data != null) {
                isVoice = data.getBooleanExtra(DashboardFrag.BUNDLE_CHOOSE_VOICE, false);
            }
            checkRequirePhoneNumber(isVoice);
        }
    }

    @Override
    public void onChoosePhone() {
        startActivity(new Intent(this, IncomingPhoneNumbersActivity.class));
    }

    @Override
    public void onBuyPhone() {
        startActivity(new Intent(this, SearchPhoneNumberActivity.class));
    }


    private void checkRequirePhoneNumber(boolean isVoice) {
        if (TextUtils.isEmpty(MyPreference.getPhoneNumber(getApplicationContext()))) {
            RequirePhoneNumberDialog requirePhoneNumberDialog = RequirePhoneNumberDialog.getInstance();
            requirePhoneNumberDialog.show(getSupportFragmentManager(), RequirePhoneNumberDialog.class.getSimpleName());
            requirePhoneNumberDialog.setRequirePhoneNumberListener(this);
            ActivityUtils.replaceFragmentToActivity(
                    getSupportFragmentManager(),
                    new Fragment(),
                    R.id.content_frame,
                    DashboardFrag.class.getSimpleName());
        } else {
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            DashboardFrag numbersFragment = DashboardFrag.getInstance(MyPreference.getPhoneNumber(getApplicationContext()), isVoice);
            ActivityUtils.replaceFragmentToActivity(
                    getSupportFragmentManager(),
                    numbersFragment,
                    R.id.content_frame,
                    DashboardFrag.class.getSimpleName());
        }
    }
}
