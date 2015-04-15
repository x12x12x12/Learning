package app.com.augmentedreality.core;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by F.U.C.K on 30-Mar-15.
 */
public class AugmentedCamera extends JavaCameraView implements PictureCallback {

    private static final String TAG = "Augmented Reality :: ";
    private String mPictureFileName;

    /**
     * Default constructor
     * @param context
     * @param attrs
     */
    public AugmentedCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void takePicture(final String fileName) {
        this.mPictureFileName = fileName;
        mCamera.setPreviewCallback(null);
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);
        try {
            FileOutputStream fos = new FileOutputStream(mPictureFileName);
            fos.write(data);
            fos.close();
        } catch (java.io.IOException e) {
            Log.e(TAG, "Exception in photoCallback", e);
        }

    }
}
