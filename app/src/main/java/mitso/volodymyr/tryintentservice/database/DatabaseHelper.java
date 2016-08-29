package mitso.volodymyr.tryintentservice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String      DATABASE_NAME       = "organizations_database.db";
    public static final int         DATABASE_VERSION    = 1;
    public static final String      DATABASE_TABLE      = "organizations_table";

    public static final String      COLUMN_DATABASE_ID              = "_id";
    public static final String      COLUMN_ORGANIZATION_ID          = "id";
    public static final String      COLUMN_ORGANIZATION_NAME        = "name";
    public static final String      COLUMN_ORGANIZATION_TYPE        = "type";
    public static final String      COLUMN_ORGANIZATION_REGION      = "region";
    public static final String      COLUMN_ORGANIZATION_CITY        = "city";
    public static final String      COLUMN_ORGANIZATION_ADDRESS     = "address";
    public static final String      COLUMN_ORGANIZATION_PHONE       = "phone";
    public static final String      COLUMN_ORGANIZATION_LINK        = "link";
    public static final String      COLUMN_ORGANIZATION_CURRENCIES  = "currencies";
    public static final String      COLUMN_ORGANIZATION_DATE        = "date";

    public static final String      CREATE_TABLE            = "create table";
    public static final String      INTEGER_PRIMARY_KEY     = "integer primary key autoincrement";
    public static final String      TEXT                    = "text not null";

    public static final String      COMMA               = ",";
    public static final String      SPACE               = " ";
    public static final String      PARENTHESES_IN      = "(";
    public static final String      PARENTHESES_OUT     = ")";

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static DatabaseHelper instance;

    private DatabaseHelper(Context _context) {

        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getDatabaseHelper(Context _context) {

        if (instance == null)
            instance = new DatabaseHelper(_context);

        return instance;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(SQLiteDatabase _db) {

        _db.execSQL(
                CREATE_TABLE + SPACE + DATABASE_TABLE + SPACE +
                        PARENTHESES_IN +
                        COLUMN_DATABASE_ID + SPACE + INTEGER_PRIMARY_KEY + COMMA + SPACE +
                        COLUMN_ORGANIZATION_ID + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_NAME + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_TYPE + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_REGION + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_CITY + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_ADDRESS + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_PHONE + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_LINK + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_CURRENCIES + SPACE + TEXT + COMMA + SPACE +
                        COLUMN_ORGANIZATION_DATE + SPACE + TEXT +
                        PARENTHESES_OUT
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {

    }
}