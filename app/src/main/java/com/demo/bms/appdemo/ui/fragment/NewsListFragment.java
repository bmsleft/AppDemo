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

import com.demo.bms.appdemo.BmsZhihuApp;
import com.demo.bms.appdemo.R;
import com.demo.bms.appdemo.adapter.NewsAdapter;
import com.demo.bms.appdemo.bean.DailyNews;
import com.demo.bms.appdemo.observable.NewsListFromAccelerateServerObservable;
import com.demo.bms.appdemo.observable.NewsListFromDatabaseObservable;
import com.demo.bms.appdemo.observable.NewsListFromZhihuObservable;
import com.demo.bms.appdemo.support.Constants;
import com.demo.bms.appdemo.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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
    public void onResume() {
        super.onResume();

        NewsListFromDatabaseObservable.ofDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        refreshIf(shouldRefreshOnVisibilityChange(isVisibleToUser));
    }

    private void refreshIf(boolean prerequisite) {
        if (prerequisite) {
            doRefresh();
        }
    }

    @Override
    public void onRefresh() {
        doRefresh();
    }

    private void doRefresh() {
        getNewsListObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

    }

    private Observable<List<DailyNews>> getNewsListObservable() {
        if (shouldSubscribeToZhihu()) {
            return NewsListFromZhihuObservable.ofDate(date);
        } else {
            return NewsListFromAccelerateServerObservable.ofDate(date);
        }
    }

    private boolean shouldSubscribeToZhihu() {
        return isToday || !shouldUseAccelerateServer();
    }

    private boolean shouldUseAccelerateServer() {
        return BmsZhihuApp.getSharedPreferences()
                .getBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_USE_ACCELERATE_SERVER, false);
    }

    private boolean shouldAutoRefresh() {
        return BmsZhihuApp.getSharedPreferences()
                .getBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_AUTO_REFRESH, true);
    }

    private boolean shouldRefreshOnVisibilityChange(boolean isVisibleToUser) {
        return isVisibleToUser && shouldAutoRefresh() && !isRefreshed;
    }

    @Override
    public void onNext(List<DailyNews> dailyNewses) {
        this.newsList = dailyNewses;
    }

    @Override
    public void onError(Throwable e) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (isAdded()) {
            ((BaseActivity) getActivity()).showSnackBar(R.string.network_error);
        }
    }

    @Override
    public void onCompleted() {
        isRefreshed = true;

        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.updateNewsList(newsList);


    }
}
