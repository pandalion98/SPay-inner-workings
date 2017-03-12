package org.bouncycastle.util;

import java.util.Collection;

public interface StreamParser {
    Object read();

    Collection readAll();
}
