package com.example.ImageFinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class LargeImageActivity extends SherlockActivity{

    public static int THEME = R.style.Theme_Sherlock;

    private static final String TAG = "myLogs";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTheme(THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.large_image);

        ImageView ivLargeImage = (ImageView) findViewById(R.id.ivLargeImage);
        Intent intent = getIntent();
        String URL = intent.getStringExtra("URL");

        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();


        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        imageLoader.displayImage(URL, ivLargeImage);

    }
}
