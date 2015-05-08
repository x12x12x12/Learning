package app.com.augmentedreality.core.tesseract;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.googlecode.leptonica.android.Binarize;
import com.googlecode.leptonica.android.Convert;
import com.googlecode.leptonica.android.GrayQuant;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.tesseract.android.TessBaseAPI;


import java.io.File;
import java.io.IOException;

import app.com.augmentedreality.MainActivity;
import app.com.augmentedreality.util.GeneralConst;
import app.com.augmentedreality.util.OcrResult;

public class ImageProcessing extends AsyncTask<Void, Void, Boolean> {

    private MainActivity mainActivity;
    private TessBaseAPI baseApi;
    private OcrResult ocrResult;
    private String path;

    public Pix processing(Bitmap bitmap) {
        Pix pix = ReadFile.readBitmap(bitmap);
        pix = Binarize.otsuAdaptiveThreshold(pix);
        pix = Convert.convertTo8(pix);
        return pix;
    }

    public ImageProcessing(MainActivity mainActivity,TessBaseAPI baseApi,String path) {
        this.mainActivity = mainActivity;
        this.baseApi = baseApi;
        this.path = path;
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {
        long start = System.currentTimeMillis();
        long timeRequired;
        String textResult;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        File imgFile = new File(path);
        if (imgFile.canRead()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
            if (bitmap != null) {
                try{
                    ExifInterface exif = new ExifInterface(path);
                    int exifOrientation = exif.getAttributeInt
                            (ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotate = 0;
                    switch (exifOrientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                    }
                    if (rotate != 0) {
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        Matrix mtx = new Matrix();
                        mtx.preRotate(rotate);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                    }
                    baseApi.setImage(processing(bitmap));
                    textResult = baseApi.getUTF8Text();
                    timeRequired = System.currentTimeMillis() - start;
                    if (textResult == null || textResult.equals("")) {
                        return false;
                    }
                    ocrResult = new OcrResult();
                    ocrResult.setText(textResult);
                    ocrResult.setWordConfidences(baseApi.wordConfidences());
                    ocrResult.setMeanConfidence( baseApi.meanConfidence());
                    ocrResult.setRegionBoundingBoxes(baseApi.getRegions().getBoxRects());
                    ocrResult.setTextlineBoundingBoxes(baseApi.getTextlines().getBoxRects());
                    ocrResult.setWordBoundingBoxes(baseApi.getWords().getBoxRects());
                    ocrResult.setStripBoundingBoxes(baseApi.getStrips().getBoxRects());
                }catch (Exception ex) {
                    baseApi.clear();
                }
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        this.mainActivity.setOcrResult(this.ocrResult);
        if (baseApi != null) {
            baseApi.clear();
        }
    }
}
