package com.sec.android.app.hwmoduletest;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleDvfs;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import egis.client.api.EgisFingerprint;

public class HrmEolOnce extends BaseActivity implements OnClickListener {
    private final String CLASS_NAME = "HrmEolOnce";
    private int Font_Size = 10;
    private int Index;
    private final int WHAT_UPDATE_STATUS = 1;
    private String[] eolData;
    private boolean firstUIcreation = true;
    private boolean isChecking = false;
    private String[] itemName;
    private LayoutParams layoutParams;
    private TableRow.LayoutParams layoutParams_MatchParent;
    private TableRow.LayoutParams layoutParams_txt;
    private LayoutParams layoutParams_upper;
    private Button mButton_Exit;
    /* access modifiers changed from: private */
    public Button mButton_Start;
    private ModuleDvfs mDvfs = null;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what != 1) {
                return;
            }
            if (!HrmEolOnce.this.mhtm_Status) {
                HrmEolOnce.this.checkStatus();
                HrmEolOnce.this.mHandler.sendEmptyMessageDelayed(1, 100);
                return;
            }
            HrmEolOnce.this.registerSysfs(false);
            HrmEolOnce.this.mhtm_Status = false;
            HrmEolOnce.this.mButton_Start.setEnabled(true);
            HrmEolOnce.this.updateUI();
        }
    };
    private boolean mSpecResult = true;
    private TextView mTextResult;
    private String[] measure;
    private String[] measureString;
    /* access modifiers changed from: private */
    public boolean mhtm_Status = false;
    private int numOfItem = 0;
    private String[] result;
    private String[] specMax;
    private String[] specMin;
    private TableLayout tableLayout;
    private TextView[] txt_itemName;
    private TextView[] txt_measure;
    private TextView[] txt_result;
    private boolean updatedState = false;

    public HrmEolOnce() {
        super("HrmEolOnce");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.hrm_eoltest);
        this.mDvfs = new ModuleDvfs(this);
        initUI();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mDvfs.setCpuBoost();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mHandler.removeMessages(1);
        if (this.isChecking) {
            registerSysfs(false);
        }
        this.mDvfs.disableCpuBoost();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0268R.C0269id.start_button /*2131296460*/:
                LtUtil.log_i("HrmEolOnce", "onClick", "start_button Click");
                registerSysfs(true);
                for (int i = 0; i < this.numOfItem; i++) {
                    this.txt_measure[i].setText("");
                    this.txt_result[i].setText("");
                }
                this.mTextResult.setText("Do not Move & please wait a moment");
                this.mTextResult.setTextColor(-16777216);
                this.mButton_Start.setEnabled(false);
                this.mSpecResult = true;
                this.mHandler.sendEmptyMessageDelayed(1, 3000);
                return;
            case C0268R.C0269id.exit_button /*2131296461*/:
                LtUtil.log_i("HrmEolOnce", "onClick", "exit_button Click");
                finish();
                return;
            default:
                return;
        }
    }

    private void initUI() {
        this.mTextResult = (TextView) findViewById(C0268R.C0269id.final_result);
        this.mTextResult.setTextSize(1, 20.0f);
        this.layoutParams_upper = new LayoutParams(-1, -2);
        this.layoutParams = new LayoutParams(-1, -2);
        this.layoutParams_txt = new TableRow.LayoutParams(-1, -2);
        this.layoutParams_MatchParent = new TableRow.LayoutParams(-1, -1);
        this.tableLayout = (TableLayout) findViewById(C0268R.C0269id.table);
        TextView classification1 = new TextView(this);
        TextView classification2 = new TextView(this);
        TextView classification3 = new TextView(this);
        TableRow tableRow_classification = new TableRow(this);
        this.layoutParams_upper.setMargins(2, 2, 2, 2);
        tableRow_classification.setLayoutParams(this.layoutParams_upper);
        classification1.setText("Item");
        classification2.setText("Measure");
        classification3.setText("Result");
        classification1.setTextSize(1, (float) this.Font_Size);
        classification2.setTextSize(1, (float) this.Font_Size);
        classification3.setTextSize(1, (float) this.Font_Size);
        classification1.setGravity(1);
        classification2.setGravity(1);
        classification3.setGravity(1);
        classification1.setBackgroundColor(-1);
        classification2.setBackgroundColor(-1);
        classification3.setBackgroundColor(-1);
        classification1.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 1));
        classification2.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 1));
        classification3.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 1));
        classification1.setTextColor(-16777216);
        classification2.setTextColor(-16777216);
        classification3.setTextColor(-16777216);
        classification1.setBackgroundColor(-256);
        classification2.setBackgroundColor(-256);
        classification3.setBackgroundColor(-256);
        this.layoutParams_txt.setMargins(1, 0, 1, 0);
        this.layoutParams_MatchParent.setMargins(1, 0, 1, 0);
        classification1.setLayoutParams(this.layoutParams_txt);
        classification2.setLayoutParams(this.layoutParams_txt);
        classification3.setLayoutParams(this.layoutParams_txt);
        tableRow_classification.addView(classification1);
        tableRow_classification.addView(classification2);
        tableRow_classification.addView(classification3);
        this.tableLayout.addView(tableRow_classification);
        this.mButton_Start = (Button) findViewById(C0268R.C0269id.start_button);
        this.mButton_Exit = (Button) findViewById(C0268R.C0269id.exit_button);
        this.mButton_Start.setOnClickListener(this);
        this.mButton_Exit.setOnClickListener(this);
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        this.updatedState = updateResult();
        if (this.updatedState) {
            if (this.firstUIcreation) {
                this.txt_itemName = new TextView[this.numOfItem];
                this.txt_measure = new TextView[this.numOfItem];
                this.txt_result = new TextView[this.numOfItem];
            }
            for (int i = 0; i < this.numOfItem; i++) {
                if (this.firstUIcreation) {
                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(this.layoutParams);
                    this.layoutParams.setMargins(2, 0, 2, 2);
                    this.txt_itemName[i] = new TextView(this);
                    TextView textView = this.txt_itemName[i];
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.itemName[i]);
                    sb.append(addSpec(i));
                    textView.setText(sb.toString());
                    this.txt_itemName[i].setTextSize(1, (float) this.Font_Size);
                    this.txt_itemName[i].setGravity(17);
                    this.txt_itemName[i].setBackgroundColor(-1);
                    this.txt_itemName[i].setTextColor(-16777216);
                    this.txt_itemName[i].setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 1));
                    this.txt_itemName[i].setLayoutParams(this.layoutParams_txt);
                    tableRow.addView(this.txt_itemName[i]);
                    this.txt_measure[i] = new TextView(this);
                    this.txt_measure[i].setTextSize(1, (float) this.Font_Size);
                    this.txt_measure[i].setGravity(17);
                    this.txt_measure[i].setBackgroundColor(-1);
                    this.txt_measure[i].setTextColor(-16777216);
                    this.txt_measure[i].setLayoutParams(this.layoutParams_MatchParent);
                    tableRow.addView(this.txt_measure[i]);
                    this.txt_result[i] = new TextView(this);
                    this.txt_result[i].setTextSize(1, (float) this.Font_Size);
                    this.txt_result[i].setGravity(17);
                    this.txt_result[i].setBackgroundColor(-1);
                    this.txt_result[i].setTextColor(-16776961);
                    this.txt_result[i].setLayoutParams(this.layoutParams_MatchParent);
                    tableRow.addView(this.txt_result[i]);
                    this.tableLayout.addView(tableRow);
                }
                this.txt_measure[i].setText(this.measure[i]);
                this.txt_result[i].setText(this.result[i]);
                if ("PASS".equals(this.result[i])) {
                    this.txt_result[i].setTextColor(-16776961);
                } else if ("FAIL".equals(this.result[i])) {
                    this.txt_result[i].setTextColor(-65536);
                }
            }
            if (this.firstUIcreation != 0) {
                this.firstUIcreation = false;
            }
        }
        checkPassFail();
    }

    private void checkPassFail() {
        if (!this.updatedState || !this.mSpecResult) {
            this.mTextResult.setText("FAIL");
            this.mTextResult.setTextColor(-65536);
            return;
        }
        this.mTextResult.setText("PASS");
        this.mTextResult.setTextColor(-16776961);
    }

    private String addSpec(int Index2) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("\n");
        String Spec = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Spec);
        sb2.append("(");
        sb2.append(this.specMin[Index2]);
        sb2.append("~");
        sb2.append(this.specMax[Index2]);
        sb2.append(")");
        return sb2.toString();
    }

    public void registerSysfs(boolean mStatus) {
        LtUtil.log_i("HrmEolOnce", "registerSysfs", "registerSysfs Node");
        if (mStatus) {
            Kernel.write(Kernel.HRM_EOL_TEST, EgisFingerprint.MAJOR_VERSION);
            this.isChecking = true;
            return;
        }
        Kernel.write(Kernel.HRM_EOL_TEST, "0");
        this.isChecking = false;
    }

    public void checkStatus() {
        String mStatus = Kernel.read(Kernel.HRM_EOL_TEST_STATUS);
        if (mStatus == null || !mStatus.equals(EgisFingerprint.MAJOR_VERSION)) {
            LtUtil.log_i("HrmEolOnce", "checkStatus", "Sysfs is not ready");
            return;
        }
        LtUtil.log_i("HrmEolOnce", "checkStatus", "Sysfs is ready");
        this.mhtm_Status = true;
    }

    public boolean updateResult() {
        String str = "";
        String mTempData = Kernel.read(Kernel.HRM_EOL_TEST_RESULT);
        if (TextUtils.isEmpty(mTempData)) {
            LtUtil.log_d("HrmEolOnce", "updateResult", "mTempData is empty, return false");
            return false;
        }
        this.eolData = mTempData.trim().split(",");
        this.numOfItem = Integer.parseInt(this.eolData[1]);
        StringBuilder sb = new StringBuilder();
        sb.append("Number of test item = (");
        sb.append(this.numOfItem);
        sb.append(")");
        LtUtil.log_i("HrmEolOnce", "updateResult", sb.toString());
        if (this.eolData.length - 2 != this.numOfItem * 5) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("(eolData length error) eolData.length = ");
            sb2.append(this.eolData.length);
            LtUtil.log_i("HrmEolOnce", "updateResult", sb2.toString());
            return false;
        }
        this.itemName = new String[this.numOfItem];
        this.result = new String[this.numOfItem];
        this.measure = new String[this.numOfItem];
        this.specMin = new String[this.numOfItem];
        this.specMax = new String[this.numOfItem];
        int dataIndex = 2;
        int i = 0;
        while (i < this.numOfItem) {
            int dataIndex2 = dataIndex + 1;
            this.itemName[i] = this.eolData[dataIndex];
            int dataIndex3 = dataIndex2 + 1;
            this.result[i] = this.eolData[dataIndex2];
            if (EgisFingerprint.MAJOR_VERSION.equals(this.result[i])) {
                this.result[i] = "PASS";
                StringBuilder sb3 = new StringBuilder();
                sb3.append("PASS : ");
                sb3.append(this.itemName[i]);
                LtUtil.log_i("HrmEolOnce", "updateResult", sb3.toString());
            } else {
                this.result[i] = "FAIL";
                this.mSpecResult = false;
            }
            int dataIndex4 = dataIndex3 + 1;
            this.measure[i] = this.eolData[dataIndex3];
            int dataIndex5 = dataIndex4 + 1;
            this.specMin[i] = this.eolData[dataIndex4];
            int dataIndex6 = dataIndex5 + 1;
            this.specMax[i] = this.eolData[dataIndex5];
            i++;
            dataIndex = dataIndex6;
        }
        return true;
    }
}
