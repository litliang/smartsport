package app.base;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
/***
 * gps工具类
 * @author Administrator
 *
 */
public class GpsUtil {
	static LocationManager locMan = null;
	public static Location getLocation(Context context) {
		Location gpslocation = null;
		Location networkLocation = null;

		
		if (locMan == null) {
			locMan = (LocationManager) context.getApplicationContext()
					.getSystemService(Context.LOCATION_SERVICE);
		}
		LocationListener locationListener = new LocationListener() {

			public void onLocationChanged(Location location) { // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
				// log it when the location changes
				if (location != null) {
					List<Address> addresses = null;
					try {
						Geocoder gc = new Geocoder(null);
						addresses = gc.getFromLocation(location.getLatitude(),
								location.getLongitude(), 1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (addresses.size() > 0) {
						String msg = "AddressLine："
								+ addresses.get(0).getAddressLine(0) + "\n";
						msg += "CountryName："
								+ addresses.get(0).getCountryName() + "\n";
						msg += "Locality：" + addresses.get(0).getLocality()
								+ "\n";
						msg += "FeatureName："
								+ addresses.get(0).getFeatureName();
					}
					Logs.i("SuperMap",
							"Location changed : Lat: " + location.getLatitude()
									+ " Lng: " + location.getLongitude());
				}
			}

			public void onProviderDisabled(String provider) {
				// Provider被disable时触发此函数，比如GPS被关闭
			}

			public void onProviderEnabled(String provider) {
				// Provider被enable时触发此函数，比如GPS被打开
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub

			}

		};
		try {
			if (locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
						1, locationListener);
				gpslocation = locMan
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			}
			if (locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						1, 1, locationListener);
				networkLocation = locMan
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		} catch (IllegalArgumentException e) {
			// Log.e(ErrorCode.ILLEGALARGUMENTERROR, e.toString());
			Logs.e("error", e.toString());
		}
		if (gpslocation == null && networkLocation == null)
			return null;

		if (gpslocation != null && networkLocation != null) {
			if (gpslocation.getTime() < networkLocation.getTime()) {
				gpslocation = null;
				return networkLocation;
			} else {
				networkLocation = null;
				return gpslocation;
			}
		}

		return networkLocation;
	}

	public static Location openGPSSettings(Activity context) {
		LocationManager alm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			// doWork(context);
			 registerLocationListener(context);
			 return null;
		}

		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		context.startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
		return null;

	}

	public static void doWork(Context context) {
		double latitude = 0.0;
		double longitude = 0.0;

		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		} else {
			LocationListener locationListener = new LocationListener() {

				// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {

				}

				// Provider被enable时触发此函数，比如GPS被打开
				@Override
				public void onProviderEnabled(String provider) {

				}

				// Provider被disable时触发此函数，比如GPS被关闭
				@Override
				public void onProviderDisabled(String provider) {

				}

				// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {
						Logs.e("Map",
								"Location changed : Lat: "
										+ location.getLatitude() + " Lng: "
										+ location.getLongitude());
					}
				}
			};
			locationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000, 0, locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude(); // 经度
				longitude = location.getLongitude(); // 纬度
			}
		}
	}

	private static class MyLocationListner implements LocationListener {
		
		@Override
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the location provider.
			Logs.w(
					"onLocationChanged " + location.getProvider());
			if (currentLocation != null) {
				if (isBetterLocation(location, currentLocation)) {
					Log.v("GPSTEST", "It's a better location");
					currentLocation = location;
					showLocation(location);
				} else {
					Log.v("GPSTEST", "Not very good!");
				}
			} else {
				Log.v("GPSTEST", "It's first location");
				currentLocation = location;
				showLocation(location);
			}
			// 移除基于LocationManager.NETWORK_PROVIDER的监听器
			if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
				locationManager.removeUpdates(this);
			}
		}

		// 后3个方法此处不做处理
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Logs.w(
					"onStatusChanged " + provider+" "+extras.keySet().toArray());
		}

		public void onProviderEnabled(String provider) {

			Logs.w(
					"onProviderEnabled " + provider);
			
		}

		public void onProviderDisabled(String provider) {

			Logs.w(
					"onProviderDisabled " + provider);
		}
	};

	static Location currentLocation;

	private static void showLocation(Location location) {
		// 纬度
		Log.v("GPSTEST", "Latitude:" + location.getLatitude());
		// 经度
		Log.v("GPSTEST", "Longitude:" + location.getLongitude());
		// 精确度
		Log.v("GPSTEST", "Accuracy:" + location.getAccuracy());
		// Location还有其它属性，请自行探索
	}

	private static final int CHECK_INTERVAL = 1000 * 30;

	protected static boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location,
		// use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must
			// be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	private static LocationListener gpsListener = null;
	private static LocationListener networkListner = null;
	private static LocationManager locationManager;

	public static void registerLocationListener(Context context) {
		if (locationManager == null) {
			locationManager = (LocationManager) context.getApplicationContext()
					.getSystemService(Context.LOCATION_SERVICE);
		}
		networkListner = new MyLocationListner();
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 1, networkListner);
//		gpsListener = new MyLocationListner();
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//				1, 1, gpsListener);
//		
		Location location = locationManager.getLastKnownLocation("network");
		Logs.w("=== location "+location);

		
	}
}
