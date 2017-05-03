package com.ethan.morephone.presentation.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberActivity;
import com.ethan.morephone.presentation.dashboard.DashboardFrag;
import com.ethan.morephone.presentation.dashboard.model.ClientProfile;
import com.ethan.morephone.presentation.license.LicenseActivity;
import com.ethan.morephone.presentation.message.compose.ComposeActivity;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersActivity;
import com.ethan.morephone.presentation.phone.service.PhoneService;
import com.ethan.morephone.presentation.review.AlertReviewDialog;
import com.ethan.morephone.presentation.setting.SettingActivity;
import com.ethan.morephone.presentation.usage.UsageActivity;
import com.ethan.morephone.utils.ActivityUtils;
import com.twilio.client.Device;

/**
 * Created by Ethan on 3/4/17.
 */

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        RequirePhoneNumberDialog.RequirePhoneNumberListener {

    private static final String TOKEN_SERVICE_URL = "https://numberphone1.herokuapp.com/token";

    private final int REQUEST_INCOMING_PHONE = 100;
    private final int MIC_PERMISSION_REQUEST_CODE = 101;

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Device clientDevice;
    private ClientProfile clientProfile;

//    private PhoneService mPhoneService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        setUpNavigation();

        boolean isVoice = false;
        if (getIntent() != null) {
            isVoice = getIntent().getBooleanExtra(DashboardFrag.BUNDLE_CHOOSE_VOICE, false);
        }
        checkRequirePhoneNumber(isVoice);

        MyPreference.setTimesUse(getApplicationContext(), MyPreference.getTimesUse(getApplicationContext()) + 1);


        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone();
        } else {
            startService();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Intent service = new Intent(this, PhoneService.class);
//        bindService(service, mConnection, Context.BIND_AUTO_CREATE);
//        registerReceiver(mMessageReceiver, new IntentFilter(MusicPlayerService.ACTION_UPDATE_UI));
//        startAudio();
    }

    @Override
    public void onPause() {
        super.onPause();
//        unbindService(mConnection);
//        unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setUpNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

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
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.nav_license:
                startActivity(new Intent(this, LicenseActivity.class));
                break;
            case R.id.nav_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Share more get nothing!");
                startActivity(Intent.createChooser(sharingIntent, "More phone more girl!"));
                break;
            case R.id.nav_review:
                AlertReviewDialog alertReviewDialog = AlertReviewDialog.getInstance();
                alertReviewDialog.show(getSupportFragmentManager(), AlertReviewDialog.class.getSimpleName());
                break;
            case R.id.nav_usage:
                startActivity(new Intent(this, UsageActivity.class));
                break;
            default:
                break;
        }
        return false;
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


    /*------------------------------------------------PERMISSION MIC ----------------------------------------*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*
         * Check if microphone permissions is granted
         */
        if (requestCode == MIC_PERMISSION_REQUEST_CODE && permissions.length > 0) {
            boolean granted = true;
            if (granted) {
                startService();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Microphone permissions needed. Please allow in App Settings for additional functionality.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkPermissionForMicrophone() {
        int resultMic = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (resultMic == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissionForMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(getApplicationContext(),
                    "Microphone permissions needed. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MIC_PERMISSION_REQUEST_CODE);
        }
    }

    private void startService() {
        PhoneService.startPhoneService(getApplicationContext());
    }

//    private ServiceConnection mConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            mPhoneService = ((PhoneService.LocalBinder) service).getService();
//            DebugTool.logD("SERVICE CONNECTED ");
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            mPhoneService = null;
//        }
//    };


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
            DashboardFrag numbersFragment = DashboardFrag.getInstance(MyPreference.getPhoneNumber(getApplicationContext()), isVoice);
            ActivityUtils.replaceFragmentToActivity(
                    getSupportFragmentManager(),
                    numbersFragment,
                    R.id.content_frame,
                    DashboardFrag.class.getSimpleName());
        }
        enableActionBar(mToolbar, MyPreference.getPhoneNumber(getApplicationContext()));
    }
}
