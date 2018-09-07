package edu.cwru.sail.imagelearning;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static edu.cwru.sail.imagelearning.R.id.canvas;


public class MainActivity extends Activity implements SensorEventListener {

	private CanvasCircle canvasCircle;

//	private DrawCircle mAnimView = null;

	private ImageSwitcher sw;
	public LinearLayout LinearLayout;
	public RelativeLayout RelativeLayout;
	public static boolean enable = false;
	public Button button1;
	public Button button2;
	public Button button3;
	public Button button4;
	public Button button5;
	public Button button6;
	public Button btntdata;
	public Button btnsdata;
	public static float ac_x,ac_y,ac_z;
	public static float mg_x,mg_y,mg_z;
	public static float gs_x,gs_y,gs_z;
	public static float rv_x,rv_y,rv_z;
	public static float la_x,la_y,la_z;
	public static float gv_x,gv_y,gv_z;
	private int countNum = 0 ;

	public  SensorManager sensorManager;
	public ArrayList<String> myImgNames=new ArrayList<>();
	private int indexOfArrayList=-1;

	public int picture_index = 0;

	public Uri uri;

	private ImageView iv;
	private final String imgDir = Environment.getExternalStorageDirectory().toString();
	private File img = new File(imgDir + File.separator + "smile1.jpg");
	private File csvFile=new File(Environment.getExternalStorageDirectory() + "/" +  System.currentTimeMillis() + "test.csv" );
	private File csvFileOfSensor=new File(Environment.getExternalStorageDirectory() + "/" +  System.currentTimeMillis() + "SensorData.csv" );

	//Make sure that this part is dynamically defined by the Browse Folder and
	// your CSV file name is "THE_SAME_FOLDER_NAME.csv"



	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]+"test");
			//resume tasks needing this permission
		}
	}

	public boolean isStoragePermissionGranted() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
					== PackageManager.PERMISSION_GRANTED) {
				Log.v(TAG,"Permission is granted");
				return true;
			} }
		return false;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		canvasCircle = (CanvasCircle) findViewById(canvas);
		canvasCircle.bringToFront();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		iv = (ImageView) findViewById(R.id.imageView);

//button1:enable
		button1=(Button)findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Code here executes on main thread after user presses button
				button1.setEnabled(true);
				if(button1.isEnabled()){
					enable=true;
					button1.setEnabled(false);
					button2.setEnabled(true);
				}

			}
		});
//Button2:disenable
		button2=(Button)findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Code here executes on main thread after user presses button
				Log.println(Log.DEBUG,"x","disenable");
				enable=false;
				button1.setEnabled(true);
				button2.setEnabled(false);

			}
		});
//button3: clear
		button3=(Button)findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CanvasCircle.scrX=new float[1024*1024];
				CanvasCircle.scrY=new float[1024*1024];
				canvasCircle.clearCanvas();
			}
		});
