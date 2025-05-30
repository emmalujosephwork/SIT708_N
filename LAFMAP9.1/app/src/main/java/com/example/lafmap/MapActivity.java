package com.example.lafmap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        dbHelper = new DatabaseHelper(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        List<Advert> adverts = dbHelper.getAllAdverts();

        if (adverts.isEmpty()) {
            LatLng defaultLocation = new LatLng(-33.8688, 151.2093); // Default fallback location
            googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("No items found"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
            android.util.Log.d("MapActivity", "No adverts found in database.");
            return;
        }

        for (Advert advert : adverts) {
            try {
                String loc = advert.location;
                android.util.Log.d("MapActivity", "Advert ID: " + advert.id + ", Location string: " + loc);

                String[] latLngStr = loc.split(",");
                if (latLngStr.length == 2) {
                    double lat = Double.parseDouble(latLngStr[0].trim());
                    double lng = Double.parseDouble(latLngStr[1].trim());
                    LatLng position = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(advert.type + ": " + advert.name));
                    android.util.Log.d("MapActivity", "Marker added for advert ID: " + advert.id);
                } else {
                    android.util.Log.w("MapActivity", "Invalid location format for advert ID: " + advert.id);
                }
            } catch (Exception e) {
                android.util.Log.e("MapActivity", "Error parsing location for advert ID: " + advert.id, e);
            }
        }

        // Move camera to first advert location
        Advert first = adverts.get(0);
        try {
            String[] latLngStr = first.location.split(",");
            if (latLngStr.length == 2) {
                double lat = Double.parseDouble(latLngStr[0].trim());
                double lng = Double.parseDouble(latLngStr[1].trim());
                LatLng firstPosition = new LatLng(lat, lng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPosition, 10));
                android.util.Log.d("MapActivity", "Camera moved to first advert location.");
            }
        } catch (Exception e) {
            android.util.Log.e("MapActivity", "Error moving camera to first advert location.", e);
        }
    }

    // Lifecycle methods (keep these as-is)
    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onStart() { super.onStart(); mapView.onStart(); }
    @Override public void onStop() { super.onStop(); mapView.onStop(); }
    @Override public void onPause() { mapView.onPause(); super.onPause(); }
    @Override public void onDestroy() { mapView.onDestroy(); super.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
}
