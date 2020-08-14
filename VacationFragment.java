package com.example.mbg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class VacationFragment  extends Fragment {
	public VacationFragment() { }
	private View rootView;
	private Context context;
	private SQLController dbController;
	private VacationListAdapter vacationAdapter = new VacationListAdapter();
	private ArrayList<Vacation> vacationList = new ArrayList<Vacation>();
	private ListView vacationListView;
	private ArrayList<Worker> workersList = new ArrayList<Worker>(); 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		rootView = inflater.inflate(R.layout.vacation_layout, container, false);
		vacationList = new ArrayList<Vacation>();
		vacationListView = (ListView) rootView.findViewById(R.id.list_vacations);
		vacationAdapter = new VacationListAdapter(getActivity(),vacationList,workersList);
		vacationListView.setAdapter(vacationAdapter);
		context = rootView.getContext();

		Button btnAdd = (Button)rootView.findViewById(R.id.btnAdd_vacation);
		Button btnUpdate = (Button)rootView.findViewById(R.id.btnUpdate_vacation);
		Button btnDelete = (Button)rootView.findViewById(R.id.btnDelete_vacation);
		
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Fragment fragment = new AddVacationFragment();

				FragmentManager fm = getFragmentManager();
				fm.beginTransaction()
		        .replace(R.id.content_frame, fragment).commit();	
			}
		});
		
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(vacationAdapter.getSelectedItemsSize() == 0) 
				{
					Toast.makeText(getActivity(), "No Vacations Selected", Toast.LENGTH_LONG).show();
				}
				else if(vacationAdapter.getSelectedItemsSize()== 1) {
					
					Bundle bundles = new Bundle();
					
					if ( vacationAdapter.getSelectedIds().size() == 1 ) {
						Vacation vacation= vacationAdapter.getSelectedIds().get(0);
					    bundles.putSerializable("vacation", vacation);
					    Log.e("vacation in Update", "is valid");
					} else {
						bundles.putSerializable("vacation", null);	
					    Log.e("vacation in Update", "is null");
					}
					
					Fragment fragment = new AddVacationFragment();
					fragment.setArguments(bundles);
					
					FragmentManager fm = getFragmentManager();
					fm.beginTransaction()
			        .replace(R.id.content_frame, fragment).commit();
					
					
					Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_LONG).show();
					
				} else if(vacationAdapter.getSelectedItemsSize() > 1) {
					
					Toast.makeText(getActivity(), "You can update only 1 Vacation at a time", Toast.LENGTH_LONG).show();
					
				}	
			}
		});
		
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(vacationAdapter.getSelectedItemsSize() == 0) 
				{
					Toast.makeText(getActivity(), "No Vacations Selected", Toast.LENGTH_LONG).show();
				}
				else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Are you sure you want to delete?")
				       .setCancelable(false)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				             int count = vacationAdapter.getSelectedItemsSize();
				             ArrayList<Vacation> dltVac = vacationAdapter.getSelectedIds();
				             dbController = new SQLController(context);
				             dbController.open();
				             for(Vacation vacation: dltVac)
				             {
				            	dbController.deleteVacation(vacation);
				             }
				             dbController.close();
				             vacationAdapter.notifyDataSetChanged();
				             Fragment fragment = new VacationFragment();	
								FragmentManager fm = getFragmentManager();
								fm.beginTransaction()
						        .replace(R.id.content_frame, fragment).commit();
				           }
				       })
				       .setNegativeButton("No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
				}
		});
		
		getWorkersFromDB();
		getVacationsFromDB();
		
		return rootView;
	}

	private void getVacationsFromDB() {
		dbController = new SQLController(getActivity());
		dbController.open();
		Cursor vacationCursor = dbController.fetchVacations();
		
		dbController.close();

		if (vacationCursor.moveToFirst()) {
			
			do {
				final int id = vacationCursor.getInt(vacationCursor.getColumnIndex(DBhelper.VACATION_ID));
				final int vacation_worker_id = vacationCursor.getInt(vacationCursor.getColumnIndex(DBhelper.VACATION_WORKER_ID));
				final String vacation_type = vacationCursor.getString(vacationCursor.getColumnIndex(DBhelper.VACATION_TYPE));
				final String vacation_period_start = vacationCursor.getString(vacationCursor.getColumnIndex(DBhelper.VACATION_PERIOD_START));
				final String vacation_period_end = vacationCursor.getString(vacationCursor.getColumnIndex(DBhelper.VACATION_PERIOD_END));
				

				Vacation vacationTemp = new Vacation(id,vacation_worker_id, vacation_type, vacation_period_start,vacation_period_end);
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String temp = ""+cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
				Date currentDate = null,startDate = null;
				try{
				currentDate = sdf.parse(temp);
				startDate = sdf.parse(vacation_period_start);
				}
				catch(Exception e)
				{
					Log.d("Parse Exception",e.getMessage());
				}
				if(currentDate.before(startDate)){
				vacationList.add(vacationTemp);
				}

			} while (vacationCursor.moveToNext());
			vacationAdapter.notifyDataSetChanged();
		}
	}
	private void getWorkersFromDB() {
		dbController = new SQLController(getActivity());
	    dbController.open();
	    Cursor workerCursor = dbController.fetchWorkers();
	    dbController.close();
	    
	   
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
	    		workersList.add(workerTemp);
	    	
	        } while (workerCursor.moveToNext());
	       
	    }
	}
}
