package com.shi.xianglixiangqin.activity.splash;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by SHI on 15/12/15.
 */
public abstract class FileCallBack extends Callback<File> {
    private String destFileDir;
    private String destFileName;
    private long currentSize;

    public FileCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    public File parseNetworkResponse(Response response, int id) throws Exception {
        return this.saveFile(response, id);
    }

    public File saveFile(Response response, final int id) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[8192];
        FileOutputStream fos = null;

        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            this.currentSize = 0L;
            File dir = new File(this.destFileDir);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, this.destFileName);
            fos = new FileOutputStream(file);

            int len1;
            while((len1 = is.read(buf)) != -1) {
                this.currentSize += (long)len1;
                fos.write(buf, 0, len1);
                fos.flush();
                OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
                    public void run() {
                        inProgress(currentSize, total, id);
                    }
                });
            }

            File var11 = file;
            return var11;
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}