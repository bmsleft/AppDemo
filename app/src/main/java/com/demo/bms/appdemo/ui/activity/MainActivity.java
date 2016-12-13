package com.demo.bms.appdemo.ui.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;

import com.demo.bms.appdemo.ui.fragment.MyFragment;
import com.demo.bms.appdemo.R;

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

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_pick_data);
        assert floatingActionButton != null;

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

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
            switch (position) {
                case 0:
                    return MyFragment.newInstance(position);
                case 1:
                    return MyFragment.newInstance(position);
                case 2:
                    return MyFragment.newInstance(position);
                case 3:
                    return MyFragment.newInstance(position);
                case 4:
                    return MyFragment.newInstance(position);
                case 5:
                    return MyFragment.newInstance(position);
                case 6:
                    return MyFragment.newInstance(position);

                default:
                    return null;
            }
        }

    }

}


