package eu.fiskur.kortidtol;
/*
 * Copyright (C) 2014 Jared Rummler <jared@jrummyapps.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;

/*
    Quick Hull method taken from: http://jaredrummler.com/2014/11/13/android-bitmaps-convex-hull/

    More details: http://fiskurgit.github.io/convex_hull_from_geojson/
    Converted to use GoogleMap LatLng objects.
 */
public class LatLngQuickHull {

  public ArrayList<LatLng> quickHull(ArrayList<LatLng> points) {

    final ArrayList<LatLng> convexHull = new ArrayList<>();

    if (points.size() < 3) {
      ArrayList<LatLng> clonedList = new ArrayList<>();
      Collections.copy(clonedList, points);
      return clonedList;
    }

    int minPoint = -1, maxPoint = -1;
    double minX = Double.MAX_VALUE;
    double maxX = Double.MIN_VALUE;
    for (int i = 0; i < points.size(); i++) {
      if (points.get(i).longitude < minX) {
        minX = points.get(i).longitude;
        minPoint = i;
      }
      if (points.get(i).longitude > maxX) {
        maxX = points.get(i).longitude;
        maxPoint = i;
      }
    }
    final LatLng a = points.get(minPoint);
    final LatLng b = points.get(maxPoint);
    convexHull.add(a);
    convexHull.add(b);
    points.remove(a);
    points.remove(b);

    ArrayList<LatLng> leftSet = new ArrayList<>();
    ArrayList<LatLng> rightSet = new ArrayList<>();

    for (int i = 0; i < points.size(); i++) {
      LatLng p = points.get(i);
      if (pointLocation(a, b, p) == -1) leftSet.add(p);
      else rightSet.add(p);
    }
    hullSet(a, b, rightSet, convexHull);
    hullSet(b, a, leftSet, convexHull);

    return convexHull;
  }

  private static double distance(LatLng a, LatLng b, LatLng c) {
    final double ABx = b.longitude - a.longitude;
    final double ABy = b.latitude - a.latitude;
    double num = ABx * (a.latitude - c.latitude) - ABy * (a.longitude - c.longitude);
    if (num < 0) num = -num;
    return num;
  }

  private static void hullSet(LatLng a, LatLng b, ArrayList<LatLng> set, ArrayList<LatLng> hull) {
    final int insertPosition = hull.indexOf(b);
    if (set.size() == 0) return;
    if (set.size() == 1) {
      final LatLng p = set.get(0);
      set.remove(p);
      hull.add(insertPosition, p);
      return;
    }
    double dist = Double.MIN_VALUE;
    int furthestPoint = -1;
    for (int i = 0; i < set.size(); i++) {
      LatLng p = set.get(i);
      double distance = distance(a, b, p);
      if (distance > dist) {
        dist = distance;
        furthestPoint = i;
      }
    }
    final LatLng p = set.get(furthestPoint);
    set.remove(furthestPoint);
    hull.add(insertPosition, p);

    // Determine who's to the left of AP
    final ArrayList<LatLng> leftSetAP = new ArrayList<>();
    for (int i = 0; i < set.size(); i++) {
      final LatLng m = set.get(i);
      if (pointLocation(a, p, m) == 1) {
        leftSetAP.add(m);
      }
    }

    // Determine who's to the left of PB
    final ArrayList<LatLng> leftSetPB = new ArrayList<>();
    for (int i = 0; i < set.size(); i++) {
      final LatLng m = set.get(i);
      if (pointLocation(p, b, m) == 1) {
        leftSetPB.add(m);
      }
    }
    hullSet(a, p, leftSetAP, hull);
    hullSet(p, b, leftSetPB, hull);
  }

  private static double pointLocation(LatLng a, LatLng b, LatLng p) {
    double cp1 = (b.longitude - a.longitude) * (p.latitude - a.latitude) - (b.latitude - a.latitude) * (p.longitude - a.longitude);
    return (cp1 > 0) ? 1 : -1;
  }
}

