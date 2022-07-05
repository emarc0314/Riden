package com.example.riden.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Ride")
public class Ride extends ParseObject {
    public static final String DRIVER_KEY = "driver";
    public static final String SEATS_KEY = "numberSeats";
    public static final String CAR_IMAGE_KEY = "carImage";
    public static final String IS_RESERVED_KEY = "isReserved";
    public static final String CITY_PICKUP_KEY = "cityPickup";
    public static final String STATE_PICKUP_KEY = "statePickup";
    public static final String CITY_DESTINATION_KEY = "cityDestination";
    public static final String STATE_DESTINATION_KEY = "stateDestination";
    public static final String DEPARTURE_DATE_KEY = "departureDate";

    public static final String PICKUP_ADDRESS_KEY = "pickupAddress";
    public static final String DESTINATION_ADDRESS_KEY = "destinationAddress";
    public static final String PICKUP_LAT_KEY = "pickupLat";
    public static final String PICKUP_LONG_KEY = "pickupLong";
    public static final String DESTINATION_LONG_KEY = "destinationLong";
    public static final String DESTINATION_LAT_KEY = "destinationLat";
    public static final String FULL_DATE_KEY = "fullDate";

    public void setPickupAddress(String address) {
        put(PICKUP_ADDRESS_KEY, address);
    }

    public String getPickupAddress() {
        return getString(PICKUP_ADDRESS_KEY);
    }

    public void setDestinationAddress(String address) {
        put(DESTINATION_ADDRESS_KEY, address);
    }

    public String getDestinationAddress() {
        return getString(DESTINATION_ADDRESS_KEY);
    }

    public void setPickupLat(Double lat) {
        put(PICKUP_LAT_KEY, lat);
    }

    public Double getPickupLat() {
        return (Double) getNumber(PICKUP_LAT_KEY);
    }

    public void setPickupLong(Double longi) {
        put(PICKUP_LONG_KEY, longi);
    }

    public Double getPickupLong() {
        return (Double) getNumber(PICKUP_LONG_KEY);
    }

    public void setDestinationLat(Double lat) {
        put(DESTINATION_LAT_KEY, lat);
    }

    public Double getDestinationLat() {
        return (Double) getNumber(DESTINATION_LAT_KEY);
    }

    public void setDestinationLong(Double longi) {
        put(DESTINATION_LONG_KEY, longi);
    }

    public Double getDestinationLong() {
        return (Double) getNumber(DESTINATION_LONG_KEY);
    }

    public void setFullDate(String date) {
        put(FULL_DATE_KEY, date);
    }

    public String getFullDate() {
        return getString(FULL_DATE_KEY);
    }



    public ParseUser getDriver() {
        return getParseUser(DRIVER_KEY);
    }

    public void setDriver(ParseUser driver) {
        put(DRIVER_KEY, driver);
    }

    public int getSeats() {
        try {
            fetchIfNeeded().getString(SEATS_KEY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getInt(SEATS_KEY);
    }

    public void setSeats(int seats) {
        put(SEATS_KEY, seats);
    }

    public ParseFile getCarImage() {
        try {
            fetchIfNeeded().getString(CAR_IMAGE_KEY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getParseFile(CAR_IMAGE_KEY);
    }

    public void setCarImage(ParseFile image) {
        put(CAR_IMAGE_KEY, image);
    }

    public Boolean isReserved() {
        return getBoolean(IS_RESERVED_KEY);
    }

    public void setReserved(Boolean reserved) {
        put(IS_RESERVED_KEY, reserved);
    }

    public void setCityPickup(String cityPickup) {
        put(CITY_PICKUP_KEY, cityPickup);
    }

    public String getCityPickup() {
        return getString(CITY_PICKUP_KEY);
    }

    public void setStatePickup(String statePickup) {
        put(STATE_PICKUP_KEY, statePickup);
    }

    public String getStatePickup() {
        return getString(STATE_PICKUP_KEY);
    }

    public void setCityDestination(String cityDestination) {
        put(CITY_DESTINATION_KEY, cityDestination);
    }

    public String getCityDestination() {
        return getString(CITY_DESTINATION_KEY);
    }

    public void setStateDestination(String stateDestination) {
        put(STATE_DESTINATION_KEY, stateDestination);
    }

    public String getStateDestination() {
        return getString(STATE_DESTINATION_KEY);
    }

    public void setDepartureDate(String departureDate) {
        put(DEPARTURE_DATE_KEY, departureDate);
    }

    public String getDepartureDate() {
        return getString(DEPARTURE_DATE_KEY);
    }


}
