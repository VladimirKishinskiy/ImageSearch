package com.example.ImageFinder;

import java.util.ArrayList;

import android.app.FragmentTransaction;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Image> images;

    ImageListAdapter(Context context, ArrayList<Image> images) {
        ctx = context;
        this.images = images;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d("myLogs","list must be changed");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View  view = lInflater.inflate(R.layout.item_search, parent, false);

        Image im = getImage(position);

        ((TextView) view.findViewById(R.id.tvPicName)).setText(im.getTitle());
        ((TextView) view.findViewById(R.id.tvSiteName)).setText(im.getVisibleUrl());
        String size = new Integer(im.getWidth()).toString() + "x" + new Integer(im.getHeight()).toString();
        ((TextView) view.findViewById(R.id.tvSize)).setText(size);

        Button btToFavorite = (Button)view.findViewById(R.id.ToFavorButton);
        btToFavorite.setOnClickListener(toFavoriteListener);
        btToFavorite.setTag(position);

        ImageView ivImage = (ImageView)view.findViewById(R.id.ivImg);
        ivImage.setOnClickListener(imageClickListener);
        ivImage.setTag(position);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
        imageLoader.displayImage(im.getTbUrl().toString(), ivImage,options);

        return view;
    }

    Image getImage(int position) {
        return ((Image) getItem(position));
    }

    View.OnClickListener toFavoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity MainAct = (MainActivity)ctx;
            MainAct.addToFavorBase(MainAct.imageBase.GetImage((Integer)v.getTag()));

            Log.d("myLogs", "button " + v.getTag());
        }
    };

    View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity MainAct = (MainActivity)ctx;
            MainAct.showLargePicture(MainAct.imageBase.GetImage((Integer)v.getTag()));
        }
    };
}