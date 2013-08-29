package com.example.ImageFinder;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ListLoad extends AsyncTask<Object, Void, ImageListAdapter> {

    ListView lvPictures;
    MainActivity MainAct;
    ImageBase imageBase;

    @Override
    protected ImageListAdapter doInBackground(Object... params) {

        imageBase = (ImageBase) params[0];    //main base with pictures and info about it
        MainAct = (MainActivity) params[1];
        ImageListAdapter ILAdapter = new ImageListAdapter(MainAct,imageBase.Images);

        return ILAdapter;
    }

    @Override
    protected void onPostExecute(ImageListAdapter sAdapter){

        MainAct.setPictureAdapter(sAdapter);
    }
}

public class Fragment_Search extends SherlockFragment {

    private static final String TAG = "myLogs";
    ListView lvPictures;

    com.example.ImageFinder.MainActivity act;

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

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        final EditText search = (EditText) v.findViewById(R.id.searchtext);


        ImageButton button = (ImageButton) v.findViewById(R.id.searchbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getSherlockActivity()).Search(search.getText().toString());
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
}
