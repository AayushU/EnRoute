package com.example.enroute;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

  //-------------------------------------------------------
  // Instance Variables
  
  Handler searchHandler;
  ProgressDialog searchDialog;
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
		setContentView(R.layout.activity_main);
		
    //find our UI elements
    destinationInput = (EditText) findViewById(R.id.queryInput);
    queryInput = (EditText) findViewById(R.id.destinationInput);
    
		//initialize instance vars
		searchHandler = new Handler();
		mainContext = this;
		
	}

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
	  searchDialog = ProgressDialog.show(MainActivity.this, "", "Loading...", true);
	  
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

      //call the main search algorithm
      String destination = destinationInput.getText().toString();
      String query = queryInput.getText().toString();
      //loadSearchResults( destination, query);
      
      String retVal = "";
      return retVal;
    }   
    
    //after background task completes
    protected void onPostExecute(String result) {
      
      //fire event back to main handler
      searchHandler.postDelayed(new Runnable() {
         public void run() {
           
           //close the progress dialog
           searchDialog.dismiss();
           
           //and start new intent to show results page
           Intent intent = new Intent(mainContext, ResultsActivity.class);
           startActivityForResult(intent, 1);

         }
      }, 0);  //0ms 

    }
  }
	  

}
