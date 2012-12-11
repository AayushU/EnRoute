package com.example.enroute;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;


public class PlaceItemizedOverlay extends ItemizedOverlay {
  
  private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
  private Context mContext;
  
  public PlaceItemizedOverlay(Drawable defaultMarker) {
    super(defaultMarker);
  }
  
  public PlaceItemizedOverlay(Drawable defaultMarker, Context context) {
      super(boundCenterBottom(defaultMarker));
      this.mContext = context;
      
  }

  public void addOverlay(OverlayItem overlay) {
    mOverlays.add(overlay);
    populate();
}

  public void clearOverlays(){
    mOverlays.clear();
  }
  
  @Override
  protected OverlayItem createItem(int i) {
    // TODO Auto-generated method stub
    return mOverlays.get(i);
  }
  
  @Override
  public int size() {
    return mOverlays.size();
  }
  
  @Override
  protected boolean onTap(int index) {
    final OverlayItem item = mOverlays.get(index);
    
    
    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
    dialog.setTitle(item.getTitle())
    .setMessage(item.getSnippet())
    .setPositiveButton(R.string.directions, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                 
                 //get address (re-calculated from lat/long here.. not ideal)
                 String addr = item.routableAddress();
                 
                 //fire intent to google navigator
                 Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                     Uri.parse("google.navigation:q=" + addr) );
                 mContext.startActivity(intent);
                 
               }
           })
           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                  //nothing to do here
               }
           });

    dialog.show();
    return true;
  }

}
