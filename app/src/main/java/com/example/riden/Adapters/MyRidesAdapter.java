package com.example.riden.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riden.R;
import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.parse.ParseFile;

import java.util.List;

public class MyRidesAdapter extends RecyclerView.Adapter<MyRidesAdapter.ViewHolder> {
    Context context;
    List<Ride> rides;
    TextView tvDestination;
    TextView tvDate;
    TextView tvTime;
    TextView tvSeats;
    ImageButton ibCarImage;
    ImageButton ibReserve;

    public MyRidesAdapter(Context context, List<Ride> rides) {
        this.context = context;
        this.rides = rides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rideView = LayoutInflater.from(context).inflate(R.layout.item_ride, parent, false);
        return new MyRidesAdapter.ViewHolder(rideView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ride ride = rides.get(position);
        holder.bind(ride);
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
