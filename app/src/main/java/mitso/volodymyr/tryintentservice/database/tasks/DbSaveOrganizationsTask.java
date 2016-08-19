package mitso.volodymyr.tryintentservice.database.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mitso.volodymyr.tryintentservice.constants.Constants;
import mitso.volodymyr.tryintentservice.database.DatabaseHelper;
import mitso.volodymyr.tryintentservice.models.Organization;

public class DbSaveOrganizationsTask extends AsyncTask<Void, Void, Void> {

    public String                   LOG_TAG = Constants.DB_SAVE_ORGANIZATIONS_TASK_LOG_TAG;

    public interface Callback {

        void onSuccess();
        void onFailure(Throwable _error);
    }

    private Callback                mCallback;
    private Exception               mException;
    private DatabaseHelper          mDatabaseHelper;
    private SQLiteDatabase          mSQLiteDatabase;
    private List<Organization>      mOrganizationList;

    public void setCallback(Callback _callback) {

        if (mCallback == null)
            mCallback = _callback;
    }

    public void releaseCallback() {

        if (mCallback != null)
            mCallback = null;
    }

    public DbSaveOrganizationsTask(Context _context, List<Organization> _organizationList) {

        this.mDatabaseHelper = DatabaseHelper.getDatabaseHelper(_context);
        this.mOrganizationList = _organizationList;
    }

    @Override
    protected Void doInBackground(Void ... _params) {

        try {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

            long numberOfRows  = DatabaseUtils.queryNumEntries(mSQLiteDatabase, DatabaseHelper.DATABASE_TABLE);

            if (numberOfRows > 0) {

                mSQLiteDatabase.delete(DatabaseHelper.DATABASE_TABLE, null, null);

                Log.i(LOG_TAG, "REWRITING DATABASE.");

            } else
                Log.i(LOG_TAG, "CREATING DATABASE FIRST TIME.");

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

                mSQLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, null, contentValues);
            }

        } catch (Exception _error) {

            _error.printStackTrace();
            mException = _error;

        } finally {

            if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen())
                mSQLiteDatabase.close();

            if (mDatabaseHelper != null)
                mDatabaseHelper.close();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void _aVoid) {
        super.onPostExecute(_aVoid);

        if (mCallback != null) {
            if (mException == null)
                mCallback.onSuccess();
            else
                mCallback.onFailure(mException);
        }
    }
}