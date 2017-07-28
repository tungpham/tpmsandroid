package com.ethan.morephone.presentation.phone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.phone.dial.DialFragment;
import com.ethan.morephone.presentation.phone.incall.InCallFragment;
import com.ethan.morephone.presentation.phone.incoming.IncomingFragment;
import com.ethan.morephone.presentation.phone.outgoing.OutgoingFragment;
import com.ethan.morephone.presentation.phone.service.PhoneService;
import com.ethan.morephone.utils.ActivityUtils;
import com.twilio.client.Connection;
import com.twilio.client.Device;

/**
 * Created by Ethan on 4/27/17.
 */

public class PhoneActivity extends BaseActivity implements
        DialFragment.DialFragmentListener,
        IncomingFragment.IncomingListener {

    private PhoneService mPhoneService;
    private UpdateUIPhoneReceiver mUpdateUIPhoneReceiver = new UpdateUIPhoneReceiver();

    private String mFromPhoneNumber;
    private String mToPhoneNumber;

    public static void starterOutgoing(Activity activity, String fromPhoneNumber, String toPhoneNumber){
        Intent intent = new Intent(activity, PhoneActivity.class);
        intent.putExtra(PhoneService.EXTRA_PHONE_STATE, PhoneService.PHONE_STATE_OUTGOING);
        intent.putExtra(PhoneService.EXTRA_FROM_PHONE_NUMBER, fromPhoneNumber);
        intent.putExtra(PhoneService.EXTRA_TO_PHONE_NUMBER, toPhoneNumber);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_fragment);

        Intent service = new Intent(this, PhoneService.class);
        bindService(service, mConnection, Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PhoneService.ACTION_UPDATE_UI);
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateUIPhoneReceiver, intentFilter);

        mFromPhoneNumber = getIntent().getStringExtra(PhoneService.EXTRA_FROM_PHONE_NUMBER);
        mToPhoneNumber = getIntent().getStringExtra(PhoneService.EXTRA_TO_PHONE_NUMBER);

        int phoneState = getIntent().getIntExtra(PhoneService.EXTRA_PHONE_STATE, PhoneService.PHONE_STATE_DEFAULT);
        if (phoneState == PhoneService.PHONE_STATE_OUTGOING) {
            showOutgoingFragment(mFromPhoneNumber, mToPhoneNumber);
            PhoneService.startServiceWithAction(getApplicationContext(), PhoneService.ACTION_OUTGOING, mFromPhoneNumber, mToPhoneNumber);
        } else if (phoneState == PhoneService.PHONE_STATE_INCOMING) {
            showIncomingFragment(mFromPhoneNumber, mToPhoneNumber);
        }

        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
/* | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON */,
                PixelFormat.RGBA_8888);

