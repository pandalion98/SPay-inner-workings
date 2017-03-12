package com.samsung.android.smartface;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class FaceInfo implements Parcelable {
    public static final int CHECK_FACE_EXISTENCE = 1;
    public static final int CHECK_FACE_EXISTENCE_WITH_ORIENTATION = 64;
    public static final int CHECK_FACE_ROTATION = 4;
    public static final Creator<FaceInfo> CREATOR = new Creator<FaceInfo>() {
        public FaceInfo createFromParcel(Parcel in) {
            return new FaceInfo(in);
        }

        public FaceInfo[] newArray(int size) {
            return new FaceInfo[size];
        }
    };
    public static final int FIND_FACES = 2;
    public static final int FIND_FACE_AND_PERSON_INFO = 8;
    public static final int FIND_FACE_COMPONENT = 32;
    public static final int FIND_FACE_POSE_INFO = 16;
    public static final int NEED_TO_PAUSE = 1;
    public static final int NEED_TO_PLAY = 0;
    public static final int NEED_TO_SLEEP = 0;
    public static final int NEED_TO_STAY = 1;
    public boolean bFaceDetected;
    public boolean bLowLightBackLighting;
    public int faceDistance;
    public int guideDir;
    public int horizontalMovement;
    public int needToPause;
    public int needToRotate;
    public int needToStay;
    public int numberOfPerson;
    public Person[] person = null;
    public int processStatus;
    public int responseType;
    public int verticalMovement;

    public static class Face {
        public FaceExpression expression = null;
        public int id;
        public Point leftEye = null;
        public Point mouth = null;
        public Point nose = null;
        public FacePoseInfo pose = null;
        public Rect rect = null;
        public Point rightEye = null;
        public int score;
    }

    public static class FaceExpression {
        public static final int FACIAL_EXPRESSION_ANGER = 8;
        public static final int FACIAL_EXPRESSION_DISGUST = 4;
        public static final int FACIAL_EXPRESSION_FEAR = 32;
        public static final int FACIAL_EXPRESSION_JOY = 2;
        public static final int FACIAL_EXPRESSION_NONE = 1;
        public static final int FACIAL_EXPRESSION_SADNESS = 64;
        public static final int FACIAL_EXPRESSION_SURPRISE = 16;
        public int expression;
    }

    public static class FacePoseInfo {
        public int pitch;
        public int roll;
        public int yaw;
    }

    public static class Person {
        public Face face = null;
        public PersonInfo personInfo = null;
    }

    public static class PersonInfo {
        public String address = null;
        public String addressEMail = null;
        public String name = null;
        public String phoneNumber = null;
    }

    public FaceInfo(Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        int i;
        int i2 = 1;
        out.writeInt(this.responseType);
        out.writeInt(this.numberOfPerson);
        out.writeInt(this.horizontalMovement);
        out.writeInt(this.verticalMovement);
        out.writeInt(this.processStatus);
        out.writeInt(this.needToRotate);
        out.writeInt(this.needToPause);
        out.writeInt(this.needToStay);
        out.writeInt(this.guideDir);
        if (this.bFaceDetected) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeByte((byte) i);
        if (!this.bLowLightBackLighting) {
            i2 = 0;
        }
        out.writeByte((byte) i2);
        out.writeInt(this.faceDistance);
        for (int i3 = 0; i3 < this.numberOfPerson; i3++) {
            out.writeInt(this.person[i3].face.rect.left);
            out.writeInt(this.person[i3].face.rect.top);
            out.writeInt(this.person[i3].face.rect.bottom);
            out.writeInt(this.person[i3].face.rect.right);
            out.writeInt(this.person[i3].face.score);
            out.writeInt(this.person[i3].face.id);
            out.writeInt(this.person[i3].face.leftEye.x);
            out.writeInt(this.person[i3].face.leftEye.y);
            out.writeInt(this.person[i3].face.rightEye.x);
            out.writeInt(this.person[i3].face.rightEye.y);
            out.writeInt(this.person[i3].face.mouth.x);
            out.writeInt(this.person[i3].face.mouth.y);
            out.writeInt(this.person[i3].face.nose.x);
            out.writeInt(this.person[i3].face.nose.y);
            out.writeInt(this.person[i3].face.pose.pitch);
            out.writeInt(this.person[i3].face.pose.roll);
            out.writeInt(this.person[i3].face.pose.yaw);
            out.writeInt(this.person[i3].face.expression.expression);
            out.writeString(this.person[i3].personInfo.addressEMail);
            out.writeString(this.person[i3].personInfo.phoneNumber);
            out.writeString(this.person[i3].personInfo.address);
            out.writeString(this.person[i3].personInfo.name);
        }
    }

    public void readFromParcel(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.responseType = in.readInt();
        this.numberOfPerson = in.readInt();
        this.horizontalMovement = in.readInt();
        this.verticalMovement = in.readInt();
        this.processStatus = in.readInt();
        this.needToRotate = in.readInt();
        this.needToPause = in.readInt();
        this.needToStay = in.readInt();
        this.guideDir = in.readInt();
        if (in.readByte() == (byte) 1) {
            z = true;
        } else {
            z = false;
        }
        this.bFaceDetected = z;
        if (in.readByte() != (byte) 1) {
            z2 = false;
        }
        this.bLowLightBackLighting = z2;
        this.faceDistance = in.readInt();
        this.person = new Person[this.numberOfPerson];
        for (int i = 0; i < this.numberOfPerson; i++) {
            this.person[i].face = new Face();
            this.person[i].face.rect = new Rect();
            this.person[i].face.rect.left = in.readInt();
            this.person[i].face.rect.top = in.readInt();
            this.person[i].face.rect.bottom = in.readInt();
            this.person[i].face.rect.right = in.readInt();
            this.person[i].face.score = in.readInt();
            this.person[i].face.id = in.readInt();
            this.person[i].face.leftEye = new Point();
            this.person[i].face.leftEye.x = in.readInt();
            this.person[i].face.leftEye.y = in.readInt();
            this.person[i].face.rightEye = new Point();
            this.person[i].face.rightEye.x = in.readInt();
            this.person[i].face.rightEye.y = in.readInt();
            this.person[i].face.mouth = new Point();
            this.person[i].face.mouth.x = in.readInt();
            this.person[i].face.mouth.y = in.readInt();
            this.person[i].face.nose = new Point();
            this.person[i].face.nose.x = in.readInt();
            this.person[i].face.nose.y = in.readInt();
            this.person[i].face.pose = new FacePoseInfo();
            this.person[i].face.pose.pitch = in.readInt();
            this.person[i].face.pose.roll = in.readInt();
            this.person[i].face.pose.yaw = in.readInt();
            this.person[i].face.expression = new FaceExpression();
            this.person[i].face.expression.expression = in.readInt();
            this.person[i].personInfo = new PersonInfo();
            this.person[i].personInfo.addressEMail = in.readString();
            this.person[i].personInfo.phoneNumber = in.readString();
            this.person[i].personInfo.address = in.readString();
            this.person[i].personInfo.name = in.readString();
        }
    }
}
