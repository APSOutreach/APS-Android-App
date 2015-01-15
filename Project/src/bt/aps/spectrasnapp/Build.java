package bt.aps.spectrasnapp;


import com.aviary.android.feather.FeatherGlobal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class Build extends Activity{
	
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web_page);
		TextView tv = (TextView) findViewById(R.id.header);
		tv.setText("Build");
		
		//Load html for build window
		WebView wv = (WebView) findViewById(R.id.page);
		wv.loadUrl("file:///android_asset/build.html");
		
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
	}
	
	
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

}
