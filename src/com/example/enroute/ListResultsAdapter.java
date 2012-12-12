package com.example.enroute;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ListResultsAdapter extends ArrayAdapter<Place> {
	private final List<Place> list;
	private final Activity context;

	public ListResultsAdapter(Activity context, List<Place> list) {
		super(context, R.layout.results_row_layout, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  
		View view = null;
		if (convertView == null) {
		  
		  //get current result
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.results_row_layout, null);
			Place current = list.get(position);
			
			//set label text
			TextView label = (TextView) view.findViewById(R.id.resultLabel);
			label.setText(current.getName() + " ("
					+ current.getDistance() + " miles) "
					+ current.getDistanceOffRoute() + " miles off route");
			
			//set panel text
			TextView name = (TextView) view.findViewById(R.id.resultName);
			name.setText(current.getName());
			
			TextView distance = (TextView) view.findViewById(R.id.resultDistance);
			distance.setText(current.getDistance()+" miles ahead");
			
			TextView offRoute = (TextView) view.findViewById(R.id.resultOffRoute);
			offRoute.setText(current.getDistanceOffRoute()+" miles off route");
			
			TextView phone = (TextView) view.findViewById(R.id.resultPhone);
			phone.setText(current.getPhoneNumber());
			
		} else {
			view = convertView;
		}
		
		return view;
	}
	
}
