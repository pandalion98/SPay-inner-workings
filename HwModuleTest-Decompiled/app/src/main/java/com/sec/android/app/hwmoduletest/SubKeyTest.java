package com.sec.android.app.hwmoduletest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.FactoryTest;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.goodix.cap.fingerprint.Constants;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;
import com.sec.xmldata.support.Support.TestCase;
import egis.client.api.EgisFingerprint;
import java.io.FileOutputStream;
import java.io.IOException;

public class SubKeyTest extends BaseActivity implements OnClickListener {
    private static final int KEYCODE_WINK = 1082;
    private static final String KEY_BIXBY = "BB";
    private static final String KEY_HOME = "HM";
    private static final String KEY_LONG_PRESS = "L";
    private static final String KEY_POWER = "PW";
    private static final String KEY_PRESS = "P";
    private static final String KEY_RELEASE = "R";
    private static final String KEY_SHORT_PRESS = "S";
    private static final String KEY_VOL_DOWN = "VD";
    private static final String KEY_VOL_UP = "VU";
    private static final int SCANCODE_BEAM = 273;
    private static final int SCANCODE_DUMMY_BACK1 = 250;
    private static final int SCANCODE_DUMMY_BACK2 = 253;
    private static final int SCANCODE_DUMMY_HOME = 252;
    private static final int SCANCODE_DUMMY_MENU1 = 251;
    private static final int SCANCODE_DUMMY_MENU2 = 249;
    private boolean IS_FOUR_KEY = Feature.getBoolean(Feature.SUBKEY_FOUR_KEY);
    boolean hasBackKey = true;
    private boolean isKeyPressed = false;
    private boolean isKeyPressedLong = false;
    boolean isPressed = false;
    boolean isSupportForceTouch = false;
    private Button mButtonExit;
    private final boolean mIsDummyKeySameKeycode = TestCase.getEnabled(TestCase.IS_DUMMYKEY_SAME_KEYCODE);
    private LinearLayout mLinearLayoutHomeHolder;
    private RelativeLayout mRelativeLayoutBackground;
    private FileOutputStream out = null;
    private int pressedKey = 0;
    private String result = "";

    public SubKeyTest() {
        super("SubKeyTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.subkeytest);
        initDisplay();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (FactoryTest.isFactoryBinary()) {
            Kernel.write(Kernel.PRESSURE_TOUCH_STATUS, EgisFingerprint.MAJOR_VERSION);
        }
        if ((Feature.getBoolean(Feature.SUBKEY_FOUR_KEY) || Feature.getBoolean(Feature.SUBKEY_FIVE_KEY) || Feature.getBoolean(Feature.SUBKEY_SIX_KEY)) && Kernel.isExistFileID(Kernel.FIVE_BUTTON)) {
            Kernel.write(Kernel.FIVE_BUTTON, EgisFingerprint.MAJOR_VERSION);
        }
        if (Feature.getBoolean(Feature.IS_DUMMYKEY_FACTORY_MODE) && Kernel.isExistFileID(Kernel.TSK_FACTORY_MODE)) {
            Kernel.write(Kernel.TSK_FACTORY_MODE, EgisFingerprint.MAJOR_VERSION);
        }
        if (Kernel.isExistFileID(Kernel.KEYPAD_BACKLIGHT_FAC)) {
            Kernel.write(Kernel.KEYPAD_BACKLIGHT_FAC, EgisFingerprint.MAJOR_VERSION);
        }
        super.onResume();
        if (isOqcsbftt) {
            NVAccessor.setNV(406, NVAccessor.NV_VALUE_ENTER);
            try {
                String secefsOqcsbfttDirectory = Kernel.getFilePath(Kernel.SECEFS_OQCSBFTT_DIRECTORY);
                StringBuilder sb = new StringBuilder();
                sb.append(secefsOqcsbfttDirectory);
                sb.append("/");
                sb.append(406);
                this.out = new FileOutputStream(sb.toString());
            } catch (IOException e) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("IOException : ");
                sb2.append(e.getMessage());
                LtUtil.log_d(this.CLASS_NAME, "onResume", sb2.toString());
            }
        }
    }

