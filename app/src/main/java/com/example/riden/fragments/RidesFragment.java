package com.example.riden.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riden.Adapters.RideAdapter;
import com.example.riden.R;
import com.example.riden.models.Ride;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RidesFragment extends Fragment {
    private RecyclerView rvRides;
    protected List<Ride> rides;
    protected RideAdapter adapter;
    public static final String TAG = "RidesFragment";

    public RidesFragment() {
        // Required empty public constructor
    }

    private void fetchTimelineAsync(int i) {
        //TODO: Implement when SwipeRefreshing
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRides = view.findViewById(R.id.rvRides);
        rides = new ArrayList<>();
        adapter = new RideAdapter(getContext(), rides);

        rvRides.setAdapter(adapter);
        rvRides.setLayoutManager(new LinearLayoutManager(getContext()));
        queryRides();
    }

    private void queryRides() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Ride> query = ParseQuery.getQuery(Ride.class);
//        query.include(Ride.DRIVER_KEY);
        query.setLimit(20);
//        query.addDescendingOrder()/]
        query.addAscendingOrder("numberSeats");
        query.findInBackground(new FindCallback<Ride>() {
            @Override
            public void done(List<Ride> theseRides, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting rides", e);
                    return;
                }

                for (Ride ride : rides) {
                    Log.i(TAG, "ride " + ride.getDriver());
                }

                rides.addAll(theseRides);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rides, container, false);
    }
}