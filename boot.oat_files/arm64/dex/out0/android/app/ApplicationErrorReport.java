package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.mtp.MtpConstants;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.util.Printer;
import android.util.Slog;
import com.android.internal.util.FastPrintWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ApplicationErrorReport implements Parcelable {
    public static final Creator<ApplicationErrorReport> CREATOR = new Creator<ApplicationErrorReport>() {
        public ApplicationErrorReport createFromParcel(Parcel source) {
            return new ApplicationErrorReport(source);
        }

        public ApplicationErrorReport[] newArray(int size) {
            return new ApplicationErrorReport[size];
        }
    };
    static final String DEFAULT_ERROR_RECEIVER_PROPERTY = "ro.error.receiver.default";
    static final String SYSTEM_APPS_ERROR_RECEIVER_PROPERTY = "ro.error.receiver.system.apps";
    public static final int TYPE_ANR = 2;
    public static final int TYPE_BATTERY = 3;
    public static final int TYPE_CRASH = 1;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_RUNNING_SERVICE = 5;
    public AnrInfo anrInfo;
    public BatteryInfo batteryInfo;
    public CrashInfo crashInfo;
    public String installerPackageName;
    public String packageName;
    public String processName;
    public RunningServiceInfo runningServiceInfo;
    public boolean systemApp;
    public long time;
    public int type;

    public static class AnrInfo {
        public String activity;
        public String cause;
        public String info;

        public AnrInfo(Parcel in) {
            this.activity = in.readString();
            this.cause = in.readString();
            this.info = in.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.activity);
            dest.writeString(this.cause);
            dest.writeString(this.info);
        }

        public void dump(Printer pw, String prefix) {
            pw.println(prefix + "activity: " + this.activity);
            pw.println(prefix + "cause: " + this.cause);
            pw.println(prefix + "info: " + this.info);
        }
    }

    public static class BatteryInfo {
        public String checkinDetails;
        public long durationMicros;
        public String usageDetails;
        public int usagePercent;

        public BatteryInfo(Parcel in) {
            this.usagePercent = in.readInt();
            this.durationMicros = in.readLong();
            this.usageDetails = in.readString();
            this.checkinDetails = in.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.usagePercent);
            dest.writeLong(this.durationMicros);
            dest.writeString(this.usageDetails);
            dest.writeString(this.checkinDetails);
        }

        public void dump(Printer pw, String prefix) {
            pw.println(prefix + "usagePercent: " + this.usagePercent);
            pw.println(prefix + "durationMicros: " + this.durationMicros);
            pw.println(prefix + "usageDetails: " + this.usageDetails);
            pw.println(prefix + "checkinDetails: " + this.checkinDetails);
        }
    }

    public static class CrashInfo {
        public String exceptionClassName;
        public String exceptionMessage;
        public String stackTrace;
        public String throwClassName;
        public String throwFileName;
        public int throwLineNumber;
        public String throwMethodName;

        public CrashInfo(Throwable tr) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new FastPrintWriter(sw, false, 256);
            tr.printStackTrace(pw);
            pw.flush();
            this.stackTrace = sw.toString();
            this.exceptionMessage = tr.getMessage();
            Throwable rootTr = tr;
            while (tr.getCause() != null) {
                tr = tr.getCause();
                if (tr.getStackTrace() != null && tr.getStackTrace().length > 0) {
                    rootTr = tr;
                }
                String msg = tr.getMessage();
                if (msg != null && msg.length() > 0) {
                    this.exceptionMessage = msg;
                }
            }
            this.exceptionClassName = rootTr.getClass().getName();
            if (rootTr.getStackTrace().length > 0) {
                StackTraceElement trace = rootTr.getStackTrace()[0];
                this.throwFileName = trace.getFileName();
                this.throwClassName = trace.getClassName();
                this.throwMethodName = trace.getMethodName();
                this.throwLineNumber = trace.getLineNumber();
                return;
            }
            this.throwFileName = "unknown";
            this.throwClassName = "unknown";
            this.throwMethodName = "unknown";
            this.throwLineNumber = 0;
        }

        public CrashInfo(Parcel in) {
            this.exceptionClassName = in.readString();
            this.exceptionMessage = in.readString();
            this.throwFileName = in.readString();
            this.throwClassName = in.readString();
            this.throwMethodName = in.readString();
            this.throwLineNumber = in.readInt();
            this.stackTrace = in.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            int start = dest.dataPosition();
            dest.writeString(this.exceptionClassName);
            dest.writeString(this.exceptionMessage);
            dest.writeString(this.throwFileName);
            dest.writeString(this.throwClassName);
            dest.writeString(this.throwMethodName);
            dest.writeInt(this.throwLineNumber);
            dest.writeString(this.stackTrace);
            if (dest.dataPosition() - start > MtpConstants.DEVICE_PROPERTY_UNDEFINED) {
                Slog.d("Error", "ERR: exClass=" + this.exceptionClassName);
                Slog.d("Error", "ERR: exMsg=" + this.exceptionMessage);
                Slog.d("Error", "ERR: file=" + this.throwFileName);
                Slog.d("Error", "ERR: class=" + this.throwClassName);
                Slog.d("Error", "ERR: method=" + this.throwMethodName + " line=" + this.throwLineNumber);
                Slog.d("Error", "ERR: stack=" + this.stackTrace);
                Slog.d("Error", "ERR: TOTAL BYTES WRITTEN: " + (dest.dataPosition() - start));
            }
        }

        public void dump(Printer pw, String prefix) {
            pw.println(prefix + "exceptionClassName: " + this.exceptionClassName);
            pw.println(prefix + "exceptionMessage: " + this.exceptionMessage);
            pw.println(prefix + "throwFileName: " + this.throwFileName);
            pw.println(prefix + "throwClassName: " + this.throwClassName);
            pw.println(prefix + "throwMethodName: " + this.throwMethodName);
            pw.println(prefix + "throwLineNumber: " + this.throwLineNumber);
            pw.println(prefix + "stackTrace: " + this.stackTrace);
        }
    }

    public static class RunningServiceInfo {
        public long durationMillis;
        public String serviceDetails;

        public RunningServiceInfo(Parcel in) {
            this.durationMillis = in.readLong();
            this.serviceDetails = in.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.durationMillis);
            dest.writeString(this.serviceDetails);
        }

        public void dump(Printer pw, String prefix) {
            pw.println(prefix + "durationMillis: " + this.durationMillis);
            pw.println(prefix + "serviceDetails: " + this.serviceDetails);
        }
    }

    ApplicationErrorReport(Parcel in) {
        readFromParcel(in);
    }

    public static ComponentName getErrorReportReceiver(Context context, String packageName, int appFlags) {
        if (Global.getInt(context.getContentResolver(), Global.SEND_ACTION_APP_ERROR, 0) == 0) {
            return null;
        }
        ComponentName result;
        PackageManager pm = context.getPackageManager();
        String candidate = null;
        try {
            candidate = pm.getInstallerPackageName(packageName);
        } catch (IllegalArgumentException e) {
        }
        if (candidate != null) {
            result = getErrorReportReceiver(pm, packageName, candidate);
            if (result != null) {
                return result;
            }
        }
        if ((appFlags & 1) != 0) {
            result = getErrorReportReceiver(pm, packageName, SystemProperties.get(SYSTEM_APPS_ERROR_RECEIVER_PROPERTY));
            if (result != null) {
                return result;
            }
        }
        return getErrorReportReceiver(pm, packageName, SystemProperties.get(DEFAULT_ERROR_RECEIVER_PROPERTY));
    }

    static ComponentName getErrorReportReceiver(PackageManager pm, String errorPackage, String receiverPackage) {
        if (receiverPackage == null || receiverPackage.length() == 0 || receiverPackage.equals(errorPackage)) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_APP_ERROR);
        intent.setPackage(receiverPackage);
        ResolveInfo info = pm.resolveActivity(intent, 0);
        if (info == null || info.activityInfo == null) {
            return null;
        }
        return new ComponentName(receiverPackage, info.activityInfo.name);
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        dest.writeInt(this.type);
        dest.writeString(this.packageName);
        dest.writeString(this.installerPackageName);
        dest.writeString(this.processName);
        dest.writeLong(this.time);
        dest.writeInt(this.systemApp ? 1 : 0);
        if (this.crashInfo == null) {
            i = 0;
        }
        dest.writeInt(i);
        switch (this.type) {
            case 1:
                if (this.crashInfo != null) {
                    this.crashInfo.writeToParcel(dest, flags);
                    return;
                }
                return;
            case 2:
                this.anrInfo.writeToParcel(dest, flags);
                return;
            case 3:
                this.batteryInfo.writeToParcel(dest, flags);
                return;
            case 5:
                this.runningServiceInfo.writeToParcel(dest, flags);
                return;
            default:
                return;
        }
    }

    public void readFromParcel(Parcel in) {
        boolean z;
        boolean hasCrashInfo;
        this.type = in.readInt();
        this.packageName = in.readString();
        this.installerPackageName = in.readString();
        this.processName = in.readString();
        this.time = in.readLong();
        if (in.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.systemApp = z;
        if (in.readInt() == 1) {
            hasCrashInfo = true;
        } else {
            hasCrashInfo = false;
        }
        switch (this.type) {
            case 1:
                CrashInfo crashInfo;
                if (hasCrashInfo) {
                    crashInfo = new CrashInfo(in);
                } else {
                    crashInfo = null;
                }
                this.crashInfo = crashInfo;
                this.anrInfo = null;
                this.batteryInfo = null;
                this.runningServiceInfo = null;
                return;
            case 2:
                this.anrInfo = new AnrInfo(in);
                this.crashInfo = null;
                this.batteryInfo = null;
                this.runningServiceInfo = null;
                return;
            case 3:
                this.batteryInfo = new BatteryInfo(in);
                this.anrInfo = null;
                this.crashInfo = null;
                this.runningServiceInfo = null;
                return;
            case 5:
                this.batteryInfo = null;
                this.anrInfo = null;
                this.crashInfo = null;
                this.runningServiceInfo = new RunningServiceInfo(in);
                return;
            default:
                return;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void dump(Printer pw, String prefix) {
        pw.println(prefix + "type: " + this.type);
        pw.println(prefix + "packageName: " + this.packageName);
        pw.println(prefix + "installerPackageName: " + this.installerPackageName);
        pw.println(prefix + "processName: " + this.processName);
        pw.println(prefix + "time: " + this.time);
        pw.println(prefix + "systemApp: " + this.systemApp);
        switch (this.type) {
            case 1:
                this.crashInfo.dump(pw, prefix);
                return;
            case 2:
                this.anrInfo.dump(pw, prefix);
                return;
            case 3:
                this.batteryInfo.dump(pw, prefix);
                return;
            case 5:
                this.runningServiceInfo.dump(pw, prefix);
                return;
            default:
                return;
        }
    }
}
