package com.example.parcial;

import org.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import android.view.View;
import android.widget.Button;

import static org.junit.Assert.*;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class homeActivityTest {

    @Test
    public void testRefreshButtonClick() {
        homeActivity activity = Robolectric.buildActivity(homeActivity.class).create().start().resume().visible().get();

        Button refreshButton = activity.findViewById(R.id.refreshButton);

        assertNotNull(refreshButton);

        refreshButton.performClick();

        activity.finish();
    }
}