package com.hci.project.cerebro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private RemoteMessage.Notification notification;
    public MyFirebaseMessagingService() {
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
               // scheduleJob();
            } else {
                // Handle message within 10 seconds
               // handleNow();
            }

        }
//        System.out.println("Message Object :" + remoteMessage.getData().get("question"));
        createNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("question_id"));
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    private void createNotification( String messageBody, String obj) {
        int quesid = Integer.parseInt(obj);
        Intent intent = new Intent( this , MessageFromLearner.class );
        Bundle bundle = new Bundle();
        //bundle.putString("Question", quesid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("PushNotification","Y");
        intent.putExtra("question_id",quesid);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder mNotificationBuilder = new Notification.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New Request from Learner")
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 001;
        notificationManager.notify(mNotificationId, mNotificationBuilder.build());
    }

}
