package com.samsung.android.sdk.look.airbutton;

import android.graphics.drawable.Drawable;
import android.view.View;
import java.util.ArrayList;

public class SlookAirButtonAdapter {
    private CharSequence mEmptyText;
    private ArrayList<AirButtonItem> mItemList = null;

    public static class AirButtonItem {
        private Object mData = null;
        private String mDescription = null;
        private Drawable mImage = null;
        private String mSubDescription = null;
        private View mView = null;

        public AirButtonItem(View view, Object data) {
            if (view == null && data == null) {
                throw new IllegalArgumentException("You should set the View and Object in Param");
            }
            set(view, data);
        }

        public AirButtonItem(Drawable image, String description, Object data) {
            if (image == null && description == null && data == null) {
                throw new IllegalArgumentException("You should set the Drawable, String and Object in Param");
            }
            set(image, description, data);
        }

        public AirButtonItem(Drawable image, String description, String subDescription, Object data) {
            if (image == null && description == null && subDescription == null && data == null) {
                throw new IllegalArgumentException("You should set the Drawable, String, subDescription and Object in Param");
            }
            set(image, description, subDescription, data);
        }

        public void set(View view, Object data) {
            this.mView = view;
            this.mData = data;
        }

        public void set(Drawable image, String description, Object data) {
            this.mImage = image;
            this.mDescription = description;
            this.mData = data;
        }

        public void set(Drawable image, String description, String subDescription, Object data) {
            this.mImage = image;
            this.mDescription = description;
            this.mSubDescription = subDescription;
            this.mData = data;
        }

        public View getView() {
            return this.mView;
        }

        public Drawable getImage() {
            return this.mImage;
        }

        public String getDescription() {
            return this.mDescription;
        }

        public String getSubDescription() {
            return this.mSubDescription;
        }

        public Object getData() {
            return this.mData;
        }
    }

    public SlookAirButtonAdapter(ArrayList<AirButtonItem> itemList) {
        addItem((ArrayList) itemList);
    }

    public void addItem(AirButtonItem item) {
        if (this.mItemList == null) {
            this.mItemList = new ArrayList();
        }
        this.mItemList.add(item);
    }

    public int addItem(ArrayList<AirButtonItem> itemList) {
        if (this.mItemList != null) {
            this.mItemList.clear();
        }
        this.mItemList = itemList;
        return this.mItemList.size() - 1;
    }

    public void insertItem(int index, AirButtonItem item) {
        if (this.mItemList != null) {
            this.mItemList.add(index, item);
        } else if (index == 0) {
            addItem(item);
        } else {
            throw new IllegalArgumentException("The list is empty.");
        }
    }

    public void removeItem(int index) {
        if (this.mItemList == null) {
            throw new IllegalArgumentException("The list is empty.");
        }
        this.mItemList.remove(index);
    }

    public void updateItem(int index, AirButtonItem item) {
        if (this.mItemList == null) {
            throw new IllegalArgumentException("The list is empty.");
        }
        this.mItemList.set(index, item);
    }

    public void clear() {
        if (this.mItemList != null) {
            this.mItemList.clear();
        }
    }

    public int getCount() {
        if (this.mItemList == null) {
            return 0;
        }
        return this.mItemList.size();
    }

    public AirButtonItem getItem(int idx) {
        if (this.mItemList == null || idx >= this.mItemList.size()) {
            return null;
        }
        return (AirButtonItem) this.mItemList.get(idx);
    }

    public void setEmptyText(CharSequence text) {
        this.mEmptyText = text;
    }

    public CharSequence getEmptyText() {
        return this.mEmptyText;
    }

    public boolean onHoverEnter(View parentView) {
        return true;
    }

    public void onHoverExit(View parentView) {
    }

    public void onShow(View parentView) {
    }

    public void onHide(View parentView) {
    }

    public void onDismiss(View parentView) {
    }
}
