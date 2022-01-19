package com.inland.pilot.Location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

public class LocationAlarmScheduler extends Service {
    private PendingIntent mAlarmSender;
    private AlarmManager am;

    @Override
    public void onCreate() {
        super.onCreate();
        if(check_flag()) {
            Intent intent_alarm = new Intent(this, LocationUpdateService.class);
            mAlarmSender = PendingIntent.getService(this, 0, intent_alarm, 0);
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC, 0, 60 * 1000, mAlarmSender);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(check_flag()) {
            Intent intent_alarm = new Intent(this, LocationUpdateService.class);
            mAlarmSender = PendingIntent.getService(this, 0, intent_alarm, 0);
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC, 0, 60 * 1000, mAlarmSender);
        }
        return START_STICKY;
    }

    public boolean check_flag()
    {
        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(this);
        boolean reaching_to_start = preferences_shared.getBoolean("reaching_to_start", false);
        boolean reaching_to_final = preferences_shared.getBoolean("reaching_to_final", false);
        if(reaching_to_start ==true || reaching_to_final ==true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}