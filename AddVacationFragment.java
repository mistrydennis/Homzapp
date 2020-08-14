package com.example.mbg;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


public class AddVacationFragment extends Fragment{
	public AddVacationFragment() { }
	private View rootView;
	private ArrayList<Worker> workerList;
	private SQLController dbController;
	private	Context context;
	private int year,month,day;
	private EditText txtStartDate;
    private EditText txtEndDate;
    private Spinner spnWorker;
    private ArrayAdapter<Worker> workerAdapter;
    private RadioButton rdoPaid;
	private RadioButton rdoUnpaid;
	int id;
	private int flag;	
	
	

	private Worker getWorkerById(int id)
	{
		Worker worker=new Worker();
		for(Worker w : workerList)
		{
			if(w.getId()==id)
			{
				worker = w;
			}
		}
		return worker;
	}
	private void makeUpdateForm(Bundle args) {
		  Vacation vac = (Vacation) args.getSerializable("vacation");
		  id = vac.getId();
		   Log.d("vacation", ""+vac.getWorker_id());
		   fillForm(vac);		
		    }
	   
   private void fillForm(Vacation vac) {
		   Worker worker = getWorkerById(vac.getWorker_id());
		   spnWorker.setSelection(workerAdapter.getPosition(worker));
		   txtStartDate.setText(vac.getVacation_period_start());
		   txtEndDate.setText(vac.getVacation_period_end());
		   String type = vac.getVacation_type();
		   if(type.equalsIgnoreCase("0") ){
			   rdoPaid.setChecked(true);   
		   } else if(type.equalsIgnoreCase("1") ){
			   rdoUnpaid.setChecked(true);
		   }
		   
	   }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.addvacation, container, false);
		
		Bundle arguments = getArguments();
		   
		  	
		
		getWorkersFromDB(rootView.getContext());
		this.context = rootView.getContext();
		spnWorker = (Spinner)rootView.findViewById(R.id.spnVacationWorker);
		workerAdapter = new ArrayAdapter<Worker>(rootView.getContext(), android.R.layout.simple_spinner_item, workerList);
        spnWorker.setAdapter(workerAdapter);
        txtStartDate = (EditText)rootView.findViewById(R.id.txtStartDate);
        txtEndDate = (EditText)rootView.findViewById(R.id.txtEndDate);       
        
        rdoPaid = (RadioButton)rootView.findViewById(R.id.rdoPaid);
		rdoUnpaid = (RadioButton)rootView.findViewById(R.id.rdoUnpaid);
        
        if(arguments != null) {
			   makeUpdateForm(arguments);
			   flag = 0;
		   }
		   else {
			   flag = 1;
		   }	
        Button btnSubmit = (Button)rootView.findViewById(R.id.btnSubmit_vacation);
        
      
        txtStartDate.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				Validation.isDate(txtStartDate, true);
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {}
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}
        	
        });
        txtEndDate.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Validation.isDate(txtEndDate, true);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
        	
        });
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Worker worker = (Worker)spnWorker.getSelectedItem();
				String strType = "";
				
				if(rdoUnpaid.isChecked())
				{
					strType = ""+1;
				}
				else
				{
					strType = ""+0;
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String startDate="",endDate="";
				
				startDate=txtStartDate.getText().toString();
				endDate = txtEndDate.getText().toString();
	if(flag==1){
				if(checkValidation())
				{
					Vacation tempVacation = new Vacation(worker.getId(),strType,startDate,endDate);
					dbController = new SQLController(context);
				    dbController.open();
					if(dbController.insertVacation(tempVacation)>0)
					{
						Toast t = Toast.makeText(context,"Added Successfully", Toast.LENGTH_LONG);
						t.show();
					}
					else
					{
						Toast t = Toast.makeText(context,"Database Error", Toast.LENGTH_LONG);
						t.show();
					}
					dbController.close();
				}
				else
				{
					Toast t = Toast.makeText(context,"Form Contains Error!", Toast.LENGTH_LONG);
					t.show();
				}
			}
			else{
				//update
				if(checkValidation())
				{
					Vacation tempVacation = new Vacation(id,worker.getId(),strType,startDate,endDate);
					dbController = new SQLController(context);
				    dbController.open();
					if(dbController.updateVacation(tempVacation)>0)
					{
						Toast t = Toast.makeText(context,"Updated Successfully", Toast.LENGTH_LONG);
						t.show();
						Fragment fragment = new VacationFragment();					
						FragmentManager fm = getFragmentManager();
						fm.beginTransaction()
				        .replace(R.id.content_frame, fragment).commit();
					}
					else
					{
						Toast t = Toast.makeText(context,"Database Error", Toast.LENGTH_LONG);
						t.show();
					}
					dbController.close();
				}
				else
				{
					Toast t = Toast.makeText(context,"Form Contains Error!", Toast.LENGTH_LONG);
					t.show();
				}
			}
			}
		});
        
		return rootView;
	}

	private boolean checkValidation() {
	       boolean ret = true;

	       if (!Validation.isDate(txtStartDate, true)) ret = false;
	       if (!Validation.isDate(txtEndDate, true)) ret = false;
	       
	       return ret;
	   }
	    
	
	private void getWorkersFromDB(Context context) {
		dbController = new SQLController(context);
	    dbController.open();
	    Cursor workerCursor = dbController.fetchWorkers();
	    dbController.close();
	    workerList = new ArrayList<Worker>();
	   
	    if (workerCursor.moveToFirst()) {
	    	System.out.println("Entered");
	        do {
	        	final int id = workerCursor.getInt(workerCursor.getColumnIndex(DBhelper.WORKER_ID));
	        	final String name = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_NAME));
	        	final String image = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_IMAGE));
	    		final String contact = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_PCONTACT));
	    		final String scontact = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_SCONTACT));
	    		final String address = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_ADDRESS));
	    		final String profession = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_PROFESSION));
	    		final String rate = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_RATE));
	    		final String type = workerCursor.getString(workerCursor.getColumnIndex(DBhelper.WORKER_TYPE));
	        	
	    		Worker workerTemp = new Worker(id,name,image,contact,scontact,address,profession,rate,type);
	    		workerList.add(workerTemp);
	    	
	        } while (workerCursor.moveToNext());
	       
	    }
	}
}
