package com.demo.bms.appdemo.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.demo.bms.appdemo.ui.fragment.MyFragment;
import com.demo.bms.appdemo.R;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends BaseActivity {

    private static final int PAGE_COUNT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);

        TabLayout tabs = (TabLayout) findViewById(R.id.main_page_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_page);
        assert tabs != null;
        assert viewPager != null;

        viewPager.setOffscreenPageLimit(PAGE_COUNT);

        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
//        tabs.setTabMode(TabLayout.MODE_FIXED);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_pick_data);
        assert floatingActionButton != null;
        floatingActionButton.setOnClickListener(view -> prepareIntent(PickDateActivity.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return prepareIntent(PrefsActivity.class);
            case R.id.action_go_to_search:
//                return prepareIntent(PrefsActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean prepareIntent(Class classname) {
        startActivity(new Intent(MainActivity.this, classname));
        return true;
    }

    private class MainPageAdapter extends FragmentStatePagerAdapter {
        public MainPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.newInstance(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Calendar displayDate = Calendar.getInstance();
            displayDate.add(Calendar.DAY_OF_YEAR, -position);
            return (position == 0 ? getString(R.string.zhihu_daily_today) + " " : "")
                    + DateFormat.getDateInstance().format(displayDate.getTime());
        }
    }

}


