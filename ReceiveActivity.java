package com.example.mbg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;



public class ReceiveActivity extends BaseDeviceActivity {
	
	SQLController dbController;
	private ArrayList<Worker> workers;
	private ArrayList<Vacation> vacations;
	private ArrayList<WorkerSalary> salary;
	private ArrayList<WorkerAttendance> attendance;
	
	private static final String TAG = "BluetoothDeviceActivity";
	
	private TextView mDeviceStatusText;
	int count;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_receive_activity);
        workers =new ArrayList<Worker>();
        vacations = new ArrayList<Vacation>();
        salary = new ArrayList<WorkerSalary>();
        attendance = new ArrayList<WorkerAttendance>();
        count=1;
    }

	@Override
	public void onNewMessage(byte[] bytes) {
		TextView txtReceive = (TextView)findViewById(R.id.txtReceive);
		
		try {
			SyncObject obj = (SyncObject)deserialize(bytes);
			if(obj.getTag().equalsIgnoreCase("w")){
				Worker w = (Worker)deserialize(obj.getBytes());
				workers.add(w);
			}
			else if(obj.getTag().equalsIgnoreCase("v")){
				Vacation v = (Vacation)deserialize(obj.getBytes());
				vacations.add(v);
			}
			else if(obj.getTag().equalsIgnoreCase("a")){
				WorkerAttendance a = (WorkerAttendance)deserialize(obj.getBytes());
				attendance.add(a);
			}
			else if(obj.getTag().equalsIgnoreCase("s")){
				WorkerSalary s = (WorkerSalary)deserialize(obj.getBytes());
				salary.add(s);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		count++;
		txtReceive.setText("Adding Records");
		int c = insertData();
		txtReceive.setText(count+ "Added");
	}
	
	private int insertData() {
	// TODO Auto-generated method stub
		int count=0;
		dbController = new SQLController(getApplicationContext());
		dbController.open();
		for(Worker worker: workers){
			if(dbController.fetchDuplicateWorker(worker)==0){
				dbController.insertWorker(worker);
				count++;
			}
		}
		
		for(Vacation vacation:vacations){
			if(dbController.fetchDuplicateVacation(vacation)==0){
				dbController.insertVacation(vacation);
				count++;
			}
		}
		
		for(WorkerSalary s: salary){
			if(dbController.fetchDuplicateSalary(s)==0){
				dbController.insertWorkerSalary(s);
				count++;
			}
		}
		for(WorkerAttendance a: attendance){
			if(dbController.fetchDuplicateAttendance(a)==0){
				dbController.insertAttendance(a);
				count++;
			}
		}
		dbController.close();
		return count;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        
        return true;
    }

}