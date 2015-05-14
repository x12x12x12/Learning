package app.com.augmentedreality.core;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;
import java.util.List;

import app.com.augmentedreality.MainActivity;
import app.com.augmentedreality.core.tesseract.ImageProcessing;

/**
 * Created by F.U.C.K on 30-Mar-15.
 */
public class AugmentedCamera extends JavaCameraView implements PictureCallback {

    private MainActivity mainActivity;
    private TessBaseAPI baseAPI;

    /**
     * Default constructor
     * @param context
     * @param attrs
     */
    public AugmentedCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void takePicture(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mCamera.setPreviewCallback(null);
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i("AugmentedCamera::", "onPictureTaken");
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);
        this.mainActivity.setImageByte(data);
        Log.i("AugmentedCamera::", "finish set image byte");
    }
}
