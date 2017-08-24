package com.ethan.morephone.presentation.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.morephone.data.database.DatabaseHelpper;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.utils.CredentialsManager;
import com.android.morephone.data.utils.TwilioManager;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.authentication.AuthenticationActivity;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberActivity;
import com.ethan.morephone.presentation.buy.payment.fund.AddFundActivity;
import com.ethan.morephone.presentation.buy.pool.PoolPhoneNumberActivity;
import com.ethan.morephone.presentation.credit.CreditActivity;
import com.ethan.morephone.presentation.license.LicenseActivity;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersFragment;
import com.ethan.morephone.presentation.phone.service.PhoneService;
import com.ethan.morephone.presentation.review.AlertReviewDialog;
import com.ethan.morephone.presentation.setting.SettingActivity;
import com.ethan.morephone.presentation.usage.UsageActivity;
import com.ethan.morephone.utils.ActivityUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Ethan on 3/4/17.
 */

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        EasyPermissions.PermissionCallbacks,
        OptionBuyPhoneNumberDialog.OptionBuyPhoneNumberListener {

    private final int MIC_PERMISSION_REQUEST_CODE = 101;

    public static final int REQUEST_INCOMING_PHONE = 100;
    public static final int REQUEST_BUY_PHONE_NUMBER = REQUEST_INCOMING_PHONE + 1;

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

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

        checkRequirePhoneNumber();

        MyPreference.setTimesUse(getApplicationContext(), MyPreference.getTimesUse(getApplicationContext()) + 1);

        recordPermission();
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

        View header = navigationView.getHeaderView(0);

        TextView textName = (TextView) header.findViewById(R.id.text_header_main_name);
        TextView textEmail = (TextView) header.findViewById(R.id.text_header_main_email);

        if (!TextUtils.isEmpty(MyPreference.getUserName(getApplicationContext()))) {
            textName.setText(MyPreference.getUserName(getApplicationContext()));
        } else if (!TextUtils.isEmpty(MyPreference.getGivenName(getApplicationContext()))) {
            textName.setText(MyPreference.getGivenName(getApplicationContext()));
        }

        textEmail.setText(MyPreference.getUserEmail(getApplicationContext()));

        ImageView imagePicture = (ImageView) header.findViewById(R.id.image_picture);
        if (!TextUtils.isEmpty(MyPreference.getUserPicture(getApplicationContext()))) {
            Picasso.with(getApplicationContext())
                    .load(MyPreference.getUserPicture(getApplicationContext()))
                    .transform(new CircleTransform())
                    .into(imagePicture);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_card:
                startActivity(new Intent(this, CreditActivity.class));
                break;
            case R.id.nav_buy_number:
                OptionBuyPhoneNumberDialog optionBuyPhoneNumberDialog = OptionBuyPhoneNumberDialog.getInstance();
                optionBuyPhoneNumberDialog.show(getSupportFragmentManager(), OptionBuyPhoneNumberDialog.class.getSimpleName());
                optionBuyPhoneNumberDialog.setOptionPhoneNumberListener(this);
                break;
//            case R.id.nav_setting:
//                startActivity(new Intent(this, SettingActivity.class));
//                break;
            case R.id.nav_license:
                startActivity(new Intent(this, LicenseActivity.class));
                break;
            case R.id.nav_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Anonymously own one or many phone numbers");
                startActivity(Intent.createChooser(sharingIntent, "Share now"));
                break;
            case R.id.nav_review:
                AlertReviewDialog alertReviewDialog = AlertReviewDialog.getInstance();
                alertReviewDialog.show(getSupportFragmentManager(), AlertReviewDialog.class.getSimpleName());
                break;
            case R.id.nav_usage:
                startActivity(new Intent(this, UsageActivity.class));
                break;
            case R.id.nav_logout:
                CredentialsManager.deleteCredentials(this);
                MyPreference.setUserId(getApplicationContext(), "");
                MyPreference.setUserEmail(getApplicationContext(), "");
                MyPreference.setRegsiterPhoneNumber(getApplicationContext(), false);
                TwilioManager.saveTwilio(getApplicationContext(), "", "");
                TwilioManager.setApplicationSid(getApplicationContext(), "");

//                Set<String> phoneNumbersUsage = MyPreference.getPhoneNumberUsage(getApplicationContext());
                List<PhoneNumber> phoneNumbers = DatabaseHelpper.findAll(getApplicationContext());
                for (PhoneNumber phoneNumber : phoneNumbers) {
                    PhoneService.startServiceUnregisterPhoneNumber(getApplicationContext(), phoneNumber.getPhoneNumber(), phoneNumber.getSid());
                }

//                MyPreference.setPhoneNumberUsage(getApplicationContext(), new ArraySet<String>());

                startActivity(new Intent(this, AuthenticationActivity.class));
                finish();
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
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DebugTool.logD("requestCode: " + requestCode);
        switch (requestCode) {
            case REQUEST_BUY_PHONE_NUMBER:
                if (resultCode == RESULT_OK) {
                    checkRequirePhoneNumber();
                }
                break;

            default:
                break;
        }

    }

    private void checkRequirePhoneNumber() {
        IncomingPhoneNumbersFragment incomingPhoneNumbersFragment = IncomingPhoneNumbersFragment.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                incomingPhoneNumbersFragment,
                R.id.content_frame,
                IncomingPhoneNumbersFragment.class.getSimpleName());
        enableActionBar(mToolbar, getString(R.string.my_number_label));
    }

    private AuthenticationAPIClient authenticationClient;

    private void setUpAuthentication() {
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        auth0.setLoggingEnabled(true);
        auth0.setOIDCConformant(true);
        authenticationClient = new AuthenticationAPIClient(auth0);
    }

    private void renewAuthentication() {
        String refreshToken = CredentialsManager.getCredentials(this).getRefreshToken();
        authenticationClient.renewAuth(refreshToken).start(new BaseCallback<Credentials, AuthenticationException>() {
            @Override
            public void onSuccess(final Credentials payload) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "New access_token: " + payload.getAccessToken(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(AuthenticationException error) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Failed to get the new access_token", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void startService() {
        if (!PhoneService.isMyServiceRunning(getApplicationContext())) {
            PhoneService.startPhoneService(getApplicationContext());
        }
    }

     /*------------------------------------------------PERMISSION MIC ----------------------------------------*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

//        switch (requestCode) {
//            case MIC_PERMISSION_REQUEST_CODE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    startService();
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(getApplicationContext(),
//                            "Microphone permissions needed. Please allow in App Settings for additional functionality.",
//                            Toast.LENGTH_LONG).show();
//                    finish();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
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
            DebugTool.logD("SHOW REQUEST");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MIC_PERMISSION_REQUEST_CODE);
        }
    }


    private void logout() {
        CredentialsManager.deleteCredentials(this);
        startActivity(new Intent(this, AuthenticationActivity.class));
        finish();
    }

    @AfterPermissionGranted(MIC_PERMISSION_REQUEST_CODE)
    public void recordPermission() {
        String perm = Manifest.permission.RECORD_AUDIO;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, Manifest.permission.RECORD_AUDIO, MIC_PERMISSION_REQUEST_CODE, perm);
        } else {
            startService();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startService();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        List<String> strings = new ArrayList<>();
        strings.add(Manifest.permission.RECORD_AUDIO);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, strings)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onOptionBuyPhoneNumber(boolean pool) {
        if (pool) {
            startActivityForResult(new Intent(this, PoolPhoneNumberActivity.class), REQUEST_BUY_PHONE_NUMBER);
        } else {
            startActivityForResult(new Intent(this, SearchPhoneNumberActivity.class), REQUEST_BUY_PHONE_NUMBER);
        }
    }


    public class CircleTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
