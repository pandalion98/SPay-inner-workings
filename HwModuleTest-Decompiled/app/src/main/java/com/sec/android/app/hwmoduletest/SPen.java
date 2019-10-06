package com.sec.android.app.hwmoduletest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.TestCase;

public class SPen extends BaseActivity implements OnClickListener {
    private Button b_SPenDraw;
    private Button b_SPenDraw2;
    private Button b_SPenHover;
    private Button b_SpenKey;

    public SPen() {
        super("SPen");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.spen);
        LtUtil.log_i(this.CLASS_NAME, "onCreate", "");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LtUtil.log_i(this.CLASS_NAME, "onResume", "");
        InitilizeUI();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_i(this.CLASS_NAME, "onPause", "");
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LtUtil.log_i(this.CLASS_NAME, "onDestroy", "");
        super.onDestroy();
    }

    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case C0268R.C0269id.spen_draw_btn /*2131296646*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "SPEN Draw!");
                intent.setClass(this, SPenDrawTest.class);
                break;
            case C0268R.C0269id.spen_hover_btn /*2131296647*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "SPEN Hover!");
                intent.setClass(this, SpenHoveringDrawTest.class);
                break;
            case C0268R.C0269id.spen_key_btn /*2131296648*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "SPEN Key!");
                intent.setClass(this, SPenKeyTest.class);
                break;
            case C0268R.C0269id.spen_draw2_btn /*2131296649*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "SPEN Draw2!");
                intent.setClass(this, SpenAccuracyTest.class);
                break;
        }
        startActivity(intent);
    }

    private void InitilizeUI() {
        LtUtil.log_i(this.CLASS_NAME, "InitilizeUI", "");
        this.b_SPenDraw = (Button) findViewById(C0268R.C0269id.spen_draw_btn);
        this.b_SPenDraw2 = (Button) findViewById(C0268R.C0269id.spen_draw2_btn);
        this.b_SPenHover = (Button) findViewById(C0268R.C0269id.spen_hover_btn);
        this.b_SpenKey = (Button) findViewById(C0268R.C0269id.spen_key_btn);
        this.b_SPenDraw.setOnClickListener(this);
        this.b_SPenDraw2.setOnClickListener(this);
        this.b_SPenHover.setOnClickListener(this);
        this.b_SpenKey.setOnClickListener(this);
        if (!IsSpenKeytest()) {
            this.b_SpenKey.setVisibility(8);
        }
    }

    private boolean IsSpenKeytest() {
        if (!TestCase.getEnabled(TestCase.KEY_TEST_BACK) || "hard".equals(TestCase.getKeyType(TestCase.KEY_TEST_BACK))) {
            return false;
        }
        return true;
    }
}
