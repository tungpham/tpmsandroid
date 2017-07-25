package com.ethan.morephone.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.android.morephone.data.log.DebugTool;
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
    private static final String NOTIFY_TITLE_DATA_KEY = "twi_title";
    private static final String NOTIFY_BODY_DATA_KEY = "twi_body";

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
        Map<String, String> data = message.getData();
        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();

        DebugTool.logD("From: " + from);
        DebugTool.logD("Body: " + body);
        DebugTool.logD("DATA: " + data.toString());
        DebugTool.logD("TO: " + message.getTo());
        DebugTool.logD("BODY: " + message.getNotification().getBody());
        DebugTool.logD("TITLE: " + message.getNotification().getTitle());


        sendNotification(title, body);
    }

    /**
     * Create and show a simple notification containing the GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
