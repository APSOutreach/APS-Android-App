package bt.aps.spectrasnapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class Help extends Activity{

	private String info = "\nFor questions about SpectraSnapp, please contact :" + "\n\n" +
	"James Roche\n American Physical Society";
	private Spanned link = Html.fromHtml("<a href=mailto:roche@aps.org>mailto:roche@aps.org</a>");
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.help_page);
		//Set Help page html text and link to email
		TextView tv = (TextView) findViewById(R.id.header2);
		tv.setText("Help");
		TextView linkView = (TextView) findViewById(R.id.mail);
		linkView.setText(link);
		linkView.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView misc = (TextView) findViewById(R.id.misc2);
		misc.setText(info);
		misc.setGravity(Gravity.CENTER);
	}
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
			startActivity(new Intent(context, InflatedCompareActivity.class), options.toBundle());
			
		}
	}
}
