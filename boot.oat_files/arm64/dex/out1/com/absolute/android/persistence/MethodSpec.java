package com.absolute.android.persistence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MethodSpec implements Parcelable {
    public static final Creator<MethodSpec> CREATOR = new Creator<MethodSpec>() {
        public MethodSpec createFromParcel(Parcel in) {
            return new MethodSpec(in);
        }

        public MethodSpec[] newArray(int size) {
            return new MethodSpec[size];
        }
    };
    private String m_apkOrJarPath;
    private Class[] m_argTypes;
    private Object[] m_argValues;
    private String m_className;
    private String m_methodName;

    public MethodSpec(String apkOrJarPath, String className, String methodName, Object[] argValues) {
        if (apkOrJarPath == null) {
            throw new NullPointerException("apk or jar path is null");
        } else if (className == null) {
            throw new NullPointerException("class name is null");
        } else if (methodName == null) {
            throw new NullPointerException("method name is null");
        } else {
            this.m_apkOrJarPath = apkOrJarPath;
            this.m_className = className;
            this.m_methodName = methodName;
            this.m_argValues = argValues;
            loadArgumentTypes();
        }
    }

    public String getApkOrJarPath() {
        return this.m_apkOrJarPath;
    }

    public String getClassName() {
        return this.m_className;
    }

    public String getMethodName() {
        return this.m_methodName;
    }

    public Class[] getArgTypes() {
        return this.m_argTypes;
    }

    public Object[] getArgValues() {
        return this.m_argValues;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.m_apkOrJarPath);
        dest.writeString(this.m_className);
        dest.writeString(this.m_methodName);
        dest.writeArray(this.m_argValues);
    }

    private MethodSpec(Parcel source) {
        this.m_apkOrJarPath = source.readString();
        this.m_className = source.readString();
        this.m_methodName = source.readString();
        this.m_argValues = source.readArray(ClassLoader.getSystemClassLoader());
        loadArgumentTypes();
    }

    private void loadArgumentTypes() {
        if (this.m_argValues != null) {
            this.m_argTypes = new Class[this.m_argValues.length];
            for (int i = 0; i < this.m_argValues.length; i++) {
                this.m_argTypes[i] = this.m_argValues[i].getClass();
            }
            return;
        }
        this.m_argTypes = null;
    }

    private MethodSpec() {
    }
}
