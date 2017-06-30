package com.ethan.morephone.presentation.phone.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.presentation.dashboard.model.ClientProfile;
import com.ethan.morephone.presentation.phone.PhoneActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
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
 * Created by Ethan on 4/27/17.
 */

public class PhoneService extends Service implements DeviceListener, ConnectionListener {

//    private static final String TOKEN_SERVICE_URL = "https://numberphone1.herokuapp.com/token";
    private static final String TOKEN_SERVICE_URL = "https://thawing-beyond-50622.herokuapp.com/twilio/create/token";

    public final static String ACTION_WAKEUP = "com.ethan.morephone.action.WAKE_UP";

    public final static String ACTION_OUTGOING = "com.ethan.morephone.action.OUTGOING";
    public final static String ACTION_INCOMING = "com.ethan.morephone.action.INCOMING";
    public final static String ACTION_HANG_UP = "com.ethan.morephone.action.HANG_UP";
    public final static String ACTION_ACCEPT_INCOMING = "com.ethan.morephone.action.ACCEPT_INCOMING";
    public final static String ACTION_DECLINE_INCOMING = "com.ethan.morephone.action.DECLINE_INCOMING";
    public final static String ACTION_SEND_DIGITS = "com.ethan.morephone.action.SEND_DIGITS";
    public final static String ACTION_MUTE_MICOPHONE = "com.ethan.morephone.action.MUTE_MICROPHONE";
    public final static String ACTION_SPEAKER_PHONE = "com.ethan.morephone.action.SPEAKER_PHONE";
    public final static String ACTION_UPDATE_UI = "com.ethan.morephonet.action.ACTION_UPDATE_UI";

    public final static String EXTRA_PHONE_STATE = "EXTRA_PHONE_STATE";
    public final static String EXTRA_FROM_PHONE_NUMBER = "EXTRA_FROM_PHONE_NUMBER";
    public final static String EXTRA_TO_PHONE_NUMBER = "EXTRA_TO_PHONE_NUMBER";
    public final static String EXTRA_SEND_DIGIT = "EXTRA_SEND_DIGIT";

    public final static int PHONE_STATE_OUTGOING = 0;
    public final static int PHONE_STATE_INCOMING = PHONE_STATE_OUTGOING + 1;
    public final static int PHONE_STATE_IN_CALL = PHONE_STATE_INCOMING + 1;
    public final static int PHONE_STATE_HANG_UP = PHONE_STATE_IN_CALL + 1;
    public final static int PHONE_STATE_DECLINE = PHONE_STATE_HANG_UP + 1;
    public final static int PHONE_STATE_DISCONNECTED = PHONE_STATE_DECLINE + 1;
    public final static int PHONE_STATE_DEFAULT = PHONE_STATE_DISCONNECTED + 1;

    private Device mClientDevice;
    private Device mPartnerDevice;
    private ClientProfile clientProfile;

    private Connection mActiveConnection;
    private Connection mPendingConnection;

    private String mFromPhoneNumber;
    private String mToPhoneNumber;

    private AudioManager mAudioManager;
    private int mSavedAudioMode = AudioManager.MODE_INVALID;

    private final IBinder mBinder = new LocalBinder();

    public static void startServiceWithAction(Context context, String action, String fromPhoneNumber, String toPhoneNumber) {
        Intent intent = new Intent(context, PhoneService.class);
        intent.putExtra(EXTRA_FROM_PHONE_NUMBER, fromPhoneNumber);
        intent.putExtra(EXTRA_TO_PHONE_NUMBER, toPhoneNumber);
        if (action != null) {
            intent.setAction(action);
        }
        context.startService(intent);
    }

    public static void startServiceWithAction(Context context, String action, String fromPhoneNumber, String toPhoneNumber, Device device, Connection connection) {
        Intent intent = new Intent(context, PhoneService.class);
        intent.putExtra(EXTRA_FROM_PHONE_NUMBER, fromPhoneNumber);
        intent.putExtra(EXTRA_TO_PHONE_NUMBER, toPhoneNumber);
        intent.putExtra(Device.EXTRA_DEVICE, device);
        intent.putExtra(Device.EXTRA_CONNECTION, connection);
        if (action != null) {
            intent.setAction(action);
        }
        context.startService(intent);
    }

