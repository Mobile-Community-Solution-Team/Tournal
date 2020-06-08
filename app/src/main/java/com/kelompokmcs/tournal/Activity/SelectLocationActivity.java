package com.kelompokmcs.tournal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokmcs.tournal.R;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private int REQUEST_CODE_AUTOCOMPLETE = 1;
    private FloatingActionButton fabSearchLocation;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Button btnSelectLocation;
    private Toolbar toolbar;
    private double selectedLat, selectedLng;
    private Marker selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_select_location);

        fabSearchLocation = findViewById(R.id.fab_search_location);
        mapView = findViewById(R.id.mapView);
        btnSelectLocation = findViewById(R.id.btn_select_location);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectedLat = 0.0;
        selectedLng = 0.0;

        mapView.getMapAsync(this);

        fabSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(SelectLocationActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });

        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("lat",selectedLat);
                i.putExtra("lng",selectedLng);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE && resultCode == RESULT_OK){
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            selectedLat = ((Point) selectedCarmenFeature.geometry()).latitude();
            selectedLng = ((Point) selectedCarmenFeature.geometry()).longitude();
            selectedMarker = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(selectedLat,selectedLng)));
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(new LatLng(selectedLat,selectedLng))
                            .zoom(14)
                            .build()), 4000);
            btnSelectLocation.setEnabled(true);
            btnSelectLocation.setBackgroundResource(R.color.colorPrimary);
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapboxMap.addOnMapClickListener(SelectLocationActivity.this);
            }
        });
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if(selectedMarker!=null){
            mapboxMap.removeMarker(selectedMarker);
        }
        selectedMarker = mapboxMap.addMarker(new MarkerOptions().position(point));
        selectedLat = point.getLatitude();
        selectedLng = point.getLongitude();
        btnSelectLocation.setEnabled(true);
        btnSelectLocation.setBackgroundResource(R.color.colorPrimary);
        return false;
    }
}
