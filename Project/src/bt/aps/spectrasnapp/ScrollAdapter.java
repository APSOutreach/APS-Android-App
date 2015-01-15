package bt.aps.spectrasnapp;
import java.util.List;


import android.widget.BaseAdapter;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import android.graphics.Color;

public class ScrollAdapter extends BaseAdapter {

    private List<String> scrollViews;
    private IntentString is;

    public ScrollAdapter(List<String> scrollViews, Context context){
        this.scrollViews = scrollViews;
        is = new IntentString(context);
    }
    @Override
    public int getCount() {
        return scrollViews.size();
    }

    @Override
    public String getItem(int i) {
        return scrollViews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    
	@Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = new OptionView(viewGroup.getContext());
        }
        OptionView currentView = (OptionView) view;
        String itemViewType = getItem(i);
        //Set a few settings for the current menu option
        ((OptionView) view).setText(itemViewType);
        ((OptionView) view).setTextColor(Color.WHITE);
        ((OptionView) view).setTextSize(30);
        ((OptionView) view).setGravity(Gravity.CENTER_VERTICAL);
        ((OptionView) view).setPadding(0, 5, 0, 5);
        ((OptionView) view).setClickable(true);
        ((OptionView) view).setOnClickListener(new menuClickListener(itemViewType, is));
        //Figure out which option is currently being changed.
        int a = 0;
        while (itemViewType != Globals.names[a]) {
        	a++;
        }
        //Set image to go with text
        ((OptionView) view).setCompoundDrawablesWithIntrinsicBounds(Globals.fixed[a], null, null, null);
        ((OptionView) view).setCompoundDrawablePadding(20);
        a = 0;
        return currentView;
    }
}
