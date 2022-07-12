package com.example.riden.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.riden.Adapters.RideAdapter;
import com.example.riden.R;
import com.example.riden.models.Ride;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RidesFragment extends Fragment {
    private RecyclerView rvRides;
    protected List<Ride> rides;
    protected List<Ride> allRides;
    protected RideAdapter adapter;
    private SearchView searchView;
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
        allRides = new ArrayList<>();
        adapter = new RideAdapter(getContext(), rides, allRides);
        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        rvRides.setAdapter(adapter);
        rvRides.setLayoutManager(new LinearLayoutManager(getContext()));
        queryRides();
    }

    private void queryRides() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Ride> query = ParseQuery.getQuery(Ride.class);
        query.setLimit(20);
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
                for(Ride ride: theseRides) {
                    if(ride.getSeats() > 0) {
                        rides.add(ride);
                        allRides.add(ride);
                    }
                }

//                rides.addAll(theseRides);
//                allRides.addAll(theseRides);


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