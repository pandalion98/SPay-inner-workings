package com.android.internal.os;

import com.android.internal.content.NativeLibraryHelper;
import java.io.PrintStream;

public abstract class BaseCommand {
    public static final String FATAL_ERROR_CODE = "Error type 1";
    public static final String NO_CLASS_ERROR_CODE = "Error type 3";
    public static final String NO_SYSTEM_ERROR_CODE = "Error type 2";
    protected String[] mArgs;
    private String mCurArgData;
    private int mNextArg;

    public abstract void onRun() throws Exception;

    public abstract void onShowUsage(PrintStream printStream);

    public void run(String[] args) {
        if (args.length < 1) {
            onShowUsage(System.out);
            return;
        }
        this.mArgs = args;
        this.mNextArg = 0;
        this.mCurArgData = null;
        try {
            onRun();
        } catch (IllegalArgumentException e) {
            onShowUsage(System.err);
            System.err.println();
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e2) {
            e2.printStackTrace(System.err);
            System.exit(1);
        }
    }

    public void showUsage() {
        onShowUsage(System.err);
    }

    public void showError(String message) {
        onShowUsage(System.err);
        System.err.println();
        System.err.println(message);
    }

    public String nextOption() {
        if (this.mCurArgData != null) {
            throw new IllegalArgumentException("No argument expected after \"" + this.mArgs[this.mNextArg - 1] + "\"");
        } else if (this.mNextArg >= this.mArgs.length) {
            return null;
        } else {
            String arg = this.mArgs[this.mNextArg];
            if (!arg.startsWith(NativeLibraryHelper.CLEAR_ABI_OVERRIDE)) {
                return null;
            }
            this.mNextArg++;
            if (arg.equals("--")) {
                return null;
            }
            if (arg.length() <= 1 || arg.charAt(1) == '-') {
                this.mCurArgData = null;
                return arg;
            } else if (arg.length() > 2) {
                this.mCurArgData = arg.substring(2);
                return arg.substring(0, 2);
            } else {
                this.mCurArgData = null;
                return arg;
            }
        }
    }

    public String nextArg() {
        if (this.mCurArgData != null) {
            String arg = this.mCurArgData;
            this.mCurArgData = null;
            return arg;
        } else if (this.mNextArg >= this.mArgs.length) {
            return null;
        } else {
            String[] strArr = this.mArgs;
            int i = this.mNextArg;
            this.mNextArg = i + 1;
            return strArr[i];
        }
    }

    public String nextArgRequired() {
        String arg = nextArg();
        if (arg != null) {
            return arg;
        }
        throw new IllegalArgumentException("Argument expected after \"" + this.mArgs[this.mNextArg - 1] + "\"");
    }
}
