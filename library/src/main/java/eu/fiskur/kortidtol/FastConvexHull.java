package eu.fiskur.kortidtol;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by fish on 28/11/2016.
 */

public class FastConvexHull implements ConvexHullAlgorithm {
  @Override
  public ArrayList<LatLng> execute(ArrayList<LatLng> points) {
    ArrayList<LatLng> xSorted = (ArrayList<LatLng>) points.clone();
    Collections.sort(xSorted, new XCompare());

    int n = xSorted.size();

    LatLng[] lUpper = new LatLng[n];

    lUpper[0] = xSorted.get(0);
    lUpper[1] = xSorted.get(1);

    int lUpperSize = 2;

    for (int i = 2; i < n; i++) {
      lUpper[lUpperSize] = xSorted.get(i);
      lUpperSize++;

      while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - 3], lUpper[lUpperSize - 2], lUpper[lUpperSize - 1])) {
        // Remove the middle point of the three last
        lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
        lUpperSize--;
      }
    }

    LatLng[] lLower = new LatLng[n];

    lLower[0] = xSorted.get(n - 1);
    lLower[1] = xSorted.get(n - 2);

    int lLowerSize = 2;

    for (int i = n - 3; i >= 0; i--) {
      lLower[lLowerSize] = xSorted.get(i);
      lLowerSize++;

      while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - 3], lLower[lLowerSize - 2], lLower[lLowerSize - 1])) {
        // Remove the middle point of the three last
        lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
        lLowerSize--;
      }
    }

    ArrayList<LatLng> result = new ArrayList<LatLng>();

    for (int i = 0; i < lUpperSize; i++) {
      result.add(lUpper[i]);
    }

    for (int i = 1; i < lLowerSize - 1; i++) {
      result.add(lLower[i]);
    }

    return result;
  }

  private boolean rightTurn(LatLng a, LatLng b, LatLng c) {
    return (b.latitude - a.latitude) * (c.longitude - a.longitude) - (b.longitude - a.longitude) * (c.latitude - a.latitude) > 0;
  }

  private class XCompare implements Comparator<LatLng> {
    @Override
    public int compare(LatLng o1, LatLng o2) {
      return (new Float(o1.latitude)).compareTo(new Float(o2.latitude));
    }
  }
}
