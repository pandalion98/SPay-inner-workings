package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

public final class ApduCommand {
    private String apduCommand;
    private String messageId;

    public static final class ApduParser {
        public static byte[] generateApduForTa(ApduCommand[] apduCommandArr) {
            if (apduCommandArr == null || apduCommandArr.length < 1) {
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (ApduCommand apduCommand : apduCommandArr) {
                if (apduCommand != null) {
                    stringBuilder.append(apduCommand.getApduCommand());
                }
            }
            return convertStringToByteArray(stringBuilder.toString());
        }

        public static byte[] convertStringToByteArray(String str) {
            int length = str.length();
            byte[] bArr = new byte[(length / 2)];
            for (int i = 0; i < length; i += 2) {
                bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
            }
            return bArr;
        }
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String str) {
        this.messageId = str;
    }

    public String getApduCommand() {
        return this.apduCommand;
    }

    public void setApduCommand(String str) {
        this.apduCommand = str;
    }
}