//        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
//        mWindowManager.addView(frameLayout, mLayoutParams);

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();

        if (intent != null) {
            /*
             * Determine if the receiving Intent has an extra for the incoming connection. If so,
             * remove it from the Intent to prevent handling it again next time the Activity is resumed
             */
            Device device = intent.getParcelableExtra(Device.EXTRA_DEVICE);
            Connection incomingConnection = intent.getParcelableExtra(Device.EXTRA_CONNECTION);

            if (incomingConnection == null && device == null) {
                return;
            }
            intent.removeExtra(Device.EXTRA_DEVICE);
            intent.removeExtra(Device.EXTRA_CONNECTION);

            mFromPhoneNumber = incomingConnection.getParameters().get(incomingConnection.IncomingParameterFromKey);
            mToPhoneNumber = incomingConnection.getParameters().get(incomingConnection.IncomingParameterToKey);

            PhoneService.startServiceWithAction(getApplicationContext(), PhoneService.ACTION_INCOMING, mFromPhoneNumber, mToPhoneNumber, device, incomingConnection);
            showIncomingFragment(mFromPhoneNumber, mToPhoneNumber);
            DebugTool.logD("RESUME INCOMING");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateUIPhoneReceiver);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mPhoneService = ((PhoneService.LocalBinder) service).getService();
            DebugTool.logD("SERVICE CONNECTED PHONE");
        }

        public void onServiceDisconnected(ComponentName className) {
            mPhoneService = null;
        }
    };

    private void showInCallFragment(String fromPhoneNumber, String toPhoneNumber) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof InCallFragment) return;
        InCallFragment incomingFragment = InCallFragment.getInstance(fromPhoneNumber, toPhoneNumber);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                incomingFragment,
                R.id.content_frame,
                InCallFragment.class.getSimpleName());
    }

    private void showOutgoingFragment(String fromPhoneNumber, String toPhoneNumber) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof IncomingFragment) return;
        OutgoingFragment outgoingFragment = OutgoingFragment.getInstance(fromPhoneNumber, toPhoneNumber);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                outgoingFragment,
                R.id.content_frame,
                OutgoingFragment.class.getSimpleName());
    }

    private void showIncomingFragment(String fromPhoneNumber, String toPhoneNumber) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof IncomingFragment) return;
        IncomingFragment incomingFragment = IncomingFragment.getInstance(fromPhoneNumber, toPhoneNumber);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                incomingFragment,
                R.id.content_frame,
                IncomingFragment.class.getSimpleName());
        incomingFragment.setIncomingListener(this);
    }

    private void showDialFragment(String fromPhoneNumber, String toPhoneNumber) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof DialFragment) return;
        DialFragment dialFragment = DialFragment.getInstance(fromPhoneNumber, toPhoneNumber);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                dialFragment,
                R.id.content_frame,
                DialFragment.class.getSimpleName());
        dialFragment.setDialFragmentListener(this);
    }

    @Override
    public void onCallNow(String toPhoneNumber) {

    }

    @Override
    public void decline() {
        PhoneService.startServiceWithAction(getApplicationContext(), PhoneService.ACTION_DECLINE_INCOMING, mFromPhoneNumber, mToPhoneNumber);
    }

    @Override
    public void accept() {
        PhoneService.startServiceWithAction(getApplicationContext(), PhoneService.ACTION_ACCEPT_INCOMING, mFromPhoneNumber, mToPhoneNumber);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            PhoneService.startServiceWithAction(getApplicationContext(), PhoneService.ACTION_HANG_UP, mFromPhoneNumber, mToPhoneNumber);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    class UpdateUIPhoneReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null)
                return;
            if (PhoneService.ACTION_UPDATE_UI.equals(intent.getAction())) {
                //Check type UI
                int phoneState = intent.getIntExtra(PhoneService.EXTRA_PHONE_STATE, PhoneService.PHONE_STATE_DEFAULT);
                String fromPhoneNumber = intent.getStringExtra(PhoneService.EXTRA_FROM_PHONE_NUMBER);
                String toPhoneNumber = intent.getStringExtra(PhoneService.EXTRA_TO_PHONE_NUMBER);
                if (phoneState == PhoneService.PHONE_STATE_OUTGOING) {
                    showOutgoingFragment(fromPhoneNumber, toPhoneNumber);
                } else if (phoneState == PhoneService.PHONE_STATE_IN_CALL) {
                    showInCallFragment(fromPhoneNumber, toPhoneNumber);
                } else if (phoneState == PhoneService.PHONE_STATE_INCOMING) {
                    showIncomingFragment(fromPhoneNumber, toPhoneNumber);
                    DebugTool.logD("UPDATE INCOMING");
                } else if (phoneState == PhoneService.PHONE_STATE_DISCONNECTED) {
//                    Toast.makeText(getApplicationContext(), getString(R.string.all_call_disconnected), Toast.LENGTH_SHORT).show();
                    finish();
                } else if (phoneState == PhoneService.PHONE_STATE_HANG_UP) {
                    finish();
                } else if (phoneState == PhoneService.PHONE_STATE_DECLINE) {
                    finish();
                }
            }
        }
    }
}
