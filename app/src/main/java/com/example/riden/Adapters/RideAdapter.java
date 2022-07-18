package com.example.riden.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riden.R;
import com.example.riden.activities.RideDetailActivity;
import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.parse.ParseFile;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Ride> rides;
    private List<Ride> allRides;

    private User user = (User) User.getCurrentUser();

    public RideAdapter(Context context, List<Ride> rides, List<Ride> allRides) {
        this.context = context;
        this.rides = rides;
        this.allRides = allRides;
    }

    public static List<Ride> cloneList(List<Ride> originalArrayList) {
        List<Ride> copyArrayofList = new ArrayList<Ride>(originalArrayList.size());
        for (Ride item : originalArrayList) copyArrayofList.add(item);
        return copyArrayofList;
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
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
       //TODO: Implement trie data structure search

        /**second complicated feature: type location, but your going in that radius
         * apart from just the name of the location, show geographical coordintaes
         * and see which rides going to that destination are close enough
         * - AD Tree
         * - geographical search Tree
         * */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Ride> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(allRides);
            }
            else {
                for (Ride ride: allRides) {
                    if(ride.getDestinationAddress().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(ride);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rides.clear();
            rides.addAll((Collection<? extends Ride>) results.values);
            notifyDataSetChanged();
        }
    };

    public void clear() {
        rides.clear();
        allRides.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDestination;
        private TextView tvDate;
        private TextView tvTime;
        private TextView tvSeats;
        private ImageButton ibCarImage;
        private ImageButton ibReserve;

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
            String cityDestination = ride.getCityDestination();
            String cityAddress = ride.getDestinationAddress();
            tvDestination.setText(cityDestination + ", " + ride.getStateDestination());
            tvDate.setText(ride.getDepartureDate());
            ParseFile carImage = ride.getCarImage();
            String objectId = ride.getObjectId();
            List<String> rideObjectIds = user.getRideObjectIds();
            tvSeats.setText(String.valueOf(ride.getSeats()));

            if (carImage != null) {
                Glide.with(context).load(carImage.getUrl()).into(ibCarImage);
            }

            if(rideObjectIds.contains(objectId)) {
                ibReserve.setImageResource(R.drawable.car_reserve);
            }
            else {
                ibReserve.setImageResource(R.drawable.car);
            }

            ibReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add the ride in Rides view to list of myRides
                    Ride ride = rides.get(getAdapterPosition());
                    boolean isReserved = ride.isReserved();
                    ibReserve = v.findViewById(R.id.ibReserve);
                    tvSeats = v.findViewById(R.id.tvSeatsCell);

                    if(user.getRideObjectIds().contains(ride.getObjectId())) {
                        ibReserve.setImageResource(R.drawable.car);
                        user.removeRide(ride);
                        ride.removeReservee(user);
                        ride.setSeats(ride.getSeats()  + 1);
                    }
                    else {
                        ibReserve.setImageResource(R.drawable.car_reserve);
                        user.addRide(ride);
                        ride.addReservee(user);
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
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Ride ride = rides.get(position);
                Intent intent = new Intent(context, RideDetailActivity.class);
                intent.putExtra(Ride.class.getSimpleName(), ride);
                intent.putExtra("isMyRidesView", false);
                context.startActivity(intent);
            }
        }
    }
}
