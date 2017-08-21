package com.ethan.morephone.presentation.phone.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.morephone.data.database.DatabaseHelpper;
import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.user.User;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.utils.TwilioManager;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.fcm.NotificationHelpper;
import com.ethan.morephone.presentation.phone.PhoneActivity;
import com.ethan.morephone.presentation.record.SoundPoolManager;
import com.ethan.morephone.token.CapabilityToken;
import com.ethan.morephone.token.TwilioCapability;
import com.ethan.morephone.utils.EnumUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twilio.client.Connection;
import com.twilio.client.ConnectionListener;
import com.twilio.client.Device;
import com.twilio.client.DeviceListener;
import com.twilio.client.PresenceEvent;
import com.twilio.client.Twilio;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ethan on 4/27/17.
 */

public class PhoneService extends Service implements DeviceListener, ConnectionListener {

    //    private static final String TOKEN_SERVICE_URL = "https://numberphone1.herokuapp.com/token";

    private static final long PROGRESS_UPDATE_INTERNAL = 60;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 0;

    public final static String ACTION_WAKEUP = "com.ethan.morephone.action.WAKE_UP";

    public final static String ACTION_REGISTER_PHONE_NUMBER = "com.ethan.morephone.action.ACTION_REGISTER_PHONE_NUMBER";
    public final static String ACTION_UNREGISTER_PHONE_NUMBER = "com.ethan.morephone.action.ACTION_UNREGISTER_PHONE_NUMBER";

    public final static String ACTION_SOUND_RINGING = "com.ethan.morephone.action.ACTION_SOUND_RINGING";
    public final static String ACTION_SOUND_STOP = "com.ethan.morephone.action.ACTION_SOUND_STOP";

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

    private ScheduledExecutorService mExecutorService;
    private ScheduledFuture<?> mScheduleFuture;

    private Device.State mDeviceState = Device.State.OFFLINE;
    private int mPhoneState = PHONE_STATE_DEFAULT;

    private final IBinder mBinder = new LocalBinder();

    private SoundPoolManager soundPoolManager;

    public static void startServiceWithAction(Context context, String action) {
        Intent intent = new Intent(context, PhoneService.class);
        if (action != null) {
            intent.setAction(action);
        }
        context.startService(intent);
    }

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

        soundPoolManager = SoundPoolManager.getInstance(this);

        mExecutorService = Executors.newSingleThreadScheduledExecutor();

        mDevices = new HashMap<>();
//        mClientProfiles = new ArrayList<>();

//        clientProfile = new ClientProfile(MyPreference.getPhoneNumber(getApplicationContext()), true, true);
        DebugTool.logD("CREATE SERVICE");
        scheduleRegisterPhoneNumber();
        registerPhoneNumberAgain();

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

