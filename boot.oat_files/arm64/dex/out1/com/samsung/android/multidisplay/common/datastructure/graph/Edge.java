package com.samsung.android.multidisplay.common.datastructure.graph;

public class Edge<E> {
    public Vertex<E> endVertex;
    public boolean flag;
    public long id;
    public Vertex<E> startVertex;

    private Edge() {
    }

    Edge(long _id, Vertex<E> _startVertex, Vertex<E> _endVertex) {
        this.id = _id;
        this.startVertex = _startVertex;
        this.endVertex = _endVertex;
        this.flag = false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append(Edge.class.getSimpleName());
        sb.append(" {");
        sb.append(" e" + this.id);
        sb.append(" (v" + this.startVertex.id);
        sb.append(" => v" + this.endVertex.id);
        sb.append(") } ");
        return sb.toString();
    }
}
