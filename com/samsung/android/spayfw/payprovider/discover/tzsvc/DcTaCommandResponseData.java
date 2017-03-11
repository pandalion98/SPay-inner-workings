package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct.UTF8String;
import javolution.io.Struct.Unsigned32;
import org.bouncycastle.crypto.macs.SkeinMac;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.tzsvc.d */
public class DcTaCommandResponseData extends TAStruct {
    Unsigned32 return_code;
    UTF8String yY;

    public DcTaCommandResponseData() {
        this.return_code = new Unsigned32();
        this.yY = new UTF8String(SkeinMac.SKEIN_1024);
    }

    public String eA() {
        return this.yY.get();
    }

    public long getReturnCode() {
        return this.return_code.get();
    }

    public boolean validate() {
        return this.return_code.get() == 0;
    }
}
