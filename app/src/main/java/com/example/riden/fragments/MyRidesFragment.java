package com.example.riden.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riden.Adapters.MyRidesAdapter;
import com.example.riden.R;
import com.example.riden.models.Ride;
import com.example.riden.models.User;

import java.util.ArrayList;
import java.util.List;

public class MyRidesFragment extends Fragment {
    public static final String TAG = "MyRideFragment";
    private RecyclerView rvRides;
    protected List<Ride> rides;
    protected MyRidesAdapter adapter;
    User user = (User) User.getCurrentUser();

    public MyRidesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRides = view.findViewById(R.id.rvReservedRides);
        rides = new ArrayList<>();
        adapter = new MyRidesAdapter(getContext(), rides);
        rvRides.setAdapter(adapter);
        rvRides.setLayoutManager(new LinearLayoutManager(getContext()));
        rides.addAll(user.getMyRides());
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_rides, container, false);
    }
}