package com.example.riden.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riden.models.Ride;
import com.example.riden.models.User;

import java.util.List;

public class myOfferedRidesAdapter extends RecyclerView.Adapter<myOfferedRidesAdapter.ViewHolder> {
    Context context;
    List<Ride> myOfferedRides;
    TextView tvDestination;
    TextView tvDate;
    TextView tvTime;
    TextView tvSeats;
    ImageButton ibCarImage;
    ImageButton ibReserve;
    User user = (User) User.getCurrentUser();

    public myOfferedRidesAdapter(Context context, List<Ride> myOfferedRides) {
       this.context = context;
       this.myOfferedRides = myOfferedRides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
