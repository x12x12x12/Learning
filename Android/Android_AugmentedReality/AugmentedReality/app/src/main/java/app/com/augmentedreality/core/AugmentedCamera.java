package app.com.augmentedreality.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private String mPictureFileName;


    /**
     * Default constructor
     * @param context
     * @param attrs
     */
    public AugmentedCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }

    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);
    }

    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }

    public void takePicture(final String fileName) {
        this.mPictureFileName = fileName;
        this.mPictureFileName = fileName;
        mCamera.setPreviewCallback(null);
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i("AugmentedCamera::", "onPictureTaken");
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);
        try {
            FileOutputStream fos = new FileOutputStream(mPictureFileName);
            fos.write(data);
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
    }
}
