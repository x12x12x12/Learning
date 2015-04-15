package app.com.augmentedreality.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.com.augmentedreality.core.AugmentedCamera;

/**
 * Created by HeavyRain on 4/1/2015.
 */
public class UtilFunctions {

    // GPSTracker class
    public static GPSTracker gps;
    /* Define variable for set auto taking photo */
    public static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

    /*Function for delete a file with file path*/
    public static void deleteFile(String filePath, Context context){
        File file = new File(filePath);
        boolean deleted = file.delete();
        Toast.makeText(context,"Deleted file "+filePath,Toast.LENGTH_SHORT).show();

    }

    /*Function for delete files and folders recursively*/
    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    /*
    *START: Function for get current location
    * */
    public static void getCurrentLocation(Context context){
        // create class object
        gps = new GPSTracker(context);
        String currentLocation= "Your current location: ";

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            currentLocation= currentLocation+ "Latitude "+latitude+"; Longitude "+longitude+"; ";

            Geocoder geocoder;
            List<Address> yourAddresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            try {
                yourAddresses= geocoder.getFromLocation(latitude, longitude, 1);
                if (yourAddresses.size() > 0)
                {
                    currentLocation= currentLocation + yourAddresses.get(0).getAddressLine(0)+" "+
                            yourAddresses.get(0).getAddressLine(1)+" "+
                            yourAddresses.get(0).getAddressLine(2);
                }

            } catch (IOException e) {
                Log.i("My Logs:",e.toString());
            }

            /*Log current location*/
            Log.i("My Logs:", currentLocation);

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    /*
    *END: Function for get current location
    * */

    /*
    * START: Auto taking photo
    * */

    /*Function for auto taking photo*/
    public static void autoTakingPhoto(final AugmentedCamera mOpenCvCameraView){
         /*Define a task for taking photo*/
        Runnable task =new Runnable() {

            public void run() {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String currentDateandTime = sdf.format(new Date());
                String fileName = Environment.getExternalStorageDirectory().getPath() +
                        "/sample_picture_" + currentDateandTime + ".png";
                mOpenCvCameraView.takePicture(fileName);

            }
        };
        /*Auto taking photo
        * worker.scheduleWithFixedDelay(command, initialDelay, Delay, unit );
        * First take photo happen after click auto button: 'initialDelay' seconds
        * After first take photo, every 'Delay' seconds take photo
        */
        worker.scheduleWithFixedDelay(task, 1, 5, TimeUnit.SECONDS);
    }

    /*Function for stop auto taking photo*/
    public static void stopAutoTakingPhoto(){
        worker.shutdown();
    }
    /*
    * STOP: Auto taking photo
    * */

    /*checks whether mobile is connected to internet*/
    public static void isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Log.i("My logs:","mobile is'nt connecting internet");
        } else
            Log.i("My logs:","mobile is connecting internet");
    }
}


