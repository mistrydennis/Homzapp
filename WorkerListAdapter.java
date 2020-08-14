package com.example.mbg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbg.R.color;

public class WorkerListAdapter extends BaseAdapter {

	 private Context context;
	 private ArrayList<Worker> workerList;
	
	 public WorkerListAdapter(Context context, ArrayList<Worker> workerlist){
	       this.context = context;
	       this.workerList = workerlist;
	      
	 }
	
	 public int getSelectedItemsSize(){
	        int size = 0;
	        for (Worker worker : workerList) {
				if(worker.isSelected()) {
					size++;
				}
			}
	        return size;
	 }
	 
	 
	 
	 public List<Worker> getSelectedIds() {
		 List<Worker> listSelect = new ArrayList<Worker>();
		 for (Worker worker : workerList) {
			 if(worker.isSelected()) {
				listSelect.add(worker);
				
			 }
		 }
		 return listSelect;

	 }
	 
	 public void checkAll(){
		 
	 }
	 
	 public void uncheckAll(){
		
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
	           convertView = mInflater.inflate(R.layout.worker_list_item, null);
	       }
	         
	       ImageView imgIcon = (ImageView) convertView.findViewById(R.id.wthumbnail);
	       TextView txtTitle = (TextView) convertView.findViewById(R.id.wtitle);
	       TextView txtProfession = (TextView) convertView.findViewById(R.id.wprofession);
	       TextView txtRate = (TextView) convertView.findViewById(R.id.wrate);
	       
	       
	       CheckBox chkId = (CheckBox)convertView.findViewById(R.id.chkWorker);
	       chkId.setTag(Integer.valueOf(position));
	       chkId.setOnClickListener(new OnClickListener() {

	    	      @Override
	    	      public void onClick(View v) {
	    	    	CheckBox chk = (CheckBox)v;
	    	        int index = Integer.parseInt(chk.getTag().toString());
	    	        if (chk.isChecked()) {
	    	        	workerList.get(index).setSelected(true);
	    	        	
	    	        }
	    	        else {
	    	        	workerList.get(index).setSelected(false);
	    	        }
	    	      }
	        });
	       
	       txtTitle.setText(workerList.get(position).getName());
	       txtProfession.setText(workerList.get(position).getProfession());
	       
	       String image = workerList.get(position).getImage().toLowerCase().trim();
	       if(image.equalsIgnoreCase(AddWorkerFragment.DEFAULT_IMAGE)) {
	    	   imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.worker_dp));
	       }
	       else {
	    	   imgIcon.setImageURI(Uri.parse(image));
	       }
	       String type = workerList.get(position).getType().toLowerCase().trim();
	       String typeStr = "Unknown period";
	       if(type.equalsIgnoreCase("d")) {
				typeStr="Day";
	       }
	       else if(type.equalsIgnoreCase("w")) {
				typeStr="Week";
	       }
	       else { 
				typeStr ="Month";
	       }
	       
	       String rate = context.getString(R.string.Rs) +  workerList.get(position).getRate() + " per " + typeStr;
	       txtRate.setText(rate);
	       
	       txtTitle.setTextColor(color.list_item_title_selected);
	        
	       return convertView;
	 }

}
