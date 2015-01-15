package bt.aps.spectrasnapp;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.content.res.Resources;

public class OptionView extends TextView {

    
    private static final float OFFSET = 550;

    public OptionView(Context context) {
        super(context);
    }

    
	public void onDraw(Canvas canvas){
        canvas.save();
        float indent = getIndent(getY());
        canvas.translate(indent, 0);
        super.onDraw(canvas);
        canvas.restore();
    }

    public float getIndent(float distance) {
    	//This math computes the indent for the menu option based on its location on the screen
    	//in order to create a semi circle with all items
    	DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    	float y_vertex = displayMetrics.heightPixels / 2 / displayMetrics.density;
    	float h = Math.abs(distance - y_vertex);
    	float sWidth = displayMetrics.widthPixels / displayMetrics.density;
    	float hypot = sWidth + OFFSET;
    	float x = (float) Math.sqrt(Math.pow(hypot, 2) - Math.pow(h,2));
    	float xFin = x - OFFSET;
    	return sWidth - xFin;
    	
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	//This thread will change the color of the text back to white once the activity switch occurs.
    	//This is a way to let the user know that the button has been clicked
    	if (!hasFocus) {
    		new Thread(new Thread() {

				@Override
				public void run() {
					super.run();
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					setTextColor(Color.WHITE);
				}
    			
    		}).run();
    		
    	}
    }
}
