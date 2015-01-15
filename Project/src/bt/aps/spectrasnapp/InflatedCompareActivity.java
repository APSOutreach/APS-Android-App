package bt.aps.spectrasnapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;

import com.aviary.android.feather.FeatherGlobal;

import bt.aps.spectrasnapp.R;
import bt.aps.spectrasnapp.R.id;
import bt.aps.spectrasnapp.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class InflatedCompareActivity extends Activity{

	
	private static Context context;
	private boolean notDone = true;
	private static ImageView iv1, iv2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inflate_compare__page);
		context = this;
		
		iv1 = (ImageView) findViewById(R.id.comp1);
		iv2 = (ImageView) findViewById(R.id.comp2);
		//Set ImageViews with current image and a spectrum
		if (Globals.path != null) {
			loadBitmap(iv1);
		}
		else {
			Toast.makeText(context, "Take a picture to see comparison", Toast.LENGTH_LONG).show();
		}
		
		iv2.setBackgroundResource(Globals.drawables_spectrum[Globals.counter]);
	
		//This thread checks to see if the orientation switches back to portrait, which
		//means this activity needs to close.
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (notDone == true) {
					if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
						Log.d("Ending Activity", "Compare Ending");
						notDone = false;
						Globals.isCompareOpen = false;
						Drawable toRecycle = iv1.getDrawable();
						if (toRecycle != null ) {
							if (((BitmapDrawable) iv1.getDrawable()).getBitmap() != null) {
								((BitmapDrawable) iv1.getDrawable()).getBitmap().recycle();
							}
						}
						
						finish();
					}
				}
			}
			
		}).start();
	}
	//Run the Async Task
	public void loadBitmap(ImageView iv) {
		BitmapWorkerTask task = new BitmapWorkerTask(iv);
		task.execute(1);
	}
	
	//Get bitmap from Uri
	public static Bitmap decodeSampledBitmapFromResource(Uri uri) {
		Bitmap bm = null;
		try {
			bm = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bm;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Globals.isCompareOpen = false;
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.d("Orientation", "SWITCHING TO LANDSCAPE"); }
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.d("Orientation", "SWITCHING TO PORTRAIT"); }
		//super.onBackPressed();
		
		this.finish();
	}
	
	//Set bitmap to adjusted size
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm,0,0,width,height,matrix,false);
		return resizedBitmap;
	}
	
	@Override
	public void onBackPressed() {
		
	}
	
	//Async Task to load and resize image from camera or gallery
	//Loading in this way prevents Out of Memory Errors
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private int data = 0;
		
		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(Integer... params) {
			data = params[0];
			//return decodeSampledBitmapFromResource(Globals.path);
			return decodeSampledBitmapFromFile(getRealPathFromURI(Globals.path), 500, 250);
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					int h = imageView.getHeight();
					int w = imageView.getWidth();
					//bitmap = getResizedBitmap(bitmap, h, w);
					imageView.setImageBitmap(bitmap);
				}
			}
		}
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
	
}
