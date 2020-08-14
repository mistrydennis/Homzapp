package com.example.mbg;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment {
	private Context context;
	View rootView;
	

	public SettingsFragment() { }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.settings_layout, container, false);
		context = rootView.getContext();
		Utils.onActivityCreateSetTheme(getActivity());
		
		Button btnSend = (Button)rootView.findViewById(R.id.btnBluetoothSend);
			
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,RemoteActivity.class);
				startActivity(i);
				
			}	
		});
		Button btnReceive = (Button)rootView.findViewById(R.id.btnReceive);
		btnReceive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,ReceiveActivity.class);
				startActivity(i);
			}
		});
		
		
		Button btnTheme1 = (Button)rootView.findViewById(R.id.btnTheme1);
		btnTheme1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.changeToTheme(getActivity(), Utils.THEME_DEFAULT);
			}
		});
		
		
		Button btnTheme2 = (Button)rootView.findViewById(R.id.btnTheme2);
		btnTheme1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.changeToTheme(getActivity(), Utils.THEME_BLUE);
				
			}
		});
		
		
		
		return rootView;
	}
}
