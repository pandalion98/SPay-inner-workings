package com.sec.knox.container.util;

import com.google.android.collect.Lists;
import java.util.ArrayList;

public class DaemonEvent {
    private final int mCmdNumber;
    private final int mCode;
    private String mMessage;
    private String[] mParsed = null;
    private String mRawEvent;

    private DaemonEvent(int cmdNumber, int code, String message, String rawEvent) {
        this.mCmdNumber = cmdNumber;
        this.mCode = code;
        this.mMessage = message;
        this.mRawEvent = rawEvent;
    }

    public int getCmdNumber() {
        return this.mCmdNumber;
    }

    public int getCode() {
        return this.mCode;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public void clear() {
        clearMessage();
        clearRawEvent();
        clearParsed();
    }

    public void clearMessage() {
        if (this.mMessage != null) {
            this.mMessage.clear();
        }
    }

    public void clearRawEvent() {
        if (this.mRawEvent != null) {
            this.mRawEvent.clear();
        }
    }

    public void clearParsed() {
        if (this.mParsed != null) {
            for (String arg : this.mParsed) {
                if (arg != null) {
                    arg.clear();
                }
            }
        }
    }

    public int getSubErrorCode() {
        int errorCode = 0;
        if (this.mMessage == null) {
            return 0;
        }
        int i = this.mMessage.indexOf(45) + 1;
        if (i <= 0) {
            return 0;
        }
        while (i < this.mMessage.length() && this.mMessage.charAt(i) >= '0' && this.mMessage.charAt(i) <= '9') {
            errorCode = (errorCode * 10) + (this.mMessage.charAt(i) - 48);
            i++;
        }
        if (errorCode > 0) {
            return errorCode * -1;
        }
        return 0;
    }

    @Deprecated
    public String getRawEvent() {
        return this.mRawEvent;
    }

    public String toString() {
        return this.mRawEvent;
    }

    public boolean isClassContinue() {
        return this.mCode >= 100 && this.mCode < 200;
    }

    public boolean isClassOk() {
        return this.mCode >= 200 && this.mCode < 300;
    }

    public boolean isClassServerError() {
        return this.mCode >= 400 && this.mCode < 500;
    }

    public boolean isClassClientError() {
        return this.mCode >= 500 && this.mCode < 600;
    }

    public boolean isClassUnsolicited() {
        return isClassUnsolicited(this.mCode);
    }

    private static boolean isClassUnsolicited(int code) {
        return code >= 600 && code < 700;
    }

    public void checkCode(int code) {
        if (this.mCode != code) {
            throw new IllegalStateException("Expected " + code + " but was: " + this);
        }
    }

    public static DaemonEvent parseRawEvent(String rawEvent) {
        String[] parsed = rawEvent.split(" ");
        if (parsed.length < 2) {
            throw new IllegalArgumentException("Insufficient arguments");
        }
        try {
            int code = Integer.parseInt(parsed[0]);
            int skiplength = parsed[0].length() + 1;
            int cmdNumber = -1;
            if (!isClassUnsolicited(code)) {
                if (parsed.length < 3) {
                    throw new IllegalArgumentException("Insufficient arguemnts");
                }
                try {
                    cmdNumber = Integer.parseInt(parsed[1]);
                    skiplength += parsed[1].length() + 1;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("problem parsing cmdNumber", e);
                }
            }
            String message = rawEvent.substring(skiplength);
            if (parsed != null) {
                for (String arg : parsed) {
                    if (arg != null) {
                        arg.clear();
                    }
                }
            }
            return new DaemonEvent(cmdNumber, code, message, rawEvent);
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException("problem parsing code", e2);
        }
    }

    public static String[] filterMessageList(DaemonEvent[] events, int matchCode) {
        ArrayList<String> result = Lists.newArrayList();
        for (DaemonEvent event : events) {
            if (event.getCode() == matchCode) {
                result.add(event.getMessage());
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    public String getField(int n) {
        if (this.mParsed == null) {
            this.mParsed = unescapeArgs(this.mRawEvent);
        }
        n += 2;
        if (n > this.mParsed.length) {
            return null;
        }
        return this.mParsed[n];
    }

    public static String[] unescapeArgs(String rawEvent) {
        String LOGTAG = "unescapeArgs";
        ArrayList<String> parsed = new ArrayList();
        int length = rawEvent.length();
        int current = 0;
        boolean quoted = false;
        if (rawEvent.charAt(0) == '\"') {
            quoted = true;
            current = 0 + 1;
        }
        while (current < length) {
            int wordEnd;
            if (quoted) {
                wordEnd = current;
                while (true) {
                    wordEnd = rawEvent.indexOf(34, wordEnd);
                    if (wordEnd == -1 || rawEvent.charAt(wordEnd - 1) != '\\') {
                        break;
                    }
                    wordEnd++;
                }
            } else {
                wordEnd = rawEvent.indexOf(32, current);
            }
            if (wordEnd == -1) {
                wordEnd = length;
            }
            String word = rawEvent.substring(current, wordEnd);
            current += word.length();
            if (quoted) {
                current++;
            } else {
                word = word.trim();
            }
            parsed.add(word.replace("\\\\", "\\").replace("\\\"", "\""));
            int nextSpace = rawEvent.indexOf(32, current);
            int nextQuote = rawEvent.indexOf(" \"", current);
            if (nextQuote <= -1 || nextQuote > nextSpace) {
                quoted = false;
                if (nextSpace > -1) {
                    current = nextSpace + 1;
                }
            } else {
                quoted = true;
                current = nextQuote + 2;
            }
        }
        return (String[]) parsed.toArray(new String[parsed.size()]);
    }
}
