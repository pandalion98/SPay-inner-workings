package com.samsung.android.multidisplay.common.datastructure.graph;

import android.util.Log;
import com.samsung.android.multidisplay.common.ContextRelationManager;
import com.samsung.android.multidisplay.common.datastructure.DepthFirstSearch;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class Graph<E> {
    private static final boolean DEBUG = ContextRelationManager.DEBUG;
    private static final String TAG = "Graph";
    private DepthFirstSearch<E> mDfs;
    private ArrayList<Edge<E>> mEdges = new ArrayList();
    String mGraphName;
    private Class<E> mGraphType;
    private long mNextEdgeId = 0;
    private long mNextVertexId = 1;
    private ArrayList<Vertex<E>> mVertices = new ArrayList();

    private Graph() {
    }

    public Graph(Class<E> cls, String name) {
        this.mGraphType = cls;
        this.mGraphName = name;
        this.mDfs = new DepthFirstSearch(this);
    }

    Class<E> getType() {
        return this.mGraphType;
    }

    public void clear() {
        Iterator i$ = vertices().iterator();
        while (i$.hasNext()) {
            Vertex<E> v = (Vertex) i$.next();
            v.incidenceList.clear();
            v.setElement(null);
        }
        i$ = edges().iterator();
        while (i$.hasNext()) {
            Edge<E> e = (Edge) i$.next();
            e.startVertex = null;
            e.endVertex = null;
        }
    }

    public String getGraphName() {
        return this.mGraphName;
    }

    public int numVertices() {
        return this.mVertices.size();
    }

    public int numEdges() {
        return this.mEdges.size();
    }

    public ArrayList<Vertex<E>> vertices() {
        return this.mVertices;
    }

    public ArrayList<Edge<E>> edges() {
        return this.mEdges;
    }

    public int degree(Vertex<E> v) {
        return incidentEdges(v).size();
    }

    public ArrayList<Vertex<E>> adjacentVertices(Vertex<E> v) {
        ArrayList<Vertex<E>> adjVertices = new ArrayList();
        Iterator i$ = incidentEdges(v).iterator();
        while (i$.hasNext()) {
            Edge<E> e = (Edge) i$.next();
            if (e.startVertex.getElement() == v.getElement()) {
                adjVertices.add(e.endVertex);
            } else {
                adjVertices.add(e.startVertex);
            }
        }
        return adjVertices;
    }

    public ArrayList<Edge<E>> incidentEdges(Vertex<E> v) {
        return v.incidenceList;
    }

    public Vertex<E> opposite(Vertex<E> v, Edge<E> e) {
        if (e.startVertex == v) {
            return e.endVertex;
        }
        return e.startVertex;
    }

    public Vertex<E> origin(Edge<E> e) {
        return e.startVertex;
    }

    public Vertex<E> destination(Edge<E> e) {
        return e.endVertex;
    }

    public boolean insertVertex(ItemWrapper<E> rootItem) {
        try {
            if (getType() == null) {
                throw new NullPointerException();
            } else if (this.mVertices.size() != 0 || rootItem == null || getType().getName().equals(rootItem.item.getClass().getName())) {
                return insertVertex(null, rootItem);
            } else {
                throw new ClassCastException();
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException();
        } catch (NullPointerException e2) {
            e2.printStackTrace();
            throw new NullPointerException();
        }
    }

    public boolean insertVertex(ItemWrapper<E> parentItem, ItemWrapper<E> childItem) {
        if (DEBUG) {
            Log.d(TAG, "insertVertex() : item=" + childItem);
        }
        Iterator i$ = this.mVertices.iterator();
        while (i$.hasNext()) {
            if (((Vertex) i$.next()).getElement().equals(childItem)) {
                return false;
            }
        }
        long j = this.mNextVertexId;
        this.mNextVertexId = 1 + j;
        Vertex<E> vertex = new Vertex(j, childItem);
        if (DEBUG) {
            Log.d(TAG, "insertVertex() : create new vertex=" + vertex);
        }
        this.mVertices.add(vertex);
        if (parentItem != null) {
            insertEdge(parentItem, childItem);
        }
        return true;
    }

    public boolean insertEdge(ItemWrapper<E> parentItem, ItemWrapper<E> childItem) {
        if (DEBUG) {
            Log.d(TAG, "insertEdge() : startVertexItem=" + parentItem + " endVertexItem=" + childItem);
        }
        Vertex<E> parentVertex = null;
        Vertex<E> childVertex = null;
        Iterator i$ = this.mVertices.iterator();
        while (i$.hasNext()) {
            Vertex<E> v = (Vertex) i$.next();
            if (v.getElement().equals(parentItem)) {
                parentVertex = v;
            } else if (v.getElement().equals(childItem)) {
                childVertex = v;
            }
        }
        if (parentVertex == null || childVertex == null) {
            return false;
        }
        long j = this.mNextEdgeId;
        this.mNextEdgeId = 1 + j;
        Edge<E> edge = new Edge(j, parentVertex, childVertex);
        if (DEBUG) {
            Log.d(TAG, "insertEdge() : create new edge=" + edge);
        }
        this.mEdges.add(edge);
        incidentEdges(parentVertex).add(edge);
        incidentEdges(childVertex).add(edge);
        return true;
    }

    public boolean removeEdge(Edge<E> e) {
        return removeEdge(e.id);
    }

    private boolean removeEdge(long id) {
        if (DEBUG) {
            Log.d(TAG, "removeEdge() : id=" + id);
        }
        Edge<E> e = idToEdge(id);
        if (e != null) {
            this.mEdges.remove(e);
            Vertex<E> startVertex = e.startVertex;
            Vertex<E> endVertex = e.endVertex;
            incidentEdges(startVertex).remove(e);
            incidentEdges(endVertex).remove(e);
        }
        return false;
    }

    public boolean removeVertex(ItemWrapper<E> item) {
        if (DEBUG) {
            Log.d(TAG, "removeVertex() : item=" + item);
        }
        Vertex<E> v = itemToVertex(item);
        if (v == null) {
            return false;
        }
        Vertex<E> parent = parentVertex(v);
        ArrayList<Vertex<E>> vChildVertices = childVertices(v);
        if (parent == null) {
            if (incidentEdges(v).size() != 0) {
                if (v.dummy) {
                    Log.w(TAG, "removeVertex() : cannot remove dummy vertex which does not have ancestor.");
                    return false;
                }
                parent = insertDummyVertex(new ItemWrapper(null), this.mVertices.indexOf(v));
                Log.d(TAG, "removeVertex() : insertDummyVertex, because there is no ancestor.");
            }
            if (parent == null) {
                return false;
            }
        }
        this.mVertices.remove(v);
        ArrayList<Edge<E>> incidenceList = incidentEdges(v);
        while (incidenceList.size() != 0) {
            removeEdge(((Edge) incidenceList.get(0)).id);
        }
        Iterator i$ = vChildVertices.iterator();
        while (i$.hasNext()) {
            insertEdge(parent.getElement(), ((Vertex) i$.next()).getElement());
        }
        return true;
    }

    public Vertex<E> itemToVertex(ItemWrapper<E> item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        Iterator i$ = this.mVertices.iterator();
        while (i$.hasNext()) {
            Vertex<E> v = (Vertex) i$.next();
            if (v.getElement().equals(item)) {
                return v;
            }
        }
        return null;
    }

    private Edge<E> idToEdge(long id) {
        if (id < 0) {
            return null;
        }
        Iterator i$ = this.mEdges.iterator();
        while (i$.hasNext()) {
            Edge<E> e = (Edge) i$.next();
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }

    Vertex<E> insertDummyVertex(ItemWrapper<E> itemWrapper, int index) {
        if (DEBUG) {
            Log.d(TAG, "insertDummyVertex() : itemWrapper=" + itemWrapper);
        }
        long j = this.mNextVertexId;
        this.mNextVertexId = 1 + j;
        Vertex<E> vertex = new Vertex(j, itemWrapper);
        vertex.dummy = true;
        if (DEBUG) {
            Log.d(TAG, "insertDummyVertex() : create new vertex=" + vertex);
        }
        this.mVertices.add(index, vertex);
        return vertex;
    }

    public Vertex<E> parentVertex(Vertex<E> v) {
        if (v == null) {
            throw new NullPointerException("v is null");
        }
        Iterator i$ = incidentEdges(v).iterator();
        while (i$.hasNext()) {
            Edge<E> e = (Edge) i$.next();
            if (destination(e) == v) {
                return origin(e);
            }
        }
        return null;
    }

    public Vertex<E> getRootVertex(Vertex<E> v) {
        if (v == null) {
            throw new NullPointerException("v is null");
        }
        Vertex<E> progenitor = parentVertex(v);
        while (progenitor != null) {
            progenitor = parentVertex(progenitor);
        }
        return progenitor;
    }

    public Vertex<E> getRootVertex(ItemWrapper<E> item) {
        if (DEBUG) {
            Log.d(TAG, "getRootVertex() : item=" + item);
        }
        Vertex<E> v = itemToVertex(item);
        if (v == null) {
            return null;
        }
        Vertex<E> progenitorVertex = getRootVertex((Vertex) v);
        return progenitorVertex != null ? progenitorVertex : v;
    }

    public ArrayList<Vertex<E>> childVertices(Vertex<E> v) {
        if (v == null) {
            throw new NullPointerException("v is null");
        }
        ArrayList<Vertex<E>> descendants = new ArrayList();
        Iterator i$ = incidentEdges(v).iterator();
        while (i$.hasNext()) {
            Edge<E> e = (Edge) i$.next();
            if (origin(e) == v) {
                descendants.add(destination(e));
            }
        }
        return descendants;
    }

    public ArrayList<Vertex<E>> ancestorVertices(Vertex<E> v) {
        if (v == null) {
            throw new NullPointerException("v is null");
        }
        ArrayList<Vertex<E>> ancestor = new ArrayList();
        Vertex<E> parent = parentVertex(v);
        while (parent != null) {
            ancestor.add(parent);
            parent = parentVertex(parent);
        }
        return ancestor;
    }

    public ArrayList<Vertex<E>> descendantVertices(Vertex<E> v) {
        if (v == null) {
            throw new NullPointerException("v is null");
        }
        ArrayList<Vertex<E>> descendants = new ArrayList();
        this.mDfs.getDescendantsOf(v, descendants);
        return descendants;
    }

    public ArrayList<Vertex<E>> getRootVertices() {
        return this.mDfs.getRootVertices();
    }

    public ArrayList<Vertex<E>> doDfsForAllVertices() {
        return this.mDfs.doDfsForAllVertices();
    }

    public Vertex<E> detachSubGraph(ItemWrapper<E> itemWrapper) {
        if (DEBUG) {
            Log.d(TAG, "detachSubGraph() : item=" + itemWrapper);
        }
        Vertex<E> v = itemToVertex(itemWrapper);
        Vertex<E> parentVertex = parentVertex(v);
        if (v == null) {
            throw new NullPointerException("v is null");
        }
        if (parentVertex != null && !parentVertex.dummy) {
            Iterator i$ = incidentEdges(v).iterator();
            while (i$.hasNext()) {
                Edge<E> e = (Edge) i$.next();
                if (destination(e) == v) {
                    removeEdge(e.id);
                    break;
                }
            }
        } else if (DEBUG) {
            Log.w(TAG, "Already splitted. Can't create a split from object=" + itemWrapper.getWrappedItem());
        }
        return v;
    }

    public boolean attachSubGraph(ItemWrapper<E> candidateParentItem, ItemWrapper<E> childItem) {
        if (DEBUG) {
            Log.d(TAG, "attachSubGraph() : childItem=" + childItem + " to candidateParentItem=" + candidateParentItem);
        }
        Vertex<E> childVertex = itemToVertex(childItem);
        Vertex<E> candidateParentVertex = itemToVertex(candidateParentItem);
        if (childVertex == null || candidateParentVertex == null) {
            throw new NullPointerException("Vertex is null");
        }
        Vertex<E> rootVertex = getRootVertex((Vertex) childVertex);
        if (rootVertex == null) {
            rootVertex = childVertex;
        }
        ArrayList<Vertex<E>> descendantVertices = descendantVertices(rootVertex);
        descendantVertices.add(rootVertex);
        if (DEBUG) {
            Log.d(TAG, "Descendants of " + rootVertex + " are " + descendantVertices);
        }
        Iterator i$ = descendantVertices.iterator();
        while (i$.hasNext()) {
            if (((Vertex) i$.next()).getElement().equals(candidateParentItem)) {
                Log.e(TAG, "Tried to add in same subgraph");
                return false;
            }
        }
        Vertex<E> detachedSubGraphRootVertex = detachSubGraph(childItem);
        insertEdge(candidateParentVertex.getElement(), detachedSubGraphRootVertex.getElement());
        if (detachedSubGraphRootVertex.dummy) {
            removeVertex(detachedSubGraphRootVertex.getElement());
        }
        return true;
    }

    public Vertex<E> getLeafVertex(ItemWrapper<E> item) {
        if (DEBUG) {
            Log.d(TAG, "getLeaf() : item=" + item);
        }
        Vertex<E> v = itemToVertex(item);
        if (v != null) {
            return this.mDfs.getLeaf(v);
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append(Graph.class.getSimpleName());
        sb.append(" {");
        sb.append(" \n name=" + this.mGraphName);
        sb.append(" \n vertex count=" + this.mVertices.size());
        sb.append(" \n edge count=" + this.mEdges.size());
        sb.append(" \n graph=" + this.mDfs.doDfsForAllVertices());
        sb.append(" \n Root vertices in graph=" + this.mDfs.getRootVertices());
        sb.append(" } ");
        return sb.toString();
    }

    public void dump(PrintWriter pw, String prefix, boolean dumpAll) {
        String innerPrefix = prefix + "  ";
        pw.print(prefix);
        pw.println("Context relation graph : ");
        pw.print(innerPrefix);
        pw.print("mGraphName=");
        pw.println(this.mGraphName);
        pw.print(innerPrefix);
        pw.print("Vertex size=");
        pw.println(this.mVertices.size());
        pw.print(innerPrefix);
        pw.print("Edge size=");
        pw.println(this.mEdges.size());
        pw.print(innerPrefix);
        pw.print("rootVertices= {");
        Iterator i$ = this.mDfs.getRootVertices().iterator();
        while (i$.hasNext()) {
            pw.print(" v" + ((Vertex) i$.next()).id);
        }
        pw.println(" }");
        if (dumpAll) {
            pw.print(innerPrefix);
            pw.println("DFS Traversed Vertices= {");
            i$ = this.mDfs.doDfsForAllVertices().iterator();
            while (i$.hasNext()) {
                Vertex<E> v = (Vertex) i$.next();
                pw.print(innerPrefix + "  ");
                pw.println(v);
            }
            pw.print(innerPrefix);
            pw.println("}");
        }
    }
}
