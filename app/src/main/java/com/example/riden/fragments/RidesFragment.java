package com.example.riden.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.riden.Adapters.RideAdapter;
import com.example.riden.R;
import com.example.riden.activities.helpers.Spelling;
import com.example.riden.activities.helpers.Trie;
import com.example.riden.models.Ride;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RidesFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private RecyclerView rvRides;
    protected List<Ride> rides;
    protected Trie ridesTrie = new Trie();
    protected List<Ride> allRides;
    protected RideAdapter adapter;
    private SearchView searchView;
    public static final String TAG = "RidesFragment";
    private Spinner spMiles;

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
        adapter = new RideAdapter(getContext(), rides, allRides, ridesTrie);
        searchView = view.findViewById(R.id.searchView);

        spMiles = view.findViewById(R.id.spMiles);
        String[] items = new String[]{"1","5","20"};

        ArrayAdapter<CharSequence> milesAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.miles, android.R.layout.simple_spinner_dropdown_item);
        milesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spMiles.setAdapter(milesAdapter);
        spMiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                if(!text.equals("None")) {
                    int miles = Integer.valueOf(text);
                    adapter.updateMileRadius(miles);
                    //call funciton to filter cells based on the location they searched
                }
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onQueryTextChange(String newText) {
                String autoCorrect = getAutoCorrect(newText);
                adapter.getFilter().filter(autoCorrect);
                return false;
            }
        });

        rvRides.setAdapter(adapter);
        rvRides.setLayoutManager(new LinearLayoutManager(getContext()));
        queryRides();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getAutoCorrect(String input) {
        String autocorrect;

        try {
            Path path = Paths.get("big.txt");
            Spelling spelling = new Spelling(path.toAbsolutePath().toString());
            autocorrect = spelling.correct("speling");
            Log.i("stringtesting", autocorrect);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return input;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                        ridesTrie.insert(ride);
                    }
                }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}