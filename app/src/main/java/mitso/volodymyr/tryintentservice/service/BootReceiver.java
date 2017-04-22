package mitso.volodymyr.tryintentservice.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.support.Support;

public class BootReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context _context, Intent _intent) {

        if (_intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

            new Support().scheduleAlarm(_context, Constants.TIME_5_MINUTES);
        }
    }
}