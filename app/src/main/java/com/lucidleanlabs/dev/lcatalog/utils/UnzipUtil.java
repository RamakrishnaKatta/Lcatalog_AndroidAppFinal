package com.lucidleanlabs.dev.lcatalog.utils;


import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtil {

    private static final String TAG = "UnZipActivity";

    private String zipFile;
    private String location;

    public UnzipUtil(String zipFileLocation, String extractLocation) {

        this.zipFile = zipFileLocation;
        this.location = extractLocation;
        dirChecker("");
    }

    public void unzip() {
        try {

            Log.e(TAG,"Zip File Location ----------------" + zipFile);
            Log.e(TAG,"Extraction Location ----------------" + location);

            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin);

            Log.e(TAG,"fin ----------------" + fin);
            Log.e(TAG,"zin ----------------" + zin);

            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(location + ze.getName());

                    for (int i = zin.read(); i != -1; i = zin.read()) {
                        fout.write(i);
                    }

//                    byte[] buffer = new byte[8192];
//                    int len;
//                    while ((len = zin.read(buffer)) != -1)
//                    {
//                        fout.write(buffer, 0, len);
//                    }
                    fout.close();
                    zin.closeEntry();
                }
            }
            zin.close();
        } catch (Exception e) {
            Log.e("Decompress", "unzip", e);
        }
    }

    private void dirChecker(String dir) {
        File f = new File(location + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
