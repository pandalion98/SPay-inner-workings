package org.bouncycastle.util.io.pem;

public interface PemObjectParser {
    Object parseObject(PemObject pemObject);
}
