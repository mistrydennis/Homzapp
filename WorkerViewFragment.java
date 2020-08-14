package com.example.mbg;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkerViewFragment extends Fragment  {
	
	private ImageView workerImage ;
	
	Worker worker ;
	
	private TextView wName;
	private TextView wPContact;
	private TextView wSContact;
	private TextView wAddress;
	private TextView wProfession;
	private TextView wRate;
	
	// Inflator View
	private View rootView;
	   
	public void makeView(Bundle args) {
		worker = (Worker) args.getSerializable("worker");
		Log.d("Worker", worker.getName());
		fillView(worker);		
	}
	
	
	public void fillView(Worker worker) {
		 workerImage = (ImageView) rootView.findViewById(R.id.workerImg);
	     workerImage.setImageResource(R.drawable.worker_dp);
	     
	       
	     String image = worker.getImage().toLowerCase().trim();
	     if(image.equalsIgnoreCase(AddWorkerFragment.DEFAULT_IMAGE)) {
	    	 workerImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.worker_dp));
	     }  
	     else {
	    	 workerImage.setImageBitmap(BitmapFactory.decodeFile(image)); 
	     }
	     
	     wName = (TextView)rootView.findViewById(R.id.txtWorkerName);
	     wName.setText(worker.getName());
	     
	     
	     wAddress = (TextView)rootView.findViewById(R.id.txtWorkerAddress);
	     wAddress.setText(worker.getAddress());
	     
	     wPContact = (TextView)rootView.findViewById(R.id.txtWorkerPContact);
	     wPContact.setText(worker.getPrimaryContact());
	     
	     wSContact = (TextView)rootView.findViewById(R.id.txtWorkerSContact);
	     wSContact.setText(worker.getSecondaryContact());
	     
	     wProfession = (TextView)rootView.findViewById(R.id.txtWorkerProfession);
	     wProfession.setText(worker.getProfession());
	     
	     wRate = (TextView)rootView.findViewById(R.id.txtWorkerRate);
	     String rate = worker.getRate() + "/" ;
	     
	     if(worker.getType().equalsIgnoreCase("M")) {
	    	 rate += " Month";
	     } else  if(worker.getType().equalsIgnoreCase("D")) {
	    	 rate += " Day";
	     } else {
	    	 rate += " Week";
	     }
	     
	     wRate.setText(rate);
	     
	     
	       
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	           Bundle savedInstanceState) {
	       rootView = inflater.inflate(R.layout.worker_view, container, false);
	       Bundle arguments = getArguments();
		   if(arguments != null) {
			   makeView(arguments);
		   }
		   else {
		   }
	       return rootView;
	   }
	
}
