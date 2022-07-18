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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.riden.Adapters.MyRidesAdapter;
import com.example.riden.Adapters.myOfferedRidesAdapter;
import com.example.riden.R;
import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private ImageButton ibProfileImage;
    private TextView tvUsername;
    private TextView tvFullName;
    private TextView tvCar;
    private ImageView ivCarImage;
    private RecyclerView rvDriverRides;
    protected List<Ride> myOfferedRides;
    protected myOfferedRidesAdapter adapter;

    public User user = (User) User.getCurrentUser();
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ibProfileImage = view.findViewById(R.id.ibProfileImage);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvCar = view.findViewById(R.id.tvCar);
        ivCarImage = view.findViewById(R.id.ivCar);

        myOfferedRides = new ArrayList<>();
        rvDriverRides = view.findViewById(R.id.rvDriversRides);
        adapter = new myOfferedRidesAdapter(getContext(), myOfferedRides);
        rvDriverRides.setAdapter(adapter);
        rvDriverRides.setLayoutManager(new LinearLayoutManager(getContext()));
        myOfferedRides.addAll(user.getMyOfferedRides());
        adapter.notifyDataSetChanged();

        ibProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Set the onClick event
            }
        });

        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                user = (User) object;
                displayUserInfo();
            }
        });
    }

    private void displayUserInfo() {
        tvUsername.setText(user.getUsername());
        tvFullName.setText(user.getFullName());

        ParseFile profileImage = user.getProfileImage();
        ParseFile carImage = user.getCarImage();

        if(profileImage != null) {
            Glide.with(getContext()).load(profileImage.getUrl()).circleCrop().into(ibProfileImage);
        }
        if(carImage != null) {
            Glide.with(getContext()).load(carImage.getUrl()).into(ivCarImage);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}