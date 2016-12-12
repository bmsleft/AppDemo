package com.demo.bms.appdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MyFragmentPageAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mViewPager = (ViewPager) findViewById(R.id.main_page);

        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new MyFragmentPageAdapter(fm);
        mViewPager.setAdapter(mAdapter);
    }




    private class MyFragmentPageAdapter extends FragmentStatePagerAdapter {
        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
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
                default:
                    return null;
            }
        }

    }
}


