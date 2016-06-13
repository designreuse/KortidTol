package eu.fiskur.kortidtol;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Test;

public class LongClickTests {

    @Test
    public void testLongClickNotReady(){
        LongClickHelper lch = new LongClickHelper();
        Assert.assertFalse(lch.isReady());

        lch.addPoint(new LatLng(0, 0));
        Assert.assertFalse(lch.isReady());

        lch.addPoint(new LatLng(0, 0));
        Assert.assertTrue(lch.isReady());

    }
}
