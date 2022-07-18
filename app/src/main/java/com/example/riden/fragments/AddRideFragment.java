package com.example.riden.fragments;
//package placepic

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.riden.R;
import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Places;
//import com.google.android.libraries.places.api.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
//import com.google.android.gms.location.places.zza;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddRideFragment extends Fragment {
    public static final String TAG = "AddRideFragment";
    private EditText etPickupLocation;
    private EditText etDestination;
    private ImageButton ibCalendar;
    private TextView tvCalendarInput;
    private DatePickerDialog datePickerDialog;
    private Button btAddRide;
    private ImageButton ibUploadCarImage;
    private EditText etNumberSeats;
    private EditText etPrice;
    public File photoFile;
    public String photoFileName = "photo.jpg";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    public String cityPickup;
    public String statePickup;
    public String cityDestination;
    public String stateDestination;
    public String departureDate;
    public String seats = "";

    public String fullDate;
    public String pickupAddress;
    public String destinationAddress;
    public Double pickupLat;
    public Double pickupLong;
    public Double destinationLat;
    public Double destinationLong;

    MarkerOptions pickupLocation, destinationLocation;
    private User user = (User) User.getCurrentUser();
    ParseFile image;
    boolean pickup = false;

    public AddRideFragment() {
        // Required empty public constructor
    }

    private String getUSStateCode(Address USAddress) {
        String fullAddress = "";
        for(int j = 0; j <= USAddress.getMaxAddressLineIndex(); j++)
            if (USAddress.getAddressLine(j) != null)
                fullAddress = fullAddress + " " + USAddress.getAddressLine(j);

        String stateCode = null;
        Pattern pattern = Pattern.compile(" [A-Z]{2} ");
        String helper = fullAddress.toUpperCase().substring(0, fullAddress.toUpperCase().indexOf("USA"));
        Matcher matcher = pattern.matcher(helper);
        while (matcher.find())
            stateCode = matcher.group().trim();

        return stateCode;
    }

    private void saveRide(String cityPickup, String statePickup, String cityDestination, String stateDestination, String departureDate, String seats, ParseFile image, String price) {
        //TODO: check if all the parameters aren't null; if they aren't, then Toast a unsuccessfull add and prompt user to add other required fields
        Ride ride = new Ride();
        ride.setSeats(Integer.valueOf(seats));
        ride.setCarImage(image);
        ride.setCityDestination(cityDestination);
        ride.setStateDestination(stateDestination);
        ride.setCityPickup(cityPickup);
        ride.setStatePickup(statePickup);
        ride.setDepartureDate(departureDate);
        ride.setReserved(false);
        ride.setDriver(User.getCurrentUser());
        ride.setPrice(price);

        ride.setFullDate(fullDate);
        ride.setPickupLat(pickupLat);
        ride.setPickupLong(pickupLong);
        ride.setDestinationLong(destinationLong);
        ride.setDestinationLat(destinationLat);
        ride.setDestinationAddress(destinationAddress);
        ride.setPickupAddress(pickupAddress);


        ride.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving",e);
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful");
                Toast.makeText(getContext(), "Successfully Added a Ride!", Toast.LENGTH_SHORT).show();
            }
        });
        user.addMyOfferedRide(ride);
        user.saveInBackground();
    }

    protected void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDatePicker();
        etPickupLocation = view.findViewById(R.id.etPickupLocation);
        etDestination = view.findViewById(R.id.etDestination);
        ibCalendar = view.findViewById(R.id.ibCalendar);
        tvCalendarInput = view.findViewById(R.id.tvCalendarInput);
        btAddRide = view.findViewById(R.id.btAddRide);
        ibUploadCarImage = view.findViewById(R.id.ibUploadCarImage);
        etNumberSeats = view.findViewById(R.id.etNumberSeats);
        etPrice = view.findViewById(R.id.etPrice);
        tvCalendarInput.setText(getTodaysDate());

        ibCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seats = etNumberSeats.getText().toString();
                String price = etPrice.getText().toString();
                saveRide(cityPickup,statePickup,cityDestination,stateDestination,departureDate,seats, image, price);
                //TODO: Create a new ride, and add it to the list of existing rides
            }
        });
        etPickupLocation.setFocusable(false);
        etPickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickup = true;
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(view.getContext());
                startActivityForResult(intent, 100);
            }
        });

        ibUploadCarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        Places.initialize(view.getContext(), getString(R.string.google_maps_api_key));
        etDestination.setFocusable(false);
        etDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickup = false;
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(view.getContext());
                startActivityForResult(intent, 100);
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = makeDateString(dayOfMonth, month, year);
                fullDate = getMonthFormat(month) + " " + dayOfMonth + ", " + year;
                departureDate = getMonthFormat(month) + " " + dayOfMonth;
                tvCalendarInput.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        return "NUL";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            LatLng coordinates = place.getLatLng();
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses =
                        geocoder.getFromLocation(
                        coordinates.latitude,
                        coordinates.longitude,
                        1); // Only retrieve 1 address
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addresses.get(0);

            if(pickup) {
                etPickupLocation.setText(place.getAddress());
                cityPickup = address.getLocality();
                statePickup = getUSStateCode(address);
                pickupAddress = place.getAddress();
                pickupLat = coordinates.latitude;
                pickupLong = coordinates.longitude;
            }
            else {
                etDestination.setText(place.getAddress());
                cityDestination = address.getLocality();
                stateDestination = getUSStateCode(address);
                destinationAddress = place.getAddress();
                destinationLat = coordinates.latitude;
                destinationLong = coordinates.longitude;

                pickupLocation = new MarkerOptions().position(new LatLng(pickupLat, pickupLong)).title("Pickup Location");
                destinationLocation = new MarkerOptions().position(new LatLng(destinationLat, destinationLong)).title("Destination Location");



                float[] results = new float[1];
                Location.distanceBetween(pickupLat, pickupLong, destinationLat, destinationLong, results);
//                Location.
                /** CALCULATION FOR PRICE
                 * - adding a profit multiplier
                 * - add a higher multiplier if it is evening or day
                 * - add a time to take a ride
                 * - too early in the morning, classes, charge more for that
                 * -
                 * - tolls on the road; using google maps api
                 * - how many people are going with you on the ride
                 * - decrease price as more poeple join the ride
                 * - type of car - optional
                 * - if there is no ride already available and you request a ride, it costs more
                 * - counting the number of states crossed and multiply the price by that
                 * - counting tolls on the way from point A to B (maybe use api for this)
                 * - stretch goal - adding spotify
                 */

                float price = (float) (results[0] * 0.585/1609);
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String priceShown = formatter.format(price);
                etPrice.setText(priceShown);
                Log.i("distance", String.valueOf(results[0]));
            }
        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

        else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ibUploadCarImage.setImageBitmap(takenImage);
                image = new ParseFile(photoFile);
            }
            else {
                Log.i("this", "something went wrong!");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ride, container, false);
    }
}