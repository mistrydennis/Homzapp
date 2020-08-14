package com.example.mbg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbg.R.color;

public class HomeWorkerListAdapter extends BaseAdapter {

	 private Context context;
	 private ArrayList<Worker> workerList;
	 private SQLController dbController;
	 private ArrayList<Integer> attend_for_day;
	 
	 
	 public HomeWorkerListAdapter(Context context, ArrayList<Worker> workerlist){
	       this.context = context;
	       this.workerList = workerlist;
	     
	 }
	
	 public int getSelectedItemsSize(){
	        int size = 0;
	        return size;
	 }
	 
	
	 public List<Worker> getSelectedIds() {
		 List<Worker> listSelect = new ArrayList<Worker>();
		/* for(Map.Entry<String, Object> mapRow: selectedWorkers.entrySet()){
	            if((Boolean)mapRow.getValue()) {
	               listSelect.add(mapRow.getKey());
	            }
	     }*/
		 int index = 0;
		/* for (Object isSelected : selectedWorkers) {
	        if((Boolean)isSelected) {
	        	listSelect.add(workerList.get(index));
	        }
	        index++;
		 }*/
		 
		
		 return listSelect;
		 
	 }
	 
	 
	 
	
	 @Override
	 public int getCount() {
		// TODO Auto-generated method stub
		return workerList.size();
	 }

	 @Override
	 public Object getItem(int position) {
		// TODO Auto-generated method stub
		return workerList.get(position);
	 }

	 @Override
	 public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	 }

	 
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 if (convertView == null) {
	           LayoutInflater mInflater = (LayoutInflater)
	                   context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	           convertView = mInflater.inflate(R.layout.home_worker_list_item, null);
	      
		 	}
		 
	         
	       ImageView imgIcon = (ImageView) convertView.findViewById(R.id.wthumbnail);
	       TextView txtTitle = (TextView) convertView.findViewById(R.id.wtitle);
	       TextView txtProfession = (TextView) convertView.findViewById(R.id.wprofession);
	       TextView txtRate = (TextView) convertView.findViewById(R.id.wrate);
	      
	      
	       
	       txtTitle.setText(workerList.get(position).getName());
	       txtProfession.setText(workerList.get(position).getProfession());
	       
	       String image = workerList.get(position).getImage().toLowerCase().trim();
	       if(image.equalsIgnoreCase(AddWorkerFragment.DEFAULT_IMAGE)) {
	    	   imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.worker_dp));
	       }
	        String type = workerList.get(position).getType().toLowerCase().trim();
	       String typeStr = "Unknown period";
	       if(type.equalsIgnoreCase("d")) {
				typeStr=" / today";
	       }
	       else if(type.equalsIgnoreCase("w")) {
				typeStr=" / week";
	       }
	       else { 
				typeStr =" / Month";
	       }
	       
	       dbController = new SQLController(context);
	       dbController.open();
	       int id = workerList.get(position).getId();	       
	       txtRate.setText(""+dbController.getSalaryById(id)+typeStr);
	       dbController.close();
	       txtTitle.setTextColor(color.list_item_title_selected);
	        
	       final Button btnAbsent = (Button)convertView.findViewById(R.id.btnAbsent);
	       btnAbsent.setTag(Integer.valueOf(position));
	       final Button btnOvertime = (Button)convertView.findViewById(R.id.btnOvertime);
	       btnOvertime.setTag(Integer.valueOf(position));
	       final int index = Integer.parseInt(btnAbsent.getTag().toString());
	       final int id_worker = workerList.get(index).getId();
	       dbController = new SQLController(context);
	       dbController.open();
	       attend_for_day=dbController.getAttendanceForDay(new Date());
	       dbController.close();
	       if(attend_for_day.contains(id_worker))
	       {
	    	   System.out.println("id: "+id_worker);
	    	   btnAbsent.setEnabled(false);
	    	   btnOvertime.setEnabled(false);
	       }
	       else
	       {
	    	   btnAbsent.setEnabled(true);
	    	   btnOvertime.setEnabled(true);
	       }
	       
	       
	       
	       
	       btnAbsent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			final int index = Integer.parseInt(((Button)v).getTag().toString());
			//
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			View promptView = layoutInflater.inflate(R.layout.absent_dialog, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setView(promptView);
			final Spinner spnAbsentType = (Spinner)promptView.findViewById(R.id.spnAbsentType);
			final int id = workerList.get(index).getId();
			alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					String spinnerValue = spnAbsentType.getSelectedItem().toString();
					WorkerAttendance attendance = new WorkerAttendance();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					Date dt = new Date();
					attendance.setDay(""+sdf.format(dt));
					attendance.setOvertime(""+0);
					attendance.setWorkerId(""+id);
					if(spinnerValue.equals("Full Day"))
					{
						attendance.setPresent(""+1);
						attendance.setDayType(""+1);
					}
					else if(spinnerValue.equals("Half Day"))
					{
						attendance.setPresent(""+2);
						attendance.setDayType(""+1);
					}
					else if(spinnerValue.equals("Full Day Paid"))
					{
						attendance.setPresent(""+1);
						attendance.setDayType(""+0);
						
					}
					else if(spinnerValue.equals("Half Day Paid"))
					{
						attendance.setPresent(""+2);
						attendance.setDayType(""+0);
					}
					
					dbController = new SQLController(context);
				    dbController.open();
				    int i = dbController.insertAttendance(attendance);
				    if(i>0)
				    {
				    	Toast t = Toast.makeText(context, "Absent Marked", Toast.LENGTH_LONG);
				    	t.show();
				    	 btnAbsent.setEnabled(false);
				    	 btnOvertime.setEnabled(false);
				    }
				    else
				    {
				    	Toast t = Toast.makeText(context, "Database Error", Toast.LENGTH_LONG);
				    	t.show();
				    }
				    dbController.close();
				}
				
			})
			.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
				
			});
			AlertDialog alertD = alertDialogBuilder.create();
			 alertD.show();
			//
			}
		});
	       
	       btnOvertime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int index = Integer.parseInt(((Button)v).getTag().toString());
				final int id = workerList.get(index).getId();
								
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(R.layout.overtime_dialog, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setView(promptView);
				final EditText input = (EditText) promptView.findViewById(R.id.txtAmount);
				alertDialogBuilder
								.setCancelable(false)
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
									int amount = Integer.parseInt(input.getText().toString());	
									
									WorkerAttendance attendance = new WorkerAttendance();
									attendance.setId(id);
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									attendance.setDay(""+sdf.format(new Date()));
									attendance.setOvertime(""+amount);
									attendance.setWorkerId(""+id);
									attendance.setPresent(""+0);
									attendance.setDayType(""+0);
									dbController = new SQLController(context);
								    dbController.open();
								    int i = dbController.insertAttendance(attendance);
								    if(i>0)
								    {
								    	Toast t = Toast.makeText(context, "Overtime Added", Toast.LENGTH_LONG);
								    	t.show();
								    	btnAbsent.setEnabled(false);
								    	btnOvertime.setEnabled(false);
								    }
								    else
								    {
								    	Toast t = Toast.makeText(context, "Database Error", Toast.LENGTH_LONG);
								    	t.show();
								    }
									dbController.close();
									}
									
								})
								.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method stub
												dialog.cancel();
											}
									
								});
				 AlertDialog alertD = alertDialogBuilder.create();
				 alertD.show();

			}
		});
	       
	       return convertView;
	 }

}
