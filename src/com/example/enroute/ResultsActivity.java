package com.example.enroute;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends FragmentActivity implements
    ActionBar.TabListener {

  // -------------------------------------------------------
  // Instance Variables

  // saved tab state (bundle key representing tab position)
  private static final String TAB_STATE = "selected_navigation_item";

  // -------------------------------------------------------
  // onCreate

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // initialize UI
    setContentView(R.layout.activity_results);

    // initialize Action Bar Tabs
    initializeActionBar();
  }

  /****************************************************************************
   * 
   * Fragments
   * 
   ***************************************************************************/

  // Results (List) Fragment
  public static class ResultsSectionFragment extends ListFragment {

    // instance variables
    public static final String ARG_SECTION_NUMBER = "section_number";
    private LinearLayout shown = null;

    // constructor
    public ResultsSectionFragment() {
    }

    // -----------------------------------------------------
    // onClick handler for list item
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

      LinearLayout layout = (LinearLayout) v.findViewById(R.id.toshow);
      layout.setVisibility(View.VISIBLE);
      if (shown != null)
        shown.setVisibility(View.GONE);
      shown = layout;
    }

    // -----------------------------------------------------
    // ??
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      // super.onCreateView(inflater, container, savedInstanceState);

      // initialize dummy data
      ArrayList<Place> places = new ArrayList<Place>();
      Place p1 = new Place("Zoo", "1234567890", "41313133", "-72925149", " 51 Prospect Street New Haven, CT 06511", 1.5, 3 );
      Place p2 = new Place("Commons", "01234567890", "4131187", "-72925669", "500 College Street New Haven, CT 06511", 5, 2 );
      Place p3 = new Place("Sterling", "1112223333", "4130000", "-72925900", "  120 High Street New Haven, CT 06511", -3, 4 );
      places.add(p1);
      places.add(p2);
      places.add(p3);

      // initialize and set listAdapter
      ResultsAdapter adapter = new ResultsAdapter(getActivity(), places);
      setListAdapter(adapter);

      // return final UI
      View view = inflater.inflate(R.layout.fragment_results, container, false);
      return view;
    }
  }

  
  // Map Fragment
  public static class MapSectionFragment extends Fragment {

    // instance variables
    public static final String ARG_SECTION_NUMBER = "section_number";

    // constructor
    public MapSectionFragment() {
    }

    // onCreate
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

      // for now just put a nice lil text box
      TextView textView = new TextView(getActivity());
      textView.setGravity(Gravity.CENTER);
      textView.setText(R.string.map_tab);
      return textView;
      
    }

  }

  /****************************************************************************
   * 
   * Tabs Code
   * 
   ***************************************************************************/

  // initialize the action bar
  private void initializeActionBar() {
    // Set up the action bar to show tabs.
    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    // Show the Up button in the action bar.
    actionBar.setDisplayHomeAsUpEnabled(true);

    // For each of the sections in the app, add a tab to the action bar.
    actionBar.addTab(actionBar.newTab().setText(R.string.list_tab)
        .setTabListener(this));
    actionBar.addTab(actionBar.newTab().setText(R.string.map_tab)
        .setTabListener(this));

    actionBar.show();
  }

  // tab selection etc
  @Override
  public void onTabSelected(Tab arg0, android.app.FragmentTransaction arg1) {
    
    //will hold our new fragment object
    Fragment fragment;
    
    //determine which fragment to build
    switch ( arg0.getPosition() ) {
    
    case 0:  //results tab
      fragment = new ResultsSectionFragment();
      break;
    case 1:  //map tab
      fragment = new MapSectionFragment();
      break;
    default:  //fallback
      fragment = new ResultsSectionFragment();
      break;
    }
    
    //build that fragment
    getSupportFragmentManager().beginTransaction()
    .replace(R.id.container, fragment).commit();
    
  }

  @Override
  public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onTabUnselected(Tab arg0, android.app.FragmentTransaction arg1) {
    // TODO Auto-generated method stub

  }

  // instance states
  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {

    // restore tabs to previous state
    if (savedInstanceState.containsKey(TAB_STATE)) {
      int prevState = savedInstanceState.getInt(TAB_STATE);
      getActionBar().setSelectedNavigationItem(prevState);
    }

  }

  @Override
  public void onSaveInstanceState(Bundle outState) {

    // save current tab state
    int curState = getActionBar().getSelectedNavigationIndex();
    outState.putInt(TAB_STATE, curState);

  }

  // options menu
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

}