    private void initDisplay() {
        String str = "";
        String[] mSpecVal = Spec.getString(Spec.BLACK_LCD_BRIGHTNESS).split(",");
        int black_color = Color.rgb(Integer.parseInt(mSpecVal[0]), Integer.parseInt(mSpecVal[1]), Integer.parseInt(mSpecVal[2]));
        this.mRelativeLayoutBackground = (RelativeLayout) findViewById(C0268R.C0269id.background);
        this.mRelativeLayoutBackground.setBackgroundColor(black_color);
        this.mButtonExit = (Button) findViewById(C0268R.C0269id.button_exit);
        this.mButtonExit.setOnClickListener(this);
        this.mLinearLayoutHomeHolder = (LinearLayout) findViewById(C0268R.C0269id.home_holder);
        this.hasBackKey = KeyCharacterMap.deviceHasKey(4);
        this.isSupportForceTouch = Feature.getBoolean(Feature.SUPPORT_PRESSURE_TOUCH);
        if (!this.hasBackKey && this.isSupportForceTouch) {
            LtUtil.setRemoveSystemUI(getWindow(), true);
            this.mLinearLayoutHomeHolder.setVisibility(0);
            this.mButtonExit.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (isOqcsbftt && this.result.length() != 0) {
            try {
                this.result = this.result.substring(0, this.result.length() - 1);
                this.out.write(this.result.getBytes());
                this.out.close();
            } catch (IOException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("IOException : ");
                sb.append(e.getMessage());
                LtUtil.log_d(this.CLASS_NAME, "onPause", sb.toString());
            }
        }
        if (Kernel.isExistFileID(Kernel.KEYPAD_BACKLIGHT_FAC)) {
            Kernel.write(Kernel.KEYPAD_BACKLIGHT_FAC, "0");
        }
        setBrightnessMode();
        if (FactoryTest.isFactoryBinary()) {
            Kernel.write(Kernel.PRESSURE_TOUCH_STATUS, "0");
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if ((Feature.getBoolean(Feature.SUBKEY_FOUR_KEY) || Feature.getBoolean(Feature.SUBKEY_FIVE_KEY) || Feature.getBoolean(Feature.SUBKEY_SIX_KEY)) && Kernel.isExistFileID(Kernel.FIVE_BUTTON)) {
            Kernel.write(Kernel.FIVE_BUTTON, "0");
        }
        if (Feature.getBoolean(Feature.IS_DUMMYKEY_FACTORY_MODE) && Kernel.isExistFileID(Kernel.TSK_FACTORY_MODE)) {
            Kernel.write(Kernel.TSK_FACTORY_MODE, "0");
        }
        super.onDestroy();
    }

    public void onClick(View v) {
        if (v.getId() == C0268R.C0269id.button_exit) {
            LtUtil.log_d(this.CLASS_NAME, "onClick", "Click EXIT button");
            finish();
        }
    }

    public void setBrightness() {
        if (Feature.getBoolean(Feature.IS_AUTOBRIGHTNESS)) {
            unSetBrightnessMode();
            LtUtil.log_d(this.CLASS_NAME, "setBrightness", "Enable AutoBrightness");
        } else {
            LtUtil.log_d(this.CLASS_NAME, "setBrightness", "Do nothing");
        }
        super.setBrightness();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int keyCode2 = keyCode;
        KeyEvent keyEvent = event;
        String DeviceName = event.getDevice().getName();
        if (this.mIsDummyKeySameKeycode) {
            int scancode = event.getScanCode();
            LtUtil.log_i(this.CLASS_NAME, "onKeyDown", "Same Keycode for dummy key: Convert");
            if (keyCode2 != 4) {
                if (keyCode2 == 82) {
                    if (scancode == 251) {
                        keyCode2 = 1023;
                    } else if (scancode == SCANCODE_DUMMY_MENU2) {
                        keyCode2 = 1026;
                    }
                }
            } else if (scancode == 250) {
                keyCode2 = 1027;
            } else if (scancode == SCANCODE_DUMMY_BACK2) {
                keyCode2 = 1025;
            }
        }
        if (this.pressedKey != keyCode2 || !this.isPressed) {
            this.isPressed = false;
            switch (keyCode2) {
                case 3:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "HOME");
                    if ("sec_touchkey2".equalsIgnoreCase(DeviceName)) {
                        this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dark_turquoise));
                    } else {
                        this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.blue));
                    }
                    this.isPressed = true;
                    if (isOqcsbftt && !this.isKeyPressed) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(this.result);
                        sb.append("HMPS,");
                        this.result = sb.toString();
                        break;
                    }
                case 4:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "BACK");
                    if (!"sec_touchkey2".equalsIgnoreCase(DeviceName)) {
                        this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.magenta));
                        break;
                    } else {
                        this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.medium_slateblue));
                        break;
                    }
                case 5:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_CALL");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.brown));
                    break;
                case 6:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_ENDCALL");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.navy));
                    break;
                case 7:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_0");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.brown));
                    break;
                case 8:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_1");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dark_gray));
                    break;
                case 9:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_2");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dark_green));
                    break;
                case 10:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_3");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dark_red));
                    break;
                case 11:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_4");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.deep_pink));
                    break;
                case 12:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_5");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dodge_blue));
                    break;
                case 13:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_6");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.gold));
                    break;
                case 14:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_7");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.hop_pink));
                    break;
                case 15:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_8");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.lime));
                    break;
                case 16:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_9");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.navy));
                    break;
                case 17:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_STAR");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.olive));
                    break;
                case 18:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_POUND");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.royal_blue));
                    break;
                case 19:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DPAD_UP");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.sky));
                    break;
                case 20:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DPAD_DOWN");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.pink));
                    break;
                case 21:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DPAD_LEFT");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.working));
                    break;
                case 22:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DPAD_RIGHT");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.yellow));
                    break;
                case 23:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DPAD_CENTER");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.gold));
                    break;
                case 24:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "VOLUME_UP");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.white));
                    if (isOqcsbftt && !this.isKeyPressed) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(this.result);
                        sb2.append("VUPS,");
                        this.result = sb2.toString();
                        break;
                    }
                case Constants.CMD_TEST_CANCEL /*25*/:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "VOLUME_DOWN");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.green));
                    if (isOqcsbftt && !this.isKeyPressed) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(this.result);
                        sb3.append("VDPS,");
                        this.result = sb3.toString();
                        break;
                    }
                case 26:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "POWER");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.red));
                    if (isOqcsbftt && !this.isKeyPressed) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(this.result);
                        sb4.append("PWPS,");
                        this.result = sb4.toString();
                        break;
                    }
                case Constants.CMD_TEST_SET_CONFIG /*27*/:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "CAMERA");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.cyan));
                    break;
                case Constants.CMD_TEST_DOWNLOAD_FW /*28*/:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_CLEAR");
                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.silver));
                    break;
                default:
                    switch (keyCode2) {
                        case 66:
                            LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_ENTER");
                            this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.cyan));
                            break;
                        case 67:
                            LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DEL");
                            this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.olive));
                            break;
                        default:
                            switch (keyCode2) {
                                case 1025:
                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DUMMY_BACK");
                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.pink));
                                    break;
                                case 1026:
                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DUMMY_HOME1");
                                    if (!this.IS_FOUR_KEY) {
                                        this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.orange));
                                        break;
                                    }
                                    break;
                                case 1027:
                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DUMMY_HOME2");
                                    if (!this.IS_FOUR_KEY) {
                                        this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dodge_blue));
                                        break;
                                    }
                                    break;
                                default:
                                    switch (keyCode2) {
                                        case 1072:
                                            LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_MESSAGE");
                                            this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dodge_blue));
                                            break;
                                        case 1073:
                                            LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_APPSELECT");
                                            this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.gold));
                                            break;
                                        default:
                                            switch (keyCode2) {
                                                case 82:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "MENU");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.yellow));
                                                    break;
                                                case 84:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_SEARCH");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.purple));
                                                    break;
                                                case 122:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_MOVE_HOME");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dark_red));
                                                    break;
                                                case 187:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "APP_SWITCH");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.orange));
                                                    break;
                                                case 207:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_CONTACTS");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.deep_pink));
                                                    break;
                                                case SCANCODE_BEAM /*273*/:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_BEAM");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.pink));
                                                    break;
                                                case 288:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_ACTIVE");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.gray));
                                                    break;
                                                case 1015:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_USER");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.dark_salmon));
                                                    break;
                                                case 1023:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_DUMMY_MENU");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.silver));
                                                    break;
                                                case 1036:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_3G");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.navy));
                                                    break;
                                                case 1047:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_NETWORK_SEL");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.sky));
                                                    break;
                                                case 1079:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_EMERGENCY");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.violet));
                                                    break;
                                                case 1082:
                                                    LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "KEYCODE_WINK");
                                                    this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.hop_pink));
                                                    if (isOqcsbftt && !this.isKeyPressed) {
                                                        StringBuilder sb5 = new StringBuilder();
                                                        sb5.append(this.result);
                                                        sb5.append("BBPS,");
                                                        this.result = sb5.toString();
                                                        break;
                                                    }
                                            }
                                            break;
                                    }
                            }
                    }
            }
            this.pressedKey = keyCode2;
            if (isOqcsbftt) {
                this.isKeyPressed = true;
                event.startTracking();
                if (keyCode2 == 4) {
                    return super.onKeyDown(keyCode2, keyEvent);
                }
                return true;
            }
        } else {
            LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "Same key is pressed, change background to black");
            this.mRelativeLayoutBackground.setBackgroundColor(getResources().getColor(C0268R.color.black));
            this.isPressed = false;
        }
        return super.onKeyDown(keyCode2, keyEvent);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 3) {
            LtUtil.log_d(this.CLASS_NAME, "onKeyUp", "HOME");
            if (isOqcsbftt) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.result);
                sb.append("HMR");
                this.result = sb.toString();
                if (this.isKeyPressedLong) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.result);
                    sb2.append("L,");
                    this.result = sb2.toString();
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(this.result);
                    sb3.append("S,");
                    this.result = sb3.toString();
                }
            }
        } else if (keyCode != 1082) {
            switch (keyCode) {
                case 24:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyUp", "VOLUME_UP");
                    if (isOqcsbftt) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(this.result);
                        sb4.append("VUR");
                        this.result = sb4.toString();
                        if (!this.isKeyPressedLong) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(this.result);
                            sb5.append("S,");
                            this.result = sb5.toString();
                            break;
                        } else {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(this.result);
                            sb6.append("L,");
                            this.result = sb6.toString();
                            break;
                        }
                    }
                    break;
                case Constants.CMD_TEST_CANCEL /*25*/:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyUp", "VOLUME_DOWN");
                    if (isOqcsbftt) {
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(this.result);
                        sb7.append("VDR");
                        this.result = sb7.toString();
                        if (!this.isKeyPressedLong) {
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append(this.result);
                            sb8.append("S,");
                            this.result = sb8.toString();
                            break;
                        } else {
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append(this.result);
                            sb9.append("L,");
                            this.result = sb9.toString();
                            break;
                        }
                    }
                    break;
                case 26:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyUp", "POWER");
                    if (isOqcsbftt) {
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(this.result);
                        sb10.append("PWR");
                        this.result = sb10.toString();
                        if (!this.isKeyPressedLong) {
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append(this.result);
                            sb11.append("S,");
                            this.result = sb11.toString();
                            break;
                        } else {
                            StringBuilder sb12 = new StringBuilder();
                            sb12.append(this.result);
                            sb12.append("L,");
                            this.result = sb12.toString();
                            break;
                        }
                    }
                    break;
            }
        } else {
            LtUtil.log_d(this.CLASS_NAME, "onKeyUp", "KEYCODE_WINK");
            if (isOqcsbftt) {
                StringBuilder sb13 = new StringBuilder();
                sb13.append(this.result);
                sb13.append("BBR");
                this.result = sb13.toString();
                if (this.isKeyPressedLong) {
                    StringBuilder sb14 = new StringBuilder();
                    sb14.append(this.result);
                    sb14.append("L,");
                    this.result = sb14.toString();
                } else {
                    StringBuilder sb15 = new StringBuilder();
                    sb15.append(this.result);
                    sb15.append("S,");
                    this.result = sb15.toString();
                }
            }
        }
        this.isKeyPressed = false;
        this.isKeyPressedLong = false;
        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        this.isKeyPressedLong = true;
        if (keyCode == 3) {
            LtUtil.log_d(this.CLASS_NAME, "onKeyLongPress", "HOME");
            if (isOqcsbftt) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.result.substring(0, this.result.length() - 2));
                sb.append(KEY_LONG_PRESS);
                sb.append(",");
                this.result = sb.toString();
            }
        } else if (keyCode != 1082) {
            switch (keyCode) {
                case 24:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyLongPress", "VOLUME_UP");
                    if (isOqcsbftt) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(this.result.substring(0, this.result.length() - 2));
                        sb2.append(KEY_LONG_PRESS);
                        sb2.append(",");
                        this.result = sb2.toString();
                        break;
                    }
                    break;
                case Constants.CMD_TEST_CANCEL /*25*/:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyLongPress", "VOLUME_DOWN");
                    if (isOqcsbftt) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(this.result.substring(0, this.result.length() - 2));
                        sb3.append(KEY_LONG_PRESS);
                        sb3.append(",");
                        this.result = sb3.toString();
                        break;
                    }
                    break;
                case 26:
                    LtUtil.log_d(this.CLASS_NAME, "onKeyLongPress", "POWER");
                    if (isOqcsbftt) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(this.result.substring(0, this.result.length() - 2));
                        sb4.append(KEY_LONG_PRESS);
                        sb4.append(",");
                        this.result = sb4.toString();
                        break;
                    }
                    break;
            }
        } else {
            LtUtil.log_d(this.CLASS_NAME, "onKeyLongPress", "KEYCODE_WINK");
            if (isOqcsbftt) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(this.result.substring(0, this.result.length() - 2));
                sb5.append(KEY_LONG_PRESS);
                sb5.append(",");
                this.result = sb5.toString();
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }
}