//button4: save
		button4=(Button)findViewById(R.id.button4);
		button4.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				CsvFiles matrix=new CsvFiles(CanvasCircle.scrX,CanvasCircle.scrY);
				FileOutputStream outputStream= null;
				try {
					String fileCsvName = myImgNames.get(indexOfArrayList).substring(0,myImgNames.get(indexOfArrayList).length()-4);
					csvFile=new File(Environment.getExternalStorageDirectory() + "/"  + fileCsvName+".txt" );
					csvFile.createNewFile();
					outputStream = new FileOutputStream(csvFile,false);
					StringBuilder sb;
					sb = new StringBuilder();
					for (int i = 0; i <240; i++) {
						for(int j=0;j<320;j++){
							sb.append(matrix.matrix[i][j]).append(" ");
						}
						sb.append("\n");
					}
					outputStream.write(sb.toString().getBytes());
					outputStream.flush();
					outputStream.close();
					Toast.makeText(MainActivity.this, "Save the file successfully !",
							Toast.LENGTH_LONG).show();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
//button for sensor data
		btnsdata=(Button)findViewById(R.id.sensordata);
		btnsdata.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				CsvFiles sensorData=new CsvFiles(
						CanvasCircle.scrX,CanvasCircle.scrY,CanvasCircle.vxArray,CanvasCircle.vyArray,CanvasCircle.preNumArray,CanvasCircle.sizeNumArray,
						CanvasCircle.timeStampArray,
						CanvasCircle.acXArray,CanvasCircle.acYArray,CanvasCircle.acZArray,
						CanvasCircle.mgXArray,CanvasCircle.mgYArray,CanvasCircle.mgZArray,
						CanvasCircle.gsXArray,CanvasCircle.gsYArray,CanvasCircle.gsZArray,
						CanvasCircle.rvXArray,CanvasCircle.rvYArray,CanvasCircle.rvZArray,
						CanvasCircle.laXArray,CanvasCircle.laYArray,CanvasCircle.laZArray,
						CanvasCircle.gvXArray,CanvasCircle.gvYArray,CanvasCircle.gvZArray);
// write the sensor data file
				FileOutputStream outputStream= null;
				try {
					csvFileOfSensor=new File(Environment.getExternalStorageDirectory() + "/"  + "SensorData.csv" );
					csvFileOfSensor.createNewFile();
					outputStream=new FileOutputStream(csvFileOfSensor,true);
					StringBuilder sb;
					sb = new StringBuilder();

					if(countNum==0){

						sb.append("TimeStamp,")
								.append("TYPE ACCELEROMETER X,")
								.append("TYPE ACCELEROMETER Y,")
								.append("TYPE ACCELEROMETER Z,")
								.append("TYPE MAGNETIC FIELD X,")
								.append("TYPE MAGNETIC FIELD Y,")
								.append("TYPE MAGNETIC FIELD Z,")
								.append("TYPE GYROSCOPE X,")
								.append("TYPE GYROSCOPE Y,")
								.append("TYPE GYROSCOPE Z,")
								.append("TYPE ROTATION VECTOR X,")
								.append("TYPE ROTATION VECTOR Y,")
								.append("TYPE ROTATION VECTOR Z,")
								.append("TYPE LINEAR ACCELERATION X,")
								.append("TYPE LINEAR ACCELERATION Y,")
								.append("TYPE LINEAR ACCELERATION Z,")
								.append("TYPE GRAVITY X,")
								.append("TYPE GRAVITY Y,")
								.append("TYPE GRAVITY Z,")
								.append("position_X,")
								.append("position_Y,")
								.append("velocity_X,")
								.append("velocity_Y,")
								.append("pressure,")
								.append("size,")
								.append("picture");
						sb.append("\n");
					countNum++;
					}
					for (int i = 0; i <sensorData.getLength(); i++) {

						sb.append(sensorData.getTime(i)).append(",");
						sb.append(sensorData.getACX(i)).append(",");
						sb.append(sensorData.getACY(i)).append(",");
						sb.append(sensorData.getACZ(i)).append(",");
						sb.append(sensorData.getMGX(i)).append(",");
						sb.append(sensorData.getMGY(i)).append(",");
						sb.append(sensorData.getMGZ(i)).append(",");
						sb.append(sensorData.getGSX(i)).append(",");
						sb.append(sensorData.getGSY(i)).append(",");
						sb.append(sensorData.getGSZ(i)).append(",");
						sb.append(sensorData.getRVX(i)).append(",");
						sb.append(sensorData.getRVY(i)).append(",");
						sb.append(sensorData.getRVZ(i)).append(",");
						sb.append(sensorData.getLAX(i)).append(",");
						sb.append(sensorData.getLAY(i)).append(",");
						sb.append(sensorData.getLAZ(i)).append(",");
						sb.append(sensorData.getGVX(i)).append(",");
						sb.append(sensorData.getGVY(i)).append(",");
						sb.append(sensorData.getGVZ(i)).append(",");
						sb.append(sensorData.getX(i)).append(",");
						sb.append(sensorData.getY(i)).append(",");
						sb.append(sensorData.getVX(i)).append(",");
						sb.append(sensorData.getVY(i)).append(",");
						sb.append(sensorData.getPre(i)).append(",");
						sb.append(sensorData.getSize(i)).append(",");
						sb.append(myImgNames.get(indexOfArrayList));
						sb.append("\n");
//						Log.e("lax",sensorData.getLAX(i)+"");
						Log.e("imgaename",myImgNames.get(indexOfArrayList)+"");
					}
					outputStream.write(sb.toString().getBytes());
					outputStream.flush();
					outputStream.close();
					Toast.makeText(MainActivity.this, "Save the sensor data file successfully !",
							Toast.LENGTH_LONG).show();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
//button5:go to previous picture
		button5=(Button)findViewById(R.id.button5);
		button5.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				indexOfArrayList--;
				if(indexOfArrayList<0) {
					indexOfArrayList = 0;
					Toast.makeText(MainActivity.this, "Warning: No previous picture!", Toast.LENGTH_SHORT).show();
				}else{
					canvasCircle.clearCanvas();
					img =  new File(imgDir + File.separator + myImgNames.get(indexOfArrayList));

					Log.e("xxxFile",myImgNames.get(indexOfArrayList)+"");
					Picasso.with(MainActivity.this)
							.load(img)
							.resize(320*4, 240*4)                        // optional
							.into(iv);
				}
			}
		});
