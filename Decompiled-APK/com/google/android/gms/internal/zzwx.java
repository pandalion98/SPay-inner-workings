package com.google.android.gms.internal;

import java.io.IOException;

public class zzwx extends IOException {
    public zzwx(String str) {
        super(str);
    }

    static zzwx zzvQ() {
        return new zzwx("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either than the input has been truncated or that an embedded message misreported its own length.");
    }

    static zzwx zzvR() {
        return new zzwx("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    static zzwx zzvS() {
        return new zzwx("CodedInputStream encountered a malformed varint.");
    }

    static zzwx zzvT() {
        return new zzwx("Protocol message contained an invalid tag (zero).");
    }

    static zzwx zzvU() {
        return new zzwx("Protocol message end-group tag did not match expected tag.");
    }

    static zzwx zzvV() {
        return new zzwx("Protocol message tag had invalid wire type.");
    }

    static zzwx zzvW() {
        return new zzwx("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }
}
