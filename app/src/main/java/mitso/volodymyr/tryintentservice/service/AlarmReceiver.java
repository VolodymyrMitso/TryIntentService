package mitso.volodymyr.tryintentservice.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context _context, Intent _intent) {

        final Intent serviceIntent = new Intent(_context, CustomService.class);
        startWakefulService(_context, serviceIntent);
    }
}