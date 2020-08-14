package com.example.myproject;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    ImageView btnmapsearch;
    DrawerLayout maplayout;
    View mapdrawer;
    EditText edtseatch;
    Myadapter ad;
    private GoogleMap mMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<>();

    double latitude = 0;
    double longitude = 0;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    ListView maplist;
    MarkerOptions mylocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        maplayout = findViewById(R.id.maplayout);
        mapdrawer = findViewById(R.id.drawerbar);
        btnmapsearch = findViewById(R.id.btnmapsearch);
        edtseatch = findViewById(R.id.edtmapsearch);
        ad = new Myadapter();
        maplist = findViewById(R.id.maplist);
        maplist.setAdapter(ad);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GpsTracker gpsTracker = new GpsTracker(MapsActivity.this);
        latitude = gpsTracker.getLatitude(); // 위도
        longitude = gpsTracker.getLongitude(); //경도
        btnmapsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array.clear();
                maplayout.openDrawer(mapdrawer);
                SearchThread searchThread = new SearchThread();
                searchThread.execute();
            }
        });

        FloatingActionButton searchmylocation=findViewById(R.id.searchmylocation);
        searchmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsTracker gpsTracker = new GpsTracker(MapsActivity.this);
                latitude = gpsTracker.getLatitude(); // 위도
                longitude = gpsTracker.getLongitude(); //경도
                LatLng sydney2 = new LatLng(latitude, longitude);
                mylocation.position(sydney2);
                mMap.addMarker(mylocation);
                // mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney2));
                //mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(sydney2, 15);
                mMap.animateCamera(location);
            }
        });

        edtseatch.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            array.clear();
                            maplayout.openDrawer(mapdrawer);
                            SearchThread searchThread = new SearchThread();
                            searchThread.execute();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    //-----------------------------------------------https://www.it-swarm.dev/ko/android/%EB%B2%A1%ED%84%B0-%EC%9E%90%EC%82%B0-%EC%95%84%EC%9D%B4%EC%BD%98%EC%9D%B4%EC%9E%88%EB%8A%94-android%EC%9D%98-google%EC%A7%80%EB%8F%84%EC%97%90%EC%84%9C-%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%A0%95%EC%9D%98-%EB%A7%88%EC%BB%A4/829632466/
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //----------------------------------------------위의것은 marker의 icon을 바꾸기위해 필요한것이다
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mylocation = new MarkerOptions().position(sydney);
        mylocation.icon(bitmapDescriptorFromVector(this, R.drawable.ic_my_location_black_24dp));
        mylocation.title("현제 위치");


        mMap.addMarker(mylocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public class GpsTracker extends Service implements LocationListener {
        private final Context mContext;
        Location location;
        double latitude;
        double longitude;
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
        protected LocationManager locationManager;

        public GpsTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!isGPSEnabled && !isNetworkEnabled) {
                    System.out.println("gps와 네트워크안켜져있음");
                } else {
                    int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
                    int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        ActivityCompat.requestPermissions(MapsActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);//권한 요청
                        System.out.println("권한없음");
                        return null;
                    }
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("gps오류 " + e.getMessage());
            }
            return location;
        }

        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }
            return latitude;
        }

        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }
            return longitude;
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }


        public void stopUsingGPS() {
            if (locationManager != null) {
                locationManager.removeUpdates(GpsTracker.this);
            }
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }


    class SearchThread extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
            String query = edtseatch.getText().toString();
            String s = Daum.connect(url + "?query=" + query + "&page=1");
            // System.out.println(s);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            parsing(s);
            //System.out.println("데이터 갯수" + array.size());
            ad.notifyDataSetChanged();
            super.onPostExecute(s);
        }
    }

    public void parsing(String s) {
        try {
            JSONObject jarray2 = new JSONObject(s).getJSONObject("meta");
            //is_end = jarray2.getBoolean("is_end");
            JSONArray jarray = new JSONObject(s).getJSONArray("documents");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject obj = jarray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("place_name", obj.getString("place_name"));
                map.put("phone", obj.getString("phone"));
                map.put("address_name", obj.getString("address_name"));
                map.put("x", obj.getString("x"));
                map.put("y", obj.getString("y"));
                array.add(map);
            }
        } catch (Exception e) {
            System.out.println("오류" + e.getMessage());
        }
    }


    class Myadapter extends BaseAdapter {
        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item_maplist, viewGroup, false);
            TextView txtlocalname = view.findViewById(R.id.txtlocalname);
            TextView txtaddress = view.findViewById(R.id.txtaddress);
            TextView contactnumber = view.findViewById(R.id.contactnumber);
            final HashMap<String, String> map = array.get(i);
            txtlocalname.setText(map.get("place_name"));
            txtaddress.setText(map.get("address_name"));
            contactnumber.setText(map.get("phone"));
            contactnumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + map.get("phone")));

                    startActivity(intent);
                }
            });
            RelativeLayout maplistlayout = view.findViewById(R.id.maplistlayout);
            maplistlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.clear();
                    double y = Double.parseDouble(map.get("y"));
                    double x = Double.parseDouble(map.get("x"));
                    LatLng sydney2 = new LatLng(y, x);
                    MarkerOptions mylocation2 = new MarkerOptions().position(sydney2).title(map.get("place_name"));
                    mMap.addMarker(mylocation2);
                   // mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney2));
                    //mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(sydney2, 15);
                    mMap.animateCamera(location);
                    maplayout.closeDrawer(mapdrawer);

                }
            });
            return view;
        }
    }

}
