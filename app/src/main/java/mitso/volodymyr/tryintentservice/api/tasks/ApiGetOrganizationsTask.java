package mitso.volodymyr.tryintentservice.api.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import mitso.volodymyr.tryintentservice.api.Api;
import mitso.volodymyr.tryintentservice.api.CustomDeserializer;
import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.models.Organization;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGetOrganizationsTask extends AsyncTask<Void, Void, List<Organization>> {

    public String                   LOG_TAG = Constants.API_GET_ORGANIZATIONS_TASK_LOG_TAG;

    public interface Callback{

        void onSuccess(List<Organization> _result);
        void onFailure(Throwable _error);
    }

    private List<Organization>      mOrganizationList;
    private Callback                mCallback;
    private Exception               mException;

    public void setCallback(Callback _callback) {

        if (mCallback == null)
            mCallback = _callback;
    }

    public void releaseCallback() {

        if (mCallback != null)
            mCallback = null;
    }

    @Override
    protected List<Organization> doInBackground(Void... _voids) {

        try {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(List.class, new CustomDeserializer());

            final Gson gson = gsonBuilder.create();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            final Api api = retrofit.create(Api.class);

            final Call<List<Organization>> call = api.getOrganizations();
            final Response<List<Organization>> response = call.execute();

            mOrganizationList = response.body();

        } catch (Exception _error) {

            _error.printStackTrace();
            mException = _error;
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Organization> _organizationList) {
        super.onPostExecute(_organizationList);

        if (mCallback != null) {
            if (mException == null)
                mCallback.onSuccess(mOrganizationList);
            else
                mCallback.onFailure(mException);
        }
    }
}