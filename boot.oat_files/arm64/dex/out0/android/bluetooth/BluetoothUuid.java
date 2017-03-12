package android.bluetooth;

import android.os.ParcelUuid;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public final class BluetoothUuid {
    public static final ParcelUuid AdvAudioDist = ParcelUuid.fromString("0000110D-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid AudioSink = ParcelUuid.fromString("0000110B-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid AudioSource = ParcelUuid.fromString("0000110A-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid AvrcpController = ParcelUuid.fromString("0000110E-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid AvrcpTarget = ParcelUuid.fromString("0000110C-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid BASE_UUID = ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid BIP = ParcelUuid.fromString("0000111A-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid BNEP = ParcelUuid.fromString("0000000f-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid BasicPrinting = ParcelUuid.fromString("00001122-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid CTP = ParcelUuid.fromString("00001109-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid DUN = ParcelUuid.fromString("00001103-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid FAX = ParcelUuid.fromString("00001111-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid FileTransfer = ParcelUuid.fromString("00001106-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid GN = ParcelUuid.fromString("00001117-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid GenericAudio = ParcelUuid.fromString("00001203-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid HSP = ParcelUuid.fromString("00001108-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid HSP_AG = ParcelUuid.fromString("00001112-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid Handsfree = ParcelUuid.fromString("0000111E-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid Handsfree_AG = ParcelUuid.fromString("0000111F-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid Hid = ParcelUuid.fromString("00001124-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid Hogp = ParcelUuid.fromString("00001812-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid ICP = ParcelUuid.fromString("00001110-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid IRMC_SYNC_CMD = ParcelUuid.fromString("00001107-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid LAP = ParcelUuid.fromString("00001102-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid LeAudio = ParcelUuid.fromString("665f0700-9b20-11e4-a15d-080027dcf8e8");
    public static final ParcelUuid MAP = ParcelUuid.fromString("00001134-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid MAS = ParcelUuid.fromString("00001132-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid MNS = ParcelUuid.fromString("00001133-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid NAP = ParcelUuid.fromString("00001116-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid ObexObjectPush = ParcelUuid.fromString("00001105-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid PANU = ParcelUuid.fromString("00001115-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid PBAP_PCE = ParcelUuid.fromString("0000112e-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid PBAP_PSE = ParcelUuid.fromString("0000112f-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid PhoneBookAccess = ParcelUuid.fromString("00001130-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid[] RESERVED_UUIDS = new ParcelUuid[]{AudioSink, AudioSource, AdvAudioDist, HSP, Handsfree, AvrcpController, AvrcpTarget, ObexObjectPush, PANU, NAP, MAP, MNS, MAS, SAP, Hid, FileTransfer, PhoneBookAccess, BasicPrinting, SerialPort, DUN};
    public static final ParcelUuid SAP = ParcelUuid.fromString("0000112D-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid SYNC_ = ParcelUuid.fromString("00001104-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid SerialPort = ParcelUuid.fromString("00001101-0000-1000-8000-00805f9b34fb");
    public static final int UUID_BYTES_128_BIT = 16;
    public static final int UUID_BYTES_16_BIT = 2;
    public static final int UUID_BYTES_32_BIT = 4;
    public static final ParcelUuid VIDEO_DIST = ParcelUuid.fromString("00001305-0000-1000-8000-00805F9B34FB");

    public static boolean isAudioSource(ParcelUuid uuid) {
        return AudioSource.equals(uuid);
    }

    public static boolean isAudioSink(ParcelUuid uuid) {
        return AudioSink.equals(uuid);
    }

    public static boolean isAdvAudioDist(ParcelUuid uuid) {
        return AdvAudioDist.equals(uuid);
    }

    public static boolean isHandsfree(ParcelUuid uuid) {
        return Handsfree.equals(uuid);
    }

    public static boolean isHeadset(ParcelUuid uuid) {
        return HSP.equals(uuid);
    }

    public static boolean isAvrcpController(ParcelUuid uuid) {
        return AvrcpController.equals(uuid);
    }

    public static boolean isAvrcpTarget(ParcelUuid uuid) {
        return AvrcpTarget.equals(uuid);
    }

    public static boolean isInputDevice(ParcelUuid uuid) {
        return Hid.equals(uuid);
    }

    public static boolean isPanu(ParcelUuid uuid) {
        return PANU.equals(uuid);
    }

    public static boolean isNap(ParcelUuid uuid) {
        return NAP.equals(uuid);
    }

    public static boolean isBnep(ParcelUuid uuid) {
        return BNEP.equals(uuid);
    }

    public static boolean isMap(ParcelUuid uuid) {
        return MAP.equals(uuid);
    }

    public static boolean isMns(ParcelUuid uuid) {
        return MNS.equals(uuid);
    }

    public static boolean isMas(ParcelUuid uuid) {
        return MAS.equals(uuid);
    }

    public static boolean isSap(ParcelUuid uuid) {
        return SAP.equals(uuid);
    }

    public static boolean isPbap(ParcelUuid uuid) {
        return PhoneBookAccess.equals(uuid);
    }

    public static boolean isOpp(ParcelUuid uuid) {
        return ObexObjectPush.equals(uuid);
    }

    public static boolean isFtp(ParcelUuid uuid) {
        return FileTransfer.equals(uuid);
    }

    public static boolean isDun(ParcelUuid uuid) {
        return DUN.equals(uuid);
    }

    public static boolean isSerialPort(ParcelUuid uuid) {
        return SerialPort.equals(uuid);
    }

    public static boolean isUuidPresent(ParcelUuid[] uuidArray, ParcelUuid uuid) {
        if ((uuidArray == null || uuidArray.length == 0) && uuid == null) {
            return true;
        }
        if (uuidArray == null) {
            return false;
        }
        for (ParcelUuid element : uuidArray) {
            if (element.equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAnyUuid(ParcelUuid[] uuidA, ParcelUuid[] uuidB) {
        if (uuidA == null && uuidB == null) {
            return true;
        }
        if (uuidA == null) {
            if (uuidB.length != 0) {
                return false;
            }
            return true;
        } else if (uuidB != null) {
            HashSet<ParcelUuid> uuidSet = new HashSet(Arrays.asList(uuidA));
            for (ParcelUuid uuid : uuidB) {
                if (uuidSet.contains(uuid)) {
                    return true;
                }
            }
            return false;
        } else if (uuidA.length != 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean containsAllUuids(ParcelUuid[] uuidA, ParcelUuid[] uuidB) {
        if (uuidA == null && uuidB == null) {
            return true;
        }
        if (uuidA == null) {
            if (uuidB.length != 0) {
                return false;
            }
            return true;
        } else if (uuidB == null) {
            return true;
        } else {
            HashSet<ParcelUuid> uuidSet = new HashSet(Arrays.asList(uuidA));
            for (ParcelUuid uuid : uuidB) {
                if (!uuidSet.contains(uuid)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static int getServiceIdentifierFromParcelUuid(ParcelUuid parcelUuid) {
        return (int) ((parcelUuid.getUuid().getMostSignificantBits() & 281470681743360L) >>> 32);
    }

    public static ParcelUuid parseUuidFrom(byte[] uuidBytes) {
        if (uuidBytes == null) {
            throw new IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = uuidBytes.length;
        if (length != 2 && length != 4 && length != 16) {
            throw new IllegalArgumentException("uuidBytes length invalid - " + length);
        } else if (length == 16) {
            ByteBuffer buf = ByteBuffer.wrap(uuidBytes).order(ByteOrder.LITTLE_ENDIAN);
            return new ParcelUuid(new UUID(buf.getLong(8), buf.getLong(0)));
        } else {
            long shortUuid;
            if (length == 2) {
                shortUuid = ((long) (uuidBytes[0] & 255)) + ((long) ((uuidBytes[1] & 255) << 8));
            } else {
                shortUuid = ((((long) (uuidBytes[0] & 255)) + ((long) ((uuidBytes[1] & 255) << 8))) + ((long) ((uuidBytes[2] & 255) << 16))) + ((long) ((uuidBytes[3] & 255) << 24));
            }
            return new ParcelUuid(new UUID(BASE_UUID.getUuid().getMostSignificantBits() + (shortUuid << 32), BASE_UUID.getUuid().getLeastSignificantBits()));
        }
    }

    public static boolean is16BitUuid(ParcelUuid parcelUuid) {
        UUID uuid = parcelUuid.getUuid();
        if (uuid.getLeastSignificantBits() == BASE_UUID.getUuid().getLeastSignificantBits() && (uuid.getMostSignificantBits() & -281470681743361L) == 4096) {
            return true;
        }
        return false;
    }

    public static boolean is32BitUuid(ParcelUuid parcelUuid) {
        UUID uuid = parcelUuid.getUuid();
        if (uuid.getLeastSignificantBits() == BASE_UUID.getUuid().getLeastSignificantBits() && !is16BitUuid(parcelUuid) && (uuid.getMostSignificantBits() & 4294967295L) == 4096) {
            return true;
        }
        return false;
    }
}
