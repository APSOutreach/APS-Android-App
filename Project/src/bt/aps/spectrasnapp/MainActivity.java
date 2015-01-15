package bt.aps.spectrasnapp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.aviary.android.feather.FeatherGlobal;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.os.Build;

public class MainActivity extends Activity {

	public static final int COMPARE_CODE = 300;
	private Context context;
	private String[] options = {"Learn", "Build", "Snapp", "Calibrate", "Analyze", "Go Further", "Physics \n Central", "Help"};
	private Dialog d;
	private static Drawable bg;
	private ListView listView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		
		context = this;
		LinearLayout ll = (LinearLayout) findViewById(R.id.circleList);
		//Resize icons
		fixDrawables();
		ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		Globals.memory = am.getMemoryClass();
		Log.d("Memory", "Max memory is : " + Globals.memory + " MB");
		//Toast.makeText(context, "App Memory : " + Globals.memory + " MB", Toast.LENGTH_LONG).show();
		//Set background
		String uri = "drawable/spectrasnapp_background";
		if (bg == null) {
			int imageResource = getResources().getIdentifier(uri, null, getPackageName());
			bg = getResources().getDrawable(imageResource);
			bg.setAlpha(150);
		}
		ll.setBackground(bg);
		
		//Create circle list with all choices
		listView = new HalfCircleListView(this);
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		listView.setLayoutParams(params);
		List<String> menuOptions = new ArrayList<String>();
		for (int i = 0; i < options.length; i++) {
			menuOptions.add(options[i]);
		}
		ScrollAdapter menuScrollAdapter = new ScrollAdapter(menuOptions,context);
		listView.setAdapter(menuScrollAdapter);
	    ll.addView(listView);
	    //menuScrollAdapter.notifyDataSetChanged();
	    
		
        
		
		/*
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		*/
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
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//return false;
			return true;
		}
		//return false;
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	//Changes the size of the list icons
	public void fixDrawables() {
		for (int i = 0; i < Globals.drawables.length; i++) {
			Drawable dr = context.getResources().getDrawable(Globals.drawables[i]);
	        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
	        Drawable df = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 150, 150, true));
	        Globals.fixed[i] = df;
		}
	}
	/*
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindDrawables(findViewById(R.id.container));
		System.gc();
	}
	private void unbindDrawables(View view) {
		if (view.getBackground()!=null) {
			view.getBackground().setCallback(null);
			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}
				((ViewGroup) view).removeAllViews();
			}
		}
	}
	 */
}
