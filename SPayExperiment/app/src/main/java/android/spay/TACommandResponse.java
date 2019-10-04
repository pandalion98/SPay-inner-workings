package android.spay;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TACommandResponse implements Parcelable {
    public static final Creator<TACommandResponse> CREATOR = new Creator<TACommandResponse>() {
        public TACommandResponse createFromParcel(Parcel in) {
            return new TACommandResponse(in);
        }

        public TACommandResponse[] newArray(int size) {
            return new TACommandResponse[size];
        }
    };
    private static final String TAG = "TACommandResponse";
    public String mErrorMsg;
    public byte[] mResponse;
    public int mResponseCode;

    public TACommandResponse() {
        this.mResponseCode = -1;
        this.mErrorMsg = null;
        this.mResponse = null;
    }

    public TACommandResponse(int responseId, String errorMsg, byte[] response) {
        this.mResponseCode = -1;
        this.mErrorMsg = null;
        this.mResponse = null;
        this.mResponseCode = responseId;
        this.mErrorMsg = errorMsg;
        this.mResponse = response;
    }

    private TACommandResponse(Parcel in) {
        this.mResponseCode = -1;
        this.mErrorMsg = null;
        this.mResponse = null;
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(this.mResponseCode);
        out.writeString(this.mErrorMsg);
        out.writeInt(this.mResponse.length);
        out.writeByteArray(this.mResponse);
    }

    public void readFromParcel(Parcel in) {
        this.mResponseCode = in.readInt();
        this.mErrorMsg = in.readString();
        this.mResponse = new byte[in.readInt()];
        in.readByteArray(this.mResponse);
    }

    public int describeContents() {
        return 0;
    }

    public void dump() {
        Exception e;
        Throwable th;
        Log.d(TAG, "Length = " + this.mResponse.length);
        StringBuilder hex = new StringBuilder((this.mResponse.length * 3) + 100);
        int i = 0;
        while (i < this.mResponse.length) {
            if (i > 0 && this.mResponse[i] != (byte) 0 && this.mResponse[i - 1] == (byte) 0) {
                hex.append("\n");
            }
            hex.append(String.format("%02X ", new Object[]{Byte.valueOf(this.mResponse[i])}));
            i++;
        }
        Log.d(TAG, hex.toString());
        FileWriter outFile = null;
        BufferedWriter bw = null;
        try {
            BufferedWriter bw2;
            FileWriter outFile2 = new FileWriter("/mnt/sdcard/respbuf.txt", false);
            try {
                bw2 = new BufferedWriter(outFile2);
            } catch (Exception e2) {
                e = e2;
                outFile = outFile2;
                try {
                    e.printStackTrace();
                    if (bw != null) {
                        try {
                            bw.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            return;
                        }
                    }
                    if (outFile == null) {
                        outFile.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (bw != null) {
                        try {
                            bw.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                            throw th;
                        } catch (Exception e42) {
                            e42.printStackTrace();
                            throw th;
                        }
                    }
                    if (outFile != null) {
                        outFile.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                outFile = outFile2;
                if (bw != null) {
                    bw.close();
                }
                if (outFile != null) {
                    outFile.close();
                }
                throw th;
            }
            try {
                bw2.append(hex.toString());
                if (bw2 != null) {
                    try {
                        bw2.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                        bw = bw2;
                        outFile = outFile2;
                        return;
                    } catch (Exception e422) {
                        e422.printStackTrace();
                        bw = bw2;
                        outFile = outFile2;
                        return;
                    }
                }
                if (outFile2 != null) {
                    outFile2.close();
                }
                bw = bw2;
                outFile = outFile2;
            } catch (Exception e5) {
                e422 = e5;
                bw = bw2;
                outFile = outFile2;
                e422.printStackTrace();
                if (bw != null) {
                    bw.close();
                }
                if (outFile == null) {
                    outFile.close();
                }
            } catch (Throwable th4) {
                th = th4;
                bw = bw2;
                outFile = outFile2;
                if (bw != null) {
                    bw.close();
                }
                if (outFile != null) {
                    outFile.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e422 = e6;
            e422.printStackTrace();
            if (bw != null) {
                bw.close();
            }
            if (outFile == null) {
                outFile.close();
            }
        }
    }
}
