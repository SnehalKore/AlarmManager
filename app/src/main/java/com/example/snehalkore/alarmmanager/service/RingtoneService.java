package com.example.snehalkore.alarmmanager.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;


public class RingtoneService extends Service {
    MediaPlayer mp;
    Vibrator vibrator;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Uri currentRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext().getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        MediaPlayer mp = MediaPlayer.create(getBaseContext(), currentRintoneUri);
        Vibrator vibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0, 18000};

        if (vibrator != null) {
            vibrator.vibrate(pattern, 0);
        }

        mp.start();
        mp.setLooping(true);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp.isLooping()) {
                    mp.seekTo(0);
                }
            }
        });


        return Service.START_STICKY;

    }


}
