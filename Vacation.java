package com.example.mbg;

import java.io.Serializable;

public class Vacation implements Serializable {
private int id;
private int worker_id;
private String vacation_type;
private String vacation_period_start;
private String vacation_period_end;

Boolean isSelected;

public Vacation()
{}
public Vacation(int vacation_worker_id, String vacation_type,
		String vacation_period_start, String vacation_period_end) {
	// 
	this.isSelected = false;
	this.worker_id=vacation_worker_id;
	this.vacation_type = vacation_type;
	this.vacation_period_start = vacation_period_start;
	this.vacation_period_end = vacation_period_end;
}
public Vacation(int id,int vacation_worker_id, String vacation_type,
		String vacation_period_start, String vacation_period_end) {
	// 
	this.id = id;
	this.isSelected = false;
	this.worker_id=vacation_worker_id;
	this.vacation_type = vacation_type;
	this.vacation_period_start = vacation_period_start;
	this.vacation_period_end = vacation_period_end;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getWorker_id() {
	return worker_id;
}
public void setWorker_id(int worker_id) {
	this.worker_id = worker_id;
}
public String getVacation_type() {
	return vacation_type;
}
public void setVacation_type(String vacation_type) {
	this.vacation_type = vacation_type;
}
public String getVacation_period_start() {
	return vacation_period_start;
}
public void setVacation_period_start(String vacation_period_start) {
	this.vacation_period_start = vacation_period_start;
}
public String getVacation_period_end() {
	return vacation_period_end;
}
public void setVacation_period_end(String vacation_period_end) {
	this.vacation_period_end = vacation_period_end;
}
public boolean isSelected() {
	// TODO Auto-generated method stub
	return this.isSelected;
}
public void setSelected(boolean b) {
	// TODO Auto-generated method stub
	this.isSelected = b;
}

}
