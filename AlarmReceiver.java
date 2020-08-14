package com.example.mbg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{

	private Context context;
	private SQLController dbController;
	
	 private ArrayList<Integer> attend_for_day;
	 private ArrayList<Integer> worker_ids;
	 private ArrayList<Worker> daily_workers;
	 private ArrayList<Worker> monthly_workers;
	 private ArrayList<Worker> weekly_workers;
	 private ArrayList<Vacation> vacationList;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context = context;
		Bundle bundle = intent.getExtras();
		int id = bundle.getInt("id");
		Log.d("Alarm id: ",""+id);
		if(id==0){
		getWorkerIdsFromDb();
		markAttendance();
		updateSalary();
		}
		else if(id==1)
		{
			
			markVacations();
		
		}
		
	}
	private void markVacations() {
		// TODO Auto-generated method stub
		getVacations();
	dbController = new SQLController(context);
	dbController.open();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	WorkerAttendance attendance = new WorkerAttendance();
		for(Vacation vacation: vacationList)
		{
			attendance.setDay(sdf.format(new Date()).toString());
			attendance.setDayType(vacation.getVacation_type());
			attendance.setOvertime(""+0);
			attendance.setPresent(""+1);
			attendance.setWorkerId(""+vacation.getWorker_id());
			dbController.insertAttendance(attendance);
		}
		dbController.close();
	}
	private void getVacations()
	{
		dbController = new SQLController(context);
		dbController.open();	
		vacationList = dbController.fetchCurrentVacations(new Date());
		dbController.close();
		
	}
	private void updateSalary()
	{
		dbController = new SQLController(context);
		dbController.open();
		Cursor cursor;
		//daily
		for(int i=0;i<daily_workers.size();i++)
		{ 
			float salary_amount=0;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String period_start = (sdf.format(new Date()));
			String period_end = (sdf.format(new Date()));
			
			cursor = dbController.fetchAttendance((daily_workers.get(i)).getId(), new Date());
			cursor.moveToFirst();
			Log.d("daily - No. of Attendance for id:"+daily_workers.get(i).getId(),""+cursor.getCount());
			int present = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_ATTEND)));
			int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_TYPE)));
			if(present==0&&type==0)
			{
				//Log.d("Rate "+daily_workers.get(i).getId()+": ",daily_workers.get(i).getRate());
				salary_amount=Float.parseFloat(daily_workers.get(i).getRate().toString());
				salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
			}
			else if(present==2&&type==0)
			{
				salary_amount=Float.parseFloat(daily_workers.get(i).getRate().toString());
				salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
			}
			else if(present==2&&type==1)
			{
				salary_amount=Float.parseFloat(daily_workers.get(i).getRate().toString());
				salary_amount/=2;
				salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
			}
			else if(present==0&&type==1||present==1)
			{
				salary_amount =0 ;
			}
			WorkerSalary salary = new WorkerSalary();
			salary.setWorkerId(daily_workers.get(i).getId());
			salary.setSalary(""+salary_amount);
			salary.setPeriodStart(period_start);
			salary.setPeriodEnd(period_end);
			dbController.insertWorkerSalary(salary);
			
		}
		
		//weekly
		Calendar c,cal = Calendar.getInstance();
		String date;
		Date dt=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.SUNDAY){
		    	//Log.d("Day: ","sunday");
			String period_end = (sdf.format(new Date()));
			for(int i=0;i<weekly_workers.size();i++)
			{
				float salary_amount=0;
				for(int j=0;j<=day;j++)
				{
					c = Calendar.getInstance();
					c.add(Calendar.DATE, -j);
					date= ""+c.get(Calendar.DATE)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
					try{
					dt = sdf.parse(date);
					}
					catch(Exception e)
					{
						Log.d("Date Exception",""+e.getMessage());
					}
					
					cursor = dbController.fetchAttendance((weekly_workers.get(i)).getId(), dt);
					Log.d("weekly - No. of Attendance for id:"+weekly_workers.get(i).getId(),""+cursor.getCount());
					if(cursor.getCount()>0){
					cursor.moveToFirst();
					int present = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_ATTEND)));
					int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_TYPE)));
					if(present==0&&type==0)
					{
						Log.d("Rate weekly "+weekly_workers.get(i).getId()+": ",weekly_workers.get(i).getRate());
						salary_amount+=Float.parseFloat(weekly_workers.get(i).getRate().toString());
						salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
					}
					else if(present==2&&type==0)
					{
						salary_amount+=Float.parseFloat(weekly_workers.get(i).getRate().toString());
						salary_amount/=6;
						salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
					}
					else if(present==2&&type==1)
					{
						salary_amount+=Float.parseFloat(weekly_workers.get(i).getRate().toString());
						salary_amount/=6;
						salary_amount/=2;
						salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
					}
					else if(present==0&&type==1||present==1)
					{
						salary_amount =0 ;
					}
					}
				}
					
				String period_start = (sdf.format(dt));
				WorkerSalary salary = new WorkerSalary();
				salary.setWorkerId(weekly_workers.get(i).getId());
				salary.setSalary(""+salary_amount);
				salary.setPeriodStart(period_start);
				salary.setPeriodEnd(period_end);
				dbController.insertWorkerSalary(salary);
			}
		}
		
		//monthly
		c = cal = Calendar.getInstance();
		
		dt=new Date();
		
		day = cal.get(Calendar.DATE);
		if (day == cal.getActualMaximum(Calendar.DATE)){ //cal.getActualMaximum(Calendar.DATE)
		    	//
			String period_end = (sdf.format(new Date()));
			for(int i=0;i<monthly_workers.size();i++)
			{
				float salary_amount=0;
				for(int j=0;j<cal.getActualMaximum(Calendar.DATE);j++)
				{
					c = Calendar.getInstance();
					c.add(Calendar.DATE, -j);
					date= ""+c.get(Calendar.DATE)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
					try{
					dt = sdf.parse(date);
					}
					catch(Exception e)
					{
						Log.d("Date Exception",""+e.getMessage());
					}
					
					cursor = dbController.fetchAttendance((monthly_workers.get(i)).getId(), dt);
					Log.d("monthly - No. of Attendance for id:"+monthly_workers.get(i).getId(),""+cursor.getCount());
					if(cursor.getCount()>0){
					cursor.moveToFirst();
					int present = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_ATTEND)));
					int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_TYPE)));
					if(present==0&&type==0)
					{
						Log.d("Rate monthly "+monthly_workers.get(i).getId()+": ",monthly_workers.get(i).getRate());
						salary_amount+=Float.parseFloat(monthly_workers.get(i).getRate().toString());
						salary_amount/=cal.getActualMaximum(Calendar.DATE);
						salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
					}
					else if(present==2&&type==0)
					{
						salary_amount+=Float.parseFloat(monthly_workers.get(i).getRate().toString());
						cal.getActualMaximum(Calendar.DATE);
						salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
					}
					else if(present==2&&type==1)
					{
						salary_amount+=Float.parseFloat(monthly_workers.get(i).getRate().toString());
						cal.getActualMaximum(Calendar.DATE);
						salary_amount/=2;
						salary_amount+=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBhelper.ATTENDANCE_OVERTIME)));
					}
					else if(present==0&&type==1||present==1)
					{
						salary_amount =0 ;
					}
				}
				}
				String period_start = (sdf.format(dt));
				WorkerSalary salary = new WorkerSalary();
				salary.setWorkerId(monthly_workers.get(i).getId());
				salary.setSalary(""+salary_amount);
				salary.setPeriodStart(period_start);
				salary.setPeriodEnd(period_end);
				dbController.insertWorkerSalary(salary);
			}
		}
		dbController.close();
	}
	private void markAttendance()
	{	
		//testing
	       //new WorkerAttendance().addDummyAttendance(context, weekly_workers.get(1));
		//--------
		   dbController = new SQLController(context);
	       dbController.open();
	       attend_for_day=dbController.getAttendanceForDay(new Date());
	       dbController.close();
	       
	       final int size = worker_ids.size();
	       
	       WorkerAttendance attendance = new WorkerAttendance();
	       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
	       dbController.open();
	       for(int i=0;i<size;i++)
	       {
	    	   int wid = worker_ids.get(i);
	    	   if(!(attend_for_day.contains(wid)))
	    	   {
	    		   //code to add attendance for daily workers
	    		 //attendance.setDay(day)
	    		   attendance.setDay(""+sdf.format(new Date()));
	    		   attendance.setWorkerId(""+wid);
	    		   attendance.setPresent(""+0);
	    		   attendance.setDayType(""+0);
	    		   attendance.setOvertime(""+0);
	    		   dbController.insertAttendance(attendance);
	    	   }
	       }
	       dbController.close();
	       
	       
	}
	private void getWorkerIdsFromDb() {
		dbController = new SQLController(context);
	    dbController.open();
	    Cursor workerCursor = dbController.fetchWorkers();
	    dbController.close();
	    worker_ids = new ArrayList<Integer>();
	    daily_workers = new ArrayList<Worker>();
	    weekly_workers = new ArrayList<Worker>();
	    monthly_workers = new ArrayList<Worker>();
	    if (workerCursor.moveToFirst()) {
	    	
	        do {
	        	final int id = workerCursor.getInt(workerCursor.getColumnIndex(DBhelper.WORKER_ID));
	        	final String name = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_NAME));
	        	final String image = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_IMAGE));
	    		final String contact = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_PCONTACT));
	    		final String scontact = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_SCONTACT));
	    		final String address = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_ADDRESS));
	    		final String profession = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_PROFESSION));
	    		final String rate = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_RATE));
	    		String type = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_TYPE));
	    		Worker workerTemp = new Worker(id,name,image,contact,scontact,address,profession,rate,type);
	        		worker_ids.add(id);
	        		
	        		if(type.equalsIgnoreCase("d")) {
	    				daily_workers.add(workerTemp);
	    	       }
	    	       else if(type.equalsIgnoreCase("w")) {
	    	    	   weekly_workers.add(workerTemp);
	    	       }
	    	       else { 
	    	    	   monthly_workers.add(workerTemp);
	    	       }
	        } while (workerCursor.moveToNext());
	    }	    
	}
}
