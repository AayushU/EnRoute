package com.example.enroute;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListResultsActivity extends ListActivity {

	// -------------------------------------------------------
	// Instance Variables

	// misc
	private Context mainContext;

	// list config
	private RelativeLayout currentSelectedPanel = null;
	private int currentSelectedResult = -1;
	ArrayList<Place> results;

	// polyline
	String polyline;

	// -------------------------------------------------------
	// onCreate
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// initialize layout
		setContentView(R.layout.activity_list);

		// initialize instance vars
		mainContext = this;

		// load results from data passed to intent
		loadPassedData(getIntent());

		// update results count
		TextView listResultsCount = (TextView) findViewById(R.id.listResultsCount);
		String rcount = String.format(getString(R.string.results_count),
				results.size());
		listResultsCount.setText(rcount);

		// initialize listAdapter
		ListResultsAdapter adapter = new ListResultsAdapter(this, results);
		setListAdapter(adapter);
		
	}

	// load results from intent parcel
	protected void loadPassedData(Intent intent) {

		// initialize results array
		results = new ArrayList<Place>();

		// unpack intent parcel into results array
		ArrayList<Place> rpack = intent.getParcelableArrayListExtra("results");
		for (int i = 0; i < rpack.size(); i++) {
			results.add(rpack.get(i));
		}
		
    //get polyline to graph results
    polyline = intent.getStringExtra("polyline");

	}

	// -----------------------------------------------------
	// onClick handler for list item
	public void onListItemClick(ListView l, View v, int position, long id) {

	  //find panel for this list result
		RelativeLayout thisPanel = (RelativeLayout) v.findViewById(R.id.resultPanel);
		
		//display the panel
		thisPanel.setVisibility(View.VISIBLE);
		
		//hide the old panel
		if (currentSelectedPanel != null)
			currentSelectedPanel.setVisibility(View.GONE);
		
		//update instance vars
		currentSelectedPanel = thisPanel;
		currentSelectedResult = position;
	}

	// onClick handler for map view toggle
	public void switchToMapView(View btn) {

		// create new intent to show results page
		Intent intent = new Intent(mainContext, MapResultsActivity.class);

		// load the intent with our results data
		ArrayList<Place> rpack = new ArrayList<Place>();
		for (int i = 0; i < results.size(); i++)
			rpack.add(results.get(i));
		intent.putParcelableArrayListExtra("results", rpack);
		
		//load the intent with our polyline
		intent.putExtra("polyline", polyline);

		// show the intent
		startActivity(intent);

	}

	
	// onClick handler for clicking the directions button for a specific place
	public void showDirections(View btn) {
		
	  //get current selected result
		Place place = results.get(currentSelectedResult);
		
		//get address 
		String addr = ""+place.getLatitude()+","+place.getLongitude();
		
		//create intent to show navigation for that result
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + addr) );
		
		//start intent
		startActivity(intent);
	}

}
