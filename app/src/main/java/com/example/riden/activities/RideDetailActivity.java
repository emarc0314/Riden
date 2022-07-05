package com.example.riden.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.riden.R;
import com.example.riden.activities.directionhelpers.TaskLoadedCallback;
import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.example.riden.activities.directionhelpers.FetchURL;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.Driver;
import java.util.ArrayList;
//import com.thecodecity;

public class RideDetailActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    GoogleMap map;
    MarkerOptions pickupLocation, destinationLocation;
    Polyline currentPolyline;
    TextView tvPickupAddress;
    TextView tvDestinationAddress;
    TextView tvDepartureDate;
    TextView tvDepartureTime;
    ImageButton ibDriverProfileDetail;
    ImageButton ibDriverCarDetail;

    Ride ride;
    User driver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        tvPickupAddress = findViewById(R.id.tvPickupAddress);
        tvDestinationAddress = findViewById(R.id.tvDestinationAddress);
        tvDepartureDate = findViewById(R.id.tvDepartureDate);
        tvDepartureTime = findViewById(R.id.tvDepartureTime);
        ibDriverProfileDetail = findViewById(R.id.ibDriverProfileDetail);
        ibDriverCarDetail = findViewById(R.id.ibDriverCarDetail);

        ride = getIntent().getParcelableExtra(Ride.class.getSimpleName());
        driver = (User) ride.getDriver();

        pickupLocation = new MarkerOptions().position(new LatLng(ride.getPickupLat(), ride.getPickupLong())).title("Pickup Location");
        destinationLocation = new MarkerOptions().position(new LatLng(ride.getDestinationLat(), ride.getDestinationLong())).title("Destination Location");
        String url = getUrl(pickupLocation.getPosition(), destinationLocation.getPosition(), "driving");
        new FetchURL(RideDetailActivity.this).execute(url, "driving");


        tvDepartureDate.setText(ride.getFullDate());
        tvPickupAddress.setText(ride.getPickupAddress());
        tvDestinationAddress.setText(ride.getDestinationAddress());

        Glide.with(this).load(driver.getProfileImage().getUrl()).into(ibDriverProfileDetail);
        Glide.with(this).load(driver.getCarImage().getUrl()).into(ibDriverCarDetail);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_orogin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_orogin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_api_key);

        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(pickupLocation);
        map.addMarker(destinationLocation);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        markers.add(destinationLocation);
        markers.add(pickupLocation);
        for (MarkerOptions m : markers) {
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 150;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.animateCamera(cu);
            }
        });
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }
}
