package com.google.gson.internal;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public final class LinkedHashTreeMap<K, V> extends AbstractMap<K, V> implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Comparator<Comparable> NATURAL_ORDER;
    Comparator<? super K> comparator;
    private EntrySet entrySet;
    final Node<K, V> header;
    private KeySet keySet;
    int modCount;
    int size;
    Node<K, V>[] table;
    int threshold;

    /* renamed from: com.google.gson.internal.LinkedHashTreeMap.1 */
    static class C02901 implements Comparator<Comparable> {
        C02901() {
        }

        public int compare(Comparable comparable, Comparable comparable2) {
            return comparable.compareTo(comparable2);
        }
    }

    static final class AvlBuilder<K, V> {
        private int leavesSkipped;
        private int leavesToSkip;
        private int size;
        private Node<K, V> stack;

        AvlBuilder() {
        }

        void reset(int i) {
            this.leavesToSkip = ((Integer.highestOneBit(i) * 2) - 1) - i;
            this.size = 0;
            this.leavesSkipped = 0;
            this.stack = null;
        }

        void add(Node<K, V> node) {
            node.right = null;
            node.parent = null;
            node.left = null;
            node.height = 1;
            if (this.leavesToSkip > 0 && (this.size & 1) == 0) {
                this.size++;
                this.leavesToSkip--;
                this.leavesSkipped++;
            }
            node.parent = this.stack;
            this.stack = node;
            this.size++;
            if (this.leavesToSkip > 0 && (this.size & 1) == 0) {
                this.size++;
                this.leavesToSkip--;
                this.leavesSkipped++;
            }
            for (int i = 4; (this.size & (i - 1)) == i - 1; i *= 2) {
                Node node2;
                Node node3;
                if (this.leavesSkipped == 0) {
                    node2 = this.stack;
                    node3 = node2.parent;
                    Node node4 = node3.parent;
                    node3.parent = node4.parent;
                    this.stack = node3;
                    node3.left = node4;
                    node3.right = node2;
                    node3.height = node2.height + 1;
                    node4.parent = node3;
                    node2.parent = node3;
                } else if (this.leavesSkipped == 1) {
                    node2 = this.stack;
                    node3 = node2.parent;
                    this.stack = node3;
                    node3.right = node2;
                    node3.height = node2.height + 1;
                    node2.parent = node3;
                    this.leavesSkipped = 0;
                } else if (this.leavesSkipped == 2) {
                    this.leavesSkipped = 0;
                }
            }
        }

        Node<K, V> root() {
            Node<K, V> node = this.stack;
            if (node.parent == null) {
                return node;
            }
            throw new IllegalStateException();
        }
    }

    static class AvlIterator<K, V> {
        private Node<K, V> stackTop;

        AvlIterator() {
        }

        void reset(Node<K, V> node) {
            Node node2 = null;
            while (node != null) {
                node.parent = node2;
                Node<K, V> node3 = node;
                node = node.left;
            }
            this.stackTop = node2;
        }

        public Node<K, V> next() {
            Node<K, V> node = this.stackTop;
            if (node == null) {
                return null;
            }
            Node node2 = node.parent;
            node.parent = null;
            for (Node node3 = node.right; node3 != null; node3 = node3.left) {
                node3.parent = node2;
                node2 = node3;
            }
            this.stackTop = node2;
            return node;
        }
    }

    private abstract class LinkedTreeMapIterator<T> implements Iterator<T> {
        int expectedModCount;
        Node<K, V> lastReturned;
        Node<K, V> next;

        private LinkedTreeMapIterator() {
            this.next = LinkedHashTreeMap.this.header.next;
            this.lastReturned = null;
            this.expectedModCount = LinkedHashTreeMap.this.modCount;
        }

        public final boolean hasNext() {
            return this.next != LinkedHashTreeMap.this.header;
        }

        final Node<K, V> nextNode() {
            Node<K, V> node = this.next;
            if (node == LinkedHashTreeMap.this.header) {
                throw new NoSuchElementException();
            } else if (LinkedHashTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                this.next = node.next;
                this.lastReturned = node;
                return node;
            }
        }

        public final void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            LinkedHashTreeMap.this.removeInternal(this.lastReturned, true);
            this.lastReturned = null;
            this.expectedModCount = LinkedHashTreeMap.this.modCount;
        }
    }

    final class EntrySet extends AbstractSet<Entry<K, V>> {

        /* renamed from: com.google.gson.internal.LinkedHashTreeMap.EntrySet.1 */
        class C02911 extends LinkedTreeMapIterator<Entry<K, V>> {
            C02911() {
                super(null);
            }

            public Entry<K, V> next() {
                return nextNode();
            }
        }

        EntrySet() {
        }

        public int size() {
            return LinkedHashTreeMap.this.size;
        }

        public Iterator<Entry<K, V>> iterator() {
            return new C02911();
        }

        public boolean contains(Object obj) {
            return (obj instanceof Entry) && LinkedHashTreeMap.this.findByEntry((Entry) obj) != null;
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Node findByEntry = LinkedHashTreeMap.this.findByEntry((Entry) obj);
            if (findByEntry == null) {
                return false;
            }
            LinkedHashTreeMap.this.removeInternal(findByEntry, true);
            return true;
        }

        public void clear() {
            LinkedHashTreeMap.this.clear();
        }
    }

    final class KeySet extends AbstractSet<K> {

        /* renamed from: com.google.gson.internal.LinkedHashTreeMap.KeySet.1 */
        class C02921 extends LinkedTreeMapIterator<K> {
            C02921() {
                super(null);
            }

            public K next() {
                return nextNode().key;
            }
        }

        KeySet() {
        }

        public int size() {
            return LinkedHashTreeMap.this.size;
        }

        public Iterator<K> iterator() {
            return new C02921();
        }

        public boolean contains(Object obj) {
            return LinkedHashTreeMap.this.containsKey(obj);
        }

        public boolean remove(Object obj) {
            return LinkedHashTreeMap.this.removeInternalByKey(obj) != null;
        }

        public void clear() {
            LinkedHashTreeMap.this.clear();
        }
    }

    static final class Node<K, V> implements Entry<K, V> {
        final int hash;
        int height;
        final K key;
        Node<K, V> left;
        Node<K, V> next;
        Node<K, V> parent;
        Node<K, V> prev;
        Node<K, V> right;
        V value;

        Node() {
            this.key = null;
            this.hash = -1;
            this.prev = this;
            this.next = this;
        }

        Node(Node<K, V> node, K k, int i, Node<K, V> node2, Node<K, V> node3) {
            this.parent = node;
            this.key = k;
            this.hash = i;
            this.height = 1;
            this.next = node2;
            this.prev = node3;
            node3.next = this;
            node2.prev = this;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (this.key == null) {
                if (entry.getKey() != null) {
                    return false;
                }
            } else if (!this.key.equals(entry.getKey())) {
                return false;
            }
            if (this.value == null) {
                if (entry.getValue() != null) {
                    return false;
                }
            } else if (!this.value.equals(entry.getValue())) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = this.key == null ? 0 : this.key.hashCode();
            if (this.value != null) {
                i = this.value.hashCode();
            }
            return hashCode ^ i;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public Node<K, V> first() {
            Node<K, V> node;
            for (Node<K, V> node2 = this.left; node2 != null; node2 = node2.left) {
                node = node2;
            }
            return node;
        }

        public Node<K, V> last() {
            Node<K, V> node;
            for (Node<K, V> node2 = this.right; node2 != null; node2 = node2.right) {
                node = node2;
            }
            return node;
        }
    }

    static {
        $assertionsDisabled = !LinkedHashTreeMap.class.desiredAssertionStatus();
        NATURAL_ORDER = new C02901();
    }

    public LinkedHashTreeMap() {
        this(NATURAL_ORDER);
    }

    public LinkedHashTreeMap(Comparator<? super K> comparator) {
        this.size = 0;
        this.modCount = 0;
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        this.comparator = comparator;
        this.header = new Node();
        this.table = new Node[16];
        this.threshold = (this.table.length / 2) + (this.table.length / 4);
    }

    public int size() {
        return this.size;
    }

    public V get(Object obj) {
        Node findByObject = findByObject(obj);
        return findByObject != null ? findByObject.value : null;
    }

    public boolean containsKey(Object obj) {
        return findByObject(obj) != null;
    }

    public V put(K k, V v) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        Node find = find(k, true);
        V v2 = find.value;
        find.value = v;
        return v2;
    }

    public void clear() {
        Arrays.fill(this.table, null);
        this.size = 0;
        this.modCount++;
        Node node = this.header;
        Node node2 = node.next;
        while (node2 != node) {
            Node node3 = node2.next;
            node2.prev = null;
            node2.next = null;
            node2 = node3;
        }
        node.prev = node;
        node.next = node;
    }

    public V remove(Object obj) {
        Node removeInternalByKey = removeInternalByKey(obj);
        return removeInternalByKey != null ? removeInternalByKey.value : null;
    }

    Node<K, V> find(K k, boolean z) {
        int i;
        Comparator comparator = this.comparator;
        Node[] nodeArr = this.table;
        int secondaryHash = secondaryHash(k.hashCode());
        int length = secondaryHash & (nodeArr.length - 1);
        Node node = nodeArr[length];
        if (node != null) {
            int compareTo;
            Comparable comparable = comparator == NATURAL_ORDER ? (Comparable) k : null;
            while (true) {
                compareTo = comparable != null ? comparable.compareTo(node.key) : comparator.compare(k, node.key);
                if (compareTo == 0) {
                    return node;
                }
                Node node2 = compareTo < 0 ? node.left : node.right;
                if (node2 == null) {
                    break;
                }
                node = node2;
            }
            i = compareTo;
        } else {
            i = 0;
        }
        if (!z) {
            return null;
        }
        Node<K, V> node3;
        Node node4 = this.header;
        if (node != null) {
            node3 = new Node(node, k, secondaryHash, node4, node4.prev);
            if (i < 0) {
                node.left = node3;
            } else {
                node.right = node3;
            }
            rebalance(node, true);
        } else if (comparator != NATURAL_ORDER || (k instanceof Comparable)) {
            node3 = new Node(node, k, secondaryHash, node4, node4.prev);
            nodeArr[length] = node3;
        } else {
            throw new ClassCastException(k.getClass().getName() + " is not Comparable");
        }
        int i2 = this.size;
        this.size = i2 + 1;
        if (i2 > this.threshold) {
            doubleCapacity();
        }
        this.modCount++;
        return node3;
    }

    Node<K, V> findByObject(Object obj) {
        Node<K, V> node = null;
        if (obj != null) {
            try {
                node = find(obj, false);
            } catch (ClassCastException e) {
            }
        }
        return node;
    }

    Node<K, V> findByEntry(Entry<?, ?> entry) {
        Node<K, V> findByObject = findByObject(entry.getKey());
        Object obj = (findByObject == null || !equal(findByObject.value, entry.getValue())) ? null : 1;
        return obj != null ? findByObject : null;
    }

    private boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    private static int secondaryHash(int i) {
        int i2 = ((i >>> 20) ^ (i >>> 12)) ^ i;
        return (i2 >>> 4) ^ ((i2 >>> 7) ^ i2);
    }

    void removeInternal(Node<K, V> node, boolean z) {
        int i = 0;
        if (z) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
        }
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node.parent;
        if (node2 == null || node3 == null) {
            if (node2 != null) {
                replaceInParent(node, node2);
                node.left = null;
            } else if (node3 != null) {
                replaceInParent(node, node3);
                node.right = null;
            } else {
                replaceInParent(node, null);
            }
            rebalance(node4, false);
            this.size--;
            this.modCount++;
            return;
        }
        int i2;
        node2 = node2.height > node3.height ? node2.last() : node3.first();
        removeInternal(node2, false);
        node4 = node.left;
        if (node4 != null) {
            i2 = node4.height;
            node2.left = node4;
            node4.parent = node2;
            node.left = null;
        } else {
            i2 = 0;
        }
        node4 = node.right;
        if (node4 != null) {
            i = node4.height;
            node2.right = node4;
            node4.parent = node2;
            node.right = null;
        }
        node2.height = Math.max(i2, i) + 1;
        replaceInParent(node, node2);
    }

    Node<K, V> removeInternalByKey(Object obj) {
        Node<K, V> findByObject = findByObject(obj);
        if (findByObject != null) {
            removeInternal(findByObject, true);
        }
        return findByObject;
    }

    private void replaceInParent(Node<K, V> node, Node<K, V> node2) {
        Node node3 = node.parent;
        node.parent = null;
        if (node2 != null) {
            node2.parent = node3;
        }
        if (node3 == null) {
            this.table[node.hash & (this.table.length - 1)] = node2;
        } else if (node3.left == node) {
            node3.left = node2;
        } else if ($assertionsDisabled || node3.right == node) {
            node3.right = node2;
        } else {
            throw new AssertionError();
        }
    }

    private void rebalance(Node<K, V> node, boolean z) {
        while (node != null) {
            int i;
            Node node2 = node.left;
            Node node3 = node.right;
            int i2 = node2 != null ? node2.height : 0;
            if (node3 != null) {
                i = node3.height;
            } else {
                i = 0;
            }
            int i3 = i2 - i;
            Node node4;
            if (i3 == -2) {
                node2 = node3.left;
                node4 = node3.right;
                if (node4 != null) {
                    i2 = node4.height;
                } else {
                    i2 = 0;
                }
                if (node2 != null) {
                    i = node2.height;
                } else {
                    i = 0;
                }
                i -= i2;
                if (i == -1 || (i == 0 && !z)) {
                    rotateLeft(node);
                } else if ($assertionsDisabled || i == 1) {
                    rotateRight(node3);
                    rotateLeft(node);
                } else {
                    throw new AssertionError();
                }
                if (z) {
                    return;
                }
            } else if (i3 == 2) {
                node3 = node2.left;
                node4 = node2.right;
                i2 = node4 != null ? node4.height : 0;
                if (node3 != null) {
                    i = node3.height;
                } else {
                    i = 0;
                }
                i -= i2;
                if (i == 1 || (i == 0 && !z)) {
                    rotateRight(node);
                } else if ($assertionsDisabled || i == -1) {
                    rotateLeft(node2);
                    rotateRight(node);
                } else {
                    throw new AssertionError();
                }
                if (z) {
                    return;
                }
            } else if (i3 == 0) {
                node.height = i2 + 1;
                if (z) {
                    return;
                }
            } else if ($assertionsDisabled || i3 == -1 || i3 == 1) {
                node.height = Math.max(i2, i) + 1;
                if (!z) {
                    return;
                }
            } else {
                throw new AssertionError();
            }
            node = node.parent;
        }
    }

    private void rotateLeft(Node<K, V> node) {
        int i;
        int i2 = 0;
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node3.left;
        Node node5 = node3.right;
        node.right = node4;
        if (node4 != null) {
            node4.parent = node;
        }
        replaceInParent(node, node3);
        node3.left = node;
        node.parent = node3;
        if (node2 != null) {
            i = node2.height;
        } else {
            i = 0;
        }
        node.height = Math.max(i, node4 != null ? node4.height : 0) + 1;
        int i3 = node.height;
        if (node5 != null) {
            i2 = node5.height;
        }
        node3.height = Math.max(i3, i2) + 1;
    }

    private void rotateRight(Node<K, V> node) {
        int i;
        int i2 = 0;
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node2.left;
        Node node5 = node2.right;
        node.left = node5;
        if (node5 != null) {
            node5.parent = node;
        }
        replaceInParent(node, node2);
        node2.right = node;
        node.parent = node2;
        if (node3 != null) {
            i = node3.height;
        } else {
            i = 0;
        }
        node.height = Math.max(i, node5 != null ? node5.height : 0) + 1;
        int i3 = node.height;
        if (node4 != null) {
            i2 = node4.height;
        }
        node2.height = Math.max(i3, i2) + 1;
    }

    public Set<Entry<K, V>> entrySet() {
        Set set = this.entrySet;
        if (set != null) {
            return set;
        }
        Set<Entry<K, V>> entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    public Set<K> keySet() {
        Set set = this.keySet;
        if (set != null) {
            return set;
        }
        Set<K> keySet = new KeySet();
        this.keySet = keySet;
        return keySet;
    }

    private void doubleCapacity() {
        this.table = doubleCapacity(this.table);
        this.threshold = (this.table.length / 2) + (this.table.length / 4);
    }

    static <K, V> Node<K, V>[] doubleCapacity(Node<K, V>[] nodeArr) {
        int length = nodeArr.length;
        Node<K, V>[] nodeArr2 = new Node[(length * 2)];
        AvlIterator avlIterator = new AvlIterator();
        AvlBuilder avlBuilder = new AvlBuilder();
        AvlBuilder avlBuilder2 = new AvlBuilder();
        for (int i = 0; i < length; i++) {
            Node node = nodeArr[i];
            if (node != null) {
                Node root;
                Node root2;
                avlIterator.reset(node);
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    Node next = avlIterator.next();
                    if (next == null) {
                        break;
                    } else if ((next.hash & length) == 0) {
                        i3++;
                    } else {
                        i2++;
                    }
                }
                avlBuilder.reset(i3);
                avlBuilder2.reset(i2);
                avlIterator.reset(node);
                while (true) {
                    node = avlIterator.next();
                    if (node == null) {
                        break;
                    } else if ((node.hash & length) == 0) {
                        avlBuilder.add(node);
                    } else {
                        avlBuilder2.add(node);
                    }
                }
                if (i3 > 0) {
                    root = avlBuilder.root();
                } else {
                    root = null;
                }
                nodeArr2[i] = root;
                i3 = i + length;
                if (i2 > 0) {
                    root2 = avlBuilder2.root();
                } else {
                    root2 = null;
                }
                nodeArr2[i3] = root2;
            }
        }
        return nodeArr2;
    }

    private Object writeReplace() {
        return new LinkedHashMap(this);
    }
}
