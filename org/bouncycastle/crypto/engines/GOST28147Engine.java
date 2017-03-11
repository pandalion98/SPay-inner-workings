package org.bouncycastle.crypto.engines;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import java.util.Hashtable;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class GOST28147Engine implements BlockCipher {
    protected static final int BLOCK_SIZE = 8;
    private static byte[] DSbox_A;
    private static byte[] DSbox_Test;
    private static byte[] ESbox_A;
    private static byte[] ESbox_B;
    private static byte[] ESbox_C;
    private static byte[] ESbox_D;
    private static byte[] ESbox_Test;
    private static byte[] Sbox_Default;
    private static Hashtable sBoxes;
    private byte[] f153S;
    private boolean forEncryption;
    private int[] workingKey;

    static {
        Sbox_Default = new byte[]{(byte) 4, (byte) 10, (byte) 9, (byte) 2, (byte) 13, (byte) 8, (byte) 0, (byte) 14, (byte) 6, (byte) 11, (byte) 1, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 15, (byte) 5, (byte) 3, (byte) 14, (byte) 11, (byte) 4, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 6, (byte) 13, (byte) 15, (byte) 10, (byte) 2, (byte) 3, (byte) 8, (byte) 1, (byte) 0, (byte) 7, (byte) 5, (byte) 9, (byte) 5, (byte) 8, (byte) 1, (byte) 13, (byte) 10, (byte) 3, (byte) 4, (byte) 2, (byte) 14, (byte) 15, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 6, (byte) 0, (byte) 9, (byte) 11, (byte) 7, (byte) 13, (byte) 10, (byte) 1, (byte) 0, (byte) 8, (byte) 9, (byte) 15, (byte) 14, (byte) 4, (byte) 6, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 11, (byte) 2, (byte) 5, (byte) 3, (byte) 6, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 1, (byte) 5, (byte) 15, (byte) 13, (byte) 8, (byte) 4, (byte) 10, (byte) 9, (byte) 14, (byte) 0, (byte) 3, (byte) 11, (byte) 2, (byte) 4, (byte) 11, (byte) 10, (byte) 0, (byte) 7, (byte) 2, (byte) 1, (byte) 13, (byte) 3, (byte) 6, (byte) 8, (byte) 5, (byte) 9, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 15, (byte) 14, (byte) 13, (byte) 11, (byte) 4, (byte) 1, (byte) 3, (byte) 15, (byte) 5, (byte) 9, (byte) 0, (byte) 10, (byte) 14, (byte) 7, (byte) 6, (byte) 8, (byte) 2, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 1, (byte) 15, (byte) 13, (byte) 0, (byte) 5, (byte) 7, (byte) 10, (byte) 4, (byte) 9, (byte) 2, (byte) 3, (byte) 14, (byte) 6, (byte) 11, (byte) 8, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG};
        ESbox_Test = new byte[]{(byte) 4, (byte) 2, (byte) 15, (byte) 5, (byte) 9, (byte) 1, (byte) 0, (byte) 8, (byte) 14, (byte) 3, (byte) 11, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 13, (byte) 7, (byte) 10, (byte) 6, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 9, (byte) 15, (byte) 14, (byte) 8, (byte) 1, (byte) 3, (byte) 10, (byte) 2, (byte) 7, (byte) 4, (byte) 13, (byte) 6, (byte) 0, (byte) 11, (byte) 5, (byte) 13, (byte) 8, (byte) 14, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 3, (byte) 9, (byte) 10, (byte) 1, (byte) 5, (byte) 2, (byte) 4, (byte) 6, (byte) 15, (byte) 0, (byte) 11, (byte) 14, (byte) 9, (byte) 11, (byte) 2, (byte) 5, (byte) 15, (byte) 7, (byte) 1, (byte) 0, (byte) 13, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 6, (byte) 10, (byte) 4, (byte) 3, (byte) 8, (byte) 3, (byte) 14, (byte) 5, (byte) 9, (byte) 6, (byte) 8, (byte) 0, (byte) 13, (byte) 10, (byte) 11, (byte) 7, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 2, (byte) 1, (byte) 15, (byte) 4, (byte) 8, (byte) 15, (byte) 6, (byte) 11, (byte) 1, (byte) 9, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 5, (byte) 13, (byte) 3, (byte) 7, (byte) 10, (byte) 0, (byte) 14, (byte) 2, (byte) 4, (byte) 9, (byte) 11, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 0, (byte) 3, (byte) 6, (byte) 7, (byte) 5, (byte) 4, (byte) 8, (byte) 14, (byte) 15, (byte) 1, (byte) 10, (byte) 2, (byte) 13, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 6, (byte) 5, (byte) 2, (byte) 11, (byte) 0, (byte) 9, (byte) 13, (byte) 3, (byte) 14, (byte) 7, (byte) 10, (byte) 15, (byte) 4, (byte) 1, (byte) 8};
        ESbox_A = new byte[]{(byte) 9, (byte) 6, (byte) 3, (byte) 2, (byte) 8, (byte) 11, (byte) 1, (byte) 7, (byte) 10, (byte) 4, (byte) 14, (byte) 15, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 0, (byte) 13, (byte) 5, (byte) 3, (byte) 7, (byte) 14, (byte) 9, (byte) 8, (byte) 10, (byte) 15, (byte) 0, (byte) 5, (byte) 2, (byte) 6, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 11, (byte) 4, (byte) 13, (byte) 1, (byte) 14, (byte) 4, (byte) 6, (byte) 2, (byte) 11, (byte) 3, (byte) 13, (byte) 8, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 15, (byte) 5, (byte) 10, (byte) 0, (byte) 7, (byte) 1, (byte) 9, (byte) 14, (byte) 7, (byte) 10, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 13, (byte) 1, (byte) 3, (byte) 9, (byte) 0, (byte) 2, (byte) 11, (byte) 4, (byte) 15, (byte) 8, (byte) 5, (byte) 6, (byte) 11, (byte) 5, (byte) 1, (byte) 9, (byte) 8, (byte) 13, (byte) 15, (byte) 0, (byte) 14, (byte) 4, (byte) 2, (byte) 3, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 10, (byte) 6, (byte) 3, (byte) 10, (byte) 13, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 1, (byte) 2, (byte) 0, (byte) 11, (byte) 7, (byte) 5, (byte) 9, (byte) 4, (byte) 8, (byte) 15, (byte) 14, (byte) 6, (byte) 1, (byte) 13, (byte) 2, (byte) 9, (byte) 7, (byte) 10, (byte) 6, (byte) 0, (byte) 8, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 4, (byte) 5, (byte) 15, (byte) 3, (byte) 11, (byte) 14, (byte) 11, (byte) 10, (byte) 15, (byte) 5, (byte) 0, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 14, (byte) 8, (byte) 6, (byte) 2, (byte) 3, (byte) 9, (byte) 1, (byte) 7, (byte) 13, (byte) 4};
        ESbox_B = new byte[]{(byte) 8, (byte) 4, (byte) 11, (byte) 1, (byte) 3, (byte) 5, (byte) 0, (byte) 9, (byte) 2, (byte) 14, (byte) 10, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 13, (byte) 6, (byte) 7, (byte) 15, (byte) 0, (byte) 1, (byte) 2, (byte) 10, (byte) 4, (byte) 13, (byte) 5, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 9, (byte) 7, (byte) 3, (byte) 15, (byte) 11, (byte) 8, (byte) 6, (byte) 14, (byte) 14, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 0, (byte) 10, (byte) 9, (byte) 2, (byte) 13, (byte) 11, (byte) 7, (byte) 5, (byte) 8, (byte) 15, (byte) 3, (byte) 6, (byte) 1, (byte) 4, (byte) 7, (byte) 5, (byte) 0, (byte) 13, (byte) 11, (byte) 6, (byte) 1, (byte) 2, (byte) 3, (byte) 10, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 15, (byte) 4, (byte) 14, (byte) 9, (byte) 8, (byte) 2, (byte) 7, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 15, (byte) 9, (byte) 5, (byte) 10, (byte) 11, (byte) 1, (byte) 4, (byte) 0, (byte) 13, (byte) 6, (byte) 8, (byte) 14, (byte) 3, (byte) 8, (byte) 3, (byte) 2, (byte) 6, (byte) 4, (byte) 13, (byte) 14, (byte) 11, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 1, (byte) 7, (byte) 15, (byte) 10, (byte) 0, (byte) 9, (byte) 5, (byte) 5, (byte) 2, (byte) 10, (byte) 11, (byte) 9, (byte) 1, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 3, (byte) 7, (byte) 4, (byte) 13, (byte) 0, (byte) 6, (byte) 15, (byte) 8, (byte) 14, (byte) 0, (byte) 4, (byte) 11, (byte) 14, (byte) 8, (byte) 3, (byte) 7, (byte) 1, (byte) 10, (byte) 2, (byte) 9, (byte) 6, (byte) 15, (byte) 13, (byte) 5, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG};
        ESbox_C = new byte[]{(byte) 1, (byte) 11, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 2, (byte) 9, (byte) 13, (byte) 0, (byte) 15, (byte) 4, (byte) 5, (byte) 8, (byte) 14, (byte) 10, (byte) 7, (byte) 6, (byte) 3, (byte) 0, (byte) 1, (byte) 7, (byte) 13, (byte) 11, (byte) 4, (byte) 5, (byte) 2, (byte) 8, (byte) 14, (byte) 15, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 9, (byte) 10, (byte) 6, (byte) 3, (byte) 8, (byte) 2, (byte) 5, (byte) 0, (byte) 4, (byte) 9, (byte) 15, (byte) 10, (byte) 3, (byte) 7, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 13, (byte) 6, (byte) 14, (byte) 1, (byte) 11, (byte) 3, (byte) 6, (byte) 0, (byte) 1, (byte) 5, (byte) 13, (byte) 10, (byte) 8, (byte) 11, (byte) 2, (byte) 9, (byte) 7, (byte) 14, (byte) 15, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 4, (byte) 8, (byte) 13, (byte) 11, (byte) 0, (byte) 4, (byte) 5, (byte) 1, (byte) 2, (byte) 9, (byte) 3, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 14, (byte) 6, (byte) 15, (byte) 10, (byte) 7, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 9, (byte) 11, (byte) 1, (byte) 8, (byte) 14, (byte) 2, (byte) 4, (byte) 7, (byte) 3, (byte) 6, (byte) 5, (byte) 10, (byte) 0, (byte) 15, (byte) 13, (byte) 10, (byte) 9, (byte) 6, (byte) 8, (byte) 13, (byte) 14, (byte) 2, (byte) 0, (byte) 15, (byte) 3, (byte) 5, (byte) 11, (byte) 4, (byte) 1, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 7, (byte) 4, (byte) 0, (byte) 5, (byte) 10, (byte) 2, (byte) 15, (byte) 14, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 6, (byte) 1, (byte) 11, (byte) 13, (byte) 9, (byte) 3, (byte) 8};
        ESbox_D = new byte[]{(byte) 15, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 2, (byte) 10, (byte) 6, (byte) 4, (byte) 5, (byte) 0, (byte) 7, (byte) 9, (byte) 14, (byte) 13, (byte) 1, (byte) 11, (byte) 8, (byte) 3, (byte) 11, (byte) 6, (byte) 3, (byte) 4, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 15, (byte) 14, (byte) 2, (byte) 7, (byte) 13, (byte) 8, (byte) 0, (byte) 5, (byte) 10, (byte) 9, (byte) 1, (byte) 1, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 11, (byte) 0, (byte) 15, (byte) 14, (byte) 6, (byte) 5, (byte) 10, (byte) 13, (byte) 4, (byte) 8, (byte) 9, (byte) 3, (byte) 7, (byte) 2, (byte) 1, (byte) 5, (byte) 14, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 10, (byte) 7, (byte) 0, (byte) 13, (byte) 6, (byte) 2, (byte) 11, (byte) 4, (byte) 9, (byte) 3, (byte) 15, (byte) 8, (byte) 0, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 8, (byte) 9, (byte) 13, (byte) 2, (byte) 10, (byte) 11, (byte) 7, (byte) 3, (byte) 6, (byte) 5, (byte) 4, (byte) 14, (byte) 15, (byte) 1, (byte) 8, (byte) 0, (byte) 15, (byte) 3, (byte) 2, (byte) 5, (byte) 14, (byte) 11, (byte) 1, (byte) 10, (byte) 4, (byte) 7, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 9, (byte) 13, (byte) 6, (byte) 3, (byte) 0, (byte) 6, (byte) 15, (byte) 1, (byte) 14, (byte) 9, (byte) 2, (byte) 13, (byte) 8, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 4, (byte) 11, (byte) 10, (byte) 5, (byte) 7, (byte) 1, (byte) 10, (byte) 6, (byte) 8, (byte) 15, (byte) 11, (byte) 0, (byte) 4, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 3, (byte) 5, (byte) 9, (byte) 7, (byte) 13, (byte) 2, (byte) 14};
        DSbox_Test = new byte[]{(byte) 4, (byte) 10, (byte) 9, (byte) 2, (byte) 13, (byte) 8, (byte) 0, (byte) 14, (byte) 6, (byte) 11, (byte) 1, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 15, (byte) 5, (byte) 3, (byte) 14, (byte) 11, (byte) 4, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 6, (byte) 13, (byte) 15, (byte) 10, (byte) 2, (byte) 3, (byte) 8, (byte) 1, (byte) 0, (byte) 7, (byte) 5, (byte) 9, (byte) 5, (byte) 8, (byte) 1, (byte) 13, (byte) 10, (byte) 3, (byte) 4, (byte) 2, (byte) 14, (byte) 15, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 6, (byte) 0, (byte) 9, (byte) 11, (byte) 7, (byte) 13, (byte) 10, (byte) 1, (byte) 0, (byte) 8, (byte) 9, (byte) 15, (byte) 14, (byte) 4, (byte) 6, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 11, (byte) 2, (byte) 5, (byte) 3, (byte) 6, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 1, (byte) 5, (byte) 15, (byte) 13, (byte) 8, (byte) 4, (byte) 10, (byte) 9, (byte) 14, (byte) 0, (byte) 3, (byte) 11, (byte) 2, (byte) 4, (byte) 11, (byte) 10, (byte) 0, (byte) 7, (byte) 2, (byte) 1, (byte) 13, (byte) 3, (byte) 6, (byte) 8, (byte) 5, (byte) 9, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 15, (byte) 14, (byte) 13, (byte) 11, (byte) 4, (byte) 1, (byte) 3, (byte) 15, (byte) 5, (byte) 9, (byte) 0, (byte) 10, (byte) 14, (byte) 7, (byte) 6, (byte) 8, (byte) 2, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 1, (byte) 15, (byte) 13, (byte) 0, (byte) 5, (byte) 7, (byte) 10, (byte) 4, (byte) 9, (byte) 2, (byte) 3, (byte) 14, (byte) 6, (byte) 11, (byte) 8, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG};
        DSbox_A = new byte[]{(byte) 10, (byte) 4, (byte) 5, (byte) 6, (byte) 8, (byte) 1, (byte) 3, (byte) 7, (byte) 13, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 14, (byte) 0, (byte) 9, (byte) 2, (byte) 11, (byte) 15, (byte) 5, (byte) 15, (byte) 4, (byte) 0, (byte) 2, (byte) 13, (byte) 11, (byte) 9, (byte) 1, (byte) 7, (byte) 6, (byte) 3, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 14, (byte) 10, (byte) 8, (byte) 7, (byte) 15, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 14, (byte) 9, (byte) 4, (byte) 1, (byte) 0, (byte) 3, (byte) 11, (byte) 5, (byte) 2, (byte) 6, (byte) 10, (byte) 8, (byte) 13, (byte) 4, (byte) 10, (byte) 7, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 0, (byte) 15, (byte) 2, (byte) 8, (byte) 14, (byte) 1, (byte) 6, (byte) 5, (byte) 13, (byte) 11, (byte) 9, (byte) 3, (byte) 7, (byte) 6, (byte) 4, (byte) 11, (byte) 9, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 2, (byte) 10, (byte) 1, (byte) 8, (byte) 0, (byte) 14, (byte) 15, (byte) 13, (byte) 3, (byte) 5, (byte) 7, (byte) 6, (byte) 2, (byte) 4, (byte) 13, (byte) 9, (byte) 15, (byte) 0, (byte) 10, (byte) 1, (byte) 5, (byte) 11, (byte) 8, (byte) 14, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 3, (byte) 13, (byte) 14, (byte) 4, (byte) 1, (byte) 7, (byte) 0, (byte) 5, (byte) 10, (byte) 3, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 8, (byte) 15, (byte) 6, (byte) 2, (byte) 9, (byte) 11, (byte) 1, (byte) 3, (byte) 10, (byte) 9, (byte) 5, (byte) 11, (byte) 4, (byte) 15, (byte) 8, (byte) 6, (byte) 7, (byte) 14, (byte) 13, (byte) 0, (byte) 2, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG};
        sBoxes = new Hashtable();
        addSBox("Default", Sbox_Default);
        addSBox("E-TEST", ESbox_Test);
        addSBox("E-A", ESbox_A);
        addSBox("E-B", ESbox_B);
        addSBox("E-C", ESbox_C);
        addSBox("E-D", ESbox_D);
        addSBox("D-TEST", DSbox_Test);
        addSBox("D-A", DSbox_A);
    }

    public GOST28147Engine() {
        this.workingKey = null;
        this.f153S = Sbox_Default;
    }

    private void GOST28147Func(int[] iArr, byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 7;
        int bytesToint = bytesToint(bArr, i);
        int bytesToint2 = bytesToint(bArr, i + 4);
        int i4;
        int i5;
        int i6;
        if (this.forEncryption) {
            i4 = 0;
            while (i4 < 3) {
                i5 = bytesToint;
                bytesToint = bytesToint2;
                bytesToint2 = 0;
                while (bytesToint2 < BLOCK_SIZE) {
                    bytesToint2++;
                    i6 = i5;
                    i5 = bytesToint ^ GOST28147_mainStep(i5, iArr[bytesToint2]);
                    bytesToint = i6;
                }
                i4++;
                bytesToint2 = bytesToint;
                bytesToint = i5;
            }
            while (i3 > 0) {
                i3--;
                i6 = bytesToint;
                bytesToint = bytesToint2 ^ GOST28147_mainStep(bytesToint, iArr[i3]);
                bytesToint2 = i6;
            }
        } else {
            i5 = 0;
            while (i5 < BLOCK_SIZE) {
                i4 = GOST28147_mainStep(bytesToint, iArr[i5]) ^ bytesToint2;
                i5++;
                bytesToint2 = bytesToint;
                bytesToint = i4;
            }
            i4 = 0;
            while (i4 < 3) {
                i5 = bytesToint;
                bytesToint = bytesToint2;
                bytesToint2 = 7;
                while (bytesToint2 >= 0 && (i4 != 2 || bytesToint2 != 0)) {
                    bytesToint2--;
                    i6 = i5;
                    i5 = bytesToint ^ GOST28147_mainStep(i5, iArr[bytesToint2]);
                    bytesToint = i6;
                }
                i4++;
                bytesToint2 = bytesToint;
                bytesToint = i5;
            }
        }
        bytesToint2 ^= GOST28147_mainStep(bytesToint, iArr[0]);
        intTobytes(bytesToint, bArr2, i2);
        intTobytes(bytesToint2, bArr2, i2 + 4);
    }

    private int GOST28147_mainStep(int i, int i2) {
        int i3 = i2 + i;
        i3 = (this.f153S[((i3 >> 28) & 15) + 112] << 28) + (((((((this.f153S[((i3 >> 0) & 15) + 0] << 0) + (this.f153S[((i3 >> 4) & 15) + 16] << 4)) + (this.f153S[((i3 >> BLOCK_SIZE) & 15) + 32] << BLOCK_SIZE)) + (this.f153S[((i3 >> 12) & 15) + 48] << 12)) + (this.f153S[((i3 >> 16) & 15) + 64] << 16)) + (this.f153S[((i3 >> 20) & 15) + 80] << 20)) + (this.f153S[((i3 >> 24) & 15) + 96] << 24));
        return (i3 >>> 21) | (i3 << 11);
    }

    private static void addSBox(String str, byte[] bArr) {
        sBoxes.put(Strings.toUpperCase(str), bArr);
    }

    private int bytesToint(byte[] bArr, int i) {
        return ((((bArr[i + 3] << 24) & ViewCompat.MEASURED_STATE_MASK) + ((bArr[i + 2] << 16) & 16711680)) + ((bArr[i + 1] << BLOCK_SIZE) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) + (bArr[i] & GF2Field.MASK);
    }

    private int[] generateWorkingKey(boolean z, byte[] bArr) {
        this.forEncryption = z;
        if (bArr.length != 32) {
            throw new IllegalArgumentException("Key length invalid. Key needs to be 32 byte - 256 bit!!!");
        }
        int[] iArr = new int[BLOCK_SIZE];
        for (int i = 0; i != BLOCK_SIZE; i++) {
            iArr[i] = bytesToint(bArr, i * 4);
        }
        return iArr;
    }

    public static byte[] getSBox(String str) {
        byte[] bArr = (byte[]) sBoxes.get(Strings.toUpperCase(str));
        if (bArr != null) {
            return Arrays.clone(bArr);
        }
        throw new IllegalArgumentException("Unknown S-Box - possible types: \"Default\", \"E-Test\", \"E-A\", \"E-B\", \"E-C\", \"E-D\", \"D-Test\", \"D-A\".");
    }

    private void intTobytes(int i, byte[] bArr, int i2) {
        bArr[i2 + 3] = (byte) (i >>> 24);
        bArr[i2 + 2] = (byte) (i >>> 16);
        bArr[i2 + 1] = (byte) (i >>> BLOCK_SIZE);
        bArr[i2] = (byte) i;
    }

    public String getAlgorithmName() {
        return "GOST28147";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithSBox) {
            ParametersWithSBox parametersWithSBox = (ParametersWithSBox) cipherParameters;
            byte[] sBox = parametersWithSBox.getSBox();
            if (sBox.length != Sbox_Default.length) {
                throw new IllegalArgumentException("invalid S-box passed to GOST28147 init");
            }
            this.f153S = Arrays.clone(sBox);
            if (parametersWithSBox.getParameters() != null) {
                this.workingKey = generateWorkingKey(z, ((KeyParameter) parametersWithSBox.getParameters()).getKey());
            }
        } else if (cipherParameters instanceof KeyParameter) {
            this.workingKey = generateWorkingKey(z, ((KeyParameter) cipherParameters).getKey());
        } else if (cipherParameters != null) {
            throw new IllegalArgumentException("invalid parameter passed to GOST28147 init - " + cipherParameters.getClass().getName());
        }
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("GOST28147 engine not initialised");
        } else if (i + BLOCK_SIZE > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + BLOCK_SIZE > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            GOST28147Func(this.workingKey, bArr, i, bArr2, i2);
            return BLOCK_SIZE;
        }
    }

    public void reset() {
    }
}
