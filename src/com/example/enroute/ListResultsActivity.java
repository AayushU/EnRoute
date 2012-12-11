package com.example.enroute;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
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

public class ListResultsActivity extends ListActivity{

  // -------------------------------------------------------
  // Instance Variables
  
  //misc
  private Context mainContext;
  
  //list config
  private LinearLayout shown = null;
  ArrayList<Place> results;
  
  
  // -------------------------------------------------------
  // onCreate
  protected void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    
    // initialize layout
    setContentView(R.layout.activity_list);
    
    //initialize instance vars
    mainContext = this;
    
    //load results from data passed to intent
    loadPassedData();
    
   //update results count
   TextView listResultsCount = (TextView) findViewById(R.id.listResultsCount);
   String rcount = String.format( getString(R.string.results_count), results.size() );
   listResultsCount.setText( rcount );
    
    // initialize listAdapter
    ListResultsAdapter adapter = new ListResultsAdapter(this, results);
    setListAdapter(adapter);
  }
  
  
  //load results from intent bundle
  protected void loadPassedData(){
    
    //TODO:  right now this is just dummy data!
    
    // initialize dummy data
    results = new ArrayList<Place>();
    Place p1 = new Place("Zoo", "1234567890", 41313133, -72925149, " 51 Prospect Street New Haven, CT 06511", 1.5, 3 );
    Place p2 = new Place("Commons", "01234567890", 41311876, -72925669, "500 College Street New Haven, CT 06511", 5, 2 );
    Place p3 = new Place("Grove Cemetary", "1112223333", 41312972, -72928244, "  120 High Street New Haven, CT 06511", -3, 4 );
    results.add(p1);
    results.add(p2);
    results.add(p3);
  }

    
  // -----------------------------------------------------
  // onClick handler for list item
  public void onListItemClick(ListView l, View v, int position, long id) {

    LinearLayout layout = (LinearLayout) v.findViewById(R.id.toshow);
    layout.setVisibility(View.VISIBLE);
    if (shown != null)
      shown.setVisibility(View.GONE);
    shown = layout;
  }
  
  // onClick handler for map view toggle
  public void switchToMapView(View btn){
    
    Intent intent = new Intent(mainContext, MapResultsActivity.class);
    startActivityForResult(intent, 1);
    
  }



  

}
