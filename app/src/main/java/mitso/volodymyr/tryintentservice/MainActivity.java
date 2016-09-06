package mitso.volodymyr.tryintentservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Date;
import java.util.List;

import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.databinding.ActivityMainBinding;
import mitso.volodymyr.tryintentservice.models.Organization;
import mitso.volodymyr.tryintentservice.service.CustomService;
import mitso.volodymyr.tryintentservice.service.ServiceResultReceiver;
import mitso.volodymyr.tryintentservice.support.Support;

public class MainActivity extends AppCompatActivity {

    private final String                LOG_TAG = Constants.MAIN_ACTIVITY_LOG_TAG;

    private ActivityMainBinding         mBinding;

    private Support                     mSupport;

    private ServiceResultReceiver       mServiceResultReceiver;

    private ProgressDialog              mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mSupport = new Support();

        mProgressDialog = new ProgressDialog(this);

        startCustomService();
    }

    private void startCustomService() {

        mSupport.showProgressDialog(mProgressDialog);

        final Date startDate = new Date();

        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setCallback(new ServiceResultReceiver.Callback() {
            @Override
            public void onReceiveResult(int _resultCode, Bundle _resultData) {

                if (_resultCode == Constants.SUCCESS_RESULT_CODE) {

                    Log.i(mServiceResultReceiver.LOG_TAG, "RESULT IS RECEIVED.");

                    final List<Organization> organizationList = _resultData.getParcelableArrayList(Constants.RESULT_BUNDLE_KEY);

                    if (organizationList != null && !organizationList.isEmpty()) {

                        Log.i(LOG_TAG, String.valueOf("ORGANIZATION LIST SIZE: " + organizationList.size()) + ".");
                        Log.i(LOG_TAG, organizationList.get(0).toString());
                        Log.i(LOG_TAG, organizationList.get(organizationList.size() - 1).toString());

                        mBinding.tv1.setText(organizationList.get(0).toString());
                        mBinding.tv2.setText(organizationList.get(organizationList.size() - 1).toString());

                        mSupport.dismissProgressDialog(mProgressDialog);

                    } else {

                        Log.e(LOG_TAG, "ORGANIZATION LIST IS NULL OR EMPTY.");

                        mSupport.dismissProgressDialog(mProgressDialog);

                        mSupport.showToastFirstRun(MainActivity.this);
                    }
                }

                mSupport.scheduleAlarm(MainActivity.this);

                final Date endDate = new Date();
                long delay = endDate.getTime() - startDate.getTime();
                Log.i(LOG_TAG, "DELAY = " + delay);

                mServiceResultReceiver.releaseCallback();
            }
        });

        final Intent serviceIntent = new Intent(MainActivity.this, CustomService.class);
        serviceIntent.putExtra(Constants.SERVICE_RESULT_RECEIVER_BUNDLE_KEY, mServiceResultReceiver);
        startService(serviceIntent);
    }
}
