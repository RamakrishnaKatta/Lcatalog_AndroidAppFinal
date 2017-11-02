/*
 *  ARActivity.java
 *  ARToolKit5
 *
 *  This file is part of ARToolKit.
 *
 *  ARToolKit is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ARToolKit is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ARToolKit.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  As a special exception, the copyright holders of this library give you
 *  permission to link this library with independent modules to produce an
 *  executable, regardless of the license terms of these independent modules, and to
 *  copy and distribute the resulting executable under terms of your choice,
 *  provided that you also meet, for each linked independent module, the terms and
 *  conditions of the license of that module. An independent module is a module
 *  which is neither derived from nor based on this library. If you modify this
 *  library, you may extend this exception to your version of the library, but you
 *  are not obligated to do so. If you do not wish to do so, delete this exception
 *  statement from your version.
 *
 *  Copyright 2015 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *
 */

package org.artoolkit.ar.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import org.artoolkit.ar.base.camera.CameraEventListener;
import org.artoolkit.ar.base.camera.CameraPreferencesActivity;
import org.artoolkit.ar.base.camera.CaptureCameraPreview;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.gles20.ARRendererGLES20;

//import android.os.AsyncTask;
//import android.os.AsyncTask.Status;

/**
 * An activity which can be subclassed to create an AR application. ARActivity handles almost all of
 * the required operations to create a simple augmented reality application.
 * <p/>
 * ARActivity automatically creates a camera preview surface and an OpenGL surface view, and
 * arranges these correctly in the user interface.The subclass simply needs to provide a FrameLayout
 * object which will be populated with these UI components, using {@link #supplyFrameLayout() supplyFrameLayout}.
 * <p/>
 * To create a custom AR experience, the subclass should also provide a custom renderer using
 * {@link #supplyRenderer() Renderer}. This allows the subclass to handle OpenGL drawing calls on its own.
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public abstract class ARActivity extends Activity implements CameraEventListener, View.OnClickListener {

    /**
     * Android logging tag for this class.
     */
    protected final static String TAG = "ARActivity";
    /**
     * Renderer to use. This is provided by the subclass using {@link #supplyRenderer() Renderer()}.
     */
    protected ARRenderer renderer;
    /**
     * Layout that will be filled with the camera preview and GL views. This is provided by the subclass using {@link #supplyFrameLayout() supplyFrameLayout()}.
     */
    protected FrameLayout mainFrameLayout;
    /**
     * Camera preview which will provide video frames.
     */
    private CaptureCameraPreview preview;
    /**
     * GL surface to render the virtual objects
     */
    private GLSurfaceView mOpenGlSurfaceViewInstance;
    private boolean firstUpdate = false;

    private Context mContext;

    private ImageButton mSettingButton;

    @SuppressWarnings("unused")
    public Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        // This needs to be done just only the very first time the application is run,
        // or whenever a new preference is added (e.g. after an application upgrade).
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Correctly configures the activity window for running AR in a layer
        // on top of the camera preview. This includes entering 
        // fullscreen landscape mode and enabling transparency. 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Toast toast = Toast.makeText(this, "WAIT !! " + "\n" + "We are configuring your Camera for Augmentation !!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        AndroidUtils.reportDisplayInformation(this);
    }

    /**
     * Allows subclasses to supply a custom {@link Renderer}.
     *
     * @return The {@link Renderer} to use.
     */
    protected abstract ARRenderer supplyRenderer();

    /**
     * Allows subclasses to supply a {@link FrameLayout} which will be populated
     * with a camera preview and GL surface view.
     *
     * @return The {@link FrameLayout} to use.
     */
    protected abstract FrameLayout supplyFrameLayout();

    @Override
    protected void onStart() {

        super.onStart();

        Log.i(TAG, "onStart(): Activity starting.");

        if (!ARToolKit.getInstance().initialiseNative(this.getCacheDir().getAbsolutePath())) { // Use cache directory for Data files.
            notifyFinish("The native library is not loaded. The application cannot continue.");
            return;
        }

        mainFrameLayout = supplyFrameLayout();
        if (mainFrameLayout == null) {
            Log.e(TAG, "onStart(): Error: supplyFrameLayout did not return a layout.");
            return;
        }

        renderer = supplyRenderer();
        if (renderer == null) {
            Log.e(TAG, "onStart(): Error: supplyRenderer did not return a renderer.");
            // No renderer supplied, use default, which does nothing
            renderer = new ARRenderer();
        }
    }

    @Override
    public void onResume() {
        //Log.i(TAG, "onResume()");
        super.onResume();

        // Create the camera preview
        preview = new CaptureCameraPreview(this, this);

        Log.e(TAG, "onResume(): CaptureCameraPreview created");

        // Create the GL view
        mOpenGlSurfaceViewInstance = new GLSurfaceView(this);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo;
        configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportsEs2;
        assert configurationInfo != null;
        supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            Log.e(TAG, "onResume(): OpenGL ES 2.x is supported");

            if (renderer instanceof ARRendererGLES20) {
                // Request an OpenGL ES 2.0 compatible context.
                mOpenGlSurfaceViewInstance.setEGLContextClientVersion(2);
            } else {
                Log.e(TAG, "onResume(): OpenGL ES 2.x is supported but only a OpenGL 1.x renderer is available." +
                        " \n Use ARRendererGLES20 for ES 2.x support. \n Continuing with OpenGL 1.x.");
                mOpenGlSurfaceViewInstance.setEGLContextClientVersion(1);
            }
        } else {
            Log.e(TAG, "onResume(): Only OpenGL ES 1.x is supported");
            if (renderer instanceof ARRendererGLES20)
                throw new RuntimeException("Only OpenGL 1.x available but a OpenGL 2.x renderer was provided.");
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            mOpenGlSurfaceViewInstance.setEGLContextClientVersion(1);
        }

        mOpenGlSurfaceViewInstance.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mOpenGlSurfaceViewInstance.getHolder().setFormat(PixelFormat.TRANSLUCENT); // Needs to be a translucent surface so the camera preview shows through.
        mOpenGlSurfaceViewInstance.setRenderer(renderer);
        mOpenGlSurfaceViewInstance.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // Only render when we have a frame (must call requestRender()).
        mOpenGlSurfaceViewInstance.setZOrderMediaOverlay(true); // Request that GL view's SurfaceView be on top of other SurfaceViews (including CameraPreview's SurfaceView).

        Log.e(TAG, "onResume(): GLSurfaceView created");

        // Add the views to the interface
        mainFrameLayout.addView(preview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainFrameLayout.addView(mOpenGlSurfaceViewInstance, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Log.e(TAG, "onResume(): Views added to main layout.");

        if (mOpenGlSurfaceViewInstance != null) {
            mOpenGlSurfaceViewInstance.onResume();
        }

        //Load settings button
        View settingsButtonLayout = this.getLayoutInflater().inflate(R.layout.settings_button_layout, mainFrameLayout, false);
        mSettingButton = settingsButtonLayout.findViewById(R.id.button_settings);
        mainFrameLayout.addView(settingsButtonLayout);
        mSettingButton.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        //Log.i(TAG, "onPause()");
        super.onPause();

        if (mOpenGlSurfaceViewInstance != null) {
            mOpenGlSurfaceViewInstance.onPause();
        }

        // System hardware must be released in onPause(), so it's available to
        // any incoming activity. Removing the CameraPreview will do this for the
        // camera. Also do it for the GLSurfaceView, since it serves no purpose
        // with the camera preview gone.
        mainFrameLayout.removeView(mOpenGlSurfaceViewInstance);
        mainFrameLayout.removeView(preview);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop(): Activity stopping.");

        super.onStop();
    }


    @Override
    public void onClick(View v) {
        if (v.equals(mSettingButton)) {
            v.getContext().startActivity(new Intent(v.getContext(), CameraPreferencesActivity.class));
        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        View decorView = getWindow().getDecorView();
//        if (hasFocus) {
//            // Now can configure view to run  full screen
//            decorView.setSystemUiVisibility(AndroidUtils.VIEW_VISIBILITY);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, CameraPreferencesActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Returns the camera preview that is providing the video frames.
     *
     * @return The camera preview that is providing the video frames.
     */
    @SuppressWarnings("unused")
    public CaptureCameraPreview getCameraPreview() {
        return preview;
    }

    /**
     * Returns the GL surface view.
     *
     * @return The GL surface view.
     */
    @SuppressWarnings("unused")
    public GLSurfaceView getGLView() {
        return mOpenGlSurfaceViewInstance;
    }

    @Override
    public void cameraPreviewStarted(int width, int height, int rate, int cameraIndex, boolean cameraIsFrontFacing) {

        if (ARToolKit.getInstance().initialiseAR(width, height, "/storage/emulated/0/L_CATALOGUE/cache/Data/camera_para.dat", cameraIndex, cameraIsFrontFacing)) {
            // Expects Data to be already in the cache dir. This can be done with the AssetUnpacker.

            Toast toast = Toast.makeText(this, "Almost there, Few more seconds !!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

            Log.e(TAG, "getGLView(): Camera initialised");
        } else {
            // Error
            Log.e(TAG, "getGLView(): Error initialising camera. Cannot continue.");
            finish();
        }

        Log.e(TAG, "Camera settings: " + width + "x" + height + "@" + rate + "fps");

        firstUpdate = true;
    }

    @Override
    public void cameraPreviewFrame(byte[] frame) {

        if (firstUpdate) {
            // ARToolKit has been initialised. The renderer can now add markers, etc...
            if (renderer.configureARScene()) {


                Toast toast = Toast.makeText(this, "YES" + "\n" + "Thank You for your patience, The furniture is ready to get Augmented !!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();

                Log.e(TAG, "cameraPreviewFrame(): Scene configured successfully");
            } else {
                // Error
                Log.e(TAG, "cameraPreviewFrame(): Error configuring scene. Cannot continue.");
                finish();
            }
            firstUpdate = false;
        }

        if (ARToolKit.getInstance().convertAndDetect(frame)) {

            // Update the renderer as the frame has changed
            if (mOpenGlSurfaceViewInstance != null)
                mOpenGlSurfaceViewInstance.requestRender();

            onFrameProcessed();
        }

    }

    public void onFrameProcessed() {
    }

    @Override
    public void cameraPreviewStopped() {
        ARToolKit.getInstance().cleanup();
    }

    @SuppressWarnings("unused")
    protected void showInfo() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setMessage("ARToolKit Version: " + NativeInterface.arwGetARToolKitVersion());

        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = dialogBuilder.create();
        alert.setTitle("ARToolKit");
        alert.show();
    }

    public void notifyFinish(String errorMessage) {
        new AlertDialog.Builder(this)
                .setMessage(errorMessage)
                .setTitle("Error")
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }
                        })
                .show();
    }
}