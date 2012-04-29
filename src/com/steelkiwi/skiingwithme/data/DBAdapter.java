package com.steelkiwi.skiingwithme.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	private static final String DATABASE_NAME = "SkiingWithMeDB";
    private static final int DATABASE_VERSION = 1;
    
    //resorts table
    private static final String RESORT_TABLE = "Resorts";
    private static final String RESORT_ID    = "_id";
    private static final String RESORT_TITLE = "title";
    private static final String RESORT_CNTRY = "cntry";
    private static final String RESORT_CITY  = "city";
    private static final String RESORT_SLOP  = "slop";
    private static final String RESORT_BOUN  = "boun";
    private static final String RESORT_CREATE_TABLE = "create table " + RESORT_TABLE + " (" + 
    														RESORT_ID    + " integer primary key autoincrement, " + 
    														RESORT_TITLE + " text, " + 
    														RESORT_CNTRY + " text, " +
    														RESORT_CITY  + " text, " +
    														RESORT_SLOP  + " blob, " +
    														RESORT_BOUN  + " blob"   +
													 ");";
    private static final String RESORT_DELETE_TABLE = "delete from " + RESORT_TABLE;    		
    private static final String RESORT_DROP_TABLE   = "drop table if exists " + RESORT_TABLE;
    private static final String RESORT_INDEX = "ResortsIndex";
    private static final String RESORT_CREATE_INDEX = "create index " + RESORT_INDEX + 
														" on " + RESORT_TABLE + "(" + RESORT_TITLE + ");";
    private static final String RESORT_DROP_INDEX   = "drop index if exists " + RESORT_INDEX;	
    
   	private final Context context;
	private DBHelper dbHelper;
    private SQLiteDatabase db;
    
	public DBAdapter(Context cContext) {
		this.context = cContext;
	}
	
	public DBAdapter open() throws SQLException {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
    	if (db != null) { db.close(); }
    	if (dbHelper != null) { dbHelper.close(); }
    }
    
    private byte[] serializeSlops(ArrayList<Slope> slope) throws IOException {
		ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(byteOS);
		objOS.writeObject(slope);
		byte[] eventBytes = byteOS.toByteArray();
		return eventBytes;
	}

    private byte[] serializeBounds(ArrayList<SWMPoint> boun) throws IOException {
		ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(byteOS);
		objOS.writeObject(boun);
		byte[] eventBytes = byteOS.toByteArray();
		return eventBytes;
	}
    
	public void storeResorts(List<Resort> resorts) throws IOException {
		db.beginTransaction();
		try {
			db.execSQL(RESORT_DELETE_TABLE);

			for (Resort resort : resorts) {
				ContentValues values = new ContentValues();
				values.put(RESORT_TITLE, resort.getTitle());
				values.put(RESORT_CNTRY, resort.getCountry());
				values.put(RESORT_CITY,  resort.getCity());
				values.put(RESORT_SLOP,  serializeSlops(resort.getSlopes()));
				values.put(RESORT_BOUN,  serializeBounds(resort.getBounds()));
				db.insert(RESORT_TABLE, null, values);
			}

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public List<Resort> restoreResorts() throws StreamCorruptedException, IOException, ClassNotFoundException {
		ArrayList<Resort> resorts = new ArrayList<Resort>();
		
		String query = "SELECT * FROM " + RESORT_TABLE;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor == null) { return resorts; }
		if (cursor.getCount() <= 0) { return resorts; }

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Resort resort = new Resort();
			
			int titleInd = cursor.getColumnIndex(RESORT_TITLE);
			if (titleInd != -1) {
				resort.setTitle(cursor.getString(titleInd));
			}
			
			int cntryInd = cursor.getColumnIndex(RESORT_CNTRY);
			if (cntryInd != -1) {
				resort.setCountry(cursor.getString(cntryInd));
			}
			
			int cityInd = cursor.getColumnIndex(RESORT_CITY);
			if (cityInd != -1) {
				resort.setCity(cursor.getString(cityInd));
			}
			
			int slopesInd = cursor.getColumnIndex(RESORT_SLOP);
			if (slopesInd != -1) {
				byte[] objBytes = cursor.getBlob(slopesInd);
				ByteArrayInputStream byteIS = new ByteArrayInputStream(objBytes);
				ObjectInputStream objIS = new ObjectInputStream(byteIS);
				ArrayList<Slope> slopes = (ArrayList<Slope>) objIS.readObject();
				resort.setSlopes(slopes);
			}
			
			int bounInd = cursor.getColumnIndex(RESORT_BOUN);
			if (bounInd != -1) {
				byte[] objBytes = cursor.getBlob(bounInd);
				ByteArrayInputStream byteIS = new ByteArrayInputStream(objBytes);
				ObjectInputStream objIS = new ObjectInputStream(byteIS);
				ArrayList<SWMPoint> bounds = (ArrayList<SWMPoint>) objIS.readObject();
				resort.setBounds(bounds);
			}
			
			resorts.add(resort);
			cursor.moveToNext();
		}

		cursor.close();
		return resorts;
	}
    
    private static class DBHelper extends SQLiteOpenHelper {
    	public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	db.execSQL(RESORT_CREATE_TABLE);
        	db.execSQL(RESORT_CREATE_INDEX);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	db.execSQL(RESORT_DROP_INDEX);
        	db.execSQL(RESORT_DROP_TABLE);
            db.execSQL(RESORT_CREATE_TABLE);
            db.execSQL(RESORT_CREATE_INDEX);
        }
    }
}
