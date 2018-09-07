package edu.cwru.sail.imagelearning;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huiyicao on 2017/9/17.
 */

public class CsvFiles {
    float xmin= Integer.MAX_VALUE,ymin= Integer.MAX_VALUE,xmax= Integer.MIN_VALUE,ymax=Integer.MIN_VALUE;
    private float [] xArray=new float[1024*1024];
    private float [] yArray=new float[1024*1024];
    private float [] vxArray=new float[1024*1024];
    private float [] vyArray=new float[1024*1024];
    private float [] preArray=new float[1024*1024];
    private float [] sizeArray=new float[1024*1024];

    private String[] timeStampArray=new String[1024*1024];

    private float[] acXArray=new float[1024*1024];
    private float[] acYArray=new float[1024*1024];
    private float[] acZArray=new float[1024*1024];
    private float[] mgXArray=new float[1024*1024];
    private float[] mgYArray=new float[1024*1024];
    private float[] mgZArray=new float[1024*1024];
    private float[] gsXArray=new float[1024*1024];
    private float[] gsYArray=new float[1024*1024];
    private float[] gsZArray=new float[1024*1024];
    private float[] rvXArray=new float[1024*1024];
    private float[] rvYArray=new float[1024*1024];
    private float[] rvZArray=new float[1024*1024];
    private float[] laXArray=new float[1024*1024];
    private float[] laYArray=new float[1024*1024];
    private float[] laZArray=new float[1024*1024];
    private float[] gvXArray=new float[1024*1024];
    private float[] gvYArray=new float[1024*1024];
    private float[] gvZArray=new float[1024*1024];


    private int arraySize=0;
    final static float XBORDER_MAX=240*4;
    final static float YBORDER_MAX=320*4;
    final static float XBORDER_MIN=0;
    final static float YBORDER_MIN=0;

    public int [][]matrix=new int[240][320];

    int i,j;

    public CsvFiles(float x[],float y[]){
        for(i=0;i<x.length;i++){
            if(x[i]==0 && y[i]==0)
                continue;
            if(x[i]<XBORDER_MIN) x[i]=XBORDER_MIN;
            if(x[i]>XBORDER_MAX) x[i]=XBORDER_MAX;
            if(y[i]<YBORDER_MIN) y[i]=YBORDER_MIN;
            if(y[i]>YBORDER_MAX) y[i]=YBORDER_MAX;
            if(x[i]<xmin) xmin=x[i];
            if(x[i]>xmax) xmax=x[i];
            if(y[i]<ymin) ymin=y[i];
            if(y[i]>ymax) ymax=y[i];
        }
        xmin/=4;xmax/=4;ymin/=4;ymax/=4;
        for(i=0;i<matrix.length;i++){
            for(j=0;j<matrix[0].length;j++){
                if(i>=(int)ymin && i<=(int)ymax && j>=(int)xmin && j<=(int)xmax){
                    matrix[i][j]=0;
                }
                else{
                    matrix[i][j]=1;
                }
            }
        }
    }
    public CsvFiles(float x[],float y[],float vx[],float vy[],float pre[],float size[]){
        for(int i=0;i<x.length && x[i]!=0 && y[i]!=0;i++){
            xArray[i]=x[i];
            yArray[i]=y[i];
            vxArray[i]=vx[i];
            vyArray[i]=vy[i];
            preArray[i]=pre[i];
            sizeArray[i]=size[i];



            arraySize++;
        }
    }

    public CsvFiles(float x[],float y[],float vx[],float vy[],float pre[],float size[],String time[],float acx[],float acy[],float acz[],float mgx[],float mgy[],float mgz[],float gsx[],float gsy[],float gsz[],
                    float rvx[],float rvy[],float rvz[],float lax[],float lay[],float laz[],float gvx[],float gvy[],float gvz[]){
        for(int i=0;i<x.length && x[i]!=0 && y[i]!=0;i++) {

            xArray[i]=x[i];
            yArray[i]=y[i];
            vxArray[i]=vx[i];
            vyArray[i]=vy[i];
            preArray[i]=pre[i];
            sizeArray[i]=size[i];

            timeStampArray[i] = time[i];
            acXArray[i]=acx[i];
            acYArray[i]=acy[i];
            acZArray[i]=acz[i];
            mgXArray[i]=mgx[i];
            mgYArray[i]=mgy[i];
            mgZArray[i]=mgz[i];
            gsXArray[i]=gsx[i];
            gsYArray[i]=gsy[i];
            gsZArray[i]=gsz[i];
            rvXArray[i]=rvx[i];
            rvYArray[i]=rvy[i];
            rvZArray[i]=rvz[i];
            laXArray[i]=lax[i];
            laYArray[i]=lay[i];
            laZArray[i]=laz[i];
            gvXArray[i]=gvx[i];
            gvYArray[i]=gvy[i];
            gvZArray[i]=gvz[i];
            arraySize++;
        }
    }

    public float getX(int i){
        return xArray[i];
    }
    public float getY(int i){
        return yArray[i];
    }
    public float getVX(int i){
        return vxArray[i];
    }
    public float getVY(int i){
        return vyArray[i];
    }
    public float getPre(int i){
        return preArray[i];
    }
    public float getSize(int i){
        return sizeArray[i];
    }
    public int getLength(){
        return arraySize;
    }

    public String getTime(int i){
        return timeStampArray[i];
    }
    public float getACX(int i){
        return acXArray[i];
    }
    public float getACY(int i){
        return acYArray[i];
    }
    public float getACZ(int i){
        return acZArray[i];
    }
    public float getMGX(int i){
        return mgXArray[i];
    }
    public float getMGY(int i){
        return mgYArray[i];
    }
    public float getMGZ(int i){
        return mgZArray[i];
    }
    public float getGSX(int i){
        return gsXArray[i];
    }
    public float getGSY(int i){
        return gsYArray[i];
    }
    public float getGSZ(int i){
        return gsZArray[i];
    }
    public float getRVX(int i){
        return rvXArray[i];
    }
    public float getRVY(int i){
        return rvYArray[i];
    }
    public float getRVZ(int i){
        return rvZArray[i];
    }
    public float getLAX(int i){
        return laXArray[i];
    }
    public float getLAY(int i){
        return laYArray[i];
    }
    public float getLAZ(int i){
        return laZArray[i];
    }
    public float getGVX(int i){
        return gvXArray[i];
    }
    public float getGVY(int i){
        return gvYArray[i];
    }
    public float getGVZ(int i){
        return gvZArray[i];
    }


    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
