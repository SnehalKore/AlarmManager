package com.example.snehalkore.alarmmanager.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.snehalkore.alarmmanager.service.RingtoneService;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final String ACTION_ALARM_RECEIVER = "Alarm";

    @Override
    public void onReceive(Context context, Intent intent) {

        /*NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                //example for large icon
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Alarm")
                .setContentText("Wake Up!!!!!!!")
                .setOngoing(true)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("From", "notifyFrag");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        if (manager != null) {
            manager.notify(12345, notification);
        }*/

        Intent intentService = new Intent(context.getApplicationContext(), RingtoneService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }

        Intent i1 = new Intent();
        i1.setClassName("com.example.snehalkore.alarmmanager", "com.example.snehalkore.alarmmanager.views.activity.MainActivity");
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i1.putExtra("From", "notifyFrag");
        context.startActivity(i1);
    }
}

