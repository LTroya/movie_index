package com.ltroya.movieindex.moviesaved.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ltroya.movieindex.R;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.ImageLoader;
import com.ltroya.movieindex.movielist.ui.adapters.OnItemClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RealmMovieAdapter extends RealmRecyclerViewAdapter<Movie, RealmMovieAdapter.ViewHolder> {

    @Nullable
    private final OrderedRealmCollection<Movie> dataset;
    private final OnItemClickListener listener;
    private final ImageLoader imageLoader;

    public RealmMovieAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Movie> data,
                             OnItemClickListener listener, ImageLoader imageLoader) {
        super(context, data, true);
        this.dataset = data;
        this.listener = listener;
        this.imageLoader = imageLoader;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = dataset.get(position);
        holder.setOnClickListener(movie, listener);
        imageLoader.load(holder.imgMoviePoster, movie.getPosterPath(), 800, 800);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_movie_poster)
        ImageView imgMoviePoster;

        @Bind(R.id.btn_save_movie)
        ImageButton btnSaveMovie;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }

        public void setOnClickListener(final Movie movie, final OnItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });

            btnSaveMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSaveButton(movie);
                }
            });
        }
    }
}
