package com.example.enroute;

import java.util.ArrayList;

import com.example.enroute.SearchBase.LocationResult;
import com.sun.tools.javac.util.List;

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
  
  //store pins to show on the map for specific places. 
  ArrayList<LocationResult> places;
  
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
      
      String polyline = "";

      //call the main search algorithm
      SearchBase sb = new SearchBase();
    //  polyline = sb.loadSearchResults( destination, query );
      //hard code for testing
      destination = "New Haven";
      query = "Pizza";
      polyline = "u~b{Fhwb|LzXhRZTvBkCzCsKhFaMr@qIBqIZeJpGkWlEoB`NbDlg@|a@hYrZ`Gbi@bArSAfc@qBhQXhQpN~g@`Rrm@`IvVjIvKlOrIbLnGdMlMtf@nmAbIfRr[bf@fa@hl@bv@hsAl`@d_AzP`i@ClX{EfX_Nts@oCp^\\vTtGxa@fO~[vLjQf\\~Unn@p^bMlOjL|RbZbXhJhOdEr]fFvc@|Nrt@r[`]zJnQdDvMhCtWnEnQhGtKbQdN`_@`WjQ|\\|Tf]bRbWpDzPTrN{@th@yAzUqErXuL`a@gBpYrGfWpJpJ|V|UnEpLzW|bAzB~a@w@np@yDzV}BlPj@jTtMtj@tKh]|IlMvRvQjOpC|Ft@bQhIhQzWrF`Tn]pyAvCf_@lAno@bFnUxInNhDjF~p@ny@tLvOfFfM|K|h@`Ixd@vMfc@Cd]qDfS{DrYeDbn@pJpyBfE`aAvA~`@~@hG~f@jlAnNjv@bNng@nEhYdEl_@UpUG|Cd@tQrHn\\bRni@bPn`@jOrSd`@lf@`Mrd@lFlJxKnIfq@z_@fK~HjHbP|AvI|Ivf@bBfX|Ej\\hLb^x@xJS~P]hb@nFfTnDbGlNvLvO`NzQ`PfJnUpHnn@bGh\\tNhYjMzc@dLhj@xKpn@k@r]aAtq@tHbb@rF|OzJ`k@zI``@|Uhv@bH`J~WpR~LdTzL~k@~Qbn@zLhk@|Az]`GlW`cAlmAbh@ja@dXzY`f@fi@zKbQdCdPAfMkAx[hAzThFjTj\\vp@lZli@fCdGhIzYr@tQ}Bjf@n@~k@`BdHnIfNfHxDb]vMxMvQxGtUlCvNxF`P|IbH~KpCbl@x`@~RrJdU~GtHjD~b@ff@`M`OfWr`@xJlXpQ~Lfb@nj@~L|OpSfNrO`GbLfJvHlRpF|Fhg@`]|n@tUvRrJdJbKzC~WmChd@bBzUvJ~j@rNv`@nLnPjMvIbLlBrOw@bYyIzc@mQhKaJrHgD`\\{Dn^rEhOuDxQeC|b@c@hKgBpG~AtCzDdY~l@~CrQ|Abf@jFte@fKh`BtOn|Bz@bMzMbT`YxXn^df@rTl]dOxb@zGlYbCjC~]pS|@zFoIfVoLt_@KtEjDAlCqG|KwA`Jt@zGrFhPx[nGbGhX|NvE|ErI`R~KhDl^iIdNbFfFvEbExEfWdVrj@rg@`HvJnJlGrQhQ~}@hv@pW~IdMtCbNvGfUaDvFqA~IuIhJw@jPlEzSbJ|PdAnUtG~JbE|HdFvAtCvBz_@xDdn@`Fdi@qAtE_F~EaCu@}JsJwBuBaFtL_KpXnJtH";
      
      places = sb.loadLocationResult(destination, query, polyline);
      
      return polyline;
    }   
    
    //after background task completes
    protected void onPostExecute(final String polylineResult) {
      
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
           
           //load intent with our polyline
           intent.putExtra("polyline", polylineResult);
           
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
