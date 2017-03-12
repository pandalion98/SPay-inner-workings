package android.app;

import android.app.FragmentManager.BackStackEntry;
import android.graphics.Rect;
import android.net.wifi.WifiEnterpriseConfig;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.ArrayMap;
import android.util.Log;
import android.util.LogWriter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import com.android.internal.util.FastPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

final class BackStackRecord extends FragmentTransaction implements BackStackEntry, Runnable {
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SHOW = 5;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack = true;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    Op mHead;
    int mIndex = -1;
    final FragmentManagerImpl mManager;
    String mName;
    int mNumOp;
    int mPopEnterAnim;
    int mPopExitAnim;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    Op mTail;
    int mTransition;
    int mTransitionStyle;

    static final class Op {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        Op next;
        int popEnterAnim;
        int popExitAnim;
        Op prev;
        ArrayList<Fragment> removed;

        Op() {
        }
    }

    public class TransitionState {
        public View enteringEpicenterView;
        public ArrayMap<String, String> nameOverrides = new ArrayMap();
        public View nonExistentView;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mName != null) {
            sb.append(" ");
            sb.append(this.mName);
        }
        sb.append("}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        dump(prefix, writer, true);
    }

    void dump(String prefix, PrintWriter writer, boolean full) {
        if (full) {
            writer.print(prefix);
            writer.print("mName=");
            writer.print(this.mName);
            writer.print(" mIndex=");
            writer.print(this.mIndex);
            writer.print(" mCommitted=");
            writer.println(this.mCommitted);
            if (this.mTransition != 0) {
                writer.print(prefix);
                writer.print("mTransition=#");
                writer.print(Integer.toHexString(this.mTransition));
                writer.print(" mTransitionStyle=#");
                writer.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (!(this.mEnterAnim == 0 && this.mExitAnim == 0)) {
                writer.print(prefix);
                writer.print("mEnterAnim=#");
                writer.print(Integer.toHexString(this.mEnterAnim));
                writer.print(" mExitAnim=#");
                writer.println(Integer.toHexString(this.mExitAnim));
            }
            if (!(this.mPopEnterAnim == 0 && this.mPopExitAnim == 0)) {
                writer.print(prefix);
                writer.print("mPopEnterAnim=#");
                writer.print(Integer.toHexString(this.mPopEnterAnim));
                writer.print(" mPopExitAnim=#");
                writer.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (!(this.mBreadCrumbTitleRes == 0 && this.mBreadCrumbTitleText == null)) {
                writer.print(prefix);
                writer.print("mBreadCrumbTitleRes=#");
                writer.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                writer.print(" mBreadCrumbTitleText=");
                writer.println(this.mBreadCrumbTitleText);
            }
            if (!(this.mBreadCrumbShortTitleRes == 0 && this.mBreadCrumbShortTitleText == null)) {
                writer.print(prefix);
                writer.print("mBreadCrumbShortTitleRes=#");
                writer.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                writer.print(" mBreadCrumbShortTitleText=");
                writer.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (this.mHead != null) {
            writer.print(prefix);
            writer.println("Operations:");
            String innerPrefix = prefix + "    ";
            Op op = this.mHead;
            int num = 0;
            while (op != null) {
                String cmdStr;
                switch (op.cmd) {
                    case 0:
                        cmdStr = WifiEnterpriseConfig.EMPTY_VALUE;
                        break;
                    case 1:
                        cmdStr = "ADD";
                        break;
                    case 2:
                        cmdStr = "REPLACE";
                        break;
                    case 3:
                        cmdStr = "REMOVE";
                        break;
                    case 4:
                        cmdStr = "HIDE";
                        break;
                    case 5:
                        cmdStr = "SHOW";
                        break;
                    case 6:
                        cmdStr = "DETACH";
                        break;
                    case 7:
                        cmdStr = "ATTACH";
                        break;
                    default:
                        cmdStr = "cmd=" + op.cmd;
                        break;
                }
                writer.print(prefix);
                writer.print("  Op #");
                writer.print(num);
                writer.print(": ");
                writer.print(cmdStr);
                writer.print(" ");
                writer.println(op.fragment);
                if (full) {
                    if (!(op.enterAnim == 0 && op.exitAnim == 0)) {
                        writer.print(innerPrefix);
                        writer.print("enterAnim=#");
                        writer.print(Integer.toHexString(op.enterAnim));
                        writer.print(" exitAnim=#");
                        writer.println(Integer.toHexString(op.exitAnim));
                    }
                    if (!(op.popEnterAnim == 0 && op.popExitAnim == 0)) {
                        writer.print(innerPrefix);
                        writer.print("popEnterAnim=#");
                        writer.print(Integer.toHexString(op.popEnterAnim));
                        writer.print(" popExitAnim=#");
                        writer.println(Integer.toHexString(op.popExitAnim));
                    }
                }
                if (op.removed != null && op.removed.size() > 0) {
                    for (int i = 0; i < op.removed.size(); i++) {
                        writer.print(innerPrefix);
                        if (op.removed.size() == 1) {
                            writer.print("Removed: ");
                        } else {
                            if (i == 0) {
                                writer.println("Removed:");
                            }
                            writer.print(innerPrefix);
                            writer.print("  #");
                            writer.print(i);
                            writer.print(": ");
                        }
                        writer.println(op.removed.get(i));
                    }
                }
                op = op.next;
                num++;
            }
        }
    }

    public BackStackRecord(FragmentManagerImpl manager) {
        this.mManager = manager;
    }

    public int getId() {
        return this.mIndex;
    }

    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }

    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }

    void addOp(Op op) {
        if (this.mHead == null) {
            this.mTail = op;
            this.mHead = op;
        } else {
            op.prev = this.mTail;
            this.mTail.next = op;
            this.mTail = op;
        }
        op.enterAnim = this.mEnterAnim;
        op.exitAnim = this.mExitAnim;
        op.popEnterAnim = this.mPopEnterAnim;
        op.popExitAnim = this.mPopExitAnim;
        this.mNumOp++;
    }

    public FragmentTransaction add(Fragment fragment, String tag) {
        doAddOp(0, fragment, tag, 1);
        return this;
    }

    public FragmentTransaction add(int containerViewId, Fragment fragment) {
        doAddOp(containerViewId, fragment, null, 1);
        return this;
    }

    public FragmentTransaction add(int containerViewId, Fragment fragment, String tag) {
        doAddOp(containerViewId, fragment, tag, 1);
        return this;
    }

    private void doAddOp(int containerViewId, Fragment fragment, String tag, int opcmd) {
        fragment.mFragmentManager = this.mManager;
        if (tag != null) {
            if (fragment.mTag == null || tag.equals(fragment.mTag)) {
                fragment.mTag = tag;
            } else {
                throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + tag);
            }
        }
        if (containerViewId != 0) {
            if (fragment.mFragmentId == 0 || fragment.mFragmentId == containerViewId) {
                fragment.mFragmentId = containerViewId;
                fragment.mContainerId = containerViewId;
            } else {
                throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + containerViewId);
            }
        }
        Op op = new Op();
        op.cmd = opcmd;
        op.fragment = fragment;
        addOp(op);
    }

