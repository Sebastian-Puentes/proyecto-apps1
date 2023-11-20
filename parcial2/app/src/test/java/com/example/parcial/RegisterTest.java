package com.example.parcial;

import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class RegisterTest {

    private Register activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(Register.class).create().start().resume().visible().get();
    }

    @Test
    public void testRegisterButtonClick() {
        Button registerButton = activity.findViewById(R.id.btn_register);

        assertNotNull(registerButton);

        registerButton.performClick();

        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        assertTrue(progressBar.getVisibility() == ProgressBar.VISIBLE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assertNull(currentUser);

    }
}
