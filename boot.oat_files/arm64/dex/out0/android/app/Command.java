package android.app;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Command implements Parcelable {
    public static final Creator<Command> CREATOR = new Creator<Command>() {
        public Command createFromParcel(Parcel in) {
            return new Command(in);
        }

        public Command[] newArray(int size) {
            return new Command[size];
        }
    };
    public PendingIntent contentIntent;
    public Intent intent;
    public String packageToLaunch;
    public int personaId;
    public String type;
    public String uri;

    public Command(Parcel in) {
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.type);
        out.writeString(this.packageToLaunch);
        out.writeInt(this.personaId);
        out.writeParcelable(this.intent, 0);
        out.writeParcelable(this.contentIntent, 0);
        out.writeString(this.uri);
    }

    private void readFromParcel(Parcel in) {
        this.type = in.readString();
        this.packageToLaunch = in.readString();
        this.personaId = in.readInt();
        this.intent = (Intent) in.readParcelable(Intent.class.getClassLoader());
        this.contentIntent = (PendingIntent) in.readParcelable(PendingIntent.class.getClassLoader());
        this.uri = in.readString();
    }

    public int describeContents() {
        return 0;
    }
}
