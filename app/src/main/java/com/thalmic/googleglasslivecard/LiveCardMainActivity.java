package com.thalmic.googleglasslivecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LiveCardMainActivity extends Activity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "running on Glass");
        startService(new Intent(this, LiveCardService.class));
        finish();
    }
}
