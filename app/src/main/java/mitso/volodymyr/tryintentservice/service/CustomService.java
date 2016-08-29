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

        super("CustomService");
    }

    @Override
    protected void onHandleIntent(final Intent _intent) {

        // TODO: handle all on failure scenarios.

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
                    mSupport.showToastNetworkIsOffline(CustomService.this);
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

        mBundle.putParcelableArrayList(Constants.RESULT_BUNDLE_KEY, new ArrayList<>(_organizationList));
        mResultReceiver.send(Constants.SUCCESS_RESULT_CODE, mBundle);

        Log.i(LOG_TAG, "RESULT IS SEND.");
    }

    public void apiGetOrganizations() {

        final ApiGetOrganizationsTask apiGetOrganizationsTask = new ApiGetOrganizationsTask();
        apiGetOrganizationsTask.setCallback(new ApiGetOrganizationsTask.Callback() {

            @Override
            public void onSuccess(List<Organization> _result) {

                Log.i(apiGetOrganizationsTask.LOG_TAG, "ON SUCCESS.");
                Log.i(apiGetOrganizationsTask.LOG_TAG, String.valueOf(_result.size()) + ".");

                mApiOrganizationList = mSupport.deleteNullPropertiesObjects(_result);

                Log.i(LOG_TAG, "API LIST SIZE: " + String.valueOf(mApiOrganizationList.size()) + ".");
                Log.i(LOG_TAG, mApiOrganizationList.get(0).toString());
                Log.i(LOG_TAG, mApiOrganizationList.get(mApiOrganizationList.size() - 1).toString());

                if (isDatabaseCreated) {

                    databaseGetOrganizations();

                } else {

                    databaseSaveOrganizations(mApiOrganizationList);
                }

                apiGetOrganizationsTask.releaseCallback();
            }

            @Override
            public void onFailure(Throwable _error) {

                Log.e(apiGetOrganizationsTask.LOG_TAG, "ON FAILURE.");
                _error.printStackTrace();

                apiGetOrganizationsTask.releaseCallback();
            }
        });

        apiGetOrganizationsTask.execute();
    }

    private void databaseGetOrganizations() {

        final DbGetOrganizationsTask dbGetOrganizationsTask = new DbGetOrganizationsTask(CustomService.this);
        dbGetOrganizationsTask.setCallback(new DbGetOrganizationsTask.Callback() {

            @Override
            public void onSuccess(List<Organization> _result) {

                Log.i(dbGetOrganizationsTask.LOG_TAG, "ON SUCCESS.");
                Log.i(dbGetOrganizationsTask.LOG_TAG, String.valueOf(_result.size()));

                mDbOrganizationList = _result;

                Log.i(LOG_TAG, "DB LIST SIZE: " + String.valueOf(mDbOrganizationList.size()) + ".");
                Log.i(LOG_TAG, mDbOrganizationList.get(0).toString());
                Log.i(LOG_TAG, mDbOrganizationList.get(mDbOrganizationList.size() - 1).toString());

                if (isNetworkOnline) {

                    final boolean areDatesTheSame = mSupport.checkDatesEquality(mDbOrganizationList, mApiOrganizationList);

                    Log.i(LOG_TAG, "DATE ARE EQUAL = " + String.valueOf(areDatesTheSame).toUpperCase() + ".");

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

                dbGetOrganizationsTask.releaseCallback();
            }

            @Override
            public void onFailure(Throwable _error) {

                Log.e(dbGetOrganizationsTask.LOG_TAG, "ON FAILURE.");
                _error.printStackTrace();

                dbGetOrganizationsTask.releaseCallback();
            }
        });

        dbGetOrganizationsTask.execute();
    }

    public void databaseSaveOrganizations(final List<Organization> _organizationList) {

        final DbSaveOrganizationsTask dbSaveOrganizationsTask = new DbSaveOrganizationsTask(CustomService.this, _organizationList);
        dbSaveOrganizationsTask.setCallback(new DbSaveOrganizationsTask.Callback() {

            @Override
            public void onSuccess() {

                Log.i(dbSaveOrganizationsTask.LOG_TAG, "ON SUCCESS.");

                if (!isResultReceiverNull)
                    resultReceiverSendResult(_organizationList);

                dbSaveOrganizationsTask.releaseCallback();
            }

            @Override
            public void onFailure(Throwable _error) {

                Log.e(dbSaveOrganizationsTask.LOG_TAG, "ON FAILURE.");
                _error.printStackTrace();

                dbSaveOrganizationsTask.releaseCallback();
            }
        });

        dbSaveOrganizationsTask.execute();
    }
}
