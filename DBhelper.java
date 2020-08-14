package com.example.mbg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

	// Table Name
	public static final String WORKER_TABLE = "worker";
	public static final String ATTENDANCE_TABLE = "worker_attendance";
	public static final String SALARY_TABLE ="worker_salary";
	public static final String VACATION_TABLE ="vacation";

	// Table columns
	public static final String WORKER_ID = "id";
	public static final String WORKER_NAME = "name";
	public static final String WORKER_IMAGE = "wimage";
	public static final String WORKER_PCONTACT = "pcontact";
	public static final String WORKER_SCONTACT = "scontact";
	public static final String WORKER_ADDRESS = "address";
	public static final String WORKER_PROFESSION = "profession";
	public static final String WORKER_RATE = "rate";
	public static final String WORKER_TYPE = "type";

	
	public static final String ATTENDANCE_ID = "id";
	public static final String ATTENDANCE_DAY = "day";
	public static final String ATTENDANCE_WORKER_ID = "worker_id";
	public static final String ATTENDANCE_TYPE = "day_type";
	public static final String ATTENDANCE_ATTEND = "present";
	public static final String ATTENDANCE_OVERTIME = "overtime";
	
	
	public static final String SALARY_ID ="id";
	public static final String SALARY_WORKER_ID = "worker_id";
	public static final String SALARY_PERIOD_START = "period_start";
	public static final String SALARY_PERIOD_END = "period_end";
	public static final String SALARY_PAID = "salary";
	
	public static final String VACATION_ID ="id";
	public static final String VACATION_WORKER_ID = "worker_id";
	public static final String VACATION_PERIOD_START = "period_start";
	public static final String VACATION_PERIOD_END = "period_end";
	public static final String VACATION_TYPE = "vacation_type";
	
	// Database Information
	static final String DB_NAME = "MBG_APP_DB";

	// database version
	//static final int DB_VERSION = 1;
	static final int DB_VERSION = 3;

	
	// Creating table query
	private static final String CREATE_WORKER_TABLE = "create table " + WORKER_TABLE + "(" 
			+ WORKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ WORKER_NAME + " TEXT NOT NULL, " 
			+ WORKER_IMAGE + " TEXT NOT NULL, " 
			+ WORKER_PCONTACT + " TEXT NOT NULL UNIQUE, " 
			+ WORKER_SCONTACT + " TEXT NOT NULL, " 
			+ WORKER_ADDRESS + " TEXT NOT NULL, " 
			+ WORKER_PROFESSION + " TEXT NOT NULL, " 
			+ WORKER_RATE + " TEXT NOT NULL, "  
			+ WORKER_TYPE + " TEXT);";
	
	private static final String CREATE_ATTENDANCE_TABLE = "create table " + ATTENDANCE_TABLE + "(" 
			+ ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ATTENDANCE_DAY + " DATETIME NOT NULL, " 
			+ ATTENDANCE_WORKER_ID + " INT NOT NULL, " 
			+ ATTENDANCE_TYPE + " INT NOT NULL, " 
			+ ATTENDANCE_ATTEND + " INT NOT NULL, " 
			+ ATTENDANCE_OVERTIME + " TEXT NOT NULL);"; 
	
	private static final String CREATE_SALARY_TABLE = " create table " + SALARY_TABLE + "("
			+ SALARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ SALARY_WORKER_ID + " INT NOT NULL, " 
			+ SALARY_PERIOD_START + " DATETIME NOT NULL, " 
			+ SALARY_PERIOD_END + " DATETIME NOT NULL, "
			+ SALARY_PAID + " TEXT NOT NULL);"; 
	
	private static final String CREATE_VACATION_TABLE = " create table " + VACATION_TABLE + "("
			+ VACATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ VACATION_WORKER_ID + " INT NOT NULL, " 
			+ VACATION_PERIOD_START + " DATETIME NOT NULL, " 
			+ VACATION_PERIOD_END + " DATETIME NOT NULL, "
			+ VACATION_TYPE + " INT NOT NULL);"; 
	
	public DBhelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_WORKER_TABLE);
		db.execSQL(CREATE_ATTENDANCE_TABLE);
		db.execSQL(CREATE_SALARY_TABLE);
		db.execSQL(CREATE_VACATION_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + WORKER_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ATTENDANCE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SALARY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + VACATION_TABLE);
		onCreate(db);
	}
}