    public FragmentTransaction replace(int containerViewId, Fragment fragment) {
        return replace(containerViewId, fragment, null);
    }

    public FragmentTransaction replace(int containerViewId, Fragment fragment, String tag) {
        if (containerViewId == 0) {
            throw new IllegalArgumentException("Must use non-zero containerViewId");
        }
        doAddOp(containerViewId, fragment, tag, 2);
        return this;
    }

    public FragmentTransaction remove(Fragment fragment) {
        Op op = new Op();
        op.cmd = 3;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction hide(Fragment fragment) {
        Op op = new Op();
        op.cmd = 4;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction show(Fragment fragment) {
        Op op = new Op();
        op.cmd = 5;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction detach(Fragment fragment) {
        Op op = new Op();
        op.cmd = 6;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction attach(Fragment fragment) {
        Op op = new Op();
        op.cmd = 7;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction setCustomAnimations(int enter, int exit) {
        return setCustomAnimations(enter, exit, 0, 0);
    }

    public FragmentTransaction setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
        this.mEnterAnim = enter;
        this.mExitAnim = exit;
        this.mPopEnterAnim = popEnter;
        this.mPopExitAnim = popExit;
        return this;
    }

    public FragmentTransaction setTransition(int transition) {
        this.mTransition = transition;
        return this;
    }

    public FragmentTransaction addSharedElement(View sharedElement, String name) {
        String transitionName = sharedElement.getTransitionName();
        if (transitionName == null) {
            throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
        }
        if (this.mSharedElementSourceNames == null) {
            this.mSharedElementSourceNames = new ArrayList();
            this.mSharedElementTargetNames = new ArrayList();
        }
        this.mSharedElementSourceNames.add(transitionName);
        this.mSharedElementTargetNames.add(name);
        return this;
    }

    public FragmentTransaction setTransitionStyle(int styleRes) {
        this.mTransitionStyle = styleRes;
        return this;
    }

    public FragmentTransaction addToBackStack(String name) {
        if (this.mAllowAddToBackStack) {
            this.mAddToBackStack = true;
            this.mName = name;
            return this;
        }
        throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
    }

    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }

    public FragmentTransaction disallowAddToBackStack() {
        if (this.mAddToBackStack) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        }
        this.mAllowAddToBackStack = false;
        return this;
    }

    public FragmentTransaction setBreadCrumbTitle(int res) {
        this.mBreadCrumbTitleRes = res;
        this.mBreadCrumbTitleText = null;
        return this;
    }

    public FragmentTransaction setBreadCrumbTitle(CharSequence text) {
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = text;
        return this;
    }

    public FragmentTransaction setBreadCrumbShortTitle(int res) {
        this.mBreadCrumbShortTitleRes = res;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }

    public FragmentTransaction setBreadCrumbShortTitle(CharSequence text) {
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = text;
        return this;
    }

    void bumpBackStackNesting(int amt) {
        if (this.mAddToBackStack) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v(TAG, "Bump nesting in " + this + " by " + amt);
            }
            for (Op op = this.mHead; op != null; op = op.next) {
                if (op.fragment != null) {
                    Fragment fragment = op.fragment;
                    fragment.mBackStackNesting += amt;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v(TAG, "Bump nesting of " + op.fragment + " to " + op.fragment.mBackStackNesting);
                    }
                }
                if (op.removed != null) {
                    for (int i = op.removed.size() - 1; i >= 0; i--) {
                        Fragment r = (Fragment) op.removed.get(i);
                        r.mBackStackNesting += amt;
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v(TAG, "Bump nesting of " + r + " to " + r.mBackStackNesting);
                        }
                    }
                }
            }
        }
    }

    public int commit() {
        return commitInternal(false);
    }

    public int commitAllowingStateLoss() {
        return commitInternal(true);
    }

    int commitInternal(boolean allowStateLoss) {
        if (this.mCommitted) {
            throw new IllegalStateException("commit already called");
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "Commit: " + this);
            PrintWriter pw = new FastPrintWriter(new LogWriter(2, TAG), false, 1024);
            dump("  ", null, pw, null);
            pw.flush();
        }
        this.mCommitted = true;
        if (this.mAddToBackStack) {
            this.mIndex = this.mManager.allocBackStackIndex(this);
        } else {
            this.mIndex = -1;
        }
        this.mManager.enqueueAction(this, allowStateLoss);
        return this.mIndex;
    }

    public void run() {
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "Run: " + this);
        }
        if (!this.mAddToBackStack || this.mIndex >= 0) {
            bumpBackStackNesting(1);
            SparseArray<Fragment> firstOutFragments = new SparseArray();
            SparseArray<Fragment> lastInFragments = new SparseArray();
            calculateFragments(firstOutFragments, lastInFragments);
            beginTransition(firstOutFragments, lastInFragments, false);
            for (Op op = this.mHead; op != null; op = op.next) {
                Fragment f;
                switch (op.cmd) {
                    case 1:
                        f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        this.mManager.addFragment(f, false);
                        break;
                    case 2:
                        f = op.fragment;
                        int containerId = f.mContainerId;
                        if (this.mManager.mAdded != null) {
                            for (int i = 0; i < this.mManager.mAdded.size(); i++) {
                                Fragment old = (Fragment) this.mManager.mAdded.get(i);
                                if (FragmentManagerImpl.DEBUG) {
                                    Log.v(TAG, "OP_REPLACE: adding=" + f + " old=" + old);
                                }
                                if (old.mContainerId == containerId) {
                                    if (old == f) {
                                        f = null;
                                        op.fragment = null;
                                    } else {
                                        if (op.removed == null) {
                                            op.removed = new ArrayList();
                                        }
                                        op.removed.add(old);
                                        old.mNextAnim = op.exitAnim;
                                        if (this.mAddToBackStack) {
                                            old.mBackStackNesting++;
                                            if (FragmentManagerImpl.DEBUG) {
                                                Log.v(TAG, "Bump nesting of " + old + " to " + old.mBackStackNesting);
                                            }
                                        }
                                        this.mManager.removeFragment(old, this.mTransition, this.mTransitionStyle);
                                    }
                                }
                            }
                        }
                        if (f == null) {
                            break;
                        }
                        f.mNextAnim = op.enterAnim;
                        this.mManager.addFragment(f, false);
                        break;
                    case 3:
                        f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        this.mManager.removeFragment(f, this.mTransition, this.mTransitionStyle);
                        break;
                    case 4:
                        f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        this.mManager.hideFragment(f, this.mTransition, this.mTransitionStyle);
                        break;
                    case 5:
                        f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        this.mManager.showFragment(f, this.mTransition, this.mTransitionStyle);
                        break;
                    case 6:
                        f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        this.mManager.detachFragment(f, this.mTransition, this.mTransitionStyle);
                        break;
                    case 7:
                        f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        this.mManager.attachFragment(f, this.mTransition, this.mTransitionStyle);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown cmd: " + op.cmd);
                }
            }
            this.mManager.moveToState(this.mManager.mCurState, this.mTransition, this.mTransitionStyle, true);
            if (this.mAddToBackStack) {
                this.mManager.addBackStackState(this);
                return;
            }
            return;
        }
        throw new IllegalStateException("addToBackStack() called after commit()");
    }

    private static void setFirstOut(SparseArray<Fragment> fragments, Fragment fragment) {
        if (fragment != null) {
            int containerId = fragment.mContainerId;
            if (containerId != 0 && !fragment.isHidden() && fragment.isAdded() && fragment.getView() != null && fragments.get(containerId) == null) {
                fragments.put(containerId, fragment);
            }
        }
    }

    private void setLastIn(SparseArray<Fragment> fragments, Fragment fragment) {
        if (fragment != null) {
            int containerId = fragment.mContainerId;
            if (containerId != 0) {
                fragments.put(containerId, fragment);
            }
        }
    }

    private void calculateFragments(SparseArray<Fragment> firstOutFragments, SparseArray<Fragment> lastInFragments) {
        if (this.mManager.mContainer.onHasView()) {
            for (Op op = this.mHead; op != null; op = op.next) {
                switch (op.cmd) {
                    case 1:
                        setLastIn(lastInFragments, op.fragment);
                        break;
                    case 2:
                        Fragment f = op.fragment;
                        if (this.mManager.mAdded != null) {
                            for (int i = 0; i < this.mManager.mAdded.size(); i++) {
                                Fragment old = (Fragment) this.mManager.mAdded.get(i);
                                if (f == null || old.mContainerId == f.mContainerId) {
                                    if (old == f) {
                                        f = null;
                                    } else {
                                        setFirstOut(firstOutFragments, old);
                                    }
                                }
                            }
                        }
                        setLastIn(lastInFragments, f);
                        break;
                    case 3:
                        setFirstOut(firstOutFragments, op.fragment);
                        break;
                    case 4:
                        setFirstOut(firstOutFragments, op.fragment);
                        break;
                    case 5:
                        setLastIn(lastInFragments, op.fragment);
                        break;
                    case 6:
                        setFirstOut(firstOutFragments, op.fragment);
                        break;
                    case 7:
                        setLastIn(lastInFragments, op.fragment);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void calculateBackFragments(SparseArray<Fragment> firstOutFragments, SparseArray<Fragment> lastInFragments) {
        if (this.mManager.mContainer.onHasView()) {
            for (Op op = this.mHead; op != null; op = op.next) {
                switch (op.cmd) {
                    case 1:
                        setFirstOut(firstOutFragments, op.fragment);
                        break;
                    case 2:
                        if (op.removed != null) {
                            for (int i = op.removed.size() - 1; i >= 0; i--) {
                                setLastIn(lastInFragments, (Fragment) op.removed.get(i));
                            }
                        }
                        setFirstOut(firstOutFragments, op.fragment);
                        break;
                    case 3:
                        setLastIn(lastInFragments, op.fragment);
                        break;
                    case 4:
                        setLastIn(lastInFragments, op.fragment);
                        break;
                    case 5:
                        setFirstOut(firstOutFragments, op.fragment);
                        break;
                    case 6:
                        setLastIn(lastInFragments, op.fragment);
                        break;
                    case 7:
                        setFirstOut(firstOutFragments, op.fragment);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private TransitionState beginTransition(SparseArray<Fragment> firstOutFragments, SparseArray<Fragment> lastInFragments, boolean isBack) {
        int i;
        TransitionState state = new TransitionState();
        state.nonExistentView = new View(this.mManager.mHost.getContext());
        for (i = 0; i < firstOutFragments.size(); i++) {
            configureTransitions(firstOutFragments.keyAt(i), state, isBack, firstOutFragments, lastInFragments);
        }
        for (i = 0; i < lastInFragments.size(); i++) {
            int containerId = lastInFragments.keyAt(i);
            if (firstOutFragments.get(containerId) == null) {
                configureTransitions(containerId, state, isBack, firstOutFragments, lastInFragments);
            }
        }
        return state;
    }

    private static Transition cloneTransition(Transition transition) {
        if (transition != null) {
            return transition.clone();
        }
        return transition;
    }

    private static Transition getEnterTransition(Fragment inFragment, boolean isBack) {
        if (inFragment == null) {
            return null;
        }
        return cloneTransition(isBack ? inFragment.getReenterTransition() : inFragment.getEnterTransition());
    }

    private static Transition getExitTransition(Fragment outFragment, boolean isBack) {
        if (outFragment == null) {
            return null;
        }
        return cloneTransition(isBack ? outFragment.getReturnTransition() : outFragment.getExitTransition());
    }

    private static TransitionSet getSharedElementTransition(Fragment inFragment, Fragment outFragment, boolean isBack) {
        if (inFragment == null || outFragment == null) {
            return null;
        }
        Transition transition = cloneTransition(isBack ? outFragment.getSharedElementReturnTransition() : inFragment.getSharedElementEnterTransition());
        if (transition == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(transition);
        return transitionSet;
    }

    private static ArrayList<View> captureExitingViews(Transition exitTransition, Fragment outFragment, ArrayMap<String, View> namedViews, View nonExistentView) {
        ArrayList<View> viewList = null;
        if (exitTransition != null) {
            viewList = new ArrayList();
            outFragment.getView().captureTransitioningViews(viewList);
            if (namedViews != null) {
                viewList.removeAll(namedViews.values());
            }
            if (!viewList.isEmpty()) {
                viewList.add(nonExistentView);
                addTargets(exitTransition, viewList);
            }
        }
        return viewList;
    }

    private ArrayMap<String, View> remapSharedElements(TransitionState state, Fragment outFragment, boolean isBack) {
        ArrayMap namedViews = new ArrayMap();
        if (this.mSharedElementSourceNames != null) {
            outFragment.getView().findNamedViews(namedViews);
            if (isBack) {
                namedViews.retainAll(this.mSharedElementTargetNames);
            } else {
                namedViews = remapNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames, namedViews);
            }
        }
        if (isBack) {
            outFragment.mEnterTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, namedViews);
            setBackNameOverrides(state, namedViews, false);
        } else {
            outFragment.mExitTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, namedViews);
            setNameOverrides(state, namedViews, false);
        }
        return namedViews;
    }

    private ArrayList<View> addTransitionTargets(TransitionState state, Transition enterTransition, TransitionSet sharedElementTransition, Transition overallTransition, View container, Fragment inFragment, Fragment outFragment, ArrayList<View> hiddenFragmentViews, boolean isBack, ArrayList<View> sharedElementTargets) {
        if (enterTransition == null && sharedElementTransition == null && overallTransition == null) {
            return null;
        }
        final ArrayList<View> enteringViews = new ArrayList();
        final View view = container;
        final Fragment fragment = inFragment;
        final ArrayList<View> arrayList = hiddenFragmentViews;
        final Transition transition = overallTransition;
        final TransitionSet transitionSet = sharedElementTransition;
        final TransitionState transitionState = state;
        final boolean z = isBack;
        final ArrayList<View> arrayList2 = sharedElementTargets;
        final Fragment fragment2 = outFragment;
        final Transition transition2 = enterTransition;
        container.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                if (fragment != null) {
                    BackStackRecord.this.excludeHiddenFragments(arrayList, fragment.mContainerId, transition);
                }
                ArrayMap<String, View> namedViews = null;
                if (transitionSet != null) {
                    namedViews = BackStackRecord.this.mapSharedElementsIn(transitionState, z, fragment);
                    BackStackRecord.removeTargets(transitionSet, arrayList2);
                    BackStackRecord.setSharedElementTargets(transitionSet, transitionState.nonExistentView, namedViews, arrayList2);
                    BackStackRecord.this.setEpicenterIn(namedViews, transitionState);
                    BackStackRecord.this.callSharedElementEnd(transitionState, fragment, fragment2, z, namedViews);
                }
                if (transition2 != null) {
                    transition2.removeTarget(transitionState.nonExistentView);
                    View view = fragment.getView();
                    if (view != null) {
                        view.captureTransitioningViews(enteringViews);
                        if (namedViews != null) {
                            enteringViews.removeAll(namedViews.values());
                        }
                        enteringViews.add(transitionState.nonExistentView);
                        BackStackRecord.addTargets(transition2, enteringViews);
                    }
                    BackStackRecord.this.setSharedElementEpicenter(transition2, transitionState);
                }
                return true;
            }
        });
        return enteringViews;
    }

    private void callSharedElementEnd(TransitionState state, Fragment inFragment, Fragment outFragment, boolean isBack, ArrayMap<String, View> namedViews) {
        (isBack ? outFragment.mEnterTransitionCallback : inFragment.mEnterTransitionCallback).onSharedElementEnd(new ArrayList(namedViews.keySet()), new ArrayList(namedViews.values()), null);
    }

    private void setEpicenterIn(ArrayMap<String, View> namedViews, TransitionState state) {
        if (this.mSharedElementTargetNames != null && !namedViews.isEmpty()) {
            View epicenter = (View) namedViews.get(this.mSharedElementTargetNames.get(0));
            if (epicenter != null) {
                state.enteringEpicenterView = epicenter;
            }
        }
    }

    private ArrayMap<String, View> mapSharedElementsIn(TransitionState state, boolean isBack, Fragment inFragment) {
        ArrayMap namedViews = mapEnteringSharedElements(state, inFragment, isBack);
        if (isBack) {
            inFragment.mExitTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, namedViews);
            setBackNameOverrides(state, namedViews, true);
        } else {
            inFragment.mEnterTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, namedViews);
            setNameOverrides(state, namedViews, true);
        }
        return namedViews;
    }

    private static Transition mergeTransitions(Transition enterTransition, Transition exitTransition, Transition sharedElementTransition, Fragment inFragment, boolean isBack) {
        boolean overlap = true;
        if (!(enterTransition == null || exitTransition == null || inFragment == null)) {
            overlap = isBack ? inFragment.getAllowReturnTransitionOverlap() : inFragment.getAllowEnterTransitionOverlap();
        }
        if (overlap) {
            Transition transitionSet = new TransitionSet();
            if (enterTransition != null) {
                transitionSet.addTransition(enterTransition);
            }
            if (exitTransition != null) {
                transitionSet.addTransition(exitTransition);
            }
            if (sharedElementTransition != null) {
                transitionSet.addTransition(sharedElementTransition);
            }
            return transitionSet;
        }
        Transition staggered = null;
        if (exitTransition != null && enterTransition != null) {
            staggered = new TransitionSet().addTransition(exitTransition).addTransition(enterTransition).setOrdering(1);
        } else if (exitTransition != null) {
            staggered = exitTransition;
        } else if (enterTransition != null) {
            staggered = enterTransition;
        }
        if (sharedElementTransition == null) {
            return staggered;
        }
        Transition together = new TransitionSet();
        if (staggered != null) {
            together.addTransition(staggered);
        }
        together.addTransition(sharedElementTransition);
        return together;
    }

    private void configureTransitions(int containerId, TransitionState state, boolean isBack, SparseArray<Fragment> firstOutFragments, SparseArray<Fragment> lastInFragments) {
        ViewGroup sceneRoot = (ViewGroup) this.mManager.mContainer.onFindViewById(containerId);
        if (sceneRoot != null) {
            Fragment inFragment = (Fragment) lastInFragments.get(containerId);
            Fragment outFragment = (Fragment) firstOutFragments.get(containerId);
            Transition enterTransition = getEnterTransition(inFragment, isBack);
            Transition sharedElementTransition = getSharedElementTransition(inFragment, outFragment, isBack);
            Transition exitTransition = getExitTransition(outFragment, isBack);
            if (enterTransition != null || sharedElementTransition != null || exitTransition != null) {
                if (enterTransition != null) {
                    enterTransition.addTarget(state.nonExistentView);
                }
                ArrayMap<String, View> namedViews = null;
                ArrayList<View> sharedElementTargets = new ArrayList();
                if (sharedElementTransition != null) {
                    namedViews = remapSharedElements(state, outFragment, isBack);
                    setSharedElementTargets(sharedElementTransition, state.nonExistentView, namedViews, sharedElementTargets);
                    (isBack ? outFragment.mEnterTransitionCallback : inFragment.mEnterTransitionCallback).onSharedElementStart(new ArrayList(namedViews.keySet()), new ArrayList(namedViews.values()), null);
                }
                ArrayList<View> exitingViews = captureExitingViews(exitTransition, outFragment, namedViews, state.nonExistentView);
                if (exitingViews == null || exitingViews.isEmpty()) {
                    exitTransition = null;
                }
                if (!(this.mSharedElementTargetNames == null || namedViews == null)) {
                    View epicenterView = (View) namedViews.get(this.mSharedElementTargetNames.get(0));
                    if (epicenterView != null) {
                        if (exitTransition != null) {
                            setEpicenter(exitTransition, epicenterView);
                        }
                        if (sharedElementTransition != null) {
                            setEpicenter(sharedElementTransition, epicenterView);
                        }
                    }
                }
                Transition transition = mergeTransitions(enterTransition, exitTransition, sharedElementTransition, inFragment, isBack);
                if (transition != null) {
                    ArrayList<View> hiddenFragments = new ArrayList();
                    ArrayList<View> enteringViews = addTransitionTargets(state, enterTransition, sharedElementTransition, transition, sceneRoot, inFragment, outFragment, hiddenFragments, isBack, sharedElementTargets);
                    transition.setNameOverrides(state.nameOverrides);
                    transition.excludeTarget(state.nonExistentView, true);
                    excludeHiddenFragments(hiddenFragments, containerId, transition);
                    TransitionManager.beginDelayedTransition(sceneRoot, transition);
                    removeTargetedViewsFromTransitions(sceneRoot, state.nonExistentView, enterTransition, enteringViews, exitTransition, exitingViews, sharedElementTransition, sharedElementTargets, transition, hiddenFragments);
                }
            }
        }
    }

    private static void setSharedElementTargets(TransitionSet transition, View nonExistentView, ArrayMap<String, View> namedViews, ArrayList<View> sharedElementTargets) {
        sharedElementTargets.clear();
        sharedElementTargets.addAll(namedViews.values());
        List<View> views = transition.getTargets();
        views.clear();
        int count = sharedElementTargets.size();
        for (int i = 0; i < count; i++) {
            bfsAddViewChildren(views, (View) sharedElementTargets.get(i));
        }
        sharedElementTargets.add(nonExistentView);
        addTargets(transition, sharedElementTargets);
    }

    private static void bfsAddViewChildren(List<View> views, View startView) {
        int startIndex = views.size();
        if (!containedBeforeIndex(views, startView, startIndex)) {
            views.add(startView);
            for (int index = startIndex; index < views.size(); index++) {
                View view = (View) views.get(index);
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int childCount = viewGroup.getChildCount();
                    for (int childIndex = 0; childIndex < childCount; childIndex++) {
                        View child = viewGroup.getChildAt(childIndex);
                        if (!containedBeforeIndex(views, child, startIndex)) {
                            views.add(child);
                        }
                    }
                }
            }
        }
    }

    private static boolean containedBeforeIndex(List<View> views, View view, int maxIndex) {
        for (int i = 0; i < maxIndex; i++) {
            if (views.get(i) == view) {
                return true;
            }
        }
        return false;
    }

    private void removeTargetedViewsFromTransitions(ViewGroup sceneRoot, View nonExistingView, Transition enterTransition, ArrayList<View> enteringViews, Transition exitTransition, ArrayList<View> exitingViews, Transition sharedElementTransition, ArrayList<View> sharedElementTargets, Transition overallTransition, ArrayList<View> hiddenViews) {
        if (overallTransition != null) {
            final ViewGroup viewGroup = sceneRoot;
            final Transition transition = enterTransition;
            final ArrayList<View> arrayList = enteringViews;
            final Transition transition2 = exitTransition;
            final ArrayList<View> arrayList2 = exitingViews;
            final Transition transition3 = sharedElementTransition;
            final ArrayList<View> arrayList3 = sharedElementTargets;
            final ArrayList<View> arrayList4 = hiddenViews;
            final Transition transition4 = overallTransition;
            final View view = nonExistingView;
            sceneRoot.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    viewGroup.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (transition != null) {
                        BackStackRecord.removeTargets(transition, arrayList);
                    }
                    if (transition2 != null) {
                        BackStackRecord.removeTargets(transition2, arrayList2);
                    }
                    if (transition3 != null) {
                        BackStackRecord.removeTargets(transition3, arrayList3);
                    }
                    int numViews = arrayList4.size();
                    for (int i = 0; i < numViews; i++) {
                        transition4.excludeTarget((View) arrayList4.get(i), false);
                    }
                    transition4.excludeTarget(view, false);
                    return true;
                }
            });
        }
    }

    public static void removeTargets(Transition transition, ArrayList<View> views) {
        int i;
        if (transition instanceof TransitionSet) {
            TransitionSet set = (TransitionSet) transition;
            int numTransitions = set.getTransitionCount();
            for (i = 0; i < numTransitions; i++) {
                removeTargets(set.getTransitionAt(i), views);
            }
        } else if (!hasSimpleTarget(transition)) {
            List<View> targets = transition.getTargets();
            if (targets != null && targets.size() == views.size() && targets.containsAll(views)) {
                for (i = views.size() - 1; i >= 0; i--) {
                    transition.removeTarget((View) views.get(i));
                }
            }
        }
    }

    public static void addTargets(Transition transition, ArrayList<View> views) {
        int i;
        if (transition instanceof TransitionSet) {
            TransitionSet set = (TransitionSet) transition;
            int numTransitions = set.getTransitionCount();
            for (i = 0; i < numTransitions; i++) {
                addTargets(set.getTransitionAt(i), views);
            }
        } else if (!hasSimpleTarget(transition) && isNullOrEmpty(transition.getTargets())) {
            int numViews = views.size();
            for (i = 0; i < numViews; i++) {
                transition.addTarget((View) views.get(i));
            }
        }
    }

    private static boolean hasSimpleTarget(Transition transition) {
        return (isNullOrEmpty(transition.getTargetIds()) && isNullOrEmpty(transition.getTargetNames()) && isNullOrEmpty(transition.getTargetTypes())) ? false : true;
    }

    private static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    private static ArrayMap<String, View> remapNames(ArrayList<String> inMap, ArrayList<String> toGoInMap, ArrayMap<String, View> namedViews) {
        ArrayMap<String, View> remappedViews = new ArrayMap();
        if (!namedViews.isEmpty()) {
            int numKeys = inMap.size();
            for (int i = 0; i < numKeys; i++) {
                View view = (View) namedViews.get(inMap.get(i));
                if (view != null) {
                    remappedViews.put(toGoInMap.get(i), view);
                }
            }
        }
        return remappedViews;
    }

    private ArrayMap<String, View> mapEnteringSharedElements(TransitionState state, Fragment inFragment, boolean isBack) {
        ArrayMap<String, View> namedViews = new ArrayMap();
        View root = inFragment.getView();
        if (root == null || this.mSharedElementSourceNames == null) {
            return namedViews;
        }
        root.findNamedViews(namedViews);
        if (isBack) {
            return remapNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames, namedViews);
        }
        namedViews.retainAll(this.mSharedElementTargetNames);
        return namedViews;
    }

    private void excludeHiddenFragments(ArrayList<View> hiddenFragmentViews, int containerId, Transition transition) {
        if (this.mManager.mAdded != null) {
            for (int i = 0; i < this.mManager.mAdded.size(); i++) {
                Fragment fragment = (Fragment) this.mManager.mAdded.get(i);
                if (!(fragment.mView == null || fragment.mContainer == null || fragment.mContainerId != containerId)) {
                    if (!fragment.mHidden) {
                        transition.excludeTarget(fragment.mView, false);
                        hiddenFragmentViews.remove(fragment.mView);
                    } else if (!hiddenFragmentViews.contains(fragment.mView)) {
                        transition.excludeTarget(fragment.mView, true);
                        hiddenFragmentViews.add(fragment.mView);
                    }
                }
            }
        }
    }

    private static void setEpicenter(Transition transition, View view) {
        final Rect epicenter = new Rect();
        view.getBoundsOnScreen(epicenter);
        transition.setEpicenterCallback(new EpicenterCallback() {
            public Rect onGetEpicenter(Transition transition) {
                return epicenter;
            }
        });
    }

    private void setSharedElementEpicenter(Transition transition, final TransitionState state) {
        transition.setEpicenterCallback(new EpicenterCallback() {
            private Rect mEpicenter;

            public Rect onGetEpicenter(Transition transition) {
                if (this.mEpicenter == null && state.enteringEpicenterView != null) {
                    this.mEpicenter = new Rect();
                    state.enteringEpicenterView.getBoundsOnScreen(this.mEpicenter);
                }
                return this.mEpicenter;
            }
        });
    }

    public TransitionState popFromBackStack(boolean doStateMove, TransitionState state, SparseArray<Fragment> firstOutFragments, SparseArray<Fragment> lastInFragments) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "popFromBackStack: " + this);
            PrintWriter pw = new FastPrintWriter(new LogWriter(2, TAG), false, 1024);
            dump("  ", null, pw, null);
            pw.flush();
        }
        if (state == null) {
            if (!(firstOutFragments.size() == 0 && lastInFragments.size() == 0)) {
                state = beginTransition(firstOutFragments, lastInFragments, true);
            }
        } else if (!doStateMove) {
            setNameOverrides(state, this.mSharedElementTargetNames, this.mSharedElementSourceNames);
        }
        bumpBackStackNesting(-1);
        for (Op op = this.mTail; op != null; op = op.prev) {
            Fragment f;
            switch (op.cmd) {
                case 1:
                    f = op.fragment;
                    f.mNextAnim = op.popExitAnim;
                    this.mManager.removeFragment(f, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                case 2:
                    f = op.fragment;
                    if (f != null) {
                        f.mNextAnim = op.popExitAnim;
                        this.mManager.removeFragment(f, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    }
                    if (op.removed == null) {
                        break;
                    }
                    for (int i = 0; i < op.removed.size(); i++) {
                        Fragment old = (Fragment) op.removed.get(i);
                        old.mNextAnim = op.popEnterAnim;
                        this.mManager.addFragment(old, false);
                    }
                    break;
                case 3:
                    f = op.fragment;
                    f.mNextAnim = op.popEnterAnim;
                    this.mManager.addFragment(f, false);
                    break;
                case 4:
                    f = op.fragment;
                    f.mNextAnim = op.popEnterAnim;
                    this.mManager.showFragment(f, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                case 5:
                    f = op.fragment;
                    f.mNextAnim = op.popExitAnim;
                    this.mManager.hideFragment(f, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                case 6:
                    f = op.fragment;
                    f.mNextAnim = op.popEnterAnim;
                    this.mManager.attachFragment(f, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                case 7:
                    f = op.fragment;
                    f.mNextAnim = op.popExitAnim;
                    this.mManager.detachFragment(f, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + op.cmd);
            }
        }
        if (doStateMove) {
            this.mManager.moveToState(this.mManager.mCurState, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle, true);
            state = null;
        }
        if (this.mIndex >= 0) {
            this.mManager.freeBackStackIndex(this.mIndex);
            this.mIndex = -1;
        }
        return state;
    }

    private static void setNameOverride(ArrayMap<String, String> overrides, String source, String target) {
        if (source != null && target != null && !source.equals(target)) {
            for (int index = 0; index < overrides.size(); index++) {
                if (source.equals(overrides.valueAt(index))) {
                    overrides.setValueAt(index, target);
                    return;
                }
            }
            overrides.put(source, target);
        }
    }

    private static void setNameOverrides(TransitionState state, ArrayList<String> sourceNames, ArrayList<String> targetNames) {
        if (sourceNames != null && targetNames != null) {
            for (int i = 0; i < sourceNames.size(); i++) {
                setNameOverride(state.nameOverrides, (String) sourceNames.get(i), (String) targetNames.get(i));
            }
        }
    }

    private void setBackNameOverrides(TransitionState state, ArrayMap<String, View> namedViews, boolean isEnd) {
        int sourceCount;
        int targetCount = this.mSharedElementTargetNames == null ? 0 : this.mSharedElementTargetNames.size();
        if (this.mSharedElementSourceNames == null) {
            sourceCount = 0;
        } else {
            sourceCount = this.mSharedElementSourceNames.size();
        }
        int count = Math.min(targetCount, sourceCount);
        for (int i = 0; i < count; i++) {
            String source = (String) this.mSharedElementSourceNames.get(i);
            View view = (View) namedViews.get((String) this.mSharedElementTargetNames.get(i));
            if (view != null) {
                String target = view.getTransitionName();
                if (isEnd) {
                    setNameOverride(state.nameOverrides, source, target);
                } else {
                    setNameOverride(state.nameOverrides, target, source);
                }
            }
        }
    }

    private void setNameOverrides(TransitionState state, ArrayMap<String, View> namedViews, boolean isEnd) {
        int count = namedViews == null ? 0 : namedViews.size();
        for (int i = 0; i < count; i++) {
            String source = (String) namedViews.keyAt(i);
            String target = ((View) namedViews.valueAt(i)).getTransitionName();
            if (isEnd) {
                setNameOverride(state.nameOverrides, source, target);
            } else {
                setNameOverride(state.nameOverrides, target, source);
            }
        }
    }

    public String getName() {
        return this.mName;
    }

    public int getTransition() {
        return this.mTransition;
    }

    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }

    public boolean isEmpty() {
        return this.mNumOp == 0;
    }
}
