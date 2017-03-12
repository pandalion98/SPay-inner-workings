package com.samsung.android.contextaware.utilbundle.logger;

import android.util.Log;
import java.util.Calendar;

public class CaLogger {
    private static final String FILE_NAME = "CAELogger";
    public static final String TAG = "CAE";
    private static volatile CaLogger instance;
    private static boolean isCaller = true;
    private static boolean isConsoleLogging = true;
    private static boolean isFileLogging = false;
    private static boolean isGrayBoxTesting = false;
    private static int mLevel = Level.TRACE.ordinal();
    private static ILoggingObserver mLoggingObserver;

    public enum Level {
        TRACE {
            String consoleLogging(String msg) {
                boolean usable;
                if (!CaLogger.isConsoleLogging || CaLogger.mLevel > ordinal()) {
                    usable = false;
                } else {
                    usable = true;
                }
                String str = Level.getCallerInfo(false);
                if (usable) {
                    Log.v(CaLogger.TAG, str);
                }
                return str;
            }

            void fileLogging(String msg) {
                boolean usable;
                if (!CaLogger.isFileLogging || CaLogger.mLevel > ordinal()) {
                    usable = false;
                } else {
                    usable = true;
                }
                if (usable) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("T", CaLogger.TAG, Level.getCallerInfo(false), msg));
                }
            }
        },
        DEBUG {
            String consoleLogging(String msg) {
                boolean usable = CaLogger.isConsoleLogging && CaLogger.mLevel <= ordinal();
                String str = Level.getCallerInfo(true) + msg;
                if (usable) {
                    Log.d(CaLogger.TAG, str);
                }
                return str;
            }

            void fileLogging(String msg) {
                boolean usable = CaLogger.isFileLogging && CaLogger.mLevel <= ordinal();
                if (usable) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("D", CaLogger.TAG, Level.getCallerInfo(true), msg));
                }
            }
        },
        INFO {
            String consoleLogging(String msg) {
                boolean usable = CaLogger.isConsoleLogging && CaLogger.mLevel <= ordinal();
                String str = Level.getCallerInfo(true) + msg;
                if (usable) {
                    Log.i(CaLogger.TAG, str);
                }
                return str;
            }

            void fileLogging(String msg) {
                boolean usable = CaLogger.isFileLogging && CaLogger.mLevel <= ordinal();
                if (usable) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("I", CaLogger.TAG, Level.getCallerInfo(true), msg));
                }
            }
        },
        WARN {
            String consoleLogging(String msg) {
                boolean usable = CaLogger.isConsoleLogging && CaLogger.mLevel <= ordinal();
                String str = Level.getCallerInfo(true) + msg;
                if (usable) {
                    Log.w(CaLogger.TAG, str);
                }
                return str;
            }

            void fileLogging(String msg) {
                boolean usable = CaLogger.isFileLogging && CaLogger.mLevel <= ordinal();
                if (usable) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("W", CaLogger.TAG, Level.getCallerInfo(true), msg));
                }
            }
        },
        ERROR {
            String consoleLogging(String msg) {
                boolean usable = CaLogger.isConsoleLogging && CaLogger.mLevel <= ordinal();
                String str = Level.getCallerInfo(true) + msg;
                if (usable) {
                    Log.e(CaLogger.TAG, str);
                }
                return str;
            }

            void fileLogging(String msg) {
                boolean usable = CaLogger.isFileLogging && CaLogger.mLevel <= ordinal();
                if (usable) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("E", CaLogger.TAG, Level.getCallerInfo(true), msg));
                }
            }
        },
        EXCEPTION {
            String consoleLogging(String msg) {
                boolean usable = CaLogger.isConsoleLogging && CaLogger.mLevel <= ordinal();
                if (usable) {
                    Log.e(CaLogger.TAG, msg);
                }
                return null;
            }

            void fileLogging(String msg) {
                boolean usable = CaLogger.isFileLogging && CaLogger.mLevel <= ordinal();
                if (usable) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("X", CaLogger.TAG, "", msg));
                }
            }
        };

        abstract String consoleLogging(String str);

        abstract void fileLogging(String str);

        private static String getCallerInfo(boolean isHyphen) {
            if (!CaLogger.isCaller) {
                return "";
            }
            StackTraceElement[] stackList = Thread.currentThread().getStackTrace();
            StringBuffer callerInfo = new StringBuffer();
            if (stackList.length >= 4) {
                String str = stackList[6].toString();
                callerInfo.append(str.substring(str.lastIndexOf(46, str.indexOf(40)) + 1));
            }
            if (isHyphen) {
                callerInfo.append(" - ");
            }
            return callerInfo.toString();
        }
    }

    public static CaLogger getInstance() {
        if (instance == null) {
            synchronized (CaLogger.class) {
                if (instance == null) {
                    instance = new CaLogger();
                }
            }
        }
        return instance;
    }

    public static void trace() {
        String str = Level.TRACE.consoleLogging("");
        Level.TRACE.fileLogging("");
        if (isGrayBoxTesting) {
            notifyLoggingObserver(str);
        }
    }

    public static void debug(String msg) {
        String str = Level.DEBUG.consoleLogging(msg);
        Level.DEBUG.fileLogging(msg);
        if (isGrayBoxTesting) {
            notifyLoggingObserver(str);
        }
    }

    public static void info(String msg) {
        String str = Level.INFO.consoleLogging(msg);
        Level.INFO.fileLogging(msg);
        if (isGrayBoxTesting) {
            notifyLoggingObserver(str);
        }
    }

    public static void warning(String msg) {
        String str = Level.WARN.consoleLogging(msg);
        Level.WARN.fileLogging(msg);
        if (isGrayBoxTesting) {
            notifyLoggingObserver(str);
        }
    }

    public static void error(String msg) {
        String str = Level.ERROR.consoleLogging(msg);
        Level.ERROR.fileLogging(msg);
        if (isGrayBoxTesting) {
            notifyLoggingObserver(str);
        }
    }

    public static void exception(Throwable exMsg) {
        Level.EXCEPTION.consoleLogging(exMsg.toString());
        Level.EXCEPTION.fileLogging(exMsg.toString());
        StackTraceElement[] trace = exMsg.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            Level.EXCEPTION.consoleLogging(trace[i].toString());
            Level.EXCEPTION.fileLogging(trace[i].toString());
        }
        Throwable ourCause = exMsg.getCause();
        if (ourCause != null) {
            exception(ourCause);
        }
    }

    private static String getFilePattern(String priority, String tag, String caller, String msg) {
        Calendar utcDate = Calendar.getInstance();
        utcDate.setTimeInMillis(System.currentTimeMillis());
        int year = utcDate.get(1);
        int month = utcDate.get(2) + 1;
        int date = utcDate.get(5);
        int hour = utcDate.get(11);
        int min = utcDate.get(12);
        int sec = utcDate.get(13);
        return String.format("[%4d-%02d-%02d %02d:%02d:%02d] [%s] [%s] %s %s", new Object[]{Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(date), Integer.valueOf(hour), Integer.valueOf(min), Integer.valueOf(sec), priority, tag, caller, msg});
    }

    public static void setConsoleLoggingEnable(boolean enabled) {
        isConsoleLogging = enabled;
    }

    public static void setFileLoggingEnable(boolean enabled) {
        boolean check;
        if (enabled) {
            check = CaFileLogger.getInstance().startLogging(FILE_NAME);
        } else {
            check = CaFileLogger.getInstance().stopLogging(FILE_NAME);
        }
        if (check) {
            isFileLogging = enabled;
        }
    }

    public static void setGrayBoxTestingEnable(boolean enabled) {
        isGrayBoxTesting = enabled;
    }

    public static void setLogOption(int level, boolean enableCaller) {
        mLevel = level;
        isCaller = enableCaller;
    }

    public static void registerLoggingObserver(ILoggingObserver observer) {
        mLoggingObserver = observer;
    }

    public static void unregisterLoggingObserver(ILoggingObserver observer) {
        mLoggingObserver = null;
    }

    public static void notifyLoggingObserver(String log) {
        if (mLoggingObserver != null) {
            mLoggingObserver.updateLogMessage(log);
        }
    }
}
