package com.sec.android.app.hwmoduletest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Arrays;

public class BarcodeEmulTest extends ListActivity implements OnItemClickListener {
    private ArrayAdapter<BeamTask> adapter;
    private TextView mBarcodeEmulTitleText;
    private ListView mListView;
    private BeamTask[] tasks = {new BeamTask("UPC-A", "123456789012", "FF AC DB 21 5C 9D 42 AE DB 8B 1A 64 9A FF"), new BeamTask("EAN-13", "1234567890128", "FF AD 90 B1 4E F5 BA AD C5 8D 32 4D BA FF"), new BeamTask("EAN-8", "12345670", "FF EB 36 C8 57 2A C5 7B B1 AB FF"), new BeamTask("Code 39", "Code-39", "FF F7 44 51 17 51 45 D5 1D 11 47 57 51 11 1D 54 74 57 44 5F FF"), new BeamTask("Code 128", "Code-128", "FF CB 7B B9 70 AF 65 37 B2 36 32 63 45 98 D9 38 A7 FF"), new BeamTask("Interleaved 2 of 5", "12345678", "FF F5 2D 4C 96 B2 CD 5A 99 2F FF"), new BeamTask("Codabar", "A1234567890A", "FF E9 B5 4D 5A 4D 54 B4 AD 6A 5A 56 54 B5 56 53 6F FF"), new BeamTask("GS1 Databar", "(01)00614141999996", "DE BB C8 07 A6 7A 11 BD DA 1F 8C F6 E4 80 A3 81 EB")};

    public static class BeamTask {
        private static byte[] fromHex;
        private static char[] toHex = "0123456789abcdef".toCharArray();
        String data;
        byte[] seq;
        String symbology;

        public BeamTask() {
        }

        public BeamTask(String symbology2, String data2, byte[] seq2) {
            this.symbology = symbology2;
            this.data = data2;
            this.seq = seq2;
        }

        public BeamTask(String symbology2, String data2, String seqHex) {
            this(symbology2, data2, parseHexString(seqHex.replaceAll("\\s", "")));
        }

        public String toString() {
            return this.symbology;
        }

        public String toHexString(byte[] b) {
            return toHexString(b, b.length);
        }

        public String toHexString(byte[] b, int size) {
            char[] c = new char[(size << 1)];
            int pos = 0;
            for (int i = 0; i < size; i++) {
                int pos2 = pos + 1;
                c[pos] = toHex[(b[i] >> 4) & 15];
                pos = pos2 + 1;
                c[pos2] = toHex[b[i] & HwModuleTest.ID_GRIP];
            }
            return new String(c);
        }

        private static synchronized byte[] getHexTable() {
            byte[] bArr;
            synchronized (BeamTask.class) {
                if (fromHex == null) {
                    fromHex = new byte[256];
                    Arrays.fill(fromHex, -1);
                    char[] cArr = toHex;
                    int length = cArr.length;
                    int i = 0;
                    int pos = 0;
                    int pos2 = 0;
                    while (pos2 < length) {
                        int pos3 = pos + 1;
                        fromHex[cArr[pos2]] = (byte) pos;
                        pos2++;
                        pos = pos3;
                    }
                    int pos4 = 10;
                    char[] charArray = "ABCDEF".toCharArray();
                    int length2 = charArray.length;
                    while (i < length2) {
                        int pos5 = pos4 + 1;
                        fromHex[charArray[i]] = (byte) pos4;
                        i++;
                        pos4 = pos5;
                    }
                }
                bArr = fromHex;
            }
            return bArr;
        }

        public static byte[] parseHexString(String s) {
            if ((s.length() & 1) == 0) {
                byte[] hexTable = getHexTable();
                char[] x = s.toCharArray();
                byte[] buf = new byte[(x.length >>> 1)];
                int i = 0;
                int pos = 0;
                while (pos < buf.length) {
                    int i2 = i + 1;
                    byte a = hexTable[x[i]];
                    int i3 = i2 + 1;
                    byte i4 = hexTable[x[i2]];
                    if (a < 0 || i4 < 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Invalid hex characters: ");
                        sb.append(x[i3 - 2]);
                        sb.append(x[i3 - 1]);
                        throw new NumberFormatException(sb.toString());
                    }
                    buf[pos] = (byte) ((a << 4) | i4);
                    pos++;
                    i = i3;
                }
                return buf;
            }
            throw new RuntimeException("Invalid hex string - odd length.");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.barcode_emul_test);
        this.mListView = (ListView) findViewById(16908298);
        ListView listView = this.mListView;
        ArrayAdapter<BeamTask> arrayAdapter = new ArrayAdapter<>(this, C0268R.layout.item, C0268R.C0269id.textView1, this.tasks);
        this.adapter = arrayAdapter;
        listView.setAdapter(arrayAdapter);
        this.mListView.setOnItemClickListener(this);
        this.mBarcodeEmulTitleText = (TextView) findViewById(C0268R.C0269id.barcodetitle);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent i = new Intent(this, BeamActivity.class);
        i.putExtra(BeamActivity.POSITION_KEY, position);
        startActivity(i);
    }
}
