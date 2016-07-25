package com.ltroya.movieindex.movielist.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ltroya.movieindex.R;
import com.ltroya.movieindex.entities.Movie;
import com.ltroya.movieindex.libs.base.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> dataset;
    private OnItemClickListener listener;
    private ImageLoader imageLoader;

    private int IMAGE_LOADER_WIDTH = 800;
    private int IMAGE_LOADER_HEIGHT = 800;

    public MovieAdapter(List<Movie> dataset, OnItemClickListener listener, ImageLoader imageLoader) {
        this.dataset = dataset;
        this.listener = listener;
        this.imageLoader = imageLoader;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = dataset.get(position);
        holder.setOnClickListener(movie, listener);
        imageLoader.load(holder.imgMoviePoster, movie.getPosterPath(), IMAGE_LOADER_WIDTH, IMAGE_LOADER_HEIGHT);
        holder.btnSaveMovie.setImageResource(movie.isKeep()
                ? R.drawable.ic_bookmark_saved
                : R.drawable.ic_bookmark_not_saved);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Movie> newItems) {
        dataset.addAll(newItems);
        notifyDataSetChanged();
    }

    public void updateMovie(String id, boolean keepMovie) {
        for (Movie movie: dataset) {
            if (movie.getId().equals(id)) {
                movie.setKeep(keepMovie);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void removeMovie(Movie movie) {
        dataset.remove(movie);
        notifyDataSetChanged();
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
