package com.example.enroute;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends Activity {

  //-------------------------------------------------------
  // Instance Variables
  
  Handler searchHandler;
  ProgressDialog progressDialog;
  Context mainContext;
  
  //Private attributes
  private EditText destinationInput;
  private EditText queryInput;
  
  //-------------------------------------------------------
  // onCreate
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//initialize the view
		setContentView(R.layout.activity_search);
		
    //find our UI elements
    destinationInput = (EditText) findViewById(R.id.queryInput);
    queryInput = (EditText) findViewById(R.id.destinationInput);
    
		//initialize instance vars
		searchHandler = new Handler();
		mainContext = this;
		
	}

	
  //-------------------------------------------------------
  // what do these two do?
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
            
        if(resultCode == 1) {  // ResultsActivity
        } 
    }
	
	
  //-------------------------------------------------------
  // search onClick handler
	public void searchOnClick (View view) {
	  
	  //show dialogue box
	  progressDialog = ProgressDialog.show(SearchActivity.this, "", "Loading...", true);
	  
	  //and fire background event 
    try {
      System.out.println("Calling backgroundSearchTask()");
      new backgroundSearchTask().execute();
    } catch(Exception e){
      Log.v("lookup failed","Exception:"+e.getMessage());
    }
	}
	
	
	//-------------------------------------------------------
	//asynchronous task for results lookup
  private class backgroundSearchTask extends AsyncTask<String, Integer, String> {
    
    //run our background task
    protected String doInBackground(String... trash) {
    
      //get values from UI
      String destination = destinationInput.getText().toString();
      String query = queryInput.getText().toString();
      
      String retVal = "";

      //call the main search algorithm
      SearchBase sb = new SearchBase();
      retVal = sb.loadSearchResults( destination, query );
      
      //return junk string
      return retVal;
    }   
    
    //after background task completes
    protected void onPostExecute(final String result) {
      
      //fire event back to main handler
      searchHandler.postDelayed(new Runnable() {
         public void run() {
           
           //TODO: this should obviously be real results data from SearchBase job
           //generate dummy results data
           ArrayList<Place> results = new ArrayList<Place>();
           Place p1 = new Place("Zoo", "1234567890", 41313133, -72925149, "51 Prospect Street New Haven, CT 06511", 1.5, 3 );
           Place p2 = new Place("Commons", "01234567890", 41311876, -72925669, "500 College Street New Haven, CT 06511", 5, 2 );
           Place p3 = new Place("Grove Cemetary", "1112223333", 41312972, -72928244, "120 High Street New Haven, CT 06511", -3, 4 );
           results.add(p1);
           results.add(p2);
           results.add(p3);
           
           //create new intent to show results page
           Intent intent = new Intent(mainContext, ListResultsActivity.class);
           intent.putExtra("polyline", result);
           
           //load the intent with our results data
           ArrayList <Place> rpack = new ArrayList <Place>();
           for (int i = 0; i < results.size(); i++)
             rpack.add (results.get(i));
           intent.putParcelableArrayListExtra ("results", rpack);
           
           //show the intent
           startActivity(intent);

           //and close the progress dialog
           progressDialog.dismiss();
         }
      }, 0);  //0ms 

    }
  }
	  

}
