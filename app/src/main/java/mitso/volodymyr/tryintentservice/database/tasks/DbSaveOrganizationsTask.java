package mitso.volodymyr.tryintentservice.database.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.database.DatabaseHelper;
import mitso.volodymyr.tryintentservice.models.Organization;

public class DbSaveOrganizationsTask {

    public String                   LOG_TAG = Constants.DB_SAVE_ORGANIZATIONS_TASK_LOG_TAG;

    private DatabaseHelper          mDatabaseHelper;
    private SQLiteDatabase          mSQLiteDatabase;
    private List<Organization>      mOrganizationList;

    public DbSaveOrganizationsTask(Context _context, List<Organization> _organizationList) {

        this.mDatabaseHelper = DatabaseHelper.getDatabaseHelper(_context);
        this.mOrganizationList = _organizationList;
    }

    public void doInBackground() {

        try {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

            for (int i = 0; i < mOrganizationList.size(); i++) {

                final Organization organization = mOrganizationList.get(i);

                final ContentValues contentValues = new ContentValues();

                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_ID, organization.getId());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_NAME, organization.getName());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_TYPE, organization.getType());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_REGION, organization.getRegion());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_CITY, organization.getCity());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_ADDRESS, organization.getAddress());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_PHONE, organization.getPhone());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_LINK, organization.getLink());

                final String currenciesString = new Gson().toJson(organization.getCurrencies());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_CURRENCIES, currenciesString);

                final String dateString = new SimpleDateFormat(Constants.DATE_AND_TIME_DB_FORMAT, Locale.getDefault()).format(organization.getDate());
                contentValues.put(DatabaseHelper.COLUMN_ORGANIZATION_DATE, dateString);

                if (organization.getDatabaseId() == 0) {

                    mSQLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, null, contentValues);

                } else {

                    mSQLiteDatabase.update(DatabaseHelper.DATABASE_TABLE, contentValues,
                            DatabaseHelper.COLUMN_DATABASE_ID + DatabaseHelper.EQUALS + organization.getDatabaseId(), null);
                }
            }

        } catch (Exception _error) {

            Log.e(LOG_TAG, "ERROR.");
            _error.printStackTrace();

        } finally {

            if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen())
                mSQLiteDatabase.close();

            if (mDatabaseHelper != null)
                mDatabaseHelper.close();
        }
    }
}