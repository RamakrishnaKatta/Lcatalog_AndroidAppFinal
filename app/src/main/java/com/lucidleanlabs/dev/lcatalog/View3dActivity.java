package com.lucidleanlabs.dev.lcatalog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
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

    private Light sun = null;
    private String _3dslocation;
    private File _3ds;

    protected void onCreate(Bundle savedInstanceState) {

        Logger.log("View3DActivity");

        if (master != null) {
            copy(master);
        }

        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(this);

        mGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGLView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        renderer = new MyRenderer();
        mGLView.setRenderer(renderer);
        mGLView.setBackgroundColor(Color.GREEN);
        setContentView(mGLView);

        Bundle b3 = getIntent().getExtras();

        _3dslocation = (String) b3.getCharSequence("3ds_location");
        Log.e(TAG, "3DS Location ---- " + _3dslocation);

//        LinearLayout anotherLayout = new LinearLayout(this);
//        anotherLayout.setBackgroundColor(Color.TRANSPARENT);
//        LinearLayout.LayoutParams linearLayoutParams =
//                new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.FILL_PARENT,
//                        LinearLayout.LayoutParams.FILL_PARENT);
//
//        addContentView(anotherLayout, linearLayoutParams);


//        BackgroundView backgroundView = new BackgroundView(this);
//        addContentView(backgroundView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
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

    class MyRenderer implements GLSurfaceView.Renderer {

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
                world.setAmbientLight(20, 20, 20);

                sun = new Light(world);
                sun.setIntensity(350, 350, 350);

                _3ds = new File(_3dslocation);

                try {
                    cube = loadModel((float) 2.0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                String TextureLocation = _3ds + "/_1.jpg";

//                if (getDrawableForStore(TextureLocation) != null) {
//
//                    Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getDrawableForStore(TextureLocation)), 512, 512));
//                    TextureManager.getInstance().addTexture("texture", texture);
//                    cube.setTexture("texture");
//                    cube.build();
//                } else {
//                    Toast.makeText(View3dActivity.this, "No Textures Available for this 3D Object ", Toast.LENGTH_SHORT).show();
//                }

                world.addObject(cube);

                Camera cam = world.getCamera();
                cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
                cam.lookAt(cube.getTransformedCenter());

                SimpleVector sv = new SimpleVector();
                sv.set(cube.getTransformedCenter());
                sv.y -= 100;
                sv.z -= 100;
                sun.setPosition(sv);
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

            _3ds = new File(_3dslocation);
            Log.e(TAG, "File Location" + _3ds);
            InputStream stream = new FileInputStream(_3ds + "/article_view.3ds");
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