package com.lucidleanlabs.dev.lcatalog.utils;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager {

    String DOWNLOAD_URL;
    String Article_Name, Article_ID;

    public DownloadManager(String url, String article_name, String article_id) {

        DOWNLOAD_URL = url;
        Article_Name = article_name;
        Article_ID = article_id;
        Download();

    }

    private void Download() {
        try {
            URL u = new URL(DOWNLOAD_URL);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + Article_Name + "/" + Article_ID + ".zip"));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }

    }
}
