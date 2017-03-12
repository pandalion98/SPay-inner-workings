package android.bluetooth.le;

import android.bluetooth.BluetoothUuid;
import android.os.ParcelUuid;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ScanRecord {
    private static final int DATA_TYPE_FLAGS = 1;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    private static final int DATA_TYPE_SERVICE_DATA = 22;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    private static final String TAG = "ScanRecord";
    private final int mAdvertiseFlags;
    private final byte[] mBytes;
    private final String mDeviceName;
    private ArrayList<byte[]> mManuDatalist;
    private final SparseArray<byte[]> mManufacturerSpecificData;
    private final Map<ParcelUuid, byte[]> mServiceData;
    private final List<ParcelUuid> mServiceUuids;
    private final int mTxPowerLevel;

    public int getAdvertiseFlags() {
        return this.mAdvertiseFlags;
    }

    public List<ParcelUuid> getServiceUuids() {
        return this.mServiceUuids;
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.mManufacturerSpecificData;
    }

    public byte[] getManufacturerSpecificData(int manufacturerId) {
        return (byte[]) this.mManufacturerSpecificData.get(manufacturerId);
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.mServiceData;
    }

    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        if (serviceDataUuid == null) {
            return null;
        }
        return (byte[]) this.mServiceData.get(serviceDataUuid);
    }

    public int getTxPowerLevel() {
        return this.mTxPowerLevel;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    private ScanRecord(List<ParcelUuid> serviceUuids, SparseArray<byte[]> manufacturerData, Map<ParcelUuid, byte[]> serviceData, int advertiseFlags, int txPowerLevel, String localName, byte[] bytes, ArrayList<byte[]> manuDatalist) {
        this.mServiceUuids = serviceUuids;
        this.mManufacturerSpecificData = manufacturerData;
        this.mServiceData = serviceData;
        this.mDeviceName = localName;
        this.mAdvertiseFlags = advertiseFlags;
        this.mTxPowerLevel = txPowerLevel;
        this.mBytes = bytes;
        this.mManuDatalist = manuDatalist;
    }

    public static ScanRecord parseFromBytes(byte[] scanRecord) {
        if (scanRecord == null) {
            return null;
        }
        int currentPos;
        Log.d(TAG, "parseFromBytes");
        int advertiseFlag = -1;
        List<ParcelUuid> serviceUuids = new ArrayList();
        String localName = null;
        int txPowerLevel = Integer.MIN_VALUE;
        ArrayList<byte[]> manuDatalist = new ArrayList();
        SparseArray<byte[]> manufacturerData = new SparseArray();
        Map<ParcelUuid, byte[]> serviceData = new ArrayMap();
        int currentPos2 = 0;
        while (currentPos2 < scanRecord.length) {
            try {
                currentPos = currentPos2 + 1;
                try {
                    int length = scanRecord[currentPos2] & 255;
                    if (length == 0) {
                        if (serviceUuids.isEmpty()) {
                            serviceUuids = null;
                        }
                        return new ScanRecord(serviceUuids, manufacturerData, serviceData, advertiseFlag, txPowerLevel, localName, scanRecord, manuDatalist);
                    }
                    int dataLength = length - 1;
                    currentPos2 = currentPos + 1;
                    switch (scanRecord[currentPos] & 255) {
                        case 1:
                            advertiseFlag = scanRecord[currentPos2] & 255;
                            break;
                        case 2:
                        case 3:
                            parseServiceUuid(scanRecord, currentPos2, dataLength, 2, serviceUuids);
                            break;
                        case 4:
                        case 5:
                            parseServiceUuid(scanRecord, currentPos2, dataLength, 4, serviceUuids);
                            break;
                        case 6:
                        case 7:
                            parseServiceUuid(scanRecord, currentPos2, dataLength, 16, serviceUuids);
                            break;
                        case 8:
                        case 9:
                            localName = new String(extractBytes(scanRecord, currentPos2, dataLength));
                            break;
                        case 10:
                            txPowerLevel = scanRecord[currentPos2];
                            break;
                        case 22:
                            serviceData.put(BluetoothUuid.parseUuidFrom(extractBytes(scanRecord, currentPos2, 2)), extractBytes(scanRecord, currentPos2 + 2, dataLength - 2));
                            break;
                        case 255:
                            int manufacturerId = ((scanRecord[currentPos2 + 1] & 255) << 8) + (scanRecord[currentPos2] & 255);
                            Object manufacturerDataBytes = extractBytes(scanRecord, currentPos2 + 2, dataLength - 2);
                            Object previousManuDataBytes = (byte[]) manufacturerData.get(manufacturerId);
                            if (previousManuDataBytes == null) {
                                Log.d(TAG, "first manudata for manu ID");
                                manufacturerData.put(manufacturerId, manufacturerDataBytes);
                                break;
                            }
                            Log.d(TAG, "same manuId data exists, combine both data and store");
                            manuDatalist.add(previousManuDataBytes);
                            manuDatalist.add(manufacturerDataBytes);
                            Object fullManufacturerDataBytes = new byte[(manufacturerDataBytes.length + previousManuDataBytes.length)];
                            System.arraycopy(previousManuDataBytes, 0, fullManufacturerDataBytes, 0, previousManuDataBytes.length);
                            System.arraycopy(manufacturerDataBytes, 0, fullManufacturerDataBytes, previousManuDataBytes.length, manufacturerDataBytes.length);
                            manufacturerData.put(manufacturerId, fullManufacturerDataBytes);
                            break;
                        default:
                            break;
                    }
                    currentPos2 += dataLength;
                } catch (Exception e) {
                }
            } catch (Exception e2) {
                currentPos = currentPos2;
            }
        }
        currentPos = currentPos2;
        if (serviceUuids.isEmpty()) {
            serviceUuids = null;
        }
        return new ScanRecord(serviceUuids, manufacturerData, serviceData, advertiseFlag, txPowerLevel, localName, scanRecord, manuDatalist);
        Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
        return new ScanRecord(null, null, null, -1, Integer.MIN_VALUE, null, scanRecord, null);
    }

    public String toString() {
        return "ScanRecord [mAdvertiseFlags=" + this.mAdvertiseFlags + ", mServiceUuids=" + this.mServiceUuids + ", mManufacturerSpecificData=" + BluetoothLeUtils.toString(this.mManufacturerSpecificData) + ", mServiceData=" + BluetoothLeUtils.toString(this.mServiceData) + ", mTxPowerLevel=" + this.mTxPowerLevel + ", mDeviceName=" + this.mDeviceName + "]";
    }

    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            serviceUuids.add(BluetoothUuid.parseUuidFrom(extractBytes(scanRecord, currentPos, uuidLength)));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }

    public boolean isMultiManudata() {
        if (this.mManuDatalist.size() > 0) {
            Log.d(TAG, "It's a Multi Manu data");
            return true;
        }
        Log.d(TAG, "Not a Multi Manu data");
        return false;
    }

    public ArrayList<byte[]> getManudataList() {
        return this.mManuDatalist;
    }
}
