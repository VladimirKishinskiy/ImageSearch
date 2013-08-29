package com.example.ImageFinder;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;

public class Fragment_Favorites extends SherlockFragment {

    @Override
    public SherlockFragmentActivity getSherlockActivity() {
        return super.getSherlockActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FavorListLoad fll = new FavorListLoad();
        MainActivity MainAct = (MainActivity)getSherlockActivity();
        fll.execute(MainAct.FavorImages, MainAct);

        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
}

class FavorListLoad extends AsyncTask<Object, Void, FavorImageListAdaptor> {

    ListView lvFPictures;
    MainActivity MainAct;
    ArrayList<Image> favorImages;

    @Override
    protected FavorImageListAdaptor doInBackground(Object... params) {

        favorImages = (ArrayList<Image>) params[0];    //main base with pictures and info about it
        MainAct = (MainActivity) params[1];
        FavorImageListAdaptor FILAdapter = new FavorImageListAdaptor(MainAct,favorImages);
        return FILAdapter;

    }

    @Override
    protected void onPostExecute(FavorImageListAdaptor sAdapter){

        MainAct.setFavoriteAdapter(sAdapter);
    }
}