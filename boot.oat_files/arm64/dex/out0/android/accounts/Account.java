package android.accounts;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class Account implements Parcelable {
    public static final Creator<Account> CREATOR = new Creator<Account>() {
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
    public final String name;
    public final String type;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account other = (Account) o;
        if (this.name.equals(other.name) && this.type.equals(other.type)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((this.name.hashCode() + 527) * 31) + this.type.hashCode();
    }

    public Account(String name, String type) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("the name must not be empty: " + name);
        } else if (TextUtils.isEmpty(type)) {
            throw new IllegalArgumentException("the type must not be empty: " + type);
        } else {
            this.name = name;
            this.type = type;
        }
    }

    public Account(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
    }

    public String toString() {
        return "Account {name=" + this.name + ", type=" + this.type + "}";
    }
}
