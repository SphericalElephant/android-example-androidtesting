package com.sphericalelephant.example.androidtesting.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sphericalelephant.example.androidtesting.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by siyb on 24/11/15.
 */
public class MainActivity extends AppCompatActivity {
    protected static final String SHAREDPREFERENCES = "SHAREDPREFS";

    protected static final String SHARED_PREFERENCES_KEY = "com.sphericalelephant.example.androidtesting.activity.MainActivity.SHARED_PREFERENCES_KEY";

    private MemoryDummyInstance instance = new MemoryDummyInstance(Integer.MAX_VALUE, UUID.randomUUID().toString());

    public static final class MemoryDummyInstance extends MemoryDummy{

        public MemoryDummyInstance(int id, String info) {
            super(id, info);
        }
    }

    private static class MemoryDummy {
        private int id;
        private String info;

        private MemoryDummy(int id, String info) {
            this.id = id;
            this.info = info;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_mainactivity);
        final SharedPreferences sp = getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);

        final EditText et = (EditText) findViewById(R.id.activity_mainactivity_et_input1);

        // clicking the TextView changes its content
        findViewById(R.id.activity_mainactivity_tv_textview).setOnClickListener(v -> {
            ((TextView) v).setText(R.string.activity_mainactivity_textviewclicked);
        });

        // this button's listener will write the content of the EditText to the SharedPreferences
        findViewById(R.id.activity_mainactivity_b_normalbutton).setOnClickListener(v -> {
            sp.edit().putString(SHARED_PREFERENCES_KEY, et.getText().toString()).apply();
        });

        findViewById(R.id.activity_mainactivity_b_cpuload).setOnClickListener(v -> {
            v.setEnabled(false);
            for (int i = 0; i < 100000000; i++) {
                Math.sqrt(i);
            }
            v.setEnabled(true);
        });
        findViewById(R.id.activity_mainactivity_b_memory).setOnClickListener(v -> {
            v.setEnabled(false);
            Runnable r = () -> {
                List<MemoryDummy> l = new ArrayList<>(10000);
                for (int i = 0; i < 10000; i++) {
                    l.add(new MemoryDummy(i, UUID.randomUUID().toString()));
                }
                runOnUiThread(() -> v.setEnabled(true));
            };
            new Thread(r).start();
        });
        findViewById(R.id.activity_mainactivity_b_network).setOnClickListener(v -> {
            v.setEnabled(false);
            queue.add(new StringRequest(Request.Method.GET, "http://www.google.com",
                    response -> {
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        v.setEnabled(true);
                    },
                    error -> {
                        Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        v.setEnabled(true);
                    }
            ));
        });

        findViewById(R.id.activity_mainactivity_b_tracing).setOnClickListener(v -> {
            v.setEnabled(false);
            Debug.startMethodTracing("sample_trace");
            a();
            Debug.stopMethodTracing();
            v.setEnabled(true);
        });
    }

    private void a() {
        b();
    }
    private void b() {
        c();
    }
    private void c() {
        d();
    }
    private void d() {
        Toast.makeText(this, R.string.activity_mainactivity_programmatical_tracing_complete, Toast.LENGTH_SHORT).show();
    }
}
