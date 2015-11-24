package com.sphericalelephant.example.androidtesting.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sphericalelephant.example.androidtesting.R;

/**
 * Created by siyb on 24/11/15.
 */
public class MainActivity extends AppCompatActivity {
    protected static final String SHAREDPREFERENCES = "SHAREDPREFS";

    protected static final String SHARED_PREFERENCES_KEY = "com.sphericalelephant.example.androidtesting.activity.MainActivity.SHARED_PREFERENCES_KEY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);
        final SharedPreferences sp = getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);

        final EditText et = (EditText) findViewById(R.id.activity_mainactivity_et_input1);

        // clicking the TextView changes its content
        findViewById(R.id.activity_mainactivity_tv_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) v).setText(R.string.activity_mainactivity_textviewclicked);
            }
        });

        // this button's listener will write the content of the EditText to the SharedPreferences
        findViewById(R.id.activity_mainactivity_b_normalbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(SHARED_PREFERENCES_KEY, et.getText().toString()).apply();
            }
        });
    }
}
