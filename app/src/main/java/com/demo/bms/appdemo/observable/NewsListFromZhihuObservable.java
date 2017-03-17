package com.demo.bms.appdemo.observable;

import com.demo.bms.appdemo.bean.DailyNews;
import com.demo.bms.appdemo.bean.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import rx.Observable;

/**
 * Created by bms on 17-3-17.
 */

public class NewsListFromZhihuObservable {
    private static final String QUESTION_SELECTOR = "div.question";
    private static final String QUESTION_TITLES_SELECTOR = "h2.question-title";
    private static final String QUESTION_LINKS_SELECTOR = "div.view-more a";

    public static Observable<List<DailyNews>> ofDate(String date) {

    }

    private static Observable<JSONArray> getStoriesJsonArrayObservable(String html) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(new JSONObject(html).getJSONArray("stories"));
                subscriber.onCompleted();
            } catch (JSONException e) {
                subscriber.onError(e);
            }
        });
    }

    private static Observable<Story> getStoriesObservable(JSONArray newsArray) {
        return Observable.create(subscriber -> {
            try {
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject newsJson = newsArray.getJSONObject(i);
                    subscriber.onNext(getStoryFromJSON(newsJson));
                }
                subscriber.onCompleted();
            } catch (JSONException e) {
                subscriber.onError(e);
            }
        });
    }

    private static Story getStoryFromJSON(JSONObject jsonStory) throws JSONException {
        Story story = new Story();
        story.setStoryId(jsonStory.getInt("id"));
        story.setDailyTitle(jsonStory.getString("title"));
        story.setThumbnaiUrl(getThumbnailUrlForStory(jsonStory));

        return story;
    }

    private static String getThumbnailUrlForStory(JSONObject jsonStory) throws JSONException {
        if (jsonStory.has("images")) {
            return (String) jsonStory.getJSONArray("image").get(0);
        } else {
            return null;
        }
    }

    private static Document getStoryDocument(String json) {
        try {
            JSONObject newsJson = new JSONObject(json);
            return newsJson.has("body") ? Jsoup.parse(newsJson.getString("body")) : null;
        } catch (JSONException e) {
            return null;
        }
    }





}
