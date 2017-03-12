package com.samsung.android.spayfw.payprovider.discover.payment.data;

import android.text.TextUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class DiscoverTrackData {
    public static final ByteBuffer wg;
    private final ByteBuffer us;
    private final long uw;
    private final ByteBuffer wh;
    private final DiscoverCDCVM wi;

    private enum TrackLayout {
        Track1Layout(60, 3, 63, 2, 57, 3, 53, 52, 1, 1),
        Track2Layout(32, 3, 35, 2, 29, 3, 25, 24, 1, 1);
        
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

        private TrackLayout(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            this.atcPosition = i;
            this.atcLength = i2;
            this.unPosition = i3;
            this.unLength = i4;
            this.dcvvPosition = i5;
            this.dcvvLength = i6;
            this.pvdPosition = i7;
            this.dcvvTypePosition = i8;
            this.dcvvTypeLength = i9;
            this.pvdTypeLength = i10;
        }
    }

    static {
        wg = new ByteBuffer(new byte[]{(byte) -97, (byte) 126});
    }

    public DiscoverTrackData(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, long j, DiscoverCDCVM discoverCDCVM) {
        this.us = byteBuffer;
        this.wh = byteBuffer2;
        this.uw = j;
        this.wi = discoverCDCVM;
    }

    public ByteBuffer m959C(ByteBuffer byteBuffer) {
        String a = m961a(new String(byteBuffer.getBytes()), TrackLayout.Track1Layout);
        return a != null ? new ByteBuffer(a.getBytes()) : null;
    }

    public ByteBuffer m960D(ByteBuffer byteBuffer) {
        String a = m961a(byteBuffer.toHexString(), TrackLayout.Track2Layout);
        return a != null ? ByteBuffer.fromHexString(a) : null;
    }

    public String m961a(String str, TrackLayout trackLayout) {
        if (this.us == null) {
            Log.m286e("DCSDK_", "buildTrack, ATC is null.");
            return null;
        } else if (this.wh == null) {
            Log.m286e("DCSDK_", "buildTrack, UN is null.");
            return null;
        } else if (this.wi == null) {
            Log.m286e("DCSDK_", "buildTrack, CDCVM result is null.");
            return null;
        } else {
            Log.m285d("DCSDK_", "DCVV: " + String.valueOf(this.uw));
            Log.m285d("DCSDK_", "ATC: " + this.us.getInt());
            Log.m285d("DCSDK_", "UN: " + this.wh.getInt());
            String b = m958b(String.valueOf(this.us.getInt()), trackLayout.atcLength);
            String b2 = m958b(this.wh.toHexString(), trackLayout.unLength);
            String b3 = m958b(String.valueOf(this.uw), trackLayout.dcvvLength);
            Log.m285d("DCSDK_", "sTrack1: " + str);
            Log.m285d("DCSDK_", "sDCVV: " + b3);
            Log.m285d("DCSDK_", "sATC: " + b);
            Log.m285d("DCSDK_", "sUN: " + b2);
            StringBuilder stringBuilder = new StringBuilder(str);
            try {
                stringBuilder.replace(trackLayout.dcvvPosition, trackLayout.dcvvPosition + trackLayout.dcvvLength, b3);
                stringBuilder.replace(trackLayout.atcPosition, trackLayout.atcPosition + trackLayout.atcLength, b);
                stringBuilder.replace(trackLayout.unPosition, trackLayout.unPosition + trackLayout.unLength, b2);
                stringBuilder.replace(trackLayout.dcvvTypePosition, trackLayout.dcvvTypePosition + trackLayout.dcvvTypeLength, String.valueOf(1));
                stringBuilder.replace(trackLayout.pvdPosition, trackLayout.pvdPosition + trackLayout.pvdTypeLength, String.valueOf(this.wi.dF()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.m285d("DCSDK_", "TRACK: " + stringBuilder.toString());
            return stringBuilder.toString();
        }
    }

    private String m958b(String str, int i) {
        StringBuffer stringBuffer;
        int i2;
        if (str == null || TextUtils.isEmpty(str)) {
            Log.m286e("DCSDK_", "buildTrackValue, wrong value." + str);
            stringBuffer = new StringBuffer();
            for (i2 = 0; i2 < i; i2++) {
                stringBuffer.insert(0, LLVARUtil.EMPTY_STRING);
            }
            return stringBuffer.toString();
        } else if (str.length() > i) {
            return str.substring(str.length() - i, str.length());
        } else {
            stringBuffer = new StringBuffer(str);
            for (i2 = 0; i2 < i - str.length(); i2++) {
                stringBuffer.insert(0, LLVARUtil.EMPTY_STRING);
            }
            return stringBuffer.toString();
        }
    }

    public static DiscoverRecord m957b(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        ByteBuffer a = BERTLV.m1001a((byte) 86, byteBuffer);
        a.append(BERTLV.m1001a((byte) 87, byteBuffer2));
        a = BERTLV.m1001a((byte) 112, a);
        DiscoverRecord discoverRecord = new DiscoverRecord();
        discoverRecord.setRecordData(a);
        discoverRecord.setSFI(new ByteBuffer(new byte[]{(byte) 1}));
        discoverRecord.setRecordNumber(new ByteBuffer(new byte[]{(byte) 1}));
        return discoverRecord;
    }

    public ByteBuffer dZ() {
        if (m958b(String.valueOf(this.uw), 3) == null) {
            return null;
        }
        return new ByteBuffer(new byte[]{Byte.parseByte(String.valueOf(m958b(String.valueOf(this.uw), 3).charAt(0))), Byte.parseByte(String.valueOf(m958b(String.valueOf(this.uw), 3).charAt(1))), Byte.parseByte(String.valueOf(m958b(String.valueOf(this.uw), 3).charAt(2)))});
    }
}
