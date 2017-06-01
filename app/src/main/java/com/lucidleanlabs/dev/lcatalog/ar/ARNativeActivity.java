package com.lucidleanlabs.dev.lcatalog.ar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog.R;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;

public class ARNativeActivity extends ARActivity {

    private ARNativeRenderer arNativeRenderer = new ARNativeRenderer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arnative);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_screenshot);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Screen Shot", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                takeScreenshot();
            }
        });
    }

    private void takeScreenshot() {
        String mPath;
        File imageFile;
        Bitmap bitmap;
        try {
            // image naming and path  to include sd card  appending name you choose for file
            mPath = Environment.getExternalStorageDirectory().toString() + "//L_CATALOGUE/Screenshots";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            if (Environment.getExternalStorageState().contains(Environment.MEDIA_MOUNTED)) {
                imageFile = new File(mPath);
            } else {
                imageFile = new File(mPath);
            }
            boolean success = true;

            if (!imageFile.exists()) {
                success = imageFile.mkdirs();
            }
            if (success) {
                java.util.Date date = new java.util.Date();
                imageFile = new File(imageFile.getAbsolutePath() + File.separator + new Timestamp(date.getTime()).toString() + "Image.jpg");
                imageFile.createNewFile();
            } else {
                Toast.makeText(getBaseContext(), "Image Not saved", Toast.LENGTH_SHORT).show();
                return;
            }

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public void onStop() {
        ARNativeRenderer.demoShutdown();
        super.onStop();
    }

    @Override
    protected ARRenderer supplyRenderer() {
        return arNativeRenderer;
    }

    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout) this.findViewById(R.id.arFrameLayout);

    }
}
