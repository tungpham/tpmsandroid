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
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.morephone.data.BaseUrl;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.presentation.phone.PhoneActivity;
import com.ethan.morephone.utils.EnumUtil;
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
import java.util.Set;

import static com.twilio.client.impl.TwilioImpl.getContext;

/**
 * Created by Ethan on 4/27/17.
 */

public class PhoneService extends Service implements DeviceListener, ConnectionListener {

    //    private static final String TOKEN_SERVICE_URL = "https://numberphone1.herokuapp.com/token";
    private static final String TOKEN_SERVICE_URL = BaseUrl.BASE_URL + "call/token";

    public final static String ACTION_WAKEUP = "com.ethan.morephone.action.WAKE_UP";

    public final static String ACTION_REGISTER_PHONE_NUMBER = "com.ethan.morephone.action.ACTION_REGISTER_PHONE_NUMBER";

    public final static String ACTION_OUTGOING = "com.ethan.morephone.action.OUTGOING";
    public final static String ACTION_INCOMING = "com.ethan.morephone.action.INCOMING";
    public final static String ACTION_HANG_UP = "com.ethan.morephone.action.HANG_UP";
    public final static String ACTION_ACCEPT_INCOMING = "com.ethan.morephone.action.ACCEPT_INCOMING";
    public final static String ACTION_DECLINE_INCOMING = "com.ethan.morephone.action.DECLINE_INCOMING";
    public final static String ACTION_SEND_DIGITS = "com.ethan.morephone.action.SEND_DIGITS";
    public final static String ACTION_MUTE_MICROPHONE = "com.ethan.morephone.action.MUTE_MICROPHONE";
    public final static String ACTION_SPEAKER_PHONE = "com.ethan.morephone.action.SPEAKER_PHONE";
    public final static String ACTION_UPDATE_UI = "com.ethan.morephone.action.ACTION_UPDATE_UI";
    public final static String ACTION_UPDATE_DEVICE_STATE = "com.ethan.morephone.action.ACTION_UPDATE_DEVICE_STATE";
    public final static String ACTION_UPDATE_DEVICE_PARTNER_STATE = "com.ethan.morephone.action.ACTION_UPDATE_DEVICE_PARTNER_STATE";

    public final static String EXTRA_DEVICE_STATE = "EXTRA_DEVICE_STATE";

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

    //    private Device mClientDevice;
    private Device mPartnerDevice;
//    private ClientProfile clientProfile;

    private Map<String, Device> mDevices;
//    private List<ClientProfile> mClientProfiles;

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

        mDevices = new HashMap<>();
//        mClientProfiles = new ArrayList<>();

//        clientProfile = new ClientProfile(MyPreference.getPhoneNumber(getApplicationContext()), true, true);
        DebugTool.logD("CREATE SERVICE");


        final Set<String> phoneNumberUsages = MyPreference.getPhoneNumberUsage(getApplicationContext());
        if (phoneNumberUsages != null) {

            if (Twilio.isInitialized()) {
                for (String phone : phoneNumberUsages) {
                    retrieveCapabilityToken(phone);
                }

            } else {
                Twilio.initialize(getApplicationContext(), new Twilio.InitListener() {
                    @Override
                    public void onInitialized() {
                        for (String phone : phoneNumberUsages) {
                            retrieveCapabilityToken(phone);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        DebugTool.logD("Failed to initialize the Twilio Client SDK");
                    }
                });

                DebugTool.logD("TWILIO DO NOT INIT");
            }


        }

//        initializeTwilioClientSDK();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setAudioFocus(false);
        mAudioManager.setSpeakerphoneOn(MyPreference.getSpeakerphone(getApplicationContext()));
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY;

        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            mFromPhoneNumber = intent.getStringExtra(EXTRA_FROM_PHONE_NUMBER);
            mToPhoneNumber = intent.getStringExtra(EXTRA_TO_PHONE_NUMBER);
            String digit = intent.getStringExtra(EXTRA_SEND_DIGIT);

            if (action.equals(ACTION_REGISTER_PHONE_NUMBER))
                registerDevicePhoneNumber(mFromPhoneNumber);
            else if (action.equals(ACTION_OUTGOING))
                processOutgoingRequest(mFromPhoneNumber, mToPhoneNumber);
            else if (action.equals(ACTION_HANG_UP))
                processHangUpRequest(mFromPhoneNumber, mToPhoneNumber);
            else if (action.equals(ACTION_ACCEPT_INCOMING))
                processAcceptIncomingRequest(mFromPhoneNumber, mToPhoneNumber);
            else if (action.equals(ACTION_INCOMING)) processIncomingRequest(intent);
            else if (action.equals(ACTION_DECLINE_INCOMING))
                processDeclineIncomingRequest(mFromPhoneNumber, mToPhoneNumber);
            else if (action.equals(ACTION_SEND_DIGITS)) processSendDigit(digit);
            else if (action.equals(ACTION_MUTE_MICROPHONE)) processMuteMicrophone();
            else if (action.equals(ACTION_SPEAKER_PHONE)) processSpeakerMicrophone();

        } else {
            return START_STICKY;
        }

