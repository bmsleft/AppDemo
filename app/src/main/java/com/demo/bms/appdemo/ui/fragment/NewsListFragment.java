package com.demo.bms.appdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.bms.appdemo.R;
import com.demo.bms.appdemo.adapter.NewsAdapter;
import com.demo.bms.appdemo.bean.DailyNews;
import com.demo.bms.appdemo.support.Constants;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;


/**
 * Created by bms on 16-12-30.
 */

public class NewsListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, Observer<List<DailyNews>> {


    private List<DailyNews> newsList = new ArrayList<>();
    private String date;

    private NewsAdapter mAdapter;

    private boolean isToday;
    private boolean isRefreshed = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            date = bundle.getString(Constants.BundleKeys.DATE);
            isToday = bundle.getBoolean(Constants.BundleKeys.IS_FIRST_PAGE);

            setRetainInstance(true);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        assert view != null;
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.news_list);
        mRecyclerView.setHasFixedSize(!isToday);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        // to add para
        mAdapter = new NewsAdapter(newsList);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_primary);

        return view;

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onNext(List<DailyNews> dailyNewses) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onCompleted() {

    }
}
