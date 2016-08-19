package mitso.volodymyr.tryintentservice.service;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import mitso.volodymyr.tryintentservice.constants.Constants;

@SuppressLint("ParcelCreator")
public class ServiceResultReceiver extends ResultReceiver {

    public String               LOG_TAG = Constants.SERVICE_RESULT_RECEIVER_LOG_TAG;

    public interface Callback {

        void onReceiveResult(int _resultCode, Bundle _resultData);
    }

    private Callback            mCallback;

    public ServiceResultReceiver(Handler _handler) {

        super(_handler);
    }

    public void setCallback(Callback _callback) {

        if (mCallback == null)
            mCallback = _callback;
    }

    public void releaseCallback() {

        if (mCallback != null)
            mCallback = null;
    }

    @Override
    protected void onReceiveResult(int _resultCode, Bundle _resultData) {

        if (mCallback != null)
            mCallback.onReceiveResult(_resultCode, _resultData);
    }
}
