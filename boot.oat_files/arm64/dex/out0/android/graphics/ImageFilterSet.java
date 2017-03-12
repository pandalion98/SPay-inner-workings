package android.graphics;

import java.util.ArrayList;

public class ImageFilterSet extends ImageFilter {
    private ArrayList<ImageFilter> mImageFilters = new ArrayList();

    public ImageFilter clone() {
        ImageFilterSet imageFilterSet = new ImageFilterSet();
        for (int i = 0; i < this.mImageFilters.size(); i++) {
            imageFilterSet.addFilter(((ImageFilter) this.mImageFilters.get(i)).clone());
        }
        return imageFilterSet;
    }

    public void addFilter(ImageFilter filter) {
        if (filter != null) {
            this.mImageFilters.add(filter);
        }
    }

    public void clearFilters() {
        this.mImageFilters.clear();
    }

    public int getFilterCount() {
        return this.mImageFilters.size();
    }

    public ImageFilter getFilterAt(int index) {
        if (index < this.mImageFilters.size()) {
            return (ImageFilter) this.mImageFilters.get(index);
        }
        return null;
    }
}
