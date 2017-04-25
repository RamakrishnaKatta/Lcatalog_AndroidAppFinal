package com.lucidleanlabs.dev.lcatalog;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog.adapters.ImageViewAdapter;

import java.io.File;
import java.util.ArrayList;

public class ScreenshotActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public static final String TAG = "ScreenshotActivity";
    Context context;
    File[] listFile;
    ArrayList<String> f;
    File file;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);

        /*Recycler view for the screenshots*/

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error No card Found!", Toast.LENGTH_SHORT).show();
        } else {
            file = new File(android.os.Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Screenshots/");
            file.mkdirs();
        }
        if (file.isDirectory()) {
            listFile = file.listFiles();

            f = new ArrayList<>(listFile.length);

            Log.e(TAG, "ScreenshotActivity------" + listFile);
            Log.e(TAG, "ScreenshotActivity--" + file);

            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());
            }
        }

        ImageViewAdapter imageViewAdapter = new ImageViewAdapter(this, f);
        recyclerView.setAdapter(imageViewAdapter);
    }
}