    public static void startServiceWithAction(Context context, String action, String fromPhoneNumber, String toPhoneNumber, String digit) {
        Intent intent = new Intent(context, PhoneService.class);
        intent.putExtra(EXTRA_FROM_PHONE_NUMBER, fromPhoneNumber);
        intent.putExtra(EXTRA_TO_PHONE_NUMBER, toPhoneNumber);
        intent.putExtra(EXTRA_SEND_DIGIT, digit);
        if (action != null) {
            intent.setAction(action);
        }
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        clientProfile = new ClientProfile(MyPreference.getPhoneNumber(getApplicationContext()), true, true);
        DebugTool.logD("CREATE SERVICE");
        initializeTwilioClientSDK();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setAudioFocus(false);
        mAudioManager.setSpeakerphoneOn(MyPreference.getSpeakerphone(getApplicationContext()));
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_NOT_STICKY;

        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            mFromPhoneNumber = intent.getStringExtra(EXTRA_FROM_PHONE_NUMBER);
            mToPhoneNumber = intent.getStringExtra(EXTRA_TO_PHONE_NUMBER);
            String digit = intent.getStringExtra(EXTRA_SEND_DIGIT);

            if (action.equals(ACTION_OUTGOING))
                processOutgoingRequest(mFromPhoneNumber, mToPhoneNumber);
            else if (action.equals(ACTION_HANG_UP))
                processHangUpRequest(mFromPhoneNumber, mToPhoneNumber);
            else if (action.equals(ACTION_ACCEPT_INCOMING))
                processAcceptIncomingRequest(mFromPhoneNumber, mToPhoneNumber);
            else if (action.equals(ACTION_INCOMING)) processIncomingRequest(intent);
            else if (action.equals(ACTION_DECLINE_INCOMING))
                processDeclineIncomingRequest(mFromPhoneNumber, mToPhoneNumber);
            else if (action.equals(ACTION_SEND_DIGITS)) processSendDigit(digit);
            else if (action.equals(ACTION_MUTE_MICOPHONE)) processMuteMicrophone();
            else if (action.equals(ACTION_SPEAKER_PHONE)) processSpeakerMicrophone();
        } else {
            return START_NOT_STICKY;
        }

