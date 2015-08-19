package br.com.geconp.geconp.calendar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import br.com.geconp.geconp.model.Despesa;

public class CalendarProvider extends ContentProvider {
	
	private static final String DATABASE_NAME = "Calendar";
	private static final String EVENTS_TABLE = "calendario";
	private static final int DATABASE_VERSION = 4;
	private static final String  AUTHORITY = "br.com.geconp.geconp.calendar.calendarprovider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/events");
	public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/events/");
	private static final UriMatcher uriMatcher;
	
	public static final String EVENT = "datas";
	public static final String LOCATION = "bancoDados";
	public static final String DESCRIPTION = "calendarioSimples";
	public static final String START = "inicio";
	public static final String END = "fim";
	public static final String ID = "_id";
	public static final String START_DAY = "diaInicio";
	public static final String END_DAY = "diaFim";
	public static final String COLOR = "color";
	
	private static final HashMap<String, String> mMap;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            createTables(db);
        }
        

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                              int newVersion) 
        {
            Log.w("CalendarProvider", "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS events");
            onCreate(db);
        }
        
    	private void createTables(SQLiteDatabase db){
    		db.execSQL("CREATE TABLE " + EVENTS_TABLE + "(" + ID + " integer primary key autoincrement, " +
    				EVENT + " TEXT, " + LOCATION + " TEXT, " + DESCRIPTION + " TEXT, "
    				+ START + " INTEGER, "+ END + " INTEGER, " + START_DAY + " INTEGER, " + END_DAY + " INTEGER, " + COLOR +" INTEGER);");
    	}
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		int num = uriMatcher.match(uri);
		if(num == 1){
			count = db.delete(EVENTS_TABLE, selection,selectionArgs);
		}else if(num == 2){
			String id = uri.getPathSegments().get(1);
			count = db.delete(EVENTS_TABLE, ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + 
		               selection + ')' : ""), 
		               selectionArgs);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(EVENTS_TABLE,null, values);
		Uri _uri = null;
		if(rowID > 0){
			_uri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE,rowID);
			getContext().getContentResolver().notifyChange(uri,null);
			
		}else{
			throw new SQLException("Failed to insert row into " + uri);
		}
		return _uri;
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DBHelper = new DatabaseHelper(context);
		db = DBHelper.getWritableDatabase();
		return (db == null)? false:true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
	    sqlBuilder.setTables(EVENTS_TABLE);

	    if(uriMatcher.match(uri) == 1){
	    	sqlBuilder.setProjectionMap(mMap);
	    }else if(uriMatcher.match(uri) == 2){
	    	sqlBuilder.setProjectionMap(mMap);
	    	sqlBuilder.appendWhere(ID + "=?");
	    	selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,new String[] {uri.getLastPathSegment()});
	    }else if(uriMatcher.match(uri) == 3){
	    	sqlBuilder.setProjectionMap(mMap);
	    	sqlBuilder.appendWhere(START + ">=? OR ");
	    	sqlBuilder.appendWhere(END + "<=?");
	    	List<String> list = uri.getPathSegments();
	    	String start = list.get(1);
	    	String end = list.get(2);
	    	selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,new String[] {start,end});
	    }
	    if(sortOrder == null || sortOrder == "")
	    	sortOrder = START + " COLLATE LOCALIZED ASC";
		Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs,null,null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		int num = uriMatcher.match(uri);
		if(num == 1){
			count = db.update(EVENTS_TABLE, values, selection, selectionArgs);
		}else if(num == 2){
			count = db.update(EVENTS_TABLE, values, ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + 
	                  selection + ')' : ""), 
	                  selectionArgs);
		}else{
			throw new IllegalArgumentException(
		            "Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

    public void inserirCalendarioDespesa(String dia, String mes, String ano){
        int inicioAno= Integer.parseInt(ano);
        int inicioMes= Integer.parseInt(mes);
        int inicioDia= Integer.parseInt(dia);
        int hora= 8;
        int minuto= 1;

        ContentValues values= new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_RED);
        values.put(CalendarProvider.DESCRIPTION, "insercao");
        values.put(CalendarProvider.LOCATION, "calendario despesa");
        values.put(CalendarProvider.EVENT, "Inserir despesa");

        Calendar cal= Calendar.getInstance();
        cal.set(inicioAno, inicioMes, inicioDia, hora, minuto);
        values.put(CalendarProvider.START, cal.getTimeInMillis());
        values.put(CalendarProvider.START_DAY, dia);
        TimeZone tz= TimeZone.getDefault();

        cal.set(inicioAno, inicioMes, inicioDia, hora, minuto);
        int fimDiaJulian= Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, fimDiaJulian);
        Uri uri= insert(CalendarProvider.CONTENT_URI, values);
    }
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY,EVENTS_TABLE,1);
		uriMatcher.addURI(AUTHORITY,EVENTS_TABLE + "/#",2);
		uriMatcher.addURI(AUTHORITY, EVENTS_TABLE+"/#/#", 3);
		
		mMap = new HashMap<String, String>();
		mMap.put(ID, ID);
		mMap.put(EVENT, EVENT);
		mMap.put(START, START);
		mMap.put(LOCATION, LOCATION);
		mMap.put(DESCRIPTION, DESCRIPTION);
		mMap.put(END, END);
		mMap.put(START_DAY, START_DAY);
		mMap.put(END_DAY, END_DAY);
		mMap.put(COLOR, COLOR);
	}

}
