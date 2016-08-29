package mitso.volodymyr.tryintentservice.api.tasks;

import android.util.Log;

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

public class ApiGetOrganizationsTask {

    public String                   LOG_TAG = Constants.API_GET_ORGANIZATIONS_TASK_LOG_TAG;

    private List<Organization>      mOrganizationList;

    public List<Organization> doInBackground() {

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

            Log.e(LOG_TAG, "ERROR.");
            _error.printStackTrace();
        }

        return mOrganizationList;
    }
}