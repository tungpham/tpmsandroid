package com.ethan.morephone.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.HTTPStatus;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class NotifyFirebaseMessagingService extends FirebaseMessagingService {

    /*
     * The Twilio Notify message data keys are as follows:
     *  "twi_body"   // The body of the message
     *  "twi_title"  // The title of the message
     *
     * You can find a more detailed description of all supported fields here:
     * https://www.twilio.com/docs/api/notifications/rest/notifications#generic-payload-parameters
     */

    public final static String ACTION_UPDATE_MESSAGE = "com.ethan.morephone.action.ACTION_UPDATE_MESSAGE";

    public final static String EXTRA_FROM_PHONE_NUMBER = "EXTRA_FROM_PHONE_NUMBER";
    public final static String EXTRA_TO_PHONE_NUMBER = "EXTRA_TO_PHONE_NUMBER";
    public final static String EXTRA_MESSAGE_BODY = "EXTRA_MESSAGE_BODY";

    private static final String NOTIFY_TITLE_DATA_KEY = "twi_title";
    private static final String NOTIFY_BODY_DATA_KEY = "twi_body";

    private static final String TAG = "VoiceFCMService";
    private static final String NOTIFICATION_ID_KEY = "NOTIFICATION_ID";
    private static final String CALL_SID_KEY = "CALL_SID";

    public static final int NOTIFY_MESSAGE_ID = 100;

    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * Called when message is received.
     *
     * @param message The remote message, containing from, and message data as key/value pairs.
     */
    @Override
    public void onMessageReceived(RemoteMessage message) {
        /*
         * The Notify service adds the message body to the remote message data so that we can
         * show a simple notification.
         */
        DebugTool.logD("MSG: " + message.toString());
        String from = message.getFrom();


        DebugTool.logD("From: " + from);
        DebugTool.logD("TO: " + message.getTo());
        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            DebugTool.logD("TITLE: " + message.getNotification().getTitle());
            DebugTool.logD("BODY: " + message.getNotification().getBody());
            if (!TextUtils.isEmpty(title) && title.equals(HTTPStatus.MONEY.getReasonPhrase())) {
                NotificationHelpper.moneyNotification(getApplicationContext());
            } else {
                sendSmsNotification(title, message.getNotification().getBody());
            }
        } else {

            DebugTool.logD("NOTIFICATION NULL");

            Map<String, String> dataTest = message.getData();
            if (dataTest != null) {
//                String title = dataTest.get(NOTIFY_TITLE_DATA_KEY);
//                String body = dataTest.get(NOTIFY_BODY_DATA_KEY);
                DebugTool.logD("DATA RESULT: " + dataTest.toString());

            }
        }


//        if (message.getData().size() > 0) {
//            Map<String, String> data = message.getData();
//            DebugTool.logD("DATA: " + data.toString());
//            final int notificationId = (int) System.currentTimeMillis();
//            Voice.handleMessage(this, data, new MessageListener() {
//                @Override
//                public void onCallInvite(CallInvite callInvite) {
//                    NotifyFirebaseMessagingService.this.notify(callInvite, notificationId);
//                    NotifyFirebaseMessagingService.this.sendCallInviteToActivity(callInvite, notificationId);
//                }
//
//                @Override
//                public void onError(MessageException messageException) {
//                    Log.e(TAG, messageException.getLocalizedMessage());
//                }
//            });
//        } else {
//            if(message.getNotification() != null) {
//                String title = message.getNotification().getTitle();
//                String body = message.getNotification().getBody();
//                sendSmsNotification(title, body);
//            }
//        }
    }

    private void moneyNotification(Context context){

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_money_title))
                .setContentText(getString(R.string.notification_money_description))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFY_MESSAGE_ID, notificationBuilder.build());
    }

    /**
     * Create and show a simple notification containing the GCM message.
     *
     * @param message GCM message received.
     */
    private void sendSmsNotification(String title, String message) {
//        if (!LifecycleHandler.isApplicationVisible()) {
//            Intent popupIntent = new Intent(getApplicationContext(), MessageReplyActivity.class);
//            popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            if (!TextUtils.isEmpty(title) && title.contains("-")) {
//                String str[] = title.split("-");
//                if (str != null && str.length == 2) {
//                    popupIntent.putExtra(MessageReplyActivity.EXTRA_PHONE_NUMBER_FROM, str[0]);
//                    popupIntent.putExtra(MessageReplyActivity.EXTRA_PHONE_NUMBER_TO, str[1]);
//                }
//            }
//
//
//            popupIntent.putExtra(MessageReplyActivity.EXTRA_MESSAGE_BODY, message);
//            startActivity(popupIntent);
//        } else {


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if (!TextUtils.isEmpty(title) && title.contains("-")) {
            String str[] = title.split("-");
            if (str != null && str.length == 2) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(str[0])
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(NOTIFY_MESSAGE_ID, notificationBuilder.build());

                updateMessage(str[0], str[1], message);
            }

        }


