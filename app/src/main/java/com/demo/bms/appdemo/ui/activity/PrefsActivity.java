package com.demo.bms.appdemo.ui.activity;

import android.os.Bundle;

import com.demo.bms.appdemo.R;
import com.demo.bms.appdemo.ui.fragment.PrefsFragment;

/**
 * Created by bms on 16-12-14.
 */

public class PrefsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, new PrefsFragment())
                .commit();

    }
}
