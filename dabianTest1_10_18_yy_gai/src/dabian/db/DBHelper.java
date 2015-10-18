package dabian.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "dabian.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		 db.execSQL("CREATE TABLE IF NOT EXISTS MyGroup (_id integer primary key , group_members  varchar(50), group_project varchar(50))");  
		db.execSQL("CREATE TABLE IF NOT EXISTS judger (_id varchar(10) primary key  , judger_name varchar(50), judger_introduce varchar(50))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE group ADD COLUMN other STRING");
		db.execSQL("ALTER TABLE judger ADD COLUMN other STRING");
	}

}
