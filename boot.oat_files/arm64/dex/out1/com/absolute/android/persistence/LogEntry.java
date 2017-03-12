package com.absolute.android.persistence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Calendar;
import java.util.TimeZone;

public class LogEntry implements Parcelable {
    public static final Creator<LogEntry> CREATOR = new Creator<LogEntry>() {
        public LogEntry createFromParcel(Parcel in) {
            return new LogEntry(in);
        }

        public LogEntry[] newArray(int size) {
            return new LogEntry[size];
        }
    };
    private String m_message;
    private String m_method;
    private int m_severity;
    private Calendar m_timeStampUTC;

    public Calendar getTimeStampUTC() {
        return this.m_timeStampUTC;
    }

    public int getSeverity() {
        return this.m_severity;
    }

    public String getMethod() {
        return this.m_method;
    }

    public String getMessage() {
        return this.m_message;
    }

    public LogEntry(int severity, String method, String message) {
        this(Calendar.getInstance(TimeZone.getTimeZone("GMT-00:00")), severity, method, message);
    }

    public LogEntry(Calendar timeStampUTC, int severity, String method, String message) {
        if (severity != 6 && severity != 5 && severity != 4 && severity != 3 && severity != 2) {
            throw new IllegalArgumentException("Invalid log severity level");
        } else if (method == null) {
            throw new NullPointerException("method is null");
        } else if (message == null) {
            throw new NullPointerException("message is null");
        } else {
            this.m_timeStampUTC = timeStampUTC;
            this.m_severity = severity;
            this.m_method = method;
            this.m_message = message;
        }
    }

    private LogEntry() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.m_severity);
        dest.writeString(this.m_message);
        dest.writeString(this.m_method);
        dest.writeValue(this.m_timeStampUTC);
    }

    private LogEntry(Parcel source) {
        this.m_severity = source.readInt();
        this.m_message = source.readString();
        this.m_method = source.readString();
        this.m_timeStampUTC = (Calendar) source.readValue(ClassLoader.getSystemClassLoader());
    }
}