//button6:go to next picture
		button6=(Button)findViewById(R.id.button6);
		button6.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				indexOfArrayList++;
				if(indexOfArrayList>myImgNames.size()-1) {
					indexOfArrayList =myImgNames.size()-1;
					Toast.makeText(MainActivity.this, "Warning: No next picture!", Toast.LENGTH_SHORT).show();
				}else{
					canvasCircle.clearCanvas();
					img =  new File(imgDir + File.separator + myImgNames.get(indexOfArrayList));
					Picasso.with(MainActivity.this)
							.load(img)
							.resize(320*4, 240*4)                        // optional
							.into(iv);
				}

			}
		});
	}

//activate gallery
	public void gallery(View view) {
		// active gallery and select a new picture
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		// activat a new Activity，request_code is 2 = PHOTO_REQUEST_GALLERY
		startActivityForResult(intent,2);
	}
//make sure sdcard(saving path) is existed
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
//create a image file path for the photo the user may take
	private void createImageFile() {
		img = new File(Environment.getExternalStorageDirectory() + "/" +  System.currentTimeMillis() + ".jpg" );
		try {
			img.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("erro",e+"");
			Toast.makeText(this, "Warning: Error !", Toast.LENGTH_SHORT).show();
		}
	}
//activate camera
	public void camera(View view) {
		createImageFile();
		if (!img.exists()) {
			return;
		}
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// judge whether the storage is available or not
		if (hasSdcard()) {
			uri=Uri.fromFile(img);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			Log.e("cunuri",uri+"");
		}
		// the request_code is 1 = PHOTO_REQUEST_CAREMA
		startActivityForResult(intent, 1);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			//get the data return by the camera
			if (data != null) {
				// get the full uri of the picture
				uri = data.getData();
				// use the method created in getImgPath to get the absolute path of the file
				String filePath= null;
				String fileName=null;
				try {
					filePath = getImgPath.getPath(this,uri);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				//get the exact name of the image file as: 123456789.jpg or 123456789.png
				for(int i=filePath.length()-1;i>0;i--){
					if(filePath.charAt(i)=='/'){
						fileName=filePath.substring(i+1,filePath.length());
						Log.e("file name",fileName+"");
						break;
					}
				}
				//save the filename into the ArrayList of myImgNames
				myImgNames.add(fileName);
				indexOfArrayList=myImgNames.size()-1;
				//once select a new picture, we need to clear canvas
				canvasCircle.clearCanvas();
				//load the image
				Picasso.with(this)
						.load(uri)
						.resize(320*4, 240*4)
						.into(iv);
				//set the canvas to the same size as imageview
				RelativeLayout.LayoutParams lparam = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				lparam.height =240*4;
				lparam.width = 320*4;
				canvasCircle.setLayoutParams(lparam);
			}
		} else if (requestCode == 1) {
			// go to the cropping photo function
			Intent intent = new Intent("com.android.camera.action.CROP"); //crop
			intent.setDataAndType(uri, "image/*");
			//set aspect ratio
			intent.putExtra("aspectX", 4);
			intent.putExtra("aspectY", 3);
			//set the length and width of the image
			intent.putExtra("outputX", 320);
			intent.putExtra("outputY", 240);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			//refresh the gallery
			startActivityForResult(intent, 2);
			//pass the cropped image to imageview
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

// activate sensor manger
	public void onResume() {
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				sensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				sensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
				sensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
				sensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				sensorManager.SENSOR_DELAY_UI);
			super.onResume();
	}
// stop sensor manager
	public void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();

	}
//record sensor data
	public void onSensorChanged(SensorEvent event) {

		if(CanvasCircle.sensorFlag){

			switch (event.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
					ac_x = event.values[0];
					ac_y = event.values[1];
					ac_z = event.values[2];
				case Sensor.TYPE_MAGNETIC_FIELD:
					mg_x = event.values[0];
					mg_y = event.values[1];
					mg_z = event.values[2];
				case Sensor.TYPE_GYROSCOPE:
					gs_x = event.values[0];
					gs_y = event.values[1];
					gs_z = event.values[2];
				case Sensor.TYPE_ROTATION_VECTOR:
					rv_x = event.values[0];
					rv_y = event.values[1];
					rv_z = event.values[2];
				case Sensor.TYPE_LINEAR_ACCELERATION:
					la_x = event.values[0];
					la_y = event.values[1];
					la_z = event.values[2];
				case Sensor.TYPE_GRAVITY:
					gv_x = event.values[0];
					gv_y = event.values[1];
					gv_z = event.values[2];
				default:
					break;
			}
			CanvasCircle.sensorFlag=false;
		}
	}

	@Override
	public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {

			Log.println(Log.DEBUG,"x","aaa");
			return true;

		}

		return super.onOptionsItemSelected(item);
	}
}







