package bt.aps.spectrasnapp;
import android.widget.ListView;
import android.widget.AbsListView;
import android.content.Context;

public class HalfCircleListView extends ListView implements AbsListView.OnScrollListener {
    public HalfCircleListView(Context context) {
        super(context);
        setOnScrollListener(this);
        setDivider(null);
        setDividerHeight(0);
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        //Ignored
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        absListView.invalidateViews();
    }
}
