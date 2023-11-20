package com.example.parcial;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import android.widget.LinearLayout;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ForecastActivityTest {

    @Test
    public void forecastActivityCreation() {
        ForecastActivity activity = Robolectric.buildActivity(ForecastActivity.class).create().start().resume().visible().get();

        assertNotNull(activity);

        assertNotNull(activity.findViewById(R.id.forecastLayout));

        assertEquals(5, ((LinearLayout) activity.findViewById(R.id.forecastLayout)).getChildCount());
    }
}