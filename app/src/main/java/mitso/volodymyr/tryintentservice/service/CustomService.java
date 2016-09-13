package mitso.volodymyr.tryintentservice.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mitso.volodymyr.tryintentservice.api.tasks.ApiGetOrganizationsTask;
import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.database.tasks.DbGetOrganizationsTask;
import mitso.volodymyr.tryintentservice.database.tasks.DbSaveOrganizationsTask;
import mitso.volodymyr.tryintentservice.models.Organization;
import mitso.volodymyr.tryintentservice.support.Support;

public class CustomService extends IntentService {

    private final String            LOG_TAG = Constants.CUSTOM_SERVICE_LOG_TAG;

    private Support                 mSupport;

    private List<Organization>      mApiOrganizationList;
    private List<Organization>      mDbOrganizationList;
    private List<Organization>      mCombinedOrganizationList;

    private ResultReceiver          mResultReceiver;
    private Bundle                  mBundle;
    private boolean                 isResultReceiverNull;

    private boolean                 isDatabaseCreated;
    private boolean                 isNetworkOnline;

    public CustomService() {

        super(CustomService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(final Intent _intent) {

        mSupport = new Support();

        initResultReceiver(_intent);

        if (mSupport.checkDatabaseExistence(this)) {

            Log.i(LOG_TAG, "DATABASE IS CREATED.");
            isDatabaseCreated = true;

            if (mSupport.checkNetworkConnection(this)) {

                Log.i(LOG_TAG, "NETWORK IS ONLINE.");
                isNetworkOnline = true;

                // necessarily: api get, db get, check date, unite, db save.
                // if activity is running: send result.

                apiGetOrganizations();

            } else {

                Log.i(LOG_TAG, "NETWORK IS OFFLINE.");
                isNetworkOnline = false;

                // if activity is running: db get, send result.

                if (!isResultReceiverNull)
                    databaseGetOrganizations();
            }

        } else {

            Log.i(LOG_TAG, "DATABASE IS NOT CREATED.");
            isDatabaseCreated = false;

            if (mSupport.checkNetworkConnection(this)) {

                Log.i(LOG_TAG, "NETWORK IS ONLINE.");

                // necessarily: api get, db save.
                // if activity is running: send result.

                apiGetOrganizations();

            } else {

                Log.i(LOG_TAG, "NETWORK IS OFFLINE.");

                // if activity is running: show toast.

                if (!isResultReceiverNull)
                    resultReceiverSendResult(new ArrayList<Organization>());
            }
        }
    }

    private void initResultReceiver(Intent _intent) {

        mResultReceiver = _intent.getParcelableExtra(Constants.SERVICE_RESULT_RECEIVER_BUNDLE_KEY);

        if (mResultReceiver != null) {

            mBundle = new Bundle();
            isResultReceiverNull = false;

        } else
            isResultReceiverNull = true;
    }

    private void resultReceiverSendResult(List<Organization> _organizationList) {

        Log.i(LOG_TAG, "RESULT IS SENT.");

        mBundle.putParcelableArrayList(Constants.RESULT_BUNDLE_KEY, new ArrayList<>(_organizationList));
        mResultReceiver.send(Constants.SUCCESS_RESULT_CODE, mBundle);
    }

    public void apiGetOrganizations() {

        final ApiGetOrganizationsTask apiGetOrganizationsTask = new ApiGetOrganizationsTask();
        final List<Organization> result = apiGetOrganizationsTask.doInBackground();

        if (result != null && !result.isEmpty()) {

            Log.i(apiGetOrganizationsTask.LOG_TAG, String.valueOf(result.size()) + ".");

            mApiOrganizationList = mSupport.deleteNullPropertiesObjects(result);
            Log.i(LOG_TAG, "API LIST SIZE: " + String.valueOf(mApiOrganizationList.size()) + ".");

        } else {

            Log.e(LOG_TAG, "API LIST IS NULL OR EMPTY.");
        }

        // TODO: handle null or empty api list scenario.

        if (isDatabaseCreated) {

            databaseGetOrganizations();

        } else {

            databaseSaveOrganizations(mApiOrganizationList);
        }
    }

    private void databaseGetOrganizations() {

        final DbGetOrganizationsTask dbGetOrganizationsTask = new DbGetOrganizationsTask(CustomService.this);
        final List<Organization> result = dbGetOrganizationsTask.doInBackground();

        if (result != null && !result.isEmpty()) {

            Log.i(dbGetOrganizationsTask.LOG_TAG, String.valueOf(result.size()) + ".");

            mDbOrganizationList = mSupport.sortList(result);
            Log.i(LOG_TAG, "DB LIST SIZE: " + String.valueOf(mDbOrganizationList.size()) + ".");

        } else {

            Log.e(LOG_TAG, "DB LIST IS NULL OR EMPTY.");
        }

        // TODO: handle null or empty db list scenario.

        if (isNetworkOnline) {

            final boolean areDatesTheSame = mSupport.checkDatesEquality(mDbOrganizationList, mApiOrganizationList);
            Log.i(LOG_TAG, "DATES ARE EQUAL = " + String.valueOf(areDatesTheSame).toUpperCase() + ".");

            if (areDatesTheSame) {

                if (!isResultReceiverNull)
                    resultReceiverSendResult(mDbOrganizationList);

            } else {

                mCombinedOrganizationList = mSupport.combineOrganizations(mDbOrganizationList, mApiOrganizationList);
                Log.i(LOG_TAG, "COMBINED LIST SIZE: " + String.valueOf(mCombinedOrganizationList.size()) + ".");

                databaseSaveOrganizations(mCombinedOrganizationList);
            }

        } else {

            if (!isResultReceiverNull)
                resultReceiverSendResult(mDbOrganizationList);
        }
    }

    public void databaseSaveOrganizations(final List<Organization> _organizationList) {

        final DbSaveOrganizationsTask dbSaveOrganizationsTask = new DbSaveOrganizationsTask(CustomService.this, _organizationList);
        dbSaveOrganizationsTask.doInBackground();
        Log.i(dbSaveOrganizationsTask.LOG_TAG, "DONE.");

        if (!isResultReceiverNull)
            resultReceiverSendResult(_organizationList);
    }
}
