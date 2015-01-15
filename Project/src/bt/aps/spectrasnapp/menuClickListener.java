package bt.aps.spectrasnapp;

import android.app.ActivityManager;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

public class menuClickListener implements OnClickListener{
	private String name;
	private IntentString is;
	
	public menuClickListener(String name, IntentString is) {
		this.name = name;
		this.is = is;
	}
	
	@Override
	public void onClick(View v) {
		is.runIntent(name);
		((OptionView) v).setTextColor(Color.RED);
	}

}
