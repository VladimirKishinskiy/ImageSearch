package com.example.ImageFinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

    public ImageBase imageBase = new ImageBase("");         //database for images of current search query
    public ArrayList<Image> FavorImages = new ArrayList<Image>();     //database for favorite images
    FavorImageListAdaptor FILAdapter;

    public static int THEME = R.style.Theme_Sherlock;

    FragmentTransaction fTrans;
    ViewPager mPager;
    Downloadtask dlt;
    ActionBar tabsActionBar;
    DBHelper dbHelper;

    private static final String TAG = "myLogs";

    @Override
    public void onCreate(Bundle savedInstanceState) {




        setTheme(THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        //FavorImages = new ArrayList<Image>();


        setContentView(R.layout.tab_navigation);
        tabsActionBar = getSupportActionBar();
        tabsActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fm = getSupportFragmentManager();
        ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabsActionBar.setSelectedNavigationItem(position);
            }
        };

        mPager.setOnPageChangeListener(ViewPagerListener);
        ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(fm);
        mPager.setAdapter(viewpageradapter);

        ActionBar.Tab tab = tabsActionBar.newTab();
        tab.setText("Search");
        tab.setTabListener(this);
        tabsActionBar.addTab(tab);

        tab = tabsActionBar.newTab();
        tab.setText("Favorites");
        tab.setTabListener(this);
        tabsActionBar.addTab(tab);



        SaveMyInstance myInstance = (SaveMyInstance)getLastCustomNonConfigurationInstance();
        if(myInstance != null){
            imageBase = myInstance.imageBase;
            FavorImages = myInstance.images;
            ListLoad ll = new ListLoad();
            ll.execute(imageBase,this);
        }
        else {
            imageBase = new ImageBase("");
            FavorImages = new ImageDB().LoadImageBase(this);
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        new ImageDB().SaveImageBase(FavorImages,this);
    }

    public void Search(String Tag){
        imageBase = new ImageBase(validateTag(Tag));
        dlt = new Downloadtask();
        dlt.execute(imageBase,this);
    }

    public String validateTag(String tag){
        try {
            String url = URLEncoder.encode(tag,"UTF8");
            Log.d("myLogs",url);
            return url;
        } catch (Exception e) {
           Log.d("myLogs","encoding error");
        }
        //String result = tag.replace(" ","%20");
        return tag;
    }


    public void onTabReselected(Tab tab, android.support.v4.app.FragmentTransaction transaction) {
    }


    public void onTabSelected(Tab tab, android.support.v4.app.FragmentTransaction transaction) {
        mPager.setCurrentItem(tab.getPosition());
    }


    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }

    public void setPictureAdapter(final ImageListAdapter adapter){
        ListView lvPictures = (ListView) findViewById(R.id.lvPictures);
        lvPictures.setAdapter(adapter);

        lvPictures.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Log.d(LOG_TAG, "scrollState = " + scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(((firstVisibleItem + visibleItemCount + 10) > totalItemCount )&&(imageBase != null))
                    if(imageBase.lastPage != 0){
                        try {
                            //MainAct.imageBase.AddMoreImages();
                            Nextloadtask nlt = new Nextloadtask();
                            nlt.execute(imageBase,MainActivity.this);
                            Log.d("myLogs", new Integer(FavorImages.size()).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                Log.d("myLogs", "scroll: firstVisibleItem = " + firstVisibleItem
                        + ", visibleItemCount" + visibleItemCount
                        + ", totalItemCount" + totalItemCount);
            }
        });
    }

    public void setFavoriteAdapter(final FavorImageListAdaptor adapter){

        ListView lvFPictures = (ListView) findViewById(R.id.lvFPictures);
        FILAdapter = adapter;
        lvFPictures.setAdapter(adapter);
    }

    public void showLargePicture(Image img){

        Intent intent = new Intent(this, LargeImageActivity.class);
        intent.putExtra("URL", img.getUrl().toString());
        startActivity(intent);
    }

    public void addToFavorBase(Image image){
        FavorImages.add(image);
        Log.d("myLogs", new Integer(FavorImages.size()).toString());
        FILAdapter.notifyDataSetChanged();
    }

    public void DelFromFavor(Integer index){
        FavorImages.remove(index.intValue());
        FILAdapter.notifyDataSetChanged();
    }

    public Object onRetainCustomNonConfigurationInstance(){

        return new SaveMyInstance(imageBase,FavorImages);
    }
}

class Downloadtask extends AsyncTask<Object,Void,ImageBase>{

    ImageBase imageBase;
    MainActivity mainAct;

    @Override
    protected ImageBase doInBackground(Object... params) {
        imageBase = (ImageBase)params[0];
        mainAct = (MainActivity)params[1];
        try {
            imageBase.AddMoreImages();
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(mainAct,"Отсутствует подключение к интернету",Toast.LENGTH_LONG);
        }
        return imageBase;
    }

    @Override
    protected void onPostExecute(ImageBase result){

        ListLoad ll = new ListLoad();
        ll.execute(result,mainAct);
    }
}

class Nextloadtask extends AsyncTask<Object,Void,ImageBase>{

    ImageBase imageBase;
    MainActivity mainAct;

    @Override
    protected ImageBase doInBackground(Object... params) {
        imageBase = (ImageBase)params[0];
        mainAct = (MainActivity)params[1];
        try {
            imageBase.AddMoreImages();
        }
        catch (Exception e) {
            Toast.makeText(mainAct,e.getMessage(),Toast.LENGTH_LONG);
        }
        return imageBase;
    }

    @Override
    protected void onPostExecute(ImageBase result){

    }
}

