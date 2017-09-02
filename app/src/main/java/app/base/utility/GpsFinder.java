package app.base.utility;


import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import app.base.framework.Init;

public class GpsFinder {
    /** Called when the activity is first created. */
   
    public String findLocation() {
		LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)Init.bigContext.getSystemService(serviceName);
        String provider = LocationManager.GPS_PROVIDER;
//        String provider = LocationManager.NETWORK_PROVIDER;
        
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        String provider = locationManager.getBestProvider(criteria, true);
        
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 2000, 10,
        		locationListener);
        return updateWithNewLocation(location);
	}
    public static LocationListener locationListener = new LocationListener() {
    	public void onLocationChanged(Location location) {
    	updateWithNewLocation(location);
    	}
    	public void onProviderDisabled(String provider){
    	updateWithNewLocation(null);
    	}
    	public void onProviderEnabled(String provider){ }

		public void onStatusChanged(String provider, int status,
    	Bundle extras){ }
    };
    public static String updateWithNewLocation(Location location) {
    	String latLongString;    	if (location != null) {
    	double lat = location.getLatitude();
    	double lng = location.getLongitude();
			latLongString = "lat:"+lat + "-" + "lon:"+lng;
    	} else {
    		
    	latLongString = null;
    	}
    	List<Address> addList = null;
    	Geocoder ge = new Geocoder(Init.bigContext);
    	try {
			addList = ge.getFromLocation(24.463, 118.1, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(addList!=null && addList.size()>0){
    		for(int i=0; i<addList.size(); i++){
    			Address ad = addList.get(i);
    			latLongString += "\n";
    			latLongString += ad.getCountryName() + ";" + ad.getLocality();
    		}
    	}
    	return latLongString;
    }
}