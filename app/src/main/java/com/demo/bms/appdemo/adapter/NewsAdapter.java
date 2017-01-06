package com.demo.bms.appdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.bms.appdemo.R;
import com.demo.bms.appdemo.bean.DailyNews;
import com.demo.bms.appdemo.support.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bms on 16-12-30.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CardViewHolder> {


    private List<DailyNews> newsList;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.noimage)
            .showImageOnFail(R.drawable.noimage)
            .showImageForEmptyUri(R.drawable.lks_for_blank_url)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    NewsAdapter(List<DailyNews> newsList) {
        this.newsList = newsList;
        setHasStableIds(true);
    }

    public void setNewsList(List<DailyNews> newsList) {
        this.newsList = newsList;
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public long getItemId(int position) {
        return newsList.get(position).hashCode();
    }

    public void updateNewsList(List<DailyNews> newsList) {
        setNewsList(newsList);
        notifyDataSetChanged();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.news_list_item, parent, false);

        return new CardViewHolder(itemView, new CardViewHolder.ClickResponseListener() {
            @Override
            public void onWholeClick(int position) {

            }

            @Override
            public void onOverflowClick(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.contextual_new_list, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_share_url:
                            ///
                            break;
                    }
                    return true;
                });
            }
        });

    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        DailyNews dailyNews = newsList.get(position);
        imageLoader.displayImage(dailyNews.getThumbnailUrl(), holder.newsImage, options, animateFirstListener);

        if (dailyNews.getQuestions().size() > 1) {
            holder.questionTitle.setText(dailyNews.getDailyTitle());
            holder.dailyTitle.setText(Constants.Strings.MULTIPLE_DISCUSSION);
        } else {
            holder.questionTitle.setText(dailyNews.getQuestions().get(0).getTitle());
            holder.dailyTitle.setText(dailyNews.getDailyTitle());
        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView    newsImage;
        public TextView     questionTitle;
        public TextView     dailyTitle;
        public ImageView    overflow;

        private ClickResponseListener mClickResponseListener;

        public CardViewHolder(View v, ClickResponseListener clickResponseListener) {
            super(v);
            this.mClickResponseListener = clickResponseListener;

            newsImage       = (ImageView) v.findViewById(R.id.thumbnail_image);
            questionTitle   = (TextView) v.findViewById(R.id.question_title);
            dailyTitle      = (TextView) v.findViewById(R.id.daily_title);
            overflow        = (ImageView) v.findViewById(R.id.card_share_overflow);

        }

        public interface ClickResponseListener {
            void onWholeClick(int position);
            void onOverflowClick(View v, int position);
        }

        @Override
        public void onClick(View v) {
            if (v == overflow) {
                mClickResponseListener.onOverflowClick(v, getAdapterPosition());
            } else {
                mClickResponseListener.onWholeClick(getAdapterPosition());
            }
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }





}