            if (action.equals(ACTION_SOUND_RINGING))
                soundPoolManager.playRinging();
            else if (action.equals(ACTION_SOUND_STOP))
                soundPoolManager.stopRinging();
            else if (action.equals(ACTION_REGISTER_PHONE_NUMBER))
                registerDevicePhoneNumber(mFromPhoneNumber);
            else if (action.equals(ACTION_UNREGISTER_PHONE_NUMBER))
                unRegisterDevicePhoneNumber(mFromPhoneNumber);
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
//        DatabaseHelpper.insert(getApplicationContext(), device.getState().name());
//
//        for (Map.Entry<String, Device> entry : mDevices.entrySet()) {
//            Device value = entry.getValue();
//            if (value == device) {
//                DatabaseHelpper.insert(getApplicationContext(), device.getState().name() + " PHONE " + entry.getKey());
//            }
//        }
        updateDeviceState(device.getState());
//        Map<Device.Capability, Object> capabilityMap = device.getCapabilities();
//        if (capabilityMap != null) {
//            long expire = (long) capabilityMap.get(Device.Capability.EXPIRATION);
//            if (expire - 100 < 0) {
//                for (Map.Entry<String, Device> entry : mDevices.entrySet()) {
//                    Device value = entry.getValue();
//                    if (value == device) {
//                        retrieveCapabilityToken(entry.getKey());
//                    }
//                }
//            }
//        }
        mDeviceState = device.getState();
    }

    @Override
    public void onStopListening(Device device) {
        DebugTool.logD("STOP LISTENING: " + device.getState().name());
//        DatabaseHelpper.insert(getApplicationContext(), device.getState().name());
        updateDeviceState(device.getState());
//
        for (Map.Entry<String, Device> entry : mDevices.entrySet()) {
            Device value = entry.getValue();
            if (value == device) {
                DatabaseHelpper.insert(getApplicationContext(), device.getState().name() + " PHONE " + entry.getKey());
            }
        }

//        if (device.getState() == Device.State.OFFLINE && mDeviceState != Device.State.OFFLINE) {
//            mDeviceState = Device.State.OFFLINE;
//
//            for (Map.Entry<String, Device> entry : mDevices.entrySet()) {
//                Device value = entry.getValue();
//                if (value == device) {
//                    retrieveCapabilityToken(entry.getKey());
//                }
//            }
//        }
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
        DebugTool.logD("PHONE STATE = " + mPhoneState);
        if (mPhoneState == PHONE_STATE_INCOMING) {
            NotificationHelpper.missingNotification(getApplicationContext(), mFromPhoneNumber);
        }

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
            updateUIPhone(PHONE_STATE_DISCONNECTED, mFromPhoneNumber, mToPhoneNumber);
        } else if (mActiveConnection != null && connection != null) {
            if (mActiveConnection == connection) {
                mActiveConnection = null;
                updateUIPhone(PHONE_STATE_DISCONNECTED, mFromPhoneNumber, mToPhoneNumber);
            }
            DebugTool.logD("Disconnect:  " + s + " || " + connection.getParameters().toString() + " STATE " + connection.getState().toString());
        }
    }

    private void scheduleRegisterPhoneNumber() {
        stopDonutProgressUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.schedule(
                    new Runnable() {
                        @Override
                        public void run() {
//                            registerPhoneNumber();
                            updateTokenFcm();
                            DebugTool.logD("PHONE STATE: " + mPhoneState);
                            if (mPhoneState != PHONE_STATE_OUTGOING && mPhoneState != PHONE_STATE_IN_CALL && mPhoneState != PHONE_STATE_INCOMING) {
                                registerPhoneNumberAgain();
                                DatabaseHelpper.insert(getApplicationContext(), "REGISTER DEVICE");
                            }
                        }
                    },
                    PROGRESS_UPDATE_INTERNAL,
                    TimeUnit.SECONDS);
        }
    }

    private void stopDonutProgressUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    private void registerPhoneNumberAgain() {
        final Set<String> phoneNumberUsages = MyPreference.getPhoneNumberUsage(getApplicationContext());

        if (phoneNumberUsages != null) {
            DebugTool.logD("REGISTER PHONE USAGE: " + phoneNumberUsages.size());

            if (Twilio.isInitialized()) {
                DebugTool.logD("TWILIO INITED: AND LOAD PHONE: " + mDevices.size());

                for (String phone : phoneNumberUsages) {
                    retrieveCapabilityToken(phone);
                }

            } else {
                Twilio.initialize(getApplicationContext(), new Twilio.InitListener() {
                    @Override
                    public void onInitialized() {
                        DebugTool.logD("TWILIO INITED SUCCESS " + mDevices.size());
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
    }

    private String getToken(final String phoneNumber) {
        TwilioCapability capability = new TwilioCapability(TwilioManager.getSid(getApplicationContext()), TwilioManager.getAuthCode(getApplicationContext()));
        capability.allowClientOutgoing(TwilioManager.getApplicationSid(getApplicationContext()));
        capability.allowClientIncoming(phoneNumber);

        try {
            return capability.generateToken();

        } catch (CapabilityToken.DomainException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void retrieveCapabilityToken(final String phoneNumber) {
        DebugTool.logD("REGISTER DEVICE NUMER: " + phoneNumber);

        TwilioCapability capability = new TwilioCapability(TwilioManager.getSid(getApplicationContext()), TwilioManager.getAuthCode(getApplicationContext()));
        capability.allowClientOutgoing(TwilioManager.getApplicationSid(getApplicationContext()));
        capability.allowClientIncoming(phoneNumber);

        try {
            String token = capability.generateToken();

            if (!mDevices.containsKey(phoneNumber)) {
                DebugTool.logD("TOKEN BEFORE : " + token);
                Device device = Twilio.createDevice(token, PhoneService.this);
                device.setIncomingSoundEnabled(true);

                Intent intent = new Intent(getApplicationContext(), PhoneActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                device.setIncomingIntent(pendingIntent);

                mDevices.put(phoneNumber, device);
                DebugTool.logD("SIZE DIVICE: " + mDevices.size() + " |||| " + device.getCapabilities().toString());
                DebugTool.logD("TOKEN: " + token);

            } else {
                mDevices.get(phoneNumber).updateCapabilityToken(token);
            }
        } catch (CapabilityToken.DomainException e) {
            e.printStackTrace();
        }


//        ApiMorePhone.createToken(getApplicationContext(), phoneNumber, new Callback<BaseResponse<String>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null && response.body().getStatus() == HTTPStatus.CREATED.getStatusCode()) {
//                        String capabilityToken = response.body().getResponse();
//                        try {
//                            DebugTool.logD("TOKEN: " + capabilityToken);
//                            if (!mDevices.containsKey(phoneNumber)) {
//                                Device device = Twilio.createDevice(capabilityToken, PhoneService.this);
//                                device.setIncomingSoundEnabled(true);
//
//                                Intent intent = new Intent(getContext(), PhoneActivity.class);
//                                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                device.setIncomingIntent(pendingIntent);
//
//                                mDevices.put(phoneNumber, device);
//                                DebugTool.logD("SIZE DIVICE: " + mDevices.size());
//                            } else {
//                                mDevices.get(phoneNumber).updateCapabilityToken(capabilityToken);
//                            }
//
//                        } catch (Exception e1) {
//                            Toast.makeText(getContext(), "Device error", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        DebugTool.logD("ERROR CREATE TOKEN");
//                    }
//                } else {
//                    DebugTool.logD("ERROR CREATE TOKEN");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
//                DebugTool.logD("FAILE CREATE TOKEN");
//            }
//        });

    }

    private void unRegisterDevicePhoneNumber(final String phoneNumber) {
        if (mDevices.containsKey(phoneNumber)) {
            Device device = mDevices.get(phoneNumber);
            device.setDeviceListener(null);
            mDevices.remove(phoneNumber);
        }
    }


    private void registerDevicePhoneNumber(final String phoneNumber) {

        if (mDevices.containsKey(phoneNumber)) {
            return;
        }

        Set<String> savePhoneNumber = MyPreference.getPhoneNumberUsage(getApplicationContext());

        DebugTool.logD("savePhoneNumber SIZE: " + savePhoneNumber.size());

        if (!savePhoneNumber.contains(phoneNumber)) {
            savePhoneNumber.add(phoneNumber);
        }

        DebugTool.logD("savePhoneNumber AFTER SIZE: " + savePhoneNumber.size());

        MyPreference.setPhoneNumberUsage(getApplicationContext(), savePhoneNumber);

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

        if (toPhoneNumber.contains("client:")) {
            toPhoneNumber.replace("client:", "");
        }
        fromPhoneNumber = fromPhoneNumber.trim();
        toPhoneNumber = toPhoneNumber.trim();

        Map<String, String> params = new HashMap<>();
//        toPhoneNumber = "client:" + toPhoneNumber.trim();
        for (Map.Entry<String, Device> entry : mDevices.entrySet()) {
            DebugTool.logD("ALL PHONE : " + entry.getKey());
        }

        params.put("To", toPhoneNumber);
        if (mDevices.containsKey(fromPhoneNumber)) {
//            retrieveCapabilityToken(fromPhoneNumber);
            Device device = mDevices.get(fromPhoneNumber);
            mActiveConnection = mDevices.get(fromPhoneNumber).connect(params, this);
            DebugTool.logD("MAKE A CALL : " + toPhoneNumber + " EXPERI: " + mDevices.get(fromPhoneNumber).getCapabilities().toString());
            if (device.getCapabilities().isEmpty()) {
                device.updateCapabilityToken(getToken(fromPhoneNumber));
                device.setDeviceListener(this);
                Toast.makeText(getApplicationContext(), "device don't allow outgoing", Toast.LENGTH_SHORT).show();
            }
            updateUIPhone(PHONE_STATE_OUTGOING, fromPhoneNumber, toPhoneNumber);
        } else {
            updateUIPhone(PHONE_STATE_DISCONNECTED, fromPhoneNumber, toPhoneNumber);
            Toast.makeText(getApplicationContext(), "No existing device", Toast.LENGTH_SHORT).show();
        }


    }

    private void processIncomingRequest(Intent intent) {

        mPhoneState = PHONE_STATE_INCOMING;

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
        mPhoneState = phoneState;
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

        Intent intent = new Intent(context, PhoneService.class);
        context.startService(intent);

//        Intent intent = new Intent(context, PhoneIntervalReceiver.class);
//        intent.setAction(ACTION_WAKEUP);
//        PendingIntent pIntent = PendingIntent.getBroadcast(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        DebugTool.logD("ALARM START");
//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            alarm.set(AlarmManager.RTC_WAKEUP, 1000, pIntent);
//        } else {
//            alarm.setExact(AlarmManager.RTC_WAKEUP, 1000, pIntent);
//        }

    }

    private void updateTokenFcm() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        DebugTool.logD("Refreshed token: " + refreshedToken);

        if (!TextUtils.isEmpty(MyPreference.getUserId(getApplicationContext()))) {
            ApiMorePhone.updateFcmToken(getApplicationContext(), MyPreference.getUserId(getApplicationContext()), refreshedToken, new Callback<BaseResponse<User>>() {
                @Override
                public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            DebugTool.logD("TOKEN UPDATE SUCCESS");
                        } else {
                            DebugTool.logD("TOKEN UPDATE ERROR");
                        }
                    } else {
                        DebugTool.logD("TOKEN UPDATE ERROR");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                    DebugTool.logD("TOKEN UPDATE ERROR");
                }
            });
        } else {
            DebugTool.logD("USER NOT REGISTER");
        }
    }

    public static boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (PhoneService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPoolManager.release();
        mExecutorService.shutdown();
        stopDonutProgressUpdate();
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
