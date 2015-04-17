package app.com.augmentedreality.core.tesseract;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.leptonica.android.Binarize;
import com.googlecode.leptonica.android.Convert;
import com.googlecode.leptonica.android.GrayQuant;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.tesseract.android.TessBaseAPI;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

import app.com.augmentedreality.util.GeneralConst;

public class ImageProcessing extends AsyncTask<String,String,String> {
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AndroidOCR/";

    public String ocrImage(String paths){
        String result = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        File folder = new File(DATA_PATH);
        if(folder.isDirectory()) {
            String[] list_path = folder.list();
            for (String child : list_path){
                if(child.endsWith("png")|| child.endsWith("PNG")){
                    File imgFile = new File(DATA_PATH+"/"+child);
                    if (imgFile.canRead()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                        if(bitmap!=null){
                            try {
                                ExifInterface exif = new ExifInterface(paths);
                                int exifOrientation = exif.getAttributeInt
                                        (ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                                int rotate = 0;
                                switch (exifOrientation){
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
                                if(rotate!=0){
                                    int w = bitmap.getWidth();
                                    int h = bitmap.getHeight();
                                    Matrix mtx = new Matrix();
                                    mtx.preRotate(rotate);
                                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                                }
                                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                TessBaseAPI baseAPI = new TessBaseAPI();
                                baseAPI.init(DATA_PATH, GeneralConst.eng_lang);
                                baseAPI.setVariable("tessedit_char_whitelist", "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
                                baseAPI.setImage(processing(bitmap));
                                result = baseAPI.getUTF8Text();
                                baseAPI.end();
                                result = result.replaceAll("[^a-zA-Z0-9]+", " ");
                                result = result.trim();
                            }catch (Exception ex) {
                                Log.d("Debug", ex.toString());
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public Pix processing(Bitmap bitmap) {
        Pix pix = ReadFile.readBitmap(bitmap);
        pix = Binarize.otsuAdaptiveThreshold(pix);
        pix = Convert.convertTo8(pix);
        return pix;
    }

//    //Resize
//    public Bitmap Resize(Bitmap bmp, int newWidth, int newHeight){
//        Bitmap temp = (Bitmap)bmp;
//        Bitmap bmap = new Bitmap(newWidth, newHeight, temp.PixelFormat);
//        double nWidthFactor = (double)temp.getWidth() / (double)newWidth;
//        double nHeightFactor = (double)temp.Height / (double)newHeight;
//        double fx, fy, nx, ny;
//        int cx, cy, fr_x, fr_y;
//        Color color1 = new Color();
//        Color color2 = new Color();
//        Color color3 = new Color();
//        Color color4 = new Color();
//        byte nRed, nGreen, nBlue;
//
//        byte bp1, bp2;
//
//        for (int x = 0; x < bmap.Width; ++x){
//            for (int y = 0; y < bmap.Height; ++y)
//            {
//
//                fr_x = (int)Math.Floor(x * nWidthFactor);
//                fr_y = (int)Math.Floor(y * nHeightFactor);
//                cx = fr_x + 1;
//                if (cx >= temp.Width) cx = fr_x;
//                cy = fr_y + 1;
//                if (cy >= temp.Height) cy = fr_y;
//                fx = x * nWidthFactor - fr_x;
//                fy = y * nHeightFactor - fr_y;
//                nx = 1.0 - fx;
//                ny = 1.0 - fy;
//
//                color1 = temp.GetPixel(fr_x, fr_y);
//                color2 = temp.GetPixel(cx, fr_y);
//                color3 = temp.GetPixel(fr_x, cy);
//                color4 = temp.GetPixel(cx, cy);
//
//                // Blue
//                bp1 = (byte)(nx * color1.B + fx * color2.B);
//
//                bp2 = (byte)(nx * color3.B + fx * color4.B);
//
//                nBlue = (byte)(ny * (double)(bp1) + fy * (double)(bp2));
//
//                // Green
//                bp1 = (byte)(nx * color1.G + fx * color2.G);
//
//                bp2 = (byte)(nx * color3.G + fx * color4.G);
//
//                nGreen = (byte)(ny * (double)(bp1) + fy * (double)(bp2));
//
//                // Red
//                bp1 = (byte)(nx * color1.R + fx * color2.R);
//
//                bp2 = (byte)(nx * color3.R + fx * color4.R);
//
//                nRed = (byte)(ny * (double)(bp1) + fy * (double)(bp2));
//
//                bmap.SetPixel(x, y, System.Drawing.Color.FromArgb
//                        (255, nRed, nGreen, nBlue));
//            }
//        }
//        bmap = SetGrayscale(bmap);
//        bmap = RemoveNoise(bmap);
//        return bmap;
//
//    }
//
//    //SetGrayscale
//    public Bitmap SetGrayscale(Bitmap img){
//
//        Bitmap temp = (Bitmap)img;
//        Bitmap bmap = (Bitmap)temp.Clone();
//        Color c;
//        for (int i = 0; i < bmap.Width; i++)
//        {
//            for (int j = 0; j < bmap.Height; j++)
//            {
//                c = bmap.GetPixel(i, j);
//                byte gray = (byte)(.299 * c.R + .587 * c.G + .114 * c.B);
//
//                bmap.SetPixel(i, j, Color.FromArgb(gray, gray, gray));
//            }
//        }
//        return (Bitmap)bmap.Clone();
//
//    }
//    //RemoveNoise
//    public Bitmap RemoveNoise(Bitmap bmap)
//    {
//
//        for (var x = 0; x < bmap.Width; x++)
//        {
//            for (var y = 0; y < bmap.Height; y++)
//            {
//                var pixel = bmap.GetPixel(x, y);
//                if (pixel.R < 162 && pixel.G < 162 && pixel.B < 162)
//                    bmap.SetPixel(x, y, Color.Black);
//            }
//        }
//
//        for (int x = 0; x < bmap.Width; x++){
//            for (int y = 0; y < bmap.Height; y++){
//                int pixel = bmap.getPixel(x, y);
//                if (pixel.R > 162 && pixel.G > 162 && pixel.B > 162)
//                    bmap.SetPixel(x, y, Color.White);
//            }
//        }
//
//        return bmap;
//    }

    @Override
    protected String doInBackground(String... paths) {
        String result = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        String path = Environment.getExternalStorageDirectory().toString() + "/AndroidOCR/text_1.png";
        File imgFile = new File(path);
        if (imgFile.canRead()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
            if(bitmap!=null){
                try {
                    ExifInterface exif = new ExifInterface(path);
                    int exifOrientation = exif.getAttributeInt
                            (ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                    int rotate = 0;
                    switch (exifOrientation){
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
                    if(rotate!=0){
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        Matrix mtx = new Matrix();
                        mtx.preRotate(rotate);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                    }
                    // Convert to ARGB_8888, required by tess
                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    TessBaseAPI baseAPI = new TessBaseAPI();
                    baseAPI.init(DATA_PATH, GeneralConst.eng_lang);
                    baseAPI.setImage(bitmap);
                    result = baseAPI.getUTF8Text();
                    baseAPI.end();
//                    result = result.replaceAll("[^a-zA-Z0-9]+", " ");
//                    result = result.trim();
                }catch (Exception ex) {
                    Log.d("Debug", ex.toString());
                }
            }
        }
        return result;
  }
}
