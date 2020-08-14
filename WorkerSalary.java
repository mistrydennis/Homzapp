package com.example.mbg;

public class WorkerSalary {

	private int worker_id;
	private String period_start;
	private String period_end;
	private String salary;
	
	public WorkerSalary(){}
	public WorkerSalary(int id,String period_start,String period_end,String salary){
		this.worker_id = id;
		this.period_start = period_start;
		this.period_end = period_end;
		this.salary = salary;
	}
	
	public void setSalary(String salary)
	{
		this.salary = salary;
	}
	public void setWorkerId(int id)
	{
		this.worker_id=id;
	}
	public void setPeriodStart(String period_start)
	{
		this.period_start=period_start;
	}
	public void setPeriodEnd(String period_end)
	{
		this.period_end=period_end;
	}
	
	public String getSalary()
	{
		return this.salary;
	}
	public int getWorkerId()
	{
		return this.worker_id;
	}
	public String getPeriodStart()
	{
		return this.period_start;
	}
	public String getPeriodEnd()
	{
		return this.period_end;
	}
}
