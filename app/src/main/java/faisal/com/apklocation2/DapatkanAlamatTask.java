package faisal.com.apklocation2;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DapatkanAlamatTask extends AsyncTask<Location, Void, String>{
    List<Address> alamat = null;
    private onTaskSelesai mListener;
    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder = new Geocoder(mContex, Locale.getDefault());
        Location location = locations[0];
        List<Address> alamat = null;
        String resultMessage = null; //ubah

        try{
            alamat = geocoder.getFromLocation(
              location.getLatitude(),
                    location.getLongitude(),1);
        }
        catch (IOException ioException) {
            resultMessage = "Servis tidak tersedia";
            Log.e("Lokasi eror", resultMessage, ioException);
        }
        catch (IllegalArgumentException ilegal) {
            resultMessage = "koordinat invalid";
            Log.e("Lokasi Eror", resultMessage +". " +
                            "Latitude = " +location.getLatitude() +
                            ", Longlitude = " +location.getLongitude(),
                    ilegal);
        }
        if (alamat == null || alamat.size() ==0)
        {
            if (resultMessage.isEmpty())
            {
                resultMessage ="almat tidak ditemukan";
                Log.e("Lokasi error", resultMessage);
            }
        }
        else
            {
                Address address = alamat.get(0);
                ArrayList<String> berisiAlamat = new ArrayList<>();
                for(int i =0; i<= address.getMaxAddressLineIndex(); i++)
                {
                    berisiAlamat.add(address.getAddressLine(i));
                }
                resultMessage = TextUtils.join("\n", berisiAlamat);

            }
        return resultMessage;
    }

    @Override
    protected void onPostExecute(String alamat) {
        mListener.onTaskCompleted(alamat);
        super.onPostExecute(alamat);

    }
     private Context mContex;
    public DapatkanAlamatTask(Context applicationContext, onTaskSelesai listener) {
        mContex = applicationContext;
        mListener = listener;
    }
    interface onTaskSelesai{
        void onTaskCompleted(String result)throws SecurityException; //ubah
    }
}
