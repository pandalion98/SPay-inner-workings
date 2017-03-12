package com.samsung.android.multidisplay.common.datastructure.graph;

import com.absolute.android.crypt.Crypt;

public class ItemWrapper<E> {
    boolean dummy;
    E item;

    private ItemWrapper() {
    }

    public ItemWrapper(E _itemRef) {
        if (_itemRef == null) {
            this.dummy = true;
            return;
        }
        this.dummy = false;
        this.item = _itemRef;
    }

    public E getWrappedItem() {
        return this.item;
    }

    public boolean isDummyWrappedItem() {
        return this.dummy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        if (this.dummy) {
            sb.append(Crypt.ATTR_StackMapTable);
        } else {
            sb.append(this.item);
        }
        return sb.toString();
    }

    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof ItemWrapper)) {
            return false;
        }
        ItemWrapper<E> itemWrapper = (ItemWrapper) otherObject;
        if (itemWrapper.getWrappedItem() == null || this.item == null) {
            if (itemWrapper.item != null || this.item != null) {
                return false;
            }
            if (itemWrapper != this) {
                return false;
            }
            return true;
        } else if (!this.item.getClass().equals(itemWrapper.item.getClass())) {
            throw new ClassCastException();
        } else if (this.item == itemWrapper.item) {
            return true;
        } else {
            return false;
        }
    }
}
