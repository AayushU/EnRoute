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
  ArrayList<Place> places = new ArrayList<Place>();
  
  //store our polyline for displaying route
  String polyline;
  
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

      //call the main search algorithm
//      SearchBase sb = new SearchBase();
//      polyline = sb.loadSearchResults( destination, query );
//      places = sb.loadLocationResult(destination, query, polyline);     
      
      
      /****************************************************************************
       * DEMO COMMENT BLOCK
       ****************************************************************************/
      
      destination = "Hartford";
      query = "Starbucks";
      polyline = "u~b{Fhwb|LqGkEtJmZtBcHEc@{E_DwEaE{BoCYWgCoETe@FiAYuCeCgJy@_NSoAi@w@e@Uw@CuBKcAg@[[qDcHg@q@wJsVuAuD_EgMo@mI[}NMcC?m@o@qDeDyOWcA}CwN_DkNsDaL}AmDiAiBiFuHiEuEeHcFwOqHkFoCm[eQwN}JuX}RwFgDuEkBaGuAoE_A{NyCiGm@}BAwADmOpAsIrAiGE{Qe@_Hb@mHfAoLlBaJzAuCTeCFwGAmHe@{Pe@eBIiIs@mDo@kAUiWoHaDeAuB]}M}AoXeDqFq@cEq@iFiBaCgAyFyDyDmDsE{F}CqFmDwImB{Gu@oDqBmRsF}r@g@mF_AiGsAaGyD}K_DqGkCyD{DiEgC}BcEeCiGwCqL_E{[iLcNcFsE_CmKyH}DkDiJoIyC}CgFqH}D_IeCaGeJwZaNuc@cFcMuA_DwHeNkAsBwH}M_FcIcHaJ}LyLwc@qb@{`@}_@kLoL{DkDqHcFcHcDcIkCaMsDm~@aXyw@iU}IoC_NcDoGy@gKs@sMG{FVy\\hCcj@hEoI^ma@l@_FTaj@zGaCl@wDh@_CPmDKoDk@yU_EkXuE{FiAsCaAiCaBuAaA_CiCyCiFgF{J_D_GuCwF_@q@gDcGaD_HiS}`@w@sAoAsBsCoFqC}DaEwEsIoGaH{CcIyEwScJ_MoGaGmDeCkBcE}DeKoMaS{a@{E{IyLkSmGmHsFiGqHcH{IsHah@}\\sSoPmBmAmEgDoFaDiGgCmI{BwX_FwJ{AyLcC}E_B{GkDkXwOwWiOaXkOoLkG}EkB{O}HiMsHoFyDkCqBkUeRq@k@}LaIgIyCeMwDeQmGcF_DwG_Fs_@e_@aA}@mE{EmIwH_EwBsE{BoCaA_FmAkEw@q[cEeHeAsCi@gEmAiDkAcOsFeFeC}P{Kgc@sZuLkIkEsDmE}DgMqMeHoGoC{Bc@_@kVcLgMqFo_Acb@sGeBmEm@iBOcC@aF^oFv@uKxAeWtDwAZ_HtBuGtCuHdDgCjAsL|EiIzBgTbDeDj@{BdA_DfBgDbD}EvG{HfKyLdSaKzPaDzCaClByBjAa@R{DtAoCj@{FLcIk@gDw@kNsDmFgBaDqAgDsBuB}AoEmDaJyGsF{BqB_@{BUwGDeCVyBf@mBr@eBz@o@j@gBhBuLlN}@~AqHrHiGpF}@|A[pAk@zH}@pCcA~EW~BeAxL_BlLu@fDq@v@{@Fa@VO^?z@P^ZPn@Dl@a@j@C|BbArAh@}@jFeDjQc@|BiBbNg@xE}@A";      
      //[32] = 41.319330,-72.893260
      Place p1 = new Place("Starbucks", "1234567890", 41319330, -72893260, 
    		  "1068-1070 Chapel Street, New Haven, CT, United States", 1.5, 3);
      
      //[64] = 41.368030,-72.869630
      Place p2 = new Place("Starbucks", "0123456789", 41368030, -72869630, 
    		  "200 Universal Drive North, North Haven, CT, United States", 3, 2);
      
      //[120] = 41.440440,-72.794350
      Place p3 = new Place("Starbucks", "4567890123", 41440440,-72794350, 
    		  "166 Washington Avenue, North Haven, CT, United States", 12, 2);
      
      //[198] = 41.611950,-72.698720
      Place p4 = new Place("Starbucks", "7890123456", 41611950, -72698720, 
    		  "412 Cromwell Avenue, Rocky Hill, CT, United States", 24, 5);
      
      //[222] = 41.653570,-72.677720
      Place p5 = new Place("Starbucks", "7890123456", 41653570,-72677720, 
    		  "1090 Silas Deane Highway, Wethersfield, CT, United States", 28, 3);
      
      //[280] = 41.747720,-72.658880
      Place p6 = new Place("Starbucks", "9012345678", 41747720,-72658880, 
    		  "185 Asylum Street, Hartford, CT, United States", 37, 3);
      
      //[320] = 41.763410,-72.685300
      Place p7 = new Place("Starbucks", "9012345678", 41763410,-72685300, 
    		  "200 Columbus Blvd, Hartford, CT, United States", 39, 3);
      
      places.add(p1);
      places.add(p2);
      places.add(p3);
      places.add(p4);
      places.add(p5);
      places.add(p6);
      places.add(p7);
      
      /****************************************************************************
       * END DEMO BLOCK
       ****************************************************************************/
      
      return "";
    }   
    
    //after background task completes
    protected void onPostExecute(final String trash) {
      
      //fire event back to main handler
      searchHandler.postDelayed(new Runnable() {
         public void run() {
           
           //create new intent to show results page
           Intent intent = new Intent(mainContext, ListResultsActivity.class);
           
           //load intent with our polyline
           intent.putExtra("polyline", polyline);
           
           //load the intent with our results data
           ArrayList <Place> rpack = new ArrayList <Place>();
           for (int i = 0; i < places.size(); i++)
             rpack.add (places.get(i));
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
