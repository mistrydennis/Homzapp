package com.example.mbg;

import java.util.ArrayList;
import java.util.List;

import com.example.mbg.R.color;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class VacationListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<Vacation> vacationList;
	private ArrayList<Worker> workerList;
	
	public VacationListAdapter(Context context,
			ArrayList<Vacation> vacationList,ArrayList<Worker> workerList) {
		// TODO Auto-generated constructor stub
		this.context = context;
	    this.vacationList = vacationList;
	    this.workerList = workerList;
	}
	
	public int getSelectedItemsSize(){
        int size = 0;
        for (Vacation vacation : vacationList) {
			if(vacation.isSelected()) {
				size++;
			}
		}
        return size;
 }
	public ArrayList<Vacation> getSelectedIds() {
		 ArrayList<Vacation> listSelect = new ArrayList<Vacation>();
		 for (Vacation vacation : vacationList) {
			 if(vacation.isSelected()) {
				listSelect.add(vacation);
				
			 }
		 }
		 return listSelect;

	 }

	public VacationListAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return vacationList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return vacationList.get(position);
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
	           convertView = mInflater.inflate(R.layout.vacation_list_item, null);
	       }
	         
	       ImageView imgIcon = (ImageView) convertView.findViewById(R.id.wthumbnail_vacation);
	       TextView txtTitle = (TextView) convertView.findViewById(R.id.wtitle_vacation);
	       TextView txtStart = (TextView) convertView.findViewById(R.id.vacationStartDate);
	       TextView txtEnd = (TextView) convertView.findViewById(R.id.vacationEndDate);
	       
	       
	       CheckBox chkId = (CheckBox)convertView.findViewById(R.id.chkVacation);
	       chkId.setTag(Integer.valueOf(position));
	       chkId.setOnClickListener(new OnClickListener() {

	    	      @Override
	    	      public void onClick(View v) {
	    	    	CheckBox chk = (CheckBox)v;
	    	        int index = Integer.parseInt(chk.getTag().toString());
	    	        if (chk.isChecked()) {
	    	        	vacationList.get(index).setSelected(true);
	    	        	
	    	        }
	    	        else {
	    	        	vacationList.get(index).setSelected(false);
	    	        }
	    	      }
	        });
	       
	       Worker tempWorker = getWorkerForVacation(vacationList.get(position));
	       
	       txtTitle.setText(tempWorker.getName());//set worker name
	       txtStart.setText("Starts: "+vacationList.get(position).getVacation_period_start());
	       
	       
	       String image = tempWorker.getImage().toLowerCase().trim();//set worker image
	       if(image.equalsIgnoreCase(AddWorkerFragment.DEFAULT_IMAGE)) {
	    	   imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.worker_dp));
	       }
	       String type = vacationList.get(position).getVacation_type();
	       String typeStr = "Unknown Type";
	       if(type.equalsIgnoreCase("0")) {
				typeStr="Paid";
	       }
	       else if(type.equalsIgnoreCase("1")) {
				typeStr="Unpaid";
	       }	        
	       txtEnd.setText("Ends: "+vacationList.get(position).getVacation_period_end()+" "+typeStr);
	       txtTitle.setTextColor(color.list_item_title_selected);
	        
	       return convertView;
	}

	private Worker getWorkerForVacation(Vacation vacation) {
		// TODO Auto-generated method stub
		Worker w = null;
		for(Worker worker : workerList)
		{
			if(worker.getId()==vacation.getWorker_id()){
				w=worker;
			}
		}
		return w;
	}

}
