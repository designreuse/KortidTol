package eu.fiskur.kortidtol;

import com.google.android.gms.maps.model.LatLng;

/**
 * Simple class for managing long-click events with a map to find a route section.
 */
public class LongClickHelper {
  private LatLng start;
  private LatLng end;

  private int count = 10;

  public void addPoint(LatLng point){
    if((count%2)==0){
      start = point;
    }else{
      end = point;
    }
    count++;
  }

  public boolean isReady(){
    return 0 == (count & 1);
  }

  public LatLng getStart(){
    return start;
  }

  public LatLng getEnd(){
    return end;
  }

}
