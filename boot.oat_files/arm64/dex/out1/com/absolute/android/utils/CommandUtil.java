package com.absolute.android.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandUtil {
    private static final String a = "APS";
    private static final boolean b = false;

    public static String executeCommand(String str, Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(str).getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return stringBuffer.toString();
                }
                stringBuffer.append(readLine);
                stringBuffer.append("\n");
            }
        } catch (Throwable th) {
            RuntimeException runtimeException = new RuntimeException(("Execution of command '" + str + "' failed. Got exception: ") + th.getMessage());
        }
    }
}
