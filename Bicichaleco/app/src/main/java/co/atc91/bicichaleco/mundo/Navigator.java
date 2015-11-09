package co.atc91.bicichaleco.mundo;

import java.util.List;

import org.w3c.dom.Document;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import co.atc91.android.utilities.gmaps.gmaps.Navigation;

public class Navigator implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;

    private LatLng startPoint;

    private LatLng endPoint;
    private List<LatLng> directions;

    private List<String> maneuvers;

    public Navigator(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Inicializa los atributos
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        startPoint = new LatLng(
                Double.valueOf(String.valueOf(location.getLatitude())),
                Double.valueOf(String.valueOf(location.getLongitude())));

        Document document = Navigation.getDocument(startPoint, endPoint, Navigation.MODE_DRIVING);

        directions = Navigation.getDirection(document);
        maneuvers = Navigation.getManeuvers(document);

        //Se subscribe a las actualizaciones de la posicion
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        startPoint = new LatLng(
                Double.valueOf(String.valueOf(location.getLatitude())),
                Double.valueOf(String.valueOf(location.getLongitude())));
    }

    public void setEndPoint( LatLng location )
    {
        endPoint = location;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public List<LatLng> getDirections() {
        return directions;
    }

    public void setDirections(List<LatLng> directions) {
        this.directions = directions;
    }

    public List<String> getManeuvers() {
        return maneuvers;
    }

    public void setManeuvers(List<String> maneuvers) {
        this.maneuvers = maneuvers;
    }
}