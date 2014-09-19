package net.gymhark;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class ScanTagDataSource {
	
	
	
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { "ID",
			"SCHUELERID", "TIME"};

	public ScanTagDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public ScanTag createScanTag(int schuelerid, long time) {
		ContentValues values = new ContentValues();
		values.put("SCHUELERID", schuelerid);
		values.put("TIME", time);
		long insertId = database.insert("SCANTAG", null,
				values);
		// To show how to query
		Cursor cursor = database.query("SCANTAG",allColumns, "ID = " + insertId, null,null, null, null);
		cursor.moveToFirst();
		return cursorToScanTag(cursor);
	}
	
	private ScanTag cursorToScanTag(Cursor cursor) {
		ScanTag si = new ScanTag();
		si.setId(cursor.getLong(0));
		si.setschuelerid(cursor.getInt(1));
		si.settime(cursor.getLong(2));
		return si;
	}

	public void deleteScanTag(ScanTag si) {
		long id = si.getId();
		database.delete("SCANTAG", "ID"
				+ " = " + id, null);	
	}
	
	public void updateScanTagInt(ScanTag si, String table, String column, int newValue) {
		long id = si.getId();
		ContentValues args = new ContentValues();
	    args.put(column, newValue);
		database.update("SCANTAG", args, "ID = " + id, null);
	}
	
	public void updateScanTagString(ScanTag si, String table, String column, String newValue) {
		long id = si.getId();
		ContentValues args = new ContentValues();
	    args.put(column, newValue);
		database.update("SCANTAG", args, "ID = " + id, null);
	}
	
	public List<ScanTag> getAllScanTags() {
		List<ScanTag> scantaglist = null;
		scantaglist = new ArrayList<ScanTag>();
		
		Cursor cursor = database.query("SCANTAG",
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		if(cursor.getCount() == 0) return scantaglist;
		
		while (!cursor.isAfterLast()) {
			ScanTag si = cursorToScanTag(cursor);
			scantaglist.add(si);
			cursor.moveToNext();
		} 	
		// Make sure to close the cursor
		cursor.close();
		
		return scantaglist;
	}

	

}