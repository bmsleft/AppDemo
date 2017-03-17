package com.demo.bms.appdemo.observable;

import com.demo.bms.appdemo.bean.DailyNews;
import com.demo.bms.appdemo.support.Constants;

import java.util.List;

import rx.Observable;

import static com.demo.bms.appdemo.observable.Helper.getHtml;
import static com.demo.bms.appdemo.observable.Helper.toNewsListObservable;

/**
 * Created by bms on 17-3-17.
 */

public class NewsListFromAccelerateServerObservable {
    public static Observable<List<DailyNews>> ofDate(String date) {
        return toNewsListObservable(getHtml(Constants.Urls.ZHIHU_DAILY_PURIFY_BEFORE, date));
    }
}
