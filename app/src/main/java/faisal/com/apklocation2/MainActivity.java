package faisal.com.apklocation2;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity implements DapatkanAlamatTask.onTaskSelesai{



    private static final int REQUEST_LOCATION_PERMISSON = 1;
    private Button mLocationButton;
    private TextView mLocationTextView;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private ImageView mAndroidImageView;
    private AnimatorSet mRotateAnim;
    private boolean mTrackingLocation;
    private LocationCallback mLocationCall;

    ImageView GambarGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationButton = (Button) findViewById(R.id.buttonLocation);
         mLocationTextView = (TextView) findViewById(R.id.textViewLocation);
         mAndroidImageView= (ImageView) findViewById(R.id.img_android);

        mRotateAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate);
        mRotateAnim.setTarget(mAndroidImageView);

        GambarGif = (ImageView)findViewById(R.id.imgGif);
        Glide.with(MainActivity.this).load(R.drawable.coba).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(GambarGif);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTrackingLocation)
                {
                    mulaiTrackingLokasi();
                }
                else
                    {
                        stopTracking();
                    }
//                getLocations(); iki modul 2 dan 1,2
            }
        });

        mLocationCall = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if (mTrackingLocation)
                {
                    new DapatkanAlamatTask( MainActivity.this,
                            MainActivity.this).execute(locationResult.getLastLocation());
                }
            }
        };
    }

    private void getLocations() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSON);
        }
        else {

//            Log.d("GETPERMISI","getLocations: permissions granted");
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            TextView mLocationTextView = (TextView) findViewById(R.id.textViewLocation);
                            if (location != null){
//     ikimodul 2                       mLastLocation = location;
//                                      mLocationTextView.setText(getString(R.string.location_text,
//                                        mLastLocation.getLatitude(),
//                                        mLastLocation.getLongitude(),
//                                        mLastLocation.getTime()));
                                new DapatkanAlamatTask(MainActivity.this, MainActivity.this).execute(location);

                            }
                            else {
                                mLocationTextView.setText("Lokasi Tidak tersedia");
                            }
                        }
                    }
            );
        }
        mLocationTextView.setText(getString(R.string.alamat_text,"sedang mencari alamat",System.currentTimeMillis()));
    }

    private void mulaiTrackingLokasi()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSON);
        }
        else {
           mFusedLocationClient.requestLocationUpdates(getLocationRequest(),mLocationCall,null);
            mLocationTextView.setText(getString(R.string.alamat_text,"sedang mencari alamat cinta",System.currentTimeMillis()));
            mTrackingLocation = true;
            mLocationButton.setText("Berhenti tracking syank");
            mRotateAnim.start();
        }

    }
    private void stopTracking()
    {
        if (mTrackingLocation)
        {

            mTrackingLocation = false;
            mFusedLocationClient.removeLocationUpdates(mLocationCall);
            mLocationButton.setText("Mulai mencari serpihan hati yang hilang");
            mLocationTextView.setText("Pencari cinta sedng berhenti");
            mRotateAnim.end();

        }
    }
    private LocationRequest getLocationRequest()
    {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSON:

                if(grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getLocations();
                }
                else {
                    Toast.makeText(this, "permisinya maaf ditolak ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        mLocationTextView.setText(getString(R.string.alamat_text,result, System.currentTimeMillis()));
    }

}
