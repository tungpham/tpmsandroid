package com.ethan.morephone.presentation.phone.incall;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.dashboard.model.ClientProfile;
import com.ethan.morephone.presentation.phone.dial.DialFragment;
import com.ethan.morephone.presentation.phone.incoming.IncomingFragment;
import com.ethan.morephone.utils.ActivityUtils;
import com.twilio.client.Connection;
import com.twilio.client.ConnectionListener;
import com.twilio.client.Device;
import com.twilio.client.DeviceListener;
import com.twilio.client.PresenceEvent;
import com.twilio.client.Twilio;

import java.util.HashMap;
import java.util.Map;

import static com.twilio.client.impl.TwilioImpl.getContext;

/**
 * Created by Ethan on 4/10/17.
 */

public class InCallActivity extends BaseActivity implements
        View.OnClickListener,
        ConnectionListener,
        DeviceListener,
        DialFragment.DialFragmentListener,
        IncomingFragment.IncomingListener{

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    public static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";

    private static final String TOKEN_SERVICE_URL = "https://numberphone1.herokuapp.com/token";

    private final int MIC_PERMISSION_REQUEST_CODE = 11;

    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_INVALID;
    private boolean speakerPhone;

    private Connection activeConnection;
    private Connection pendingConnection;

    private ImageView mImageSpeaker;

    private Device clientDevice;

    private ClientProfile clientProfile;

    private String mPhoneNumber;
    private String mToPhoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setContentView(R.layout.activity_fragment);

        mPhoneNumber = getIntent().getStringExtra(DashboardActivity.BUNDLE_PHONE_NUMBER);
        mToPhoneNumber = getIntent().getStringExtra(BUNDLE_TO_PHONE_NUMBER);
        showDialFragment();
//        showInCallFragment();

//
//        setContentView(R.layout.fragment_c);
//
//        TextView textToPhoneNumber = (TextView) findViewById(R.id.text_in_call_phone_number);
//
//        String toPhoneNumber = getIntent().getExtras().getString(BUNDLE_TO_PHONE_NUMBER);
//        textToPhoneNumber.setText(toPhoneNumber);


//        mImageSpeaker = (ImageView) findViewById(R.id.image_in_call_speaker);
//        mImageSpeaker.setOnClickListener(this);
//
//        findViewById(R.id.floating_button_in_call_hang_up).setOnClickListener(this);

        audioManager.setSpeakerphoneOn(speakerPhone);

        clientProfile = new ClientProfile(mPhoneNumber, true, true);
         /*
         * Check microphone permissions. Needed in Android M.
         */
        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone();
        } else {
            /*
             * Initialize the Twilio Client SDK
             */
            initializeTwilioClientSDK();
        }

//        makeCall(toPhoneNumber);

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

            pendingConnection = incomingConnection;
            pendingConnection.setConnectionListener(this);
            DebugTool.logD("INCOMING NOW");

            showIncomingFragment();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_in_call_speaker:
                speakerPhone = !speakerPhone;

                setAudioFocus(true);
                audioManager.setSpeakerphoneOn(speakerPhone);

