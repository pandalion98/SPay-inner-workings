package android.app.im.feature;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public interface IOptionsMenu extends IInjection {
    public static final int ON_CREATE = 1;
    public static final int ON_PREPARE = 2;
    public static final int ON_SELECTED = 3;

    boolean onCreateOptionsMenu(Menu menu, MenuInflater menuInflater);

    boolean onOptionsItemSelected(MenuItem menuItem);

    boolean onPrepareOptionsMenu(Menu menu);
}