        return START_NOT_STICKY; // Means we started the service, but don't want it to
        // restart in case it's killed.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onStartListening(Device device) {
        DebugTool.logD("START LISTENING: " + device.getCapabilities().toString());
        DebugTool.logD("START LISTENING STATE: " + device.getState().name());
    }

    @Override
    public void onStopListening(Device device) {
        DebugTool.logD("STOP LISTENING: " + device.getState().name());
    }

    @Override
    public void onStopListening(Device device, int i, String s) {
        DebugTool.logD("STOP LISTENING2 : " + s + " |||| " + device.getCapabilities().toString());
    }

    @Override
    public boolean receivePresenceEvents(Device device) {
        DebugTool.logD("RECEIVE PRESENCE EVENTS: " + device.getCapabilities().toString());
        return false;
    }

    @Override
    public void onPresenceChanged(Device device, PresenceEvent presenceEvent) {
        DebugTool.logD("PRESENCE CHANGE: " + device.toString());
    }

    @Override
    public void onConnecting(Connection connection) {
        DebugTool.logD("CONNECTING : " + connection.getParameters().toString());
    }

    @Override
    public void onConnected(Connection connection) {
        DebugTool.logD("CONNECTED");
        updateUIPhone(PHONE_STATE_IN_CALL, mFromPhoneNumber, mToPhoneNumber);
//        showInCallFragment();
    }

    @Override
    public void onDisconnected(Connection connection) {
        DebugTool.logD("DISCONNECTED ");
        updateUIPhone(PHONE_STATE_DISCONNECTED, mFromPhoneNumber, mToPhoneNumber);
//        showDialFragment();
        if (connection.getState() == Connection.State.PENDING) {
            DebugTool.logD("PENDING");
        } else if (connection.getState() == Connection.State.CONNECTED) {
            DebugTool.logD("CONNECTED");
        } else if (connection.getState() == Connection.State.CONNECTING) {
            DebugTool.logD("CONNECTING");
        } else if (connection.getState() == Connection.State.DISCONNECTED) {
            DebugTool.logD("DISCONNECTED");
        }

        if (mPartnerDevice != null) {
            if (mPartnerDevice.getState() == Device.State.BUSY) {
                DebugTool.logD("BUSY");
            } else if (mPartnerDevice.getState() == Device.State.OFFLINE) {
                DebugTool.logD("OFFLINE");
            }
            DebugTool.logD("DIVECE: "  + mPartnerDevice.getState().name());
        }
    }

    @Override
    public void onDisconnected(Connection connection, int i, String s) {
        if (connection == mPendingConnection) {
            mPendingConnection = null;
//            alertDialog.dismiss();
            DebugTool.logD("Disconnect pending");
        } else if (mActiveConnection != null && connection != null) {
            if (mActiveConnection == connection) {
                mActiveConnection = null;
                updateUIPhone(PHONE_STATE_DISCONNECTED, mFromPhoneNumber, mToPhoneNumber);
            }
            DebugTool.logD("Disconnect");
        }
    }

    private void initializeTwilioClientSDK() {
        if (!Twilio.isInitialized()) {
            Twilio.initialize(getApplicationContext(), new Twilio.InitListener() {
                @Override
                public void onInitialized() {
                    retrieveCapabilityToken(clientProfile);
                }

                @Override
                public void onError(Exception e) {
                    DebugTool.logD("Failed to initialize the Twilio Client SDK");
//                    Toast.makeText(getContext(), "Failed to initialize the Twilio Client SDK", Toast.LENGTH_LONG).show();
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
//        if (newClientProfile.isAllowOutgoing()) {
//            b.appendQueryParameter("allowOutgoing", newClientProfile.isAllowOutgoing() ? "true" : "false");
//        }
        if (newClientProfile.isAllowIncoming() && newClientProfile.getName() != null) {
            b.appendQueryParameter("client", newClientProfile.getName());
        }

        DebugTool.logD("NAME PHONE: " + newClientProfile.getName());
        Ion.with(getContext())
                .load(b.toString())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String capabilityToken) {
                        if (e == null) {
                            clientProfile = newClientProfile;
                            DebugTool.logD("NEW: " + clientProfile.getName());
                            createDevice(capabilityToken);
                        } else {
                            DebugTool.logD("Error retrieving token");
                        }
                    }
                });
    }

    private void createDevice(String capabilityToken) {
        try {
            if (mClientDevice == null) {
                mClientDevice = Twilio.createDevice(capabilityToken, this);
                mClientDevice.setIncomingSoundEnabled(true);

                /*
                 * Providing a PendingIntent to the newly created Device, allowing you to receive incoming calls
                 *
                 *  What you do when you receive the intent depends on the component you set in the Intent.
                 *
                 *  If you're using an Activity, you'll want to override Activity.onNewIntent()
                 *  If you're using a Service, you'll want to override Service.onStartCommand().
                 *  If you're using a BroadcastReceiver, override BroadcastReceiver.onReceive().
                 */

                Intent intent = new Intent(this, PhoneActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mClientDevice.setIncomingIntent(pendingIntent);
            } else {
                mClientDevice.updateCapabilityToken(capabilityToken);
            }

//            EventBus.getDefault().postSticky(mClientDevice);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Device error", Toast.LENGTH_SHORT).show();
        }
    }

    private void processOutgoingRequest(String fromPhoneNumber, String toPhoneNumber) {
        Map<String, String> params = new HashMap<>();
//        toPhoneNumber = "client:" + toPhoneNumber.trim();
        params.put("To", toPhoneNumber);
        if (mClientDevice != null) {
            mActiveConnection = mClientDevice.connect(params, this);
            DebugTool.logD("MAKE A CALL : " + toPhoneNumber);
        } else {
            Toast.makeText(getContext(), "No existing device", Toast.LENGTH_SHORT).show();
        }
        updateUIPhone(PHONE_STATE_OUTGOING, fromPhoneNumber, toPhoneNumber);
    }

    private void processIncomingRequest(Intent intent) {
        Device device = intent.getParcelableExtra(Device.EXTRA_DEVICE);
        Connection incomingConnection = intent.getParcelableExtra(Device.EXTRA_CONNECTION);

        if (incomingConnection == null && device == null) {
            return;
        } else {
            intent.removeExtra(Device.EXTRA_DEVICE);
            intent.removeExtra(Device.EXTRA_CONNECTION);

            mPartnerDevice = device;
            mPendingConnection = incomingConnection;
            mPendingConnection.setConnectionListener(this);
            DebugTool.logD("DEVICE " + device.getCapabilities().toString());
            DebugTool.logD("INTENT ");
        }

    }

    private void processHangUpRequest(String fromPhoneNumber, String toPhoneNumber) {
        if (mActiveConnection != null) {
            mActiveConnection.disconnect();
            mActiveConnection = null;
        }
        updateUIPhone(PHONE_STATE_HANG_UP, fromPhoneNumber, toPhoneNumber);

    }

    private void processAcceptIncomingRequest(String fromPhoneNumber, String toPhoneNumber) {
        if (mActiveConnection != null) {
            mActiveConnection.disconnect();
        }
        mPendingConnection.accept();
        mActiveConnection = mPendingConnection;
        mPendingConnection = null;
        updateUIPhone(PHONE_STATE_INCOMING, fromPhoneNumber, toPhoneNumber);
    }

    private void processDeclineIncomingRequest(String fromPhoneNumber, String toPhoneNumber) {
        if (mPendingConnection != null) {
            mPendingConnection.reject();
        }
        updateUIPhone(PHONE_STATE_DECLINE, fromPhoneNumber, toPhoneNumber);
    }

    private void processSendDigit(String digit) {
        if (mActiveConnection != null && !TextUtils.isEmpty(digit)) {
            mActiveConnection.sendDigits(digit);
        }
    }

    private void processMuteMicrophone() {
        if (mActiveConnection != null) {
            mActiveConnection.setMuted(MyPreference.getMuteMicrophone(getApplicationContext()));
        }
    }

    private void processSpeakerMicrophone() {
        setAudioFocus(true);
        mAudioManager.setSpeakerphoneOn(MyPreference.getSpeakerphone(getApplicationContext()));
    }

    private void updateUIPhone(int phoneState, String fromPhoneNumber, String toPhoneNumber) {
        Intent intent = new Intent(PhoneService.ACTION_UPDATE_UI);
        intent.putExtra(EXTRA_PHONE_STATE, phoneState);
        intent.putExtra(EXTRA_FROM_PHONE_NUMBER, fromPhoneNumber);
        intent.putExtra(EXTRA_TO_PHONE_NUMBER, toPhoneNumber);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void setAudioFocus(boolean setFocus) {
        if (mAudioManager != null) {
            if (setFocus) {
                mSavedAudioMode = mAudioManager.getMode();
                // Request audio focus before making any device switch.
                mAudioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                /*
                 * Start by setting MODE_IN_COMMUNICATION as default audio mode. It is
                 * required to be in this mode when playout and/or recording starts for
                 * best possible VoIP performance. Some devices have difficulties with speaker mode
                 * if this is not set.
                 */
                mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                mAudioManager.setMode(mSavedAudioMode);
                mAudioManager.abandonAudioFocus(null);
            }
        }
    }

    public static void startPhoneService(Context context) {
        context.stopService(new Intent(context, PhoneService.class));

        Intent intent = new Intent(context, PhoneIntervalReceiver.class);
        intent.setAction(ACTION_WAKEUP);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        DebugTool.logD("ALARM START");
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarm.set(AlarmManager.RTC_WAKEUP, 0, pIntent);
        } else {
            alarm.setExact(AlarmManager.RTC_WAKEUP, 0, pIntent);
        }

    }

    public class LocalBinder extends Binder {
        public PhoneService getService() {
            return PhoneService.this;
        }
    }
}
