package com.synaptics.bpd.fingerprint;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class VcsTUIDs {
    public List<byte[]> tuidList = new ArrayList();

    public boolean setTUID(byte[] tuid) {
        this.tuidList.add(tuid);
        return true;
    }

    public List<byte[]> getTUIDs() {
        return this.tuidList;
    }

    public static int getTUID(byte[] tuid) {
        int retTUID;
        if (tuid == null || tuid.length == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(tuid);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        if (byteBuffer.remaining() >= 4) {
            retTUID = byteBuffer.getInt();
        } else {
            retTUID = 0;
        }
        return retTUID;
    }

    public byte[] getTUID(int index) {
        if (this.tuidList == null || this.tuidList.size() == 0 || index < 0 || index >= this.tuidList.size()) {
            return null;
        }
        return (byte[]) this.tuidList.get(index);
    }

    public int getSize() {
        if (this.tuidList == null) {
            return 0;
        }
        return this.tuidList.size();
    }
}
