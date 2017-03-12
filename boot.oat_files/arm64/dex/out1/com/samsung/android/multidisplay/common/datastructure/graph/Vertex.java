package com.samsung.android.multidisplay.common.datastructure.graph;

import java.util.ArrayList;

public class Vertex<E> {
    public boolean dummy;
    public long id;
    public ArrayList<Edge<E>> incidenceList = new ArrayList();
    private ItemWrapper<E> item;
    public boolean marked;

    private Vertex() {
    }

    public Vertex(long _id, ItemWrapper<E> _item) {
        this.id = _id;
        this.item = _item;
        this.marked = false;
        this.dummy = false;
    }

    public ItemWrapper<E> getElement() {
        return this.item;
    }

    public void setElement(ItemWrapper<E> _item) {
        this.item = _item;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append(Vertex.class.getSimpleName());
        sb.append(" {");
        sb.append("v" + String.format("%02d", new Object[]{Long.valueOf(this.id)}));
        sb.append(" item" + this.item);
        sb.append(" incidenceList=" + this.incidenceList);
        sb.append(" } ");
        return sb.toString();
    }
}
