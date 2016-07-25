package com.ltroya.movieindex.libs.base;

import android.support.annotation.Nullable;
import android.widget.ImageView;

public interface ImageLoader {
    void load(ImageView imageView, String URL, int width, int height);
}
