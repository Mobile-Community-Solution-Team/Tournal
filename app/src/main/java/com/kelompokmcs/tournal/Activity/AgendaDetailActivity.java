package com.kelompokmcs.tournal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.R;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.FillManager;
import com.mapbox.mapboxsdk.plugins.annotation.FillOptions;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaDetailActivity extends AppCompatActivity {
    private TextView tvAgendaTitle, tvStartTime, tvEndTime;
    private MapView mapView;
    private Toolbar toolbar;
    private Marker selectedMarker;
    NavigationMapRoute navigationMapRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,  getString(R.string.access_token));
        setContentView(R.layout.activity_agenda_detail);

        mapView = findViewById(R.id.mapView);
        tvAgendaTitle = findViewById(R.id.tv_agenda_title);
        tvStartTime = findViewById(R.id.tv_agenda_start_time);
        tvEndTime = findViewById(R.id.tv_agenda_end_time);
        toolbar = findViewById(R.id.toolbar);
        mapView.onCreate(savedInstanceState);

        final Agenda agenda = getIntent().getParcelableExtra("agenda");

        tvAgendaTitle.setText(agenda.getAgendaTitle());
        tvStartTime.setText(parseDateToddMMMMyyyyhhmma(agenda.getStartTime()));
        tvEndTime.setText(parseDateToddMMMMyyyyhhmma(agenda.getEndTime()));

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        if(agenda.getEndLat() == 0.0){
                            selectedMarker = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(agenda.getStartLat(),agenda.getStartLng())));

                            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                                    .target(new LatLng(agenda.getStartLat(),agenda.getStartLng()))
                                    .zoom(15)
                                    .build());
                        }
                        else{
                            Point origin = Point.fromLngLat(agenda.getStartLng(),agenda.getStartLat());
                            final Point destination = Point.fromLngLat(agenda.getEndLng(),agenda.getEndLat());

                            NavigationRoute.builder(AgendaDetailActivity.this)
                                    .accessToken(getString(R.string.access_token))
                                    .origin(origin)
                                    .destination(destination)
                                    .build()
                                    .getRoute(new Callback<DirectionsResponse>() {
                                        @Override
                                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                                            DirectionsRoute currentRoute = response.body().routes().get(0);
                                            navigationMapRoute = new NavigationMapRoute(null,mapView,mapboxMap);
                                            navigationMapRoute.addRoute(currentRoute);

                                            //animation zoom
                                            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                                    .include(new LatLng(agenda.getStartLat(),agenda.getStartLng()))
                                                    .include(new LatLng(agenda.getEndLat(),agenda.getEndLng()))
                                                    .build();

                                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,300),4000);
                                        }

                                        @Override
                                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                                        }
                                    });
                        }
                    }
                });
            }
        });
    }

    private String parseDateToddMMMMyyyyhhmma(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
            return new SimpleDateFormat("dd MMMM yyyy hh:mm a").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
