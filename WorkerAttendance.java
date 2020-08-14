package com.example.mbg;

import java.util.Calendar;

import android.content.Context;

public class WorkerAttendance {
	int id;
	String day;
	String workerId;
	String dayType;
	String present;
	String overtime;
	
	public WorkerAttendance() {
		
	}
	
	public WorkerAttendance( String day, String workerId,
			String dayType, String present, String overtime) {
		super();
		
		this.day = day;
		this.workerId = workerId;
		this.dayType = dayType;
		this.present = present;
		this.overtime = overtime;
	}
	
	public WorkerAttendance(int id, String day, String workerId,
			String dayType, String present, String overtime) {
		super();
		this.id = id;
		this.day = day;
		this.workerId = workerId;
		this.dayType = dayType;
		this.present = present;
		this.overtime = overtime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getDayType() {
		return dayType;
	}

	public void setDayType(String dayType) {
		this.dayType = dayType;
	}

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}
	
	public void addDummyAttendance(Context context,Worker worker)
	{
		int id = worker.getId();
		String type=worker.getType();
		Calendar c= Calendar.getInstance(),cal = Calendar.getInstance();
		WorkerAttendance attendance;
		SQLController dbController = new SQLController(context);
		if(type.equalsIgnoreCase("d")) {
			
       }
       else if(type.equalsIgnoreCase("w")) {
			for(int i=(cal.get(Calendar.DAY_OF_WEEK)-1),j=1;i>Calendar.SUNDAY;i--,j++)
			{
				c= Calendar.getInstance();
				attendance = new WorkerAttendance();
				c.add(Calendar.DATE, -j);
				String date = ""+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
				attendance.setDay(date);
				attendance.setWorkerId(""+id);
				attendance.setDayType(""+1);
				attendance.setPresent(""+1);
				attendance.setOvertime(""+0);
				dbController.open();
				dbController.insertAttendance(attendance);
			}
       }
       else { 
    	   for(int i=(cal.get(Calendar.DAY_OF_MONTH)-1),j=1;i>0;i--,j++)
			{
    		   c= Calendar.getInstance();
				attendance = new WorkerAttendance();
				c.add(Calendar.DATE, -j);
				String date = ""+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
				attendance.setDay(date);
				attendance.setWorkerId(""+id);
				attendance.setDayType(""+1);
				attendance.setPresent(""+1);
				attendance.setOvertime(""+0);
				dbController.open();
				dbController.insertAttendance(attendance);
			}
       }
       
	}
	
}
