package mitso.volodymyr.tryintentservice.api;

import java.util.List;

import mitso.volodymyr.tryintentservice.models.Organization;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("/ru/public/currency-cash.json")
    Call<List<Organization>> getOrganizations();
}