package org.marfnk.georallye.services;

import java.util.LinkedList;
import java.util.List;

import org.marfnk.georallye.adapter.Observer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class Navigator implements LocationListener {
    private static final String TAG = "Navigator";
    private Location lastKnownLocation;
    private LocationManager locationManager;
    private String locationProvider = LocationManager.GPS_PROVIDER;
    private Location reference;
    private List<Observer> actionListeners = new LinkedList<Observer>();

    public Navigator(Context c) {
        locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
    }

    public void registerListener() {
        locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
    }

    public void unregisterListener() {
        locationManager.removeUpdates(this);
    }

    public void setLocation(double latitude, double longitude) {
        Location l = new Location("CUSTOM");
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        reference = l;
        
        onLocationChanged(lastKnownLocation);
    }

    public float getBearing() {
        if (lastKnownLocation != null && reference != null) {
            float bearTo = lastKnownLocation.bearingTo(reference);
            if (bearTo < 0) {
                bearTo = bearTo + 360;
            }
            return bearTo;
        } else {
            return 0;
        }
    }

    public float getDistance() {
        if (lastKnownLocation != null && reference != null) {
            return lastKnownLocation.distanceTo(reference);
        } else {
            return 0;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "provider enabled: " + provider + " " + status + " (" + extras + ")");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "provider enabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "provider disabled" + provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("TAG", "new location: " + location);
        lastKnownLocation = location;

        notifyListeners();
    }

    public void addObserver(Observer al) {
        actionListeners.add(al);
    }

    private void notifyListeners() {
        for (Observer l : actionListeners) {
            l.onNotified();
        }
    }

    public void resetLocation() {
        reference = null;
        onLocationChanged(reference);
    }
}
