package bt.aps.spectrasnapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bt.aps.spectrasnapp.InflatedCompareActivity.BitmapWorkerTask;

import com.aviary.android.feather.FeatherGlobal;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Analyze extends Activity {

	private String[] options = { "Aluminum", "Argon", "Barium", "Black Light", "Calcium", "Carbon", "Compact Fluorescent White", "Compact Fluorescent Yellow", "Computer Monitor", "Helium", "Hydrogen", "Iron", "Krypton", "Lithium", "Magnesium", "Mercury", "Neon", "Nitrogen", "Overhead Fluorescent", "Oxygen", "Potassium", "Radon", "Silicon", "Sodium", "Strontium", "Sulfur", "Xenon", "Rainbow"};
	private int[] drawables = {R.drawable.aluminum, R.drawable.argon, R.drawable.barium, R.drawable.blacklight, R.drawable.calcium, R.drawable.carbon, R.drawable.compact_fluorescent_white,
			R.drawable.compact_fluorescent_yellow, R.drawable.computer_monitor, R.drawable.helium, R.drawable.hydrogen, R.drawable.iron, R.drawable.krypton, R.drawable.lithium, R.drawable.magnesium, R.drawable.mercury, R.drawable.neon, R.drawable.nitrogen, R.drawable.overhead_fluorescent, 
			R.drawable.oxygen, R.drawable.potassium, R.drawable.radon, R.drawable.silicon, R.drawable.sodium, R.drawable.strontium, R.drawable.sulfur, R.drawable.xenon, R.drawable.rainbow};
	private String instructions = "For best results, crop and rotate your photo so the emission lines are aligned with"
			+ " the colors in the full spectrum. Go back to the calibrate screen to fine tune.";
	private int loc = 0;
	private int total;
	private JSONObject obj;
	private JSONArray elements;
	private static Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.analyze_page);
		context = this;
		final TextView title = (TextView) findViewById(R.id.spectra_name);
		title.setText("Aluminum");
		TextView tv = (TextView) findViewById(R.id.inst);
		tv.setText(instructions);
		
		ImageView iv = (ImageView) findViewById(R.id.compareImage);
		//iv.setImageURI(Globals.path);
		if (Globals.path != null) {
			loadBitmap(iv);
		}
		
		//Create a JSON Object for a file in assets to file analyze window content
		//Placed in a global variable to prevent loading multiple times
		if (Globals.json == null) {
			try {
				Globals.json = new JSONObject(loadJSON());
				obj = Globals.json;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else {
			obj = Globals.json;
		}
		
		//Get every element in the object
		try {
			elements = obj.getJSONArray("spectrum");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		total = elements.length();
		
		final TextView description = (TextView) findViewById(R.id.spectrum_description);
		//Set initial description
		try {
			description.setText(elements.getJSONObject(0).getString(options[loc]));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		final ImageView spectrum = (ImageView) findViewById(R.id.spectrum);
		
		//Code to run when left arrow is clicked
		ImageButton left = (ImageButton) findViewById(R.id.left);
		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loc--;
				if (loc < 0) {
					loc = options.length - 1;
				}
				Globals.counter = loc;
				FeatherGlobal.counter2 = loc;
				title.setText(options[loc]);
				//spectrum.setImageDrawable(getResources().getDrawable(drawables[loc]));
				spectrum.setImageDrawable(getResources().getDrawable(Globals.drawables_spectrum[loc]));
				try {
					description.setText(elements.getJSONObject(loc).getString(options[loc]));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		});
		//Code to run when right arrow is clicked
		ImageButton right = (ImageButton) findViewById(R.id.right);
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loc++;
				if (loc > options.length - 1) {
					loc = 0;
				}
				Globals.counter = loc;
				FeatherGlobal.counter2 = loc;
				title.setText(options[loc]);
				//spectrum.setImageDrawable(getResources().getDrawable(drawables[loc]));
				spectrum.setImageDrawable(getResources().getDrawable(Globals.drawables_spectrum[loc]));
				try {
					description.setText(elements.getJSONObject(loc).getString(options[loc]));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
	}
	
	//Read spectrums.json into a buffer and return a string 
	public String loadJSON() {
		String json = null;
		try {
			InputStream is = getAssets().open("spectrums.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}
	
	//Run InflatedCompareActivity if phone is in landscape mode
	
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
