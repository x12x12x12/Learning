package app.com.augmentedreality;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import app.com.augmentedreality.core.AugmentedCamera;
import app.com.augmentedreality.core.tesseract.ImageProcessing;
import app.com.augmentedreality.util.AppData;
import app.com.augmentedreality.util.GeneralConst;


public class MainActivity extends Activity implements
        CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {

    private static final String TAG = "APP :: ";
    private AugmentedCamera mOpenCvCameraView;
    private ImageProcessing imageProcessing;
    private Camera mCamera;
    /*Define button for change to google_search's sreen*/
    private Button btnGoogleSearch;
    /*Hard code list for example*/
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:{
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);
                } break;
                default:{
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/AndroidOCR/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFolderData();
        releaseCameraAndPreview();
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (AugmentedCamera) findViewById(R.id.augmented_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        imageProcessing = new ImageProcessing();
        btnGoogleSearch =(Button)findViewById(R.id.btnGoogleSearch);
        btnGoogleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), GoogleSearchActivity.class);
                startActivity(nextScreen);
            }
        });

        mainListView = (ListView)findViewById(R.id.mainListView);
        String[] keyWords= new String[]{"UIT VN","TIKI.VN","DANTRI","TUOITRE"};
        ArrayList<String> keyWordList= new ArrayList<String>();
        keyWordList.addAll(Arrays.asList(keyWords));
        listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row, keyWordList);
        mainListView.setAdapter(listAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextScreen = new Intent(getApplicationContext(), GoogleSearchActivity.class);
                nextScreen.putExtra("searching","true");
                nextScreen.putExtra("stringForSearch",(String)mainListView.getItemAtPosition(position));
                startActivity(nextScreen);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = sdf.format(new Date());
        String fileName = Environment.getExternalStorageDirectory().getPath() +
                "/AndroidOCR/sample_picture_" + timeStamp + ".jpg";
        try{
            mOpenCvCameraView.takePicture(fileName);
//            TessBaseAPI baseAPI = new TessBaseAPI();
//            baseAPI.init(DATA_PATH, GeneralConst.eng_lang);
//            Bitmap bitmap = imageProcessing.prepareImage(fileName);
//            baseAPI.setImage(bitmap);
//            String text = baseAPI.getUTF8Text();
//            baseAPI.end();
//            text = text.replaceAll("[^a-zA-Z0-9]+", " ");
//            text = text.trim();
            Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
        }catch (Exception ex) {
            Log.e(TAG, ex.toString());
            return false;
        }
        return false;
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume(){
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }

    public void initFolderData(){
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return;
                }
            }
        }
        List<String> allow_lang = new ArrayList<>();
        allow_lang.add(GeneralConst.eng_lang);
        allow_lang.add(GeneralConst.vie_lang);
        for(String lang : allow_lang){
            String file_path = DATA_PATH + "tessdata/" + lang + ".traineddata";
            if (!(new File(file_path)).exists()) {
                try {
                    AssetManager assetManager = getAssets();
                    InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
                    OutputStream out = new FileOutputStream(file_path);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                } catch (IOException ex) {
                    Log.e(TAG, ex.getMessage().toString());
                }
            }
        }

    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
