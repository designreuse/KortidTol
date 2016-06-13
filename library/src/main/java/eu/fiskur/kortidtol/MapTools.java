package eu.fiskur.kortidtol;

import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapTools {

  private static Polyline sectionPolyline = null;

  public static LatLngBounds createBounds(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
    return new LatLngBounds(new LatLng(minLatitude, minLongitude),
        new LatLng(maxLatitude, maxLongitude));
  }

  public static LatLngBounds createBounds(LatLng[] coordinates) {
    LatLngBounds.Builder builder = LatLngBounds.builder();
    for (LatLng coordinate : coordinates) {
      builder.include(coordinate);
    }
    return builder.build();
  }

  public static void limitBounds(GoogleMap map, CameraPosition cameraPosition, LatLngBounds bounds, float minZoom) {
    limitBounds(map, cameraPosition, bounds, minZoom, null);
  }

  public static void limitBounds(GoogleMap map, CameraPosition cameraPosition, LatLngBounds bounds, float minZoom, LatLng centre) {
    if (bounds.contains(map.getCameraPosition().target) && cameraPosition.zoom >= minZoom) {
      return;
    }

    float targetZoom = cameraPosition.zoom;
    if (cameraPosition.zoom < minZoom) {
      targetZoom = minZoom;
    }

    if (centre != null) {
      map.animateCamera(CameraUpdateFactory.newLatLngZoom(centre, targetZoom));
    } else {
      double targetLong = cameraPosition.target.longitude;
      if (cameraPosition.target.longitude < bounds.southwest.longitude) {
        targetLong = bounds.southwest.longitude;
      } else if (cameraPosition.target.longitude > bounds.northeast.longitude) {
        targetLong = bounds.northeast.longitude;
      }

      double targetLat = cameraPosition.target.latitude;
      if (cameraPosition.target.latitude < bounds.southwest.latitude) {
        targetLat = bounds.southwest.latitude;
      } else if (cameraPosition.target.latitude > bounds.northeast.latitude) {
        targetLat = bounds.northeast.latitude;
      }

      LatLng targetLatLng = new LatLng(targetLat, targetLong);
      map.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, targetZoom));
    }
  }

  public static int nearestIndex(List<LatLng> coords, LatLng target){
    int nearestIndex = 0;
    float minDistance = Float.MAX_VALUE;
    int index = 0;
    for(LatLng coord : coords){
      float distance = distanceBetween(coord, target);
      if(distance < minDistance){
        minDistance = distance;
        nearestIndex = index;
      }
      index++;
    }

    return nearestIndex;
  }

  public static void drawSubsection(GoogleMap map, List<LatLng> coords, LatLng start, LatLng end, int color){
    int startIndex = nearestIndex(coords, start);
    int endIndex = nearestIndex(coords, end);
    drawSubsection(map, coords, startIndex, endIndex, color);
  }

  public static void drawSubsection(GoogleMap map, List<LatLng> coords, int startIndex, int endIndex, int color){
    if(sectionPolyline != null){
      sectionPolyline.remove();
    }

    PolylineOptions line = new PolylineOptions();
    line.geodesic(true);
    line.width(14);
    line.zIndex(100);
    line.color(color);

    int start = startIndex;
    int end = endIndex;

    if(startIndex > endIndex){
      int temp = startIndex;
      start = endIndex;
      end = temp;
    }

    for(LatLng ll : coords.subList(start, end)){
      line.add(ll);
    }

    sectionPolyline = map.addPolyline(line);
  }

    public static void removeSubsection(){
        if(sectionPolyline != null){
            sectionPolyline.remove();
        }
    }

  /*
    Returns distance in meters of a subsection on a route (not as the crow flies)
   */
  public static float subsectionDistance(List<LatLng> coords, LatLng start, LatLng end){
    int startIndex = nearestIndex(coords, start);
    int endIndex = nearestIndex(coords, end);
    return subsectionDistance(coords, startIndex, endIndex);
  }
  public static float subsectionDistance(List<LatLng> coords, int startIndex, int endIndex){
    float distance = 0;
    LatLng last = coords.get(startIndex);

    int start = startIndex;
    int end = endIndex;

    if(startIndex > endIndex){
      int temp = startIndex;
      start = endIndex;
      end = temp;
    }

    for(LatLng current : coords.subList(start, end)){
      distance += distanceBetween(last, current);
      last = current;
    }
    return distance;
  }

  public static float distanceBetween(LatLng a, LatLng b){
    float[] results = new float[1];
    Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results);
    return results[0];
  }

    public static float metersToMiles(float meters){
        return round((float) (meters / 1609.344), 2);
    }

    public static float metersToKM(float meters){
        return round((float) (meters / 1000), 2);
    }

    private static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }


  /*
      From: http://stackoverflow.com/a/31791765

      Rather than use expensive JSONObject or GSON + Pojos just use a regular expression to extract
      latitude and longitude pairs from JSON in the format: [a,b]

   */
  public static List<LatLng> getPoints(String jsonStr) {
    List<LatLng> points = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\[([^\\[\\],]*),([^\\[\\],]*)]");
    Matcher latLonMatcher = pattern.matcher(jsonStr);
    while (latLonMatcher.find()) {
      double v1 = Double.parseDouble(latLonMatcher.group(1));
      double v2 = Double.parseDouble(latLonMatcher.group(2));

      if (v1 > v2) {
        points.add(new LatLng(v1, v2));
      } else {
        points.add(new LatLng(v2, v1));
      }
    }

    return points;
  }

  /*
       Get the center of an array of LatLng points, more accurate than using bounds as outliers
       won't influence the result as much
   */
  public static LatLng getCenter(List<LatLng> points) {
    double totalLatitude = 0;
    double totalLongitude = 0;
    for (LatLng point : points) {
      totalLatitude += point.latitude;
      totalLongitude += point.longitude;
    }

    return new LatLng(totalLatitude / points.size(), totalLongitude / points.size());
  }
}
