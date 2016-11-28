package eu.fiskur.kortidtol;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public interface ConvexHullAlgorithm
{
  ArrayList<LatLng> execute(ArrayList<LatLng> points);
}
