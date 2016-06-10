package eu.fiskur.kortidtol;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class MapToolsUnitTest {

  @Test
  public void boundCalculation() throws Exception {
    List<LatLng> points = new ArrayList<>();
    points.add(new LatLng(54.5682827, -4.8719848));//NW
    points.add(new LatLng(54.5140224, -1.0605092));//NE
    points.add(new LatLng(49.7720708, -5.6709004));//SW
    points.add(new LatLng(50.1985043, 2.104402));//SE
    points.add(new LatLng(52.3959891, -1.2004356));//Middle

    LatLngBounds bounds = MapTools.createBounds((LatLng[])points.toArray());
    Assert.assertEquals(49.7720708, bounds.southwest.latitude);
    Assert.assertEquals(-5.6709004, bounds.southwest.longitude);

    Assert.assertEquals(54.5140224, bounds.northeast.latitude);
    Assert.assertEquals(-1.0605092, bounds.northeast.longitude);
  }
}