        return START_STICKY; // Means we started the service, but don't want it to
        // restart in case it's killed.
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
        updateDeviceState(device.getState());
    }

    @Override
    public void onStopListening(Device device) {
        DebugTool.logD("STOP LISTENING: " + device.getState().name());
        updateDeviceState(device.getState());
    }

    @Override
    public void onStopListening(Device device, int i, String s) {
        updateDeviceState(device.getState());
        DebugTool.logD("STOP LISTENING2: " + device.getState().name());
        DebugTool.logD("STOP LISTENING2 : " + s + " |||| " + device.getCapabilities().toString());
    }

    @Override
    public boolean receivePresenceEvents(Device device) {
        DebugTool.logD("RECEIVE PRESENCE EVENTS: " + device.getCapabilities().toString());
        return false;
    }

    @Override
    public void onPresenceChanged(Device device, PresenceEvent presenceEvent) {
        DebugTool.logD("PRESENCE CHANGE: " + device.toString() + "  |||| " + device.getState().name());
    }

    @Override
    public void onConnecting(Connection connection) {
        DebugTool.logD("onConnecting: " + connection.getParameters().toString() + " |||| " + connection.getState().name());
    }

    @Override
    public void onConnected(Connection connection) {
        DebugTool.logD("onConnected: " + connection.getParameters().toString() + " |||| " + connection.getState().name());
        updateUIPhone(PHONE_STATE_IN_CALL, mFromPhoneNumber, mToPhoneNumber);
    }

    @Override
    public void onDisconnected(Connection connection) {
        DebugTool.logD("onDisconnected: " + connection.getParameters().toString() + " |||| " + connection.getState().name());
        updateUIPhone(PHONE_STATE_DISCONNECTED, mFromPhoneNumber, mToPhoneNumber);

        if (mPartnerDevice != null) {
            updateDevicePartnerState(mPartnerDevice.getState());
            DebugTool.logD("mPartnerDevice: " + mPartnerDevice.getState().name());
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
            DebugTool.logD("Disconnect:  " + s + " || " + connection.getParameters().toString() + " STATE " + connection.getState().toString());
        }
    }

    private void retrieveCapabilityToken(final String phoneNumber) {
        DebugTool.logD("REGISTER DEVICE NUMER");
        Uri.Builder b = Uri.parse(TOKEN_SERVICE_URL).buildUpon();
        b.appendQueryParameter("client", phoneNumber);

        Ion.with(getContext())
                .load(b.toString())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String capabilityToken) {
                        if (e == null) {

                            try {
                                if (!mDevices.containsKey(phoneNumber)) {
                                    Device device = Twilio.createDevice(capabilityToken, PhoneService.this);
                                    device.setIncomingSoundEnabled(true);

                                    Intent intent = new Intent(getContext(), PhoneActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    device.setIncomingIntent(pendingIntent);

                                    mDevices.put(phoneNumber, device);
                                    DebugTool.logD("SIZE DIVICE: " + mDevices.size());
                                } else {
                                    mDevices.get(phoneNumber).updateCapabilityToken(capabilityToken);
                                }

                            } catch (Exception e1) {
                                Toast.makeText(getContext(), "Device error", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            DebugTool.logD("Error retrieving token");
                        }
                    }
                });
    }


    private void registerDevicePhoneNumber(final String phoneNumber) {
        if (Twilio.isInitialized()) {
            retrieveCapabilityToken(phoneNumber);
        } else {
            Twilio.initialize(getApplicationContext(), new Twilio.InitListener() {
                @Override
                public void onInitialized() {
                    retrieveCapabilityToken(phoneNumber);
                }

                @Override
                public void onError(Exception e) {
                    DebugTool.logD("Failed to initialize the Twilio Client SDK");
                }
            });

            DebugTool.logD("TWILIO DO NOT INIT");
        }
    }

    private void processOutgoingRequest(String fromPhoneNumber, String toPhoneNumber) {
        Map<String, String> params = new HashMap<>();
//        toPhoneNumber = "client:" + toPhoneNumber.trim();
        params.put("To", toPhoneNumber);
        if (mDevices.containsKey(fromPhoneNumber)) {
            mActiveConnection = mDevices.get(fromPhoneNumber).connect(params, this);
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

    private void updateDeviceState(Device.State deviceState) {
        Intent intent = new Intent(PhoneService.ACTION_UPDATE_DEVICE_STATE);
        EnumUtil.serialize(deviceState).to(intent);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void updateDevicePartnerState(Device.State deviceState) {
        Intent intent = new Intent(PhoneService.ACTION_UPDATE_DEVICE_PARTNER_STATE);
        EnumUtil.serialize(deviceState).to(intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);

    }

    public class LocalBinder extends Binder {
        public PhoneService getService() {
            return PhoneService.this;
        }
    }
}
