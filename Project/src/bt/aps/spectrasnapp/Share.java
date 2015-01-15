package bt.aps.spectrasnapp;


import com.aviary.android.feather.FeatherGlobal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

public class Share extends Activity{

	private String information = "Share your spectra! To take a screenshot: go back to the analyze screen, line up"
			+ " your spectrum, then press the power and home buttons simultaneously. Your picture should"
			+ " be saved to the iphone gallery where you can share on facebook or twitter. Make sure to add"
			+ " #SpectraSnapp and follow @PhysicsCentral!";
	
	private ImageView facebook, twitter, rss, google, youtube;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web_page);
		context = this;
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		TextView tv = (TextView) findViewById(R.id.header);
		tv.setText("Go Further");
		
		WebView wv = (WebView) findViewById(R.id.page);
		wv.loadUrl("file:///android_asset/share.html");
		/*
		//Each button goes to a different APS social media site
		TextView header = (TextView) findViewById(R.id.follow);
		header.setText("Follow PhysicsCentral");
		TextView info = (TextView) findViewById(R.id.info);
		info.setText(information);
		
		facebook = (ImageView) findViewById(R.id.facebookid);
		facebook.setOnClickListener(new OnClickListener() {
			String url = "https://www.facebook.com/PhysCentral";
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		twitter = (ImageView) findViewById(R.id.twitterid);
		twitter.setOnClickListener(new OnClickListener() {
			String url = "https://twitter.com/PhysicsCentral";
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		
		youtube = (ImageView) findViewById(R.id.youtubeid);
		youtube.setOnClickListener(new OnClickListener() {
			String url = "http://www.youtube.com/user/PhysicsCentralAPS";
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		
		rss = (ImageView) findViewById(R.id.rssid);
		rss.setOnClickListener(new OnClickListener() {
			String url = "http://www.physicscentral.com/about/feed/";
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		*/
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
