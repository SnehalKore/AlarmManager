package com.example.snehalkore.alarmmanager.views.fragments;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.snehalkore.alarmmanager.R;
import com.example.snehalkore.alarmmanager.receiver.AlarmReceiver;

import java.util.Calendar;

public class SetAlarmFragment extends Fragment {
    View view;
    Button setAlarmButton;
    Button stopAlarmButton;
    TimePickerDialog timePickerDialog;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;


    public static SetAlarmFragment newInstance() {
        return new SetAlarmFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_set_alarm, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        setAlarmButton = view.findViewById(R.id.set_alarm);
        stopAlarmButton = view.findViewById(R.id.stop_alarm);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePickerDialog();
            }
        });
        stopAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().add(R.id.tabcontent, MathGameFragment.newInstance(), MathGameFragment.class.getName()).commit();
            }
        });
    }

    private void openTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(
                getActivity(),
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if (calSet.compareTo(calNow) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);
            Toast.makeText(getActivity(), "Alarm is set for" + calSet.getTime().toString(), Toast.LENGTH_LONG).show();
        }
    };

    private void setAlarm(Calendar targetCal) {
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_ALARM_RECEIVER);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 1001, intent, 0);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), 0, pendingIntent);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
