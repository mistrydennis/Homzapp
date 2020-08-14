package com.example.mbg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLController {

	private DBhelper dbHelper;
	private Context ourcontext;
	private SQLiteDatabase database;

	public SQLController(Context c) {
		ourcontext = c;
	}
	/*public int getVacationId(Vacation vacation){
		int id;
		String[] columns = new String[] {
				DBhelper.VACATION_ID, 
				DBhelper.VACATION_WORKER_ID,
				DBhelper.VACATION_TYPE,
				DBhelper.VACATION_PERIOD_START,
				DBhelper.VACATION_PERIOD_END,
		};		
		String whereClause=""+DBhelper.ATTENDANCE_WORKER_ID+"=? and "+DBhelper.ATTENDANCE_DAY+"=?";
		String[] whereArgs = new String[]{
				""+id,
				sdf.format(date).toString()
		};
		Cursor cursor = database.query(DBhelper.VACATION_TABLE, columns, null,
				null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return id;
	}*/

	public SQLController open() throws SQLException {
		dbHelper = new DBhelper(ourcontext);
		database = dbHelper.getWritableDatabase();
		return this;

	}

	public void close() {
		dbHelper.close();
	}
	
	public int insertWorkerSalary(WorkerSalary salary)
	{
		int count=0;
		ContentValues values = new ContentValues();
		values.put(DBhelper.SALARY_WORKER_ID,salary.getWorkerId());
		values.put(DBhelper.SALARY_PAID,salary.getSalary());
		values.put(DBhelper.SALARY_PERIOD_START,salary.getPeriodStart());
		values.put(DBhelper.SALARY_PERIOD_END,salary.getPeriodEnd());
		
		count = (int)database.insert(DBhelper.SALARY_TABLE, null, values);
		return count;
	}
	public int insertAttendance(WorkerAttendance attendance)
	{
		ContentValues values = new ContentValues();
		values.put(DBhelper.ATTENDANCE_ATTEND, attendance.getPresent());
		values.put(DBhelper.ATTENDANCE_DAY, attendance.getDay());
		values.put(DBhelper.ATTENDANCE_TYPE, attendance.getDayType());
		values.put(DBhelper.ATTENDANCE_WORKER_ID,attendance.getWorkerId());
		values.put(DBhelper.ATTENDANCE_OVERTIME, attendance.getOvertime());
		//int i = database.update(DBhelper.ATTENDANCE_TABLE, values,DBhelper.ATTENDANCE_ID + " = " + id, null);
		int i = (int) database.insert(DBhelper.ATTENDANCE_TABLE,null, values);
		return i;
		//database.execSQL("UPDATE TABLE worker SET present="+present+", day_type="+type+" WHERE id="+id);
	}
	public int insertDummyWorker() {
		int count = 0;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('William Shepard','worker_dp','1134567890','9987409697','est tristique tincidunt','Vegetable Vendor',558.62,'W')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Abraham Waters','worker_dp','2234567890','9987409696','scelerisque laoreet Nunc','Vegetable Vendor',1653.08,'D')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Plato Hyde','worker_dp','3334567890','9987409692','Phasellus consequat fermentum','Milkman',229.87,'M')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Noble Carter','worker_dp','1434567890','9987409692','odio scelerisque volutpat','Milkman',4200.45,'M')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Blake Sweet','worker_dp','1534567890','9987409692','fermentum facilisis vitae','Maid',100.75,'D')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Joan Foreman','worker_dp','1634567890','9987409692','facilisis neque ullamcorper','Milkman',500,'W')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Lawrence Talley','worker_dp','1734567890','9987409692','auctor lacinia viverra','Newspaper',20.00,'D')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Constance Puckett','worker_dp','1834567890','9987409692','convallis nibh Curae','Newspaper',300.00,'W')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Kathleen Buckley','worker_dp','1934567890','9987409692','consequat risus nibh','Vegetable Vendor',50.00,'D')"); 
		count++;
		database.execSQL("INSERT INTO worker (`name`,`wimage`,`pcontact`,`scontact`,`address`,`profession`,`rate`,`type`) VALUES ('Erin Frederick','worker_dp','1034567890','9987419692','dictum et mattis','Maid',5000.00,'M')"); 
		count++;
		return count;
	}

	public long insertWorker(Worker worker) {
		ContentValues contentValue = new ContentValues();
		contentValue.put(DBhelper.WORKER_NAME, worker.getName());
		contentValue.put(DBhelper.WORKER_IMAGE, worker.getImage());
		contentValue.put(DBhelper.WORKER_PCONTACT, worker.getPrimaryContact());
		contentValue.put(DBhelper.WORKER_SCONTACT, worker.getSecondaryContact());
		contentValue.put(DBhelper.WORKER_ADDRESS, worker.getAddress());
		contentValue.put(DBhelper.WORKER_PROFESSION, worker.getProfession());
		contentValue.put(DBhelper.WORKER_RATE, worker.getRate());
		contentValue.put(DBhelper.WORKER_TYPE, worker.getType());
		return database.insert(DBhelper.WORKER_TABLE, null, contentValue);
	}
	
	public ArrayList<Integer> getAttendanceForDay(Date date)
	 {
		 ArrayList<Integer> attendance = new ArrayList<Integer>();
		
		
		 Cursor cursor =fetchWorkerAttendanceIds(date);
		 		 
		 if (cursor.moveToFirst()) {
		    	System.out.println("Entered again");
		 Log.d("Cursor Count",""+cursor.getCount());
		 //cursor.moveToNext();
		 int i = 0;
		        do{
		        	
		        	final int id = cursor.getInt(cursor.getColumnIndex(DBhelper.ATTENDANCE_WORKER_ID));
		        	Log.d("Cursor value",""+id);
		        	attendance.add(id);		    	
		        	i++;
		        	//cursor.moveToNext();
		        }while (cursor.moveToNext());
		       
		   }
		 return attendance;
	 }

	public Cursor fetchAttendance(int id,Date date){
		String []columns = new String[]{
				DBhelper.ATTENDANCE_ID,
				DBhelper.ATTENDANCE_WORKER_ID,
				DBhelper.ATTENDANCE_DAY,
				DBhelper.ATTENDANCE_ATTEND,
				DBhelper.ATTENDANCE_OVERTIME,
				DBhelper.ATTENDANCE_TYPE,
		};
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String whereClause=""+DBhelper.ATTENDANCE_WORKER_ID+"=? and "+DBhelper.ATTENDANCE_DAY+"=?";
		String[] whereArgs = new String[]{
				""+id,
				sdf.format(date).toString()
		};
		Cursor cursor = database.query(DBhelper.ATTENDANCE_TABLE, columns,whereClause, whereArgs, null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	private Cursor fetchWorkerAttendanceIds(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String[] columns = new String[]{DBhelper.ATTENDANCE_WORKER_ID};
		String whereClause =""+DBhelper.ATTENDANCE_DAY+"=?";
		String[] whereArgs = new String[]{sdf.format(date).toString()};
		Cursor cursor = database.query(DBhelper.ATTENDANCE_TABLE,columns,whereClause,whereArgs,null,null,null);
		
		if(cursor!=null){
			cursor.moveToFirst();
		}
		return cursor;
	}
	public Cursor fetchWorkers() {
		String[] columns = new String[] { DBhelper.WORKER_ID, 
				DBhelper.WORKER_NAME,
				DBhelper.WORKER_IMAGE,
				DBhelper.WORKER_PCONTACT,
				DBhelper.WORKER_SCONTACT,
				DBhelper.WORKER_ADDRESS,
				DBhelper.WORKER_PROFESSION,
				DBhelper.WORKER_RATE,
				DBhelper.WORKER_TYPE};
		
		Cursor cursor = database.query(DBhelper.WORKER_TABLE, columns, null,
				null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	public int fetchDuplicateWorker(Worker w){
		int count=0;
		String[] columns = new String[] { DBhelper.WORKER_ID, 
				DBhelper.WORKER_NAME,
				DBhelper.WORKER_IMAGE,
				DBhelper.WORKER_PCONTACT,
				DBhelper.WORKER_SCONTACT,
				DBhelper.WORKER_ADDRESS,
				DBhelper.WORKER_PROFESSION,
				DBhelper.WORKER_RATE,
				DBhelper.WORKER_TYPE};
		String whereClause = ""+DBhelper.WORKER_NAME+"=? and "+DBhelper.WORKER_ADDRESS+"=? and "+DBhelper.WORKER_TYPE+"=?";
		String whereArgs[] = new String[]{w.getName(),w.getAddress(),w.getType()};
		Cursor cursor = database.query(DBhelper.WORKER_TABLE, columns, whereClause,
				whereArgs, null, null, null);
		count = cursor.getCount();
		return count;
	}

	public int fetchDuplicateVacation(Vacation v){
		int count = 0;
		String[] columns = new String[] { DBhelper.VACATION_ID, 
				DBhelper.VACATION_WORKER_ID,
				DBhelper.VACATION_TYPE,
				DBhelper.VACATION_PERIOD_START,
				DBhelper.VACATION_PERIOD_END,
		};		
		String whereClause = ""+DBhelper.VACATION_WORKER_ID+"=? and "+DBhelper.VACATION_TYPE+"=? and "+DBhelper.VACATION_PERIOD_START+"=? and "+DBhelper.VACATION_PERIOD_END+"=?";
		String whereArgs[] = new String[]{""+v.getWorker_id(),v.getVacation_type(),v.getVacation_period_start(),v.getVacation_period_end()};
		Cursor cursor = database.query(DBhelper.VACATION_TABLE, columns, whereClause,
				whereArgs, null, null, null);
		count = cursor.getCount();
		return count;
	}
	
	public int fetchDuplicateAttendance(WorkerAttendance attendance){
		String []columns = new String[]{
				DBhelper.ATTENDANCE_ID,
				DBhelper.ATTENDANCE_WORKER_ID,
				DBhelper.ATTENDANCE_DAY,
				DBhelper.ATTENDANCE_ATTEND,
				DBhelper.ATTENDANCE_OVERTIME,
				DBhelper.ATTENDANCE_TYPE,
		};
		String whereClause=""+DBhelper.ATTENDANCE_WORKER_ID+"=? and "+DBhelper.ATTENDANCE_DAY+"=?";
		String[] whereArgs = new String[]{
				""+attendance.getWorkerId(),
				attendance.getDay()
		};
		Cursor cursor = database.query(DBhelper.ATTENDANCE_TABLE, columns,whereClause, whereArgs, null, null, null);
		int count = 0;
		count = cursor.getCount();
		return count;
	}
	public int fetchDuplicateSalary(WorkerSalary salary){
		int count=0;
		String []columns = new String[]{
				DBhelper.SALARY_WORKER_ID,
				DBhelper.SALARY_PERIOD_START,
				DBhelper.SALARY_PERIOD_END,
		};
		String whereClause = ""+DBhelper.SALARY_WORKER_ID+"=? and "+DBhelper.SALARY_PERIOD_START+"=? and "+DBhelper.SALARY_PERIOD_END+"=?";
		String whereArgs[] = new String[]{""+salary.getWorkerId(),salary.getPeriodStart(),salary.getPeriodEnd()};
		Cursor cursor = database.query(DBhelper.SALARY_TABLE, columns, whereClause,whereArgs, null, null, null);
		count = cursor.getCount();
		return count;
	}
	
	public int updateWorker(Worker worker) {
		ContentValues contentValue = new ContentValues();
		contentValue.put(DBhelper.WORKER_NAME, worker.getName());
		contentValue.put(DBhelper.WORKER_IMAGE, worker.getName());
		contentValue.put(DBhelper.WORKER_PCONTACT, worker.getPrimaryContact());
		contentValue.put(DBhelper.WORKER_SCONTACT, worker.getSecondaryContact());
		contentValue.put(DBhelper.WORKER_ADDRESS, worker.getAddress());
		contentValue.put(DBhelper.WORKER_PROFESSION, worker.getProfession());
		contentValue.put(DBhelper.WORKER_RATE, worker.getRate());
		contentValue.put(DBhelper.WORKER_TYPE, worker.getType());
		
		int i = database.update(DBhelper.WORKER_TABLE, contentValue,
				DBhelper.WORKER_ID + " = " + worker.getId(), null);
		return i;
	}

	public void deleteWorker(long _id) {
		database.delete(DBhelper.WORKER_TABLE, DBhelper.WORKER_ID + "=" + _id, null);
	}

	public int updateVacation(Vacation vacation) {
		ContentValues contentValue = new ContentValues();
		contentValue.put(DBhelper.VACATION_WORKER_ID, vacation.getWorker_id());
		contentValue.put(DBhelper.VACATION_TYPE, vacation.getVacation_type());
		contentValue.put(DBhelper.VACATION_PERIOD_START, vacation.getVacation_period_start());
		contentValue.put(DBhelper.VACATION_PERIOD_END, vacation.getVacation_period_end());
		Log.d("update: ",""+vacation.getId());
		int i = database.update(DBhelper.VACATION_TABLE, contentValue,
				DBhelper.VACATION_ID + " = " + vacation.getId(), null);
		return i;
	}
	public int insertVacation(Vacation vacation){
		ContentValues contentValue = new ContentValues();
		contentValue.put(DBhelper.VACATION_WORKER_ID,vacation.getWorker_id());
		contentValue.put(DBhelper.VACATION_TYPE, vacation.getVacation_type());
		contentValue.put(DBhelper.VACATION_PERIOD_START, vacation.getVacation_period_start());
		contentValue.put(DBhelper.VACATION_PERIOD_END, vacation.getVacation_period_end());
		return (int) database.insert(DBhelper.VACATION_TABLE, null, contentValue);
		
	}
	
	public Cursor fetchVacations() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { DBhelper.VACATION_ID, 
				DBhelper.VACATION_WORKER_ID,
				DBhelper.VACATION_TYPE,
				DBhelper.VACATION_PERIOD_START,
				DBhelper.VACATION_PERIOD_END,
		};		
		Cursor cursor = database.query(DBhelper.VACATION_TABLE, columns, null,
				null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	public long getSalaryById(int id)
	{
		long salary;
		String columns[] = new String[]{"sum("+DBhelper.SALARY_PAID+")"};
		
		Calendar date = Calendar.getInstance();
		String startDate = ""+01+"/"+(date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.YEAR);
		String endDate = ""+date.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+(date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.YEAR);
		String whereArgs[] = new String[]{""+id,startDate,endDate};
		String whereClause=""+DBhelper.SALARY_WORKER_ID+"=? and period_start >=? and period_end <=?";
		Cursor cursor = database.query(DBhelper.SALARY_TABLE,columns,null,null,null, null, null);
		cursor.moveToFirst();
		salary = cursor.getLong(0);
		return salary;
	}

	public void deleteVacation(Vacation vacation) {
		// TODO Auto-generated method stub
		database.delete(DBhelper.VACATION_TABLE, DBhelper.VACATION_ID + "=" + vacation.getId(), null);
	}

	public ArrayList<Vacation> fetchCurrentVacations(Date date) {
		// TODO Auto-generated method stub
		ArrayList<Vacation> vacations = new ArrayList<Vacation>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dt = sdf.format(date).toString();
		String columns[] = new String[]{DBhelper.VACATION_ID,
				DBhelper.VACATION_WORKER_ID,
				DBhelper.VACATION_PERIOD_START,
				DBhelper.VACATION_PERIOD_END,
				DBhelper.VACATION_TYPE};
		String whereClause = ""+DBhelper.VACATION_PERIOD_START+"<=? and "+DBhelper.VACATION_PERIOD_END+">=?";
		String whereArgs[] = new String[]{dt,dt};
		Cursor cursor = database.query(DBhelper.VACATION_TABLE,columns,whereClause, whereArgs, null, null, null, null);
		
		if (cursor.moveToFirst()) {
	    	System.out.println("Entered");
	        do {
	        	final int id = cursor.getInt(cursor.getColumnIndex(DBhelper.VACATION_ID));
	        	final int worker_id = cursor.getInt(cursor.getColumnIndex(DBhelper.VACATION_WORKER_ID));
	        	final String startDate = cursor.getString(cursor.getColumnIndex(DBhelper.VACATION_PERIOD_START));
	        	final String endDate = cursor.getString(cursor.getColumnIndex(DBhelper.VACATION_PERIOD_END));
	        	final int type = cursor.getInt(cursor.getColumnIndex(DBhelper.VACATION_TYPE));
	        	
	        	Vacation tempVacation = new Vacation(id,worker_id,""+type,startDate,endDate);
	        	vacations.add(tempVacation);
	  
	        } while (cursor.moveToNext());
		}
		return vacations;
	}
}
