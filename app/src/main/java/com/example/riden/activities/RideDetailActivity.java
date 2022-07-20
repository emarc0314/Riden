package com.example.riden.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riden.Adapters.MyRidesAdapter;
import com.example.riden.Adapters.RidersAdapter;
import com.example.riden.R;
import com.example.riden.activities.helpers.SwipeToDeleteCallback;
import com.example.riden.activities.helpers.TaskLoadedCallback;
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
import com.example.riden.activities.helpers.FetchURL;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.sql.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
//import com.thecodecity;

public class RideDetailActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap map;
    private MarkerOptions pickupLocation, destinationLocation;
    private Polyline currentPolyline;
    private TextView tvPickupAddress;
    private TextView tvDestinationAddress;
    private TextView tvDepartureDate;
    private TextView tvDepartureTime;
    private ImageButton ibDriverProfileDetail;
    private ImageButton ibDriverCarDetail;
    private TextView tvPrice;
    private Button btReserve;

    private Ride ride;
    private User driver;
    private User currentUser;
    private Boolean isMyRidesView;
    private User rider;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private ImageView ivProfilePop;
    private TextView tvNamePop;
    private TextView tvPhoneNumberPop;
    private TextView tvYearPop;
    private TextView tvMajorPop;
    private ImageView ivCarPop;
    private TextView tvLicensePlatePop;

    private Boolean isDriver;
    private RecyclerView rvRiders;
    private List<User> riders;
    private RidersAdapter adapter;
    CoordinatorLayout clCoordinateProfileLayout;

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
        tvPrice = findViewById(R.id.tvPrice);
        btReserve = findViewById(R.id.btReserve);
        rvRiders = findViewById(R.id.rvRiders);
        clCoordinateProfileLayout = findViewById(R.id.clCoordinateProfileLayout);

        currentUser = (User) User.getCurrentUser();
        ride = getIntent().getParcelableExtra(Ride.class.getSimpleName());
        driver = (User) ride.getDriver();

        Boolean reserved = currentUser.getRideObjectIds().contains(ride.getObjectId());

        if(reserved) {
            btReserve.setText("Reserved!");
        }
        else {
            btReserve.setText("Reserve");
        }

        isMyRidesView = getIntent().getParcelableExtra("isMyRidesView");
        isDriver = getIntent().getExtras().getBoolean("isDriver");

        if(isDriver) {
            ibDriverProfileDetail.setVisibility(View.GONE);
            ibDriverCarDetail.setVisibility(View.GONE);
            btReserve.setVisibility(View.GONE);

            riders = new ArrayList<>();
            riders.addAll(ride.getReservees());
            adapter = new RidersAdapter(this, riders);
            rvRiders.setAdapter(adapter);
            rvRiders.setLayoutManager(new LinearLayoutManager(this));
            adapter.notifyDataSetChanged();

            enableSwipeToDeleteAndUndo();
        }
        else {
            rvRiders.setVisibility(View.GONE);
        }

        pickupLocation = new MarkerOptions().position(new LatLng(ride.getPickupLat(), ride.getPickupLong())).title("Pickup Location");
        destinationLocation = new MarkerOptions().position(new LatLng(ride.getDestinationLat(), ride.getDestinationLong())).title("Destination Location");

        String url = getUrl(pickupLocation.getPosition(), destinationLocation.getPosition(), "driving");
        new FetchURL(RideDetailActivity.this).execute(url, "driving");

        btReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> rideObjectIds = currentUser.getRideObjectIds();
                Boolean reserved = rideObjectIds.contains(ride.getObjectId());
                if(reserved) {
                    currentUser.removeRide(ride);
                    ride.setSeats(ride.getSeats() + 1);

//                    ride.setPrice();
                    ride.removeReservee(currentUser);
                    btReserve.setText("Reserve");

                    //TODO: refresh the previous cell layout view
                }
                else {
                    ride.setSeats(ride.getSeats() - 1);
                    currentUser.addRide(ride);
                    ride.addReservee(currentUser);
                    btReserve.setText("Reserved!");
                }

                currentUser.saveInBackground();
                ride.saveInBackground();
            }
        });

        ibDriverProfileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfileDetailDialog();
            }
        });

        tvDepartureDate.setText(ride.getFullDate());
        tvPickupAddress.setText(ride.getPickupAddress());
        tvDestinationAddress.setText(ride.getDestinationAddress());

        int reservedRides;
        if(ride.getReservees().isEmpty()) {
            reservedRides = 1;
        }
        else {
            reservedRides = ride.getReservees().size();
        }

        float totalPrice = Float.valueOf(ride.getPrice().substring(1).replace(",",""))/reservedRides;

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String totalProfitDollar = formatter.format(totalPrice);

        tvPrice.setText(totalProfitDollar);

        Glide.with(this).load(driver.getProfileImage().getUrl()).into(ibDriverProfileDetail);
        Glide.with(this).load(driver.getCarImage().getUrl()).into(ibDriverCarDetail);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                rider = adapter.getData().get(position);
                adapter.removeItem(position);
                rider.removeRide(ride);
                ride.removeReservee(rider);
//                rider.removeRide(ride);

                Snackbar snackbar = Snackbar
                        .make(clCoordinateProfileLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.restoreItem(rider, position);
                        ride.addReservee(rider);
                        rvRiders.scrollToPosition(position);
                        rider.addRide(ride);
                    }
                });

                rider.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("I succeeded", "I deleted the thing");
                    }
                });

                ride.saveInBackground();
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvRiders);
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


    public void createProfileDetailDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View profileDetailView = getLayoutInflater().inflate(R.layout.popup, null);
        ivProfilePop = profileDetailView.findViewById(R.id.ivProfilePop);
        tvNamePop = profileDetailView.findViewById(R.id.tvNamePop);
        tvPhoneNumberPop = profileDetailView.findViewById(R.id.tvPhoneNumberPop);
        tvYearPop = profileDetailView.findViewById(R.id.tvYearPop);
        tvMajorPop = profileDetailView.findViewById(R.id.tvMajorPop);
        ivCarPop = profileDetailView.findViewById(R.id.ivCarImagePop);

        tvNamePop.setText(driver.getFullName());
        tvPhoneNumberPop.setText(driver.getPhoneNumber());

        Glide.with(this).load(driver.getProfileImage().getUrl()).into(ivProfilePop);
        Glide.with(this).load(driver.getCarImage().getUrl()).into(ivCarPop);

        dialogBuilder.setView(profileDetailView);

        dialog = dialogBuilder.create();
        dialog.show();
    }
}
