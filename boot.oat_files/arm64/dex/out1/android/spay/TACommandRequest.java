package android.spay;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TACommandRequest implements Parcelable {
    public static final Creator<TACommandRequest> CREATOR = new Creator<TACommandRequest>() {
        public TACommandRequest createFromParcel(Parcel in) {
            return new TACommandRequest(in);
        }

        public TACommandRequest[] newArray(int size) {
            return new TACommandRequest[size];
        }
    };
    private static boolean DEBUG = true;
    public static final int HEADER_SIZE = 100;
    public static final int MAX_BUFFER_SIZE = 5242880;
    public static final int MAX_DATA_TRANSACTION_SIZE = 3072;
    public static final int PAYLOAD_SIZE = 2972;
    private static final String TAG = "TACommandRequest";
    public int mCommandId;
    public int mLength;
    public byte[] mMagicNum;
    public int mOffset;
    public byte[] mRequest;
    public int mVersion;

    public TACommandRequest() {
        this.mVersion = -1;
        this.mMagicNum = null;
        this.mLength = 0;
        this.mOffset = 0;
        this.mCommandId = -1;
        this.mRequest = null;
    }

    public void init(int version, byte[] magic, int cmdId, byte[] request) {
        this.mVersion = version;
        this.mMagicNum = magic;
        this.mCommandId = cmdId;
        this.mRequest = request;
        if (request != null) {
            this.mLength = request.length;
        } else {
            this.mLength = 0;
        }
        this.mOffset = 0;
    }

    private TACommandRequest(Parcel in) {
        this.mVersion = -1;
        this.mMagicNum = null;
        this.mLength = 0;
        this.mOffset = 0;
        this.mCommandId = -1;
        this.mRequest = null;
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(this.mVersion);
        out.writeInt(this.mMagicNum.length);
        out.writeByteArray(this.mMagicNum);
        out.writeInt(this.mCommandId);
        out.writeInt(this.mLength);
        out.writeInt(this.mOffset);
        if (this.mRequest == null || this.mRequest.length == 0) {
            out.writeInt(0);
            return;
        }
        out.writeInt(this.mRequest.length);
        out.writeByteArray(this.mRequest);
    }

    public void readFromParcel(Parcel in) {
        this.mVersion = in.readInt();
        int len = in.readInt();
        if (len > 0) {
            this.mMagicNum = new byte[len];
            in.readByteArray(this.mMagicNum);
        }
        this.mCommandId = in.readInt();
        this.mLength = in.readInt();
        this.mOffset = in.readInt();
        len = in.readInt();
        if (len > 0) {
            this.mRequest = new byte[len];
            in.readByteArray(this.mRequest);
        }
    }

    public int describeContents() {
        return 0;
    }

    private TACommandRequest(int _version, byte[] _magicNum, int _commandId, int _length, int _offset, byte[] _requestPayload) {
        this.mVersion = -1;
        this.mMagicNum = null;
        this.mLength = 0;
        this.mOffset = 0;
        this.mCommandId = -1;
        this.mRequest = null;
        this.mVersion = _version;
        this.mMagicNum = _magicNum;
        this.mCommandId = _commandId;
        this.mLength = _length;
        this.mOffset = _offset;
        this.mRequest = _requestPayload;
    }

    public int getTotalLength() {
        return this.mLength;
    }

    public int getChunkOffset() {
        return this.mOffset;
    }

    public byte[] getPayload() {
        return this.mRequest;
    }

    public List<TACommandRequest> disassemble() {
        List<TACommandRequest> arr = new ArrayList();
        if (this.mRequest == null) {
            return null;
        }
        if (this.mRequest.length <= PAYLOAD_SIZE) {
            arr.add(this);
            if (!DEBUG) {
                return arr;
            }
            Log.i(TAG, "no need to divide the mRequest, len=" + this.mRequest.length);
            return arr;
        }
        if (DEBUG) {
            Log.i(TAG, "dividing the mRequest, len=" + this.mRequest.length);
        }
        int offset = 0;
        while (offset + PAYLOAD_SIZE < this.mLength) {
            if (DEBUG) {
                Log.i(TAG, "generating the chunk from " + offset + " to " + ((offset + PAYLOAD_SIZE) - 1));
            }
            arr.add(new TACommandRequest(this.mVersion, this.mMagicNum, this.mCommandId, this.mLength, offset, Arrays.copyOfRange(this.mRequest, offset, offset + PAYLOAD_SIZE)));
            offset += PAYLOAD_SIZE;
        }
        arr.add(new TACommandRequest(this.mVersion, this.mMagicNum, this.mCommandId, this.mLength, offset, Arrays.copyOfRange(this.mRequest, offset, this.mLength)));
        if (!DEBUG) {
            return arr;
        }
        Log.i(TAG, "generating the chunk from " + offset + " to " + (this.mLength - 1));
        return arr;
    }

    public void dump() {
        Exception e;
        Throwable th;
        Log.d(TAG, "Command ID = " + this.mCommandId);
        Log.d(TAG, "Length = " + this.mRequest.length);
        StringBuilder hex = new StringBuilder((this.mRequest.length * 2) + 100);
        hex.append("{");
        for (int i = 0; i < this.mRequest.length; i++) {
            hex.append(String.format("0x%02X", new Object[]{Byte.valueOf(this.mRequest[i])}));
            if (i != this.mRequest.length) {
                hex.append(", ");
            }
        }
        hex.append("}");
        Log.d(TAG, hex.toString());
        FileWriter outFile = null;
        BufferedWriter bw = null;
        try {
            FileWriter outFile2 = new FileWriter("/mnt/sdcard/sendbuf.txt", false);
            try {
                BufferedWriter bw2 = new BufferedWriter(outFile2);
                try {
                    bw2.append(hex.toString());
                    if (bw2 != null) {
                        try {
                            bw2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            bw = bw2;
                            outFile = outFile2;
                            return;
                        } catch (Exception e3) {
                            e3.printStackTrace();
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
                } catch (Exception e4) {
                    e3 = e4;
                    bw = bw2;
                    outFile = outFile2;
                    try {
                        e3.printStackTrace();
                        if (bw != null) {
                            try {
                                bw.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                                return;
                            } catch (Exception e32) {
                                e32.printStackTrace();
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
                            } catch (IOException e222) {
                                e222.printStackTrace();
                                throw th;
                            } catch (Exception e322) {
                                e322.printStackTrace();
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
            } catch (Exception e5) {
                e322 = e5;
                outFile = outFile2;
                e322.printStackTrace();
                if (bw != null) {
                    bw.close();
                }
                if (outFile == null) {
                    outFile.close();
                }
            } catch (Throwable th4) {
                th = th4;
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
            e322 = e6;
            e322.printStackTrace();
            if (bw != null) {
                bw.close();
            }
            if (outFile == null) {
                outFile.close();
            }
        }
    }
}
