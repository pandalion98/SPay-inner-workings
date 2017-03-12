package com.samsung.sensorframework.sda.p036c;

import android.content.Context;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p030a.GlobalConfig;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.sensorframework.sda.c.b */
public abstract class CommunicationProcessor extends AbstractProcessor {
    public CommunicationProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    protected String cc(String str) {
        String str2 = BuildConfig.FLAVOR;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                str2 = str2 + str.charAt(i);
            }
        }
        return str2;
    }

    protected String cd(String str) {
        if (!GlobalConfig.gO().gQ()) {
            return str;
        }
        if (str == null || str.length() == 0) {
            return BuildConfig.FLAVOR;
        }
        String cc = cc(str);
        if (cc.length() > 10) {
            cc = cc.substring(cc.length() - 10, cc.length());
        }
        return ce(cc);
    }

    protected String ce(String str) {
        if (!GlobalConfig.gO().gQ()) {
            return str;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.reset();
        byte[] digest = messageDigest.digest(str.getBytes());
        String str2 = BuildConfig.FLAVOR;
        for (byte b : digest) {
            String toHexString = Integer.toHexString(b & GF2Field.MASK);
            if (toHexString.length() == 1) {
                toHexString = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + toHexString;
            }
            str2 = str2 + toHexString.toUpperCase(Locale.ENGLISH);
        }
        return str2;
    }
}
