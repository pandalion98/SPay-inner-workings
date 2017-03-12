package android.view.textservice;

import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.textservice.ISpellCheckerSession;
import com.android.internal.textservice.ISpellCheckerSessionListener;
import com.android.internal.textservice.ITextServicesManager;
import com.android.internal.textservice.ITextServicesSessionListener;
import com.android.internal.textservice.ITextServicesSessionListener.Stub;
import java.util.LinkedList;
import java.util.Queue;

public class SpellCheckerSession {
    private static final boolean DBG = false;
    private static final int MSG_ON_GET_SUGGESTION_MULTIPLE = 1;
    private static final int MSG_ON_GET_SUGGESTION_MULTIPLE_FOR_SENTENCE = 2;
    public static final String SERVICE_META_DATA = "android.view.textservice.scs";
    private static final String TAG = SpellCheckerSession.class.getSimpleName();
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SpellCheckerSession.this.handleOnGetSuggestionsMultiple((SuggestionsInfo[]) msg.obj);
                    return;
                case 2:
                    SpellCheckerSession.this.handleOnGetSentenceSuggestionsMultiple((SentenceSuggestionsInfo[]) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    private final InternalListener mInternalListener;
    private boolean mIsUsed;
    private final SpellCheckerInfo mSpellCheckerInfo;
    private final SpellCheckerSessionListener mSpellCheckerSessionListener;
    private final SpellCheckerSessionListenerImpl mSpellCheckerSessionListenerImpl;
    private final SpellCheckerSubtype mSubtype;
    private final ITextServicesManager mTextServicesManager;

    private static class InternalListener extends Stub {
        private final SpellCheckerSessionListenerImpl mParentSpellCheckerSessionListenerImpl;

        public InternalListener(SpellCheckerSessionListenerImpl spellCheckerSessionListenerImpl) {
            this.mParentSpellCheckerSessionListenerImpl = spellCheckerSessionListenerImpl;
        }

        public void onServiceConnected(ISpellCheckerSession session) {
            this.mParentSpellCheckerSessionListenerImpl.onServiceConnected(session);
        }
    }

    public interface SpellCheckerSessionListener {
        void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfoArr);

        void onGetSuggestions(SuggestionsInfo[] suggestionsInfoArr);
    }

    private static class SpellCheckerSessionListenerImpl extends ISpellCheckerSessionListener.Stub {
        private static final int STATE_CLOSED_AFTER_CONNECTION = 2;
        private static final int STATE_CLOSED_BEFORE_CONNECTION = 3;
        private static final int STATE_CONNECTED = 1;
        private static final int STATE_WAIT_CONNECTION = 0;
        private static final int TASK_CANCEL = 1;
        private static final int TASK_CLOSE = 3;
        private static final int TASK_GET_SUGGESTIONS_MULTIPLE = 2;
        private static final int TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE = 4;
        private Handler mAsyncHandler;
        private Handler mHandler;
        private ISpellCheckerSession mISpellCheckerSession;
        private final Queue<SpellCheckerParams> mPendingTasks = new LinkedList();
        private int mState = 0;
        private HandlerThread mThread;

        private static class SpellCheckerParams {
            public final boolean mSequentialWords;
            public ISpellCheckerSession mSession;
            public final int mSuggestionsLimit;
            public final TextInfo[] mTextInfos;
            public final int mWhat;

            public SpellCheckerParams(int what, TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
                this.mWhat = what;
                this.mTextInfos = textInfos;
                this.mSuggestionsLimit = suggestionsLimit;
                this.mSequentialWords = sequentialWords;
            }
        }

        private static String taskToString(int task) {
            switch (task) {
                case 1:
                    return "TASK_CANCEL";
                case 2:
                    return "TASK_GET_SUGGESTIONS_MULTIPLE";
                case 3:
                    return "TASK_CLOSE";
                case 4:
                    return "TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE";
                default:
                    return "Unexpected task=" + task;
            }
        }

