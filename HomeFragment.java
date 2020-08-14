package com.example.mbg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HomeFragment extends Fragment {

	public HomeFragment() {
	}

	private View rootView;

	private SQLController dbController;
	private HomeWorkerListAdapter homeWorkerAdapter;
	private ArrayList<Worker> workersList = new ArrayList<Worker>();
	private ListView homeWorkerListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		rootView = inflater.inflate(R.layout.home_layout, container, false);
		workersList = new ArrayList<Worker>();
		homeWorkerListView = (ListView) rootView.findViewById(R.id.home_list);
		homeWorkerAdapter = new HomeWorkerListAdapter(getActivity(),
				workersList);
		homeWorkerListView.setAdapter(homeWorkerAdapter);

		getWorkersFromDB();
		// Alarm
		Context context = rootView.getContext();
		setRecurringAlarm(context);
		 

		   
		return rootView;
	}

	private void getWorkersFromDB() {
		dbController = new SQLController(getActivity());
		dbController.open();
		Cursor workerCursor = dbController.fetchWorkers();
		//int count = dbController.insertDummyWorker();
		// Log.d("Insert","Record Inserted : " + count);
		dbController.close();

		if (workerCursor.moveToFirst()) {
			System.out.println("Entered");
			do {
				final int id = workerCursor.getInt(workerCursor
						.getColumnIndex(DBhelper.WORKER_ID));
				final String name = workerCursor.getString(workerCursor
						.getColumnIndex(DBhelper.WORKER_NAME));
				final String image = workerCursor.getString(workerCursor
						.getColumnIndex(DBhelper.WORKER_IMAGE));
				final String contact = workerCursor.getString(workerCursor
						.getColumnIndex(DBhelper.WORKER_PCONTACT));
				final String scontact = workerCursor.getString(workerCursor
						.getColumnIndex(DBhelper.WORKER_SCONTACT));
				final String address = workerCursor.getString(workerCursor
						.getColumnIndex(DBhelper.WORKER_ADDRESS));
				final String profession = workerCursor.getString(workerCursor
						.getColumnIndex(DBhelper.WORKER_PROFESSION));
				final String rate = workerCursor.getString(workerCursor
						.getColumnIndex(DBhelper.WORKER_RATE));
				final String type = workerCursor.getString(workerCursor
						.getColumnIndex(DBhelper.WORKER_TYPE));

				Worker workerTemp = new Worker(id, name, image, contact,
						scontact, address, profession, rate, type);
				workersList.add(workerTemp);

			} while (workerCursor.moveToNext());
			homeWorkerAdapter.notifyDataSetChanged();
		}
	}

	private void setRecurringAlarm(Context context) {

		//alarm1 -- todays preseties and salary calculation
		//Create alarm manager
		AlarmManager alarmMgr0 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		//Create pending intent & register it to your alarm notifier class
		Intent intent0 = new Intent(context,AlarmReceiver.class);
		intent0.putExtra("id", 0);
		PendingIntent pendingIntent0 = PendingIntent.getBroadcast(context, 0, intent0, 0);

		//set timer you want alarm to work (here I have set it to 11:50pm)
		Calendar timeOff9 = Calendar.getInstance();
		timeOff9.set(Calendar.HOUR_OF_DAY, 23);
		timeOff9.set(Calendar.MINUTE, 50);
		timeOff9.set(Calendar.SECOND, 0);

		//set that timer as a RTC Wakeup to alarm manager object
		alarmMgr0.set(AlarmManager.RTC_WAKEUP, timeOff9.getTimeInMillis(), pendingIntent0);
		
		//alarm2 -- early morning alarm for marking vacation absentees
		AlarmManager alarmMgr1 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		//Create pending intent & register it to your alarm notifier class
		Intent intent1 = new Intent(context,AlarmReceiver.class);
		intent1.putExtra("id", 1);
		PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 1, intent1, 0);

		//set timer you want alarm to work (here I have set it to 11:50pm)
		Calendar timeO = Calendar.getInstance();
		timeO.set(Calendar.HOUR_OF_DAY,0);
		timeO.set(Calendar.MINUTE,15);
		timeO.set(Calendar.SECOND, 0);

		//set that timer as a RTC Wakeup to alarm manager object
		alarmMgr1.set(AlarmManager.RTC_WAKEUP, timeO.getTimeInMillis(), pendingIntent1);
		
	}

	
	
}
