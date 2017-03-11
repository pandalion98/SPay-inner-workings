package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverApduProcessingResult;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext.DiscoverClTransactionType;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverDataTags;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.p017a.CommandApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.p017a.SelectRApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.TLVData;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.f */
public class DiscoverSelectApduHandler extends DiscoverApduHandler {
    private CommandApdu td;

    public DiscoverSelectApduHandler(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        super(byteBuffer, discoverTransactionContext, discoverPaymentCard);
        this.td = new CommandApdu(byteBuffer);
    }

    public DiscoverApduProcessingResult cK() {
        int i = 0;
        Log.m285d("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select start...");
        if (this.td.dk() != 0) {
            Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, cla is not supported, cla = " + this.td.dk() + ", expected " + 0);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_CLA_NOT_SUPPORTED);
        } else if (this.td.getINS() != com.mastercard.mobile_api.utils.apdu.ISO7816.INS_SELECT) {
            Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, ins is not supported, ins = " + this.td.getINS() + ", expected " + -92);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_INS_NOT_SUPPORTED);
        } else if (this.td.getP1() != (byte) 4) {
            Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, wrong p1, p1 = " + this.td.getP1() + ", expected " + 4);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_INCORRECT_P1P2);
        } else if (this.td.getP2() == null || this.td.getP2() == (byte) 2) {
            ByteBuffer data = this.td.getData();
            if (data == null || this.td.getLc() != data.getSize()) {
                Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, wrong length  = " + this.td.getLc() + ", actual length " + (data != null ? Integer.valueOf(data.getSize()) : null));
                return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_LENGTH);
            } else if (this.td.dl() != null) {
                Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, wrong Le = " + this.td.dl() + ", expected Le  = " + 0);
                return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_LENGTH);
            } else if (cM() == null) {
                Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Payment profile not found.");
                return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
            } else {
                ByteBuffer fciPpse;
                Log.m287i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select aid = " + data.toHexString());
                if (data.toHexString().startsWith(DiscoverCLTransactionContext.ui.toHexString())) {
                    Log.m287i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select ppse aid");
                    fciPpse = cM().getFciPpse();
                } else if (data.toHexString().startsWith(DiscoverClTransactionType.DISCOVER_CL_EMV.getAid().toHexString())) {
                    Log.m287i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select emv aid");
                    r0 = cM().getFciMainAid();
                    cN().m947a(DiscoverClTransactionType.DISCOVER_CL_EMV);
                    fciPpse = r0;
                } else if (data.toHexString().startsWith(DiscoverClTransactionType.DISCOVER_CL_ZIP.getAid().toHexString())) {
                    Log.m287i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select zip aid");
                    cN().m947a(DiscoverClTransactionType.DISCOVER_CL_ZIP);
                    fciPpse = cM().getFciZipAid();
                } else if (data.toHexString().startsWith(DiscoverClTransactionType.DISCOVER_CL_EMV_DEBIT.getAid().toHexString())) {
                    Log.m287i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select emv debit aid");
                    r0 = cM().getFciDebitAid();
                    cN().m947a(DiscoverClTransactionType.DISCOVER_CL_EMV_DEBIT);
                    fciPpse = r0;
                } else if (cM().getFciAltAid() != null) {
                    Log.m285d("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select alt aid: " + data.toHexString());
                    HashMap fciAltAid = cM().getFciAltAid();
                    if (fciAltAid != null) {
                        Log.m287i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Alt FCI selected");
                        r0 = (ByteBuffer) fciAltAid.get(data.toHexString());
                        DiscoverClTransactionType[] values = DiscoverClTransactionType.values();
                        int length = values.length;
                        while (i < length) {
                            DiscoverClTransactionType discoverClTransactionType = values[i];
                            if (!(discoverClTransactionType.getAid() == null || discoverClTransactionType.getAid().toHexString() == null || !data.toHexString().startsWith(discoverClTransactionType.getAid().toHexString()))) {
                                Log.m285d("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Alt AID found " + discoverClTransactionType.name());
                                cN().m947a(discoverClTransactionType);
                            }
                            i++;
                        }
                        if (cN().dK() == null) {
                            Log.m287i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Alt AID unknown alias, set to emv.");
                            cN().m947a(DiscoverClTransactionType.DISCOVER_CL_EMV);
                        }
                        fciPpse = r0;
                    } else {
                        Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select alt aid list is empty.");
                        return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
                    }
                } else {
                    Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, not supported aid, aid = " + data.toHexString());
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
                }
                if (fciPpse == null) {
                    Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU fci is null.");
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
                }
                Log.m285d("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select fci = " + fciPpse.toHexString());
                if (data.equals(DiscoverCLTransactionContext.ui) || m990a(data, fciPpse)) {
                    List applicationBlockedList = cM().getDiscoverApplicationData().getApplicationBlockedList();
                    if (applicationBlockedList == null || applicationBlockedList.isEmpty() || !applicationBlockedList.contains(data)) {
                        cN().m946N(m989c(data));
                        return new DiscoverApduProcessingResult(new SelectRApdu(data, fciPpse).dj(), DiscoverApduHandlerState.DiscoverAPDUStateSelected);
                    }
                    Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, aid found in the blocked list: " + data.toHexString());
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_SELECTED_FILE_INVALIDATED);
                }
                Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, aid not found in adf, aid = " + data.toHexString());
                return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
            }
        } else {
            Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, wrong p2, p2 = " + this.td.getP2() + ", expected " + 0 + " or " + 2);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_INCORRECT_P1P2);
        }
    }

    public boolean m990a(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        if (cM().getFciPpse() == null) {
            Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, checkAidInAdf PPSE FCI is null.");
            return false;
        } else if (byteBuffer2 == null) {
            Log.m286e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, checkAidInAdf FCI is null.");
            return false;
        } else {
            ByteBuffer byteBuffer3;
            String toHexString;
            CharSequence toHexString2;
            boolean z;
            String toHexString3 = cM().getFciPpse().toHexString();
            Log.m285d("DCSDK_", "parsePPSE_FCI, parse FCI Template tag.");
            TLVData c = BERTLV.m1005c(byteBuffer2.getBytes(), 0, byteBuffer2.getSize());
            if (!(c == null || c.m1006O(DiscoverDataTags.vO.getInt()) == null)) {
                List O = c.m1006O(DiscoverDataTags.vO.getInt());
                if (O == null || O.isEmpty() || O.get(0) == null) {
                    Log.m286e("DCSDK_", "checkAidInAdf, fci template list is empty.");
                    return false;
                }
                c = BERTLV.m1005c(((ByteBuffer) O.get(0)).getBytes(), 0, ((ByteBuffer) O.get(0)).getSize());
                Log.m285d("DCSDK_", "checkAidInAdf, returned parsed proprietaryTemplate.");
                if (!(c == null || c.m1006O(DiscoverDataTags.vP.getInt()) == null)) {
                    List O2 = c.m1006O(DiscoverDataTags.vP.getInt());
                    if (O2 == null || O2.isEmpty() || O2.get(0) == null) {
                        Log.m286e("DCSDK_", "checkAidInAdf, df not found empty.");
                        return false;
                    }
                    byteBuffer3 = (ByteBuffer) O2.get(0);
                    if (byteBuffer3 != null) {
                        Log.m286e("DCSDK_", "checkAidInAdf, df is null.");
                        return false;
                    }
                    toHexString = byteBuffer3.toHexString();
                    Log.m287i("DCSDK_", "checkAidInAdf, df: " + toHexString);
                    toHexString2 = byteBuffer.toHexString();
                    z = toHexString.contains(toHexString2) && toHexString3.contains(toHexString2);
                    return z;
                }
            }
            byteBuffer3 = null;
            if (byteBuffer3 != null) {
                toHexString = byteBuffer3.toHexString();
                Log.m287i("DCSDK_", "checkAidInAdf, df: " + toHexString);
                toHexString2 = byteBuffer.toHexString();
                if (!toHexString.contains(toHexString2)) {
                }
                return z;
            }
            Log.m286e("DCSDK_", "checkAidInAdf, df is null.");
            return false;
        }
    }

    private int m989c(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            Log.m286e("DCSDK_", "findAliasAid, aid is null.");
            return -1;
        }
        List aliasList = cM().getAliasList();
        if (aliasList == null || aliasList.isEmpty()) {
            Log.m286e("DCSDK_", "findAliasAid, alias list is empty.");
            return -1;
        }
        for (int i = 0; i < aliasList.size(); i++) {
            ByteBuffer byteBuffer2 = (ByteBuffer) aliasList.get(i);
            if (byteBuffer2 != null && byteBuffer2.toHexString().startsWith(byteBuffer.toHexString())) {
                return i;
            }
        }
        return -1;
    }
}
