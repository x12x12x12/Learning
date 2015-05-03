package app.com.augmentedreality;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import app.com.augmentedreality.core.AugmentedCamera;
import app.com.augmentedreality.core.DetectionBasedTracker;
import app.com.augmentedreality.core.tesseract.ImageProcessing;
import app.com.augmentedreality.util.AppData;
import app.com.augmentedreality.util.GeneralConst;


public class MainActivity  extends ActionBarActivity implements
        CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {

    private static final String TAG = "APP :: ";
    private AugmentedCamera mOpenCvCameraView;
    private ImageProcessing imageProcessing;
    private Camera mCamera;
    private Button btnGoogleSearch;
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    private Boolean                usingDetect = false;
    private Boolean                loadCache = false;
    private Mat                    mRgba;
    private Mat                    mGray;
    private MatOfRect              faces;
    private File                   mCascadeFile;
    private CascadeClassifier      mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;
    private int                    mDetectorType       = JAVA_DETECTOR;
    private String[]               mDetectorName;

    private Rect[]                 facesArrayCache;
    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 1;
    private int                    count               = 0;
    public static final int        JAVA_DETECTOR       = 0;
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);

    public static final String DATA_PATH =
            Environment.getExternalStorageDirectory().toString() + "/AndroidOCR/";
    /**
     * Load open cv lib && cascade file
     */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "Open cv lib loaded  successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);
                    try {
                        // load cascade file from application resources
//                        InputStream is = getResources().openRawResource( R.raw.cascade);
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
//                        mCascadeFile = new File(cascadeDir, "cascade.xml");
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else{
                            Log.i(TAG, "Loaded cascade classifier from "
                                    + mCascadeFile.getAbsolutePath());
                        }
                        cascadeDir.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }
                } break;
                default:{
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

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

        /**
         * Testing
         */
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
    public void onPause(){
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume(){
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.detect:
                usingDetect = true;
                return true;
            case R.id.no:
                usingDetect = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
//        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//        Imgproc.findContours(mGray,contours,new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        if(usingDetect) {
            if (mAbsoluteFaceSize == 0) {
                int height = mGray.rows();
                if (Math.round(height * mRelativeFaceSize) > 0) {
                    mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
                }
                mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
            }
            faces = new MatOfRect();
            if (mDetectorType == JAVA_DETECTOR) {
                if (mJavaDetector != null ){
                    mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2,
                            new org.opencv.core.Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new org.opencv.core.Size());
                    Rect[] facesArray = faces.toArray();
                    if(facesArray.length>0){
                        for (int i = 0; i < facesArray.length; i++){
                            Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 1);
                            StringBuilder sb = new StringBuilder();
                            sb.append("");
                            sb.append(facesArray[i].height);
                        }
                    }
                }
            }
        }
        return mRgba;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timeStamp = sdf.format(new Date());
            String fileName = "picture_" + timeStamp + ".png";
            mOpenCvCameraView.takePicture(DATA_PATH+fileName);
            imageProcessing = new ImageProcessing();
            String text = imageProcessing.ocrImage(fileName);
            if(!text.isEmpty()){
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return false;
    }

    private void initFolderData(){
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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                if (mAbsoluteFaceSize == 0) {
                    int height = mGray.rows();
                    if (Math.round(height * mRelativeFaceSize) > 0) {
                        mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
                    }
                    mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
                }
                faces = new MatOfRect();
                if (mDetectorType == JAVA_DETECTOR) {
                    if (mJavaDetector != null){
                        mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2,
                                new org.opencv.core.Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new org.opencv.core.Size());
                    }
                }
                Rect[] facesArray = faces.toArray();
                if(facesArray.length>0){
                    for (int i = 0; i < facesArray.length; i++){
                        Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(facesArray[i].height);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
