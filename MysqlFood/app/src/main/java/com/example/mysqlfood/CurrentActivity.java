package com.example.mysqlfood;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class CurrentActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude,longitude;
    TextView txtaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final Intent intent=getIntent();
        latitude=intent.getDoubleExtra("latitude",0);
        longitude=intent.getDoubleExtra("longitude",0);
        txtaddress=findViewById(R.id.txtaddress2);
        Button btnadd=findViewById(R.id.btnAdd);
        btnadd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(CurrentActivity.this,AddActivity.class);
                intent1.putExtra("latitude",latitude);
                intent1.putExtra("longitude",longitude);
                intent1.putExtra("address",txtaddress.getText().toString());
                startActivityForResult(intent1,1);
            }
        });
        getAddress(new LatLng(latitude,longitude));
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("현재위치"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                 latitude=latLng.latitude;//클릭한 위치
                 longitude=latLng.longitude;

                mMap.clear();//기존위치 삭제
                MarkerOptions mOptions=new MarkerOptions();
                mOptions.title("위치");
                mOptions.position(latLng);
                mOptions.snippet(latitude + "/" + longitude);
                mMap.addMarker(mOptions);

                getAddress(latLng);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            setResult(RESULT_OK);
            finish();

        }
    }
    //주소구하기
    public void getAddress(LatLng latLng){
        Geocoder geocoder=new Geocoder(CurrentActivity.this, Locale.KOREAN);
        try{
            List<Address> list=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(!list.isEmpty()){
                String strAddress=list.get(0).getAddressLine(0).toString();
                txtaddress.setText(strAddress);
            }
        }catch(Exception e){ }
    }
}
