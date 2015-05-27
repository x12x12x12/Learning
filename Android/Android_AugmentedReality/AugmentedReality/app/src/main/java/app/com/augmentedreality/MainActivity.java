package app.com.augmentedreality;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

import com.google.zxing.Reader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import app.com.augmentedreality.core.AugmentedCamera;
import app.com.augmentedreality.core.DetectionBasedTracker;
import app.com.augmentedreality.core.tesseract.ImageProcessing;
import app.com.augmentedreality.util.AppData;
import app.com.augmentedreality.util.GeneralConst;
import app.com.augmentedreality.util.OcrResult;
import app.com.augmentedreality.util.UtilFunctions;


public class MainActivity extends ActionBarActivity implements
        CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {

    public static final String TAG = "APP :: ";
    private AugmentedCamera mOpenCvCameraView;
    private OcrResult ocrResult;
    private Camera mCamera;
    public TessBaseAPI baseAPI;
    private Button btnGoogleSearch;
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    private Boolean                usingDetect = false;
    private Boolean                findContours = false;
    private Boolean                loadCache = false;
    private Mat                    mRgba;
    private Mat                    mGray;
    private MatOfRect              faces;
    private File                   mCascadeFile;
    private CascadeClassifier      mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;
    private int                    mDetectorType       = JAVA_DETECTOR;

    private Rect[]                 facesArrayCache;
    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 1;
    private int                    count               = 0;
    public static final int        JAVA_DETECTOR       = 0;
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AndroidOCR/";

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
                            Log.i(TAG, "Loaded cascade classifier from "+ mCascadeFile.getAbsolutePath());
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
        AppData appData = new AppData(this);
        appData.initFolderData();
        releaseCameraAndPreview();
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mOpenCvCameraView = (AugmentedCamera) findViewById(R.id.augmented_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        /**
         * Init OCR engine
         */
        baseAPI = new TessBaseAPI();
        baseAPI.init(DATA_PATH, GeneralConst.eng_lang);
        baseAPI.setVariable("tessedit_char_whitelist", "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

        /**
         * Testing section
         */
//        btnGoogleSearch =(Button)findViewById(R.id.btnGoogleSearch);
//        btnGoogleSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Starting a new Intent
//                Intent nextScreen = new Intent(getApplicationContext(), GoogleSearchActivity.class);
//                startActivity(nextScreen);
//            }
//        });
//        mainListView = (ListView)findViewById(R.id.mainListView);
//        String[] keyWords= new String[]{"UIT VN","TIKI.VN","DANTRI","TUOITRE"};
//        ArrayList<String> keyWordList= new ArrayList<String>();
//        keyWordList.addAll(Arrays.asList(keyWords));
//        listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row, keyWordList);
//        mainListView.setAdapter(listAdapter);
//        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent nextScreen = new Intent(getApplicationContext(), GoogleSearchActivity.class);
//                nextScreen.putExtra("searching","true");
//                nextScreen.putExtra("stringForSearch",(String)mainListView.getItemAtPosition(position));
//                startActivity(nextScreen);
//            }
//        });
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
        baseAPI.end();
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
                break;
            case R.id.no:
                usingDetect = false;
                break;
            case R.id.yes_contours:
                findContours = true;
                break;
            case R.id.no_contours:
                findContours = false;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
        if(findContours){
//            mRgba = findContours(mRgba, mGray);
            mRgba = detectTextArea(mRgba);
        }
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
            String fileName = Environment.getExternalStorageDirectory().getPath() + "openCV_" + timeStamp + ".jpg";
            mOpenCvCameraView.takePicture(fileName);
            Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
            readingImage(fileName);
        }catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return false;
    }

    public void readingImage(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        File imgFile = new File(filePath);
        if (imgFile.canRead()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
            if (bitmap != null) {
                Toast.makeText(this,"Test Reading Image",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void setOcrResult(OcrResult ocrResult) {
        this.ocrResult = ocrResult;
        String text = ocrResult.getText();
        Date d = new Date(ocrResult.getRecognitionTimeRequired());
        long timestamp = d.getTime();
        Log.i(TAG, "--- Got OCR result ---");
        Log.d(TAG, text);
        Log.i(TAG, "Time required :" + timestamp);
        Log.i(TAG, "--- End OCR result ---");
        if(!text.isEmpty()){
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    public void setImageByte(byte[] data){
        Log.i(TAG, "Got image data here");
        if(data.length>0) {
            String fileName = "cv_8s.PNG";
            new ImageProcessing(this, baseAPI, data, DATA_PATH + fileName).execute();
        }
    }

    public Mat detectTextArea(Mat mRgba) {
        Mat gray = new Mat();
        Mat edges = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        Imgproc.cvtColor(mRgba, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
        Imgproc.Canny(gray, edges, 75, 200);
        Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.sort(contours,Collections.reverseOrder());
        for(MatOfPoint contour : contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour);
            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
            if(approxDistance==4) {
                // Convert back to MatOfPoint
                MatOfPoint points = new MatOfPoint( approxCurve.toArray() );
                // Get bounding rect of contour
                Rect rect = Imgproc.boundingRect(points);
                // Draw enclosing rectangle
                Core.rectangle(mRgba, rect.tl(), rect.br(), new Scalar(255, 0, 0),1, 8,0);
                break;
            }
        }
        return mRgba;
    }

    public Mat findContours(Mat mRgba, Mat mGray) {
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.threshold(mGray, mGray, 100, 255, Imgproc.THRESH_BINARY);
        Imgproc.findContours(mGray,contours,new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        for (int i=0; i<contours.size(); i++){
            MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(i).toArray() );
            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
            MatOfPoint points = new MatOfPoint( approxCurve.toArray() );
            Rect rect = Imgproc.boundingRect(points);
            Core.rectangle(mRgba, rect.tl(), rect.br(), new Scalar(255, 0, 0),1, 8,0);
        }
        return mRgba;
    }
}
