package mitso.volodymyr.tryintentservice.support;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mitso.volodymyr.tryintentservice.R;
import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.database.DatabaseHelper;
import mitso.volodymyr.tryintentservice.models.Organization;
import mitso.volodymyr.tryintentservice.service.AlarmReceiver;

public class Support {

    public void scheduleAlarm(Context _context, int _triggerAfter) {

        final Intent alarmIntent = new Intent(_context, AlarmReceiver.class);
        final PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(_context, Constants.ALARM_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + _triggerAfter, AlarmManager.INTERVAL_HOUR, alarmPendingIntent);
    }

    public boolean checkNetworkConnection(Context _context) {

        final ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo networkInfoMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return ((networkInfoWifi != null && networkInfoWifi.isConnected()) || (networkInfoMobile != null && networkInfoMobile.isConnected()));
    }

    public boolean checkDatabaseExistence(Context _context) {

        return _context.getDatabasePath(DatabaseHelper.DATABASE_NAME).exists();
    }

    public boolean checkDatesEquality(List<Organization> _dbOrganizationList, List<Organization> _apiOrganizationList) {

        if (_dbOrganizationList == null || _dbOrganizationList.isEmpty())
            return false;

        if (_apiOrganizationList == null || _apiOrganizationList.isEmpty())
            return false;

        Date latestDbDate = _dbOrganizationList.get(0).getDate();

        for (int i = 0; i < _dbOrganizationList.size(); i++) {

            final Organization organization = _dbOrganizationList.get(i);

            if (organization.getDate().after(latestDbDate))
                latestDbDate = organization.getDate();
        }

        final Date apiDate = _apiOrganizationList.get(0).getDate();

        return latestDbDate.equals(apiDate);
    }

    public List<Organization> combineOrganizations(List<Organization> _dbOrganizationList, List<Organization> _apiOrganizationList) {

        if (_dbOrganizationList == null || _dbOrganizationList.isEmpty())
            return new ArrayList<>(_apiOrganizationList);

        if (_apiOrganizationList == null || _apiOrganizationList.isEmpty())
            return new ArrayList<>(_dbOrganizationList);

        final List<Organization> combinedOrganizationList = new ArrayList<>();
        combinedOrganizationList.addAll(_dbOrganizationList);
        combinedOrganizationList.addAll(_apiOrganizationList);

        final List<Organization> removeOrganizationList = new ArrayList<>();

        for (int i = 0; i < combinedOrganizationList.size(); i++) {

            for (int j = i + 1; j < combinedOrganizationList.size(); j++) {

                final Organization organizationI = combinedOrganizationList.get(i);
                final Organization organizationJ = combinedOrganizationList.get(j);

                if (organizationI.getId().equals(organizationJ.getId())) {

                    organizationJ.setDatabaseId(organizationI.getDatabaseId());

                    removeOrganizationList.add(organizationI);
                }
            }
        }

        combinedOrganizationList.removeAll(removeOrganizationList);

        Collections.sort(combinedOrganizationList);

        return combinedOrganizationList;
    }

    public List<Organization> deleteNullFieldsObjects(List<Organization> _organizationList) {

        final List<Organization> checkedOrganizationList = new ArrayList<>(_organizationList);

        for (int i = 0; i < checkedOrganizationList.size(); i++) {

            final Organization organization = checkedOrganizationList.get(i);

            if (organization.getId() == null
                    || organization.getName() == null
                    || organization.getType() == null
                    || organization.getRegion() == null
                    || organization.getCity() == null
                    || organization.getAddress() == null
                    || organization.getPhone() == null
                    || organization.getLink() == null
                    || organization.getCurrencies() == null
                    || organization.getDate() == null)

                checkedOrganizationList.remove(organization);
        }

        return checkedOrganizationList;
    }

    public List<Organization> sortList(List<Organization> _organizationList) {

        final List<Organization> sortedList = new ArrayList<>(_organizationList);
        Collections.sort(sortedList);

        return sortedList;
    }

    public void showProgressDialog(ProgressDialog _progressDialog) {

        if (_progressDialog != null) {

            _progressDialog.setCancelable(false);
            _progressDialog.show();
            _progressDialog.setContentView(R.layout.dialog_progress);
        }
    }

    public void dismissProgressDialog(ProgressDialog _progressDialog) {

        if (_progressDialog != null && _progressDialog.isShowing())
            _progressDialog.dismiss();
    }

    public void showToastFirstRun(final Context _context) {

        Toast.makeText(_context, R.string.first_run, Toast.LENGTH_LONG).show();
    }
}
