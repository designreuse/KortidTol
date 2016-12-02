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
    List<Point> points = convertToPoint(polygonPoints, projection);
    GeometryFactory gf = new GeometryFactory();

    Coordinate[] coords = new Coordinate[points.size()];

    for(int i = 0 ; i < coords.length ; i++){
      Point p = points.get(i);
      coords[i] = new Coordinate(p.x, p.y);
    }

    GrossoConcaveHull ch = new GrossoConcaveHull(gf.createMultiPoint(coords), threshold);
    Geometry concaveHull = ch.getConcaveHull();

    Coordinate[] hullCoords = concaveHull.getCoordinates();
    List<LatLng> hullLatLngs = convertToLatLng(hullCoords);
    return hullLatLngs;
  }

  private List<Point> convertToPoint(List<LatLng> coords, Projection projection){
    List<Point> points = new ArrayList<>();

    for(LatLng coord : coords){
      points.add(projection.toScreenLocation(coord));
    }

    return points;
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
