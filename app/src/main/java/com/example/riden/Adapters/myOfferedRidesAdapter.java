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

public class myOfferedRidesAdapter extends RecyclerView.Adapter<myOfferedRidesAdapter.ViewHolder> {
    private Context context;
    private List<Ride> myOfferedRides;
    private TextView tvDestination;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvSeats;
    private ImageButton ibCarImage;
    private ImageButton ibReserve;
    private User user = (User) User.getCurrentUser();

    public myOfferedRidesAdapter(Context context, List<Ride> myOfferedRides) {
       this.context = context;
       this.myOfferedRides = myOfferedRides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rideView = LayoutInflater.from(context).inflate(R.layout.item_reserved_ride, parent, false);
        return new myOfferedRidesAdapter.ViewHolder(rideView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ride offeredRide = myOfferedRides.get(position);
        holder.bind(offeredRide);
    }

    @Override
    public int getItemCount() {
        return myOfferedRides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSeats = itemView.findViewById(R.id.tvSeatsReserved);
            ibCarImage = itemView.findViewById(R.id.ibCarImage);
            ibReserve = itemView.findViewById(R.id.ibReserve);
            itemView.setOnClickListener(this);
        }

        public void bind(Ride offeredRide) {
            tvSeats.setText(String.valueOf(offeredRide.getSeats()));
            tvDestination.setText(offeredRide.getCityDestination() + ", " + offeredRide.getStateDestination());

            ParseFile carImage = offeredRide.getCarImage();
            if (carImage != null) {
                Glide.with(context).load(carImage.getUrl()).into(ibCarImage);
            }
        }

        @Override
        public void onClick(View v) {
            //TODO: Implement OnClick for Driver's Offered Rides
        }
    }
}
