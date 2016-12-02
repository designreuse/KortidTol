package eu.fiskur.kortidtol.concavehull;

import android.graphics.Point;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.ArrayList;
import java.util.List;

public class ConcaveHull {

  private List<LatLng> polygonPoints;
  private double threshold;
  private Projection projection;

  public ConcaveHull(List<LatLng> polygonPoints, double threshold, Projection projection){
    this.polygonPoints = polygonPoints;
    this.threshold = threshold;
    this.projection = projection;
  }

  public List<LatLng> getHull(){

    Coordinate[] coords = convertToCoords(polygonPoints, projection);
    GrossoConcaveHull ch = new GrossoConcaveHull(new GeometryFactory().createMultiPoint(coords), threshold);

    Geometry concaveHull = ch.getConcaveHull();

    Coordinate[] hullCoords = concaveHull.getCoordinates();
    List<LatLng> hullLatLngs = convertToLatLng(hullCoords);
    return hullLatLngs;
  }

  private Coordinate[] convertToCoords(List<LatLng> latLngs, Projection projection){
    int count = latLngs.size();
    Coordinate[] coords = new Coordinate[count];
    int index = 0;
    for(LatLng latLng : latLngs){
      Point p = projection.toScreenLocation(latLng);
      coords[index] = new Coordinate(p.x, p.y);
        index++;
    }

    return coords;
  }
  
  private List<LatLng> convertToLatLng(Coordinate[] coords){
    List<LatLng> latlngs = new ArrayList<>();

    for(Coordinate c : coords){
      Point p = new Point((int) c.x, (int) c.y);
      latlngs.add(projection.fromScreenLocation(p));
    }

    return latlngs;
  }
}
