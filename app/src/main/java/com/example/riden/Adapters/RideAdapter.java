package com.example.riden.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riden.R;
import com.example.riden.activities.RideDetailActivity;
import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.parse.ParseFile;

import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {
    private Context context;
    private List<Ride> rides;
    private TextView tvDestination;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvSeats;
    private ImageButton ibCarImage;
    private ImageButton ibReserve;
    private User user = (User) User.getCurrentUser();

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
        tvSeats.setText(String.valueOf(ride.getSeats()));

        ibReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add the ride in Rides view to list of myRides
                Ride ride = rides.get(holder.getAdapterPosition());
                boolean isReserved = ride.isReserved();
                ibReserve = v.findViewById(R.id.ibReserve);
                tvSeats = v.findViewById(R.id.tvSeatsCell);

                if(isReserved) {
                    ibReserve.setImageResource(R.drawable.car);
                    user.removeRide(ride);
                    ride.setSeats(ride.getSeats()  + 1);
                }
                else {
                    ibReserve.setImageResource(R.drawable.car_reserve);

                    user.addRide(ride);
                    ride.setSeats(ride.getSeats()  - 1);
                }

                String seats = String.valueOf(ride.getSeats());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            ibCarImage = itemView.findViewById(R.id.ibCarImage);
            ibReserve = itemView.findViewById(R.id.ibReserve);
            tvSeats = itemView.findViewById(R.id.tvSeatsCell);
            itemView.setOnClickListener(this);
        }

        public void bind(Ride ride) {
            tvSeats.setText(String.valueOf(ride.getSeats()));
            tvDestination.setText(ride.getCityDestination() + ", " + ride.getStateDestination());
            tvDate.setText(ride.getDepartureDate());
            ParseFile carImage = ride.getCarImage();
            if (carImage != null) {
                Glide.with(context).load(carImage.getUrl()).into(ibCarImage);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Ride ride = rides.get(position);
                Intent intent = new Intent(context, RideDetailActivity.class);
                intent.putExtra(Ride.class.getSimpleName(), ride);
                context.startActivity(intent);
            }
        }
    }
}
