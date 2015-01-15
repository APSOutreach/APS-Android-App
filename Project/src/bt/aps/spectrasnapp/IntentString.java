package bt.aps.spectrasnapp;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class IntentString {

	private ArrayList<String> names;
	private ArrayList<Intent> intents;
	private Context context;
	
	public IntentString(Context context) {
		this.context = context;
		//Set names to every option in main menu
		names = new ArrayList<String>();
		names.add("Learn"); names.add("Build");
		names.add("Snapp"); names.add("Calibrate");
		names.add("Analyze"); names.add("Go Further");
		names.add("Physics \n Central"); names.add("Help");
		//names.add("Test");
		
		//Create an intent to be linked to each option in main menu
		intents = new ArrayList<Intent>();
		intents.add(new Intent(context, Learn.class));
		intents.add(new Intent(context, Build.class));
		intents.add(new Intent(context, Snapp.class));
		intents.add(new Intent(context, Calibrate.class));
		intents.add(new Intent(context, Analyze.class));
		intents.add(new Intent(context, Share.class));
		String url = "http://physicscentral.com";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		intents.add(i);
		intents.add(new Intent(context, Help.class));
		//intents.add(new Intent(context, TempActivity.class));
	}
	
	//Run the intent corresponding to the option just selected
	
	public void runIntent(String name) {
		int loc = names.indexOf(name);
		ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.right_to_center, R.anim.center_to_left);
		//if Calibrate or Analyze are selected before a picture is taken, send an alert dialog
		if (Globals.path == null && name == "Calibrate") {
			new AlertDialog.Builder(context)
			.setTitle("Instrument Error")
			.setMessage("Need to sample a spectrum before calibration")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			})
			.show();
		} else if (Globals.path == null && name == "Analyze") {
			new AlertDialog.Builder(context)
			.setTitle("Instrument Error")
			.setMessage("Need to sample a spectrum before analysis")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			})
			.show();
		}
		else {
			context.startActivity(intents.get(loc), options.toBundle());
			
		}
	}
	
}

