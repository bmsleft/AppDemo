package com.demo.bms.appdemo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.bms.appdemo.R;

/**
 * Created by bms on 16-12-12.
 */

public class MyFragment extends Fragment {

    int mNum;
    public static MyFragment newInstance(int num) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_list, null);
        TextView textView = (TextView) view.findViewById(R.id.fm_text);
        textView.setText("this fragment is : " + mNum + " \n" + getResources().getString(R.string.my_test));
        return view;
    }




}
