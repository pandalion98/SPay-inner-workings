package android.os.storage.sensitive;

public class SecureStorageLSM {
    private static SecureStorageLSM lsmss = new SecureStorageLSM();

    public static class SecureStorageLSMException extends Exception {
        public SecureStorageLSMException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static native boolean lsmCheckIfDefaultPasswordIsSet(byte[] bArr) throws SecureStorageLSMException;

    public static native byte[] lsmDKEKReEncrypt(byte[] bArr);

    public static native byte[] lsmDecrypt(byte[] bArr);

    public static native byte[] lsmEncrypt(byte[] bArr, byte[] bArr2, int i);

    public static native byte[] lsmGenKeypair(byte[] bArr);

    public static native int lsmInitialize();

    public static native byte[] lsmKeysReEncrypt(byte[] bArr);

    public static native int lsmSetState(byte[] bArr, byte[] bArr2, int i);

    private SecureStorageLSM() {
    }

    public static SecureStorageLSM getInstance() {
        return lsmss;
    }

    public byte[] encrypt(byte[] dataBlock, byte[] password, int flag) {
        return lsmEncrypt(dataBlock, password, flag);
    }

    public byte[] decrypt(byte[] dataBlock) {
        return lsmDecrypt(dataBlock);
    }

    public byte[] gen_keypair(byte[] dataBlock) {
        return lsmGenKeypair(dataBlock);
    }

    public byte[] keys_re_encrypt(byte[] dataBlock) {
        return lsmKeysReEncrypt(dataBlock);
    }

    public byte[] dkek_re_encrypt(byte[] dataBlock) {
        return lsmDKEKReEncrypt(dataBlock);
    }

    public int set_state(byte[] password, byte[] dkek, int flag) {
        return lsmSetState(password, dkek, flag);
    }

    public int initialize() {
        return lsmInitialize();
    }

    public boolean check_if_default_password_is_set(byte[] dkek) throws SecureStorageLSMException {
        return lsmCheckIfDefaultPasswordIsSet(dkek);
    }
}
