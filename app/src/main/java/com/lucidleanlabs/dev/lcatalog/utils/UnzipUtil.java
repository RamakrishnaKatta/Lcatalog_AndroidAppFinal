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
    private String dir_location;

    public UnzipUtil(String zipFileLocation, String extractLocation) {

        zipFile = zipFileLocation;
        location = extractLocation;
        dirChecker(zipFile);
        unzip();
    }

    private void unzip() {
        try {

            Log.e(TAG + " :unzip-Decompress", "Zip File Location ------" + zipFile);
            Log.e(TAG + " :unzip-Decompress", "Extraction Location ------" + location);

            FileInputStream file_in = new FileInputStream(zipFile);
            ZipInputStream zip_in = new ZipInputStream(file_in);

            Log.e(TAG + " :unzip-Decompress", "fin ------" + file_in);
            Log.e(TAG + " :unzip-Decompress", "zin ------" + zip_in);

            ZipEntry zip_entry;

            while ((zip_entry = zip_in.getNextEntry()) != null) {
                dir_location = zip_entry.getName();
                Log.e(TAG + " :unzip-Decompress", "Unzipping " + dir_location);

                if (zip_entry.isDirectory()) {
                    Log.e(TAG + " :unzip-Decompress", "Directory Location ----------------" + dir_location);
                    dirChecker(zip_entry.getName());
                } else {
                    FileOutputStream file_out = new FileOutputStream(location + dir_location);

                    for (int i = zip_in.read(); i != -1; i = zip_in.read()) {
                        file_out.write(i);
                    }
                    file_out.close();
                    zip_in.closeEntry();
                }
            }
            zip_in.close();
        } catch (Exception e) {
            Log.e(TAG + " :unzip-Decompress", "unzip", e);
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
            Log.e(TAG + " -dirChecker", "Directory is Created --- '" + wasSuccessful + "' Thank You !!");
        }else {
            Log.e(TAG + " -dirChecker", "Directory already exists" );
        }
    }
}