        private static String stateToString(int state) {
            switch (state) {
                case 0:
                    return "STATE_WAIT_CONNECTION";
                case 1:
                    return "STATE_CONNECTED";
                case 2:
                    return "STATE_CLOSED_AFTER_CONNECTION";
                case 3:
                    return "STATE_CLOSED_BEFORE_CONNECTION";
                default:
                    return "Unexpected state=" + state;
            }
        }

        public SpellCheckerSessionListenerImpl(Handler handler) {
            this.mHandler = handler;
        }

        private void processTask(ISpellCheckerSession session, SpellCheckerParams scp, boolean async) {
            if (async || this.mAsyncHandler == null) {
                switch (scp.mWhat) {
                    case 1:
                        try {
                            session.onCancel();
                            break;
                        } catch (RemoteException e) {
                            Log.e(SpellCheckerSession.TAG, "Failed to cancel " + e);
                            break;
                        }
                    case 2:
                        try {
                            session.onGetSuggestionsMultiple(scp.mTextInfos, scp.mSuggestionsLimit, scp.mSequentialWords);
                            break;
                        } catch (RemoteException e2) {
                            Log.e(SpellCheckerSession.TAG, "Failed to get suggestions " + e2);
                            break;
                        }
                    case 3:
                        try {
                            session.onClose();
                            break;
                        } catch (RemoteException e22) {
                            Log.e(SpellCheckerSession.TAG, "Failed to close " + e22);
                            break;
                        }
                    case 4:
                        try {
                            session.onGetSentenceSuggestionsMultiple(scp.mTextInfos, scp.mSuggestionsLimit);
                            break;
                        } catch (RemoteException e222) {
                            Log.e(SpellCheckerSession.TAG, "Failed to get suggestions " + e222);
                            break;
                        }
                }
            }
            scp.mSession = session;
            this.mAsyncHandler.sendMessage(Message.obtain(this.mAsyncHandler, 1, scp));
            if (scp.mWhat == 3) {
                synchronized (this) {
                    processCloseLocked();
                }
            }
        }

        private void processCloseLocked() {
            this.mISpellCheckerSession = null;
            if (this.mThread != null) {
                this.mThread.quit();
            }
            this.mHandler = null;
            this.mPendingTasks.clear();
            this.mThread = null;
            this.mAsyncHandler = null;
            switch (this.mState) {
                case 0:
                    this.mState = 3;
                    return;
                case 1:
                    this.mState = 2;
                    return;
                default:
                    Log.e(SpellCheckerSession.TAG, "processCloseLocked is called unexpectedly. mState=" + stateToString(this.mState));
                    return;
            }
        }

        public synchronized void onServiceConnected(ISpellCheckerSession session) {
            synchronized (this) {
                switch (this.mState) {
                    case 0:
                        if (session != null) {
                            this.mISpellCheckerSession = session;
                            if ((session.asBinder() instanceof Binder) && this.mThread == null) {
                                this.mThread = new HandlerThread("SpellCheckerSession", 10);
                                this.mThread.start();
                                this.mAsyncHandler = new Handler(this.mThread.getLooper()) {
                                    public void handleMessage(Message msg) {
                                        SpellCheckerParams scp = msg.obj;
                                        SpellCheckerSessionListenerImpl.this.processTask(scp.mSession, scp, true);
                                    }
                                };
                            }
                            this.mState = 1;
                            while (!this.mPendingTasks.isEmpty()) {
                                processTask(session, (SpellCheckerParams) this.mPendingTasks.poll(), false);
                            }
                            break;
                        }
                        Log.e(SpellCheckerSession.TAG, "ignoring onServiceConnected due to session=null");
                        break;
                    case 3:
                        break;
                    default:
                        Log.e(SpellCheckerSession.TAG, "ignoring onServiceConnected due to unexpected mState=" + stateToString(this.mState));
                }
            }
        }

        public void cancel() {
            processOrEnqueueTask(new SpellCheckerParams(1, null, 0, false));
        }

