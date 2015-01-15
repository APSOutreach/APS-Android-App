package bt.aps.spectrasnapp;

import org.json.JSONObject;

import bt.aps.spectrasnapp.R;
import bt.aps.spectrasnapp.R.drawable;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Globals {
	public static int counter = 0;
	public static String[] names = {"Learn", "Build", "Snapp", "Calibrate", "Analyze", "Go Further", "Physics \n Central", "Help"};
	public static int[] drawables = {R.drawable.icon_snapp_learn, R.drawable.icon_snapp_build, R.drawable.icon_snapp_sample, R.drawable.icon_snapp_calibration,
		R.drawable.icon_snapp_analyze, R.drawable.icon_snapp_share, R.drawable.icon_snapp_physicscentral, R.drawable.icon_snapp_helpdesk};
	public static int[] drawables_red = {R.drawable.icon_snapp_learn_red, R.drawable.icon_snapp_build_red, R.drawable.icon_snapp_sample_red, R.drawable.icon_snapp_calibration_red,
		R.drawable.icon_snapp_analyze_red, R.drawable.icon_snapp_share_red, R.drawable.icon_snapp_physicscentral_red, R.drawable.icon_snapp_helpdesk_red};
	public static Drawable[] fixed = new Drawable[9];
	public static Drawable[] fixed_red	= new Drawable[9];
	public static int main_counter = 0;
	public static int[] drawables_spectrum = {R.drawable.aluminum, R.drawable.argon, R.drawable.barium, R.drawable.blacklight, R.drawable.calcium, R.drawable.carbon, R.drawable.compact_fluorescent_white,
			R.drawable.compact_fluorescent_yellow, R.drawable.computer_monitor, R.drawable.helium, R.drawable.hydrogen, R.drawable.iron, R.drawable.krypton, R.drawable.lithium, R.drawable.magnesium, R.drawable.mercury, R.drawable.neon, R.drawable.nitrogen, R.drawable.overhead_fluorescent, 
			R.drawable.oxygen, R.drawable.potassium, R.drawable.radon, R.drawable.silicon, R.drawable.sodium, R.drawable.strontium, R.drawable.sulfur, R.drawable.xenon, R.drawable.rainbow};
	
	public static boolean isCompareOpen = false;
	

	public static JSONObject json;
	

	public static Uri path;
	public static String pathString;
	public static int memory;
}
