package mitso.volodymyr.tryintentservice.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.models.Currency;
import mitso.volodymyr.tryintentservice.models.Organization;
import mitso.volodymyr.tryintentservice.models.json.JsonData;
import mitso.volodymyr.tryintentservice.models.json.JsonOrganization;

public class CustomDeserializer implements JsonDeserializer<List<Organization>> {

    private JsonData                        mJsonData;

    private List<JsonOrganization>          mJsonOrganizationList;

    private Map<Integer, String>            mOrgTypesMap;
    private Map<String, String>             mCurrenciesMap;
    private Map<String, String>             mRegionsMap;
    private Map<String, String>             mCitiesMap;

    private Map<Integer, List<Currency>>    mCurrencyListMap;
    private List<Currency>                  mCurrencyList;

    private List<Organization>              mOrganizationList;

    @Override
    public List<Organization> deserialize(final JsonElement _jsonElement, final Type _typeOfT, final JsonDeserializationContext _context) {

        try {

            initData(_jsonElement);

            getCurrencyListMap(_jsonElement);

            getOrganizations();

        } catch (Exception _error) {

            _error.printStackTrace();
        }

        return mOrganizationList;
    }

    private void initData(JsonElement _jsonElement) throws JSONException {

        mJsonData = new Gson().fromJson(_jsonElement, JsonData.class);

        mJsonOrganizationList = mJsonData.getJsonOrganizations();

        mOrgTypesMap = mJsonData.getOrgTypes();
        mCurrenciesMap = mJsonData.getCurrencies();
        mRegionsMap = mJsonData.getRegions();
        mCitiesMap = mJsonData.getCities();
    }

    private void getCurrencyListMap(JsonElement _jsonElementMain) throws JSONException {

        final String stringMain = _jsonElementMain.getAsJsonObject().toString();
        final JSONObject jsonObjectMain = new JSONObject(stringMain);

        mCurrencyListMap = new HashMap<>();

        for (int i = 0; i < mJsonOrganizationList.size(); i++) {

            final JSONObject jsonObjectCurrencies = jsonObjectMain
                    .getJSONArray(Constants.ORGANISATIONS_KEY)
                    .getJSONObject(i)
                    .getJSONObject(Constants.CURRENCIES_KEY);

            mCurrencyList = new ArrayList<>();

            for (String key : mCurrenciesMap.keySet()) {

                if (jsonObjectCurrencies.has(key)) {

                    final String StringCurrency = jsonObjectCurrencies.getJSONObject(key).toString();
                    final Currency currency = new Gson().fromJson(StringCurrency, Currency.class);

                    currency.setName(mCurrenciesMap.get(key));
                    currency.setAbbreviation(key);

                    mCurrencyList.add(currency);
                }
            }

            mCurrencyListMap.put(i, mCurrencyList);
        }
    }

    private void getOrganizations() throws ParseException, JSONException {

        mOrganizationList = new ArrayList<>();

        for (int i = 0; i < mJsonOrganizationList.size(); i++) {

            final Organization organization = new Organization();

            organization.setId(mJsonOrganizationList.get(i).getId());
            organization.setName(mJsonOrganizationList.get(i).getTitle());

            organization.setType(mOrgTypesMap.get(mJsonOrganizationList.get(i).getOrgType()));
            organization.setRegion(mRegionsMap.get(mJsonOrganizationList.get(i).getRegionId()));
            organization.setCity(mCitiesMap.get(mJsonOrganizationList.get(i).getCityId()));

            organization.setAddress(mJsonOrganizationList.get(i).getAddress());
            organization.setPhone(mJsonOrganizationList.get(i).getPhone());
            organization.setLink(mJsonOrganizationList.get(i).getLink());

            organization.setCurrencies(mCurrencyListMap.get(i));

            organization.setDate(new SimpleDateFormat(Constants.DATE_AND_TIME_API_FORMAT, Locale.getDefault()).parse(mJsonData.getDate()));

            mOrganizationList.add(organization);
        }
    }
}