        public void getSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
            processOrEnqueueTask(new SpellCheckerParams(2, textInfos, suggestionsLimit, sequentialWords));
        }

        public void getSentenceSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit) {
            processOrEnqueueTask(new SpellCheckerParams(4, textInfos, suggestionsLimit, false));
        }

        public void close() {
            processOrEnqueueTask(new SpellCheckerParams(3, null, 0, false));
        }

        public boolean isDisconnected() {
            boolean z = true;
            synchronized (this) {
                if (this.mState == 1) {
                    z = false;
                }
            }
            return z;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void processOrEnqueueTask(android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams r7) {
            /*
            r6 = this;
            r5 = 3;
            r4 = 1;
            monitor-enter(r6);
            r3 = r6.mState;	 Catch:{ all -> 0x004a }
            if (r3 == 0) goto L_0x003d;
        L_0x0007:
            r3 = r6.mState;	 Catch:{ all -> 0x004a }
            if (r3 == r4) goto L_0x003d;
        L_0x000b:
            r3 = android.view.textservice.SpellCheckerSession.TAG;	 Catch:{ all -> 0x004a }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004a }
            r4.<init>();	 Catch:{ all -> 0x004a }
            r5 = "ignoring processOrEnqueueTask due to unexpected mState=";
            r4 = r4.append(r5);	 Catch:{ all -> 0x004a }
            r5 = r7.mWhat;	 Catch:{ all -> 0x004a }
            r5 = taskToString(r5);	 Catch:{ all -> 0x004a }
            r4 = r4.append(r5);	 Catch:{ all -> 0x004a }
            r5 = " scp.mWhat=";
            r4 = r4.append(r5);	 Catch:{ all -> 0x004a }
            r5 = r7.mWhat;	 Catch:{ all -> 0x004a }
            r5 = taskToString(r5);	 Catch:{ all -> 0x004a }
            r4 = r4.append(r5);	 Catch:{ all -> 0x004a }
            r4 = r4.toString();	 Catch:{ all -> 0x004a }
            android.util.Log.e(r3, r4);	 Catch:{ all -> 0x004a }
            monitor-exit(r6);	 Catch:{ all -> 0x004a }
        L_0x003c:
            return;
        L_0x003d:
            r3 = r6.mState;	 Catch:{ all -> 0x004a }
            if (r3 != 0) goto L_0x0076;
        L_0x0041:
            r3 = r7.mWhat;	 Catch:{ all -> 0x004a }
            if (r3 != r5) goto L_0x004d;
        L_0x0045:
            r6.processCloseLocked();	 Catch:{ all -> 0x004a }
            monitor-exit(r6);	 Catch:{ all -> 0x004a }
            goto L_0x003c;
        L_0x004a:
            r3 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x004a }
            throw r3;
        L_0x004d:
            r0 = 0;
            r3 = r7.mWhat;	 Catch:{ all -> 0x004a }
            if (r3 != r4) goto L_0x0068;
        L_0x0052:
            r3 = r6.mPendingTasks;	 Catch:{ all -> 0x004a }
            r3 = r3.isEmpty();	 Catch:{ all -> 0x004a }
            if (r3 != 0) goto L_0x0068;
        L_0x005a:
            r3 = r6.mPendingTasks;	 Catch:{ all -> 0x004a }
            r2 = r3.poll();	 Catch:{ all -> 0x004a }
            r2 = (android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams) r2;	 Catch:{ all -> 0x004a }
            r3 = r2.mWhat;	 Catch:{ all -> 0x004a }
            if (r3 != r5) goto L_0x0052;
        L_0x0066:
            r0 = r2;
            goto L_0x0052;
        L_0x0068:
            r3 = r6.mPendingTasks;	 Catch:{ all -> 0x004a }
            r3.offer(r7);	 Catch:{ all -> 0x004a }
            if (r0 == 0) goto L_0x0074;
        L_0x006f:
            r3 = r6.mPendingTasks;	 Catch:{ all -> 0x004a }
            r3.offer(r0);	 Catch:{ all -> 0x004a }
        L_0x0074:
            monitor-exit(r6);	 Catch:{ all -> 0x004a }
            goto L_0x003c;
        L_0x0076:
            r1 = r6.mISpellCheckerSession;	 Catch:{ all -> 0x004a }
            monitor-exit(r6);	 Catch:{ all -> 0x004a }
            r3 = 0;
            r6.processTask(r1, r7, r3);
            goto L_0x003c;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.processOrEnqueueTask(android.view.textservice.SpellCheckerSession$SpellCheckerSessionListenerImpl$SpellCheckerParams):void");
        }

        public void onGetSuggestions(SuggestionsInfo[] results) {
            synchronized (this) {
                if (this.mHandler != null) {
                    this.mHandler.sendMessage(Message.obtain(this.mHandler, 1, results));
                }
            }
        }

        public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
            synchronized (this) {
                if (this.mHandler != null) {
                    this.mHandler.sendMessage(Message.obtain(this.mHandler, 2, results));
                }
            }
        }
    }

    public SpellCheckerSession(SpellCheckerInfo info, ITextServicesManager tsm, SpellCheckerSessionListener listener, SpellCheckerSubtype subtype) {
        if (info == null || listener == null || tsm == null) {
            throw new NullPointerException();
        }
        this.mSpellCheckerInfo = info;
        this.mSpellCheckerSessionListenerImpl = new SpellCheckerSessionListenerImpl(this.mHandler);
        this.mInternalListener = new InternalListener(this.mSpellCheckerSessionListenerImpl);
        this.mTextServicesManager = tsm;
        this.mIsUsed = true;
        this.mSpellCheckerSessionListener = listener;
        this.mSubtype = subtype;
    }

    public boolean isSessionDisconnected() {
        return this.mSpellCheckerSessionListenerImpl.isDisconnected();
    }

    public SpellCheckerInfo getSpellChecker() {
        return this.mSpellCheckerInfo;
    }

    public void cancel() {
        this.mSpellCheckerSessionListenerImpl.cancel();
    }

    public void close() {
        this.mIsUsed = false;
        try {
            this.mSpellCheckerSessionListenerImpl.close();
            this.mTextServicesManager.finishSpellCheckerService(this.mSpellCheckerSessionListenerImpl);
        } catch (RemoteException e) {
        }
    }

    public void getSentenceSuggestions(TextInfo[] textInfos, int suggestionsLimit) {
        this.mSpellCheckerSessionListenerImpl.getSentenceSuggestionsMultiple(textInfos, suggestionsLimit);
    }

    @Deprecated
    public void getSuggestions(TextInfo textInfo, int suggestionsLimit) {
        getSuggestions(new TextInfo[]{textInfo}, suggestionsLimit, false);
    }

    @Deprecated
    public void getSuggestions(TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
        this.mSpellCheckerSessionListenerImpl.getSuggestionsMultiple(textInfos, suggestionsLimit, sequentialWords);
    }

    private void handleOnGetSuggestionsMultiple(SuggestionsInfo[] suggestionInfos) {
        this.mSpellCheckerSessionListener.onGetSuggestions(suggestionInfos);
    }

    private void handleOnGetSentenceSuggestionsMultiple(SentenceSuggestionsInfo[] suggestionInfos) {
        this.mSpellCheckerSessionListener.onGetSentenceSuggestions(suggestionInfos);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (this.mIsUsed) {
            Log.e(TAG, "SpellCheckerSession was not finished properly.You should call finishShession() when you finished to use a spell checker.");
            close();
        }
    }

    public ITextServicesSessionListener getTextServicesSessionListener() {
        return this.mInternalListener;
    }

    public ISpellCheckerSessionListener getSpellCheckerSessionListener() {
        return this.mSpellCheckerSessionListenerImpl;
    }
}
