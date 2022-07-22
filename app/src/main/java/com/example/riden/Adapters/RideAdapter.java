package com.example.riden.Adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riden.R;
import com.example.riden.activities.RideDetailActivity;
import com.example.riden.activities.helpers.Trie;
import com.example.riden.models.Ride;
import com.example.riden.models.User;
//import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.MarkerOptions;

//import com.google.android.libraries.places.api.model.Place;
import com.parse.ParseFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Ride> rides;
    private final List<Ride> allRides;
    private User user = (User) User.getCurrentUser();
    private int radiusMiles = 20;
    private Trie rideTrie;

    public RideAdapter(Context context, List<Ride> rides, List<Ride> allRides, Trie rideTrie) {
        this.context = context;
        this.rides = rides;
        this.allRides = allRides;
        this.rideTrie = rideTrie;
    }

    public void updateMileRadius(int radiusMiles) {
        this.radiusMiles = radiusMiles;
        notifyDataSetChanged();
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

        String objectId = ride.getObjectId();
        List<String> rideObjectIds = user.getRideObjectIds();
        if(rideObjectIds.contains(objectId)) {
            holder.ibReserve.setImageResource(R.drawable.car_reserve);
        }
        else {
            holder.ibReserve.setImageResource(R.drawable.car);
        }

        holder.tvSeats.setText(String.valueOf(ride.getSeats()));

        holder.ibReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add the ride in Rides view to list of myRides
                Ride ride = rides.get(holder.getAdapterPosition());
                boolean isReserved = ride.isReserved();
                holder.ibReserve = v.findViewById(R.id.ibReserve);
                holder.tvSeats = v.findViewById(R.id.tvSeatsCell);

                if(user.getRideObjectIds().contains(ride.getObjectId())) {
                    holder.ibReserve.setImageResource(R.drawable.car);
                    user.removeRide(ride);
                    ride.removeReservee(user);
                    ride.setSeats(ride.getSeats()  + 1);
                }
                else {
                    holder.ibReserve.setImageResource(R.drawable.car_reserve);
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
    public int getItemCount() {
        return rides.size();
    }

    @Override
    public Filter getFilter() {
        Log.i("I'm used", "getFilter");
        return filter;
    }

    public Filter getSpecificFilter(String type) {
        Log.i("oioi", "getSpecificFilter");
        if(type.equals("Geo")) return geoFilter;
        else return filter;
    }

    Filter geoFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    };

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

            if (constraint == null || constraint.toString().isEmpty()) {
                filteredList.addAll(allRides);
            }
            else {
                String filteredString = constraint.toString().toLowerCase().replace(" ","");
                filteredList = rideTrie.findTrieRides(filteredString);
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(constraint.toString().isEmpty()) {
                rides.clear();
                rides.addAll(allRides);
                notifyDataSetChanged();
            }
            else {
                if (!((ArrayList<Ride>) results.values).isEmpty()) {
                    rides.clear();
                    rides.addAll((ArrayList<Ride>) results.values);
                    notifyDataSetChanged();
                }
                else {
                    callToGoogleMapsSearch(constraint.toString());
                }
            }
        }
    };

    public void callToGoogleMapsSearch(String query) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        String urlBeforeQuery = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json")
                .url(urlBeforeQuery + query + "&key=" + context.getString(R.string.google_maps_api_key))
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                else {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray results = jsonResponse.getJSONArray("results");

                        if (results != null) {
                            rides.clear();
                            JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            Double lat = location.getDouble("lat");
                            Double lng = location.getDouble("lng");

                            for(Ride ride: allRides) {
                                float[] distanceBetween = new float[1];
                                Location.distanceBetween(lat, lng, ride.getDestinationLat(), ride.getDestinationLong(), distanceBetween);
                                float miles = distanceBetween[0]/1609;
                                if(miles < radiusMiles) {
                                    rides.add(ride);
                                }
                            }
                            ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void clear() {
        rides.clear();
        allRides.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvDestination;
        public TextView tvDate;
        public TextView tvTime;
        public TextView tvSeats;
        public ImageButton ibCarImage;
        public ImageButton ibReserve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvNameRider);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            ibCarImage = itemView.findViewById(R.id.ibProfileImageRider);
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
                intent.putExtra("isMyRidesView", false);
                intent.putExtra("isDriver",false);
                context.startActivity(intent);
            }
        }
    }
}
