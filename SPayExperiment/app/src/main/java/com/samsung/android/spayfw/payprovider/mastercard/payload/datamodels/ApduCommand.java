/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Character
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

public final class ApduCommand {
    private String apduCommand;
    private String messageId;

    public String getApduCommand() {
        return this.apduCommand;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setApduCommand(String string) {
        this.apduCommand = string;
    }

    public void setMessageId(String string) {
        this.messageId = string;
    }

    public static final class ApduParser {
        public static byte[] convertStringToByteArray(String string) {
            int n2 = string.length();
            byte[] arrby = new byte[n2 / 2];
            for (int i2 = 0; i2 < n2; i2 += 2) {
                arrby[i2 / 2] = (byte)((Character.digit((char)string.charAt(i2), (int)16) << 4) + Character.digit((char)string.charAt(i2 + 1), (int)16));
            }
            return arrby;
        }

        /*
         * Enabled aggressive block sorting
         */
        public static byte[] generateApduForTa(ApduCommand[] arrapduCommand) {
            if (arrapduCommand == null || arrapduCommand.length < 1) {
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            int n2 = arrapduCommand.length;
            int n3 = 0;
            while (n3 < n2) {
                ApduCommand apduCommand = arrapduCommand[n3];
                if (apduCommand != null) {
                    stringBuilder.append(apduCommand.getApduCommand());
                }
                ++n3;
            }
            return ApduParser.convertStringToByteArray(stringBuilder.toString());
        }
    }

}

