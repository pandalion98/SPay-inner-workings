/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.Byte
 *  java.lang.CharSequence
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import android.text.TextUtils;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCDCVM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.a;

public class DiscoverTrackData {
    public static final ByteBuffer wg = new ByteBuffer(new byte[]{-97, 126});
    private final ByteBuffer us;
    private final long uw;
    private final ByteBuffer wh;
    private final DiscoverCDCVM wi;

    public DiscoverTrackData(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, long l2, DiscoverCDCVM discoverCDCVM) {
        this.us = byteBuffer;
        this.wh = byteBuffer2;
        this.uw = l2;
        this.wi = discoverCDCVM;
    }

    public static DiscoverRecord b(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        ByteBuffer byteBuffer3 = a.a((byte)86, byteBuffer);
        byteBuffer3.append(a.a((byte)87, byteBuffer2));
        ByteBuffer byteBuffer4 = a.a((byte)112, byteBuffer3);
        DiscoverRecord discoverRecord = new DiscoverRecord();
        discoverRecord.setRecordData(byteBuffer4);
        discoverRecord.setSFI(new ByteBuffer(new byte[]{1}));
        discoverRecord.setRecordNumber(new ByteBuffer(new byte[]{1}));
        return discoverRecord;
    }

    private String b(String string, int n2) {
        if (string == null || TextUtils.isEmpty((CharSequence)string)) {
            c.e("DCSDK_", "buildTrackValue, wrong value." + string);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < n2; ++i2) {
                stringBuffer.insert(0, '0');
            }
            return stringBuffer.toString();
        }
        if (string.length() > n2) {
            return string.substring(string.length() - n2, string.length());
        }
        StringBuffer stringBuffer = new StringBuffer(string);
        for (int i3 = 0; i3 < n2 - string.length(); ++i3) {
            stringBuffer.insert(0, '0');
        }
        return stringBuffer.toString();
    }

    public ByteBuffer C(ByteBuffer byteBuffer) {
        String string = this.a(new String(byteBuffer.getBytes()), TrackLayout.wj);
        if (string != null) {
            return new ByteBuffer(string.getBytes());
        }
        return null;
    }

    public ByteBuffer D(ByteBuffer byteBuffer) {
        String string = this.a(byteBuffer.toHexString(), TrackLayout.wk);
        if (string != null) {
            return ByteBuffer.fromHexString(string);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String a(String string, TrackLayout trackLayout) {
        if (this.us == null) {
            c.e("DCSDK_", "buildTrack, ATC is null.");
            return null;
        }
        if (this.wh == null) {
            c.e("DCSDK_", "buildTrack, UN is null.");
            return null;
        }
        if (this.wi == null) {
            c.e("DCSDK_", "buildTrack, CDCVM result is null.");
            return null;
        }
        c.d("DCSDK_", "DCVV: " + String.valueOf((long)this.uw));
        c.d("DCSDK_", "ATC: " + this.us.getInt());
        c.d("DCSDK_", "UN: " + this.wh.getInt());
        String string2 = this.b(String.valueOf((int)this.us.getInt()), trackLayout.atcLength);
        String string3 = this.b(this.wh.toHexString(), trackLayout.unLength);
        String string4 = this.b(String.valueOf((long)this.uw), trackLayout.dcvvLength);
        c.d("DCSDK_", "sTrack1: " + string);
        c.d("DCSDK_", "sDCVV: " + string4);
        c.d("DCSDK_", "sATC: " + string2);
        c.d("DCSDK_", "sUN: " + string3);
        StringBuilder stringBuilder = new StringBuilder(string);
        try {
            stringBuilder.replace(trackLayout.dcvvPosition, trackLayout.dcvvPosition + trackLayout.dcvvLength, string4);
            stringBuilder.replace(trackLayout.atcPosition, trackLayout.atcPosition + trackLayout.atcLength, string2);
            stringBuilder.replace(trackLayout.unPosition, trackLayout.unPosition + trackLayout.unLength, string3);
            stringBuilder.replace(trackLayout.dcvvTypePosition, trackLayout.dcvvTypePosition + trackLayout.dcvvTypeLength, String.valueOf((int)1));
            stringBuilder.replace(trackLayout.pvdPosition, trackLayout.pvdPosition + trackLayout.pvdTypeLength, String.valueOf((long)this.wi.dF()));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        c.d("DCSDK_", "TRACK: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public ByteBuffer dZ() {
        String string = this.b(String.valueOf((long)this.uw), 3);
        if (string != null) {
            byte[] arrby = new byte[]{Byte.parseByte((String)String.valueOf((char)string.charAt(0))), Byte.parseByte((String)String.valueOf((char)string.charAt(1))), Byte.parseByte((String)String.valueOf((char)string.charAt(2)))};
            return new ByteBuffer(arrby);
        }
        return null;
    }

    private static final class TrackLayout
    extends Enum<TrackLayout> {
        public static final /* enum */ TrackLayout wj = new TrackLayout(60, 3, 63, 2, 57, 3, 53, 52, 1, 1);
        public static final /* enum */ TrackLayout wk = new TrackLayout(32, 3, 35, 2, 29, 3, 25, 24, 1, 1);
        private static final /* synthetic */ TrackLayout[] wl;
        public int atcLength;
        public int atcPosition;
        public int dcvvLength;
        public int dcvvPosition;
        public int dcvvTypeLength;
        public int dcvvTypePosition;
        public int pvdPosition;
        public int pvdTypeLength;
        public int unLength;
        public int unPosition;

        static {
            TrackLayout[] arrtrackLayout = new TrackLayout[]{wj, wk};
            wl = arrtrackLayout;
        }

        private TrackLayout(int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12) {
            this.atcPosition = n3;
            this.atcLength = n4;
            this.unPosition = n5;
            this.unLength = n6;
            this.dcvvPosition = n7;
            this.dcvvLength = n8;
            this.pvdPosition = n9;
            this.dcvvTypePosition = n10;
            this.dcvvTypeLength = n11;
            this.pvdTypeLength = n12;
        }

        public static TrackLayout valueOf(String string) {
            return (TrackLayout)Enum.valueOf(TrackLayout.class, (String)string);
        }

        public static TrackLayout[] values() {
            return (TrackLayout[])wl.clone();
        }
    }

}

