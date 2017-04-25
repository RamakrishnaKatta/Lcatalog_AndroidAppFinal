package com.lucidleanlabs.dev.lcatalog.dots;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

import com.lucidleanlabs.dev.lcatalog.utils.Utils;


public class fav_circle extends View {
    private static final int START_COLOR = 0xFFFF5722;
    private static final int END_COLOR = 0xFFFFC107;

    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();


    private Paint circlePaint = new Paint();
    private Paint maskPaint = new Paint();

    private Bitmap tempBitmap;
    private Canvas tempCanvas;

    private float outerCircleRadiusProgress = 0f;
    private float innerCircleRadiusProgress = 0f;

    private int maxCircleSize;


    public fav_circle(Context context) {
        super(context);
        init();
    }


    public fav_circle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public fav_circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public fav_circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        circlePaint.setStyle(Paint.Style.FILL);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxCircleSize = w / 2;
        tempBitmap = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);
        tempCanvas = new Canvas(tempBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tempCanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR);
        tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, outerCircleRadiusProgress * maxCircleSize, circlePaint);
        tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, innerCircleRadiusProgress * maxCircleSize, maskPaint);
        canvas.drawBitmap(tempBitmap, 0, 0, null);
    }

    public void setInnerCircleRadiusProgress(float innerCircleRadiusProgress) {
        this.innerCircleRadiusProgress = innerCircleRadiusProgress;
        postInvalidate();
    }

    public float getInnerCircleRadiusProgress() {
        return innerCircleRadiusProgress;
    }

    public void setOuterCircleRadiusProgress(float outerCircleRadiusProgress) {
        this.outerCircleRadiusProgress = outerCircleRadiusProgress;
        updateCircleColor();
        postInvalidate();
    }

    private void updateCircleColor() {
        float colorProgress = (float) Utils.clamp(outerCircleRadiusProgress, 0.5, 1);
        colorProgress = (float) Utils.mapValueFromRangeToRange(colorProgress, 0.5f, 1f, 0f, 1f);
        this.circlePaint.setColor((Integer) argbEvaluator.evaluate(colorProgress, START_COLOR, END_COLOR));
    }

    public float getOuterCircleRadiusProgress() {
        return outerCircleRadiusProgress;
    }

    public static final Property<fav_circle, Float> INNER_CIRCLE_RADIUS_PROGRESS =
            new Property<fav_circle, Float>(Float.class, "innerCircleRadiusProgress") {
                @Override
                public Float get(fav_circle object) {
                    return object.getInnerCircleRadiusProgress();
                }

                @Override
                public void set(fav_circle object, Float value) {
                    object.setInnerCircleRadiusProgress(value);
                }
            };

    public static final Property<fav_circle, Float> OUTER_CIRCLE_RADIUS_PROGRESS =
            new Property<fav_circle, Float>(Float.class, "outerCircleRadiusProgress") {
                @Override
                public Float get(fav_circle object) {
                    return object.getOuterCircleRadiusProgress();
                }

                @Override
                public void set(fav_circle object, Float value) {
                    object.setOuterCircleRadiusProgress(value);
                }
            };
}


//    private static final int START_COLOR = 0xFFFF5722;
//    private static final int END_COLOR = 0xFFFFC107;
//
//
//    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
//
//
//    private Paint circlePaint = new Paint();
//    private Paint maskPaint = new Paint();
//
//
//    private Bitmap bitmap;
//    private Canvas tempcanvas;
//
//
//    private float outerCircleRadius = 0f;
//    private float InnerCircleRadius = 0f;
//
//
//    private int maxCircleSize;
//
//    public fav_circle(Context context) {
//        super(context);
//        init();
//    }
//
//
//    public fav_circle(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public fav_circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    public fav_circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }
//
//    private void init() {
//        circlePaint.setStyle(Paint.Style.FILL);
//        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        maxCircleSize = w / 2;
//        bitmap = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);
//        tempcanvas  = new Canvas(bitmap);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        tempcanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR);
//        tempcanvas.drawCircle(getWidth() / 2, getHeight() / 2, outerCircleRadius * maxCircleSize, circlePaint);
//        tempcanvas.drawCircle(getWidth() / 2, getHeight() / 2, InnerCircleRadius * maxCircleSize, maskPaint);
//        canvas.drawBitmap(bitmap, 0, 0, null);
//    }
//
//    public void setInnerCircleRadiusProgress(float innerCircleRadiusProgress) {
//        this.InnerCircleRadius = innerCircleRadiusProgress;
//        postInvalidate();
//    }
//
//    public float getInnerCircleRadiusProgress() {
//        return InnerCircleRadius;
//    }
//
//    public void setOuterCircleRadiusProgress(float outerCircleRadiusProgress) {
//        this.outerCircleRadius = outerCircleRadiusProgress;
//        updateCircleColor();
//        postInvalidate();
//    }
//
//    private void updateCircleColor() {
//        float colorProgress = (float) Utils.clamp(outerCircleRadius, 0.5, 1);
//        colorProgress = (float) Utils.mapValueFromRangeToRange(colorProgress, 0.5f, 1f, 0f, 1f);
//        this.circlePaint.setColor((Integer) argbEvaluator.evaluate(colorProgress, START_COLOR, END_COLOR));
//    }
//
//    public float getOuterCircleRadiusProgress() {
//        return outerCircleRadius;
//    }
//
//    public static final Property<fav_circle, Float> INNER_CIRCLE_RADIUS_PROGRESS =
//            new Property<fav_circle, Float>(Float.class, "innerCircleRadiusProgress") {
//                @Override
//                public Float get(fav_circle object) {
//                    return object.getInnerCircleRadiusProgress();
//                }
//
//                @Override
//                public void set(fav_circle object, Float value) {
//                    object.setInnerCircleRadiusProgress(value);
//                }
//            };
//
//    public static final Property<fav_circle, Float> OUTER_CIRCLE_RADIUS_PROGRESS =
//            new Property<fav_circle, Float>(Float.class, "outerCircleRadiusProgress") {
//                @Override
//                public Float get(fav_circle object) {
//                    return object.getOuterCircleRadiusProgress();
//                }
//
//                @Override
//                public void set(fav_circle object, Float value) {
//                    object.setOuterCircleRadiusProgress(value);
//                }
//            };
//}