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
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.results_row_layout, null);
			TextView text = (TextView) view.findViewById(R.id.label);
			Place current = list.get(position);
			text.setText(current.getName() + " ("
					+ current.getDistance() + " miles) "
					+ current.getDistanceOffRoute() + " miles off route");
			TextView text2 = (TextView) view.findViewById(R.id.label2);
			text2.setText(current.getName());
			TextView text3 = (TextView) view.findViewById(R.id.label3);
			text3.setText(current.getDistance()+" miles ahead");
			TextView text4 = (TextView) view.findViewById(R.id.label4);
			text4.setText(current.getDistanceOffRoute()+" miles off route");
			TextView text5 = (TextView) view.findViewById(R.id.label5);
			text5.setText(current.getPhoneNumber());
		} else {
			view = convertView;
		}
		return view;
	}
	
}
