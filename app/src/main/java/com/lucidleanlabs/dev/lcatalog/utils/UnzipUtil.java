package com.lucidleanlabs.dev.lcatalog.utils;


import android.os.Environment;
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

        zipFile = zipFileLocation;
        location = extractLocation;
        //dirChecker(zipFile);
        dirChecker("");
        unzip();
    }

    private void unzip() {
        try {

            Log.e(TAG, "Zip File Location ----------------" + zipFile);
            Log.e(TAG, "Extraction Location ----------------" + location);

            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin);

            Log.e(TAG, "fin ----------------" + fin);
            Log.e(TAG, "zin ----------------" + zin);

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

        String state = Environment.getExternalStorageState();
        File folder = null;
        if (state.contains(Environment.MEDIA_MOUNTED)) {

            folder = new File(location + dir);
        }
        assert folder != null;
        if (!folder.exists()) {
            boolean wasSuccessful = folder.mkdirs();
            Log.e(TAG, "Directory is Created --- '" + wasSuccessful + "' Thank You !!");
        }
    }
}