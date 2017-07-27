package com.ethan.morephone.presentation.dashboard;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.presentation.phone.service.PhoneService;
import com.ethan.morephone.utils.ActivityUtils;
import com.ethan.morephone.utils.EnumUtil;
import com.twilio.client.Device;

/**
 * Created by Ethan on 3/23/17.
 */

public class DashboardActivity extends BaseActivity {

    private final int MIC_PERMISSION_REQUEST_CODE = 101;

    private UpdateDeviceReceiver mUpdateDeviceReceiver = new UpdateDeviceReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PhoneService.ACTION_UPDATE_DEVICE_STATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateDeviceReceiver, intentFilter);

        IntentFilter intentFilterDevicePartner = new IntentFilter();
        intentFilterDevicePartner.addAction(PhoneService.ACTION_UPDATE_DEVICE_PARTNER_STATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateDeviceReceiver, intentFilterDevicePartner);


        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setTitleActionBar(toolbar, MyPreference.getPhoneNumber(getApplicationContext()));

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ConversationsFragment) return;
        DashboardFrag voiceFragment = DashboardFrag.getInstance(MyPreference.getPhoneNumber(getApplicationContext()), getIntent().getBooleanExtra(DashboardFrag.BUNDLE_CHOOSE_VOICE, false));
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                voiceFragment,
                R.id.content_frame,
                DashboardFrag.class.getSimpleName());


        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone();
        } else {
            startService();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
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
//        PhoneService.startPhoneService(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateDeviceReceiver);
    }


    class UpdateDeviceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null)
                return;
            if (PhoneService.ACTION_UPDATE_DEVICE_STATE.equals(intent.getAction())) {

                Device.State state = EnumUtil.deserialize(Device.State.class).from(intent);

                switch (state) {
                    case BUSY:
                        DebugTool.logD("DEVICE BUSY ");
                        break;
                    case READY:
                        DebugTool.logD("DEVICE READY ");
                        break;
                    case OFFLINE:
                        DebugTool.logD("DEVICE OFFLINE ");
                        break;
                    default:
                        break;
                }

            } else if (PhoneService.ACTION_UPDATE_DEVICE_STATE.equals(intent.getAction())) {

                Device.State state = EnumUtil.deserialize(Device.State.class).from(intent);

                switch (state) {
                    case BUSY:
                        DebugTool.logD("DEVICE PARTNER BUSY ");
                        break;
                    case READY:
                        DebugTool.logD("DEVICE PARTNER READY ");
                        break;
                    case OFFLINE:
                        DebugTool.logD("DEVICE PARTNER OFFLINE ");
                        break;
                    default:
                        break;
                }

            }
        }
    }
}
