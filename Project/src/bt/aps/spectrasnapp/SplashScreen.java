package bt.aps.spectrasnapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		//Splash screen that gets displayed before Main Activity
		Thread splash = new Thread() {
			@Override
			public void run() {
				try {
					super.run();
					sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		};
		splash.start();
	}
}