//                if (speakerPhone) {
//                    mImageSpeaker.
//                } else {
//                    mImageSpeaker.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_speaker_off_black_24dp));
//                }
                break;
            case R.id.floating_button_in_call_hang_up:
                disconnect();
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onConnecting(Connection connection) {
        DebugTool.logD("CONNECTING : " + connection.isIncoming());
    }

    @Override
    public void onConnected(Connection connection) {
        DebugTool.logD("CONNECTED");
        showInCallFragment();
    }

    @Override
    public void onDisconnected(Connection connection) {
        DebugTool.logD("DISCONNECTED ");
//        showDialFragment();
        if (connection.getState() == Connection.State.PENDING) {
            DebugTool.logD("PENDING");
        } else if (connection.getState() == Connection.State.CONNECTED) {
            DebugTool.logD("CONNECTED");
        } else if (connection.getState() == Connection.State.CONNECTING) {
            DebugTool.logD("CONNECTING");
        }

        finish();
    }

    @Override
    public void onDisconnected(Connection connection, int i, String s) {
        if (connection == pendingConnection) {
            pendingConnection = null;
//            alertDialog.dismiss();
        } else if (activeConnection != null && connection != null) {
            if (activeConnection == connection) {
                activeConnection = null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        resetUI();
                        showDialFragment();
                    }
                });
            }
            DebugTool.logD("Disconnect");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*
         * Check if microphone permissions is granted
         */
        if (requestCode == MIC_PERMISSION_REQUEST_CODE && permissions.length > 0) {
            boolean granted = true;
            if (granted) {
                /*
                * Initialize the Twilio Client SDK
                */
                initializeTwilioClientSDK();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Microphone permissions needed. Please allow in App Settings for additional functionality.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkPermissionForMicrophone() {
        int resultMic = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        return resultMic == PackageManager.PERMISSION_GRANTED;
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

    private void initializeTwilioClientSDK() {

        if (!Twilio.isInitialized()) {
            Twilio.initialize(getApplicationContext(), new Twilio.InitListener() {

                /*
                 * Now that the SDK is initialized we can register using a Capability Token.
                 * A Capability Token is a JSON Web Token (JWT) that specifies how an associated Device
                 * can interact with Twilio services.
                 */
                @Override
                public void onInitialized() {
//                    Twilio.setLogLevel(Log.DEBUG);
                    /*
                     * Retrieve the Capability Token from your own web server
                     */
                    retrieveCapabilityToken(clientProfile);
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), "Failed to initialize the Twilio Client SDK", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            retrieveCapabilityToken(clientProfile);
            DebugTool.logD("INITED");
        }
    }

    private void retrieveCapabilityToken(final ClientProfile newClientProfile) {

        // Correlate desired properties of the Device (from ClientProfile) to properties of the Capability Token
        Uri.Builder b = Uri.parse(TOKEN_SERVICE_URL).buildUpon();
        if (newClientProfile.isAllowOutgoing()) {
            b.appendQueryParameter("allowOutgoing", newClientProfile.isAllowOutgoing() ? "true" : "false");
        }
        if (newClientProfile.isAllowIncoming() && newClientProfile.getName() != null) {
            b.appendQueryParameter("client", newClientProfile.getName());
        }

        DebugTool.logD("NAME PHONE: " + newClientProfile.getName());
        DebugTool.logD("URL: " + b.toString());
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
    }


    /*
     * Create a Device or update the capabilities of the current Device
     */
    private void createDevice(String capabilityToken) {
        try {
            if (clientDevice == null) {
                clientDevice = Twilio.createDevice(capabilityToken, this);
                clientDevice.setIncomingSoundEnabled(true);

                /*
                 * Providing a PendingIntent to the newly created Device, allowing you to receive incoming calls
                 *
                 *  What you do when you receive the intent depends on the component you set in the Intent.
                 *
                 *  If you're using an Activity, you'll want to override Activity.onNewIntent()
                 *  If you're using a Service, you'll want to override Service.onStartCommand().
                 *  If you're using a BroadcastReceiver, override BroadcastReceiver.onReceive().
                 */

                Intent intent = new Intent(this, InCallActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                clientDevice.setIncomingIntent(pendingIntent);
                DebugTool.logD("CREATE DEVICE: " + clientProfile.getName());

            } else {
                clientDevice.updateCapabilityToken(capabilityToken);
            }

//            EventBus.getDefault().postSticky(clientDevice);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Device error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStartListening(Device device) {
        DebugTool.logD("Device has started listening for incoming connections");
    }

    @Override
    public void onStopListening(Device device) {
        DebugTool.logD("Device has stopped listening for incoming connections");
    }

    @Override
    public void onStopListening(Device device, int i, String s) {
        DebugTool.logD(String.format("Device has encountered an error and has stopped" +
                " listening for incoming connections: %s", s));
    }

    @Override
    public boolean receivePresenceEvents(Device device) {
        return false;
    }

    @Override
    public void onPresenceChanged(Device device, PresenceEvent presenceEvent) {

    }

    private void disconnect() {
        if (activeConnection != null) {
            activeConnection.disconnect();
            activeConnection = null;
        }
    }

    private void setAudioFocus(boolean setFocus) {
        if (audioManager != null) {
            if (setFocus) {
                savedAudioMode = audioManager.getMode();
                // Request audio focus before making any device switch.
                audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                /*
                 * Start by setting MODE_IN_COMMUNICATION as default audio mode. It is
                 * required to be in this mode when playout and/or recording starts for
                 * best possible VoIP performance. Some devices have difficulties with speaker mode
                 * if this is not set.
                 */
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(savedAudioMode);
                audioManager.abandonAudioFocus(null);
            }
        }
    }


    @Override
    public void onCallNow(String toPhoneNumber) {
        Map<String, String> params = new HashMap<>();
//        toPhoneNumber = "client:" + toPhoneNumber.trim();
        params.put("To", toPhoneNumber);
        if (clientDevice != null) {
            // Create an outgoing connection
            activeConnection = clientDevice.connect(params, this);
            DebugTool.logD("MAKE A CALL : " + toPhoneNumber);
//            setCallUI();
            showOutgoingFragment();
        } else {
            Toast.makeText(getContext(), "No existing device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void decline() {
        if (pendingConnection != null) {
            pendingConnection.reject();
        }
        showDialFragment();
    }

    @Override
    public void accept() {
        if (activeConnection != null) {
            activeConnection.disconnect();
        }
        pendingConnection.accept();
        activeConnection = pendingConnection;
        pendingConnection = null;
        showInCallFragment();
    }

    private void showInCallFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof InCallFragment) return;
        InCallFragment incomingFragment = InCallFragment.getInstance(mPhoneNumber, mPhoneNumber);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                incomingFragment,
                R.id.content_frame,
                InCallFragment.class.getSimpleName());
    }

    private void showOutgoingFragment() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
//        if (fragment instanceof IncomingFragment) return;
//        OutgoingFragment outgoingFragment = OutgoingFragment.getInstance(mPhoneNumber);
//        ActivityUtils.replaceFragmentToActivity(
//                getSupportFragmentManager(),
//                outgoingFragment,
//                R.id.content_frame,
//                OutgoingFragment.class.getSimpleName());
//        outgoingFragment.setOutGoingFragmentListener(this);
    }

    private void showIncomingFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof IncomingFragment) return;
        IncomingFragment incomingFragment = IncomingFragment.getInstance(mPhoneNumber, "");
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                incomingFragment,
                R.id.content_frame,
                IncomingFragment.class.getSimpleName());
        incomingFragment.setIncomingListener(this);
    }

    private void showDialFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof DialFragment) return;
        DialFragment dialFragment = DialFragment.getInstance(mPhoneNumber, mToPhoneNumber);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                dialFragment,
                R.id.content_frame,
                DialFragment.class.getSimpleName());
        dialFragment.setDialFragmentListener(this);
    }

//    @Override
//    public void hangUp() {
//        disconnect();
//        finish();
////        showDialFragment();
//    }
//
//    @Override
//    public void sendDigit(int digit) {
//        DebugTool.logD("DIGIT: " + digit);
//        if (activeConnection != null) {
//            activeConnection.sendDigits(String.valueOf(digit));
//        }
//    }
//
//    @Override
//    public void onHangup() {
//        disconnect();
//        finish();
////        showDialFragment();
//    }
}
