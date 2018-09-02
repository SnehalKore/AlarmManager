package com.example.snehalkore.alarmmanager.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class PersistentService extends Service {

    private static final int INTERVAL = 3000; // poll every 3 secs

    private static boolean stopTask;
    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onCreate() {
        super.onCreate();

        stopTask = false;

        // Optional: Screen Always On Mode!
        // Screen will never switch off this way
        mWakeLock = null;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "a_tag");
        }
        mWakeLock.acquire();


        // Start your (polling) task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // If you wish to stop the task/polling
                if (stopTask) {
                    this.cancel();
                }

                // The first in the list of RunningTasks is always the foreground task.
                ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                ActivityManager.RunningTaskInfo foregroundTaskInfo = null;
                if (am != null) {
                    foregroundTaskInfo = am.getRunningTasks(1).get(0);
                }
                String foregroundTaskPackageName = null;
                if (foregroundTaskInfo != null) {
                    foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
                    // Check foreground app: If it is not in the foreground... bring it!
                    if (!foregroundTaskPackageName.equals("com.example.snehalkore.alarmmanager")) {
                        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.example.snehalkore.alarmmanager.views.activity.MainActivity");
                        if (LaunchIntent != null) {
                            LaunchIntent.putExtra("From", "notifyFrag");
                        }
                        startActivity(LaunchIntent);
                    }
                }

            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, INTERVAL);
    }

    @Override
    public void onDestroy() {
        stopTask = true;
        if (mWakeLock != null)
            mWakeLock.release();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

