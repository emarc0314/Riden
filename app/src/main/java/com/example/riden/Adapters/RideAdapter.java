package com.example.riden.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riden.R;
import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.google.gson.JsonArray;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {
    Context context;
    List<Ride> rides;
    TextView tvDestination;
    TextView tvDate;
    TextView tvTime;
    TextView tvSeats;
    ImageButton ibCarImage;
    ImageButton ibReserve;
    User user = (User) User.getCurrentUser();

    public RideAdapter(Context context, List<Ride> rides) {
        this.context = context;
        this.rides = rides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rideView = LayoutInflater.from(context).inflate(R.layout.item_ride, parent, false);
        return new ViewHolder(rideView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ride ride = rides.get(position);
        holder.bind(ride);

        if(ride.isReserved()) {
            ibReserve.setImageResource(R.drawable.car_reserve);
        }
        else {
            ibReserve.setImageResource(R.drawable.car);
        }

        ibReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add the ride in Rides view to list of myRides
                Ride ride = rides.get(holder.getAdapterPosition());
                boolean isReserved = ride.isReserved();
                ibReserve = v.findViewById(R.id.ibReserve);

                if(isReserved) {
                    ibReserve.setImageResource(R.drawable.car);
                    user.removeRide(ride);
                }
                else {
                    ibReserve.setImageResource(R.drawable.car_reserve);
                    user.addRide(ride);
                }

                ride.setReserved(!isReserved);
                ride.saveInBackground();
                user.saveInBackground();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            ibCarImage = itemView.findViewById(R.id.ibCarImage);
            ibReserve = itemView.findViewById(R.id.ibReserve);
        }

        public void bind(Ride ride) {
            tvSeats.setText(String.valueOf(ride.getSeats()));
            ParseFile carImage = ride.getCarImage();
            if (carImage != null) {
                Glide.with(context).load(carImage.getUrl()).into(ibCarImage);
            }
        }
    }
}
