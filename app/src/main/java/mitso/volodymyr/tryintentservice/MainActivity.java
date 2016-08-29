package mitso.volodymyr.tryintentservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.models.Organization;
import mitso.volodymyr.tryintentservice.service.CustomService;
import mitso.volodymyr.tryintentservice.service.ServiceResultReceiver;
import mitso.volodymyr.tryintentservice.support.Support;

public class MainActivity extends AppCompatActivity {

    private final String                LOG_TAG = Constants.MAIN_ACTIVITY_LOG_TAG;

    private Support                     mSupport;

    private ServiceResultReceiver       mServiceResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSupport = new Support();

        startCustomService();
    }

    private void startCustomService() {

        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setCallback(new ServiceResultReceiver.Callback() {
            @Override
            public void onReceiveResult(int _resultCode, Bundle _resultData) {

                if (_resultCode == Constants.SUCCESS_RESULT_CODE) {

                    Log.i(mServiceResultReceiver.LOG_TAG, "RESULT IS RECEIVED.");

                    final List<Organization> organizationList = _resultData.getParcelableArrayList(Constants.RESULT_BUNDLE_KEY);

                    if (organizationList != null) {

                        Log.i(LOG_TAG, String.valueOf(organizationList.size()) + ".");

                        Log.i(LOG_TAG, organizationList.get(0).toString());
                        Log.i(LOG_TAG, organizationList.get(organizationList.size() - 1).toString());

                        final TextView textView1 = (TextView) findViewById(R.id.tv1);
                        textView1.setText(organizationList.get(0).toString());

                        final TextView textView2 = (TextView) findViewById(R.id.tv2);
                        textView2.setText(organizationList.get(organizationList.size() - 1).toString());
                    }
                }

                mSupport.scheduleAlarm(MainActivity.this);

                mServiceResultReceiver.releaseCallback();
            }
        });

        final Intent serviceIntent = new Intent(MainActivity.this, CustomService.class);
        serviceIntent.putExtra(Constants.SERVICE_RESULT_RECEIVER_BUNDLE_KEY, mServiceResultReceiver);
        startService(serviceIntent);
    }
}
