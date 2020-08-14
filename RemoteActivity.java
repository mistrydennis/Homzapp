package com.example.mbg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class RemoteActivity extends BaseRemoteActivity{
	 ArrayList<Worker> workers;
	 ArrayList<WorkerSalary> workers_salary;
	 ArrayList<WorkerAttendance> workers_attendance;
	 ArrayList<Vacation> vacations;
	 SQLController dbController;
	@Override
	public void onDeviceResponse(boolean success) {
		// TODO Auto-generated method stub
		//byte[] bytes = ("Response - " + success).getBytes();
		//sendMessage(bytes);
		//Toast.makeText(getApplicationContext(),"Response - " + success, Toast.LENGTH_LONG).show();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_remote_activity);
		
		workers = new ArrayList<Worker>();
		vacations = new ArrayList<Vacation>();
		 workers_attendance = new ArrayList<WorkerAttendance>();
		workers_salary = new ArrayList<WorkerSalary>();
		dbController = new SQLController(getApplicationContext());
		
		Button btnDevices =(Button)findViewById(R.id.btDiscover);
		btnDevices.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				requestDeviceConnection();
			}
		});
		Button btnSync = (Button)findViewById(R.id.btnSync);
		btnSync.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				getData();
			   for(Worker worker : workers){
				 try {
					SyncObject obj = new SyncObject("w",serialize(worker));
					sendMessage(serialize(obj));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("Serialize","worker");
				}   
			   }
				
			   for(Vacation vacation : vacations){
					 try {
						SyncObject obj = new SyncObject("v",serialize(vacation));
						sendMessage(serialize(obj));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("Serialize","worker");
					}   
				   }
			}

			
		});
		
        
	}
	public void getData(){
		dbController = new SQLController(getApplicationContext());
		dbController.open();
		Cursor workerCursor = dbController.fetchWorkers();
		Worker w = new Worker();
		if(workerCursor.moveToFirst()){
		do{
			final int id = workerCursor.getInt(workerCursor.getColumnIndex(DBhelper.WORKER_ID));
        	final String name = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_NAME));
        	final String image = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_IMAGE));
    		final String contact = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_PCONTACT));
    		final String scontact = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_SCONTACT));
    		final String address = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_ADDRESS));
    		final String profession = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_PROFESSION));
    		final String rate = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_RATE));
    		final String type = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_TYPE));
        	
    		w = new Worker(id,name,image,contact,scontact,address,profession,rate,type);
			workers.add(w);
		}
		while(workerCursor.moveToNext());
		}
		Cursor vacationCursor = dbController.fetchVacations();
		if (vacationCursor.moveToFirst()) {
			
			do {
				final int id = vacationCursor.getInt(vacationCursor.getColumnIndex(DBhelper.VACATION_ID));
				final int vacation_worker_id = vacationCursor.getInt(vacationCursor.getColumnIndex(DBhelper.VACATION_WORKER_ID));
				final String vacation_type = vacationCursor.getString(vacationCursor.getColumnIndex(DBhelper.VACATION_TYPE));
				final String vacation_period_start = vacationCursor.getString(vacationCursor.getColumnIndex(DBhelper.VACATION_PERIOD_START));
				final String vacation_period_end = vacationCursor.getString(vacationCursor.getColumnIndex(DBhelper.VACATION_PERIOD_END));
				

				Vacation vacationTemp = new Vacation(id,vacation_worker_id, vacation_type, vacation_period_start,vacation_period_end);
				vacations.add(vacationTemp);
			}while(vacationCursor.moveToNext());
		}
	}
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	@Override
	public void onWriteSuccess(String message) {
		TextView txtSent = (TextView)findViewById(R.id.txtSent);
		txtSent.setText("SENT");
	}

}
