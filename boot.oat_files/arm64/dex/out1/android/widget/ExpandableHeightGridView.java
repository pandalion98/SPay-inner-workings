package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;

public class ExpandableHeightGridView extends GridView {
    private boolean expanded = false;
    private int mItemHeight = 0;
    private int mLineNum;

    public ExpandableHeightGridView(Context context) {
        super(context);
    }

    public ExpandableHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableHeightGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isExpanded()) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(16777215, Integer.MIN_VALUE));
            LayoutParams params = getLayoutParams();
            if (this.mItemHeight > 0) {
                this.mLineNum = this.mLineNum == 0 ? 1 : this.mLineNum;
                params.height = this.mLineNum * this.mItemHeight;
                return;
            }
            params.height = getMeasuredHeight();
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void setItemHeight(int height) {
        this.mItemHeight = height;
    }
}
