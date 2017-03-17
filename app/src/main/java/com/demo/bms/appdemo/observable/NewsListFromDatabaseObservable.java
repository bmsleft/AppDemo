package com.demo.bms.appdemo.observable;

import com.demo.bms.appdemo.BmsZhihuApp;
import com.demo.bms.appdemo.bean.DailyNews;

import java.util.List;

import rx.Observable;

/**
 * Created by bms on 17-3-17.
 */

public class NewsListFromDatabaseObservable {
    public static Observable<List<DailyNews>> ofDate(String date) {
        return Observable.create(subscriber -> {
            List<DailyNews> newsList
                    = BmsZhihuApp.getDataSource().newsOfTheDay(date);

            if (newsList != null) {
                subscriber.onNext(newsList);
            }
            subscriber.onCompleted();
        });
    }
}
