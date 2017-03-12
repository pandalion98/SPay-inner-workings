package com.samsung.android.motion;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.security.keystore.KeyProperties;

public class MREvent implements Parcelable {
    public static final int BLOW = 66;
    public static final int BOUNCE = 46;
    public static final int BOUNCE_MOTION_LEFT_LONG = 118;
    public static final int BOUNCE_MOTION_LEFT_SHORT = 117;
    public static final int BOUNCE_MOTION_NONE = 114;
    public static final int BOUNCE_MOTION_RIGHT_LONG = 116;
    public static final int BOUNCE_MOTION_RIGHT_SHORT = 115;
    public static final int BOUNCE_MOTION_UNHAND = 119;
    public static final int BT_SHARING_RECEIVE_NOT_READY = 41;
    public static final int BT_SHARING_RECEIVE_READY = 40;
    public static final int BT_SHARING_SEND_PAUSE = 43;
    public static final int BT_SHARING_SEND_START = 42;
    public static final int BT_SHARING_SEND_STOP = 44;
    public static final int CALLPOSE_L = 76;
    public static final int CALLPOSE_NONE = 100;
    public static final int CALLPOSE_R = 77;
    public static final Creator<MREvent> CREATOR = new Creator<MREvent>() {
        public MREvent createFromParcel(Parcel in) {
            return new MREvent(in);
        }

        public MREvent[] newArray(int size) {
            return new MREvent[size];
        }
    };
    public static final int DIRECT_CALL = 68;
    public static final int DIRECT_CALL_LEFT = 101;
    public static final int DIRECT_CALL_RIGHT = 102;
    public static final int FLAT = 71;
    public static final int FLIP_BOTTOM_TO_TOP = 86;
    public static final int FLIP_TOP_TO_BOTTOM = 10;
    public static final int LCD_UP_STEADY = 99;
    public static final int LOCK_EXECUTE_APP = 69;
    public static final int LOCK_EXECUTE_CAMERA_L = 73;
    public static final int LOCK_EXECUTE_CAMERA_R = 74;
    public static final int MAX = 120;
    public static final int NONE = 0;
    public static final int ONE_TAPPING_GYRO = 62;
    public static final int ONE_TIPPING_GYRO = 64;
    public static final int PANNING_GYRO = 61;
    public static final int REACTIVE_ALERT = 67;
    public static final int ROTATE_0 = 28;
    public static final int ROTATE_180 = 30;
    public static final int ROTATE_270 = 31;
    public static final int ROTATE_90 = 29;
    public static final int ROTATE_HORIZONTAL = 45;
    public static final int ROTATE_START = 32;
    public static final int ROTATE_STOP = 33;
    public static final int ROTATION_AXIS_X = 94;
    public static final int ROTATION_AXIS_Y = 95;
    public static final int ROTATION_AXIS_Z = 96;
    public static final int SHAKE = 34;
    public static final int SHAKE_START = 35;
    public static final int SHAKE_STOP = 36;
    public static final int SHORT_SHAKE = 37;
    public static final int SHORT_SHAKE_START = 38;
    public static final int SHORT_SHAKE_STOP = 39;
    public static final int SMART_ALERT_SETTING = 98;
    public static final int SMART_RELAY = 113;
    public static final int SMART_SCROLL_CAMERA_OFF = 111;
    public static final int SMART_SCROLL_CAMERA_ON = 112;
    public static final int SMART_SCROLL_TILT_DOWN_START = 104;
    public static final int SMART_SCROLL_TILT_DOWN_START_LAND = 108;
    public static final int SMART_SCROLL_TILT_FACE_IN_STOP = 105;
    public static final int SMART_SCROLL_TILT_FACE_IN_STOP_LAND = 109;
    public static final int SMART_SCROLL_TILT_FACE_OUT_STOP = 106;
    public static final int SMART_SCROLL_TILT_FACE_OUT_STOP_LAND = 110;
    public static final int SMART_SCROLL_TILT_UP_START = 103;
    public static final int SMART_SCROLL_TILT_UP_START_LAND = 107;
    public static final int SNAP1_X_NEGATIVE = 48;
    public static final int SNAP1_X_POSITIVE = 47;
    public static final int SNAP1_Y_NEGATIVE = 50;
    public static final int SNAP1_Y_POSITIVE = 49;
    public static final int SNAP1_Z_NEGATIVE = 52;
    public static final int SNAP1_Z_POSITIVE = 51;
    public static final int SNAP2_X_NEGATIVE = 54;
    public static final int SNAP2_X_POSITIVE = 53;
    public static final int SNAP2_Y_NEGATIVE = 56;
    public static final int SNAP2_Y_POSITIVE = 55;
    public static final int SNAP2_Z_NEGATIVE = 58;
    public static final int SNAP2_Z_POSITIVE = 57;
    public static final int SNAP_LEFT = 59;
    public static final int SNAP_RIGHT = 60;
    public static final int SNAP_X_NEGATIVE = 3;
    public static final int SNAP_X_POSITIVE = 2;
    public static final int SNAP_Y_NEGATIVE = 5;
    public static final int SNAP_Y_POSITIVE = 4;
    public static final int SNAP_Z_NEGATIVE = 7;
    public static final int SNAP_Z_POSITIVE = 6;
    public static final int SPEAKER_PHONE_OFF = 9;
    public static final int SPEAKER_PHONE_ON = 8;
    public static final int TILT = 72;
    public static final int TILT_BACK = 23;
    public static final int TILT_DOWN_LEVEL_1 = 81;
    public static final int TILT_DOWN_LEVEL_1_LAND = 90;
    public static final int TILT_DOWN_LEVEL_2 = 82;
    public static final int TILT_DOWN_LEVEL_2_LAND = 91;
    public static final int TILT_DOWN_LEVEL_3 = 83;
    public static final int TILT_DOWN_LEVEL_3_LAND = 92;
    public static final int TILT_FRONT = 22;
    public static final int TILT_FRONT_BACK_END = 24;
    public static final int TILT_LANDSCAPE_LEFT_LEVEL_1 = 17;
    public static final int TILT_LANDSCAPE_LEFT_LEVEL_2 = 18;
    public static final int TILT_LANDSCAPE_LEFT_RIGHT_STOP = 21;
    public static final int TILT_LANDSCAPE_RIGHT_LEVEL_1 = 19;
    public static final int TILT_LANDSCAPE_RIGHT_LEVEL_2 = 20;
    public static final int TILT_LEFT = 25;
    public static final int TILT_LEFT_RIGHT_END = 27;
    public static final int TILT_LEVEL_FLAT = 85;
    public static final int TILT_LEVEL_ZERO = 84;
    public static final int TILT_LEVEL_ZERO_LAND = 93;
    public static final int TILT_PORTRAIT_BACK = 12;
    public static final int TILT_PORTRAIT_FRONT = 11;
    public static final int TILT_PORTRAIT_FRONT_BACK_STOP = 13;
    public static final int TILT_PORTRAIT_LEFT = 14;
    public static final int TILT_PORTRAIT_LEFT_RIGHT_STOP = 16;
    public static final int TILT_PORTRAIT_RIGHT = 15;
    public static final int TILT_RIGHT = 26;
    public static final int TILT_TO_UNLOCK = 75;
    public static final int TILT_TO_UNLOCK_LAND = 97;
    public static final int TILT_UP_LEVEL_1 = 78;
    public static final int TILT_UP_LEVEL_1_LAND = 87;
    public static final int TILT_UP_LEVEL_2 = 79;
    public static final int TILT_UP_LEVEL_2_LAND = 88;
    public static final int TILT_UP_LEVEL_3 = 80;
    public static final int TILT_UP_LEVEL_3_LAND = 89;
    public static final int TWO_TAPPING = 1;
    public static final int TWO_TAPPING_GYRO = 63;
    public static final int TWO_TIPPING_GYRO = 65;
    public static final int VOLUMEDOWN = 70;
    private int motion;
    private int panningDx;
    private int panningDxImage;
    private int panningDy;
    private int panningDyImage;
    private int panningDz;
    private int panningDzImage;
    private int tilt;
    private int walkingStatus;

