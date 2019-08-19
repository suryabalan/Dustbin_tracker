package com.example.myapplication10;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.net.DatagramPacket;
import java.util.List;
import java.util.Objects;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

// classes needed to initialize map
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add the location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;

// classes needed to add a marker
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;


public class dustbin extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private Button button;



    double[] latitude ={9.883327,9.883380,9.882816,9.882672,9.882493,9.882837,9.882869,9.883269,9.883396,9.882555,9.882205};
    double[] longitude={78.083431,78.0829,78.082490,78.081645,78.082044,78.082254,78.081404,78.0806,78.0800,78.082383,78.0830};
    public double j=0;
    public double k=0;
    public double e=0;
    public double f=0;
    public double minDistance=2;
    double distance;

    public double s1,s2;

    private DatagramPacket document;

    public void haversine(){

        double latDistance;
        double lonDistance;
        int R=6371;

        int i;

        for(i=0;i<11;i++)
        {
            e= Objects.requireNonNull(locationComponent.getLastKnownLocation()).getLatitude();

            f=locationComponent.getLastKnownLocation().getLongitude();


            latDistance = Math.toRadians(latitude[i]-e);
            lonDistance = Math.toRadians(longitude[i]-f);


            double g = Math.pow(Math.sin(latDistance / 2), 2) +
                    Math.pow(Math.sin(lonDistance / 2), 2) *
                            Math.cos(e) *
                            Math.cos(latitude[i]);
            double h = 2 * Math.asin(Math.sqrt(g));
            distance = R * h;

            if(distance<minDistance){
                minDistance=distance;
                j= latitude[i];
                k= longitude[i];

            }

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_dustbin);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                addDestinationIconSymbolLayer(style);


                final Marker m1 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.883327, 78.083431)));
                final Marker m2 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.883380, 78.0829)));
                final Marker m3 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.882816, 78.082490)));
                final Marker m4 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.882672, 78.081645)));
                final Marker m5 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.882493, 78.082044)));
                final Marker m6 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.882837, 78.082254)));
                final Marker m7 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.882869, 78.081404)));
                final Marker m8 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.883269, 78.0806)));
                final Marker m9 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.883396, 78.0800)));
                final Marker m10 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.882555, 78.082383)));
                final Marker m11 = mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(9.882205, 78.0830)));



                haversine();

                mapboxMap.addOnMapClickListener(dustbin.this);
                button = findViewById(R.id.startButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean simulateRoute = true;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build();
// Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(dustbin.this, options);
                    }
                });
            }
        });
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }
    public void loc (View view){
        if(minDistance==2)
        {
            AlertDialog.Builder dlgAlert=new AlertDialog.Builder(this);
            dlgAlert.setMessage("There are no Dustbins around 2Kms");
            dlgAlert.setTitle("Alert");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //dismiss
                }
            });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
        Point destinationPoint = Point.fromLngLat(k, j);
        assert locationComponent.getLastKnownLocation() != null;


        Point originPoint = Point.fromLngLat(f, e);


        getRoute(originPoint, destinationPoint);
        button.setEnabled(true);
        button.setBackgroundResource(R.color.mapboxBlue);
    }

    @SuppressWarnings( {"MissingPermission"})

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

// Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
// Activate the MapboxMap LocationComponent to show user location
// Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }
    @Override
    public void onBackPressed() {
        Intent I = new Intent(dustbin.this, MainActivity.class);
        startActivity(I);
        finish();
    }
}
