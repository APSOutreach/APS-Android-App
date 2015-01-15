package bt.aps.spectrasnapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

import com.aviary.android.feather.FeatherGlobal;
import com.aviary.android.feather.async_tasks.DownloadImageAsyncTask;
import com.aviary.android.feather.library.Constants;

import bt.aps.spectrasnapp.InflatedCompareActivity.BitmapWorkerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Snapp extends Activity {
	private static int RESULT_LOAD_IMAGE = 20;
	private static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 30;
	private static Context context;
	private ImageView current;
	long avail;
	int viewH;
	int viewW;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.snapp_page);
		context = this;
		current = (ImageView) findViewById(R.id.current);
		if (Globals.memory < 1 && Globals.path != null) {
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("Memory Warning!");
			adb.setMessage("Your phone does not have enough memory to display your image in this window. You can still compare your spectrum to others in the \"Analyze\" window, or take a new picture if you need to.");
			adb.setCancelable(false);
			adb.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			AlertDialog ad = adb.create();
			ad.show();
		} else {
			
			
			if (Globals.path != null) {
				avail = calcAvailableMemory();
				viewH = current.getHeight();
				viewW = current.getWidth();
				
				try {
					
					newLoadBitmap t = new newLoadBitmap(current);
					t.execute(0);
					//current.setImageBitmap(decodeSampledBitmapFromFile(getRealPathFromURI(Globals.path), 500, 250));
					//bmp = scaleBitmap(Globals.path, avail);
					//current.setImageBitmap(bmp);
					//current.setScaleType(ImageView.ScaleType.FIT_XY);
				//} catch (FileNotFoundException e) {
				//	e.printStackTrace();
				} catch (OutOfMemoryError oom) {
					Log.d("Memory", "Out of Memory");
					current.setImageBitmap(null);
					System.gc();
					AlertDialog.Builder adb = new AlertDialog.Builder(context);
					adb.setTitle("Memory Warning!");
					adb.setMessage("Your phone does not have enough memory to display your image in this window. You can still compare your spectrum to others in the \"Analyze\" window, or take a new picture if you need to.");
					adb.setCancelable(false);
					adb.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					AlertDialog ad = adb.create();
					ad.show();
				}
			}
			
			/*
			if (Globals.path != null) {
				loadBitmap(current);
			}
			*/
		}
		
		//The gallery opens when this button is pressed
		Button gallery = (Button) findViewById(R.id.gallery);
		gallery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
		//The camera opens when this button is pressed
		Button camera = (Button) findViewById(R.id.takeImage);
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
					Toast.makeText(context, "No camera on device", Toast.LENGTH_LONG).show();
					((Button) v).setTextColor(Color.BLACK);
					((Button) v).setBackgroundColor(Color.WHITE);
				}
				else {
					Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(i2, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
					
				}
			}
			
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
		Drawable toRecycle = current.getDrawable();
		if (toRecycle != null ) {
			if (((BitmapDrawable) current.getDrawable()).getBitmap() != null) {
				((BitmapDrawable) current.getDrawable()).getBitmap().recycle();
			}
		}
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Save selected or taken image to GlobalImagePath.path
		if (requestCode == RESULT_LOAD_IMAGE || requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
				Globals.path = data.getData();
				FeatherGlobal.path2 = data.getData();
				Globals.pathString = data.getDataString();
				Drawable toRecycle = current.getDrawable();
				if (toRecycle != null ) {
					if (((BitmapDrawable) current.getDrawable()).getBitmap() != null) {
						((BitmapDrawable) current.getDrawable()).getBitmap().recycle();
					}
				}
				finish();
			} else if (resultCode == RESULT_CANCELED) {
				if (requestCode == RESULT_LOAD_IMAGE) {
					Toast.makeText(this, "Image selection cancelled", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show();
				}
				Drawable toRecycle = current.getDrawable();
				if (toRecycle != null ) {
					if (((BitmapDrawable) current.getDrawable()).getBitmap() != null) {
						((BitmapDrawable) current.getDrawable()).getBitmap().recycle();
					}
				}
				finish();
			} else {
				Toast.makeText(this, "Unexpected Error Occurred", Toast.LENGTH_SHORT).show();
				Drawable toRecycle = current.getDrawable();
				if (toRecycle != null ) {
					if (((BitmapDrawable) current.getDrawable()).getBitmap() != null) {
						((BitmapDrawable) current.getDrawable()).getBitmap().recycle();
					}
				}
				finish();
			}
		}
	}
	
	//Run InflatedCompareActivity when the phone screen is switched to landscape mode
		
		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			
			if (Globals.isCompareOpen == false && newConfig.orientation == 2) {
				ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.right_to_center, R.anim.center_to_left);
				Globals.isCompareOpen = true;
				FeatherGlobal.isCompareOpen2 = true;
				startActivity(new Intent(context, InflatedCompareActivity.class), options.toBundle());
				
			}
		}
	
	class newLoadBitmap extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<ImageView> ivRef;
		private int data;
		public newLoadBitmap(ImageView iv) {
			ivRef = new WeakReference<ImageView>(iv);
		}
		
		@Override
		protected Bitmap doInBackground(Integer... arg0) {
			data = arg0[0];
			return decodeSampledBitmapFromFile(getRealPathFromURI(Globals.path), 500, 250);
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (ivRef != null && bitmap != null) {
				final ImageView imageView = ivRef.get();
				if (imageView != null) {
					int h = imageView.getHeight();
					int w = imageView.getWidth();
					imageView.setImageBitmap(bitmap);
				}
			}
		}
		
	}
	
	private long calcAvailableMemory() {
		long value = Runtime.getRuntime().maxMemory();
		String type = "";
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			value = (value / 1024) - (Runtime.getRuntime().totalMemory() / 1024);
			type = "JAVA";
		} else {
			value = (value / 1024) - (Debug.getNativeHeapAllocatedSize() / 1024);
			type = "NATIVE";
		}
		Log.i("Memory", "calcAvailableMemory, size = " + value + ", type = " + type);
		return value;
	}
	
	private String getRealPathFromURI(Uri contentUri) {
		int columnIndex = 0;
		String res = null;
		String[] proj = {MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		if (cursor.moveToFirst()) {
			columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(columnIndex);
		}
		cursor.close();
		return res;
	}
	
	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		int inSampleSize = 1;
		
		if (height > reqHeight) {
			inSampleSize = Math.round((float)width / (float) reqWidth);
		}
		
		int expectedWidth = width / inSampleSize;
		
		if (expectedWidth > reqWidth) {
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}
		
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Matrix matrix = new Matrix();
		try {
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			Log.d("EXIF", "Exif" + orientation);
			if (orientation == 6) {
				matrix.postRotate(90);
			} else if (orientation == 3) {
				matrix.postRotate(180);
			} else if (orientation == 8) {
				matrix.postRotate(270);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap temp = BitmapFactory.decodeFile(path, options);
		
		return Bitmap.createBitmap(temp,0,0,temp.getWidth(), temp.getHeight(), matrix, true );
	}
	
	private Bitmap scaleBitmap(Uri path, long maxImageMemory) throws FileNotFoundException, OutOfMemoryError {
		//File f = new File(path.getPath());
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		String realPath = getRealPathFromURI(path);
		BitmapFactory.decodeFile(realPath, options);
		//BitmapFactory.decodeStream(context.getContentResolver().openInputStream(("content://media"+f.getAbsolutePath())), null, options);
		//BitmapFactory.decodeStream(context.getContentResolver().openInputStream(path));
		
		int sample = 1;
		if (options.outHeight > viewH || options.outWidth > viewW) {
			int heightRatio = Math.round((float) options.outHeight / (float) viewH);
			int widthRatio = Math.round((float) options.outWidth / (float) viewW);
			sample = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		
		while (calcBitmapSize(options.outWidth, options.outHeight, sample) > maxImageMemory) {
			sample++;
		}
		
		
		options.inSampleSize = sample;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = false;
		Log.i("Scale", "scaleBitmap, scaleSample = " + sample);
		
		Toast.makeText(context, realPath, Toast.LENGTH_LONG).show();
		return BitmapFactory.decodeFile(realPath,options);
		//return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(path));
		
	}
	
	private long calcBitmapSize(int width, int height, int sample) {
		long value = ((width/sample) * (height/sample) * 4) / 1024;
		Log.i("Bitmap Size", "calcBitmapSize, size = " + value);
		return value;
	}
	

}
