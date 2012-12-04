package com.example.enroute;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.search_tab)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.results_tab)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.map_tab)
				.setTabListener(this));

		actionBar.show();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_results, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

		// currently it just displays an integer.
		// based on tab.getPosition(), we want to display a different item.

		// Results pane
		if (tab.getPosition() == 1) {
			ListFragment fragment = new ResultsSectionFragment();
			// Bundle args = new Bundle();
			// args.putString(DummySectionFragment.ARG_SECTION_NUMBER, "AVI");
			// fragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		} else {
			// When the given tab is selected, show the tab contents in the
			// container view.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
					tab.getPosition() + 1);
			fragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		}
	}

	public static class ResultsSectionFragment extends ListFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		LinearLayout shown = null;

		public ResultsSectionFragment() {
		}
		
		@Override
		 public void onListItemClick(ListView l, View v, int position, long id)
	     { 
			// When clicked, show a toast with the TextView text
			Toast.makeText(getActivity(), "Test", Toast.LENGTH_LONG)
					.show();
			LinearLayout layout = (LinearLayout) v.findViewById(R.id.toshow);
			layout.setVisibility(View.VISIBLE);
			if (shown != null)
				shown.setVisibility(View.GONE);
			shown = layout;
	     }

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// super.onCreateView(inflater, container, savedInstanceState);
			ArrayList<Place> places = new ArrayList<Place>();
			Place p1 = new Place("Yale", 0, 10, "2034562823");
			Place p2 = new Place("Harvard", 20, 5.4, "65055534232");
			Place p3 = new Place("Princeton", -4.2, 6, "43683262343");
			places.add(p1);
			places.add(p2);
			places.add(p3);
			ResultsAdapter adapter = new ResultsAdapter(getActivity(), places);
			setListAdapter(adapter);

			View view = inflater.inflate(R.layout.fragment_results, container,
					false);

			return view;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}

	}

	@Override
	public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, android.app.FragmentTransaction arg1) {
		// currently it just displays an integer.
		// based on tab.getPosition(), we want to display a different item.

		// Results pane
		if (arg0.getPosition() == 1) {
			ListFragment fragment = new ResultsSectionFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		} else {
			// When the given tab is selected, show the tab contents in the
			// container view.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
					arg0.getPosition() + 1);
			fragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

}
