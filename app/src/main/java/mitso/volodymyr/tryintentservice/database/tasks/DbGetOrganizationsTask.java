package mitso.volodymyr.tryintentservice.database.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.database.DatabaseHelper;
import mitso.volodymyr.tryintentservice.models.Currency;
import mitso.volodymyr.tryintentservice.models.Organization;

public class DbGetOrganizationsTask {

    public String                   LOG_TAG = Constants.DB_GET_ORGANIZATIONS_TASK_LOG_TAG;

    private DatabaseHelper          mDatabaseHelper;
    private SQLiteDatabase          mSQLiteDatabase;
    private Cursor                  mCursor;
    private List<Organization>      mOrganizationList;

    public DbGetOrganizationsTask(Context _context) {

        this.mDatabaseHelper = DatabaseHelper.getDatabaseHelper(_context);
        this.mOrganizationList = new ArrayList<>();
    }

    public List<Organization> doInBackground() {

        try {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

            final String[] projection = {
                    DatabaseHelper.COLUMN_DATABASE_ID,
                    DatabaseHelper.COLUMN_ORGANIZATION_ID,
                    DatabaseHelper.COLUMN_ORGANIZATION_NAME,
                    DatabaseHelper.COLUMN_ORGANIZATION_TYPE,
                    DatabaseHelper.COLUMN_ORGANIZATION_REGION,
                    DatabaseHelper.COLUMN_ORGANIZATION_CITY,
                    DatabaseHelper.COLUMN_ORGANIZATION_ADDRESS,
                    DatabaseHelper.COLUMN_ORGANIZATION_PHONE,
                    DatabaseHelper.COLUMN_ORGANIZATION_LINK,
                    DatabaseHelper.COLUMN_ORGANIZATION_CURRENCIES,
                    DatabaseHelper.COLUMN_ORGANIZATION_DATE
            };

            mCursor = mSQLiteDatabase.query(DatabaseHelper.DATABASE_TABLE, projection,
                    null, null, null, null, null);

            while (mCursor.moveToNext()) {

                final int databaseId = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COLUMN_DATABASE_ID));
                final String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_ID));
                final String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_NAME));
                final String type = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_TYPE));
                final String region = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_REGION));
                final String city = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_CITY));
                final String address = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_ADDRESS));
                final String phone = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_PHONE));
                final String link = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_LINK));

                final String currenciesString = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_CURRENCIES));
                final Currency[] currencyArray = new Gson().fromJson(currenciesString, Currency[].class);
                final List<Currency> currencyList = new ArrayList<>(Arrays.asList(currencyArray));

                final String dateString = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ORGANIZATION_DATE));
                final Date date = new SimpleDateFormat(Constants.DATE_AND_TIME_DB_FORMAT, Locale.getDefault()).parse(dateString);

                final Organization organization = new Organization();

                organization.setDatabaseId(databaseId);
                organization.setId(id);
                organization.setName(name);
                organization.setType(type);
                organization.setRegion(region);
                organization.setCity(city);
                organization.setAddress(address);
                organization.setPhone(phone);
                organization.setLink(link);
                organization.setCurrencies(currencyList);
                organization.setDate(date);

                mOrganizationList.add(organization);
            }

        } catch (Exception _error) {

            Log.e(LOG_TAG, "ERROR.");
            _error.printStackTrace();

        } finally {

            if (mCursor != null && !mCursor.isClosed())
                mCursor.close();

            if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen())
                mSQLiteDatabase.close();

            if (mDatabaseHelper != null)
                mDatabaseHelper.close();
        }

        return mOrganizationList;
    }
}