package android.animation;

import android.animation.Animator.AnimatorListener;
import android.util.ArrayMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class AnimatorSet extends Animator {
    private ValueAnimator mDelayAnim = null;
    private long mDuration = -1;
    private TimeInterpolator mInterpolator = null;
    private boolean mNeedsSort = true;
    private ArrayMap<Animator, Node> mNodeMap = new ArrayMap();
    private ArrayList<Node> mNodes = new ArrayList();
    private ArrayList<Animator> mPlayingSet = new ArrayList();
    private boolean mReversible = true;
    private AnimatorSetListener mSetListener = null;
    private ArrayList<Node> mSortedNodes = new ArrayList();
    private long mStartDelay = 0;
    private boolean mStarted = false;
    boolean mTerminated = false;

    private class AnimatorSetListener implements AnimatorListener {
        private AnimatorSet mAnimatorSet;

        AnimatorSetListener(AnimatorSet animatorSet) {
            this.mAnimatorSet = animatorSet;
        }

        public void onAnimationCancel(Animator animation) {
            if (!AnimatorSet.this.mTerminated && AnimatorSet.this.mPlayingSet.size() == 0 && AnimatorSet.this.mListeners != null) {
                int numListeners = AnimatorSet.this.mListeners.size();
                for (int i = 0; i < numListeners; i++) {
                    ((AnimatorListener) AnimatorSet.this.mListeners.get(i)).onAnimationCancel(this.mAnimatorSet);
                }
            }
        }

        public void onAnimationEnd(Animator animation) {
            animation.removeListener(this);
            AnimatorSet.this.mPlayingSet.remove(animation);
            ((Node) this.mAnimatorSet.mNodeMap.get(animation)).done = true;
            if (!AnimatorSet.this.mTerminated) {
                int i;
                ArrayList<Node> sortedNodes = this.mAnimatorSet.mSortedNodes;
                boolean allDone = true;
                int numSortedNodes = sortedNodes.size();
                for (i = 0; i < numSortedNodes; i++) {
                    if (!((Node) sortedNodes.get(i)).done) {
                        allDone = false;
                        break;
                    }
                }
                if (allDone) {
                    if (AnimatorSet.this.mListeners != null) {
                        ArrayList<AnimatorListener> tmpListeners = (ArrayList) AnimatorSet.this.mListeners.clone();
                        int numListeners = tmpListeners.size();
                        for (i = 0; i < numListeners; i++) {
                            ((AnimatorListener) tmpListeners.get(i)).onAnimationEnd(this.mAnimatorSet);
                        }
                    }
                    this.mAnimatorSet.mStarted = false;
                    this.mAnimatorSet.mPaused = false;
                }
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }
    }

    public class Builder {
        private Node mCurrentNode;

        Builder(Animator anim) {
            this.mCurrentNode = (Node) AnimatorSet.this.mNodeMap.get(anim);
            if (this.mCurrentNode == null) {
                this.mCurrentNode = new Node(anim);
                AnimatorSet.this.mNodeMap.put(anim, this.mCurrentNode);
                AnimatorSet.this.mNodes.add(this.mCurrentNode);
            }
        }

        public Builder with(Animator anim) {
            Node node = (Node) AnimatorSet.this.mNodeMap.get(anim);
            if (node == null) {
                node = new Node(anim);
                AnimatorSet.this.mNodeMap.put(anim, node);
                AnimatorSet.this.mNodes.add(node);
            }
            node.addDependency(new Dependency(this.mCurrentNode, 0));
            return this;
        }

        public Builder before(Animator anim) {
            AnimatorSet.this.mReversible = false;
            Node node = (Node) AnimatorSet.this.mNodeMap.get(anim);
            if (node == null) {
                node = new Node(anim);
                AnimatorSet.this.mNodeMap.put(anim, node);
                AnimatorSet.this.mNodes.add(node);
            }
            node.addDependency(new Dependency(this.mCurrentNode, 1));
            return this;
        }

        public Builder after(Animator anim) {
            AnimatorSet.this.mReversible = false;
            Node node = (Node) AnimatorSet.this.mNodeMap.get(anim);
            if (node == null) {
                node = new Node(anim);
                AnimatorSet.this.mNodeMap.put(anim, node);
                AnimatorSet.this.mNodes.add(node);
            }
            this.mCurrentNode.addDependency(new Dependency(node, 1));
            return this;
        }

        public Builder after(long delay) {
            Animator anim = ValueAnimator.ofFloat(0.0f, 1.0f);
            anim.setDuration(delay);
            after(anim);
            return this;
        }
    }

    private static class Dependency {
        static final int AFTER = 1;
        static final int WITH = 0;
        public Node node;
        public int rule;

        public Dependency(Node node, int rule) {
            this.node = node;
            this.rule = rule;
        }
    }

    private static class DependencyListener implements AnimatorListener {
        private AnimatorSet mAnimatorSet;
        private Node mNode;
        private int mRule;

        public DependencyListener(AnimatorSet animatorSet, Node node, int rule) {
            this.mAnimatorSet = animatorSet;
            this.mNode = node;
            this.mRule = rule;
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            if (this.mRule == 1) {
                startIfReady(animation);
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
            if (this.mRule == 0) {
                startIfReady(animation);
            }
        }

        private void startIfReady(Animator dependencyAnimation) {
            if (this.mAnimatorSet != null && !this.mAnimatorSet.mTerminated) {
                Dependency dependencyToRemove = null;
                int numDependencies = this.mNode.tmpDependencies.size();
                for (int i = 0; i < numDependencies; i++) {
                    Dependency dependency = (Dependency) this.mNode.tmpDependencies.get(i);
                    if (dependency.rule == this.mRule && dependency.node.animation == dependencyAnimation) {
                        dependencyToRemove = dependency;
                        dependencyAnimation.removeListener(this);
                        break;
                    }
                }
                this.mNode.tmpDependencies.remove(dependencyToRemove);
                if (this.mNode.tmpDependencies.size() == 0) {
                    this.mNode.animation.start();
                    this.mAnimatorSet.mPlayingSet.add(this.mNode.animation);
                }
            }
        }
    }

    private static class Node implements Cloneable {
        public Animator animation;
        public ArrayList<Dependency> dependencies = null;
        public boolean done = false;
        private Node mTmpClone = null;
        public ArrayList<Node> nodeDependencies = null;
        public ArrayList<Node> nodeDependents = null;
        public ArrayList<Dependency> tmpDependencies = null;

        public Node(Animator animation) {
            this.animation = animation;
        }

        public void addDependency(Dependency dependency) {
            if (this.dependencies == null) {
                this.dependencies = new ArrayList();
                this.nodeDependencies = new ArrayList();
            }
            this.dependencies.add(dependency);
            if (!this.nodeDependencies.contains(dependency.node)) {
                this.nodeDependencies.add(dependency.node);
            }
            Node dependencyNode = dependency.node;
            if (dependencyNode.nodeDependents == null) {
                dependencyNode.nodeDependents = new ArrayList();
            }
            dependencyNode.nodeDependents.add(this);
        }

        public Node clone() {
            try {
                Node node = (Node) super.clone();
                node.animation = this.animation.clone();
                node.done = false;
                return node;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    public void playTogether(Animator... items) {
        if (items != null) {
            this.mNeedsSort = true;
            Builder builder = play(items[0]);
            for (int i = 1; i < items.length; i++) {
                builder.with(items[i]);
            }
        }
    }

    public void playTogether(Collection<Animator> items) {
        if (items != null && items.size() > 0) {
            this.mNeedsSort = true;
            Builder builder = null;
            for (Animator anim : items) {
                if (builder == null) {
                    builder = play(anim);
                } else {
                    builder.with(anim);
                }
            }
        }
    }

    public void playSequentially(Animator... items) {
        if (items != null) {
            this.mNeedsSort = true;
            if (items.length == 1) {
                play(items[0]);
                return;
            }
            this.mReversible = false;
            for (int i = 0; i < items.length - 1; i++) {
                play(items[i]).before(items[i + 1]);
            }
        }
    }

    public void playSequentially(List<Animator> items) {
        if (items != null && items.size() > 0) {
            this.mNeedsSort = true;
            if (items.size() == 1) {
                play((Animator) items.get(0));
                return;
            }
            this.mReversible = false;
            for (int i = 0; i < items.size() - 1; i++) {
                play((Animator) items.get(i)).before((Animator) items.get(i + 1));
            }
        }
    }

    public ArrayList<Animator> getChildAnimations() {
        ArrayList<Animator> childList = new ArrayList();
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            childList.add(((Node) i$.next()).animation);
        }
        return childList;
    }

    public void setTarget(Object target) {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            Animator animation = ((Node) i$.next()).animation;
            if (animation instanceof AnimatorSet) {
                ((AnimatorSet) animation).setTarget(target);
            } else if (animation instanceof ObjectAnimator) {
                ((ObjectAnimator) animation).setTarget(target);
            }
        }
    }

    public int getChangingConfigurations() {
        int conf = super.getChangingConfigurations();
        for (int i = 0; i < this.mNodes.size(); i++) {
            conf |= ((Node) this.mNodes.get(i)).animation.getChangingConfigurations();
        }
        return conf;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public Builder play(Animator anim) {
        if (anim == null) {
            return null;
        }
        this.mNeedsSort = true;
        return new Builder(anim);
    }

    public void cancel() {
        this.mTerminated = true;
        if (isStarted()) {
            Iterator i$;
            ArrayList<AnimatorListener> tmpListeners = null;
            if (this.mListeners != null) {
                tmpListeners = (ArrayList) this.mListeners.clone();
                i$ = tmpListeners.iterator();
                while (i$.hasNext()) {
                    ((AnimatorListener) i$.next()).onAnimationCancel(this);
                }
            }
            if (this.mDelayAnim != null && this.mDelayAnim.isRunning()) {
                this.mDelayAnim.cancel();
            } else if (this.mSortedNodes.size() > 0) {
                i$ = this.mSortedNodes.iterator();
                while (i$.hasNext()) {
                    ((Node) i$.next()).animation.cancel();
                }
            }
            if (tmpListeners != null) {
                i$ = tmpListeners.iterator();
                while (i$.hasNext()) {
                    ((AnimatorListener) i$.next()).onAnimationEnd(this);
                }
            }
            this.mStarted = false;
        }
    }

    public void end() {
        this.mTerminated = true;
        if (isStarted()) {
            Iterator i$;
            if (this.mSortedNodes.size() != this.mNodes.size()) {
                sortNodes();
                i$ = this.mSortedNodes.iterator();
                while (i$.hasNext()) {
                    Node node = (Node) i$.next();
                    if (this.mSetListener == null) {
                        this.mSetListener = new AnimatorSetListener(this);
                    }
                    node.animation.addListener(this.mSetListener);
                }
            }
            if (this.mDelayAnim != null) {
                this.mDelayAnim.cancel();
            }
            if (this.mSortedNodes.size() > 0) {
                i$ = this.mSortedNodes.iterator();
                while (i$.hasNext()) {
                    ((Node) i$.next()).animation.end();
                }
            }
            if (this.mListeners != null) {
                i$ = ((ArrayList) this.mListeners.clone()).iterator();
                while (i$.hasNext()) {
                    ((AnimatorListener) i$.next()).onAnimationEnd(this);
                }
            }
            this.mStarted = false;
        }
    }

    public boolean isRunning() {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            if (((Node) i$.next()).animation.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public void setStartDelay(long startDelay) {
        if (this.mStartDelay > 0) {
            this.mReversible = false;
        }
        this.mStartDelay = startDelay;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public AnimatorSet setDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("duration must be a value of zero or greater");
        }
        this.mDuration = duration;
        return this;
    }

    public void setupStartValues() {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            ((Node) i$.next()).animation.setupStartValues();
        }
    }

    public void setupEndValues() {
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            ((Node) i$.next()).animation.setupEndValues();
        }
    }

    public void pause() {
        boolean previouslyPaused = this.mPaused;
        super.pause();
        if (!previouslyPaused && this.mPaused) {
            if (this.mDelayAnim != null) {
                this.mDelayAnim.pause();
                return;
            }
            Iterator i$ = this.mNodes.iterator();
            while (i$.hasNext()) {
                ((Node) i$.next()).animation.pause();
            }
        }
    }

    public void resume() {
        boolean previouslyPaused = this.mPaused;
        super.resume();
        if (previouslyPaused && !this.mPaused) {
            if (this.mDelayAnim != null) {
                this.mDelayAnim.resume();
                return;
            }
            Iterator i$ = this.mNodes.iterator();
            while (i$.hasNext()) {
                ((Node) i$.next()).animation.resume();
            }
        }
    }

    public void start() {
        int i;
        ArrayList<AnimatorListener> tmpListeners;
        int numListeners;
        this.mTerminated = false;
        this.mStarted = true;
        this.mPaused = false;
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            ((Node) i$.next()).animation.setAllowRunningAsynchronously(false);
        }
        if (this.mDuration >= 0) {
            i$ = this.mNodes.iterator();
            while (i$.hasNext()) {
                ((Node) i$.next()).animation.setDuration(this.mDuration);
            }
        }
        if (this.mInterpolator != null) {
            i$ = this.mNodes.iterator();
            while (i$.hasNext()) {
                ((Node) i$.next()).animation.setInterpolator(this.mInterpolator);
            }
        }
        sortNodes();
        int numSortedNodes = this.mSortedNodes.size();
        for (i = 0; i < numSortedNodes; i++) {
            Node node = (Node) this.mSortedNodes.get(i);
            ArrayList<AnimatorListener> oldListeners = node.animation.getListeners();
            if (oldListeners != null && oldListeners.size() > 0) {
                i$ = new ArrayList(oldListeners).iterator();
                while (i$.hasNext()) {
                    AnimatorListener listener = (AnimatorListener) i$.next();
                    if ((listener instanceof DependencyListener) || (listener instanceof AnimatorSetListener)) {
                        node.animation.removeListener(listener);
                    }
                }
            }
        }
        final ArrayList<Node> nodesToStart = new ArrayList();
        for (i = 0; i < numSortedNodes; i++) {
            node = (Node) this.mSortedNodes.get(i);
            if (this.mSetListener == null) {
                this.mSetListener = new AnimatorSetListener(this);
            }
            if (node.dependencies == null || node.dependencies.size() == 0) {
                nodesToStart.add(node);
            } else {
                int numDependencies = node.dependencies.size();
                for (int j = 0; j < numDependencies; j++) {
                    Dependency dependency = (Dependency) node.dependencies.get(j);
                    dependency.node.animation.addListener(new DependencyListener(this, node, dependency.rule));
                }
                node.tmpDependencies = (ArrayList) node.dependencies.clone();
            }
            node.animation.addListener(this.mSetListener);
        }
        if (this.mStartDelay <= 0) {
            i$ = nodesToStart.iterator();
            while (i$.hasNext()) {
                node = (Node) i$.next();
                node.animation.start();
                this.mPlayingSet.add(node.animation);
            }
        } else {
            float[] fArr = new float[2];
            this.mDelayAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mDelayAnim.setDuration(this.mStartDelay);
            this.mDelayAnim.addListener(new AnimatorListenerAdapter() {
                boolean canceled = false;

                public void onAnimationCancel(Animator anim) {
                    this.canceled = true;
                }

                public void onAnimationEnd(Animator anim) {
                    if (!this.canceled) {
                        int numNodes = nodesToStart.size();
                        for (int i = 0; i < numNodes; i++) {
                            Node node = (Node) nodesToStart.get(i);
                            node.animation.start();
                            AnimatorSet.this.mPlayingSet.add(node.animation);
                        }
                    }
                    AnimatorSet.this.mDelayAnim = null;
                }
            });
            this.mDelayAnim.start();
        }
        if (this.mListeners != null) {
            tmpListeners = (ArrayList) this.mListeners.clone();
            numListeners = tmpListeners.size();
            for (i = 0; i < numListeners; i++) {
                ((AnimatorListener) tmpListeners.get(i)).onAnimationStart(this);
            }
        }
        if (this.mNodes.size() == 0 && this.mStartDelay == 0) {
            this.mStarted = false;
            if (this.mListeners != null) {
                tmpListeners = (ArrayList) this.mListeners.clone();
                numListeners = tmpListeners.size();
                for (i = 0; i < numListeners; i++) {
                    ((AnimatorListener) tmpListeners.get(i)).onAnimationEnd(this);
                }
            }
        }
    }

    public AnimatorSet clone() {
        int n;
        AnimatorSet anim = (AnimatorSet) super.clone();
        int nodeCount = this.mNodes.size();
        anim.mNeedsSort = true;
        anim.mTerminated = false;
        anim.mStarted = false;
        anim.mPlayingSet = new ArrayList();
        anim.mNodeMap = new ArrayMap();
        anim.mNodes = new ArrayList(nodeCount);
        anim.mSortedNodes = new ArrayList(nodeCount);
        anim.mReversible = this.mReversible;
        anim.mSetListener = null;
        for (n = 0; n < nodeCount; n++) {
            int i;
            Node node = (Node) this.mNodes.get(n);
            Node nodeClone = node.clone();
            node.mTmpClone = nodeClone;
            anim.mNodes.add(nodeClone);
            anim.mNodeMap.put(nodeClone.animation, nodeClone);
            nodeClone.dependencies = null;
            nodeClone.tmpDependencies = null;
            nodeClone.nodeDependents = null;
            nodeClone.nodeDependencies = null;
            ArrayList<AnimatorListener> cloneListeners = nodeClone.animation.getListeners();
            if (cloneListeners != null) {
                for (i = cloneListeners.size() - 1; i >= 0; i--) {
                    if (((AnimatorListener) cloneListeners.get(i)) instanceof AnimatorSetListener) {
                        cloneListeners.remove(i);
                    }
                }
            }
        }
        for (n = 0; n < nodeCount; n++) {
            Iterator i$;
            node = (Node) this.mNodes.get(n);
            Node clone = node.mTmpClone;
            if (node.dependencies != null) {
                clone.dependencies = new ArrayList(node.dependencies.size());
                int depSize = node.dependencies.size();
                for (i = 0; i < depSize; i++) {
                    Dependency dependency = (Dependency) node.dependencies.get(i);
                    clone.dependencies.add(new Dependency(dependency.node.mTmpClone, dependency.rule));
                }
            }
            if (node.nodeDependents != null) {
                clone.nodeDependents = new ArrayList(node.nodeDependents.size());
                i$ = node.nodeDependents.iterator();
                while (i$.hasNext()) {
                    clone.nodeDependents.add(((Node) i$.next()).mTmpClone);
                }
            }
            if (node.nodeDependencies != null) {
                clone.nodeDependencies = new ArrayList(node.nodeDependencies.size());
                i$ = node.nodeDependencies.iterator();
                while (i$.hasNext()) {
                    clone.nodeDependencies.add(((Node) i$.next()).mTmpClone);
                }
            }
        }
        for (n = 0; n < nodeCount; n++) {
            ((Node) this.mNodes.get(n)).mTmpClone = null;
        }
        return anim;
    }

    private void sortNodes() {
        int numNodes;
        int i;
        Node node;
        int j;
        if (this.mNeedsSort) {
            this.mSortedNodes.clear();
            ArrayList<Node> roots = new ArrayList();
            numNodes = this.mNodes.size();
            for (i = 0; i < numNodes; i++) {
                node = (Node) this.mNodes.get(i);
                if (node.dependencies == null || node.dependencies.size() == 0) {
                    roots.add(node);
                }
            }
            ArrayList<Node> tmpRoots = new ArrayList();
            while (roots.size() > 0) {
                int numRoots = roots.size();
                for (i = 0; i < numRoots; i++) {
                    Node root = (Node) roots.get(i);
                    this.mSortedNodes.add(root);
                    if (root.nodeDependents != null) {
                        int numDependents = root.nodeDependents.size();
                        for (j = 0; j < numDependents; j++) {
                            node = (Node) root.nodeDependents.get(j);
                            node.nodeDependencies.remove(root);
                            if (node.nodeDependencies.size() == 0) {
                                tmpRoots.add(node);
                            }
                        }
                    }
                }
                roots.clear();
                roots.addAll(tmpRoots);
                tmpRoots.clear();
            }
            this.mNeedsSort = false;
            if (this.mSortedNodes.size() != this.mNodes.size()) {
                throw new IllegalStateException("Circular dependencies cannot exist in AnimatorSet");
            }
            return;
        }
        numNodes = this.mNodes.size();
        for (i = 0; i < numNodes; i++) {
            node = (Node) this.mNodes.get(i);
            if (node.dependencies != null && node.dependencies.size() > 0) {
                int numDependencies = node.dependencies.size();
                for (j = 0; j < numDependencies; j++) {
                    Dependency dependency = (Dependency) node.dependencies.get(j);
                    if (node.nodeDependencies == null) {
                        node.nodeDependencies = new ArrayList();
                    }
                    if (!node.nodeDependencies.contains(dependency.node)) {
                        node.nodeDependencies.add(dependency.node);
                    }
                }
            }
            node.done = false;
        }
    }

    public boolean canReverse() {
        if (!this.mReversible) {
            return false;
        }
        Iterator i$ = this.mNodes.iterator();
        while (i$.hasNext()) {
            Node node = (Node) i$.next();
            if (!node.animation.canReverse()) {
                return false;
            }
            if (node.animation.getStartDelay() > 0) {
                return false;
            }
        }
        return true;
    }

    public void reverse() {
        if (canReverse()) {
            Iterator i$ = this.mNodes.iterator();
            while (i$.hasNext()) {
                ((Node) i$.next()).animation.reverse();
            }
        }
    }

    public String toString() {
        String returnVal = "AnimatorSet@" + Integer.toHexString(hashCode()) + "{";
        boolean prevNeedsSort = this.mNeedsSort;
        sortNodes();
        this.mNeedsSort = prevNeedsSort;
        Iterator i$ = this.mSortedNodes.iterator();
        while (i$.hasNext()) {
            returnVal = returnVal + "\n    " + ((Node) i$.next()).animation.toString();
        }
        return returnVal + "\n}";
    }
}