    public MREvent() {
        this.motion = 0;
        this.panningDx = 0;
        this.panningDy = 0;
        this.panningDz = 0;
        this.panningDxImage = 0;
        this.panningDyImage = 0;
        this.panningDzImage = 0;
        this.walkingStatus = 0;
    }

    public MREvent(Parcel src) {
        readFromParcel(src);
    }

    public int getMotion() {
        return this.motion;
    }

    public void setMotion(int m) {
        this.motion = 0;
        if (m >= 0 && m <= 120) {
            this.motion = m;
        }
    }

    public void setPanningDx(int dx) {
        this.panningDx = dx;
    }

    public void setPanningDy(int dy) {
        this.panningDy = dy;
    }

    public void setPanningDz(int dz) {
        this.panningDz = dz;
    }

    public void setTilt(int t) {
        this.tilt = t;
    }

    public void setWalkingStatus(int ws) {
        this.walkingStatus = ws;
    }

    public int getPanningDx() {
        return this.panningDx;
    }

    public int getPanningDy() {
        return this.panningDy;
    }

    public int getPanningDz() {
        return this.panningDz;
    }

    public int getSmartMotion() {
        return this.panningDz;
    }

    public int getTilt() {
        return this.tilt;
    }

    public int getWalkingStatus() {
        return this.walkingStatus;
    }

