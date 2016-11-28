package eu.fiskur.kortidtol;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/*
  Adapted from http://stackoverflow.com/a/32743889
 */
public class QHull {

  public ArrayList<LatLng> quickHull(ArrayList<LatLng> points) {
    ArrayList<LatLng> sortedPoints = new ArrayList<>();
    Collections.copy(sortedPoints, points);
    Collections.sort(sortedPoints, new Comparator<LatLng>() {
      @Override
      public int compare(LatLng lhs, LatLng rhs) {
        return (Double.valueOf(lhs.latitude).compareTo(rhs.latitude));
      }
    });

    int n = sortedPoints.size();

    LatLng[] lUpper = new LatLng[n];

    lUpper[0] = sortedPoints.get(0);
    lUpper[1] = sortedPoints.get(1);

    int lUpperSize = 2;

    for (int i = 2; i < n; i++) {
      lUpper[lUpperSize] = sortedPoints.get(i);
      lUpperSize++;

      while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - 3], lUpper[lUpperSize - 2], lUpper[lUpperSize - 1])) {
        // Remove the middle point of the three last
        lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
        lUpperSize--;
      }
    }

    LatLng[] lLower = new LatLng[n];

    lLower[0] = sortedPoints.get(n - 1);
    lLower[1] = sortedPoints.get(n - 2);

    int lLowerSize = 2;

    for (int i = n - 3; i >= 0; i--) {
      lLower[lLowerSize] = sortedPoints.get(i);
      lLowerSize++;

      while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - 3], lLower[lLowerSize - 2], lLower[lLowerSize - 1])) {
        // Remove the middle point of the three last
        lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
        lLowerSize--;
      }
    }

    ArrayList<LatLng> result = new ArrayList<>();

    result.addAll(Arrays.asList(lUpper).subList(0, lUpperSize));
    result.addAll(Arrays.asList(lLower).subList(1, lLowerSize - 1));

    return result;
  }

  private boolean rightTurn(LatLng a, LatLng b, LatLng c) {
    return (b.latitude - a.latitude) * (c.longitude - a.longitude) - (b.longitude - a.longitude) * (c.latitude - a.latitude) > 0;
  }
}
