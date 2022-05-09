package com.example.protego;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.example.protego.web.schemas.Patient;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context, PatientMedicationActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(PatientMedicationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent( 0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);//building the notification

        Notification notification = builder.setContentTitle("Medication Reminder")
                .setContentText("Remember to take your medication today!")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("Medication Reminder");
        }

        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent PI = PendingIntent.getBroadcast(context, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        if (PatientMedicationActivity.am != null) {
            PatientMedicationActivity.am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 86400000, PI);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //below creating notification channel, because of androids latest update, O is Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "Medication Reminder",
                    "NotificationDemo",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);

/*
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context, "Medication Reminder").setSmallIcon(R.drawable.ic_illustration)
                .setContentTitle("Alarm Fired")
                .setContentText("Events to be Performed").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(id, mNotifyBuilder.build());
        id++;
*/
    }
}