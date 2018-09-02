package com.example.snehalkore.alarmmanager.views.activity;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.example.snehalkore.alarmmanager.views.fragments.MathGameFragment;
import com.example.snehalkore.alarmmanager.R;
import com.example.snehalkore.alarmmanager.views.fragments.SetAlarmFragment;

public class MainActivity extends AppCompatActivity {
    public static boolean closeApp;
    private Window wind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction().add(R.id.tabcontent, SetAlarmFragment.newInstance(), SetAlarmFragment.class.getName()).commit();
        String type = getIntent().getStringExtra("From");
        if (type != null) {
            switch (type) {
                case "notifyFrag":
                    getFragmentManager().beginTransaction().replace(R.id.tabcontent, MathGameFragment.newInstance(), MathGameFragment.class.getName()).commit();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        restartGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        restartGame();
    }

    private void restartGame() {
        if (!closeApp) {
            Fragment myFragment = getFragmentManager().findFragmentByTag(MathGameFragment.class.getName());
            if (myFragment != null && myFragment.isVisible()) {
                Intent i1 = new Intent();
                i1.setClassName("com.example.snehalkore.alarmmanager", "com.example.snehalkore.alarmmanager.views.activity.MainActivity");
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i1.putExtra("From", "notifyFrag");
                startActivity(i1);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

}
