package com.example.ImageFinder;

import android.*;
import android.R;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class Image {
    private String title;
    private URL url;
    private URL tbUrl;
    private String visibleUrl;
    private int width;
    private int height;
    private int tbWidth;
    private int tbHeight;

    public int getHeight() {
        return height;
    }

    public URL getTbUrl() {
        return tbUrl;
    }

    public int getTbWidth() {
        return tbWidth;
    }

    public String getTitle() {
        return title;
    }

    public URL getUrl() {
        return url;
    }

    public String getVisibleUrl() {
        return visibleUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getTbHeight() {
        return tbHeight;
    }

    public Image(String title, URL url, URL tbUrl, String visibleUrl, int width, int height, int tbWidth, int tbHeight) {
        this.title = title;
        this.url = url;
        this.tbUrl = tbUrl;
        this.visibleUrl = visibleUrl;
        this.width = width;
        this.height = height;
        this.tbWidth = tbWidth;
        this.tbHeight = tbHeight;
    }
}

public class ImageBase{
    String SearchTag;
    ArrayList<Image> Images;
    boolean IsEnd;
    int lastPage;
    String resultCount;

    public ImageBase(String SearchTag) {
        this.SearchTag = SearchTag;
        Images = new ArrayList<Image>();
        IsEnd = false;
        lastPage = 0;
    }

    public String getSearchTag() {
        return SearchTag;
    }

    public int count(){
        return this.Images.size();
    }

    private String ValidateSearchTag() {     //this function validate search tag and must replace forbidden sympols
        String ValidateSearchTag = SearchTag;
        return ValidateSearchTag;
    }

    synchronized public void AddMoreImages() throws Exception {

        final int RSZ = 8;      //count images in one query, must be 4 to 8
        HttpURLConnection connection;

        try {
            URL url = new URL("http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + SearchTag + "&rsz=" + RSZ + "&start=" + this.lastPage);
            connection = (HttpURLConnection) url.openConnection();

            //connection.connect();

            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            connection.disconnect();

            Log.d("myLogs", builder.toString());
            JSONObject json = new JSONObject(builder.toString());
            JSONObject imageArray = json.getJSONObject("responseData");
            JSONArray imageArray2 = imageArray.getJSONArray("results");

            for (int i = 0; i < imageArray2.length(); i++) {
                JSONObject image = imageArray2.getJSONObject(i);
                String Title = image.getString("titleNoFormatting");
                URL URL = new URL(image.getString("url"));
                URL tbURL = new URL(image.getString("tbUrl"));
                String visibleURL = image.getString("visibleUrl");
                Integer Width = image.getInt("width");
                Integer tbWidth = image.getInt("tbWidth");
                Integer Height = image.getInt("height");
                Integer tbHeight = image.getInt("tbHeight");

                this.Images.add(new Image(Title,URL,tbURL,visibleURL,Width,Height,tbWidth,tbHeight));

            }

            if (imageArray2.length() < RSZ)
                this.IsEnd = true;      //if imagearray is not complete, so there are no more pictures

            this.lastPage += RSZ;       //increment search start position to current search page
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEnd() {
        IsEnd = true;
    }

    public void IncLastPage() {
        lastPage++;
    }

    public int GetLastPage() {
        return lastPage;
    }

    public boolean GetEnd() {
        return IsEnd;
    }

    public Image GetImage(int num) {
        return this.Images.get(num);
    }
}
