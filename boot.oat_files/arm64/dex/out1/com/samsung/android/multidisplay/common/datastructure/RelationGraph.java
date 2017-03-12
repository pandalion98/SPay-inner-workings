package com.samsung.android.multidisplay.common.datastructure;

import android.util.Log;
import com.samsung.android.multidisplay.common.ContextRelationManager;
import com.samsung.android.multidisplay.common.datastructure.graph.Graph;
import com.samsung.android.multidisplay.common.datastructure.graph.ItemWrapper;
import com.samsung.android.multidisplay.common.datastructure.graph.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RelationGraph {
    static final boolean DEBUG = ContextRelationManager.DEBUG;
    static final String TAG = "RelationGraph";
    private Graph<RelationObject> mGraph;
    private HashMap<RelationObject, ItemWrapper<RelationObject>> mItemWrapperHashMap = new HashMap();

    public RelationGraph(String name) {
        this.mGraph = new Graph(RelationObject.class, name);
    }

    public boolean addRelation(RelationObject parentRelationObject, RelationObject childRelationObject) {
        if (DEBUG) {
            Log.d(TAG, "addRelation() : object=" + childRelationObject);
        }
        if (childRelationObject == null) {
            return false;
        }
        ItemWrapper<RelationObject> childItem = new ItemWrapper(childRelationObject);
        if (!this.mGraph.insertVertex((ItemWrapper) this.mItemWrapperHashMap.get(parentRelationObject), childItem)) {
            return false;
        }
        this.mItemWrapperHashMap.put(childRelationObject, childItem);
        garbageCollect();
        return true;
    }

    public boolean removeRelation(RelationObject relationObject) {
        ItemWrapper<RelationObject> itemWrapper = (ItemWrapper) this.mItemWrapperHashMap.get(relationObject);
        if (itemWrapper != null) {
            if (this.mGraph.removeVertex(itemWrapper)) {
                this.mItemWrapperHashMap.remove(relationObject);
                garbageCollect();
                return true;
            }
        } else if (DEBUG) {
            Log.w(TAG, "no WrapperItem for object=" + relationObject);
        }
        return false;
    }

    public void propagateChangedRelationInfo(RelationObject relationObject, Object updateInfo) {
        if (DEBUG) {
            Log.d(TAG, "propagateChangedRelationInfo() : object=" + relationObject + " updateInfo=" + updateInfo);
        }
        ArrayList<RelationObject> descendantVertices = getDescendantRelation(relationObject);
        if (descendantVertices.size() > 0) {
            Iterator i$ = descendantVertices.iterator();
            while (i$.hasNext()) {
                RelationObject r = (RelationObject) i$.next();
                if (r != null) {
                    if (DEBUG) {
                        Log.d(TAG, " propagate to =" + r);
                    }
                    r.updateRelatedInfo(updateInfo);
                }
            }
            garbageCollect();
            return;
        }
        throw new NoSuchElementException();
    }

    public RelationObject getLeafRelation(RelationObject relationObject) {
        ItemWrapper<RelationObject> itemWrapper = (ItemWrapper) this.mItemWrapperHashMap.get(relationObject);
        if (itemWrapper != null) {
            Vertex<RelationObject> v = this.mGraph.getLeafVertex(itemWrapper);
            if (v != null) {
                ItemWrapper<RelationObject> leafItemWrapper = v.getElement();
                if (leafItemWrapper != null) {
                    return (RelationObject) leafItemWrapper.getWrappedItem();
                }
                return null;
            } else if (!DEBUG) {
                return null;
            } else {
                Log.w(TAG, "getLeafRelation() : no vertex for itemWrapper=" + itemWrapper);
                return null;
            }
        } else if (!DEBUG) {
            return null;
        } else {
            Log.w(TAG, "getLeafRelation() : no WrapperItem for object=" + relationObject);
            return null;
        }
    }

    public RelationObject getRootRelation(RelationObject relationObject) {
        ItemWrapper itemWrapper = (ItemWrapper) this.mItemWrapperHashMap.get(relationObject);
        if (itemWrapper != null) {
            Vertex<RelationObject> v = this.mGraph.getRootVertex(itemWrapper);
            if (v != null) {
                ItemWrapper<RelationObject> rootItemWrapper = v.getElement();
                if (rootItemWrapper != null) {
                    return (RelationObject) rootItemWrapper.getWrappedItem();
                }
                return null;
            } else if (!DEBUG) {
                return null;
            } else {
                Log.w(TAG, "getRootRelation() : no vertex for itemWrapper=" + itemWrapper);
                return null;
            }
        } else if (!DEBUG) {
            return null;
        } else {
            Log.w(TAG, "getRootRelation() : no WrapperItem for object=" + relationObject);
            return null;
        }
    }

    public ArrayList<RelationObject> getDescendantRelation(RelationObject relationObject) {
        ArrayList<RelationObject> descendantRelationList = new ArrayList();
        ItemWrapper<RelationObject> itemWrapper = (ItemWrapper) this.mItemWrapperHashMap.get(relationObject);
        if (itemWrapper != null) {
            Vertex self = this.mGraph.itemToVertex(itemWrapper);
            Vertex<RelationObject> progenitorVertex = this.mGraph.getRootVertex(self);
            ArrayList<Vertex<RelationObject>> descendantVertices = new ArrayList();
            if (progenitorVertex != null) {
                descendantVertices = this.mGraph.descendantVertices(progenitorVertex);
                descendantVertices.add(progenitorVertex);
            } else if (self != null) {
                descendantVertices = this.mGraph.descendantVertices(self);
                descendantVertices.add(self);
            }
            Iterator i$ = descendantVertices.iterator();
            while (i$.hasNext()) {
                Vertex<RelationObject> v = (Vertex) i$.next();
                if (v != null) {
                    ItemWrapper<RelationObject> wrapper = v.getElement();
                    if (wrapper != null) {
                        descendantRelationList.add(wrapper.getWrappedItem());
                    }
                }
            }
        } else if (DEBUG) {
            Log.w(TAG, "getDescendantRelation() : no WrapperItem for object=" + relationObject);
        }
        return descendantRelationList;
    }

    public boolean attachRelation(RelationObject candidateParentRelationObject, RelationObject childRelationObject) {
        Log.d(TAG, "attachRelation() : new relation root=" + candidateParentRelationObject + " : child root = " + childRelationObject);
        ItemWrapper<RelationObject> candidateParentWrapperItem = (ItemWrapper) this.mItemWrapperHashMap.get(candidateParentRelationObject);
        ItemWrapper<RelationObject> childWrapperItem = (ItemWrapper) this.mItemWrapperHashMap.get(childRelationObject);
        if (candidateParentWrapperItem == null || childWrapperItem == null) {
            if (DEBUG) {
                Log.w(TAG, "no WrapperItem for object=" + candidateParentRelationObject + " OR object=" + childRelationObject);
            }
            return false;
        }
        boolean result = this.mGraph.attachSubGraph(candidateParentWrapperItem, childWrapperItem);
        garbageCollect();
        return result;
    }

    public boolean detachRelation(RelationObject relationObject) {
        Log.d(TAG, "detachRelation() : new relation root=" + relationObject);
        ItemWrapper<RelationObject> itemWrapper = (ItemWrapper) this.mItemWrapperHashMap.get(relationObject);
        if (itemWrapper == null) {
            if (DEBUG) {
                Log.w(TAG, "detachRelation() : no ItemWrapper for object=" + relationObject);
            }
            return false;
        }
        this.mGraph.detachSubGraph(itemWrapper);
        garbageCollect();
        return true;
    }

    public void garbageCollect() {
        Log.d(TAG, "garbageCollect()");
        ArrayList<Vertex<RelationObject>> vertices = this.mGraph.vertices();
        ArrayList<Vertex<RelationObject>> gargabeVertices = new ArrayList();
        Iterator i$ = vertices.iterator();
        while (i$.hasNext()) {
            Vertex<RelationObject> v = (Vertex) i$.next();
            if (v.dummy) {
                Vertex<RelationObject> parentVertex = this.mGraph.parentVertex(v);
                if (parentVertex != null && parentVertex.dummy) {
                    if (DEBUG) {
                        Log.d(TAG, "garbageCollect() : release duplicated dummy");
                    }
                    gargabeVertices.add(v);
                }
                if (this.mGraph.incidentEdges(v).size() == 0) {
                    gargabeVertices.add(v);
                }
            } else {
                ItemWrapper<RelationObject> itemWrapper = v.getElement();
                RelationObject relationObject = (RelationObject) itemWrapper.getWrappedItem();
                if (relationObject == null || relationObject.getObject() == null) {
                    if (DEBUG) {
                        Log.d(TAG, "garbageCollect() : release itemWrapper=" + itemWrapper);
                    }
                    gargabeVertices.add(v);
                    Iterator<ItemWrapper<RelationObject>> iterator = this.mItemWrapperHashMap.values().iterator();
                    while (iterator.hasNext()) {
                        ItemWrapper<RelationObject> w = (ItemWrapper) iterator.next();
                        if (w == itemWrapper) {
                            iterator.remove();
                            if (DEBUG) {
                                Log.w(TAG, "Garbage collected " + w);
                            }
                        }
                    }
                }
            }
        }
        i$ = gargabeVertices.iterator();
        while (i$.hasNext()) {
            this.mGraph.removeVertex(((Vertex) i$.next()).getElement());
        }
        gargabeVertices.clear();
    }

    public String toString() {
        return "Graph \n " + this.mGraph;
    }

    public Graph<RelationObject> getGraph() {
        return this.mGraph;
    }
}