    public void setPanningDxImage(int dx) {
        this.panningDxImage = dx;
    }

    public void setPanningDyImage(int dy) {
        this.panningDyImage = dy;
    }

    public void setPanningDzImage(int dz) {
        this.panningDzImage = dz;
    }

    public int getPanningDxImage() {
        return this.panningDxImage;
    }

    public int getPanningDyImage() {
        return this.panningDyImage;
    }

    public int getPanningDzImage() {
        return this.panningDzImage;
    }

    public String toString() {
        String string = Integer.toString(this.motion) + "=";
        switch (this.motion) {
            case 0:
                string = string + KeyProperties.DIGEST_NONE;
                break;
            case 1:
                string = string + "TWO_TAPPING";
                break;
            case 2:
                string = string + "SNAP_X_POSITIVE";
                break;
            case 3:
                string = string + "SNAP_X_NEGATIVE";
                break;
            case 4:
                string = string + "SNAP_Y_POSITIVE";
                break;
            case 5:
                string = string + "SNAP_Y_NEGATIVE";
                break;
            case 6:
                string = string + "SNAP_Z_POSITIVE";
                break;
            case 7:
                string = string + "SNAP_Z_NEGATIVE";
                break;
            case 8:
                string = string + "SPEAKER_PHONE_ON";
                break;
            case 9:
                string = string + "SPEAKER_PHONE_OFF";
                break;
            case 10:
                string = string + "FLIP_TOP_TO_BOTTOM";
                break;
            case 11:
                string = string + "TILT_PORTRAIT_FRONT";
                break;
            case 12:
                string = string + "TILT_PORTRAIT_BACK";
                break;
            case 13:
                string = string + "TILT_PORTRAIT_FRONT_BACK_STOP";
                break;
            case 14:
                string = string + "TILT_PORTRAIT_LEFT";
                break;
            case 15:
                string = string + "TILT_PORTRAIT_RIGHT";
                break;
            case 16:
                string = string + "TILT_PORTRAIT_LEFT_RIGHT_STOP";
                break;
            case 17:
                string = string + "TILT_LANDSCAPE_LEFT_LEVEL_1";
                break;
            case 18:
                string = string + "TILT_LANDSCAPE_LEFT_LEVEL_2";
                break;
            case 19:
                string = string + "TILT_LANDSCAPE_RIGHT_LEVEL_1";
                break;
            case 20:
                string = string + "TILT_LANDSCAPE_RIGHT_LEVEL_2";
                break;
            case 21:
                string = string + "TILT_LANDSCAPE_LEFT_RIGHT_STOP";
                break;
            case 22:
                string = string + "TILT_FRONT";
                break;
            case 23:
                string = string + "TILT_BACK";
                break;
            case 24:
                string = string + "TILT_FRONT_BACK_END";
                break;
            case 25:
                string = string + "TILT_LEFT";
                break;
            case 26:
                string = string + "TILT_RIGHT";
                break;
            case 27:
                string = string + "TILT_LEFT_RIGHT_END";
                break;
            case 28:
                string = string + "ROTATE_0";
                break;
            case 29:
                string = string + "ROTATE_90";
                break;
            case 30:
                string = string + "ROTATE_180";
                break;
            case 31:
                string = string + "ROTATE_270";
                break;
            case 32:
                string = string + "ROTATE_START";
                break;
            case 33:
                string = string + "ROTATE_STOP";
                break;
            case 34:
                string = string + "SHAKE";
                break;
            case 35:
                string = string + "SHAKE_START";
                break;
            case 36:
                string = string + "SHAKE_STOP";
                break;
            case 37:
                string = string + "SHORT_SHAKE";
                break;
            case 38:
                string = string + "SHORT_SHAKE_START";
                break;
            case 39:
                string = string + "SHORT_SHAKE_STOP";
                break;
            case 40:
                string = string + "BT_SHARING_RECEIVE_READY";
                break;
            case 41:
                string = string + "BT_SHARING_RECEIVE_NOT_READY";
                break;
            case 42:
                string = string + "BT_SHARING_SEND_START";
                break;
            case 43:
                string = string + "BT_SHARING_SEND_PAUSE";
                break;
            case 44:
                string = string + "BT_SHARING_SEND_STOP";
                break;
            case 45:
                string = string + "ROTATE_HORIZONTAL";
                break;
            case 46:
                string = string + "BOUNCE";
                break;
            case 47:
                string = string + "SNAP1_X_POSITIVE";
                break;
            case 48:
                string = string + "SNAP1_X_NEGATIVE";
                break;
            case 49:
                string = string + "SNAP1_Y_POSITIVE";
                break;
            case 50:
                string = string + "SNAP1_Y_NEGATIVE";
                break;
            case 51:
                string = string + "SNAP1_Z_POSITIVE";
                break;
            case 52:
                string = string + "SNAP1_Z_NEGATIVE";
                break;
            case 53:
                string = string + "SNAP2_X_POSITIVE";
                break;
            case 54:
                string = string + "SNAP2_X_NEGATIVE";
                break;
            case 55:
                string = string + "SNAP2_Y_POSITIVE";
                break;
            case 56:
                string = string + "SNAP2_Y_NEGATIVE";
                break;
            case 57:
                string = string + "SNAP2_Z_POSITIVE";
                break;
            case 58:
                string = string + "SNAP2_Z_NEGATIVE";
                break;
            case 59:
                string = string + "SNAP_LEFT";
                break;
            case 60:
                string = string + "SNAP_RIGHT";
                break;
            case 61:
                string = string + "PANNING_GYRO";
                break;
            case 62:
                string = string + "ONE_TAPPING_GYRO";
                break;
            case 63:
                string = string + "TWO_TAPPING_GYRO";
                break;
            case 64:
                string = string + "ONE_TIPPING_GYRO";
                break;
            case 65:
                string = string + "TWO_TIPPING_GYRO";
                break;
            case 66:
                string = string + "BLOW";
                break;
            case 67:
                string = string + "REACTIVE_ALERT";
                break;
            case 68:
                string = string + "DIRECT_CALL";
                break;
            case 69:
                string = string + "LOCK_EXECUTE_APP";
                break;
            case 70:
                string = string + "VOLUMEDOWN";
                break;
            case 71:
                string = string + "FLAT";
                break;
            case 72:
                string = string + "TILT";
                break;
            case 73:
                string = string + "LOCK_EXECUTE_CAMERA_L";
                break;
            case 74:
                string = string + "LOCK_EXECUTE_CAMERA_R";
                break;
            case 75:
                string = string + "TILT_TO_UNLOCK";
                break;
            case 76:
                string = string + "CALLPOSE_L";
                break;
            case 77:
                string = string + "CALLPOSE_R";
                break;
            case 78:
                string = string + "TILT_UP_LEVEL_1";
                break;
            case 79:
                string = string + "TILT_UP_LEVEL_2";
                break;
            case 80:
                string = string + "TILT_UP_LEVEL_3";
                break;
            case 81:
                string = string + "TILT_DOWN_LEVEL_1";
                break;
            case 82:
                string = string + "TILT_DOWN_LEVEL_2";
                break;
            case 83:
                string = string + "TILT_DOWN_LEVEL_3";
                break;
            case 84:
                string = string + "TILT_LEVEL_ZERO";
                break;
            case 85:
                string = string + "TILT_LEVEL_FLAT";
                break;
            case 86:
                string = string + "FLIP_BOTTOM_TO_TOP";
                break;
            case 87:
                string = string + "TILT_UP_LEVEL_1_LAND";
                break;
            case 88:
                string = string + "TILT_UP_LEVEL_2_LAND";
                break;
            case 89:
                string = string + "TILT_UP_LEVEL_3_LAND";
                break;
            case 90:
                string = string + "TILT_DOWN_LEVEL_1_LAND";
                break;
            case 91:
                string = string + "TILT_DOWN_LEVEL_2_LAND";
                break;
            case 92:
                string = string + "TILT_DOWN_LEVEL_3_LAND";
                break;
            case 93:
                string = string + "TILT_LEVEL_ZERO_LAND";
                break;
            case 97:
                string = string + "TILT_TO_UNLOCK_LAND";
                break;
            case 98:
                string = string + "SMART_ALERT_SETTING";
                break;
            case 99:
                string = string + "LCD_UP_STEADY";
                break;
            case 100:
                string = string + "CALLPOSE_NONE";
                break;
            case 101:
                string = string + "DIRECT_CALL_LEFT";
                break;
            case 102:
                string = string + "DIRECT_CALL_RIGHT";
                break;
            case 103:
                string = string + "SMART_SCROLL_TILT_UP_START";
                break;
            case 104:
                string = string + "SMART_SCROLL_TILT_DOWN_START";
                break;
            case 105:
                string = string + "SMART_SCROLL_TILT_FACE_IN_STOP";
                break;
            case 106:
                string = string + "SMART_SCROLL_TILT_FACE_OUT_STOP";
                break;
            case 107:
                string = string + "SMART_SCROLL_TILT_UP_START_LAND";
                break;
            case 108:
                string = string + "SMART_SCROLL_TILT_DOWN_START_LAND";
                break;
            case 109:
                string = string + "SMART_SCROLL_TILT_FACE_IN_STOP_LAND";
                break;
            case 110:
                string = string + "SMART_SCROLL_TILT_FACE_OUT_STOP_LAND";
                break;
            case 111:
                string = string + "SMART_SCROLL_CAMERA_OFF";
                break;
            case 112:
                string = string + "SMART_SCROLL_CAMERA_ON";
                break;
            case 113:
                string = string + "SMART_RELAY";
                break;
            case 115:
                string = string + "BOUNCE_MOTION_RIGHT_SHORT";
                break;
            case 116:
                string = string + "BOUNCE_MOTION_RIGHT_LONG";
                break;
            case 117:
                string = string + "BOUNCE_MOTION_LEFT_SHORT";
                break;
            case 118:
                string = string + "BOUNCE_MOTION_LEFT_LONG";
                break;
            case 119:
                string = string + "BOUNCE_MOTION_UNHAND";
                break;
            case 120:
                string = string + "MAX";
                break;
        }
        string = string.trim();
        if (this.motion == 61) {
            string = string + " (" + this.panningDx + ", " + this.panningDy + ", " + this.panningDz + ")";
        }
        if (this.motion == 72) {
            return string + " (" + this.tilt + "), walking status (" + this.walkingStatus + ")";
        }
        return string;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.motion);
        dest.writeInt(this.panningDx);
        dest.writeInt(this.panningDy);
        dest.writeInt(this.panningDz);
        dest.writeInt(this.panningDxImage);
        dest.writeInt(this.panningDyImage);
        dest.writeInt(this.panningDzImage);
        dest.writeInt(this.tilt);
        dest.writeInt(this.walkingStatus);
    }

    public void readFromParcel(Parcel src) {
        this.motion = src.readInt();
        this.panningDx = src.readInt();
        this.panningDy = src.readInt();
        this.panningDz = src.readInt();
        this.panningDxImage = src.readInt();
        this.panningDyImage = src.readInt();
        this.panningDzImage = src.readInt();
        this.tilt = src.readInt();
        this.walkingStatus = src.readInt();
    }
}
