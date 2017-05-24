package com.lucidleanlabs.dev.lcatalog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class View3dActivity extends AppCompatActivity {
    private static final String TAG = "View3dActivity";


    // Used to handle pause and resume...
    public static View3dActivity master = null;

    private GLSurfaceView mGLView;
    private MyRenderer renderer = null;
    private FrameBuffer fb = null;
    private World world = null;

    private float touchTurnUp = 0;
    private float touchTurn = 0;

    private float xpos = -1;
    private float ypos = -1;

    private Object3D cube = null;
    private int fps = 0;

    private Light sun1 = null;
    private Light sun2 = null;
    private Light sun3 = null;
    String location, _3dslocation, _textureLocation_1, _textureLocation_2;
    File _3ds_file, _texture_fil_1, _texture_fil_2;

    protected void onCreate(Bundle savedInstanceState) {

        Logger.log("View3DActivity");

        if (master != null) {
            copy(master);
        }

        Bundle b3 = getIntent().getExtras();
        location = (String) b3.getCharSequence("3ds_location");
        Log.e(TAG, "Location ---- " + location);

        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(this);

        mGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        renderer = new MyRenderer();
        mGLView.setRenderer(renderer);
        setContentView(mGLView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void copy(Object src) {
        try {
            Logger.log("Copying data from master Activity!");
            Field[] fs = src.getClass().getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                f.set(this, f.get(src));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean onTouchEvent(MotionEvent me) {

        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            xpos = me.getX();
            ypos = me.getY();
            return true;
        }

        if (me.getAction() == MotionEvent.ACTION_UP) {
            xpos = -1;
            ypos = -1;
            touchTurn = 0;
            touchTurnUp = 0;
            return true;
        }

        if (me.getAction() == MotionEvent.ACTION_MOVE) {
            float xd = me.getX() - xpos;
            float yd = me.getY() - ypos;

            xpos = me.getX();
            ypos = me.getY();

            touchTurn = xd / -100f;
            touchTurnUp = yd / -100f;
            return true;
        }

        try {
            Thread.sleep(15);
        } catch (Exception e) {
            // No need for this...
        }

        return super.onTouchEvent(me);
    }

    protected boolean isFullscreenOpaque() {
        return true;
    }

    private class MyRenderer implements GLSurfaceView.Renderer {

        private long time = System.currentTimeMillis();

        public MyRenderer() {
        }

        public void onSurfaceChanged(GL10 gl, int w, int h) {
            if (fb != null) {
                fb.dispose();
            }
            fb = new FrameBuffer(gl, w, h);

            if (master == null) {

                world = new World();
                world.setAmbientLight(25, 25, 25);

                sun1 = new Light(world);
                sun1.setIntensity(250, 250, 250);

                sun2 = new Light(world);
                sun2.setIntensity(250, 250, 250);

                sun3 = new Light(world);
                sun3.setIntensity(250, 250, 250);


                _textureLocation_1 = location + "/_1.jpg";
                _textureLocation_2 = location + "/_2.jpg";
                _3dslocation = location + "/article_view.3ds";

                _texture_fil_1 = new File(_textureLocation_1);
                Log.e(TAG, "3DS Object Texture 1 ----" + _texture_fil_1);
                _texture_fil_2 = new File(_textureLocation_2);
                Log.e(TAG, "3DS Object Texture 2 ----" + _texture_fil_2);
                _3ds_file = new File(_3dslocation);
                Log.e(TAG, "3DS Object File ----" + _3ds_file);

                if (_3ds_file.exists()) {

                    Log.e(TAG, "3DS Object Available ----" + _3ds_file);
                    try {
                        cube = loadModel((float) 0.5);
                        Log.e(TAG, "3DS Object Loaded");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (_texture_fil_1.exists()) {

                    Log.e(TAG, "Texture 1 Available for this 3DS Object ----" + _texture_fil_1);
                    Texture texture1 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getDrawableForStore(_textureLocation_1)), 512, 512));
                    TextureManager.getInstance().addTexture("texture1", texture1);
                    cube.calcTextureWrapSpherical();
                    cube.setTexture("texture1");

                } else {
//                    Toast.makeText(View3dActivity.this, "No Textures Available for this 3DS Object ", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "No Textures 1 Available for this 3DS Object ----" + _texture_fil_1);
                }

                if (_texture_fil_2.exists()) {

                    Log.e(TAG, "Texture 2 Available for this 3DS Object ----" + _texture_fil_2);
                    Texture texture2 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getDrawableForStore(_textureLocation_2)), 512, 512));
                    TextureManager.getInstance().addTexture("texture2", texture2);
                    cube.setTexture("texture2");

                } else {
//                    Toast.makeText(View3dActivity.this, "No Textures Available for this 3DS Object ", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "No Textures 2 Available for this 3DS Object ----" + _texture_fil_2);
                }

                cube.build();
                world.addObject(cube);

                Camera cam = world.getCamera();
                cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
                cam.lookAt(cube.getTransformedCenter());

                SimpleVector sv1 = new SimpleVector();
                sv1.set(cube.getTransformedCenter());
                sv1.y -= 100;
                sv1.z -= 100;
                Log.e(TAG, "Sun 1 - Vector Value" + sv1);
                sun1.setPosition(sv1);

                SimpleVector sv2 = new SimpleVector();
                sv2.set(cube.getTransformedCenter());
                sv2.x += 100;
                sv2.y += 100;
                sv2.z += 100;
                Log.e(TAG, "Sun 2 - Vector Value" + sv2);
                sun2.setPosition(sv2);

                SimpleVector sv3 = new SimpleVector();
                sv3.set(cube.getTransformedCenter());
                sv3.x -= 100;
                sv3.y += 100;
                sv3.z -= 100;
                Log.e(TAG, "Sun 2 - Vector Value" + sv3);
                sun2.setPosition(sv3);

                MemoryHelper.compact();

                if (master == null) {
                    Logger.log("Saving master Activity!");
                    master = View3dActivity.this;
                }
            }
        }

        public Drawable getDrawableForStore(String localLink) {
            Bitmap thumbnail = null;
            try {
                File filePath = new File(localLink);
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                Log.e("getThumbnail() on internal storage", ex.getMessage());
                return null;
            }

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float scaledDensity = metrics.density;
            int width = thumbnail.getWidth();
            int height = thumbnail.getHeight();

            if (scaledDensity < 1) {

                width = (int) (width * scaledDensity);
                height = (int) (height * scaledDensity);
            } else {
                width = (int) (width + width * (scaledDensity - 1));
                height = (int) (height + height * (scaledDensity - 1));
            }

            thumbnail = Bitmap.createScaledBitmap(thumbnail, width, height, true);
            Drawable d = new BitmapDrawable(getResources(), thumbnail);

            return d;

        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            gl.glDisable(GL10.GL_DITHER);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

            gl.glClearColor(0, 0, 0, 0);
            gl.glEnable(GL10.GL_CULL_FACE);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glEnable(GL10.GL_DEPTH_TEST);
        }

        public void onDrawFrame(GL10 gl) {
            if (touchTurn != 0) {
                cube.rotateY(touchTurn);
                touchTurn = 0;
            }

            if (touchTurnUp != 0) {
                cube.rotateX(touchTurnUp);
                touchTurnUp = 0;
            }

            fb.clear();
            world.renderScene(fb);
            world.draw(fb);
            fb.display();

            if (System.currentTimeMillis() - time >= 1000) {
                Logger.log(fps + "fps");
                fps = 0;
                time = System.currentTimeMillis();
            }
            fps++;
        }

        public Object3D loadModel(float scale) throws IOException {
            //InputStream stream = getResources().openRawResource(R.raw.flatsofa);

            InputStream stream = new FileInputStream(_3ds_file);
            Log.e(TAG, "3DS Object Retrieved ----" + _3ds_file);
            Log.e(TAG, "Check the Stream ----" + stream);

            Object3D[] model = Loader.load3DS(stream, scale);
            Object3D o3d = new Object3D(0);
            Object3D temp = null;

            for (int i = 0; i < model.length; i++) {
                temp = model[i];
                temp.setCenter(SimpleVector.ORIGIN);
                temp.rotateX((float) (-.5 * Math.PI));
                temp.rotateMesh();
                temp.setRotationMatrix(new Matrix());
                o3d = Object3D.mergeObjects(o3d, temp);
                o3d.build();
            }

            stream.close();
            return o3d;
        }
    }
}