package bt.aps.spectrasnapp;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.FeatherGlobal;
import com.aviary.android.feather.library.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

public class Calibrate extends Activity {
	
	private static int AVIARY_CODE = 2;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		//Load Intent for the Aviary editor
		Intent newIntent = new Intent(this, FeatherActivity.class);
		//String[] toolList = {"CROP", "CONTRAST", "BRIGHTNESS", "SATURATION"};
        newIntent.setData(Globals.path);
        newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET, "3627a0646020ebbe");
        //newIntent.putExtra(Constants.EXTRA_TOOLS_LIST, toolList);
        startActivityForResult(newIntent, AVIARY_CODE);
	}
	
	//Save new image to GlobalImagePath.path
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AVIARY_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
				Globals.path = data.getData();
				FeatherGlobal.path2 = data.getData();
				finish();
			}
			else if (resultCode == RESULT_CANCELED) {
				//Toast.makeText(this, "Image calibration cancelled", Toast.LENGTH_SHORT).show();
				finish();
			}
			else {
				Toast.makeText(this, "Unexpected Error Occurred", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
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
