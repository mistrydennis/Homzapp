package com.example.mbg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class WorkerFragment extends Fragment implements OnClickListener {
	public WorkerFragment () { }
	
	private View rootView;
	
	private SQLController dbController;
	private WorkerListAdapter workerAdapter;
	private ArrayList<Worker> workersList = new ArrayList<Worker>();
	private ListView workerListView;
	
	private Button btnAdd;
	private Button btnUpdate;
	private Button btnDelete;
	
	
	
	public void InitializeControls(View view) {
		btnAdd = (Button)view.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		
		btnUpdate = (Button)view.findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(this);
		
		btnDelete = (Button)view.findViewById(R.id.btnOvertime);
		btnDelete.setOnClickListener(this);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	           Bundle savedInstanceState) {
		 
			dbController = new SQLController(getActivity());
			
	       rootView = inflater.inflate(R.layout.worker_layout, container, false);
	       InitializeControls(rootView);
	       
	       
	       workersList = new ArrayList<Worker>();
	       
	       workerListView = (ListView)rootView.findViewById(R.id.list);
	       workerAdapter = new WorkerListAdapter(getActivity(), workersList);
	       workerListView.setAdapter(workerAdapter);
	       
	       getWorkersFromDB();
	      
	       return rootView;
	}
	
	private void getWorkersFromDB() {
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
	        workerAdapter.notifyDataSetChanged();
	    }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()) {
        	case R.id.btnAdd:
        		// it was the first button
        		redirectToAdd();
        		break;
        	case R.id.btnUpdate:
          		redirectToUpdate();
        		// it was the second button
        		break;
        	case R.id.btnOvertime:
        		performDelete();
        		// it was the third button
        		break;
		}
		
	}
	

	


	public void redirectToAdd() {
		Fragment fragment = new AddWorkerFragment();

		FragmentManager fm = getFragmentManager();
		fm.beginTransaction()
        .replace(R.id.content_frame, fragment).commit();
		
		
	}
	
	public void redirectToUpdate() {
		if(workerAdapter.getSelectedItemsSize() == 0) 
		{
			Toast.makeText(getActivity(), "No Workers Selected", Toast.LENGTH_LONG).show();
		}
		else if(workerAdapter.getSelectedItemsSize()== 1) {
			
			Bundle bundles = new Bundle();
			
			if ( workerAdapter.getSelectedIds().size() == 1 ) {
				Worker worker= workerAdapter.getSelectedIds().get(0);
			    bundles.putSerializable("worker", worker);
			    Log.e("worker in Update", "is valid");
			} else {
				bundles.putSerializable("worker", null);	
			    Log.e("worker in Update", "is null");
			}
			
			Fragment fragment = new AddWorkerFragment();
			fragment.setArguments(bundles);
			
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction()
	        .replace(R.id.content_frame, fragment).commit();
			
			
			Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_LONG).show();
			
		} else if(workerAdapter.getSelectedItemsSize() > 1) {
			
			Toast.makeText(getActivity(), "You can update only Worker at a time", Toast.LENGTH_LONG).show();
			
		}
	}
	

	public void performDelete() {
		if(workerAdapter.getSelectedItemsSize() > 1)
		{
			List<Worker> workers = workerAdapter.getSelectedIds();
			int count = 0;
			try {
				dbController.open();
				for (Worker worker : workers) {
						dbController.deleteWorker(worker.getId());
						count ++;
				}
			} catch(Exception ex) {
				Toast.makeText(getActivity(), "Error removing the workers", Toast.LENGTH_LONG).show();
			} finally {
				dbController.close();
			}
			Toast.makeText(getActivity(), count + " Workers removed", Toast.LENGTH_LONG).show();
			getWorkersFromDB();
			
		}
	}
}
