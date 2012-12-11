package com.example.enroute;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

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
      mContext = context;
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
    OverlayItem item = mOverlays.get(index);
    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
    dialog.setTitle(item.getTitle());
    dialog.setMessage(item.getSnippet());
    dialog.show();
    return true;
  }

}
