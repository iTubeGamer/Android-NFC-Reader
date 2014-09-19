package net.gymhark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	
	private static final String DATABASE_NAME = "nfctagdb2.db";
	//löschen: nfctagdb.db, nfctagdb1.db
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String TABLE_CREATE_SCANTAG= ""
			+"create table SCANTAG ("
			+"  ID integer primary key autoincrement, "
			+   "SCHUELERID int, "
			+  	"TIME int)";
			

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_CREATE_SCANTAG);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS SCANITEM");
		onCreate(db);
	}

}