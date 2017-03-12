package android.net;

import com.android.internal.util.StateMachine;

public abstract class BaseDhcpStateMachine extends StateMachine {
    public abstract void doQuit();

    public abstract void registerForPreDhcpNotification();

    protected BaseDhcpStateMachine(String tag) {
        super(tag);
    }
}
