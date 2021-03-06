package com.demo.bms.appdemo.observable;

import android.graphics.Path;
import android.text.TextUtils;
import android.util.Pair;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.demo.bms.appdemo.bean.DailyNews;
import com.demo.bms.appdemo.bean.Question;
import com.demo.bms.appdemo.bean.Story;
import com.demo.bms.appdemo.support.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.demo.bms.appdemo.observable.Helper.getHtml;
import static com.demo.bms.appdemo.observable.Helper.toNonempty;

/**
 * Created by bms on 17-3-17.
 */

public class NewsListFromZhihuObservable {
    private static final String QUESTION_SELECTOR = "div.question";
    private static final String QUESTION_TITLES_SELECTOR = "h2.question-title";
    private static final String QUESTION_LINKS_SELECTOR = "div.view-more a";

    public static Observable<List<DailyNews>> ofDate(String date) {
        Observable<Story> stories = getHtml(Constants.Urls.ZHIHU_DAILY_BEFORE, date)
                .flatMap(NewsListFromZhihuObservable::getStoriesJsonArrayObservable)
                .flatMap(NewsListFromZhihuObservable::getStoriesObservable);

        Observable<Document> documents = stories
                .flatMap(NewsListFromZhihuObservable::getDocumentObservable);

        Observable<Optional<Pair<Story, Document>>> optionalStoryDocuments
                = Observable.zip(stories, documents, NewsListFromZhihuObservable::createPair);

        Observable<Pair<Story, Document>> storyDocuments = toNonempty(optionalStoryDocuments);

        return toNonempty(storyDocuments.map(NewsListFromZhihuObservable::convertToDailyNews))
                .doOnNext(dailyNews -> dailyNews.setDate(date))
                .toList();
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

    private static Observable<Document> getDocumentObservable(Story news) {
        return getHtml(Constants.Urls.ZHIHU_DAILY_OFFLINE_NEWS, news.getStoryId())
                .map(NewsListFromZhihuObservable::getStoryDocument);
    }

    private static Document getStoryDocument(String json) {
        try {
            JSONObject newsJson = new JSONObject(json);
            return newsJson.has("body") ? Jsoup.parse(newsJson.getString("body")) : null;
        } catch (JSONException e) {
            return null;
        }
    }

    private static Optional<Pair<Story, Document>> createPair(Story story, Document document) {
        return Optional.ofNullable(document == null ? null : Pair.create(story, document));
    }

    private static Optional<DailyNews> convertToDailyNews(Pair<Story, Document> pair) {
        DailyNews result = null;

        Story story = pair.first;
        Document document = pair.second;
        String dailyTitle = story.getDailyTitle();

        List<Question> questions = getQuestions(document, dailyTitle);
        if (Stream.of(questions).allMatch(Question::isValidZhihuQuestion)) {
            result = new DailyNews();

            result.setDailyTitle(dailyTitle);
            result.setThumbnailUrl(story.getThumbnaiUrl());
            result.setQuestions(questions);
        }

        return Optional.ofNullable(result);
    }

    private static List<Question> getQuestions(Document document, String dailyTitle) {
        List<Question> result = new ArrayList<>();
        Elements questionElements = getQuestionsElements(document);

        for (Element questionElement : questionElements) {
            Question question = new Question();

            String questionTitle = getQuestionTitleFromQuestionElement(questionElement);
            String questionUrl = getQuestionUrlFromQuesionElement(questionElement);

            questionTitle = TextUtils.isEmpty(questionTitle) ? dailyTitle : questionTitle;

            question.setTitle(questionTitle);
            question.setUrl(questionUrl);

            result.add(question);
        }

        return result;
    }

    private static Elements getQuestionsElements(Document document) {
        return document.select(QUESTION_SELECTOR);
    }

    private static String getQuestionTitleFromQuestionElement(Element questionElement) {
        Element questionTitleElement = questionElement.select(QUESTION_TITLES_SELECTOR).first();
        if (questionTitleElement == null) {
            return null;
        } else {
            return questionTitleElement.text();
        }
    }

    private static String getQuestionUrlFromQuesionElement(Element questionElement) {
        Element viewMoreElement = questionElement.select(QUESTION_LINKS_SELECTOR).first();

        if (null == viewMoreElement) {
            return null;
        } else {
            return viewMoreElement.attr("href");
        }
    }
}
