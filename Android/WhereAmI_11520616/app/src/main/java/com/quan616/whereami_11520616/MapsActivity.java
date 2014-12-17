package com.quan616.whereami_11520616;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.Toast;
import android.provider.Settings;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private boolean gps_enabled=false;
    private boolean network_enabled=false;
    Location location;
    private Double myLatitude, myLongitude;
    private String CityName="";
    private String StateName="";
    private String CountryName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        turnGPSOn();
        getMyCurrentLocation();
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyCurrentLocation();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.setMyLocationEnabled(true);
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        if(myLatitude!=null && myLongitude!=null){
            Toast.makeText(this, "Vị trí hiện tại của bạn là : " +CityName +","+CountryName, Toast.LENGTH_SHORT).show();
            mMap.addMarker(new MarkerOptions().position(new LatLng(myLatitude,myLongitude)).title("Bạn đang ở đây"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude,myLongitude),15));
        }else{
            Toast.makeText(this, "Không tìm được vị trí hiện tại của bạn", Toast.LENGTH_SHORT).show();
        }
    }
    protected void getMyCurrentLocation() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();
        try{
            gps_enabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){}
        try{
            network_enabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){}
        if(gps_enabled){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            location=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if(network_enabled && location==null){
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location != null) {
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();
        } else {
            Location loc= getLastKnownLocation(this);
            if (loc != null) {
                myLatitude = loc.getLatitude();
                myLongitude = loc.getLongitude();
            }
        }
        locManager.removeUpdates(locListener);
        try{
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(myLatitude, myLongitude, 1);
            StateName= addresses.get(0).getAdminArea();
            CityName = addresses.get(0).getLocality();
            CountryName = addresses.get(0).getCountryName();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            if (location != null) {}
        }
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    public static Location getLastKnownLocation(Context context) {
        Location location = null;
        LocationManager locationmanager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            if(i != false && !locationmanager.isProviderEnabled(s))
                continue;
            Location location1 = locationmanager.getLastKnownLocation(s);
            if(location1 == null)
                continue;
            if(location != null)
            {
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if(f >= f1)
                {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if(l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            i = locationmanager.isProviderEnabled(s);
        } while(true);
        return location;
    }

    public void turnGPSOn(){
        try  {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        }
        catch (Exception e) {}
    }

    public void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

}