package com.inland.pilot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inland.pilot.Location.LocationAlarmScheduler;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Broadcast Listened", "Service tried to stop");
        //Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      //      context.startForegroundService(new Intent(context, LocationAlarmScheduler.class));
      //  } else {
            context.startService(new Intent(context, LocationAlarmScheduler.class));
      //  }
    }
}