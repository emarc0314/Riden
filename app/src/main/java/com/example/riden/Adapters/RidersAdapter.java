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

import java.util.ArrayList;
import java.util.List;


public class RidersAdapter extends RecyclerView.Adapter<RidersAdapter.ViewHolder> {
    private Context context;
    private List<User> riders;

    private ImageButton ibProfileImageRider;
    private TextView tvNameRider;

    User driver = (User) User.getCurrentUser();

    public RidersAdapter(final Context context, final List<User> riders) {
        this.context = context;
        this.riders = riders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View driverHolder = LayoutInflater.from(context).inflate(R.layout.item_rider, parent, false);
        return new RidersAdapter.ViewHolder(driverHolder);
    }

    public void clear() {
        riders.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User rider = riders.get(position);
        holder.bind(rider);
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }

    public void removeItem(int position) {
        User rider = riders.get(position);
        riders.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(User rider, int position) {
        riders.add(position, rider);
    }

    public List<User> getData() {
        return riders;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameRider = itemView.findViewById(R.id.tvNameRider);
            ibProfileImageRider = itemView.findViewById(R.id.ibProfileImageRider);
        }

        public void bind(User rider) {
            tvNameRider.setText(rider.getFullName());
            ParseFile profileImage = rider.getProfileImage();
            if(profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).into(ibProfileImageRider);
            }
        }
    }
}