//        }


    }

    private void updateMessage(String fromPhoneNumber, String toPhoneNumber, String body) {
        Intent intent = new Intent(ACTION_UPDATE_MESSAGE);
        intent.putExtra(EXTRA_FROM_PHONE_NUMBER, fromPhoneNumber);
        intent.putExtra(EXTRA_TO_PHONE_NUMBER, toPhoneNumber);
        intent.putExtra(EXTRA_MESSAGE_BODY, body);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


//    private final void notify(CallInvite callInvite, int notificationId) {
//        String callSid = callInvite.getCallSid();
//
//        if (callInvite.getState() == CallInvite.State.PENDING) {
//            Intent intent = new Intent(this, TestVoiceActivity.class);
//            intent.setAction(TestVoiceActivity.ACTION_INCOMING_CALL);
//            intent.putExtra(TestVoiceActivity.INCOMING_CALL_NOTIFICATION_ID, notificationId);
//            intent.putExtra(TestVoiceActivity.INCOMING_CALL_INVITE, callInvite);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            PendingIntent pendingIntent =
//                    PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
//            /*
//             * Pass the notification id and call sid to use as an identifier to cancel the
//             * notification later
//             */
//            Bundle extras = new Bundle();
//            extras.putInt(NOTIFICATION_ID_KEY, notificationId);
//            extras.putString(CALL_SID_KEY, callSid);
//
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentTitle(getString(R.string.app_name))
//                            .setContentText(callInvite.getFrom() + " is calling.")
//                            .setAutoCancel(true)
//                            .setExtras(extras)
//                            .setContentIntent(pendingIntent)
//                            .setGroup("test_app_notification")
//                            .setColor(Color.rgb(214, 10, 37));
//
//            notificationManager.notify(notificationId, notificationBuilder.build());
//        } else {
//            SoundPoolManager.getInstance(this).stopRinging();
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                /*
//                 * If the incoming call was cancelled then remove the notification by matching
//                 * it with the call sid from the list of notifications in the notification drawer.
//                 */
//                StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
//                for (StatusBarNotification statusBarNotification : activeNotifications) {
//                    Notification notification = statusBarNotification.getNotification();
//                    Bundle extras = notification.extras;
//                    String notificationCallSid = extras.getString(CALL_SID_KEY);
//
//                    if (callSid.equals(notificationCallSid)) {
//                        notificationManager.cancel(extras.getInt(NOTIFICATION_ID_KEY));
//                    } else {
//                        sendCallInviteToActivity(callInvite, notificationId);
//                    }
//                }
//            } else {
//                /*
//                 * Prior to Android M the notification manager did not provide a list of
//                 * active notifications so we lazily clear all the notifications when
//                 * receiving a cancelled call.
//                 *
//                 * In order to properly cancel a notification using
//                 * NotificationManager.cancel(notificationId) we should store the call sid &
//                 * notification id of any incoming calls using shared preferences or some other form
//                 * of persistent storage.
//                 */
//                notificationManager.cancelAll();
//            }
//        }
//    }
//
//    /*
//     * Send the CallInvite to the VoiceActivity
//     */
//    private void sendCallInviteToActivity(CallInvite callInvite, int notificationId) {
//        Intent intent = new Intent(TestVoiceActivity.ACTION_INCOMING_CALL);
//        intent.putExtra(TestVoiceActivity.INCOMING_CALL_NOTIFICATION_ID, notificationId);
//        intent.putExtra(TestVoiceActivity.INCOMING_CALL_INVITE, callInvite);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }
}
