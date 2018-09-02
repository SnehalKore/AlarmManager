package com.example.snehalkore.alarmmanager.views.fragments;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snehalkore.alarmmanager.R;
import com.example.snehalkore.alarmmanager.receiver.AlarmReceiver;
import com.example.snehalkore.alarmmanager.service.RingtoneService;
import com.example.snehalkore.alarmmanager.views.activity.MainActivity;

import java.util.Random;


public class MathGameFragment extends Fragment {

    View view;
    TextView title;
    TextView value1;
    TextView value2;
    EditText answer;
    Button submitButton;
    int score = 0;
    int noOfQuestns = 1;
    int ans = 0;

    public static MathGameFragment newInstance() {
        return new MathGameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_math_game, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        if (!isMyServiceRunning(RingtoneService.class)) {
            Intent intentService = new Intent(getActivity(), RingtoneService.class);
            getActivity().startService(intentService);
        }
        value1 = view.findViewById(R.id.value_1);
        value2 = view.findViewById(R.id.value_2);
        answer = view.findViewById(R.id.answer);
        title = view.findViewById(R.id.title);
        submitButton = view.findViewById(R.id.submit_button);
        setQuestion();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(answer.getText().toString())) {
                    if (ans == Integer.valueOf(answer.getText().toString())) {
                        score++;
                    }
                    setQuestion();
                } else {
                    answer.setError("Enter your answer");
                }
            }
        });
    }

    private void setQuestion() {
        if (noOfQuestns <= 5) {

            title.setText(getActivity().getResources().getString(R.string.question) + noOfQuestns);
            noOfQuestns++;
            Random r = new Random();
            int randomNumber = r.nextInt(10);
            int randomNumber2 = r.nextInt(10);
            value1.setText(String.valueOf(randomNumber));
            value2.setText(String.valueOf(randomNumber2));
            answer.setText("");
            ans = Integer.valueOf(value1.getText().toString()) + Integer.valueOf(value2.getText().toString());

        } else {

            Toast.makeText(getActivity(), "Your score is " + score + "out of 5!!! \n Good Morning!!! Have a great day!!!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            intent.setAction(AlarmReceiver.ACTION_ALARM_RECEIVER);
            boolean isWorking = (PendingIntent.getBroadcast(getActivity(), 1001, intent, 0) != null);
            if (isWorking) {
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1001, intent, 0);
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }
                /*NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.cancel(12345);
                }*/
                MainActivity.closeApp = true;
                Intent intentService = new Intent(getActivity(), RingtoneService.class);
                getActivity().stopService(intentService);

                getActivity().finish();

                System.exit(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().finishAndRemoveTask();
                }

            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
