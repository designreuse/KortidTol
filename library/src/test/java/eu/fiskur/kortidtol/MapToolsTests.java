package eu.fiskur.kortidtol;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MapToolsTests {

    private static final LatLng SW = new LatLng(53.6793938405, -2.2186232802);
    private static final LatLng NW = new LatLng(53.758891677, -2.2186232802);
    private static final LatLng NE = new LatLng(53.758891677, -2.0749710802);
    private static final LatLng SE = new LatLng(53.6793938405, -2.0749710802);
    private static final LatLng TOD = new LatLng(53.7098827,-2.1715071);
    private static final LatLng SOURHALL = new LatLng(53.7184144,-2.1279741);


    @Mock
    Location location;

    @Test
    public void testBoundsFromDoubles(){
        LatLngBounds bounds = MapTools.createBounds(53.6793938405, 53.758891677, -2.2186232802, -2.0749710802);
        Assert.assertEquals(53.6793938405, bounds.southwest.latitude);
        Assert.assertEquals(-2.2186232802, bounds.southwest.longitude);
        Assert.assertEquals(53.758891677, bounds.northeast.latitude);
        Assert.assertEquals(-2.0749710802, bounds.northeast.longitude);
    }

    @Test
    public void testBoundsFromLatLngs(){
        LatLngBounds bounds = MapTools.createBounds(new LatLng[]{SW, NW, NE, SE, TOD});
        Assert.assertEquals(53.6793938405, bounds.southwest.latitude);
        Assert.assertEquals(-2.2186232802, bounds.southwest.longitude);
        Assert.assertEquals(53.758891677, bounds.northeast.latitude);
        Assert.assertEquals(-2.0749710802, bounds.northeast.longitude);
    }


    @Test
    public void testDistanceBetweenAndroidAPI(){
        float[] results = new float[1];
        location.distanceBetween(SOURHALL.latitude, SOURHALL.longitude, TOD.latitude, TOD.longitude, results);
        Assert.assertEquals(2.0, results[0]);
    }

    @Test
    public void testDistanceToAndroidAPI(){
        Location locA = new Location("A");
        locA.setLatitude(SOURHALL.latitude);
        locA.setLongitude(SOURHALL.longitude);
        Location locB = new Location("B");
        locB.setLatitude(TOD.latitude);
        locB.setLongitude(TOD.longitude);
        float distance = locA.distanceTo(locB);
        Assert.assertEquals(2.0, distance);
    }

    @Test
    public void testDistanceBetween(){
        float distance = MapTools.distanceBetween(TOD, SOURHALL);
        Assert.assertEquals(2.0, distance);
    }

    @Test
    public void testDistanceCalc(){
        List<LatLng> todPath = new ArrayList<>();
        todPath.add(SW);
        todPath.add(NW);
        todPath.add(NE);
        todPath.add(SE);
        todPath.add(TOD);

        float distance = MapTools.subsectionDistance(todPath, 1, 4);

        Assert.assertEquals(2.0, distance);
    }

    @Test
    public void testNearestIndex(){
        List<LatLng> todPath = new ArrayList<>();
        todPath.add(SW);
        todPath.add(NW);
        todPath.add(NE);
        todPath.add(SE);
        todPath.add(TOD);
        int nearestIndex = MapTools.nearestIndex(todPath, SOURHALL);

        Assert.assertEquals(4, nearestIndex);
    }
}
