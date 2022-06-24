package com.example.riden.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.riden.R;
import com.example.riden.models.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class ProfileFragment extends Fragment {
    ImageButton ibProfileImage;
    TextView tvUsername;
    TextView tvFullName;
    TextView tvCar;
    ImageView ivCarImage;

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