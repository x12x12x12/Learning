package app.com.augmentedreality.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import app.com.augmentedreality.MainActivity;

/**
 * Created by F.U.C.K on 03-Apr-15.
 */
public class AppData {

    private Context context;

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/AndroidOCR/";

    public AppData(Context context) {
        this.context = context;
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
        List<String> allow_lang = new ArrayList<String>();
        allow_lang.add(GeneralConst.eng_lang);
        allow_lang.add(GeneralConst.vie_lang);
        for(String lang : allow_lang){
            String file_path = DATA_PATH + "tessdata/" + lang + ".traineddata";
            if (!(new File(file_path)).exists()) {
                try {
                    AssetManager assetManager = context.getAssets();
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
                    Log.e(MainActivity.TAG, ex.getMessage().toString());
                }
            }
        }
    }
}
