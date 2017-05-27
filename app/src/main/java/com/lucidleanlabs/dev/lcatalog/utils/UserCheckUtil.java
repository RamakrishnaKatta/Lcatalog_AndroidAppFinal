package com.lucidleanlabs.dev.lcatalog.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserCheckUtil {

    private static final String TAG = "UserCheckUtil";

    public static void writeToFile(String data, String user_type) {
        try {
            Log.e(TAG + "-writeToFile", "Storage State: " + isExternalStorageReadOnly() + "Storage State(ExternalStorageAvailability): " + isExternalStorageAvailable());
            String text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/" + user_type + ".txt";
            FileOutputStream fileOutputStream = new FileOutputStream(text_file_location);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e(TAG + "-Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(String user_type) {

        String ret = "";
        String text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/" + user_type + ".txt";

        try {
            Log.e(TAG + "-readFromFile", "Storage State(ReadOnlyState): " + !isExternalStorageReadOnly() + " Storage State(ExternalStorageAvailability): " + isExternalStorageAvailable());
            FileInputStream fileInputStream = new FileInputStream(text_file_location);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
            String strLine;

            while ((strLine = bufferedReader.readLine()) != null) {
                ret = ret + strLine;
            }
            dataInputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG + "-login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
