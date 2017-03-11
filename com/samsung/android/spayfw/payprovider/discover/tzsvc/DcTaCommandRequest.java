package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import android.spay.TACommandRequest;

public abstract class DcTaCommandRequest extends TACommandRequest {
    abstract boolean validate();
}
