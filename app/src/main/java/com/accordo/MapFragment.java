package com.accordo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment implements
        OnMapReadyCallback {

    private static final String LAT = "lat";
    private static final String LON = "lon";
    private final String TAG = "MYTAG_MapFragment";
    private static final String LOCATION_POST_ICON = "embassy-15";

    private SymbolManager symbolManager;
    private Symbol symbol;
    private MapView mapView;
    private MapboxMap mMapboxMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Double mLat;
    private Double mLon;

    public MapFragment() {}

    public static MapFragment newInstance(String lat, String lon) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(LAT, lat);
        args.putString(LON, lon);
        fragment.setArguments(args);
        return fragment;
    }
/*
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    //TODO premission granted
                } else {
                    //TODO premission still not granted
                }
            });

 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token));
        if (getArguments() != null) {
            mLat = Double.parseDouble(getArguments().getString(LAT));
            mLon = Double.parseDouble(getArguments().getString(LON));
        }
/*
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(mLon, mLat)));
        mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(mLat, mLon))
                .zoom(10)
                .build()));
        mapboxMap.setStyle(Style.LIGHT
                , style -> {
                    symbolManager = new SymbolManager(mapView, mapboxMap, style);
                    symbolManager.setIconAllowOverlap(true);
                    symbolManager.setTextAllowOverlap(true);
                    symbol = symbolManager.create(new SymbolOptions()
                            .withLatLng(new LatLng(mLat, mLon))
                            .withIconImage(LOCATION_POST_ICON)
                            .withIconSize(1.5f));
                });
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}