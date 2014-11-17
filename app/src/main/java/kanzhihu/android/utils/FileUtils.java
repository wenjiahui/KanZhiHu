package kanzhihu.android.utils;

import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import kanzhihu.android.App;

/**
 * Created by Jiahui.wen on 2014/11/11.
 */
public class FileUtils {

    public static String getCachePath() {
        String path;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            //SD卡已挂载
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            path = App.getAppContext().getCacheDir().getAbsolutePath();
        }
        return path;
    }

    public static void writeStream2File(File file, InputStream is) throws FileNotFoundException {
        byte[] bytes = new byte[1024];
        int readCount;
        FileOutputStream fos = new FileOutputStream(file);
        try {
            while ((readCount = is.read(bytes)) != -1) {
                fos.write(bytes, 0, readCount);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fos);
            IOUtils.close(is);
        }
    }
}
