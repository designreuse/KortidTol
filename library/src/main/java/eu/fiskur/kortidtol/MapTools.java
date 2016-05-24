package eu.fiskur.kortidtol;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapTools {

  /*
      From: http://stackoverflow.com/a/31791765

      Rather than use expensive JSONObject or GSON + Pojos just use a regular expression to extract
      latitude and longitude pairs from JSON

   */
  public static List<LatLng> getPoints(String jsonStr){
    List<LatLng> points = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\[([^\\[\\],]*),([^\\[\\],]*)]");
    Matcher latLonMatcher = pattern.matcher(jsonStr);
    while (latLonMatcher.find()) {
      double v1 = Double.parseDouble(latLonMatcher.group(1));
      double v2 = Double.parseDouble(latLonMatcher.group(2));

      if(v1 > v2){
        points.add(new LatLng(v1, v2));
      }else{
        points.add(new LatLng(v2, v1));
      }
    }

    return points;
  }

  /*
       Get the center of an array of LatLng points, more accurate than using bounds as outliers
       won't influence the result as much
   */
  public static LatLng getCenter(List<LatLng> points){
    double totalLatitude = 0;
    double totalLongitude = 0;
    for(LatLng point : points){
      totalLatitude += point.latitude;
      totalLongitude += point.longitude;
    }

    return new LatLng(totalLatitude/points.size(), totalLongitude/points.size());
  }

  //Untested
  public static LatLngBounds getBounds(List<LatLng> points){
    LatLng sw;
    LatLng ne;

    double highestLat = 0;
    double highestLon = 0;
    double lowestLat = Double.MAX_VALUE;
    double lowestLon = Double.MAX_VALUE;

    for(LatLng point : points){
      double thisLat = point.latitude;
      double thisLon = point.longitude;

      if(thisLat > highestLat){
        highestLat = thisLat;
      }
      if(thisLon > highestLon){
        highestLon = thisLon;
      }
      if(thisLat < lowestLat){
        lowestLat = thisLat;
      }
      if(thisLon < lowestLon){
        lowestLon = thisLon;
      }
    }

    sw = new LatLng(lowestLat, lowestLon);
    ne = new LatLng(highestLat, highestLon);

    return new LatLngBounds(sw, ne);
  }
}
