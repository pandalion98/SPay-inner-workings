/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.Token;
import java.util.ArrayList;
import java.util.List;

public class ProvisionTokenResult
implements Parcelable {
    public static final String BUNDLE_KEY_LOYALTY_RESPONSE_DATA = "loyaltyResponseData";
    public static final String BUNDLE_KEY_RESPONSE_DATA_FD = "loyaltyResponseDataFd";
    public static final String BUNDLE_KEY_RESPONSE_DATA_FILE_PATH = "loyaltyResponseDataFilePath";
    public static final Parcelable.Creator<ProvisionTokenResult> CREATOR = new Parcelable.Creator<ProvisionTokenResult>(){

        public ProvisionTokenResult createFromParcel(Parcel parcel) {
            return new ProvisionTokenResult(parcel);
        }

        public ProvisionTokenResult[] newArray(int n2) {
            return new ProvisionTokenResult[n2];
        }
    };
    Bundle bundle;
    private List<IdvMethod> idv = new ArrayList();
    private Token token;

    public ProvisionTokenResult() {
    }

    private ProvisionTokenResult(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
    }

    /*
     * Exception decompiling
     */
    private void copyContentFromFd() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [19[TRYBLOCK]], but top level block is 4[TRYBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public int describeContents() {
        return 0;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public List<IdvMethod> getIdv() {
        return this.idv;
    }

    public Token getToken() {
        return this.token;
    }

    public void readFromParcel(Parcel parcel) {
        parcel.readList(this.idv, this.getClass().getClassLoader());
        this.token = (Token)parcel.readParcelable(this.getClass().getClassLoader());
        this.bundle = parcel.readBundle();
        this.copyContentFromFd();
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setIdv(List<IdvMethod> list) {
        this.idv = list;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        String string = this.token != null ? "ProvisionTokenResult:" + this.token.toString() : "ProvisionTokenResult:" + " token: null";
        String string2 = this.idv != null ? string + this.idv.toString() : string + "idv : null ";
        if (this.bundle != null) {
            return string2 + this.bundle.toString();
        }
        return string2 + "bundle : null ";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeList(this.idv);
        parcel.writeParcelable((Parcelable)this.token, n2);
        parcel.writeBundle(this.bundle);
    }

}

