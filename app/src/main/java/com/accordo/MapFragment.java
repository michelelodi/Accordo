package com.accordo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

    private static final String LAT = "lat";
    private static final String LON = "lon";
    private final String TAG = "MYTAG_MapFragment";
    Double mLat;
    Double mLon;

    public MapFragment() {}


    public static MapFragment newInstance(String lat, String lon) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(LAT, lat);
        args.putString(LON, lon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLat = getArguments().getDouble(LAT);
            mLon = getArguments().getDouble(LON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { return inflater.inflate(R.layout.fragment_map, container, false); }
}