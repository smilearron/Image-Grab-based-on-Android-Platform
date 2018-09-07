package edu.cwru.sail.imagelearning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * Created by huiyicao on 2017/9/13.
 */

public class CanvasCircle extends View {
    public int width;
    public int height;
    public static boolean sensorFlag=false;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float mposX,mposY;
    private float dx,dy;
    private static final float TOLERANCE =5;
    Context context;
    public static float[] scrX=new float[1024*1024];
    public static float[] scrY=new float[1024*1024];
    public static float[] vxArray =new float[1024*1024];
    public static float[] vyArray=new float[1024*1024];
    public static float[] preNumArray=new float[1024*1024];
    public static float[] sizeNumArray=new float[1024*1024];

    public static String[] timeStampArray=new String[1024*1024];

    public static float[] acXArray=new float[1024*1024];
    public static float[] acYArray=new float[1024*1024];
    public static float[] acZArray=new float[1024*1024];
    public static float[] mgXArray=new float[1024*1024];
    public static float[] mgYArray=new float[1024*1024];
    public static float[] mgZArray=new float[1024*1024];
    public static float[] gsXArray=new float[1024*1024];
    public static float[] gsYArray=new float[1024*1024];
    public static float[] gsZArray=new float[1024*1024];
    public static float[] rvXArray=new float[1024*1024];
    public static float[] rvYArray=new float[1024*1024];
    public static float[] rvZArray=new float[1024*1024];
    public static float[] laXArray=new float[1024*1024];
    public static float[] laYArray=new float[1024*1024];
    public static float[] laZArray=new float[1024*1024];
    public static float[] gvXArray=new float[1024*1024];
    public static float[] gvYArray=new float[1024*1024];
    public static float[] gvZArray=new float[1024*1024];



    public static int i=0;
    public int myPoint=0;
    public int mMaxVelocity;
    private VelocityTracker mVelocityTracker;


    public CanvasCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }

    private void onStartTouch(float x, float y){
        mPath.moveTo(x,y);
        mposX = x;
        mposY = y;
    }

    private void moveTouch(float x,float y){
        dx=Math.abs(x-mposX);
        dy=Math.abs(y-mposY);
        if(dx >= TOLERANCE || dy <= TOLERANCE){
            mPath.quadTo(mposX,mposY,(x+mposX)/2,(y+mposY)/2);
            mposX=x;
            mposY=y;
        }
    }

    public void clearCanvas(){
        mPath.reset();
        invalidate();
    }

    private void upTouch(){
        mPath.lineTo(mposX,mposY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sensorFlag=true;

        mMaxVelocity = Integer.MAX_VALUE;
        float x = event.getX();
        float y = event.getY();
        scrX[i%(1024*1024)]=x;
        scrY[i%(1024*1024)]=y;

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();//获得VelocityTracker类实例
        }
        mVelocityTracker.addMovement(event);
        final VelocityTracker vt = mVelocityTracker;

        vt.computeCurrentVelocity(1000);
        float vx = vt.getXVelocity();
        float vy = vt.getYVelocity();
        vxArray[i%(1024*1024)]=vx;
        vyArray[i%(1024*1024)]=vy;
        Log.println(Log.DEBUG,"vx",vx+" "+vy);


        float preNum = event.getPressure();
        float sizeNum = event.getSize();
        Log.println(Log.DEBUG,"pressure",preNum+" "+sizeNum);
        preNumArray[i%(1024*1024)]=preNum;
        sizeNumArray[i%(1024*1024)]=sizeNum;


        acXArray[i%(1024*1024)]=MainActivity.ac_x;
        acYArray[i%(1024*1024)]=MainActivity.ac_y;
        acZArray[i%(1024*1024)]=MainActivity.ac_z;

        mgXArray[i%(1024*1024)]=MainActivity.mg_x;
        mgYArray[i%(1024*1024)]=MainActivity.mg_y;
        mgZArray[i%(1024*1024)]=MainActivity.mg_z;

        gsXArray[i%(1024*1024)]=MainActivity.gs_x;
        gsYArray[i%(1024*1024)]=MainActivity.gs_y;
        gsZArray[i%(1024*1024)]=MainActivity.gs_z;

        rvXArray[i%(1024*1024)]=MainActivity.rv_x;
        rvYArray[i%(1024*1024)]=MainActivity.rv_y;
        rvZArray[i%(1024*1024)]=MainActivity.rv_z;

        laXArray[i%(1024*1024)]=MainActivity.la_x;
        laYArray[i%(1024*1024)]=MainActivity.la_y;
        laZArray[i%(1024*1024)]=MainActivity.la_z;

        gvXArray[i%(1024*1024)]=MainActivity.gv_x;
        gvYArray[i%(1024*1024)]=MainActivity.gv_y;
        gvZArray[i%(1024*1024)]=MainActivity.gv_z;

        timeStampArray[i%(1024*1024)]=CsvFiles.getCurrentTimeStamp();


        i++;
        Log.println(Log.DEBUG,"x",x+" "+y);
        if(MainActivity.enable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onStartTouch(x, y);
                    myPoint=event.getPointerId(0);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveTouch(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    upTouch();
                    invalidate();
                    break;
            }
        }
        return true;
    }
}



