package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import java.util.List;

public class zzb {
    private static int zzF(Parcel parcel, int i) {
        parcel.writeInt(SupportMenu.CATEGORY_MASK | i);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    private static void zzG(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        int i2 = dataPosition - i;
        parcel.setDataPosition(i - 4);
        parcel.writeInt(i2);
        parcel.setDataPosition(dataPosition);
    }

    public static void zzH(Parcel parcel, int i) {
        zzG(parcel, i);
    }

    public static int zzK(Parcel parcel) {
        return zzF(parcel, 20293);
    }

    public static void zza(Parcel parcel, int i, byte b) {
        zzb(parcel, i, 4);
        parcel.writeInt(b);
    }

    public static void zza(Parcel parcel, int i, double d) {
        zzb(parcel, i, 8);
        parcel.writeDouble(d);
    }

    public static void zza(Parcel parcel, int i, float f) {
        zzb(parcel, i, 4);
        parcel.writeFloat(f);
    }

    public static void zza(Parcel parcel, int i, long j) {
        zzb(parcel, i, 8);
        parcel.writeLong(j);
    }

    public static void zza(Parcel parcel, int i, Bundle bundle, boolean z) {
        if (bundle != null) {
            int zzF = zzF(parcel, i);
            parcel.writeBundle(bundle);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, IBinder iBinder, boolean z) {
        if (iBinder != null) {
            int zzF = zzF(parcel, i);
            parcel.writeStrongBinder(iBinder);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, Parcel parcel2, boolean z) {
        if (parcel2 != null) {
            int zzF = zzF(parcel, i);
            parcel.appendFrom(parcel2, 0, parcel2.dataSize());
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, Parcelable parcelable, int i2, boolean z) {
        if (parcelable != null) {
            int zzF = zzF(parcel, i);
            parcelable.writeToParcel(parcel, i2);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, Boolean bool, boolean z) {
        int i2 = 0;
        if (bool != null) {
            zzb(parcel, i, 4);
            if (bool.booleanValue()) {
                i2 = 1;
            }
            parcel.writeInt(i2);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, Integer num, boolean z) {
        if (num != null) {
            zzb(parcel, i, 4);
            parcel.writeInt(num.intValue());
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, Long l, boolean z) {
        if (l != null) {
            zzb(parcel, i, 8);
            parcel.writeLong(l.longValue());
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, String str, boolean z) {
        if (str != null) {
            int zzF = zzF(parcel, i);
            parcel.writeString(str);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, List<Integer> list, boolean z) {
        if (list != null) {
            int zzF = zzF(parcel, i);
            int size = list.size();
            parcel.writeInt(size);
            for (int i2 = 0; i2 < size; i2++) {
                parcel.writeInt(((Integer) list.get(i2)).intValue());
            }
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, short s) {
        zzb(parcel, i, 4);
        parcel.writeInt(s);
    }

    public static void zza(Parcel parcel, int i, boolean z) {
        zzb(parcel, i, 4);
        parcel.writeInt(z ? 1 : 0);
    }

    public static void zza(Parcel parcel, int i, byte[] bArr, boolean z) {
        if (bArr != null) {
            int zzF = zzF(parcel, i);
            parcel.writeByteArray(bArr);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, int[] iArr, boolean z) {
        if (iArr != null) {
            int zzF = zzF(parcel, i);
            parcel.writeIntArray(iArr);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static <T extends Parcelable> void zza(Parcel parcel, int i, T[] tArr, int i2, boolean z) {
        if (tArr != null) {
            int zzF = zzF(parcel, i);
            parcel.writeInt(r3);
            for (Parcelable parcelable : tArr) {
                if (parcelable == null) {
                    parcel.writeInt(0);
                } else {
                    zza(parcel, parcelable, i2);
                }
            }
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, String[] strArr, boolean z) {
        if (strArr != null) {
            int zzF = zzF(parcel, i);
            parcel.writeStringArray(strArr);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zza(Parcel parcel, int i, byte[][] bArr, boolean z) {
        int i2 = 0;
        if (bArr != null) {
            int zzF = zzF(parcel, i);
            int length = bArr.length;
            parcel.writeInt(length);
            while (i2 < length) {
                parcel.writeByteArray(bArr[i2]);
                i2++;
            }
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    private static <T extends Parcelable> void zza(Parcel parcel, T t, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(1);
        int dataPosition2 = parcel.dataPosition();
        t.writeToParcel(parcel, i);
        int dataPosition3 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition3 - dataPosition2);
        parcel.setDataPosition(dataPosition3);
    }

    private static void zzb(Parcel parcel, int i, int i2) {
        if (i2 >= HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
            parcel.writeInt(SupportMenu.CATEGORY_MASK | i);
            parcel.writeInt(i2);
            return;
        }
        parcel.writeInt((i2 << 16) | i);
    }

    public static void zzb(Parcel parcel, int i, List<String> list, boolean z) {
        if (list != null) {
            int zzF = zzF(parcel, i);
            parcel.writeStringList(list);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zzc(Parcel parcel, int i, int i2) {
        zzb(parcel, i, 4);
        parcel.writeInt(i2);
    }

    public static <T extends Parcelable> void zzc(Parcel parcel, int i, List<T> list, boolean z) {
        if (list != null) {
            int zzF = zzF(parcel, i);
            int size = list.size();
            parcel.writeInt(size);
            for (int i2 = 0; i2 < size; i2++) {
                Parcelable parcelable = (Parcelable) list.get(i2);
                if (parcelable == null) {
                    parcel.writeInt(0);
                } else {
                    zza(parcel, parcelable, 0);
                }
            }
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }

    public static void zzd(Parcel parcel, int i, List list, boolean z) {
        if (list != null) {
            int zzF = zzF(parcel, i);
            parcel.writeList(list);
            zzG(parcel, zzF);
        } else if (z) {
            zzb(parcel, i, 0);
        }
    }
}
