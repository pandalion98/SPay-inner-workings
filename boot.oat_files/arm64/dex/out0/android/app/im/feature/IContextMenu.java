package android.app.im.feature;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;

public interface IContextMenu extends IInjection {
    public static final int ON_CREATE = 1;
    public static final int ON_SELECTED = 2;

    boolean onContextItemSelected(MenuItem menuItem);

    void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo);
}
